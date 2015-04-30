package com.test.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4j {
	private static Logger logger;

	private static String filePath = "config/log4j.properties";	

	private static boolean flag = false;

	private static synchronized void getPropertyFile() {
		logger = Logger.getLogger("TestAutomation");		
		PropertyConfigurator.configure(new File(filePath).getAbsolutePath());
		flag = true;
	}

	private static void getFlag() {
		if (flag == false)
			Log4j.getPropertyFile();
	}

	public static void logInfo(String message) {
		Log4j.getFlag();
		logger.info(message);		
	}

	public static void logError(String message) {
		Log4j.getFlag();
		logger.error(message);
	}

	public static void logWarn(String message) {
		Log4j.getFlag();
		logger.warn(message);
	}
}
