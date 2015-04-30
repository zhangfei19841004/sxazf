package com.test.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {

	public static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>();

	public static WebDriver getDriver() {
		WebDriver driver = DriverManager.threadDriver.get();
		if (driver == null) {
			driver = new FirefoxDriver();
			threadDriver.set(driver);			
		}
		return driver;
	}

}
