package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.exception.MyException;
import com.hyc.nsms.mapper.ScheduleMapper;
import com.hyc.nsms.model.entity.Leave;
import com.hyc.nsms.mapper.LeaveMapper;
import com.hyc.nsms.model.entity.Schedule;
import com.hyc.nsms.model.entity.Shift;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.UserWorkloadVo;
import com.hyc.nsms.model.vo.queryVo.LeaveQueryVo;
import com.hyc.nsms.service.LeaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.nsms.service.ShiftService;
import com.hyc.nsms.service.UserService;
import com.hyc.nsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService {
    @Resource
    private LeaveMapper leaveMapper;

    @Resource
    private ScheduleMapper scheduleMapper;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private UserService userService;

    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询请假信息
    @Override
    public Page<Leave> findPageLeave(Page<Leave> page, Integer level, Integer hospitalId, Integer departmentId, LeaveQueryVo leaveQueryVo) {
        String name = leaveQueryVo.getName();
        Page<Leave> leavePage = leaveMapper.findPageLeave(page, level, hospitalId, departmentId, name);
        return leavePage;
    }

    @Override
    public List<UserWorkloadVo> getMatchStaff(Integer userId, String scheduleDate) throws ParseException {
        //可以替班的人员的集合
        List<UserWorkloadVo> matchStaff = null;
        //通过userId查询人员信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        User user = userService.getOne(userQueryWrapper);
        Integer departmentId = user.getDepartmentId();
        Integer roleId = user.getRoleId();

        //通过userId和scheduleDate查询schedule得到shiftId
        QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.eq("user_id", userId);
        scheduleQueryWrapper.eq("schedule_date", scheduleDate);
        Schedule schedule = scheduleMapper.selectOne(scheduleQueryWrapper);

        //通过shiftId得到shift
        QueryWrapper<Shift> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", schedule.getShiftId());
        Shift shift = shiftService.getOne(queryWrapper);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获得昨天的日期
        Date yesterday = DateUtils.calculateDate(simpleDateFormat.parse(scheduleDate), -1);
        //获得今天的日期
        Date today = DateUtils.calculateDate(simpleDateFormat.parse(scheduleDate), 0);
        //如果要替班的是白班或者小夜班：前一天不能上大夜班，今天不能上班，前一天请假的
        if (shift.getShiftSign() == 1 || shift.getShiftSign() == 2) {
            //找到的昨天要上夜班、今天要上班的员工、昨天没请假的
            List<Integer> workUserIdList = scheduleMapper.getWorkUserIdList(departmentId,yesterday, today, null);
            //通过科室id和上班的员工，找到没上班的员工（可以替班的人员）
            matchStaff = leaveMapper.getMatchStaff(departmentId, roleId, workUserIdList);
        }
        //如果为大夜班：前一天不能上大夜班，今天不能上班，明天不能上班
        else if (shift.getShiftSign() == 3) {
            //获得明天天的日期
            Date tomorrow = DateUtils.calculateDate(simpleDateFormat.parse(scheduleDate), 1);
            //找到的昨天要上夜班、今天要上班、明天要上班的员工
            List<Integer> workUserIdList = scheduleMapper.getWorkUserIdList(departmentId,yesterday, today, tomorrow);
            //通过科室id和上班的员工，找到没上班的员工（可以替班的人员）
            matchStaff = leaveMapper.getMatchStaff(departmentId, roleId, workUserIdList);
        }
        System.out.println("-------------------------可以替班的人员");
        for (UserWorkloadVo userWorkloadVo : matchStaff) {
            System.out.println(userWorkloadVo);
        }
        //没有合适的人员可以替班
        if (matchStaff == null || matchStaff.isEmpty()) {
            throw new MyException(ResultCodeEnum.MATCH_STAFF_ERROR);
        }
        return matchStaff;
    }
}
