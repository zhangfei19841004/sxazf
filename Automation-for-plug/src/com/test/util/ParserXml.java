package com.test.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParserXml {

	public ArrayList<HashMap<String, HashMap<String, String>>> parser3Xml(
			String fileName) {
		File inputXml = new File(fileName);
		ArrayList<HashMap<String, HashMap<String, String>>> list = new ArrayList<HashMap<String, HashMap<String, String>>>();
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element employees = document.getRootElement();
			for (Iterator<?> i = employees.elementIterator(); i.hasNext();) {
				Element employee = (Element) i.next();
				HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
				HashMap<String, String> tempMap = new HashMap<String, String>();
				for (Iterator<?> j = employee.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					tempMap.put(node.getName(), node.getText());
				}
				map.put(employee.getName(), tempMap);
				list.add(map);
			}
		} catch (DocumentException e) {
			Log.comment(e.getMessage());
		}
		return list;
	}

	public HashMap<String, String> parser2Xml(String fileName) {
		HashMap<String, String> configMap = null;
		try {
			File inputXml = new File(fileName);
			if (inputXml.exists()) {
				configMap = new HashMap<String, String>();
				SAXReader saxReader = new SAXReader();
				Document document = saxReader.read(inputXml);
				Element employees = document.getRootElement();
				for (Iterator<?> i = employees.elementIterator(); i.hasNext();) {
					Element employee = (Element) i.next();
					configMap.put(employee.getName(), employee.getText());
				}
			} else {
				Log.comment("WARNING: file: " + fileName + " is not exist");
			}

		} catch (DocumentException e) {
			Log.comment(e.getMessage());
		}
		return configMap;
	}
}
