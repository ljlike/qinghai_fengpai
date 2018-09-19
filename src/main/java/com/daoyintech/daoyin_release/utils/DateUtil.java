package com.daoyintech.daoyin_release.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 * @created by WeiJie on 2016年11月24日
 */
public class DateUtil {

    /** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    private static final String dtLong = "yyyyMMddHHmmss";
    
    /** 完整时间 yyyy-MM-dd HH:mm:ss */
    public static final String simple = "yyyy-MM-dd HH:mm:ss";

	private static final String simples = "yyyy/MM/dd HH:mm";
    /** 年月日(无下划线) yyyyMMdd */
    private static final String dtShortNoLine = "yyyyMMdd";
    /** 年月日 yyyy-MM-dd */
    private static final String dtShort = "yyyy-MM-dd";
    /** 年月日 yyyy年MM月dd日 */
    private static final String dtShortNoLineInCn = "yyyy年MM月dd日";

	public static final String parttern_yyyyImage = "yyyyMMddHHmmssSSS";

    /**
     * 将时间戳转换成java.util.Date
     * @param time 时间戳
     * @return
     * @throws ParseException
     */
    public static Date longParseDate(Long time) {
    	Date date=new Date(time);
		return date;
    }

    /**
     * 将时间字符串（yyyy-MM-dd HH:mm:ss）转换成时间戳
     * @return
     * @throws ParseException
     */
	//时间转时间戳
	public static String DateToStr(Date dateT, String fromat) {
		SimpleDateFormat sdf = new SimpleDateFormat(fromat);
		String str = sdf.format(dateT);
		return str;
	}

	/**
	 * date转时间字符串（yyyy-MM-dd）
	 * @param date
	 * @return
	 */
	public static String dateToDsStr(Date date){
		return new SimpleDateFormat(dtShort).format(date);
	}

