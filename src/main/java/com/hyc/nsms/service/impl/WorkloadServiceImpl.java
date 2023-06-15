package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Workload;
import com.hyc.nsms.mapper.WorkloadMapper;
import com.hyc.nsms.model.vo.queryVo.WorkloadQueryVo;
import com.hyc.nsms.service.WorkloadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorkloadServiceImpl extends ServiceImpl<WorkloadMapper, Workload> implements WorkloadService {
    @Resource
    private WorkloadMapper workloadMapper;

    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询工作量信息
    @Override
    public Page<Workload> findPageWorkload(Page<Workload> page, Integer level, Integer hospitalId, Integer departmentId, WorkloadQueryVo workloadQueryVo) {
        String name = workloadQueryVo.getName();
        //创建page对象，传递当前页，每页记录数
        Page<Workload> workloadPage = workloadMapper.findPageWorkload(page, level, hospitalId, departmentId, name);
        return workloadPage;
    }

    //通过科室id和角色id获得人员数量集合
    @Override
    public Integer getCountByDepartmentIdAndRoleId(Integer departmentId, Integer roleId) {
        Integer count = workloadMapper.getCountByDepartmentIdAndRoleId(departmentId, roleId);
        return count;
    }

    //根据科室id和角色id获得人员工作量集合
    @Override
    public List<Workload> getWorkloadByDepartmentIdAndRoleId(Integer departmentId, Integer roleId) {
        List<Workload> list = workloadMapper.getWorkloadByDepartmentIdAndRoleId(departmentId, roleId);
        return list;
    }

    //将预计排班数设置为实际排班数
    @Override
    public void updateNextMonth() {
        workloadMapper.updateNextMonth();
    }
}
