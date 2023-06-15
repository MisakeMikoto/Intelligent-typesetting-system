package com.hyc.nsms.model.result;

import lombok.Getter;

//统一返回结果状态信息类
@Getter
public enum ResultCodeEnum {
    HEAD_NURSE(3, "护士长"),
    NURSE(4, "护士"),
    ASSISTANT(5, "助手"),
    DAY_SHIFT(1, "白班"),
    MIDDLE_SHIFT(2, "小夜班"),
    NIGHT_SHIFT(3, "大夜班"),
    MORNING_SHIFT(4, "早班"),
    EVENING_SHIFT(5, "晚班"),

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    PARAM_ERROR(202, "参数不正确"),
    USER_ALREADY_EXISTS(203, "用户已存在"),
    LOGIN_ERROR(204, "账号或密码错误"),
    PASSWORD_ERROR(205, "密码错误"),
    SERVICE_ERROR(206, "服务异常"),

    SCHEDULE_ERROR(207, "排班系统错误"),
    SHIFT_ERROR(208, "未设置班次"),
    NURSE_NUMBER_ERROR(209, "排班所需要的护士人数不足"),
    CARER_NUMBER_ERROR(210, "排班所需要的助手人数不足"),
    MATCH_STAFF_ERROR(211, "找不到合适的顶班人员"),
    SCHEDULE_STAFF_ERROR(212, "排班结果不完整"),
    DATE_ERROR(213, "开始时间不能大于结束时间"),
    LEAVE_DATE_ERROR(214, "请假时间应该在今天之后"),
    EXPECTATION_DATE_ERROR(214, "期望时间应该在今天之后"),
    CHANGE_DATE_ERROR(214, "换班时间应该在今天之后"),
    EXPORT_ERROR(215, "导出失败，没有排班数据"),
    LEAVE_ERROR(216, "没有排班数据"),
    WORKLOAD_ERROR(217, "没有工作量数据"),

    TOKEN_ERROR(301, "无token，请重新登录"),
    TOKEN_CHECK_ERROR(302, "token验证失败，请重新登录"),
    USER_NOT_EXISTS(303, "用户不存在，请重新登录"),
    ;

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
