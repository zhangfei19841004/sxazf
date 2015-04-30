package com.test.page;

import com.test.base.Page;
import com.test.control.DefinedException;
import com.test.control.TestCaseDefined;

public class Template extends Page {
	

	public void search(String keyword,String keywords) throws DefinedException{
		new TestCaseDefined().run("TestBaidu1", new String[]{keyword,keywords});
	}

	public String searchResult() {
		return this.getElement("baidu_input").getAttribute("value");
	}
	
	public void getTest(String a,String b){
		System.out.println(a);
		System.out.println(b);
	}

}
