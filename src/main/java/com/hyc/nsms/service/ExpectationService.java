package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Expectation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.vo.queryVo.ExpectationQueryVo;

import java.util.Date;
import java.util.List;

public interface ExpectationService extends IService<Expectation> {
    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询期望信息
    Page<Expectation> findPageExpectation(Page<Expectation> page, Integer level, Integer hospitalId, Integer departmentId, ExpectationQueryVo expectationQueryVo);

    //通过开始时间和结束时间获得被采用的期望
    List<Expectation> getExpectationByFirstDateAndLastDate(Date firstDate, Date lastDate);
}
