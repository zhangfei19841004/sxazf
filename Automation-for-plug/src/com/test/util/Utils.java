package com.test.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	private String dateFormat = "yyyy-MM-dd";

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateToString() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public String getAfterDateToString(String after) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, +Integer.valueOf(after));
		return sdf.format(c.getTime());
	}

	public String getRandom(String count) {
		return String.valueOf((int) Math.round(Math.random()
				* (Integer.valueOf(count) - 1)));
	}

}
