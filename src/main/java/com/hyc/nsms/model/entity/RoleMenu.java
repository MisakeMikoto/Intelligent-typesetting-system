package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@Data
@TableName("role_menu")
public class RoleMenu implements Serializable {
    private Integer roleId;//角色id

    private Integer menuId;//菜单id
}
