package com.test.implement;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.test.interfaces.TestDatas;
import com.test.util.ParserXml;

public class TestData implements TestDatas {
	
	private ArrayList<HashMap<String, HashMap<String, String>>> list;

	private Map<String, String> globalMap = new HashMap<String, String>();

	private Map<String, String> commonMap = new HashMap<String, String>();

	private String dataFile;
	
	private void getXmlData() {
		ParserXml p = new ParserXml();
		list = p.parser3Xml(new File("test-data/" + dataFile + ".xml")
				.getAbsolutePath());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).containsKey("common")) {
				commonMap = list.get(i).get("common");
				list.remove(i);
			}
		}
	}

	private void getGlobalXmlData() {
		ParserXml p = new ParserXml();
		globalMap = p.parser2Xml(new File("test-data/Global.xml")
				.getAbsolutePath());
	}

	private ArrayList<Map<String, String>> getData(Method method) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, HashMap<String, String>> methods = list.get(i);
			if (methods.containsKey(method.getName())) {
				Map<String, String> dm = methods.get(method.getName());
				result.add(dm);
				if (!globalMap.isEmpty()) {
					Object[] gar = globalMap.keySet().toArray();
					for (int n = 0; n < gar.length; n++) {
						if (!commonMap.containsKey(gar[n].toString())) {
							commonMap.put((String) gar[n],
									globalMap.get(gar[n]));
						}
					}
				}
				if (!commonMap.isEmpty()) {
					Object[] ar = commonMap.keySet().toArray();
					for (int j = 0; j < ar.length; j++) {
						if (!dm.containsKey(ar[j].toString())) {
							dm.put((String) ar[j],
									(String) commonMap.get(ar[j].toString()));
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public ArrayList<Map<String, String>> getTestData(String className, Method method) {
		dataFile = className;
		this.getXmlData();
		this.getGlobalXmlData();		
		return this.getData(method);
	}
	
}
