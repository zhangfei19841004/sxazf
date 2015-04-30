package plug.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ho.yaml.Yaml;

import plug.util.Constants;

public class ElementData {
	
	private String rootPath;
	
	private List<String> dataKey;
	
	private String currentObj;	

	public void setCurrentObj(String currentObj) {
		this.currentObj = currentObj;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String[] getData(){
		dataKey = new ArrayList<String>();	
		if(!currentObj.equals(Constants.OBJ_THIS) && !currentObj.equals(Constants.OBJ_ASSERT) && !currentObj.equals(Constants.OBJ_LOG)){
			this.getElementKey();
		}
		String[] datas = new String[dataKey.size()];
		for (int i = 0; i < dataKey.size(); i++) {
			datas[i] = dataKey.get(i);
		}
		return datas;
	}		
	
	private String getYamlFileName(){
		return rootPath+"/"+Constants.LOCATOR_DIR+"/"+String.valueOf(currentObj.substring(0, 1).toUpperCase()).concat(currentObj.substring(1))+".yaml";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getElementKey(){
		try {			
			Map elements = Yaml.loadType(new FileInputStream(this.getYamlFileName()), HashMap.class);			
			Iterator<String> it = elements.keySet().iterator();
			while(it.hasNext()){
				dataKey.add(it.next());
			}
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String s = "ab.c.zf";
		System.out.println(s.substring(0, s.lastIndexOf(".")));
	}
	
}
