package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Shift;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.vo.queryVo.ShiftQueryVo;

public interface ShiftService extends IService<Shift> {
    //通过传递当前页、每页记录数、医院id、科室id、查询条件分页查询班次信息
    Page<Shift> findPageShift(Page<Shift> page, Integer hospitalId, Integer departmentId, ShiftQueryVo shiftQueryVo);

    //通过departmentId和shiftName获得该科室 某班次 的信息
    Shift getShiftByDepartmentIdAndShiftName(Integer departmentId, Integer ShiftSign);
}
