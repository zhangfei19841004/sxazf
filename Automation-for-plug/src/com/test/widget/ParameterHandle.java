package com.test.widget;

import java.util.Map;

import com.test.control.DefinedException;
import com.test.control.Keyword;
import com.test.control.RegExp;
import com.test.interfaces.Linked;

public class ParameterHandle {

	private RegExp re;

	private String description;

	private String replaceDesc;

	private Map<String, String> param;

	private Linked linked;

	public void setRe(RegExp re) {
		this.re = re;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setReplaceDesc(String replaceDesc) {
		this.replaceDesc = replaceDesc;
	}

	public String getReplaceDesc() {
		return replaceDesc;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public void setLinked(Linked linked) {
		this.linked = linked;
	}

	private Object[] replaceComma(String pName) throws DefinedException {
		Object[] param = null;
		if (!pName.equals("")) {
			param = pName.split("(?<!\\\\),");
			for (int i = 0; i < param.length; i++) {
				if (this.param.containsKey(param[i]))
					param[i] = this.replacePlus(this.param.get(param[i]
							.toString().replaceAll("\\\\,", ",")));
				else
					param[i] = this.replacePlus(param[i].toString().replaceAll(
							"\\\\,", ","));
			}
			replaceDesc = re.replaceParameter(replaceDesc,
					new Utils().join(param, ","));
		} else
			throw new DefinedException("Parameter should not be null!");
		return param;
	}

	private String replacePlus(String pName) throws DefinedException {
		Object[] param = null;
		if (!pName.equals("")) {
			param = pName.split("(?<!\\\\)\\+");
			for (int i = 0; i < param.length; i++) {
				if (this.param.containsKey(param[i]))
					param[i] = this.param.get(param[i].toString().replace(
							"\\+", "+"));
				else
					param[i] = param[i].toString().replace("\\+", "+");
			}
			replaceDesc = re.replaceParameter(replaceDesc,
					new Utils().join(param, ""));
		}
		if (param != null) {
			pName = new Utils().join(param, "");
		}
		return pName;
	}

	private Object[] parameterReplace(String pName) throws DefinedException {		
		return this.replaceComma(pName);
	}

	public Object[] handleParamter(Object[] param) throws DefinedException {
		if (re.hasParameter(description)) {
			String pName = re.getMethodParameterName(description).get(0);
			param = this.parameterReplace(pName);
		}
		return param;
	}

	public Object[] handleElementParamter(Object[] param)
			throws DefinedException {
		if (re.isElementParameter(description)) {
			String pName = re.getElementParameter(description).get(0);
			param = this.parameterReplace(pName);
		}
		return param;
	}

	public Object[] handleSpecialParamter(String methodName, Object[] param,
			String currentObject, String currentMethod) throws DefinedException {
		if (methodName.equals(Keyword.getSystemKeyword("sendKeys"))) {
			if (param != null) {
				CharSequence[] cs = new CharSequence[] { (String) param[0] };
				param = new Object[] { cs };
			} else
				throw new DefinedException("Method: " + methodName
						+ " parameter is not exist or null!");
		}
		if (methodName.equals(Keyword.getSystemKeyword("executePageMethod"))) {
			String[] p = new String[] {};
			if (param.length > 0)
				p = (String[]) param;
			String className = currentObject;
			String mName = currentMethod;
			if (linked.returnIsExist(className, mName)
					&& !linked.getReturn(className, mName).equals(""))
				param = new Object[] { p, linked.getReturn(className, mName),
						linked.getLinked(className, mName) };
			else
				param = new Object[] { p, linked.getLinked(className, mName) };
		}
		return param;
	}	

}
