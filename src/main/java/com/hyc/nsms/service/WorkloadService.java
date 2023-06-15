package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Workload;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.vo.queryVo.WorkloadQueryVo;

import java.util.List;

public interface WorkloadService extends IService<Workload> {
    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询工作量信息
    Page<Workload> findPageWorkload(Page<Workload> page, Integer level, Integer hospitalId, Integer departmentId, WorkloadQueryVo workloadQueryVo);

    //通过科室id和角色id获得人员数量集合
    Integer getCountByDepartmentIdAndRoleId(Integer departmentId, Integer roleId);

    //通过科室id和角色id获得人员工作量集合
    List<Workload> getWorkloadByDepartmentIdAndRoleId(Integer departmentId, Integer roleId);

    //将预计排班数设置为实际排班数
    void updateNextMonth();
}
