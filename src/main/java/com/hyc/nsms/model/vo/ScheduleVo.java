package com.hyc.nsms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ScheduleVo {
    private Integer hospitalId;//医院id

    private Integer departmentId;//科室id

    private Integer userId;//用户id

    private String name;//姓名

    private String roleName;//角色名称

    private Integer shiftId;//班次id

    private Integer shiftSign;//班次标识

    private String shiftName;//班次名称

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date scheduleDate;//排班日期

    private Boolean leaveStatus;//请假状态（1：请假 0：未请假）
}
