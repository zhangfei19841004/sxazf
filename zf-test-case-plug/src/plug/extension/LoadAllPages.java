package plug.extension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import plug.util.Constants;

public class LoadAllPages {
	
	private List<String> fileNames = new ArrayList<String>();
	
	private String returnPath = null;
	
	private String rootPath;
	
	public String getReturnPath(String name) {
		this.loadPageByName(rootPath+"/src/" + Constants.ROOT_PAGE.replaceAll("\\.", "/"), name);		
		return returnPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}	

	private void loadAllPages(String pagePath){
		File file = new File(pagePath);
		File[] files = file.listFiles();		
		for (File f : files) {			
			if (f.isFile()) {
				String fileBaseName = f.getName().replaceAll("\\.java", "");
				fileNames.add(String.valueOf(fileBaseName.substring(0, 1).toLowerCase()).concat(fileBaseName.substring(1)));				
			}else {				
				this.loadAllPages(pagePath+"/" + f.getName());				
			}
		}	
	}
	
	private void loadPageByName(String path, String objectName){
		File file = new File(path);
		File[] files = file.listFiles();		
		for (File f : files) {			
			if (f.isFile()) {
				String fileBaseName = f.getName().replaceAll("\\.java", "");
				String filePath = path + "/" + fileBaseName;
				if (fileBaseName.substring(0, 1).toLowerCase().concat(fileBaseName.substring(1)).equals(objectName)) {									
					returnPath = filePath;
					return;
				} 
			}else {
				returnPath = null;
				this.loadPageByName(path + "/" + f.getName(), objectName);
				if(returnPath != null){
					return;
				}					
			}
		}	
	}
	
	private void loadAllYamlFiles(String yamlPath){
		File file = new File(yamlPath);
		File[] files = file.listFiles();		
		for (File f : files) {			
			if (f.isFile()) {
				if(f.getName().endsWith(".yaml")){
					String fileBaseName = f.getName().replaceAll("\\.yaml", "");
					fileNames.add(String.valueOf(fileBaseName.substring(0, 1).toLowerCase()).concat(fileBaseName.substring(1)));
				}								
			}else {				
				this.loadAllPages(yamlPath+"/" + f.getName());				
			}
		}
	}
	
	public List<String> getFileNames(){
		this.loadAllPages(rootPath+"/src/" + Constants.ROOT_PAGE.replaceAll("\\.", "/"));
		this.loadAllYamlFiles(rootPath+"/"+Constants.LOCATOR_DIR);
		return fileNames;
	}

	public static void main(String[] args) {

	}

}
