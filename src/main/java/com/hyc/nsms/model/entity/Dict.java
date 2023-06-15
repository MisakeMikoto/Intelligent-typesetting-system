package com.hyc.nsms.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict")
public class Dict implements Serializable {
    private String dictName;//名称

    private String value;//内容

    private String type;//类型
}
