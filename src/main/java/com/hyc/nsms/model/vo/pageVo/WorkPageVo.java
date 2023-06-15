package com.hyc.nsms.model.vo.pageVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class WorkPageVo {
    private Integer id;//id

    private Integer userId;//用户id

    private String username;//用户名

    private String name;//姓名

    private Integer realWorkDays;//实际上班天数

    private Integer workDays;//排班之后的上班天数

    private Integer realMorningShifts;//实际白班次数

    private Integer morningShifts;//下个月预计白班次数

    private Integer realMiddleShifts;//实际小夜班次数

    private Integer middleShifts;//下个月预计小夜班次数

    private Integer realEveningShifts;//实际大夜班次数

    private Integer eveningShifts;//下个月预计大夜班次数

    private Integer leftDays;//放假天数

    private Integer annualLeaveDays;//年休天数

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间
}
