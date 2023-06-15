package com.hyc.nsms;

import com.hyc.nsms.mapper.ScheduleMapper;
import com.hyc.nsms.model.entity.Schedule;
import com.hyc.nsms.model.vo.UserWorkloadVo;
import com.hyc.nsms.service.LeaveService;
import com.hyc.nsms.service.ScheduleService;
import com.hyc.nsms.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@SpringBootTest
class NsmsApplicationTests {
    @Autowired
    ScheduleService scheduleService;

    @Resource
    ScheduleMapper scheduleMapper;

    @Autowired
    LeaveService leaveService;

    @Test
    public void test1() {
//        List<Schedule> list = scheduleService.list();
//        //①获取下月的第一天0点0分0秒
//        Date firstDate = DateUtils.getFirstDate();
//        //②获取下月的最后一天23点59分59秒
//        Date lastDate = DateUtils.getLastDate();
//        //③通过科室id从数据库删除区间数据
//        scheduleMapper.deleteByScheduleDate(2, firstDate, lastDate);
//        System.out.println(list);
    }
}
