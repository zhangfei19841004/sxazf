package com.test.implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.ho.yaml.Yaml;
import com.test.interfaces.Locators;
import com.test.util.Log;

public class Locator implements Locators {

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, HashMap<String, String>> getLocators(String pageName) {
		String filePath = "locator/" + pageName + ".yaml";
		File f = new File(filePath);
		HashMap<String, HashMap<String, String>> ml = new HashMap<String, HashMap<String, String>>();
		if (f.exists()) {
			try {
				ml = Yaml.loadType(new FileInputStream(f.getAbsolutePath()),
						HashMap.class);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			Log.comment("File " + filePath + " is not exist!");
		}
		return ml;
	}

}
