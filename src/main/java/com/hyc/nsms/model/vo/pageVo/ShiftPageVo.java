package com.hyc.nsms.model.vo.pageVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShiftPageVo extends BasePageVo{
    private String shiftName;//班次名称

    private Integer shiftSign;//班次标识（对应三班制：1.白班 2.小夜班 3.大夜班）（对应两班制：4.早班 5.晚班 ）

    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date startTime;//班次开始时间

    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date endTime;//班次结束时间

    private Double duration;//班次时长（小时）

    private Integer nurseNumber;//需要护士人数

    private Integer carerNumber;//需要护工人数
}
