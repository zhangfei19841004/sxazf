package com.test.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class GenerateTestdata {
	
	private Document document;
	
	private String root = "data";
	
	private String common = "common";
	
	private String fileDir = "test-data/";
	
	private String fileName = null;
	
	private List<String> methodList;
	
	@SuppressWarnings("rawtypes")
	public Class getClazz(String clazzPath){
		Class clazz = null;
		try {
			clazz = Class.forName(clazzPath);
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		return clazz;
	}
	
	@SuppressWarnings("rawtypes")
	public void getTestMethod(Class clazz){
		Method[] methods = clazz.getMethods();		
		methodList.add(common);
		for(Method method:methods){
			Annotation[] anns = method.getAnnotations();
			for(Annotation ann:anns){
				if(ann!=null && ann.annotationType().getSimpleName().equals("Test")){
					methodList.add(method.getName());
					break;
				}					
			}			
		}
	}
	
	public void createXML(){
		document = DocumentHelper.createDocument();
		Element root = document.addElement(this.root);
		for(String methodName : methodList){
			root.addElement(methodName);
		}
	}
	
	public void saveXML(Document document, String path) {
		XMLWriter writer;
		try {				
			OutputFormat format = OutputFormat.createPrettyPrint();
			writer = new XMLWriter(new FileWriter(new File(path)), format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void generateTestdata(String className, Class clazz){
		fileName = className;
		String path = fileDir+fileName+".xml";
		File file = new File(path);
		if(!file.exists()){
			methodList = new ArrayList<String>();
			this.getTestMethod(clazz);
			this.createXML();
			this.saveXML(document, path);
		}
	}
	
	public static void main(String[] args){
		GenerateTestdata gt = new GenerateTestdata();
		gt.generateTestdata("TestBaidu", gt.getClazz("com.test.testcase.TestUI"));
	}
	
	
}
