package com.daoyintech.daoyin_release.utils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class WebUtil {

	public static int[] getParms(String ids){
		String[] parms =  ids.split(",");
		int[] idss =new int[parms.length];
		for(int i = 0;i<parms.length;i++){
			idss[0] = Integer.parseInt(parms[0]);
		}
		return idss;
	}

	public static Map<String,Object> getMap(String[] keys,Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0, len = keys.length; i < len; i++) {
				map.put(keys[i], values[i]);
			}
			return map;
		}
		return null;
	}

	public static Map<String,Object> generate(String[] keys, Object[] values) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0, len = keys.length; i < len; i++) {
			map.put(keys[i], values[i]);
		}
		return map;
	}

	/**
	 * 删除本地文件
	 * Qing W
	 */
	public static boolean deleteFile(String url){
		File file = new File(url);
		return file.delete();
	}

	public static int dayBetweenDates(Date beginDate,Date endDate){
		long intervalMill = endDate.getTime() - beginDate.getTime();

		return (int)(intervalMill/(24*60*60*1000));
	}

	public static List<Date> getDatesSinceTheDate(Date date){
		int days = dayBetweenDates(date,new Date());
		List<Date> dates = new ArrayList();
		for(int i = 1; i <= days; i++){
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.DATE,i);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				continue;
			}
			dates.add(cal.getTime());
		}
		return dates;
	}








	//日期转换
	public static Boolean IsDate(String dateTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sdf.parse(dateTime);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static Date parseDate(String datetime,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}






	public static Date lastDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}

	public static void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	public static void addToZipFile(String fileName, ZipOutputStream zos,String destName) throws IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(destName);
		zos.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	public static String getExtName(String fileName){
		String[] args = fileName.split("\\.");
		return args[1];
	}






	public static String noceStr(){
		String str = String.valueOf(new Date().getTime());

		return randomStr() + str;
	}

	/**
	 * 生成六位随机数
	 * @return
	 */

	public static String randomStr(){
		String randomcode = "";
		String model = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] m = model.toCharArray();
		for (int j = 0; j < 6; j++) {
			char c = m[(int) (Math.random() * 36)];
			// 保证六位随机数之间没有重复的
			if (randomcode.contains(String.valueOf(c))) {
				j--;
				continue;
			}
			randomcode = randomcode + c;
		}
		return randomcode;
	}

	/**
	 * 生成1位1到4的随机数
	 * @return
     */



	public static String savefile(InputStream file,String allpath,String context,String name){
		String imgname=new Date().getTime()+name;
		try {
			File destFile = new File(context+"/upload/images/"+imgname);
			FileOutputStream out = new FileOutputStream(destFile);
			int len=0;
			byte buffer[] = new byte[1024];
			while((len=file.read(buffer))>0){
				out.write(buffer, 0, len);
			}
			out.close();
			return allpath+""+imgname;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean compareDateTimeFromNow(Long timestamp){
		long count = timestamp - System.currentTimeMillis();
		if(count > 0){
			return true;
		}else{
			return false;
		}
	}

	public static String initUserInviteCode(){
		String base = "0123456789";
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<6;i++){
			int number = ThreadLocalRandom.current().nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String fileToString(File file) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {

			reader = new InputStreamReader(new FileInputStream(file));

			//将输入流写入输出流
			char[] buffer = new char[100];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		//返回转换结果
		if (writer != null)
			return writer.toString();
		else return null;
	}
}
