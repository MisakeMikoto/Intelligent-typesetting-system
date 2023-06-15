package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("hospital")
public class Hospital extends BaseEntity {
    private String hospitalName;//医院名称

    private String province;//省

    private String address;//详细地址

    private String hospitalLogo;//医院logo

    private String intro;//医院简介
}
