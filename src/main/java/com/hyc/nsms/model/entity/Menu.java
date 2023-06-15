package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("menu")
public class Menu extends BaseEntity {
    private String menuName;//名称

    private String description;//描述

    private String path;//路径

    private String pagePath;//页面路径

    private String icon;//图标

    private Integer pid;//父级id

    @TableField(exist = false)
    private List<Menu> children;//树形数据
}
