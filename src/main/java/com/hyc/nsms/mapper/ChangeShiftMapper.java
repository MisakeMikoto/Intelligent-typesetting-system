package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.ChangeShift;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChangeShiftMapper extends BaseMapper<ChangeShift> {
    Page<ChangeShift> findPageChangeShift(Page<ChangeShift> page, @Param("level") Integer level,
                                          @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                                          @Param("name") String name, @Param("changedName") String changedName);
}

