package com.test.base;

import java.io.File;
import java.util.HashMap;

import com.test.util.Log;
import com.test.util.ParserXml;

public class Config {

	private static HashMap<String, String> configMap = null;

	private static synchronized void getConfigMap() {
		if (configMap == null) {
			ParserXml p = new ParserXml();
			configMap = p.parser2Xml(new File("config/config.xml")
					.getAbsolutePath());
		}
	}

	public static String getConfig(String key) {
		String value = null;
		if (configMap == null)
			Config.getConfigMap();
		if (configMap.containsKey(key)) {
			value = configMap.get(key);
		} else
			Log.comment(key + " is not exist in config.xml");
		return value;
	}

}
