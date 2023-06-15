package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Shift;
import com.hyc.nsms.mapper.ShiftMapper;
import com.hyc.nsms.model.vo.queryVo.ShiftQueryVo;
import com.hyc.nsms.service.ShiftService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper, Shift> implements ShiftService {
    @Resource
    private ShiftMapper shiftMapper;

    //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询班次信息
    @Override
    public Page<Shift> findPageShift(Page<Shift> page, Integer hospitalId, Integer departmentId, ShiftQueryVo shiftQueryVo) {
        String shiftName = shiftQueryVo.getShiftName();
        String departmentName = shiftQueryVo.getDepartmentName();
        //创建page对象，传递当前页，每页记录数
        Page<Shift> shiftPage = shiftMapper.findPageShift(page, hospitalId, departmentId, shiftName, departmentName);
        return shiftPage;
    }

    //通过departmentId和shiftName获得该科室 某班次 的信息
    @Override
    public Shift getShiftByDepartmentIdAndShiftName(Integer departmentId, Integer shiftSign) {
        QueryWrapper<Shift> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        queryWrapper.eq("shift_sign", shiftSign);
        Shift shift = shiftMapper.selectOne(queryWrapper);
        return shift;
    }
}
