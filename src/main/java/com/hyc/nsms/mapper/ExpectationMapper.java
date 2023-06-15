package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Expectation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpectationMapper extends BaseMapper<Expectation> {
    Page<Expectation> findPageExpectation(Page<Expectation> page, @Param("level") Integer level,
                                          @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                                          @Param("name") String name);
}
