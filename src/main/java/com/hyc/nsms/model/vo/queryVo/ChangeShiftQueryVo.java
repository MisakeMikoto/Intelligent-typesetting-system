package com.hyc.nsms.model.vo.queryVo;

import lombok.Data;

@Data
public class ChangeShiftQueryVo {
    private String name;//请求换班员工姓名

    private String changedName;//被换班员工名称
}
