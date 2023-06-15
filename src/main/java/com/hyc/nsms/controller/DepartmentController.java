package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Department;
import com.hyc.nsms.model.result.Result;
import com.hyc.nsms.model.vo.queryVo.DepartmentQueryVo;
import com.hyc.nsms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //条件查询带分页
    @PostMapping("findPageDepartment/{current}/{limit}")
    public Result findPageDepartment(@PathVariable Integer current,
                                     @PathVariable Integer limit,
                                     @RequestParam(required = false) Integer hospitalId,
                                     @RequestParam(required = false) Integer departmentId,
                                     @RequestBody(required = false) DepartmentQueryVo departmentQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<Department> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询科室信息
        Page<Department> departmentPage = departmentService.findPageDepartment(page, hospitalId, departmentId, departmentQueryVo);

        //返回结果
        return Result.ok(departmentPage);
    }

    //根据id获取科室
    @GetMapping("getDepartmentById/{id}")
    public Result getDepartmentById(@PathVariable Integer id) {
        Department department = departmentService.getById(id);
        return Result.ok(department);
    }

    //根据hospitalId获取科室
    @GetMapping("getDepartmentByHospitalId")
    public Result getDepartmentByHospitalId(@RequestParam(required = false) Integer hospitalId) {
        List<Department> list = null;
        if (hospitalId != null) {
            list = departmentService.getDepartmentByHospitalId(hospitalId);
        } else {
            list = departmentService.list();
        }
        return Result.ok(list);
    }

    //添加科室
    @PostMapping("saveDepartment")
    public Result saveDepartment(@RequestBody Department department) {
        boolean save = departmentService.save(department);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改科室信息
    @PostMapping("updateDepartment")
    public Result updateDepartment(@RequestBody Department department) {
        boolean flag = departmentService.updateById(department);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除科室
    @DeleteMapping("{id}")
    public Result removeDepartment(@PathVariable Integer id) {
        boolean flag = departmentService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除科室
    @DeleteMapping("batchRemove")
    public Result batchRemoveDepartment(@RequestBody List<Integer> idList) {
        departmentService.removeByIds(idList);
        return Result.ok();
    }
}
