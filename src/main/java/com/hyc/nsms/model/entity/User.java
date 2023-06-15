package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("user")
public class User extends BaseEntity {
    private String name;//姓名

    private String username;//账号

    private String password;//密码

    private String sex;//性别

    private Integer roleId;//角色Id

    private String phone;//手机号

    private Integer hospitalId;//医院id

    private Integer departmentId;//科室id

    private String avatarUrl;//头像地址
}
