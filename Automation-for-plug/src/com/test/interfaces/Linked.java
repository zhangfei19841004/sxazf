package com.test.interfaces;

public interface Linked {
	
	public boolean methodIsExist(String className, String methodName);
	
	public boolean returnIsExist(String className, String methodName);
	
	public String getReturn(String className, String methodName);
	
	public String getLinked(String className, String methodName);

}
