package com.hyc.nsms.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//计算日期工具类
@Component
public class DateUtils {
    //获得下个月的月份
    public static int getNextMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 2;
        return month;
    }

    //获得下个月的天数
    public static int getNextMonthDays() {
        //获取下月的第一天
        Calendar calendar = Calendar.getInstance();
        //调到下个月
        calendar.add(Calendar.MONTH, 1);
        //下个月的天数
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    //根据下个月第几天获得该天的日期
    public static Date getNextMonthByDay(int day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取下月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day, 0, 0, 0);
        //Date类型转化成String类型
        String format = simpleDateFormat.format(calendar.getTime());
        Date date = null;
        try {
            //String类型转化成Date类型
            date = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //获得这个月最后一天的23点59分59秒（获得下个月第一天的0点0分0秒）
    public static Date getFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0, 23, 59, 59);
        Date firstDate = calendar.getTime();
        return firstDate;

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        //获取下月的第一天
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0, 0, 0, 0);
//        //Date类型转化成String类型
//        String format = simpleDateFormat.format(calendar.getTime());
//        Date firstDate = null;
//        try {
//            //String类型转化成Date类型
//            firstDate = simpleDateFormat.parse(format);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return firstDate;
    }

    //获取下月的最后一天的23点59分59秒
    public static Date getLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 2, 0, 23, 59, 59);
        Date lastDate = calendar.getTime();
        return lastDate;
    }

    //获得某个月份的开始日期
    public static Date getMonthStartDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), month - 1, 1, 0, 0, 0);
        Date startDate = calendar.getTime();
        return startDate;
    }

    //获得某个月份的结束日期
    public static Date getMonthEndDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), month, 0, 23, 59, 59);
        Date endDate = calendar.getTime();
        return endDate;
    }

    //判断开始日期是否小于等于结束日期
    public static boolean ifStartDateLessThanEndDate(Date startDate, Date endDate) {
        if (startDate.compareTo(endDate) <= 0) {
            return true;
        }
        return false;
    }

    //判断这段日期是否在另一段日期内
    public static boolean ifDateOnPeriodDate(Date date, Date startDate, Date endDate) {
        if (startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
            return true;
        }
        return false;
    }

    //判断这段日期是否在今天以后的时间段里面
    public static boolean ifAfterToday(Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) - 1, 23, 59, 59);
        Date now = calendar.getTime();
        if (startDate.compareTo(now) >= 0 && endDate.compareTo(now) >= 0) {
            return true;
        }
        return false;
    }

    //计算日期 value +1 代表日期的第二天 -1 代表这个日期的前一天 以此类推
    public static Date calculateDate(Date scheduleDate, Integer value) {
        Calendar calendar = Calendar.getInstance();
        //获取那天
        calendar.setTime(scheduleDate);
        //获取那天 过value天后的 日期
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + value, 0, 0, 0);

        Date date = calendar.getTime();
        return date;
    }

    //计算日期差
    public static Integer getDateDifference(Date startDate, Date endDate) {
        long difParse = endDate.getTime() - startDate.getTime();//计算日期差
        Integer result = Math.toIntExact(difParse / (24 * 60 * 60 * 1000) + 1);
        return result;
    }

    public static void main(String[] args) {
        //测试方法1
        int month = getNextMonth();
        System.out.println("获得下个月的月份是： " + month);

        //测试方法2
        int nextMonthDays = getNextMonthDays();
        System.out.println("下个月的天数是：" + nextMonthDays);

        //测试方法3
        int day = 1;
        Date nextMonthByDay = getNextMonthByDay(day);
        System.out.println("下个月的第" + day + "天是：" + nextMonthByDay);

        //测试方法4
        Date firstDate = getFirstDate();
        System.out.println("下个月的第一天是：" + firstDate);

        //测试方法5
        Date lastDate = getLastDate();
        System.out.println("下个月的第最后一天是：" + lastDate);

        Date nextMonthStartDate = getMonthStartDate(7);
        System.out.println("7月的第一天是：" + nextMonthStartDate);

        Date monthEndDate = getMonthEndDate(7);
        System.out.println("7月的最后一天是：" + monthEndDate);

        //测试方法6
        int value = 2;
        Date date = calculateDate(firstDate, value);
        System.out.println(firstDate + "过" + value + "天，日期变为:" + date);
        Date date1 = calculateDate(lastDate, value);
        System.out.println(lastDate + "过" + value + "天，日期变为:" + date1);
    }
}
