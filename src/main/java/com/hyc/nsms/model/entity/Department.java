package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("department")
public class Department extends BaseEntity{
    private Integer hospitalId;//医院编号

    private String departmentName;//科室名称
}
