package com.hyc.nsms.model.vo;

import lombok.Data;

@Data
public class StaffTimeVo {
    private String name;//用户名称

    private String roleName;//角色名称

    private String date;//排班日期

    private int shiftSign;//班次标识

    private String shiftName;//班次名称
}
