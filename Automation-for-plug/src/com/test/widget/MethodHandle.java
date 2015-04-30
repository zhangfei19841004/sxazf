package com.test.widget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.test.control.DefinedException;
import com.test.control.Keyword;
import com.test.control.RegExp;
import com.test.interfaces.Linked;
import com.test.util.Log;

public class MethodHandle {

	private RegExp re;

	private String description;

	private String replaceDesc;

	private String currentObject;

	private String currentMethod;

	private Linked linked;

	private Map<String, String> param;

	public void setRe(RegExp re) {
		this.re = re;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setReplaceDesc(String replaceDesc) {
		this.replaceDesc = replaceDesc;
	}

	public void setCurrentObject(String currentObject) {
		this.currentObject = currentObject;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public void setLinked(Linked linked) {
		this.linked = linked;
	}

	public String getCurrentMethod() {
		return currentMethod;
	}

	private Method handleMethodFromPage(Method[] methods, int paramCount,
			String getMethod) {
		Method action = null;
		for (Method m : methods) {
			if (m.getName().equals(getMethod)
					&& m.getParameterAnnotations().length == paramCount) {
				action = m;
				break;
			}
		}
		return action;
	}

	@SuppressWarnings("rawtypes")
	private Method handleMethodFromLinked(Object object, Method[] methods,
			Linked linked, String getMethod) throws DefinedException {
		Method action = null;
		String className = currentObject;
		currentMethod = getMethod;
		if (linked.methodIsExist(className, getMethod)) {
			Class[] parameterTypes = null;
			if (linked.returnIsExist(className, getMethod)
					&& !linked.getReturn(className, getMethod).equals(""))
				parameterTypes = new Class[] { String[].class, String.class,
						String.class };
			else
				parameterTypes = new Class[] { String[].class, String.class };
			try {
				action = object.getClass()
						.getMethod(Keyword.getSystemKeyword("executePageMethod"),
								parameterTypes);
			} catch (SecurityException e) {
				throw new DefinedException("Can not find the method: "
						+ Keyword.getSystemKeyword("executePageMethod")
						+ " Page class");
			} catch (NoSuchMethodException e) {
				throw new DefinedException("Can not find the method: "
						+ Keyword.getSystemKeyword("executePageMethod")
						+ " Page class");
			}
		}
		return action;
	}

	public Method handleMethod(Object object, Method[] methods, int paramCount)
			throws DefinedException {
		Method action = null;
		if (re.hasMethod(description)
				&& !re.methodName(description).get(0).equals("")) {
			String getMethod = re.methodName(description).get(0);
			if (Keyword.getKeyword(getMethod) != null)
				getMethod = Keyword.getKeyword(getMethod);
			action = this.handleMethodFromPage(methods, paramCount, getMethod);
			if (action == null)
				action = this.handleMethodFromLinked(object, methods, linked,
						getMethod);
			if (action == null)
				throw new DefinedException("Had not defined the method: "
						+ getMethod + " in LinkedPages or Page class");
		}
		return action;
	}

	public void invokeMethod(Object object, Method action, Object[] param)
			throws DefinedException {
		try {
			if (re.hasReturnValue(description)
					&& re.returnName(description) != null) {
				String returnValue = action.invoke(object, param).toString();
				this.param.put(re.returnName(description).get(0), returnValue);
				replaceDesc = re.replaceReturn(replaceDesc, returnValue);
				Log.commentStep(replaceDesc);
			} else {
				Log.commentStep(replaceDesc);
				action.invoke(object, param);
			}
		} catch (IllegalArgumentException e) {
			throw new DefinedException(object.getClass().getName() + " "
					+ action.getName() + " argument error!");
		} catch (IllegalAccessException e) {
			throw new DefinedException(object.getClass().getName() + " "
					+ action.getName() + " is invoked error!");
		} catch (InvocationTargetException e) {
			throw new DefinedException(e.getTargetException().toString());
		} catch (Exception e) {
			throw new DefinedException(object.getClass().getName() + " "
					+ action.getName() + " " + new Utils().join(param, ",")
					+ " error!");
		}
	}

}
