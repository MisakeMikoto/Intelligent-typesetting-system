package com.hyc.nsms.model.vo.pageVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SchedulePageVo {
    private Integer hospitalId;//医院编号

    private String hospitalName;//医院名称

    private Integer departmentId;//科室编号

    private String departmentName;//科室名称

    private Integer userId;//用户id

    private String username;//用户名

    private String name;//姓名

    private Integer shiftId;//班次id

    private String shiftName;//班次名称

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date scheduleDate;//排班日期
}
