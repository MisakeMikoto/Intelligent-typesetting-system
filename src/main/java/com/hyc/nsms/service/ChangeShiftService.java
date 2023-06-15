package com.hyc.nsms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.ChangeShift;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.nsms.model.vo.pageVo.ChangeShiftPageVo;
import com.hyc.nsms.model.vo.queryVo.ChangeShiftQueryVo;

public interface ChangeShiftService extends IService<ChangeShift> {
    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询换班信息
    Page<ChangeShift> findPageChangeShift(Page<ChangeShift> page, Integer level, Integer hospitalId, Integer departmentId, ChangeShiftQueryVo changeShiftQueryVo);
}
