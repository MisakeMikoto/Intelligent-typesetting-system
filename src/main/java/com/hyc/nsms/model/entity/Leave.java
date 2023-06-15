package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ask_for_leave")
public class Leave extends BaseEntity {
    private Integer userId;//员工id

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date startDate;//请假开始日期

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date endDate;//请假结束日期

    private Integer leaveDays;//请假天数

    private String reason;//请假原因

    private Boolean status;//批假状态（0：拒绝 1：同意）

    private Integer replaceUserId;//顶班员工id
}
