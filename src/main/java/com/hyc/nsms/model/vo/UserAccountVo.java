package com.hyc.nsms.model.vo;

import lombok.Data;

@Data
public class UserAccountVo {
    private String username;//账号

    private String password;//密码

    private String newPassword;//新密码
}
