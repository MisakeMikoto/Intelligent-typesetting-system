package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.vo.pageVo.InformationPageVo;
import com.hyc.nsms.model.vo.pageVo.UserPageVo;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.model.vo.UserAccountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserAccountVo userAccountVo);

    Page<User> findPageUser(Page<User> page, @Param("level") Integer level,
                            @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                            @Param("departmentName") String departmentName, @Param("name") String name);

    InformationPageVo getInformationById(@Param("id") Integer id);

    List<UserPageVo> getUserByHospitalIdOrDepartmentId(@Param("level") Integer level, @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId);
}