    /**
     * 将时间字符串（yyyy-MM-dd）转为sqlDate
     * @param strDate
     * @return
     */
    public static java.sql.Date getSqlDateByStr(String strDate){
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(dtShort);
        try {
            Date date = bartDateFormat.parse(strDate);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
		return null;
    }

	/**
	 * 获取当前时间字符串（yyyy-MM-dd HH:mm:ss）
	 * @return
	 * @描述
	 */
	public static String getNowDateString(){
		Date now = new Date();
		//可以方便地修改日期格式
		SimpleDateFormat dateFormat = new SimpleDateFormat(simple);
		String nowTime = dateFormat.format( now );
		return nowTime;
	}
    

	/**
	 * 将java.util.Date转为（yyyy-MM-dd HH:mm:ss）时间字符串
	 * @param date
	 * @return
	 */
	public static String getDateStrByDate(Date date){
		//可以方便地修改日期格式
		SimpleDateFormat dateFormat = new SimpleDateFormat(simple);
		String newTime = dateFormat.format( date );
		return newTime;
	}
	
	
    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
	public static String getOrderNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtLong);
		return df.format(date);
	}
	/**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(simple);
		return df.format(date);
	}
	
	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
	 * @return
	 */
	public static String getDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShortNoLine);
		return df.format(date);
	}

	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyy年MM月dd日
	 * @return
	 */
	public static String get_CNyyyyMMddDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShortNoLineInCn);
		return df.format(date);
	}

	/**
	 * 时间字符串（yyyy-MM-dd HH:mm:ss）转date
	 * @param dateStr
	 * @return
	 */
	public static Date strToDate(String dateStr){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(simple);
	    try {
	        date = df.parse(dateStr);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return date;
	}

	/**
	 * 比较两个时间
	 * @param data1
	 * @param date2
	 * @return
	 */
	public static boolean  lgortg(String data1,String date2){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(data1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return false;
			}
			return true;
		}catch (Exception e) {
		}
		return false;
	}

	/**
	 * 字符串（yyyy-MM-dd HH:mm:ss）是转成时间
	 * @param dateStr
	 * @return
	 * @throws ParseException 
	 */
	public static Date SimpleStrToDate(String dateStr) throws ParseException{
		Date date = new Date();
		DateFormat df=new SimpleDateFormat(simple);
		date = df.parse(dateStr);
		return date;
	}
    /**
     * 时间字符串（yyyy-MM-dd）转date
     * @param dstr
     * @return
     * @throws ParseException
     */
    public static Date dtShortStrToDate(String dstr) throws ParseException {
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShort);
		date=df.parse(dstr);
		return date;
    }

	//获取当前秒的偏移量
	public static Date getOffsetSecond(Date date,int offsetSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, offsetSecond);
		return calendar.getTime();
	}

	//获取当前时间的偏移量
	public static Date getTimeOffset(int timeOffset,int times) throws ParseException {
		Calendar endPay_dt = Calendar.getInstance();
		endPay_dt.add(timeOffset, times);
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = DateUtil.SimpleStrToDate(sim.format(endPay_dt.getTime()));
		return time;
	}

	public static Date getYesterday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}

	public static Date getLastDayStartTime(){
		Date date=getYesterday();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		//	calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getLastDayEndTime(){
		Date date=getYesterday();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		//	calendar.set(Calendar.MILLISECOND,999);
		return calendar.getTime();
	}

	public static Date getFirstDayByMonth(){
		Calendar   calendar=Calendar.getInstance();//获取当前日期
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		return calendar.getTime();
	}

	public static Integer getMonth(Date currentTime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentTime);
		return calendar.get(Calendar.MONTH)+1;
	}

	public static Date getCurrentMonthFristDay(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		//获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());
		Date date = null;
		try {
			date = format.parse(first);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getCurrentMonthLastDay(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		//获取当前月第一天：
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		Date date = null;
		try {
			date = format.parse(last);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/*
        获取系统时间的前一天
     */
	public static Date getNBeforeDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}


	public static Date getTodayStartTime() {
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		return todayStart.getTime();
	}

	public static Date getTodayEndTime(){
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		return todayEnd.getTime();
	}

	public static String dateToTimestampStr(Date date){
		DateFormat sdf = new SimpleDateFormat(simple);
		String dataStr = sdf.format(date);
		return dataStr;
	}

	public static String dateToStr(Date date){
		DateFormat sdf = new SimpleDateFormat(simple);
		String dataStr = sdf.format(date);
		return dataStr;
	}

	public static String dateStr(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}

	/*
		判断是否为周末
	 */
	public static boolean isWeekend(){
		Calendar cal = Calendar.getInstance();
		int week=cal.get(Calendar.DAY_OF_WEEK)-1;

		if(week ==6 || week==0){//0代表周日，6代表周六
			return true;
		}
		return false;
	}

	public static Date tomorrow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	/**
	 * 计算离rabbitmq执行时间
	 * @param week//周几，int表示（周日为7）
	 */
	public static Long convertWeekDate(int week) {

		Date time = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"); //设置时间格式

		Calendar cal = Calendar.getInstance();

		cal.setTime(time);

		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了

		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天

		if (1 == dayWeek) {

			cal.add(Calendar.DAY_OF_MONTH, -1);

		}

		System.out.println("要计算日期为:" + sdf.format(cal.getTime())); //输出要计算日期

		cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

		int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		if (day > week +1){
			cal.add(Calendar.DAY_OF_MONTH,7);
		}

		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = sdf.format(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH,week-1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);//设置为0时0分0秒
		String imptimeBegin2 = sdf.format(cal.getTime());
		System.out.println("imptimeBegin===="+ imptimeBegin2);
		long dateGap = cal.getTime().getTime() - new Date().getTime();
		if (dateGap <=0){
			dateGap = 1000 * 10;
		}
		System.out.println("dateGap====" + dateGap);
		return dateGap;
	}
}