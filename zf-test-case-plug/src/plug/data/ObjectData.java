package plug.data;

import java.util.List;

import plug.extension.LoadAllPages;
import plug.util.Constants;

public class ObjectData {
	
	private String rootPath;	
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String[] getData(){
		return new String[]{"打开", "页面", "对象", "返回值", "创建对象", "对象名", "如果{}等于{}", "如果{}不等于{}", "并且{}等于{}", "并且{}不等于{}", "或者{}等于{}", "或者{}不等于{}", "循环{}从{}到{}"};
	}
	
	public String[] getDataParam(){
		LoadAllPages lp = new LoadAllPages();
		lp.setRootPath(rootPath);
		List<String> fileNames = lp.getFileNames();
		String[] names = new String[fileNames.size()+3]; 
		for (int i = 0; i < fileNames.size(); i++) {
			names[i] = fileNames.get(i);
		}
		names[fileNames.size()] = Constants.OBJ_THIS;
		names[fileNames.size()+1] = Constants.OBJ_ASSERT;
		names[fileNames.size()+2] = Constants.OBJ_LOG;
		return names;
	}	
	
}
