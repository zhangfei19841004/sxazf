package com.test.control;

import org.testng.Assert;

import com.test.util.Log;

public class AssertObject {
	
	public void assertEquals(String actual, String expected){
		Assert.assertEquals(actual, expected);
		Log.comment("[Verify Pass]: " + actual);
	}
	
	public void assertEquals(String actual, String expected, String log){
		Assert.assertEquals(actual, expected, log);		
		Log.comment("[Verify Pass]: " + log);
	}
	
}
