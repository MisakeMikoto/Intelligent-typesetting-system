package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.exception.MyException;
import com.hyc.nsms.model.result.Result;

import java.util.Date;
import java.util.List;

import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.queryVo.ExpectationQueryVo;
import com.hyc.nsms.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.service.ExpectationService;
import com.hyc.nsms.model.entity.Expectation;


import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expectation")
public class ExpectationController {
    @Autowired
    private ExpectationService expectationService;

    //查询Expectation表所有信息
    @GetMapping("findAllExpectation")
    public Result findAllExpectation() {
        List<Expectation> list = expectationService.list();
        return Result.ok(list);
    }

    //根据id获取Expectation
    @GetMapping("getExpectationById/{id}")
    public Result getExpectationById(@PathVariable Integer id) {
        Expectation expectation = expectationService.getById(id);
        return Result.ok(expectation);
    }

    //条件查询带分页
    @PostMapping("findPageExpectation/{current}/{limit}")
    public Result findPageExpectation(@PathVariable Integer current,
                                      @PathVariable Integer limit,
                                      @RequestParam Integer level,
                                      @RequestParam(required = false) Integer hospitalId,
                                      @RequestParam(required = false) Integer departmentId,
                                      @RequestBody(required = false) ExpectationQueryVo expectationQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<Expectation> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询期望信息
        Page<Expectation> expectationPage = expectationService.findPageExpectation(page, level, hospitalId, departmentId, expectationQueryVo);

        //返回结果
        return Result.ok(expectationPage);
    }

    //添加Expectation
    @PostMapping("saveExpectation")
    public Result saveExpectation(@RequestBody Expectation expectation) {
        Date startDate = expectation.getStartDate();
        Date endDate = expectation.getEndDate();
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            if (!DateUtils.ifStartDateLessThanEndDate(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.DATE_ERROR);
            }
            if (!DateUtils.ifAfterToday(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.EXPECTATION_DATE_ERROR);
            }

            Integer duration = DateUtils.getDateDifference(startDate, endDate);
            expectation.setExpectedDays(duration);//计算时间差
        }
        boolean save = expectationService.save(expectation);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Expectation信息
    @PostMapping("updateExpectation")
    public Result updateExpectation(@RequestBody Expectation expectation) {
        Date startDate = expectation.getStartDate();
        Date endDate = expectation.getEndDate();
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            if (!DateUtils.ifStartDateLessThanEndDate(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.DATE_ERROR);
            }
            if (!DateUtils.ifAfterToday(startDate, endDate)) {
                throw new MyException(ResultCodeEnum.EXPECTATION_DATE_ERROR);
            }

            Integer duration = DateUtils.getDateDifference(startDate, endDate);
            expectation.setExpectedDays(duration);//计算时间差
        }
        boolean flag = expectationService.updateById(expectation);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Expectation
    @DeleteMapping("{id}")
    public Result removeExpectation(@PathVariable Integer id) {
        boolean flag = expectationService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Expectation
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Integer> idList) {
        expectationService.removeByIds(idList);
        return Result.ok();
    }
}

