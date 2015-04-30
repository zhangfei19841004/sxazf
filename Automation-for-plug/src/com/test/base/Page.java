package com.test.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import com.test.control.DefinedException;
import com.test.control.TestCaseDefined;

public class Page extends LocatorManager{		
	
	private Actions action;
	
	public Page(){
		super();
	}
	
	public Page(String name){
		super(name);
	}		
	
	public String executePageMethod(String[] parameter, String returnValue,
			String linked) throws DefinedException {
		TestCaseDefined tdt = new TestCaseDefined();
		tdt.run(linked, parameter);
		return tdt.getParam().get(returnValue);
	}

	public void executePageMethod(String[] parameter, String linked)
			throws DefinedException {
		TestCaseDefined tdt = new TestCaseDefined();
		tdt.run(linked, parameter);
	}

	public void sleep(String time) {
		try {
			Thread.sleep((Integer.parseInt(time)) * 1000);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void moveToElement(String key) {
		if (action == null)
			action = new Actions(driver);
		action.moveToElement(this.getElement(key)).perform();
	}
	
	public void clickElement(String key) {
		if (action == null)
			action = new Actions(driver);
		action.click(this.getElement(key)).perform();
	}
	
	public void clickElement(String key, String param) {
		if (action == null)
			action = new Actions(driver);
		action.click(this.getElement(key,new String[]{param})).perform();
	}
	
	public void executeScript(String script){
		JavascriptExecutor j =(JavascriptExecutor)driver;
		j.executeScript(script);
	}
	
	public String replaceId(String id){
		return id.replace("itemRow", "btnBook");
	}

}
