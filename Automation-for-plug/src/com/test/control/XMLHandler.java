package com.test.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLHandler {

	private String filePath;

	private Document document;

	public XMLHandler(String filePath) {
		this.filePath = filePath;
		this.load(this.filePath);
	}

	private void load(String filePath) {
		try {
			if (document == null) {
				File inputXml = new File(filePath);
				if (inputXml.exists()) {
					SAXReader saxReader = new SAXReader();
					document = saxReader.read(inputXml);
				} else
					System.out.println("File does not exists : " + filePath);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private Element getElementObject(String elementPath) {
		return (Element) document.selectSingleNode(elementPath);
	}
	
	@SuppressWarnings("unchecked")
	public List<Element> getElementObjects(String elementPath) {
		return document.selectNodes(elementPath);
	}
	
	public boolean isExist(String elementPath){
		boolean flag = false;
		Element element = this.getElementObject(elementPath);
		if(element != null) flag = true;
		return flag;
	}

	@SuppressWarnings("rawtypes")
	private ArrayList<Attribute> getAttributesOject(String elementPath) {
		Element element = this.getElementObject(elementPath);
		Iterator itAtt = element.attributeIterator();
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		while (itAtt.hasNext()) {
			Attribute attribute = (Attribute) itAtt.next();
			attributes.add(attribute);
		}
		return attributes;
	}

	public String getElementText(String elementPath) {
		Element element = this.getElementObject(elementPath);
		return element.getText().trim();
	}
	
	public String getElementName(String elementPath) {
		Element element = this.getElementObject(elementPath);
		return element.getName();
	}

	public HashMap<String, String> getElementAttributes(String elementPath) {
		ArrayList<Attribute> attributes = this.getAttributesOject(elementPath);
		HashMap<String, String> attrMap = new HashMap<String, String>();
		for (Attribute attribute : attributes) {
			attrMap.put(attribute.getName(), attribute.getValue());
		}
		return attrMap;
	}

	public HashMap<String, String> getElementData(String elementPath) {
		HashMap<String, String> attrMap = this
				.getElementAttributes(elementPath);
		attrMap.put("text", this.getElementText(elementPath));
		return attrMap;
	}

	public int getElementAttrCount(String elementPath) {
		Element element = this.getElementObject(elementPath);
		return element.attributeCount();
	}

	public int getChildrenElementCount(String elementPath,
			String childrenElementName) {
		Element element = this.getElementObject(elementPath);
		return element.elements(childrenElementName).size();
	}

	public int getChildrenElementCount(String elementPath) {
		Element element = this.getElementObject(elementPath);
		return element.elements().size();
	}

	@SuppressWarnings("unchecked")
	private List<Element> getChildrenElementByName(String elementPath,
			String childrenElementName) {
		Element element = this.getElementObject(elementPath);
		return element.elements(childrenElementName);
	}

	public Element getChildElement(String elementPath,
			String childrenElementName, int index) {
		List<Element> elements = this.getChildrenElementByName(elementPath,
				childrenElementName);
		return elements.get(index);
	}

	@SuppressWarnings("unchecked")
	public List<Element> getChildrenElement(String elementPath) {
		Element element = this.getElementObject(elementPath);
		return element.elements();
	}

	public Element getChildElement(String elementPath, int index) {
		List<Element> elements = this.getChildrenElement(elementPath);
		return elements.get(index);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getChildrenElementName(String elementPath) {
		Element element = this.getElementObject(elementPath);
		ArrayList<String> childrenName = new ArrayList<String>();
		List<Element> elements = element.elements();
		for (Element elemtent : elements) {
			childrenName.add(elemtent.getName());
		}
		return childrenName;
	}
	

	public void saveXML(String path) {
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

	public static void main(String[] args) {
		XMLHandler xh = new XMLHandler("config/keyword.xml");
		List<Element> es = xh.getElementObjects("/*/*/*");
		for (Element element : es) {
			System.out.println(element.getName()+"-------------"+element.getTextTrim());
		}
		System.out.println();
	}

}
