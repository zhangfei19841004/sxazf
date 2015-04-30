package plug.data;

import java.io.File;
import java.util.List;

import org.dom4j.Element;

import plug.extension.LoadAllPages;
import plug.extension.ParseXml;
import plug.extension.RegExp;
import plug.util.Constants;

public class MethodData {
	
	private String currentObj;	
	
	private String content;
	
	private boolean flag;	
	
	private String rootPath;	
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setCurrentObj(String currentObj) {
		this.currentObj = currentObj;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String[] getData(){
		if(currentObj==null){
			return null;
		}
		if(currentObj.equals(Constants.OBJ_THIS)){
			if(flag){
				return new String[]{Constants.OBJ_THIS_METHOD};
			}else{
				return null;
			}			
		}else if(currentObj.equals(Constants.OBJ_ASSERT)){
			return new String[]{Constants.OBJ_ASSERT_METHOD};
		}else if(currentObj.equals(Constants.OBJ_LOG)){
			return new String[]{Constants.OBJ_LOG_METHOD};
		}else{
			RegExp re = new RegExp();
			if(re.hasElement(content)){
				return this.getMethods("/*/"+Constants.KEYWORD_DRIVER_API+"/*");							
			}else{
				if(this.isPages()){
					return this.getMethods("/*/"+Constants.KEYWORD_SELF_API+"/*");	
				}else{
					return this.getMethods("/*/"+Constants.KEYWORD_OTHER_API+"/*");
				}
			}
		}
	}	
	
	private String[] getMethods(String xpath){
		ParseXml px = new ParseXml(rootPath+"/"+"config/keyword.xml");
		List<Element> es = px.getElementObjects(xpath);
		if(es != null && es.size()>0){
			String[] s = new String[es.size()];
			for (int i = 0; i < es.size(); i++) {
				s[i] = es.get(i).getName();
			}
			return s;
		}	
		return null;
	}
	
	private boolean isPages(){
		LoadAllPages lp = new LoadAllPages();
		lp.setRootPath(rootPath);		
		String filePath = lp.getReturnPath(currentObj);
		if(filePath!=null){
			return true;
		}
		File file = new File(rootPath+"/"+Constants.LOCATOR_DIR+"/"+currentObj.substring(0, 1).toUpperCase().concat(currentObj.substring(1))+".yaml");
		if(file.exists()){
			return true;
		}
		return false;
	}
		
	public static void main(String[] args) {
		String s = "ab.c.zf";
		System.out.println(s.substring(0, s.lastIndexOf(".")));
	}
	
}
