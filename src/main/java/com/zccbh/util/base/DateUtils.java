package com.zccbh.util.base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 * Function List:<br>
 * 常用的时间操作
 *
 * @author
 * @version Revision:1.0 Date: 2017-11-14 上午09:59:59
 */
public class DateUtils {

	public static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_HM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_ = "yyyy-MM-dd";
	public static final String FORMAT_DATE_CN = "yyyy年MM月dd日";
	public static final String FORMAT_DATE = "yyyy年MM月";
	public static final String FORMAT_DATE_CN1 = "yyyy年MM月dd日 HH:mm:ss";
	public static final String FORMAT_FULL_CN = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String FORMAT_YMD = "yyyyMMdd";
	public static final String FORMAT_YMD_HM = "HH:mm";
	private static final String FORMAT_TIME ="HH:mm:ss";
	private static final String FORMAT_TIME1 ="HH";
	private static final String FORMAT_DATETIME ="yyyyMMddHHmmss";

	/**
	 * 计算两个日期之间的差距天数
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static int cutTwoDateToDay(Date a, Date b) {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_FULL);
		Calendar calendar = Calendar.getInstance();
		int theday = 0;
		try {
			Date beginDate = format.parse(format.format(a));
			Date endDate = format.parse(format.format(b));

			calendar.setTime(beginDate);
			long begin = calendar.getTimeInMillis();
			calendar.setTime(endDate);
			long end = calendar.getTimeInMillis();

			if (((end - begin) % (86400000))==0) theday = (int) ((end - begin) / (86400000));
			else theday = (int) ((end - begin) / (86400000))+1;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return theday;
	}

	/**
	 * 计算两个日期之间的差距天数(不满一天直接舍去)
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static int cutTwoDateToDayInteger(Date a, Date b) {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_FULL);
		Calendar calendar = Calendar.getInstance();
		int theday = 0;
		try {
			Date beginDate = format.parse(format.format(a));
			Date endDate = format.parse(format.format(b));

			calendar.setTime(beginDate);
			long begin = calendar.getTimeInMillis();
			calendar.setTime(endDate);
			long end = calendar.getTimeInMillis();
			theday = (int) ((end - begin) / (86400000));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return theday;
	}


	/**
	 * 计算两个日期之间的差距秒数
	 *
	 * @param b
	 * @return
	 */
	public static int cutTwoDateToSeconds( Date b) {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_FULL);
		Calendar calendar = Calendar.getInstance();
		int theday = 0;
		try {
			Date endDate = format.parse(format.format(b));
			long begin = calendar.getTimeInMillis();
			calendar.setTime(endDate);
			long end = calendar.getTimeInMillis();
			theday = (int)((begin-end)/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return theday;
	}

	/**
	 * 将数字型的时间转为字符串（80 -> 01:20）
	 *
	 * @param time
	 * @return
	 */
	public static String intToTimeString(int time) {
		String hour = String.valueOf(time / 60);
		String minute = String.valueOf(time - time / 60 * 60);

		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		return hour + ":" + minute;
	}

	/**
	 * 取出两个时间出较大的时间
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Date MaxDate(Date a, Date b) {
		if (a.before(b)) {
			return b;
		} else {
			return a;
		}
	}

	/**
	 * 取出两个时间出较大的时间
	 *
	 * @param a  token有效期
	 * @param b  当前时间
	 * @return
	 */
	public static boolean booleanToken(Date a, Date b) {
		return a.getTime()>b.getTime()?true:false;
	}

	/**
	 * 取出两个时间出较小的时间
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Date MinDate(Date a, Date b) {
		if (a.before(b)) {
			return a;
		} else {
			return b;
		}
	}

	/**
	 * 计算给定日期是星期几
	 *
	 * @param date
	 * @return
	 */
	public static int getWeekOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w == 0)
			w = 7;
		return w;
	}

	/**
	 * 格式化日期
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 格式化日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getFormatDate(Date date){
		try {
			if (date == null) {
				return null;
			}
			DateFormat dateFormat = new SimpleDateFormat(FORMAT_FULL);
			Date nowDate = dateFormat.parse(dateFormat.format(date));
			return nowDate;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取传入时间的一年后时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getYearDate(Date date){
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, +1);
			date = calendar.getTime();
			return getFormatDate(date);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取传入时间的某一天时间
	 * @param date
	 * @param operation  加减天数
	 * @param dayNumber		天数
	 * @return
	 */
	public static Date getDayDate(Date date,String operation,Integer dayNumber){
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if ("-".equals(operation))
				calendar.add(Calendar.DAY_OF_YEAR, -dayNumber);
			else
				calendar.add(Calendar.DAY_OF_YEAR, +dayNumber);
			date = calendar.getTime();
			return DateUtils.getFormatDate(date);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 标准化格式化日期时间
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_FULL);
		return format.format(date);
	}

	/**
	 * 标准化格式化日期时间
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate1(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
		return format.format(date);
	}

	/**
	 * 根据对象格式化日期字符串
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static String formatDateFromObject(Object obj, String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		if(obj == null || obj.toString() == ""){
			return "";
		}
		else if(obj instanceof Date){
			return format.format((Date)obj);
		}
		else if(obj instanceof String){
			return format.format(createDate(obj.toString(), pattern));
		}
		return null;
	}

	/**
	 * 根据给定日期字符串和日期格式 创建日期
	 *
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static Date createDateWithUnknowFormat(String dateString) {
		Date result = null;
		String pattern = FORMAT_DATE_CN;
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			result = format.parse(dateString);
		} catch (ParseException e) {
			try {
				pattern = "yyyy/M/d";
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				result = format.parse(dateString);
			} catch (ParseException e1) {
				try {
					pattern = "yyyy-MM-dd";
					SimpleDateFormat format = new SimpleDateFormat(pattern);
					result = format.parse(dateString);
				} catch (ParseException e2) {

				}
			}
		}
		return result;
	}

	/**
	 * 根据给定日期字符串和日期格式 创建日期
	 *
	 * @param dateString
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date createDate(String dateString, String pattern) {
		Date result = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			result = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date createDate(String dateString) {
		return createDate(dateString, FORMAT_FULL);
	}


	public static Date addDay(Date date, int n) {
		return  org.apache.commons.lang3.time.DateUtils.addDays(date, n);
	}


	/**
	 * 获取日期的前后天数
	 * @param oldDate 日期格式 yyyy-MM-dd
	 * @param num 负数代表前几天  正数代表后几天
	 * @return yyyy-MM-dd
	 * @throws ParseException
	 */
	public static Date getBeforeOrAfterDay(Date oldDate, Integer num) {
		try {
			if(oldDate != null && num != null){
				DateFormat dateFormat = new SimpleDateFormat(FORMAT_);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(oldDate);
				calendar.add(Calendar.DATE, num);
				String month = String.valueOf(calendar.get(Calendar.MONTH));
				String day = String.valueOf(calendar.get(Calendar.DATE));
				if(calendar.get(Calendar.MONTH) < 10){
					month = "0"+String.valueOf(calendar.get(Calendar.MONTH)+1);
				}
				if(calendar.get(Calendar.DATE) < 10){
					day = "0"+String.valueOf(calendar.get(Calendar.DATE));
				}
				String newDate = String.valueOf(calendar.get(Calendar.YEAR))+"-"+month+"-"+day;
				return dateFormat.parse(newDate);
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前日期
	 * @return yyyy-MM-dd
	 */
	public static Date getDate(){
		try {
			DateFormat dateFormat = new SimpleDateFormat(FORMAT_);
			Date nowDate = dateFormat.parse(dateFormat.format(new Date()));
			return nowDate;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前日期
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static Date getDateTime(){
		try {
			DateFormat dateFormat = new SimpleDateFormat(FORMAT_FULL);
			Date nowDate = dateFormat.parse(dateFormat.format(new Date()));
			return nowDate;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前日期字符串
	 * @return
	 */
	public static String getDateString(){
		try {
			return formatDate(getDate(), FORMAT_);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前日期字符串
	 * @return
	 */
	public static String getDateTimeString(){
		try {
			return formatDate(getDate(), FORMAT_FULL);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前日期字符串
	 * @return
	 */
	public static String getDateTimeStr(){
		try {
			return formatDate(getDate(), FORMAT_YMD);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 计算当前日期距新日期天数
	 * @param newDate
	 * @return
	 */
	public static int countDistanceDate(Date newDate) {
		try {
			if(newDate != null){
				DateFormat dateFormat = new SimpleDateFormat(FORMAT_);
				Date nowDate = dateFormat.parse(dateFormat.format(new Date()));
				int day = cutTwoDateToDay(nowDate, newDate);
				return day;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 *
	 * @return  字符串的保险开始时间
	 */
	public static String getStringStartTime() {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH));
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 1);
		calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
				00, 00, 00);
		date = calendar.getTime();
		return new SimpleDateFormat(FORMAT_FULL).format(date);
	}

	/**
	 *
	 * @return 字符串的保险结束时间
	 */
	public static String getStringEndTime() {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		String year = String.valueOf(calendar.get(Calendar.YEAR)+1);
		String month = String.valueOf(calendar.get(Calendar.MONTH));
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
				23, 59, 59);
		date = calendar.getTime();
		return new SimpleDateFormat(FORMAT_FULL).format(date);
	}

	/**
	 *
	 * @return 计算保险报价结束时间
	 */
	public static String getStringvalidity() {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH));
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
				23, 59, 59);
		date = calendar.getTime();
		return new SimpleDateFormat(FORMAT_FULL).format(date);
	}

	/**
	 *
	 * @return 传个时间进来计算报价有效期
	 */
	public static String getStringQuoteValidityPeriod(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat(FORMAT_FULL);
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String month = String.valueOf(calendar.get(Calendar.MONTH));
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
					23, 59, 59);
			date = calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat(FORMAT_FULL).format(date);
	}


	/**
	 *
	 * @param
	 * @return      返回日期格式
	 * @throws ParseException
	 */
	public  static Date getDateTime(String sProvince) {
		String[] split = sProvince.split("\\u002B");
		sProvince=split[0]+" "+split[1];
		Date date=null;
		try {
			date = new SimpleDateFormat(FORMAT_FULL).parse(sProvince);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 *
	 * @return 返回当前时间 格式为yyyy-MM-dd
	 */
	public  static String getStringDate(Date date) {
		return new SimpleDateFormat(FORMAT_).format(date);
	}

	/**
	 *
	 * @return 返回当前时间 格式为HH:mm:ss
	 */
	public  static String getStringTime(Date date) {
		return new SimpleDateFormat(FORMAT_TIME).format(date);
	}

	/**
	 *
	 * @return 返回当前时间 格式为yyyy-MM-dd HH:mm:ss
	 */
	public  static String getStringDateTime(Date date) {
		return new SimpleDateFormat(FORMAT_FULL).format(date);
	}

	/**
	 *
	 * @return 返回当前时间 格式为yyyy-MM-dd HH:mm:ss
	 */
	public  static String getStringDateTime(Date date,String  format ) {
		return new SimpleDateFormat(format).format(date);
	}
	/**
	 *
	 * @return 返回当前时间 格式为yyyyMMddHHmmss
	 */
	public  static String getStringDateAndTime(Date date) {
		return new SimpleDateFormat(FORMAT_DATETIME).format(date);
	}

	/**
	 *
	 * @return 返回当前时间 格式为yyyy-MM-dd HH:mm:ss
	 */
	public  static Date getDateTimeByString(String date) {
		try {
			return new SimpleDateFormat(FORMAT_FULL).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}	/**
	 *
	 * @return 返回当前时间 格式为HH
	 */
	public  static String getDateHHByString(String date) {
		try {
			Date parse = new SimpleDateFormat(FORMAT_FULL).parse(date);
			String format = new SimpleDateFormat(FORMAT_TIME1).format(parse);
			String substring = format.substring(0,1);
			if (substring.equals("0")) {
				return format.substring(1);
			}
			return format;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *
	 * @return 返回当前时间 格式为yyyy-MM-dd
	 */
	public  static Map<String,String> getDateMap(long ab) {
		long curren = System.currentTimeMillis();
		curren += ab;
		Date date = new Date(curren);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		Integer day = calendar.get(Calendar.DAY_OF_MONTH);
		Integer month = calendar.get(Calendar.MONTH) + 1;
		Integer year = calendar.get(Calendar.YEAR);
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("year",year.toString());
		resultMap.put("month",month.toString());
		resultMap.put("day",day.toString());
		resultMap.put("hour",hour.toString());
		resultMap.put("minute",minute.toString());
		resultMap.put("second",second.toString());
		return resultMap;
	}
	
	/**
	 * @return 返回指定时间后的ab时间
	 */
	public  static Map<String,String> getDateTime(String createAt,long ab) {
		//当前时间戳
		long curren = System.currentTimeMillis();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取已过去时长
		long a=0;
		try {
			a = curren - dateFormat.parse(createAt).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long curren1 =curren + ab;
		long curren2 = curren1 - a;
		
		Date date = new Date(curren2);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		Integer day = calendar.get(Calendar.DAY_OF_MONTH);
		Integer month = calendar.get(Calendar.MONTH) + 1;
		Integer year = calendar.get(Calendar.YEAR);
		Map<String,String> resultMap = new HashMap<>();
		resultMap.put("year",year.toString());
		resultMap.put("month",month.toString());
		resultMap.put("day",day.toString());
		resultMap.put("hour",hour.toString());
		resultMap.put("minute",minute.toString());
		resultMap.put("second",second.toString());
		return resultMap;
	}

	/**
	 *
	 * @return 返回下一个月、当前时间  格式为yyyy-MM-dd HH:mm:ss
	 */
	public  static Date getTokenTime() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime localDateTime = now.plusDays(CommonField.TOKEN_VALIDITY_PERIOD);
		String t = localDateTime.toString().replace("T", " ");
		String substring1 = t.substring(0, t.indexOf("."));
		return DateUtils.getDateTimeByString(substring1);
	}

	/**
	 *
	 * @return 传个时间进来计算是否过期
	 */
	public static Boolean isOverdue(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat(FORMAT_FULL);
		Date date = null;
		Date date1 = new Date();
		try {
			date = dateFormat.parse(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String month = String.valueOf(calendar.get(Calendar.MONTH));
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
					23, 59, 59);
			date = calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (date1.before(date))?true:false;
	}
}