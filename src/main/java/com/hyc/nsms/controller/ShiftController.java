package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.result.Result;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hyc.nsms.model.vo.queryVo.ShiftQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.service.ShiftService;
import com.hyc.nsms.model.entity.Shift;


import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shift")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    //条件查询带分页
    @PostMapping("findPageShift/{current}/{limit}")
    public Result findPageShift(@PathVariable Integer current,
                                @PathVariable Integer limit,
                                @RequestParam(required = false) Integer hospitalId,
                                @RequestParam(required = false) Integer departmentId,
                                @RequestBody(required = false) ShiftQueryVo shiftQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<Shift> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询班次信息
        Page<Shift> shiftPage = shiftService.findPageShift(page, hospitalId, departmentId, shiftQueryVo);

        //返回结果
        return Result.ok(shiftPage);
    }

    //根据departmentId获取Shift
    @GetMapping("getShiftByDepartmentId")
    public Result getShiftByDepartmentId(@RequestParam(required = false) Integer departmentId) {
        QueryWrapper<Shift> queryWrapper = new QueryWrapper<>();
        if(departmentId!=null){
            queryWrapper.eq("department_id", departmentId);
        }
        List<Shift> list = shiftService.list(queryWrapper);
        return Result.ok(list);
    }

    //根据id获取Shift
    @GetMapping("getShiftById/{id}")
    public Result getShiftById(@PathVariable Integer id) {
        Shift shift = shiftService.getById(id);
        return Result.ok(shift);
    }

    //添加Shift
    @PostMapping("saveShift")
    public Result saveShift(@RequestBody Shift shift) {
        Date startTime = shift.getStartTime();
        Date endTime = shift.getEndTime();
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            double duration = this.getDuration(startTime, endTime);
            shift.setDuration(duration);//计算时间差
        }
        boolean save = shiftService.save(shift);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Shift信息
    @PostMapping("updateShift")
    public Result updateShift(@RequestBody Shift shift) {
        Date startTime = shift.getStartTime();
        Date endTime = shift.getEndTime();
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            double duration = this.getDuration(startTime, endTime);
            shift.setDuration(duration);//计算时间差
        }
        boolean flag = shiftService.updateById(shift);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Shift
    @DeleteMapping("{id}")
    public Result removeShift(@PathVariable Integer id) {
        boolean flag = shiftService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Shift
    @DeleteMapping("batchRemove")
    public Result batchRemoveShift(@RequestBody List<Integer> idList) {
        shiftService.removeByIds(idList);
        return Result.ok();
    }

    //计算时间差
    private double getDuration(Date startTime, Date endTime) {
        //如果下班时间小于上时间，代表下班为第二天，则需要将在原时间加上24小时,即一天
        if (endTime.before(startTime)) {//比较上班和下班时间，如果下班时间小于上班时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);//设置时间
            calendar.add(Calendar.DATE, 1);//在该时间加上一天
            endTime = calendar.getTime();//新的下班日期
        }
        long result = endTime.getTime() - startTime.getTime();//计算时间差
        double duration = result / (1000.0 * 60 * 60);//转化为小时

        return duration;
    }
}

