package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Expectation;
import com.hyc.nsms.mapper.ExpectationMapper;
import com.hyc.nsms.model.vo.queryVo.ExpectationQueryVo;
import com.hyc.nsms.service.ExpectationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ExpectationServiceImpl extends ServiceImpl<ExpectationMapper, Expectation> implements ExpectationService {
    @Resource
    private ExpectationMapper expectationMapper;

    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询期望信息
    @Override
    public Page<Expectation> findPageExpectation(Page<Expectation> page, Integer level, Integer hospitalId, Integer departmentId, ExpectationQueryVo expectationQueryVo) {
        String name = expectationQueryVo.getName();
        Page<Expectation> expectationPage = expectationMapper.findPageExpectation(page, level, hospitalId, departmentId, name);
        return expectationPage;
    }

    //通过开始时间和结束时间获得被采用的期望
    @Override
    public List<Expectation> getExpectationByFirstDateAndLastDate(Date firstDate, Date lastDate) {
        QueryWrapper<Expectation> exceptionQueryWrapper = new QueryWrapper<>();
        exceptionQueryWrapper.eq("status", 1);//期望被批准
        exceptionQueryWrapper.between("start_date", firstDate, lastDate);
        List<Expectation> exceptionList = expectationMapper.selectList(exceptionQueryWrapper);
        return exceptionList;
    }
}
