package com.hyc.nsms.model.vo.pageVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ExpectationPageVo extends BasePageVo {
    private Integer userId;//员工id

    private String name;//姓名

    private String username;//用户名

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;//期望开始日期

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;//期望结束日期

    private Integer expectedDays;//期望天数

    private Integer shiftSign;//班次标识

    private String reason; //期望原因

    private Boolean status;//期望状态（1：同意 0：拒绝）

    private Boolean complete;//完成期望状态（1：成功 0：失败）
}
