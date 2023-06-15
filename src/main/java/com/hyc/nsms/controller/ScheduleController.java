package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.result.Result;

import java.util.List;

import com.hyc.nsms.model.vo.StaffTimeVo;
import com.hyc.nsms.model.vo.queryVo.ScheduleQueryVo;
import com.hyc.nsms.model.vo.ScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.service.ScheduleService;
import com.hyc.nsms.model.entity.Schedule;


import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //自动排班
    @PostMapping("autoSchedule/{departmentId}")
    public Result autoSchedule(@PathVariable Integer departmentId) {
        List<Schedule> scheduleList = scheduleService.autoSchedule(departmentId);
        return Result.ok(scheduleList);
    }

    //条件查询带分页
    @PostMapping("findPageSchedule/{current}/{limit}")
    public Result findPageSchedule(@PathVariable Integer current,
                                   @PathVariable Integer limit,
                                   @RequestParam(required = false) Integer hospitalId,
                                   @RequestParam(required = false) Integer departmentId,
                                   @RequestBody(required = false) ScheduleQueryVo scheduleQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<Schedule> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询排班信息
        Page<Schedule> schedulePage = scheduleService.findPageSchedule(page, hospitalId, departmentId, scheduleQueryVo);

        //返回结果
        return Result.ok(schedulePage);
    }

    //根据用户id和工作日期获取用户、排班和班次信息
    @GetMapping("getScheduleByUserIdAndDate/{userId}/{startDate}/{endDate}")
    public Result getScheduleByUserIdAndDate(@PathVariable Integer userId, @PathVariable String startDate, @PathVariable String endDate) {
        List<ScheduleVo> scheduleVoList = scheduleService.getScheduleByUserIdAndDate(userId, startDate, endDate);
        return Result.ok(scheduleVoList);
    }

    //根据科室id和工作日期获取用户、排班和班次信息
    @GetMapping("getScheduleByDepartmentIdAndDate/{departmentId}/{startDate}/{endDate}")
    public Result getScheduleByDepartmentIdAndDate(@PathVariable Integer departmentId, @PathVariable String startDate, @PathVariable String endDate) {
        List<StaffTimeVo> scheduleVoList = scheduleService.getScheduleByDepartmentIdAndDate(departmentId, startDate, endDate);
        return Result.ok(scheduleVoList);
    }

    //添加Schedule
    @PostMapping("saveSchedule")
    public Result saveSchedule(@RequestBody Schedule schedule) {
        boolean save = scheduleService.save(schedule);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Schedule信息
    @PutMapping("updateSchedule/{userId}/{scheduleDate}")
    public Result updateSchedule(@RequestBody Schedule schedule, @PathVariable Integer userId, @PathVariable String scheduleDate) {
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("schedule_date", scheduleDate);
        boolean flag = scheduleService.update(schedule, queryWrapper);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Schedule
    @DeleteMapping("removeSchedule")
    public Result removeSchedule(@RequestBody Schedule schedule) {
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("schedule_date", schedule.getScheduleDate());
        queryWrapper.eq("user_id", schedule.getUserId());
        boolean flag = scheduleService.remove(queryWrapper);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }

    //批量删除Schedule
    @DeleteMapping("batchRemove")
    public Result batchRemoveSchedule(@RequestBody List<Schedule> schedules) {
        QueryWrapper<Schedule> queryWrapper;
        for (Schedule schedule : schedules) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("schedule_date", schedule.getScheduleDate());
            queryWrapper.eq("user_id", schedule.getUserId());
            queryWrapper.eq("shift_id", schedule.getShiftId());
            scheduleService.remove(queryWrapper);
        }
        return Result.ok();
    }

    //删除缓存
    private void flushRedis(String key) {
        stringRedisTemplate.delete(key);
    }
}

