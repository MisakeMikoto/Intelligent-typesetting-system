package com.hyc.nsms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("workload")
public class Workload extends BaseEntity {
    private Integer userId;//用户id

    private Integer realWorkDays;//实际上班天数

    private Integer workDays;//下个月预计上班天数

    private Integer realMorningShifts;//实际白班次数

    private Integer morningShifts;//下个月预计白班次数

    private Integer realMiddleShifts;//实际小夜班次数

    private Integer middleShifts;//下个月预计小夜班次数

    private Integer realEveningShifts;//实际大夜班次数

    private Integer eveningShifts;//下个月预计大夜班次数

    private Integer leftDays;//放假天数

    private Integer annualLeaveDays;//年休天数

    @TableField(exist = false)
    private Integer roleId;//角色id

    @TableField(exist = false)
    private Integer weekWorkDays;//一周上班天数

    @TableField(exist = false)
    private Integer weekEveningShifts;//一周晚班天数

    @TableField(exist = false)
    private Integer currentWorkDays;//用于排序的上班天数

    @TableField(exist = false)
    private Integer currentMorningShifts;//用于排序的白班天数

    @TableField(exist = false)
    private Integer currentMiddleShifts;//用于排序的小夜班天数

    @TableField(exist = false)
    private Integer currentEveningShifts;//用于排序的大夜班天数
}
