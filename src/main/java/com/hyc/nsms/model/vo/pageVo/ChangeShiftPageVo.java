package com.hyc.nsms.model.vo.pageVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChangeShiftPageVo extends BasePageVo {
    private Integer userId;//请求换班员工id

    private String username;//请求换班员工用户名

    private String name;//请求换班员工姓名

    private Integer shiftId;//请求换班员工排班班次id

    private String shiftName;//请求换班员工排班班次名称

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date scheduleDate;//请求换班员工排班日期

    private Integer changedUserId;//被换班员工id

    private String changedUsername;//被换班员工用户名

    private String changedName;//被换班员工名称

    private Integer changedShiftId;//被换班员工排班班次id

    private String changedShiftName;//被换班员工排班班次id

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date changedScheduleDate;//被换班员工排班日期

    private String reason;//换班原因

    private Boolean status;//换班状态（0：不同意换班 1：被换班用户同意换班 2：护士长也同意）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//更新时间
}
