package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.entity.Department;
import com.hyc.nsms.model.vo.queryVo.DepartmentQueryVo;

import java.util.List;

public interface DepartmentService extends IService<Department> {
    //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询科室信息
    Page<Department> findPageDepartment(Page<Department> page, Integer hospitalId, Integer departmentId, DepartmentQueryVo departmentQueryVo);

    //根据hospitalId获取科室
    List<Department> getDepartmentByHospitalId(Integer hospitalId);
}
