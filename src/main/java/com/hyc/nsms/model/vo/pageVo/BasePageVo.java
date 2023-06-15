package com.hyc.nsms.model.vo.pageVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BasePageVo {
    private Integer id;//编号

    private Integer hospitalId;//医院编号

    private String hospitalName;//医院名称

    private Integer departmentId;//科室编号

    private String departmentName;//科室名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//更新时间
}
