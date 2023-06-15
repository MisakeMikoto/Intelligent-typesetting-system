package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.result.Result;

import java.io.IOException;
import java.util.List;

import com.hyc.nsms.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.model.entity.Files;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/files")
public class FilesController {
    @Autowired
    private FilesService filesService;

    //获取上传文件
    @PostMapping("upload")
    public Result upload(@RequestParam MultipartFile file) {
        String url = filesService.upload(file);
        return Result.ok(url);
    }

    //下载文件
    @GetMapping("/{filesUUID}")
    public void download(@PathVariable String filesUUID, HttpServletResponse response) {
        filesService.download(filesUUID, response);
    }

    //导出排班表
    @GetMapping("exportSchedule/{departmentId}/{month}")
    public void exportSchedule(HttpServletResponse response, @PathVariable Integer departmentId, @PathVariable Integer month) throws IOException {
        filesService.exportSchedule(response, departmentId, month);
    }

    //条件查询带分页
    @PostMapping("findPageFiles/{current}/{limit}")
    public Result findPageFiles(@PathVariable Integer current, @PathVariable Integer limit, @RequestBody(required = false) Files files) {
        //创建page对象，传递当前页，每页记录数
        Page<Files> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<Files> wrapper = new QueryWrapper<>();

        String filesName = files.getFilesName();//Files名称

        if (!StringUtils.isEmpty(filesName)) {
            wrapper.like("files_name", filesName);
        }

        //调用方法实现分页查询
        Page<Files> FilesPage = filesService.page(page, wrapper);
        //返回结果
        return Result.ok(FilesPage);
    }

    //修改Files
    @PostMapping("updateFiles")
    public Result updateFiles(@RequestBody Files files) {
        boolean flag = filesService.updateById(files);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Files
    @DeleteMapping("{id}")
    public Result removeFiles(@PathVariable Integer id) {
        boolean flag = filesService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Files
    @DeleteMapping("batchRemove")
    public Result batchRemoveFiles(@RequestBody List<Integer> idList) {
        filesService.removeByIds(idList);
        return Result.ok();
    }
}

