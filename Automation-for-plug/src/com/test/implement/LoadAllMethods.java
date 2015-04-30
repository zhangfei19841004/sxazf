package com.test.implement;

import java.util.HashMap;

import com.test.control.DefinedException;
import com.test.control.XMLHandler;
import com.test.interfaces.Linked;

public class LoadAllMethods implements Linked {

	private XMLHandler xh;

	public LoadAllMethods() {
		xh = new XMLHandler("config/LinkedPages.xml");
	}

	public boolean methodIsExist(String className, String methodName) {		
		String xpath = this.getValidXpath(className, methodName);
		if(xpath == null){
			return false;
		}
		return true;
	}
	
	private String getValidXpath(String className, String methodName){
		if(xh.isExist("//" + className + "/method[@name='" + methodName + "']")){
			return "//" + className + "/method[@name='" + methodName + "']";
		}
		if(xh.isExist("//common/method[@name='" + methodName + "']")){
			return "//common/method[@name='" + methodName + "']";
		}
		return null;
	}

	private HashMap<String, String> getMethodAtrribute(String className,
			String methodName) {		
		return xh.getElementAttributes(this.getValidXpath(className, methodName));
	}

	public boolean returnIsExist(String className, String methodName) {
		return this.getMethodAtrribute(className, methodName).containsKey(
				"return");
	}

	public String getReturn(String className, String methodName) {
		return this.getMethodAtrribute(className, methodName).get("return");
	}

	public String getLinked(String className, String methodName) {
		return this.getMethodAtrribute(className, methodName).get("linked");
	}

	public static void main(String[] args) throws DefinedException {
		LoadAllMethods lm = new LoadAllMethods();
		System.out.println(lm.getLinked("TestBaidu", "search"));
	}

}
