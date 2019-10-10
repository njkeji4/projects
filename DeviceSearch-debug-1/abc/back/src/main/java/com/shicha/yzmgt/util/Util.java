package com.shicha.yzmgt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;



public class Util {

	public static String formatDate() {		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(System.currentTimeMillis());
	}
	public static String formatDate2() {		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
		
		return sdf.format(System.currentTimeMillis());
	}
	
	public static String formatDate(long datetime) {		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(new Date(datetime));
	}
	
	public static long getPreviousDay(long time) {
		return getDay(getDay(time) - 1);
	}
	public static long getDay(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);	
		
		return c.getTimeInMillis();
	}
	
	public static long getWeek(long time) {
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getDay(time));
		c.set(Calendar.DAY_OF_WEEK, 1);
		
		return c.getTimeInMillis();
	}
	
	public static long getMonth(long time) {		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getDay(time));		
		c.set(Calendar.DAY_OF_MONTH, 1);		
		
		return c.getTimeInMillis();
		
	}
	
	public static long begin() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	public static long end() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}

	
	
	public static long parseStringDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		
		try {
			return sdf.parse(date).getTime();
		} catch (ParseException e) {
			
			return 0;
		}
	}
	
	public static String get32Id() {
		String uuid = UUID.randomUUID().toString();		
		uuid = uuid.replace("-", "");
		return uuid;
	}
	
	public static String UUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
	
	public static void base64decode(String code) {
		
		Base64 decoder = new Base64();
		
	}
	
}
