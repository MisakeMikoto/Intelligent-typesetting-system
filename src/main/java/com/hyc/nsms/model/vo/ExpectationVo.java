package com.hyc.nsms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ExpectationVo{
    private Integer userId;//员工id

    private Integer shiftSign;//班次标识

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;//期望开始日期
}
