package plug.extension;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import plug.util.Constants;

public class ParseXml {	
	/**
	 * 解析xml文件，我们需要知道xml文件的路径，然后根据其路径加载xml文件后，生成一个Document的对象，
	 * 于是我们先定义两个变量String filePath,Document document
	 * 然后再定义一个load方法，这个方法用来加载xml文件，从而产生document对象。
	 */
	private String filePath;

	private Document document; 
	
	/**
	 * 	构造器用来new ParseXml对象时，传一个filePath的参数进来,从而初始化filePath的值
	 * 调用load方法，从而在ParseXml对象产生时，就会产生一个document的对象。
	 */
	public ParseXml(String filePath) {		
		this.filePath = filePath;
		this.load(this.filePath);
	}
	
	/**
	 * 用来加载xml文件，并且产生一个document的对象
	 */
	private void load(String filePath){
		File file = new File(filePath);
		if (file.exists()) {
			SAXReader saxReader = new SAXReader();
			try {
				document = saxReader.read(file);
			} catch (DocumentException e) {		
				System.out.println("文件解析错误!");			
			}
		} else{
			System.out.println("文件不存在 : " + filePath);
		}			
	}
	
	/**
	 * 用xpath来得到一个元素节点对象
	 * @param elementPath elementPath是一个xpath路径,比如"/config/driver"
	 * @return 返回的是一个节点Element对象
	 */
	public Element getElementObject(String elementPath) {
		return (Element) document.selectSingleNode(elementPath);
	}	
	
	@SuppressWarnings("unchecked")
	public List<Element> getElementObjects(String elementPath) {
		if(document!=null){
			return document.selectNodes(elementPath);
		}
		return new ArrayList<Element>();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getChildrenInfoByElement(Element element){
		Map<String, String> map = new HashMap<String, String>();
		List<Element> children = element.elements();
		for (Element e : children) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}
	
	/**
	 * 用xpath来判断一个结点对象是否存在
	 */
	public boolean isExist(String elementPath){
		boolean flag = false;
		Element element = this.getElementObject(elementPath);
		if(element != null) flag = true;
		return flag;
	}
	
	/**
	 * 用xpath来取得一个结点对象的值
	 */
	public String getElementText(String elementPath) {
		Element element = this.getElementObject(elementPath);
		if(element != null){
			return element.getText().trim();
		}else{
			return null;
		}		
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
	
	public HashMap<String, String> getElementAttributes(String elementPath) {
		ArrayList<Attribute> attributes = this.getAttributesOject(elementPath);
		HashMap<String, String> attrMap = new HashMap<String, String>();
		for (Attribute attribute : attributes) {
			attrMap.put(attribute.getName(), attribute.getValue());
		}
		return attrMap;
	}
	
	public static void main(String[] args) {
//		ParseXml px = new ParseXml("config/config.xml");//给定config.xml的路径
//		String browser = px.getElementText("/*/browser");
//		Log.logInfo(browser);
//		String waitTime = px.getElementText("/config/waitTime");
//		Log.logInfo(waitTime);
		
		ParseXml px = new ParseXml("files/Global.xml");//给定config.xml的路径
		List<Element> elements = px.getElementObjects("/*/"+Constants.KEYWORD_DRIVER_API+"/*");
		for (Element element : elements) {
			System.out.println(element.getName());
		}	
	}
	
}
