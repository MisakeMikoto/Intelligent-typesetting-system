package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Leave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.nsms.model.vo.UserWorkloadVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {
    Page<Leave> findPageLeave(Page<Leave> page, @Param("level") Integer level,
                              @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                              @Param("name") String name);

    List<UserWorkloadVo> getMatchStaff(@Param("departmentId") Integer departmentId, @Param("roleId") Integer roleId, @Param("workUserIdList") List<Integer> workUserIdList);
}
