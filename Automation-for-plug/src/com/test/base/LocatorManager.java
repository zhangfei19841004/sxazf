package com.test.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Set;

import org.ho.yaml.Yaml;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.implement.Locator;
import com.test.interfaces.Locators;
import com.test.util.Log;

public class LocatorManager {
	
	public WebDriver driver;	
	
	private String name;

	private Locators locator;
	
	private HashMap<String, HashMap<String, String>> ml;

	private HashMap<String, HashMap<String, String>> extendLocator;
	
	public LocatorManager(){
		this.driver = DriverManager.getDriver();
		this.name = this.getClass().getSimpleName();
	}	

	public LocatorManager(String name) {		
		this.driver = DriverManager.getDriver();
		this.name = name;
	}	

	private void getLocator() {
		try {
			if (!Config.getConfig("locator").equals(""))
				locator = (Locators) Class.forName(Config.getConfig("locator"))
						.newInstance();
			else
				locator = new Locator();
			ml = locator.getLocators(name);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void loadExtendLocator(String fileName) {
		File f = new File("locator/" + fileName + ".yaml");
		try {
			extendLocator = Yaml.loadType(
					new FileInputStream(f.getAbsolutePath()), HashMap.class);
			ml.putAll(extendLocator);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setLocatorVariableValue(String variable, String value) {
		Set<String> keys = ml.keySet();
		for (String key : keys) {
			String v = ml.get(key).get("value")
					.replaceAll("%" + variable + "%", value);
			ml.get(key).put("value", v);
		}
	}

	private String getLocatorString(String locatorString, String[] ss) {
		for (String s : ss) {
			locatorString = locatorString.replaceFirst("%s", s);
		}
		return locatorString;
	}

	private By getBy(String type, String value) {
		By by = null;
		if (type.equals("id")) {
			by = By.id(value);
		}
		if (type.equals("name")) {
			by = By.name(value);
		}
		if (type.equals("xpath")) {
			by = By.xpath(value);
		}
		if (type.equals("className")) {
			by = By.className(value);
		}
		if (type.equals("linkText")) {
			by = By.linkText(value);
		}
		return by;
	}

	private WebElement watiForElement(final By by) {
		WebElement element = null;
		int waitTime = Integer.parseInt(Config.getConfig("waitTime"));
		try {
			element = new WebDriverWait(driver, waitTime)
					.until(new ExpectedCondition<WebElement>() {
						public WebElement apply(WebDriver d) {
							return d.findElement(by);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			Log.comment(by.toString() + " is not exist until " + waitTime);
		}
		return element;
	}

	private boolean waitElementToBeDisplayed(final WebElement element) {
		boolean wait = false;
		if (element == null)
			return wait;
		try {
			wait = new WebDriverWait(driver, Integer.parseInt(Config
					.getConfig("waitTime")))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return element.isDisplayed();
						}
					});
		} catch (Exception e) {
			Log.comment(element.toString() + " is not displayed");
		}
		return wait;
	}

	public boolean waitElementToBeNonDisplayed(final WebElement element) {
		boolean wait = false;
		if (element == null)
			return wait;
		try {
			wait = new WebDriverWait(driver, Integer.parseInt(Config
					.getConfig("waitTime")))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return !element.isDisplayed();
						}
					});
		} catch (Exception e) {
			Log.comment("Locator [" + element.toString()
					+ "] is also displayed");
		}
		return wait;
	}

	private void loadLocator() {
		if (ml == null)
			this.getLocator();
	}

	private WebElement getLocator(String key, String[] replace, boolean wait) {		
		this.loadLocator();
		WebElement element = null;
		if (ml.containsKey(key)) {
			HashMap<String, String> m = ml.get(key);
			String type = m.get("type");
			String value = m.get("value");
			if (replace != null)
				value = this.getLocatorString(value, replace);
			By by = this.getBy(type, value);
			if (wait) {
				element = this.watiForElement(by);
				boolean flag = this.waitElementToBeDisplayed(element);
				if (!flag)
					element = null;
			} else {
				try {
					element = driver.findElement(by);
				} catch (Exception e) {
					element = null;
				}
			}
		} else
			Log.comment("Locator " + key + " is not exist in " + name + ".yaml");
		return element;
	}

	public WebElement getElement(String key) {
		return this.getLocator(key, null, true);
	}

	public WebElement getElementNoWait(String key) {
		return this.getLocator(key, null, false);
	}

	public WebElement getElement(String key, String[] replace) {
		return this.getLocator(key, replace, true);
	}

	public WebElement getElementNoWait(String key, String[] replace) {
		return this.getLocator(key, replace, false);
	}	
	
}
