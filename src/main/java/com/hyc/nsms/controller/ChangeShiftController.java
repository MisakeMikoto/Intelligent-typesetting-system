package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.exception.MyException;
import com.hyc.nsms.model.entity.ChangeShift;
import com.hyc.nsms.model.result.Result;
import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.queryVo.ChangeShiftQueryVo;
import com.hyc.nsms.service.ChangeShiftService;
import com.hyc.nsms.service.ScheduleService;
import com.hyc.nsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/changeShift")
public class ChangeShiftController {
    @Autowired
    private ChangeShiftService changeShiftService;

    @Autowired
    private ScheduleService scheduleService;

    //条件查询带分页
    @PostMapping("findPageChangeShift/{current}/{limit}")
    public Result findPageChangeShift(@PathVariable Integer current,
                                      @PathVariable Integer limit,
                                      @RequestParam Integer level,
                                      @RequestParam(required = false) Integer hospitalId,
                                      @RequestParam(required = false) Integer departmentId,
                                      @RequestBody(required = false) ChangeShiftQueryVo changeShiftQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<ChangeShift> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询换班信息
        Page<ChangeShift> changeShiftPage = changeShiftService.findPageChangeShift(page, level, hospitalId, departmentId, changeShiftQueryVo);

        //返回结果
        return Result.ok(changeShiftPage);
    }

    //根据id获取换班
    @GetMapping("getChangeShiftById/{id}")
    public Result getChangeShiftById(@PathVariable Integer id) {
        ChangeShift ChangeShift = changeShiftService.getById(id);
        return Result.ok(ChangeShift);
    }

    //添加换班
    @PostMapping("saveChangeShift")
    public Result saveChangeShift(@RequestBody ChangeShift changeShift) {
        Date scheduleDate = changeShift.getScheduleDate();
        Date changedScheduleDate = changeShift.getChangedScheduleDate();
        if (!StringUtils.isEmpty(scheduleDate) && !StringUtils.isEmpty(changedScheduleDate)) {
            if (!DateUtils.ifAfterToday(scheduleDate, changedScheduleDate)) {
                throw new MyException(ResultCodeEnum.CHANGE_DATE_ERROR);
            }
        }
        boolean save = changeShiftService.save(changeShift);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改换班信息
    @PostMapping("updateChangeShift")
    public Result updateChangeShift(@RequestBody ChangeShift changeShift) {
        Date scheduleDate = changeShift.getScheduleDate();
        Date changedScheduleDate = changeShift.getChangedScheduleDate();
        if (!StringUtils.isEmpty(scheduleDate) && !StringUtils.isEmpty(changedScheduleDate)) {
            if (!DateUtils.ifAfterToday(scheduleDate, changedScheduleDate)) {
                throw new MyException(ResultCodeEnum.CHANGE_DATE_ERROR);
            }
        }
        //更新换班表
        boolean flag = changeShiftService.updateById(changeShift);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除换班
    @DeleteMapping("{id}")
    public Result removeChangeShift(@PathVariable Integer id) {
        boolean flag = changeShiftService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除换班
    @DeleteMapping("batchRemove")
    public Result batchRemoveChangeShift(@RequestBody List<Integer> idList) {
        changeShiftService.removeByIds(idList);
        return Result.ok();
    }
}

