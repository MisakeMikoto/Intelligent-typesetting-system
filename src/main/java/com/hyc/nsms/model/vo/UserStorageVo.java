package com.hyc.nsms.model.vo;

import com.hyc.nsms.model.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class UserStorageVo {
    private Integer id;//用户id

    private String name;//姓名

    private String username;//账号

    private Integer level;//权限等级

    private Integer hospitalId;//医院id

    private Integer departmentId;//科室id

    private String avatarUrl;//头像地址

    private String token;//token

    private List<Menu> menus;//对应菜单
}
