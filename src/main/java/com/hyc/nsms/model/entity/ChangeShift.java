package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("change_shift")
public class ChangeShift extends BaseEntity {
    private Integer userId;//请求换班员工id

    private Integer shiftId;//请求换班员工排班班次id

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date scheduleDate;//请求换班员工排班日期

    private Integer changedUserId;//被换班员工id

    private Integer changedShiftId;//被换班员工排班班次id

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date changedScheduleDate;//被换班员工排班日期

    private String reason;//换班原因

    private Boolean status;//换班状态（0：不同意换班 1：被换班用户同意换班 2：护士长也同意）
}
