package com.hyc.nsms.model.vo;

import com.hyc.nsms.model.entity.Schedule;
import lombok.Data;

import java.util.List;

@Data
public class UserWorkloadVo {
    private Integer id;//用户id

    private String name;//用户姓名

    private Integer realWorkDays;//实际上班天数

    private Integer workDays;//排班之后的上班天数

    private Integer realEveningShifts;//实际夜班次数

    private Integer eveningShifts;//排班之后的大夜班次数

    private Integer leftDays;//放假天数

    private List<Schedule> schedule;//Schedule类
}
