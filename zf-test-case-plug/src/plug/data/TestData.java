package plug.data;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import plug.extension.ParseXml;
import plug.util.Constants;

public class TestData {
	
	private String rootPath;
	
	private List<String> dataKey;
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String[] getData(String caseName){
		dataKey = new ArrayList<String>();		
		ParseXml parseDataFile = new ParseXml(rootPath+"/"+Constants.TEST_DATA_DIR+"/"+caseName+".xml");
		this.parseTestData(parseDataFile, "/*/"+Constants.TEST_DATA_NODE+"[1]/*");
		this.parseTestData(parseDataFile, "/*/"+Constants.TEST_DATA_COMMON+"/*");
		ParseXml parseGlobalFile = new ParseXml(rootPath+"/"+Constants.TEST_DATA_DIR+"/"+Constants.TEST_DATA_GLOBAL_FILE+".xml");
		this.parseTestData(parseGlobalFile, "/*/*");	
		String[] datas = new String[dataKey.size()+1];
		for (int i = 0; i < dataKey.size(); i++) {
			datas[i] = dataKey.get(i);
		}
		datas[dataKey.size()] = "args[]";
		return datas;
	}
	
	private void parseTestData(ParseXml parseFile, String xpath){		
		List<Element> elements = parseFile.getElementObjects(xpath);
		for (Element element : elements) {
			if(!dataKey.contains(element.getName())){
				dataKey.add(element.getName());
			}
		}
	}
	
	public static void main(String[] args) {
		String s = "ab.c.zf";
		System.out.println(s.substring(0, s.lastIndexOf(".")));
	}
	
}
