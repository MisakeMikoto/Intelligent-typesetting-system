package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Leave;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.vo.UserWorkloadVo;
import com.hyc.nsms.model.vo.queryVo.LeaveQueryVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface LeaveService extends IService<Leave> {
    //员工请假后，获得替班员工
    List<UserWorkloadVo> getMatchStaff(Integer userId, String scheduleDate) throws ParseException;

    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询请假信息
    Page<Leave> findPageLeave(Page<Leave> page, Integer level, Integer hospitalId, Integer departmentId, LeaveQueryVo leaveQueryVo);
}
