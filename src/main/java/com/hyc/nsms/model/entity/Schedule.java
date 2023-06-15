package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("schedule")
public class Schedule implements Serializable {
    private Integer userId;//用户id

    private Integer shiftId;//班次id

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date scheduleDate;//排班日期

    private Boolean leaveStatus;//请假状态（1：请假 0：未请假）

    @TableField(exist = false)
    private Integer roleId;//角色id
}
