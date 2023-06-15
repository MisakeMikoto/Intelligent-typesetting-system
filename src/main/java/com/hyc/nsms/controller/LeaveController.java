package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.exception.MyException;
import com.hyc.nsms.model.entity.Schedule;
import com.hyc.nsms.model.entity.Workload;
import com.hyc.nsms.model.result.Result;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.LeaveVo;
import com.hyc.nsms.model.vo.UserWorkloadVo;
import com.hyc.nsms.model.vo.queryVo.LeaveQueryVo;
import com.hyc.nsms.service.ScheduleService;
import com.hyc.nsms.service.WorkloadService;
import com.hyc.nsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.service.LeaveService;
import com.hyc.nsms.model.entity.Leave;


import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private WorkloadService workloadService;

    //得到合适的顶班人员
    @PostMapping("getMatchStaff/{userId}/{scheduleDate}")
    public Result getMatchStaff(@PathVariable Integer userId, @PathVariable String scheduleDate) throws ParseException {
        List<UserWorkloadVo> matchStaff = leaveService.getMatchStaff(userId, scheduleDate);
        return Result.ok(matchStaff);
    }

    //顶班
    @PostMapping("replaceSchedule/{id}/{userId}/{replaceUserId}/{scheduleDate}")
    public Result replaceSchedule(@PathVariable Integer id, @PathVariable Integer userId, @PathVariable Integer replaceUserId, @PathVariable String scheduleDate) {
        //通过userId和scheduleDate查询schedule
        QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.eq("user_id", userId);
        scheduleQueryWrapper.eq("schedule_date", scheduleDate);
        Schedule schedule = scheduleService.getOne(scheduleQueryWrapper);
        schedule.setUserId(replaceUserId);//设置顶班人员
        schedule.setLeaveStatus(false);//设置为未请假
        //保存到数据库
        boolean save = scheduleService.save(schedule);

        //通过id查询leave
        QueryWrapper<Leave> leaveQueryWrapper = new QueryWrapper<>();
        leaveQueryWrapper.eq("id", id);
        Leave leave = leaveService.getOne(leaveQueryWrapper);
        leave.setReplaceUserId(replaceUserId);//设置顶班人员
        //保存到数据库
        boolean update = leaveService.updateById(leave);

        if (save && update) {
            return Result.ok();
        }
        return Result.ok();
    }

    //条件查询带分页
    @PostMapping("findPageLeave/{current}/{limit}")
    public Result findPageLeave(@PathVariable Integer current,
                                @PathVariable Integer limit,
                                @RequestParam Integer level,
                                @RequestParam(required = false) Integer hospitalId,
                                @RequestParam(required = false) Integer departmentId,
                                @RequestBody(required = false) LeaveQueryVo leaveQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<Leave> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询请假信息
        Page<Leave> leavePage = leaveService.findPageLeave(page, level, hospitalId, departmentId, leaveQueryVo);

        //返回结果
        return Result.ok(leavePage);
    }

    //根据id获取Leave
    @GetMapping("getLeaveById/{id}")
    public Result getLeaveById(@PathVariable Integer id) {
        Leave leave = leaveService.getById(id);
        return Result.ok(leave);
    }

    //添加Leave
    @PostMapping("saveLeave")
    public Result saveLeave(@RequestBody Leave leave) {
        Date startDate = leave.getStartDate();
        Date endDate = leave.getEndDate();
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            if (!DateUtils.ifStartDateLessThanEndDate(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.DATE_ERROR);
            }
            if (!DateUtils.ifAfterToday(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.LEAVE_DATE_ERROR);
            }

            Integer duration = DateUtils.getDateDifference(startDate, endDate);
            leave.setLeaveDays(duration);//计算时间差
        }
        boolean save = leaveService.save(leave);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Leave信息
    @PostMapping("updateLeave")
    public Result updateLeave(@RequestBody Leave leave) {
        Date startDate = leave.getStartDate();
        Date endDate = leave.getEndDate();
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            if (!DateUtils.ifStartDateLessThanEndDate(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.DATE_ERROR);
            }
            if (!DateUtils.ifAfterToday(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.LEAVE_DATE_ERROR);
            }

            Integer duration = DateUtils.getDateDifference(startDate, endDate);
            leave.setLeaveDays(duration);//计算时间差
        }
        //更新请假表
        boolean flag = false;
        boolean updateSchedule = false;
        boolean updateWorkload = false;

        Calendar calendar = Calendar.getInstance();//定义Calendar类
        Integer leaveDays = leave.getLeaveDays();//获得请假天数
        Integer userId = leave.getUserId();//获得用户id
        for (int i = 0; i < leaveDays; i++) {//从开始时间一直叠加天数
            calendar.setTime(leave.getStartDate());//设置开始时间
            calendar.add(Calendar.DATE, i);//在该时间叠加天数
            //查询排班
            QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
            scheduleQueryWrapper.eq("user_id", userId);
            scheduleQueryWrapper.eq("schedule_date", calendar.getTime());
            Schedule schedule = scheduleService.getOne(scheduleQueryWrapper);

            //查询工作量
            QueryWrapper<Workload> workloadQueryWrapper = new QueryWrapper<>();
            workloadQueryWrapper.eq("user_id", userId);
            Workload workload = workloadService.getOne(workloadQueryWrapper);

            //如果没有排班数据，不能批假
            if (schedule == null) {
                throw new MyException(ResultCodeEnum.LEAVE_ERROR);
            }
            //当批假成功时，排班表的请假状态由0转化成1
            if (leave.getStatus() == true) {
                //设置请假状态
                schedule.setLeaveStatus(true);
                //请假天数-1
                workload.setLeftDays(workload.getLeftDays() + 1);
            } else {
                //删除顶班人员
                leave.setReplaceUserId(0);
                //设置请假状态
                schedule.setLeaveStatus(false);
                //请假天数-1
                workload.setLeftDays(workload.getLeftDays() - 1);
            }
            //更新数据库
            flag = leaveService.updateById(leave);
            updateSchedule = scheduleService.update(schedule, scheduleQueryWrapper);
            updateWorkload = workloadService.update(workload, workloadQueryWrapper);
        }

        if (flag && updateSchedule && updateWorkload) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Leave
    @DeleteMapping("{id}")
    public Result removeLeave(@PathVariable Integer id) {
        boolean flag = leaveService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Leave
    @DeleteMapping("batchRemove")
    public Result batchRemoveLeave(@RequestBody List<Integer> idList) {
        leaveService.removeByIds(idList);
        return Result.ok();
    }
}

