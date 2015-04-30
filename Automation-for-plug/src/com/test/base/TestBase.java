package com.test.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.test.control.ObjectManager;
import com.test.implement.TestData;
import com.test.interfaces.TestDatas;
import com.test.testcase.TestUI;
import com.test.util.GenerateTestdata;
import com.test.util.Log;

public class TestBase {

	public String testCase;

	private TestDatas td;

	@Parameters({ "testCase" })
	@BeforeClass(alwaysRun = true)
	public void getTestCaseName(String testCase) {
		this.testCase = testCase;
		this.getTestData();
	}

	@BeforeTest(alwaysRun = true)
	public void setUp() {
		 DriverManager.getDriver();
	}

	@AfterTest(alwaysRun = true)
	public void tearDown() {
		if (DriverManager.getDriver() != null) {
			DriverManager.getDriver().close();
			DriverManager.getDriver().quit();
		}
		DriverManager.threadDriver.set(null);
		ObjectManager.initialObject();
		Log.step = 0;
	}

	private void getTestData() {
		GenerateTestdata gt = new GenerateTestdata();
		gt.generateTestdata(testCase, TestUI.class);
		try {
			if (!Config.getConfig("testData").equals(""))
				td = (TestDatas) Class.forName(Config.getConfig("testData"))
						.newInstance();
			else
				td = new TestData();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@DataProvider
	public Object[][] dataProvider(Method method) {
		ArrayList<Map<String, String>> result = td
				.getTestData(testCase, method);
		Object[][] files = new Object[result.size()][];
		for (int i = 0; i < result.size(); i++) {
			files[i] = new Object[] { result.get(i) };
		}
		return files;
	}

}
