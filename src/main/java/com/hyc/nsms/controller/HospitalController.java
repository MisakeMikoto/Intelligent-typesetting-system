package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Hospital;
import com.hyc.nsms.model.result.Result;
import com.hyc.nsms.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //查询Hospital表所有信息
    @GetMapping("findAllHospital")
    public Result findAllHospital() {
        List<Hospital> list = hospitalService.list();
        return Result.ok(list);
     }

    //条件查询带分页
    @PostMapping("findPageHospital/{current}/{limit}")
    public Result findPageHospital(@PathVariable Integer current, @PathVariable Integer limit, @RequestBody(required = false) Hospital hospital) {
        //创建page对象，传递当前页，每页记录数
        Page<Hospital> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<Hospital> wrapper = new QueryWrapper<>();

        String hospitalName = hospital.getHospitalName();//Hospital名称

        if (!StringUtils.isEmpty(hospitalName)) {
            wrapper.like("hospital_name", hospitalName);
        }

        //调用方法实现分页查询
        Page<Hospital> HospitalPage = hospitalService.page(page, wrapper);
        //返回结果
        return Result.ok(HospitalPage);
     }

    //根据id获取Hospital
    @GetMapping("getHospitalById/{id}")
    public Result getHospitalById(@PathVariable Integer id) {
        Hospital hospital = hospitalService.getById(id);
        return Result.ok(hospital);
    }

    //添加Hospital
    @PostMapping("saveHospital")
    public Result saveHospital(@RequestBody Hospital hospital) {
        boolean save = hospitalService.save(hospital);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Hospital信息
    @PostMapping("updateHospital")
    public Result updateHospital(@RequestBody Hospital hospital) {
        boolean flag = hospitalService.updateById(hospital);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Hospital
    @DeleteMapping("{id}")
    public Result removeHospital(@PathVariable Integer id) {
        boolean flag = hospitalService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Hospital
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospital(@RequestBody List<Integer> idList) {
        hospitalService.removeByIds(idList);
        return Result.ok();
    }
}

