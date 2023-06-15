package com.hyc.nsms.model.vo.pageVo;

import lombok.Data;

@Data
public class InformationPageVo extends BasePageVo{
    private String roleName;//角色名称

    private String username;//用户名

    private String name;//姓名

    private String sex;//性别

    private String phone;//手机号

    private String avatarUrl;//头像地址
}
