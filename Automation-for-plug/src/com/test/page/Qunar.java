package com.test.page;

import java.util.ArrayList;
import com.test.base.Page;
import com.test.util.Utils;

public class Qunar extends Page {
	
	public ArrayList<String[]> list = null;
	
	private void getAllResultElement() {
		int labelIndex = 1;
		int rowIndex = 1;
		list = new ArrayList<String[]>();
		while (true) {
			if (this.getElementNoWait("结果一栏",new String[]{String.valueOf(labelIndex)}) != null) {
				if (this.getElementNoWait("结果信息",new String[]{String.valueOf(labelIndex),String.valueOf(rowIndex)}) != null) {
					String[] s = new String[]{String.valueOf(labelIndex),String.valueOf(rowIndex)};
					list.add(s);
					rowIndex++;
				} else{
					labelIndex++;
					rowIndex = 1;
				}
			} else
				break;
		}		
	}
	
	public String getListSize(){
		if(list==null)
			this.getAllResultElement();
		return String.valueOf(list.size());
	}
	
	public String getScriptString(){
		Utils util = new Utils();
		return "$('input[name=toDate]').val('"+util.getAfterDateToString("7")+"')";
	}
	
	public String getLabelIndex(String index){
		if(list==null)
			this.getAllResultElement();
		return list.get(Integer.valueOf(index))[0];
	}
	
	public String getRowIndex(String index){
		if(list==null)
			this.getAllResultElement();
		return list.get(Integer.valueOf(index))[1];
	}

}
