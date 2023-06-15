package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Shift;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShiftMapper extends BaseMapper<Shift> {
    Page<Shift> findPageShift(Page<Shift> page, @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                              @Param("shiftName") String shiftName, @Param("departmentName") String departmentName);
}
