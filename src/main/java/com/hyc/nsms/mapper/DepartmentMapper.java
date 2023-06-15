package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    Page<Department> findPageDepartment(Page<Department> page,
                                        @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                                        @Param("departmentName") String departmentName);
}
