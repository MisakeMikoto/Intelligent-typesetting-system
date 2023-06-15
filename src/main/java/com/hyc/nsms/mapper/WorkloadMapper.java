package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Workload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WorkloadMapper extends BaseMapper<Workload> {
    Page<Workload> findPageWorkload(Page<Workload> page, @Param("level") Integer level,
                                    @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                                    @Param("name") String name);

    Integer getCountByDepartmentIdAndRoleId(@Param("departmentId") Integer departmentId, @Param("roleId") Integer roleId);

    List<Workload> getWorkloadByDepartmentIdAndRoleId(@Param("departmentId") Integer departmentId, @Param("roleId") Integer roleId);

    @Update("update workload set work_days = 0, morning_shifts = 0, middle_shifts = 0, evening_shifts = 0")
    void updateNextMonth();
}
