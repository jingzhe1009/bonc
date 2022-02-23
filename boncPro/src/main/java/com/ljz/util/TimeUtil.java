package com.ljz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ljz.constant.BoncConstant;

public class TimeUtil {
	
	public static java.sql.Date getTomorrow() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        return new java.sql.Date(calendar.getTime().getTime());
    }
	
	public static Date getTw() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(calendar.getTime());
        return StringtoDate(format);
    }
	
	public static java.sql.Date getToday(){
		return new java.sql.Date(new Date().getTime());
	}
	
	public static Date getTy(){
		return new Date();
	}
	
	public static java.sql.Date getEdate() {
		return ExcelUtil.getInstance().StringToDate(BoncConstant.CON_E_DATE);
	}
	
	public static void main(String[] args) {
		System.out.println(getTw());
	}
	
	public static Date getE(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = simpleDateFormat.parse(BoncConstant.CON_E_DATE);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}
	
	public static Date StringtoDate(String str){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = simpleDateFormat.parse(str);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

}
