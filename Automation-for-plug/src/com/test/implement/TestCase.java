package com.test.implement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.test.interfaces.TestCases;

public class TestCase implements TestCases {

	@Override
	public ArrayList<String> getTestCase(String testCaseName) {
		ArrayList<String> ml = null;
		File file = new File("test-cases/" + testCaseName + ".zf");
		try {
			String encoding = "UTF-8";
			ml = new ArrayList<String>();
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (!lineTxt.equals(""))
						ml.add(lineTxt);
				}
				read.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ml;
	}
}
