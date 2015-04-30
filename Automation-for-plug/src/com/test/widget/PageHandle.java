package com.test.widget;

import com.test.base.Config;
import com.test.control.LoadAllPages;
import com.test.implement.LoadAllMethods;
import com.test.interfaces.Linked;

public class PageHandle {

	private LoadAllPages allPages;

	private Linked linked;

	public PageHandle() {
		this.allPages = new LoadAllPages();
		this.getXMLLinked();
	}

	public LoadAllPages getAllPages() {
		return allPages;
	}

	public Linked getLinked() {
		return linked;
	}

	private void getXMLLinked() {
		if (!Config.getConfig("linked").equals(""))
			try {
				linked = (Linked) Class.forName(Config.getConfig("linked"))
						.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		else
			linked = new LoadAllMethods();
	}
}
