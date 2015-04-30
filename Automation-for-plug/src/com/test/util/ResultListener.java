package com.test.util;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class ResultListener extends TestListenerAdapter {	

	public void onTestFailure(ITestResult tr) {		
		TakeScreenshot ts = new TakeScreenshot();
		ts.takeScreenshot();
	}

}
