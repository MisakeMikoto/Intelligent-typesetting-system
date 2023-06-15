package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.ChangeShift;
import com.hyc.nsms.mapper.ChangeShiftMapper;
import com.hyc.nsms.model.vo.pageVo.ChangeShiftPageVo;
import com.hyc.nsms.model.vo.queryVo.ChangeShiftQueryVo;
import com.hyc.nsms.service.ChangeShiftService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class ChangeShiftServiceImpl extends ServiceImpl<ChangeShiftMapper, ChangeShift> implements ChangeShiftService {
    @Resource
    private ChangeShiftMapper changeShiftMapper;

    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询换班信息
    @Override
    public Page<ChangeShift> findPageChangeShift(Page<ChangeShift> page, Integer level, Integer hospitalId, Integer departmentId, ChangeShiftQueryVo changeShiftQueryVo) {
        String name = changeShiftQueryVo.getName();
        String changedName = changeShiftQueryVo.getChangedName();

        //创建page对象，传递当前页，每页记录数
        Page<ChangeShift> changeShiftPage = changeShiftMapper.findPageChangeShift(page, level, hospitalId, departmentId, name, changedName);
        return changeShiftPage;
    }
}
