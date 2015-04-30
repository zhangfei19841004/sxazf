package com.test.control;

import java.io.File;

public class LoadAllPages {

	private String packagePath;
	
	private String returnPath = null;

	public String getPackagePath() {
		return packagePath;
	}	

	public String getReturnPath() {
		return returnPath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	@SuppressWarnings("rawtypes")
	public void createInstance(String filePath, String objectName)
			throws DefinedException {
		try {
			Class clazz = Class.forName(filePath);
			Object page = clazz.newInstance();
			ObjectManager.objectMap().put(objectName, page);
		} catch (ClassNotFoundException e) {
			throw new DefinedException(filePath + " is not found!");
		} catch (InstantiationException e) {
			throw new DefinedException(filePath + " is instanced exception!");
		} catch (IllegalAccessException e) {
			throw new DefinedException(filePath + " is instanced exception!");
		} catch (Exception e) {
			throw new DefinedException(filePath + " imported error!");
		}
	}

	public void loadAllPages(String path, String objectName)
			throws DefinedException {
		File file = new File("src/" + path.replaceAll("\\.", "/"));
		File[] files = file.listFiles();		
		for (File f : files) {			
			if (f.isFile()) {
				String fileBaseName = f.getName().replaceAll("\\.java", "");
				String filePath = path + "." + fileBaseName;
				if (String.valueOf(fileBaseName.substring(0, 1).toLowerCase())
						.concat(fileBaseName.substring(1)).equals(objectName)) {									
					returnPath = filePath;
					return;
				} 
			}else {
				returnPath = null;
				this.loadAllPages(path + "." + f.getName(), objectName);
				if(returnPath != null)
					return;
			}
		}	
	}

	public static void main(String[] args) throws DefinedException {
		LoadAllPages lp = new LoadAllPages();
		lp.loadAllPages(lp.getPackagePath(), "testBaidu");
		System.out.println(lp.getReturnPath());
	}

}
