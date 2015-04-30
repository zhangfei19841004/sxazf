package com.test.widget;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.test.base.DriverManager;
import com.test.control.RegExp;
import com.test.util.Log;

public class Open {
	
	private WebDriver driver;

	private RegExp re;

	private Map<String, String> param;	

	private String description;

	private String replaceDesc;

	public Open() {
		this.driver = DriverManager.getDriver();
		this.re = new RegExp();		
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setReplaceDesc(String replaceDesc) {
		this.replaceDesc = replaceDesc;
	}

	public void open() {
		String url = re.parameterName(description).get(0);
		if (param.containsKey(url)) {
			url = param.get(url);
			replaceDesc = re.replaceOpenUrl(replaceDesc, url);
		}
		Log.commentStep(replaceDesc);
		driver.navigate().to(url);
		driver.manage().window().maximize();
	}

}
