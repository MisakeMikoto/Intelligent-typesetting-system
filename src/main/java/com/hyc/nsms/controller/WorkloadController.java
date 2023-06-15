package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.result.Result;

import java.util.List;

import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.queryVo.WorkloadQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.service.WorkloadService;
import com.hyc.nsms.model.entity.Workload;


import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workload")
public class WorkloadController {
    @Autowired
    private WorkloadService workloadService;

    //条件查询带分页
    @PostMapping("findPageWorkload/{current}/{limit}")
    public Result findPageWorkload(@PathVariable Integer current,
                                   @PathVariable Integer limit,
                                   @RequestParam Integer level,
                                   @RequestParam(required = false) Integer hospitalId,
                                   @RequestParam(required = false) Integer departmentId,
                                   @RequestBody(required = false) WorkloadQueryVo workloadQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<Workload> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询工作量信息
        Page<Workload> workloadPage = workloadService.findPageWorkload(page, level, hospitalId, departmentId, workloadQueryVo);

        //返回结果
        return Result.ok(workloadPage);
    }

    //根据id获取Workload
    @GetMapping("getWorkloadById/{id}")
    public Result getWorkloadById(@PathVariable Integer id) {
        Workload workload = workloadService.getById(id);
        return Result.ok(workload);
    }

    //根据userId获取Workload
    @GetMapping("getWorkloadByUserId/{userId}")
    public Result getWorkloadByUserId(@PathVariable Integer userId) {
        QueryWrapper<Workload> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Workload workload = workloadService.getOne(queryWrapper);
        return Result.ok(workload);
    }

    //添加Workload
    @PostMapping("saveWorkload")
    public Result saveWorkload(@RequestBody Workload workload) {
        boolean save = workloadService.save(workload);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Workload信息
    @PostMapping("updateWorkload")
    public Result updateWorkload(@RequestBody Workload workload) {
        boolean flag = workloadService.updateById(workload);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Workload
    @DeleteMapping("{id}")
    public Result removeWorkload(@PathVariable Integer id) {
        boolean flag = workloadService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Workload
    @DeleteMapping("batchRemove")
    public Result batchRemoveWorkload(@RequestBody List<Integer> idList) {
        workloadService.removeByIds(idList);
        return Result.ok();
    }

    //通过userId删除Workload
    @DeleteMapping("/userId/{userId}")
    public Result removeWorkloadByUserId(@PathVariable Integer userId) {
        QueryWrapper<Workload> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        boolean flag = workloadService.remove(queryWrapper);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail(ResultCodeEnum.WORKLOAD_ERROR);
        }
    }

    //通过userId批量删除Workload
    @DeleteMapping("/userId/batchRemove")
    public Result batchRemoveWorkloadByUserId(@RequestBody List<Integer> idList) {
        QueryWrapper<Workload> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", idList);
        boolean flag = workloadService.remove(queryWrapper);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
}

