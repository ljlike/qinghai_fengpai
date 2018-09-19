package com.daoyintech.daoyin_release.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: pei
 */
public class DateUtils {

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date longToDate(Long strDate) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= null;
        try {
            String d = format.format(strDate);
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    public static String dateLongToStr(Long strDate) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= null;
        try {
            String d = format.format(strDate);
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  getStringDate(date);
    }



    /**
     * 得到几天后的时间
     * @param nowDate
     * @param day
     * @return
     */
    public static String getDateAfter(Date nowDate,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return getStringDate(now.getTime());
    }

    public static Long getDateLong(Date nowDate,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        Date time = now.getTime();
        return Long.valueOf(time.getTime()/1000);
    }

    public static Long dateLongToLong(Date nowDate,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        Date time = now.getTime();
        return time.getTime();
    }

    public static Long dateStringTypeToLong(String dateType){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(dateType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();
        return timestamp;
    }



    public static Long StringTLong(String StringDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try {
            date = sdf.parse(StringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


}
