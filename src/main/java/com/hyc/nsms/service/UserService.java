package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.vo.pageVo.InformationPageVo;
import com.hyc.nsms.model.vo.pageVo.UserPageVo;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.model.vo.UserAccountVo;
import com.hyc.nsms.model.vo.queryVo.UserQueryVo;
import com.hyc.nsms.model.vo.UserStorageVo;

import java.util.List;

public interface UserService extends IService<User> {
    //登录
    UserStorageVo login(UserAccountVo userAccountVo);

    //注册
    User register(UserAccountVo userAccountVo);

    //修改密码
    boolean updatePassword(UserAccountVo userAccountVo);

    //通过传递当前页、每页记录数、用户权限等级、查询条件分页查询用户信息
    Page<User> findPageUser(Page<User> page, Integer level, Integer hospitalId, Integer departmentId, UserQueryVo userQueryVo);

    //根据id获取用户详细信息
    InformationPageVo getInformationById(Integer id);

    //根据hospitalId或者departmentId获取用户信息
    List<UserPageVo> getUserByHospitalIdOrDepartmentId(Integer level, Integer hospitalId, Integer departmentId);
}
