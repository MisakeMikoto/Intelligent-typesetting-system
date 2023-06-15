package com.hyc.nsms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.nsms.model.vo.ScheduleVo;
import com.hyc.nsms.model.vo.StaffTimeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {
    Page<Schedule> findPageSchedule(Page<Schedule> page,
                                    @Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId,
                                    @Param("name") String name, @Param("departmentName") String departmentName, @Param("shiftName") String shiftName);


    List<Integer> getWorkUserIdList(@Param("departmentId") Integer departmentId,@Param("yesterday") Date yesterday, @Param("today") Date today, @Param("tomorrow") Date tomorrow);

    List<ScheduleVo> getScheduleByUserIdAndDate(@Param("userId") Integer userId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ScheduleVo> getScheduleByDepartmentIdAndDate(@Param("departmentId") Integer departmentId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ScheduleVo> getStaffByDepartmentIdAndDate(@Param("departmentId") Integer departmentId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<Integer> getStaffCount(@Param("departmentId") Integer departmentId, @Param("firstDate") String firstDate, @Param("lastDate") String lastDate);

    Integer deleteByScheduleDate(@Param("departmentId") Integer departmentId, @Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate);
}
