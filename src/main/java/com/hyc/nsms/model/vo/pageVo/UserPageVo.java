package com.hyc.nsms.model.vo.pageVo;

import lombok.Data;

@Data
public class UserPageVo extends BasePageVo{
    private String username;//用户名

    private String password;//密码

    private String name;//姓名

    private String sex;//性别

    private String phone;//手机号

    private Integer roleId;//角色Id

    private String roleName;//角色名称

    private Integer level;//等级
}
