package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.nsms.mapper.DepartmentMapper;
import com.hyc.nsms.model.entity.Department;
import com.hyc.nsms.model.vo.queryVo.DepartmentQueryVo;
import com.hyc.nsms.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;

    //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询科室信息
    @Override
    public Page<Department> findPageDepartment(Page<Department> page, Integer hospitalId, Integer departmentId, DepartmentQueryVo departmentQueryVo) {
        String departmentName = departmentQueryVo.getDepartmentName();
        Page<Department> departmentPage = departmentMapper.findPageDepartment(page, hospitalId, departmentId, departmentName);
        return departmentPage;
    }

    //根据hospitalId获取科室
    @Override
    public List<Department> getDepartmentByHospitalId(Integer hospitalId) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (hospitalId != null) {
            queryWrapper.eq("hospital_id", hospitalId);
        }
        List<Department> list = departmentMapper.selectList(queryWrapper);
        return list;
    }
}
