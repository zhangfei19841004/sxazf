package com.test.interfaces;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public interface TestDatas {
	
	public ArrayList<Map<String, String>> getTestData(String className, Method method);
	
}
