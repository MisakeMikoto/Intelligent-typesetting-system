package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role extends BaseEntity {
    private String roleName;//名称

    private Integer level;//等级

    private String description;//描述
}
