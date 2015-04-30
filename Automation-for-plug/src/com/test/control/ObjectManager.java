package com.test.control;

import java.util.HashMap;

public class ObjectManager {

	private static HashMap<String, Object> objectMap;

	public static HashMap<String, Object> objectMap() throws DefinedException {
		if (objectMap == null) {
			objectMap = new HashMap<String, Object>();			
		}
		return objectMap;
	}
	
	public static void initialObject(){
		objectMap = null;
	}
}
