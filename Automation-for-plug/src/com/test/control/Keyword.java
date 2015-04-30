package com.test.control;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.test.util.ParserXml;

public class Keyword {

	private static HashMap<String, String> sysKeyword = null;
	
	private static HashMap<String, String> keyword = null;

	private static synchronized void getSysKeywordMap() {
		if (sysKeyword == null) {
			ParserXml p = new ParserXml();
			sysKeyword = p.parser2Xml(new File("config/SystemKeyword.xml").getAbsolutePath());
		}
	}
	
	private static synchronized void getKeywordMap() {
		if (keyword == null) {
			XMLHandler xh = new XMLHandler("config/keyword.xml");
			List<Element> es = xh.getElementObjects("/*/*/*");
			keyword = new HashMap<String, String>();
			for (Element element : es) {
				keyword.put(element.getName(), element.getTextTrim());
			}			
		}
	}

	public static String getSystemKeyword(String key) {
		String value = null;
		if (sysKeyword == null)
			Keyword.getSysKeywordMap();
		if (sysKeyword.containsKey(key))
			value = sysKeyword.get(key);		
		return value;
	}
	
	public static String getKeyword(String key) {
		String value = null;
		if (keyword == null)
			Keyword.getKeywordMap();
		if (keyword.containsKey(key))
			value = keyword.get(key);		
		return value;
	}

}
