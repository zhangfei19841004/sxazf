package com.test.widget;

import com.test.control.AssertObject;
import com.test.control.DefinedException;
import com.test.control.Keyword;
import com.test.control.ObjectManager;
import com.test.control.RegExp;
import com.test.util.Log;

public class Create {

	private RegExp re;

	private String description;

	public Create() {
		this.re = new RegExp();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void createAssert() throws DefinedException {
		ObjectManager.objectMap().put(Keyword.getKeyword("Assert"),
				new AssertObject());
	}

	public void createLog() throws DefinedException {
		ObjectManager.objectMap().put(Keyword.getKeyword("Log"),Log.class);
	}

	@SuppressWarnings("rawtypes")
	public void createPage() throws DefinedException {
		Log.commentStep(description);
		String importClass;
		String importName;
		try {
			importClass = re.importClass(description).get(0);
			importName = re.importName(description).get(0);
		} catch (Exception e) {
			throw new DefinedException("Import object error, parameter missed!");
		}
		if (importClass.equals("") || importName.equals("")) {
			throw new DefinedException(importClass + " or " + importName
					+ " can not be null!");
		}
		try {			
			Class clazz = Class.forName(importClass);
			Object page = clazz.newInstance();			
			ObjectManager.objectMap().put(importName, page);
		} catch (ClassNotFoundException e) {
			throw new DefinedException(importClass + " is not found!");
		} catch (InstantiationException e) {
			throw new DefinedException(importClass + " is instanced exception!");
		} catch (IllegalAccessException e) {
			throw new DefinedException(importClass + " is instanced exception!");
		} catch (Exception e) {
			throw new DefinedException(importClass + " imported error!");
		}
	}
}
