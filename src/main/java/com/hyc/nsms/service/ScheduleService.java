package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.vo.StaffTimeVo;
import com.hyc.nsms.model.vo.queryVo.ScheduleQueryVo;
import com.hyc.nsms.model.vo.ScheduleVo;

import java.util.List;

public interface ScheduleService extends IService<Schedule> {
    //自动排班
    List<Schedule> autoSchedule(Integer departmentId);

    //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询排班信息
    Page<Schedule> findPageSchedule(Page<Schedule> page, Integer hospitalId, Integer departmentId, ScheduleQueryVo scheduleQueryVo);

    //根据用户id和工作日期获取用户、排班和班次信息
    List<ScheduleVo> getScheduleByUserIdAndDate(Integer userId, String startDate, String endDate);

    //根据科室id和工作日期获取用户、排班和班次信息
    List<StaffTimeVo> getScheduleByDepartmentIdAndDate(Integer departmentId, String startDate, String endDate);
}
