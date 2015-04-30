package com.test.widget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.test.base.Config;
import com.test.base.Page;
import com.test.control.DefinedException;
import com.test.control.Keyword;
import com.test.control.LoadAllPages;
import com.test.control.ObjectManager;
import com.test.control.RegExp;

public class ObjectHandle {

	private RegExp re;
	
	private String description;
	
	private LoadAllPages allPages;
	
	private Map<String, String> param;
	
	private String currentObject = null;
	
	public void setRe(RegExp re) {
		this.re = re;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
	
	public String getCurrentObject() {
		return currentObject;
	}

	public void setAllPages(LoadAllPages allPages) {
		this.allPages = allPages;
	}
	
	public void setParam(Map<String, String> param) {
		this.param = param;
	}	

	public void setCurrentObject(String currentObject) {
		this.currentObject = currentObject;
	}

	public Object getObject(ParameterHandle ph) throws DefinedException {
		Object object = null;		
		List<String> objectNames = re.pageOrObjectName(description);
		if ((objectNames == null || objectNames.get(0).equals(""))
				&& currentObject == null) {
			throw new DefinedException("Get object name failed from "
					+ description);
		}
		String objectName;
		if (objectNames != null)
			objectName = objectNames.get(0);
		else
			objectName = String.valueOf(
					currentObject.substring(0, 1).toLowerCase()).concat(
					currentObject.substring(1));
		String upOn = String.valueOf(objectName.substring(0, 1).toUpperCase())
				.concat(objectName.substring(1));
		if (objectName.equals("this"))
			return this;
		if (objectNames != null)
			currentObject = upOn;
		if (!ObjectManager.objectMap().keySet().contains(objectName)) {
			allPages.setPackagePath(Config.getConfig("rootPages"));
			allPages.loadAllPages(allPages.getPackagePath(), objectName);
			if (allPages.getReturnPath() != null)
				allPages.createInstance(allPages.getReturnPath(), objectName);
			else {
				ObjectManager.objectMap().put(objectName, new Page(upOn));
			}
		}
		if (ObjectManager.objectMap().keySet().contains(objectName)) {
			object = ObjectManager.objectMap().get(objectName);
			object = this.getWebElement(object, ph);
		} else {
			throw new DefinedException(objectName + " object is not created!");
		}

		return object;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getWebElement(Object object,ParameterHandle ph)
			throws DefinedException {
		String element = null;
		Class[] c = null;
		Object[] args = null;
		try {
			if (re.hasElement(description)) {
				element = re.elementName(description).get(0);
				if (element.equals(""))
					throw new DefinedException("element can not be null in "
							+ description);
				String[] pa = null;
				if (re.isElementParameter(description)) {
					pa = (String[]) ph.handleElementParamter(new Object[] {});
				}
				if (pa != null) {
					c = new Class[] { element.getClass(), pa.getClass() };
					args = new Object[] { element, pa };
				} else {
					c = new Class[] { element.getClass() };
					args = new Object[] { element };
				}
				Class clazz = object.getClass();
				String methodName = null;
				if (re.hadElement1(description))
					methodName = Keyword.getSystemKeyword("getElement");
				if (re.hadElement2(description))
					methodName = Keyword.getSystemKeyword("getElementNoWait");
				Method method = clazz.getMethod(methodName, c);
				object = method.invoke(object, args);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new DefinedException("Get locator: " + element + " error!");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new DefinedException("Get locator: " + element + " error!");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new DefinedException("Get locator: " + element + " error!");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new DefinedException("Get locator: " + element + " error!");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new DefinedException("Get locator: " + element + " error!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DefinedException("Get locator: " + element + " error!");
		}
		return object;
	}

	public void addValueToParam(String key, String value) {
		this.param.put(key, value);
	}
}
