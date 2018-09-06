package com.delllogistics.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by IORI on 2017/7/8.
 */
public class DateUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 比较当前时间是否过期
     *
     * @param createTime 开始时间
     * @param expireTime 有效期，单位为分钟
     * @return true 过期
     */
    public static boolean compareDateByExpire(Date createTime, int expireTime) {
        /*
         * 计算过期时间
         */
        Date expireDate = new Date(createTime.getTime() + expireTime * 60 * 1000);
        Date now = new Date();
        /*
         * 比较当前时间和过期时间
         */
        return now.before(expireDate);
    }

    /**
     * 获取当前时间戳
     * @param pattern   pattern
     * @return  a String
     */
    public static String getCurrentDateTimeStr(String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.now().format(dateTimeFormatter);

    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 1);
//        System.out.println(c.getTime());
        return format.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 7);
        return format.format(c.getTime());
    }

    /**
     * 获得本月第一天0点时间
     * @return yyyy-MM-dd
     */
    public static String getFirstDayOfThisMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return format.format(cal.getTime());
    }



    public static String formatDate(Date currentTime, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(currentTime);
    }

    /**
     * 计算日期
     * @param referenceDate 基准日期
     * @param num 变化数目
     * @param calendarField 时间属性,如Calendar.MINUTE
     * @return 目标时间
     */
    public static Date addDate(Date referenceDate, int num, int calendarField){
        Calendar cal = Calendar.getInstance();
        cal.setTime(referenceDate);
        cal.add(calendarField,num);
        return cal.getTime();
    }

    /**
     * 计算变化某个分钟的日期
     * @param referenceDate 基准日期
     * @param num 变化数目
     * @return 目标时间
     */
    public static Date addDateByMinute(Date referenceDate,int num){
        return addDate(referenceDate,num,Calendar.MINUTE);
    }


    public static Date parseDateStr(String dateStr){
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        //本周周一
        System.out.println(getMondayOfThisWeek());
        //得到本周周日
        System.out.println(getSundayOfThisWeek());
        // 获得本月第一天0点时间
        System.out.println(getFirstDayOfThisMonth());

        Date date = parseDateStr("2018-04-02 22:48:56");
        System.out.println(date);
    }

}
