package com.hyc.nsms.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.nsms.exception.MyException;
import com.hyc.nsms.mapper.ScheduleMapper;
import com.hyc.nsms.model.entity.*;
import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.LeaveVo;
import com.hyc.nsms.model.vo.StaffTimeVo;
import com.hyc.nsms.model.vo.queryVo.ScheduleQueryVo;
import com.hyc.nsms.model.vo.ScheduleVo;
import com.hyc.nsms.service.*;
import com.hyc.nsms.utils.DateUtils;
import com.hyc.nsms.utils.WorkloadSortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {
    @Resource
    private ScheduleMapper scheduleMapper;

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private ExpectationService expectationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //获得下个月的日期
    static Date firstDate = DateUtils.getFirstDate();//获得下个月第一天
    static Date lastDate = DateUtils.getLastDate();//获得下个月最后一天

    //排班
    @Override
    public List<Schedule> autoSchedule(Integer departmentId) {
        //----------step1:获得科室班次信息----------
        //①通过departmentId 和shiftSign 获得该科室 大夜班 的信息
        Shift nightShift = shiftService.getShiftByDepartmentIdAndShiftName(departmentId, ResultCodeEnum.NIGHT_SHIFT.getCode());
        Integer nightShiftId = nightShift.getId();//该科室大夜班的id
        //如果未设置班次，则抛出异常
        if (nightShiftId == null) {
            throw new MyException(ResultCodeEnum.SHIFT_ERROR);
        }
        Integer nightNurseNumber = nightShift.getNurseNumber();//大夜班需要的护士数量
        Integer nightCarerNumber = nightShift.getCarerNumber();//大夜班需要的助手数量
        //②通过departmentId 和shiftSign 获得该科室 白班 的信息
        Shift morningShift = shiftService.getShiftByDepartmentIdAndShiftName(departmentId, ResultCodeEnum.DAY_SHIFT.getCode());
        Integer morningShiftId = morningShift.getId();//该科室白班的id
        Integer morningShiftSign = morningShift.getShiftSign();//该科室白班的标识
        Integer morningNurseNumber = morningShift.getNurseNumber();//白班需要的护士数量
        Integer morningCarerNumber = morningShift.getCarerNumber();//白班需要的助手数量
        //③通过departmentId 和shiftSign 获得该科室 小夜班 的信息
        Shift middleShift = shiftService.getShiftByDepartmentIdAndShiftName(departmentId, ResultCodeEnum.MIDDLE_SHIFT.getCode());
        Integer middleShiftId = 0;//该科室夜班的id
        Integer middleShiftSign = 0;//该科室小夜班的标识
        Integer middleNurseNumber = 0;//小夜班需要的护士数量
        Integer middleCarerNumber = 0;//小夜班需要的助手数量
        if (middleShift != null) {
            middleShiftId = middleShift.getId();//该科室夜班的id
            middleShiftSign = middleShift.getShiftSign();//该科室小夜班的标识
            middleNurseNumber = middleShift.getNurseNumber();//小夜班需要的护士数量
            middleCarerNumber = middleShift.getCarerNumber();//小夜班需要的助手数量
        }


        //----------step2:计算实际排班需要的员工人数----------
        //①计算实际排班需要的护士人数
        int nurseNumber = morningNurseNumber + middleNurseNumber + nightNurseNumber;//科室需要的护士人数
        int allNeedNurseNumber = (int) Math.ceil((double) nurseNumber * 7 / 5);//通过累加计算总共需要护士人数(确保一个人一周最少放假7 - 5 = 2天)
        int nightNeedNurseNumber = (int) Math.ceil((double) nightNurseNumber * 7 / 2);//通过夜班计算需要护士人数(确保一个人一周最多上2天夜班)
        nurseNumber = Math.max(allNeedNurseNumber, nightNeedNurseNumber);//实际排班至少需要的护士人数
        //②计算实际排班需要的助手人数
        int carerNumber = morningCarerNumber + middleCarerNumber + nightCarerNumber;//科室需要的助手人数
        int allNeedCarerNumber = (int) Math.ceil((double) carerNumber * 7 / 5);//通过累加计算总共需要助手人数(确保一个人一周最少放假7 - 5 = 2天)
        int nightNeedCarerNumber = (int) Math.ceil((double) nightCarerNumber * 7 / 2);//通过夜班计算需要助手人数(确保一个人一周最多上2天夜班)
        carerNumber = Math.max(allNeedCarerNumber, nightNeedCarerNumber);//实际排班至少需要的助手人数


        //----------step3:获得已有的护士和助手数量----------
        //①通过departmentId获得所有护士的数量
        Integer nurseCount = workloadService.getCountByDepartmentIdAndRoleId(departmentId, ResultCodeEnum.NURSE.getCode());
        //②通过departmentId获得所有助手的数量
        Integer carerCount = workloadService.getCountByDepartmentIdAndRoleId(departmentId, ResultCodeEnum.ASSISTANT.getCode());
        //③通过departmentId获得所有护士长的数量
        Integer headNurseCount = workloadService.getCountByDepartmentIdAndRoleId(departmentId, ResultCodeEnum.HEAD_NURSE.getCode());


        //----------step4:判断排班人数是否足够----------
        if (nurseCount + headNurseCount < nurseNumber) {
            throw new MyException(ResultCodeEnum.NURSE_NUMBER_ERROR.getMessage() + "：" + nurseNumber + "人。", ResultCodeEnum.NURSE_NUMBER_ERROR.getCode());
        } else if (carerCount < carerNumber) {
            throw new MyException(ResultCodeEnum.CARER_NUMBER_ERROR.getMessage() + "：" + carerNumber + "人。", ResultCodeEnum.CARER_NUMBER_ERROR.getCode());
        }


        //----------step5:将下个月的工作天数恢复成0----------
        workloadService.updateNextMonth();


        //----------step6:获取员工“请假开始”到”请假结束“的日期并保存到集合leaveVoList中----------
        //定义Calendar类
        Calendar calendar;
        //①查询员工批假成功的信息
        QueryWrapper<Leave> leaveQueryWrapper = new QueryWrapper<>();
        leaveQueryWrapper.eq("status", 1);//请假被批准
        leaveQueryWrapper.between("start_date", firstDate, lastDate);
        List<Leave> leaveList = leaveService.list(leaveQueryWrapper);
        //②遍历请假集合，将员工请假开始到请假结束的日期计算并保存到新的集合
        calendar = Calendar.getInstance();//定义Calendar类
        LeaveVo leaveVo;//定义LeaveVo类
        List<LeaveVo> leaveVoList = new ArrayList<>();//定义新的请假集合
        for (Leave l : leaveList) {//遍历请假集合
            for (int i = 0; i < l.getLeaveDays(); i++) {//从开始时间一直叠加天数
                calendar.setTime(l.getStartDate());//设置开始时间
                calendar.add(Calendar.DATE, i);//在该时间叠加天数
                leaveVo = new LeaveVo();
                leaveVo.setUserId(l.getUserId());//设置员工id
                leaveVo.setStartDate(calendar.getTime());//设置新的请假日期
                leaveVoList.add(leaveVo);//添加到leaveVoList集合
            }
        }
//        for (LeaveVo vo : leaveVoList) {
//            System.out.println("============================================" + vo);
//        }


        //----------step7:通过下个月开始日期和结束日期获取员工采用成功的”期望“集合----------
        List<Expectation> exceptionList = expectationService.getExpectationByFirstDateAndLastDate(firstDate, lastDate);
//        for (Expectation expectation : exceptionList) {
//            System.out.println("============================================" + expectation);
//        }


        //----------step8:获得所有护士和助手信息----------
        //①通过departmentId获得所有护士的信息
        List<Workload> nurseWorkloadList = workloadService.getWorkloadByDepartmentIdAndRoleId(departmentId, ResultCodeEnum.NURSE.getCode());
        //②通过departmentId获得所有助手的信息
        List<Workload> carerWorkloadList = workloadService.getWorkloadByDepartmentIdAndRoleId(departmentId, ResultCodeEnum.ASSISTANT.getCode());
        //③通过departmentId获得所有护士长的信息
        List<Workload> headNurseWorkloadList = workloadService.getWorkloadByDepartmentIdAndRoleId(departmentId, ResultCodeEnum.HEAD_NURSE.getCode());
        //④合并护士和护士长的集合
        nurseWorkloadList.addAll(headNurseWorkloadList);


        //==========step9.0：排班之前的准备工作==========
        //①定义排班集合，用于保存排班信息
        List<Schedule> scheduleList = new ArrayList<>();
        //②定义存储前一天晚排大夜班员工id的集合
        List<Integer> lastNightStaffIds = new ArrayList<>();
        //③定义存储今晚排大夜班员工id的集合
        List<Integer> tonightStaffIds = new ArrayList<>();
        //④定义存储今天排班员工id的集合
        List<Integer> todayStaffIds = new ArrayList<>();
        //⑤获得下个月天数(28,29,30,31)
        int days = DateUtils.getNextMonthDays();
        //----------step9:开始排班（排下个月的班,遍历天数，排一个月）----------
        for (int day = 1; day <= days; day++) {
            //==========step8.1:初始化工作量信息==========
            if (day == 1) {
                //初始化护士工作量信息
                initWorkload(nurseWorkloadList);
                //初始化助手工作量信息
                initWorkload(carerWorkloadList);
            }

            //==========step9.2:根据天数获取下个月该天的日期==========
            Date thatDay = DateUtils.getNextMonthByDay(day);

            //==========step9.3:员工请假处理==========
            //①定义集合保存今天请假员工的id
            List<Integer> leaveIdList = new ArrayList<>();
            //②查询leaveVoList中是否有该天的日期
            for (LeaveVo l : leaveVoList) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                //如果leaveVoList中有该天的日期，代表今天有人请假
                if (format.format(l.getStartDate()).equals(format.format(thatDay))) {
                    leaveIdList.add(l.getUserId());//③将id保存到请假员工的id集合里面
                }
            }

            //==========step9.4:员工期望处理==========
            //查询是否今天有排班期望
            for (Expectation expectation : exceptionList) {
                expectation.setComplete(false);//完成状态重置为false
                Date startDate = expectation.getStartDate();//期望开始时间
                Date endDate = expectation.getEndDate();//期望结束时间
                //判断今天是否在期望的日期期间内
                if (DateUtils.ifDateOnPeriodDate(thatDay, startDate, endDate)) {
                    //①将护士期望进行实现
                    this.handleExpectation(expectation, nurseWorkloadList, scheduleList,
                            lastNightStaffIds, todayStaffIds, tonightStaffIds,
                            nightNurseNumber, morningNurseNumber, middleNurseNumber,
                            nightShiftId, morningShiftId, middleShiftId,
                            morningShiftSign, middleShiftSign, thatDay, leaveIdList, ResultCodeEnum.NURSE.getCode());
                    //②将助手期望进行实现
                    this.handleExpectation(expectation, carerWorkloadList, scheduleList,
                            lastNightStaffIds, todayStaffIds, tonightStaffIds,
                            nightNurseNumber, morningNurseNumber, middleNurseNumber,
                            nightShiftId, morningShiftId, middleShiftId,
                            morningShiftSign, middleShiftSign, thatDay, leaveIdList, ResultCodeEnum.ASSISTANT.getCode());
                }
            }

            //==========step9.5:排 一天 护士 的班==========
            //①排大夜班的护士
            this.scheduleNight(nurseWorkloadList, scheduleList, nightNurseNumber, lastNightStaffIds, tonightStaffIds, todayStaffIds, nightShiftId, thatDay, leaveIdList, ResultCodeEnum.NURSE.getCode());
            //②排白班的护士
            this.scheduleMorning(nurseWorkloadList, scheduleList, morningNurseNumber, lastNightStaffIds, todayStaffIds, morningShiftId, morningShiftSign, thatDay, leaveIdList, ResultCodeEnum.NURSE.getCode());
            //③排小夜班的护士
            this.scheduleMiddle(nurseWorkloadList, scheduleList, middleNurseNumber, lastNightStaffIds, todayStaffIds, middleShiftId, middleShiftSign, thatDay, leaveIdList, ResultCodeEnum.NURSE.getCode());

            //==========step9.6:排 一天 助手 的班==========
            //④排大夜班的助手
            this.scheduleNight(carerWorkloadList, scheduleList, nightCarerNumber, lastNightStaffIds, tonightStaffIds, todayStaffIds, nightShiftId, thatDay, leaveIdList, ResultCodeEnum.ASSISTANT.getCode());
            //⑤排白班的助手
            this.scheduleMorning(carerWorkloadList, scheduleList, morningCarerNumber, lastNightStaffIds, todayStaffIds, morningShiftId, morningShiftSign, thatDay, leaveIdList, ResultCodeEnum.ASSISTANT.getCode());
            //⑥排小夜班的助手
            this.scheduleMiddle(carerWorkloadList, scheduleList, middleCarerNumber, lastNightStaffIds, todayStaffIds, middleShiftId, middleShiftSign, thatDay, leaveIdList, ResultCodeEnum.ASSISTANT.getCode());

            //==========step9.7:集合操作==========
            //①清空昨晚的员工id的集合
            lastNightStaffIds.clear();
            //②让昨晚的员工id的集合 等于 今晚员工id的集合
            lastNightStaffIds.addAll(tonightStaffIds);
            //③清空今晚员工id的集合
            tonightStaffIds.clear();
            //④清空今天的员工id的集合
            todayStaffIds.clear();

            //==========step9.8:每过一周，重置每周大夜班天数==========
            if (day >= 7 && day % 7 == 0) {
                //①将护士大夜班天数设置为0
                for (Workload workload : nurseWorkloadList) {
                    workload.setWeekWorkDays(0);
                    workload.setWeekEveningShifts(0);
                }
                //②将助手大夜班天数设置为0
                for (Workload workload : carerWorkloadList) {
                    workload.setWeekWorkDays(0);
                    workload.setWeekEveningShifts(0);
                }
            }
        }


        //----------step10:排班完毕之前，删除区间在下个月第一天和最后一天的数据----------
        scheduleMapper.deleteByScheduleDate(departmentId, firstDate, lastDate);


        //----------step11:排班完毕，将数据保存到数据库----------
        //修改工作量表
        workloadService.updateBatchById(nurseWorkloadList);
        workloadService.updateBatchById(carerWorkloadList);

        //保存排班表
        saveBatch(scheduleList);

//        //删除缓存
//        this.flushRedis("getScheduleByUserIdAndDate");
//        this.flushRedis("getScheduleByDepartmentIdAndDate");


        //----------step12:结束----------
        return scheduleList;
    }

    /**
     * 初始化员工上班天数
     *
     * @param workloadList 员工工作量集合
     */
    private void initWorkload(List<Workload> workloadList) {
        for (Workload workload : workloadList) {
            //①将员工一周内工作天数设置为0
            workload.setWeekWorkDays(0);
            //②将员工一周内大夜班天数设置为0
            workload.setWeekEveningShifts(0);

            //③将员工排序用的工作天数设置为实际上班天数
            workload.setCurrentWorkDays(workload.getRealWorkDays());
            //④将员工排序用的大夜班天数设置为实际上班天数
            workload.setCurrentEveningShifts(workload.getRealEveningShifts());
            //⑤将员工排序用的白班天数设置为实际上班天数
            workload.setCurrentMorningShifts(workload.getRealMorningShifts());
            //⑥将员工排序用的白班天数设置为实际上班天数
            workload.setCurrentMiddleShifts(workload.getRealMiddleShifts());
        }
    }

    /**
     * step8.4：处理员工期望
     *
     * @param expectation        期望对象
     * @param staffWorkloadList  员工工作量集合
     * @param scheduleList       排班集合
     * @param lastNightStaffIds  昨晚排班的员工id集合
     * @param todayStaffIds      今天排班的员工id集合
     * @param tonightStaffIds    今晚排班的员工id集合
     * @param nightStaffNumber   大夜班需要的员工数量
     * @param morningStaffNumber 白班需要的员工数量
     * @param middleStaffNumber  小夜班需要的员工数量
     * @param nightShiftId       大夜班的班次id
     * @param morningShiftId     白班的班次id
     * @param middleShiftId      小夜班的班次id
     * @param morningShiftSign   白班的班次标识
     * @param middleShiftSign    小夜班的班次标识
     * @param thatDay            下个月的那天
     * @param leaveIdList        请假员工id集合
     * @param roleId             角色id
     */
    private void handleExpectation(Expectation expectation, List<Workload> staffWorkloadList, List<Schedule> scheduleList,
                                   List<Integer> lastNightStaffIds, List<Integer> todayStaffIds, List<Integer> tonightStaffIds,
                                   Integer nightStaffNumber, Integer morningStaffNumber, Integer middleStaffNumber,
                                   Integer nightShiftId, Integer morningShiftId, Integer middleShiftId,
                                   Integer morningShiftSign, Integer middleShiftSign,
                                   Date thatDay, List<Integer> leaveIdList, Integer roleId) {
        boolean flagComplete = false;
        Integer userId = expectation.getUserId();//用户id
        //将员工期望进行实现
        for (Workload workload : staffWorkloadList) {
            //通过userId对比找到有期望需要实现的员工
            if (userId.equals(workload.getUserId())) {
                if (expectation.getShiftSign() == 3) {//①护士大夜班期望
                    //如果此时今天排大夜班的员工人数已经足够，则不需要再排班
                    if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, nightShiftId, roleId) >= nightStaffNumber) {
                        break;
                    } else if (!lastNightStaffIds.contains(userId) && workload.getWeekWorkDays() != 6 && workload.getWeekEveningShifts() != 3 && !leaveIdList.contains(userId)) {
                        //保存 排班信息 和 工作天数信息（大夜班）
                        this.updateAndSaveNight(userId, scheduleList, workload, tonightStaffIds, todayStaffIds, nightShiftId, thatDay);
                        //判断是否完成期望
                        flagComplete = true;
                    }
                } else if (expectation.getShiftSign() == 1) {//②护士白班期望
                    //如果此时今天排白班的员工人数已经足够，则不需要再排班
                    if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, morningShiftId, roleId) >= morningStaffNumber) {
                        break;
                    } else if (workload.getWeekWorkDays() != 6 && !lastNightStaffIds.contains(userId) && !todayStaffIds.contains(userId) && !leaveIdList.contains(userId)) {
                        //保存 排班信息 和 工作天数信息
                        this.updateAndSaveDay(userId, scheduleList, workload, todayStaffIds, morningShiftId, morningShiftSign, thatDay);
                        //判断是否完成期望
                        flagComplete = true;
                    }
                } else if (expectation.getShiftSign() == 2) {//③护士小夜班期望
                    //如果此时今天排小夜班的员工人数已经足够，则不需要再排班
                    if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, middleShiftId, roleId) >= middleStaffNumber) {
                        break;
                    } else if (workload.getWeekWorkDays() != 6 && !lastNightStaffIds.contains(userId) && !todayStaffIds.contains(userId) && !leaveIdList.contains(userId)) {
                        //保存 排班信息 和 工作天数信息
                        this.updateAndSaveDay(userId, scheduleList, workload, todayStaffIds, middleShiftId, middleShiftSign, thatDay);
                        //判断是否完成期望
                        flagComplete = true;
                    }
                }
                //更新期望
                expectation.setComplete(flagComplete);
                expectationService.updateById(expectation);
            }
        }
    }

    /**
     * step9:排大夜班具体实现
     *
     * @param staffList         员工集合
     * @param scheduleList      排班集合
     * @param nightStaffNumber  大夜班需要的员工数量
     * @param lastNightStaffIds 昨晚排班的员工id集合
     * @param tonightStaffIds   今晚排班的员工id集合
     * @param todayStaffIds     今天排班的员工id集合
     * @param nightShiftId      大夜班班次id
     * @param thatDay           下个月的那天
     * @param leaveIdList       请假员工id集合
     * @param roleId            角色id
     */
    private void scheduleNight(List<Workload> staffList, List<Schedule> scheduleList, Integer nightStaffNumber,
                               List<Integer> lastNightStaffIds, List<Integer> tonightStaffIds, List<Integer> todayStaffIds,
                               Integer nightShiftId, Date thatDay, List<Integer> leaveIdList, Integer roleId) {
        //----------step1：将所有员工按照大夜班规则排序----------
        WorkloadSortUtils.nightWorkloadSort(staffList);


        //----------step2：员工信息集合staffList迭代----------
        for (Workload workload : staffList) {
            //如果此时今天排班的员工人数已经足够，则不需要再排班
            if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, nightShiftId, roleId) >= nightStaffNumber) {
                break;
            }

            //获得员工id
            Integer userId = workload.getUserId();

            //①如果员工这周已经排了6个班次或者已经排过3天晚班，则这为员工一周上班的最大次数已满，跳过这位员工；
            //②如果员工这周已经排过3天晚班，则这为员工一周上大夜班的最大次数已满，跳过这位员工；
            //③如果员工前一天排了大夜班,因为要隔3个班次，所以今天不能排大夜班，跳过这位员工；（lastNightStaffIds中没有该员工的id,代表该员工没有被排进前一天的的大夜班，可以排今天的大夜班。）
            //④如果该员工没有在这天请假。（leaveIdList中没有该员工的id,代表该员工没有在今天请假。）
            //⑤如果员工没有自主排班期望。（expectationIdList中没有该员工的id,代表该员工今天没有排班期望）
            if (!lastNightStaffIds.contains(userId) && workload.getWeekWorkDays() != 6 && workload.getWeekEveningShifts() != 3 && !leaveIdList.contains(userId)) {
                //保存 排班信息 和 工作天数信息（大夜班）
                this.updateAndSaveNight(userId, scheduleList, workload, tonightStaffIds, todayStaffIds, nightShiftId, thatDay);
            }
        }


        //----------step3：如果此时今天排的夜班员工人数不够，直接抛出异常----------
        if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, nightShiftId, roleId) < nightStaffNumber) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            throw new MyException(format.format(thatDay) + "出现问题，导致" + ResultCodeEnum.SCHEDULE_STAFF_ERROR.getMessage(), ResultCodeEnum.SCHEDULE_STAFF_ERROR.getCode());
        }
    }

    /**
     * 保存 排班信息 和 工作天数信息（大夜班）
     *
     * @param userId          用户id
     * @param scheduleList    排班集合
     * @param workload        工作量对象
     * @param tonightStaffIds 今晚排班的员工id集合
     * @param todayStaffIds   今天排班的员工id集合
     * @param nightShiftId    大夜班班次id
     * @param thatDay         下个月的那天
     */
    private void updateAndSaveNight(Integer userId, List<Schedule> scheduleList, Workload workload,
                                    List<Integer> tonightStaffIds, List<Integer> todayStaffIds, Integer nightShiftId, Date thatDay) {
        //①将员工排班信息保存
        Schedule schedule = new Schedule();
        schedule.setUserId(userId);//员工id
        schedule.setShiftId(nightShiftId);//夜班班次id
        schedule.setScheduleDate(thatDay);//当天日期
        schedule.setRoleId(workload.getRoleId());//角色id
        scheduleList.add(schedule);//保存到scheduleList集合

        //②员工工作天数计算
        workload.setWorkDays(workload.getWorkDays() + 1);//将员工下个月 预计 的工作天数+1
        workload.setCurrentWorkDays(workload.getCurrentWorkDays() + 1);//将员工 用于排序 的工作天数+1
        workload.setWeekEveningShifts(workload.getWeekEveningShifts() + 1);//将员工 周 大夜班天数+1(不能超过2)

        workload.setEveningShifts(workload.getEveningShifts() + 1);//将员工下个月 预计 的大夜班天数+1
        workload.setCurrentEveningShifts(workload.getCurrentEveningShifts() + 1);//将员工 用于排序 的大夜班天数+1
        workload.setWeekWorkDays(workload.getWeekWorkDays() + 1);//将员工 周 工作天数+1(不能超过6)

        //③将该员工id存储今天排了班的员工id集合
        todayStaffIds.add(userId);

        //④将该员工id存储今天排了大夜班的员工id集合
        tonightStaffIds.add(userId);
    }

    /**
     * step9:排白班具体实现
     *
     * @param staffList          员工集合
     * @param scheduleList       排班集合
     * @param morningStaffNumber 白班需要的员工数量
     * @param lastNightStaffIds  昨晚排班的员工id集合
     * @param todayStaffIds      今天排班的员工id集合
     * @param morningShiftId     本科室白班班次id
     * @param morningShiftSign   本科室白班班次标识
     * @param thatDay            下个月的那天
     * @param leaveIdList        请假员工id集合
     * @param roleId             角色id
     */
    private void scheduleMorning(List<Workload> staffList, List<Schedule> scheduleList, Integer morningStaffNumber,
                                 List<Integer> lastNightStaffIds, List<Integer> todayStaffIds, Integer morningShiftId,
                                 Integer morningShiftSign, Date thatDay, List<Integer> leaveIdList, Integer roleId) {
        //----------step1：将所有员工按照白班规则排序----------
        WorkloadSortUtils.morningWorkloadSort(staffList);


        //----------step2：员工信息集合staffList迭代----------
        for (Workload workload : staffList) {
            //如果此时今天排班的员工人数已经足够，则不需要再排班
            if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, morningShiftId, roleId) >= morningStaffNumber) {
                break;
            }

            //获得员工id
            Integer userId = workload.getUserId();

            //①如果员工这周已经排了6个班次，则这为员工一周最大上班的次数已满，跳过这位员工；
            //②如果员工前一天排了大夜班，因为要隔3个班次,所以今天不能排白班或小夜班，跳过这位员工；（lastNightStaffIds中没有该员工的id,代表该员工没有被排进前一天的的大夜班，可以排今天的白班）
            //③如果员工今天排了大夜班，则不能排白班，跳过这位员工；（todayStaffIds中没有该员工的id,代表该员工没有被排进今天的其他任何班次，可以排今天的班）
            //④如果员工没有在这天请假。（leaveIdList中没有该员工的id,代表该员工没有在今天请假）
            if (workload.getWeekWorkDays() != 6 && !lastNightStaffIds.contains(userId) && !todayStaffIds.contains(userId) && !leaveIdList.contains(userId)) {
                //保存 排班信息 和 工作天数信息
                this.updateAndSaveDay(userId, scheduleList, workload, todayStaffIds, morningShiftId, morningShiftSign, thatDay);
            }
        }


        //----------step3：如果此时今天排的白班员工人数不够，直接抛出异常----------
        if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, morningShiftId, roleId) < morningStaffNumber) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            throw new MyException(format.format(thatDay) + "出现问题，导致" + ResultCodeEnum.SCHEDULE_STAFF_ERROR.getMessage(), ResultCodeEnum.SCHEDULE_STAFF_ERROR.getCode());
        }
    }

    /**
     * step9:排小夜班具体实现
     *
     * @param staffList         员工集合
     * @param scheduleList      排班集合
     * @param middleStaffNumber 小夜班需要的员工数量
     * @param lastNightStaffIds 昨晚排班的员工
     * @param todayStaffIds     今天排班的员工
     * @param middleShiftId     本科室小夜班次id
     * @param middleShiftSign   本科室小夜班次标识
     * @param thatDay           第几天
     * @param leaveIdList       请假员工id集合
     * @param roleId            角色id
     */
    private void scheduleMiddle(List<Workload> staffList, List<Schedule> scheduleList, Integer middleStaffNumber,
                                List<Integer> lastNightStaffIds, List<Integer> todayStaffIds, Integer middleShiftId,
                                Integer middleShiftSign, Date thatDay, List<Integer> leaveIdList, Integer roleId) {
        //----------step1：将所有员工按照白班或者小夜班规则排序----------
        WorkloadSortUtils.middleWorkloadSort(staffList);


        //----------step2：员工信息集合staffList迭代----------
        for (Workload workload : staffList) {
            //如果此时今天排班的员工人数已经足够，则不需要再排班
            if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, middleShiftId, roleId) >= middleStaffNumber) {
                break;
            }

            //获得员工id
            Integer userId = workload.getUserId();

            //①如果员工这周已经排了5个班次，则这为员工一周上班的次数已满，跳过这位员工；
            //②如果员工前一天排了大夜班，因为要隔3个班次,所以今天不能排白班或小夜班，跳过这位员工；（lastNightStaffIds中没有该员工的id,代表该员工没有被排进前一天的的大夜班，可以排今天的白班或小夜班。）
            //③如果员工今天排了大夜班或者白班，则不能排小夜班，跳过这位员工；（todayStaffIds中没有该员工的id,代表该员工没有被排进今天的其他任何班次，可以排今天的班。）
            //④如果员工没有在这天请假。（leaveIdList中没有该员工的id,代表该员工没有在今天请假。）
            if (!lastNightStaffIds.contains(userId) && workload.getWeekWorkDays() != 6 && !todayStaffIds.contains(userId) && !leaveIdList.contains(userId)) {
                //保存 排班信息 和 工作天数信息
                this.updateAndSaveDay(userId, scheduleList, workload, todayStaffIds, middleShiftId, middleShiftSign, thatDay);
            }
        }


        //----------step3：如果此时今天排的小夜班员工人数不够，直接抛出异常----------
        if (this.getCountByDepartmentIdAndDate(scheduleList, thatDay, middleShiftId, roleId) < middleStaffNumber) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            throw new MyException(format.format(thatDay) + "出现问题，导致" + ResultCodeEnum.SCHEDULE_STAFF_ERROR.getMessage(), ResultCodeEnum.SCHEDULE_STAFF_ERROR.getCode());
        }
    }

    /**
     * 保存 排班信息 和 工作天数信息（白班和小夜班）
     *
     * @param userId        用户id
     * @param scheduleList  排班集合
     * @param workload      工作量对象
     * @param todayStaffIds 今天排班的员工id集合
     * @param dayShiftId    白班或者小夜班班次id
     * @param dayShiftSign  白班或者小夜班班次标识
     * @param thatDay       下个月的那天
     */
    private void updateAndSaveDay(Integer userId, List<Schedule> scheduleList, Workload workload,
                                  List<Integer> todayStaffIds, Integer dayShiftId, Integer dayShiftSign, Date thatDay) {
        //①将员工排班信息保存
        Schedule schedule = new Schedule();
        schedule.setUserId(userId);//员工id
        schedule.setShiftId(dayShiftId);//白班或者小夜班班次id
        schedule.setScheduleDate(thatDay);//当天日期
        schedule.setRoleId(workload.getRoleId());//角色id
        scheduleList.add(schedule);//保存到scheduleList集合

        //②将员工工作天数计算
        workload.setWorkDays(workload.getWorkDays() + 1);//将员工下个月 预计 的工作天数+1
        workload.setCurrentWorkDays(workload.getCurrentWorkDays() + 1);//将员工 用于排序 的工作天数+1
        workload.setWeekWorkDays(workload.getWeekWorkDays() + 1);//将员工 周 工作天数+1(不能超过6)

        if (dayShiftSign == 1) {
            workload.setMorningShifts(workload.getMorningShifts() + 1);//将员工下个月 预计 的白班天数+1
            workload.setCurrentMorningShifts(workload.getCurrentMorningShifts() + 1);//将员工 用于排序 的白班天数+1
        } else if (dayShiftSign == 2) {
            workload.setMiddleShifts(workload.getMiddleShifts() + 1);//将员工下个月 预计 的小夜班天数+1
            workload.setCurrentMiddleShifts(workload.getCurrentMiddleShifts() + 1);//将员工 用于排序 的小夜班天数+1
        }

        //③将该员工id存储今天排了班的员工id集合
        todayStaffIds.add(userId);
    }

    /**
     * 根据科室id、工作日期、班次id、角色id获取排班 数量
     *
     * @param scheduleList 排班集合
     * @param scheduleDate 排班日期
     * @param shiftId      班次id
     * @param roleId       角色id
     * @return
     */
    private Integer getCountByDepartmentIdAndDate(List<Schedule> scheduleList, Date scheduleDate, Integer shiftId, Integer roleId) {
        int count = 0;
        for (Schedule schedule : scheduleList) {
            if (scheduleDate.equals(schedule.getScheduleDate()) && shiftId.equals(schedule.getShiftId())) {
                //护士长也算护士
                if (roleId == 4 && 4 == schedule.getRoleId() || 3 == schedule.getRoleId()) {
                    count++;
                } else if (roleId == 5 && 5 == schedule.getRoleId()) {
                    count++;
                }
            }
        }
        return count;
    }

    //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询排班信息
    @Override
    public Page<Schedule> findPageSchedule(Page<Schedule> page, Integer hospitalId, Integer
            departmentId, ScheduleQueryVo scheduleQueryVo) {
        String name = scheduleQueryVo.getName();
        String departmentName = scheduleQueryVo.getDepartmentName();
        String shiftName = scheduleQueryVo.getShiftName();
        Page<Schedule> schedulePage = scheduleMapper.findPageSchedule(page, hospitalId, departmentId, name, departmentName, shiftName);
        return schedulePage;
    }

    //根据用户id和工作日期获取用户、排班和班次信息
    @Override
    public List<ScheduleVo> getScheduleByUserIdAndDate(Integer userId, String startDate, String endDate) {
        List<ScheduleVo> scheduleVoList = scheduleMapper.getScheduleByUserIdAndDate(userId, startDate, endDate);
        return scheduleVoList;
    }

    //根据科室id和工作日期获取用户、排班和班次信息
    @Override
    public List<StaffTimeVo> getScheduleByDepartmentIdAndDate(Integer departmentId, String startDate, String endDate) {
//        //标识
//        String sign = "getBy" + departmentId + "And" + startDate + "And" + endDate;
//        //1.从缓存获取数据
//        String jsonStr = stringRedisTemplate.opsForValue().get(sign);
//        List<ScheduleVo> scheduleVoList;
//        if (StringUtils.isEmpty(jsonStr)) {//2.取出来的json是空的
//            //3.从数据库取值
//            scheduleVoList = scheduleMapper.getScheduleByDepartmentIdAndDate(departmentId, startDate, endDate);
//            //4.再去缓存到redis
//            stringRedisTemplate.opsForValue().set(sign, JSON.toJSONString(scheduleVoList));
//        } else {//5.如果有, 从redis缓存中获取数据(减轻数据库的压力)
//            String getSchedule = stringRedisTemplate.opsForValue().get(sign);
//            scheduleVoList = JSONObject.parseArray(getSchedule, ScheduleVo.class);
//        }
        List<ScheduleVo> scheduleVoList = scheduleMapper.getScheduleByDepartmentIdAndDate(departmentId, startDate, endDate);
        List<Integer> staffCountList = scheduleMapper.getStaffCount(departmentId, startDate, endDate);
        int personCount = staffCountList.size();//排班人数
        Date start = null;//开始时间
        Date end = null;//结束时间
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            start = ft.parse(startDate);
            end = ft.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //转换数据格式
        List<StaffTimeVo> newList = toNewSchedule(start, end, scheduleVoList, personCount);
        return newList;
    }

    //数据操作形成新的排班表
    private List toNewSchedule(Date start, Date end, List<ScheduleVo> list, int personCount) {
        int k = 0;//记录list的指引
        Date date;//日期
        StaffTimeVo staffTime;//某个人今天上班情况
        List<StaffTimeVo> staffTimeList = new ArrayList<>();//一个人的一个月情况
        //形成新的返回数组
        for (int i = 0; i < personCount; i++) {
            //每换一个人就要更新一次date变为这个月的开始日期
            date = start;
            //判断是否有数据
            if (list.size() == 0 || list == null) {
                throw new MyException(ResultCodeEnum.EXPORT_ERROR);
            }
            //记录这个人的名字
            String name = list.get(k).getName();
            //往这个人的这个月里填充排班情况
            while (date.compareTo(end) <= 0) {
                staffTime = new StaffTimeVo();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                staffTime.setName(name);
                staffTime.setRoleName(list.get(k).getRoleName());
                staffTime.setDate(simpleDateFormat.format(date));
                //判断此时还是不是同一个人
                if (list.get(k).getName().equals(name)) {
                    //判断今天有没上班，没有shiftSign就为0,有就是list列表数据里的值
                    if (date.compareTo(list.get(k).getScheduleDate()) == 0) {
                        if (list.get(k).getLeaveStatus()) {
                            staffTime.setShiftSign(4);
                            staffTime.setShiftName("请假");
                        } else {
                            staffTime.setShiftSign(list.get(k).getShiftSign());
                            staffTime.setShiftName(list.get(k).getShiftName());
                        }
                        //根据这个list的结构规律，指向下一个的上班日期
                        if (k != list.size() - 1) {
                            //防止溢出
                            k++;
                        }
                    } else {
                        staffTime.setShiftSign(0);
                        staffTime.setShiftName("休息");
                    }
                } else {
                    staffTime.setShiftSign(0);
                    staffTime.setShiftName("休息");
                }
                //往这个人的这个月的列表添加这天情况
                staffTimeList.add(staffTime);
                //日期++
                date = datePlusPlus(date);
            }
        }
        return staffTimeList;
    }

    //date天数+1
    private Date datePlusPlus(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);
        date = calendar.getTime();
        return date;
    }

    //设置缓存
    private void setCache(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    //删除缓存
    private void flushRedis(String key) {
        stringRedisTemplate.delete(key);
    }
}
