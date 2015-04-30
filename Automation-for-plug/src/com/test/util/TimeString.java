package com.test.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeString {

	private String valueOfString(String str, int len) {
		String string = "";
		for (int i = 0; i < len - str.length(); i++) {
			string = string + "0";
		}
		return (str.length() == len) ? (str) : (string + str);
	}
	
	public String getSimpleDateFormat(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date()); 		 
	}	

	public String getTimeString() {
		Calendar calendar = new GregorianCalendar();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = this.valueOfString(String.valueOf(calendar.get(Calendar.MONTH) + 1),2);	
		String day = this.valueOfString(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),2);
		String hour = this.valueOfString(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),2);
		String minute = this.valueOfString(String.valueOf(calendar.get(Calendar.MINUTE)),2);
		String second = this.valueOfString(String.valueOf(calendar.get(Calendar.SECOND)),2);
		String millisecond = this.valueOfString(String.valueOf(calendar.get(Calendar.MILLISECOND)),3);
		return year+month+day+hour+minute+second+millisecond;
	}	
}
