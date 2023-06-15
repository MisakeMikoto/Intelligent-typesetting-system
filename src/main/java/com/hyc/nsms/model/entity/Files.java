package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("files")
public class Files extends BaseEntity {
    private String filesName;//文件名称

    private String type;//文件类型

    private Double size;//文件大小

    private String url;//下载链接

    private String md5;//文件MD5

    private Boolean status;//链接是否可用（1：是 0：否）
}
