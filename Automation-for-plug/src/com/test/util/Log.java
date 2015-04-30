package com.test.util;

import java.util.ArrayList;
import java.util.List;

import org.testng.Reporter;

public class Log {

	public static int step = 0;

	public static List<String> log = new ArrayList<String>();

	public static void comment(String logStr) {
		Log4j.logInfo(logStr);
		String dateInfo = new TimeString().getSimpleDateFormat();
		Reporter.log("<font color=Blue>[" + dateInfo + "] " + logStr
				+ "</font>", false);
	}

	public static void commentStep(String logStr) {
		Log4j.logInfo("STEP " + String.valueOf(++step) + ": " + logStr);
		String dateInfo = new TimeString().getSimpleDateFormat();
		Reporter.log("<font color=Blue>[" + dateInfo + "] " + logStr
				+ "</font>", false);
	}

	private static String comment(String time, String logStr, boolean screen) {
		Log4j.logInfo(logStr);
		if (screen)
			logStr = "<a href=\"../snapshot/" + logStr + "\" "
					+ "target=\"_blank\" style=\"color:red\">" + logStr
					+ "</a>";
		logStr = "<font color=Blue> " + logStr + "</font>";
		Reporter.log(logStr, false);
		return logStr;
	}

	public static void comment(String logStr, boolean screen) {
		String dateInfo = new TimeString().getSimpleDateFormat();
		comment(dateInfo, logStr, screen);
	}

	public static void comment(String logStr, boolean screen, boolean failed) {
		String dateInfo = new TimeString().getSimpleDateFormat();
		logStr = comment(dateInfo, logStr, screen);
		if (failed)
			log.add("<font color=Blue>[" + dateInfo + "] " + logStr + "</font>");
	}
}
