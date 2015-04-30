package com.test.widget;

import java.lang.reflect.Method;
import java.util.Map;

import com.test.control.DefinedException;
import com.test.control.RegExp;

public class Excute {

	private RegExp re;

	private String description;

	private String replaceDesc;

	private Map<String, String> param;

	private PageHandle pageHandle;

	private ObjectHandle oh;

	private ParameterHandle ph;

	private MethodHandle mh;

	public Excute() {
		this.re = new RegExp();
		pageHandle = new PageHandle();
		oh = new ObjectHandle();
		ph = new ParameterHandle();
		mh = new MethodHandle();
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
		ph.setParam(param);
	}

	public void setDescription(String description) {
		this.description = description;

	}

	public void setReplaceDesc(String replaceDesc) {
		this.replaceDesc = replaceDesc;
	}

	private void initialObjectHandle() {
		oh.setRe(re);
		oh.setDescription(description);
		oh.setAllPages(pageHandle.getAllPages());
		oh.setParam(param);
	}

	private void initialMethodHandle() {
		mh.setRe(re);
		mh.setDescription(description);
		mh.setReplaceDesc(replaceDesc);
		mh.setParam(param);
		mh.setLinked(pageHandle.getLinked());
		mh.setCurrentObject(oh.getCurrentObject());
	}

	private void initialParameterHandle() {
		ph.setRe(re);
		ph.setDescription(description);
		ph.setReplaceDesc(replaceDesc);
		ph.setLinked(pageHandle.getLinked());
	}

	@SuppressWarnings("rawtypes")
	public void excute() throws DefinedException {
		this.initialObjectHandle();
		this.initialParameterHandle();
		Object object = oh.getObject(ph);
		Class clazz = object.getClass();
		if (object instanceof Class) {
			clazz = (Class) object;
		}
		Method[] methods = clazz.getMethods();
		Method action = null;
		Object[] param = ph.handleParamter(new Object[] {});
		this.initialMethodHandle();
		action = mh.handleMethod(object, methods, param.length);
		param = ph.handleSpecialParamter(action.getName(), param,
				oh.getCurrentObject(), mh.getCurrentMethod());
		mh.setReplaceDesc(ph.getReplaceDesc());
		mh.invokeMethod(object, action, param);
	}	
}
