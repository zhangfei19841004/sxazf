package com.test.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.control.DefinedException;
import com.test.control.RegExp;
import com.test.control.Keyword;
import com.test.util.Log;
import com.test.widget.Excute;

/*
 * 
 "如果"{1}等于{1},"并且"{2}不等于{3}
 -"对象"{Log},输出[信息]{a}
 -"如果"{1}等于{1}
 --"循环"{i}从{1}到{3}
 ---"对象"{Log},输出[信息]{i}
 -"如果"{1}等于{1}
 --"对象"{Log},输出[信息]{1}
 -"对象"{Log},输出[信息]{a}

 {1=1, 2=4, 3=3}
 {1="如果"{1}等于{1}, 2=-"如果"{1}等于{1}, "并且"{2}不等于{3}, 3=--"循环"{i}从{1}到{3}, 4=-"如果"{1}等于{1}}
 {1=[-"对象"{Log},输出[信息]{a}, 2, 4, -"对象"{Log},输出[信息]{a}], 2=[3], 3=[---"对象"{Log},输出[信息]{i}], 4=[--"对象"{Log},输出[信息]{1}]}
 */

public class LogicHandle {

	private RegExp re;

	private Excute excute;

	private Map<String, String> param;

	private Map<String, String> logicTitle;

	private Map<String, ArrayList<String>> logicBlock;

	private Map<Integer, Integer> multiLogic;

	private int logicIndex;

	private boolean logicFlag;

	public LogicHandle(Excute excute) {
		this.re = new RegExp();
		this.excute = excute;
		this.logicTitle = new HashMap<String, String>();
		this.logicBlock = new HashMap<String, ArrayList<String>>();
		this.multiLogic = new HashMap<Integer, Integer>();
		this.logicIndex = 0;
		this.logicFlag = false;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public Map<String, String> getLogicTitle() {
		return logicTitle;
	}

	public void setLogicTitle(Map<String, String> logicTitle) {
		this.logicTitle = logicTitle;
	}

	public Map<String, ArrayList<String>> getLogicBlock() {
		return logicBlock;
	}

	public void setLogicBlock(Map<String, ArrayList<String>> logicBlock) {
		this.logicBlock = logicBlock;
	}

	public Map<Integer, Integer> getMultiLogic() {
		return multiLogic;
	}

	public void setMultiLogic(Map<Integer, Integer> multiLogic) {
		this.multiLogic = multiLogic;
	}

	public int getLogicIndex() {
		return logicIndex;
	}

	public void setLogicIndex(int logicIndex) {
		this.logicIndex = logicIndex;
	}

	public boolean isLogicFlag() {
		return logicFlag;
	}

	public void setLogicFlag(boolean logicFlag) {
		this.logicFlag = logicFlag;
	}

	public void logicCaseCollect(String description) {
		if ((re.isIfCase(description) || re.isForCase(description))
				&& !re.isLogicBlock(description)) {
			logicTitle.put(String.valueOf(++logicIndex), description);
			int level = re.getLogicLevel(description);
			multiLogic.put(level + 1, logicIndex);
			logicFlag = true;
		}
		if ((!re.isIfCase(description) && !re.isForCase(description))
				&& re.isLogicBlock(description)) {
			int level = re.getLogicLevel(description);
			int index = multiLogic.get(level);
			if (logicBlock.containsKey(String.valueOf(index)))
				logicBlock.get(String.valueOf(index)).add(description);
			else {
				ArrayList<String> block = new ArrayList<String>();
				block.add(description);
				logicBlock.put(String.valueOf(index), block);
			}
		}
		if ((re.isIfCase(description) || re.isForCase(description))
				&& re.isLogicBlock(description)) {
			logicTitle.put(String.valueOf(++logicIndex), description);
			int level = re.getLogicLevel(description);
			multiLogic.put(level + 1, logicIndex);
			int index = multiLogic.get(level);
			if (logicBlock.containsKey(String.valueOf(index)))
				logicBlock.get(String.valueOf(index)).add(
						String.valueOf(logicIndex));
			else {
				ArrayList<String> block = new ArrayList<String>();
				block.add(String.valueOf(logicIndex));
				logicBlock.put(String.valueOf(index), block);
			}
		}

	}

	private String ifParameter(String str) {
		List<String> temp = new ArrayList<String>();
		List<String> list = re.parameterName(str);
		for (int i = 0; i < list.size(); i++) {
			if (param.containsKey(list.get(i)))
				temp.add(param.get(list.get(i)));
			else
				temp.add(list.get(i));
		}
		return re.replaceIfParameter(str, temp);
	}

	private boolean logicConnector(String str, List<Boolean> temp)
			throws DefinedException {
		boolean result = false;
		String conn = re.getLogicConnector(str);
		if (conn != null) {
			try {
				if (conn.equals(Keyword.getSystemKeyword("and")))
					result = temp.get(0) && temp.get(1);
				if (conn.equals(Keyword.getSystemKeyword("or")))
					result = temp.get(0) || temp.get(1);
			} catch (Exception e) {
				throw new DefinedException("if title error, miss condition!");
			}
		} else
			result = temp.get(0);
		return result;
	}

	private boolean ifCondition(String str) throws DefinedException {
		List<String> conList = re.getIfCondition(str);
		List<Boolean> temp = new ArrayList<Boolean>();
		List<String> conditions = new ArrayList<String>();
		for (String con : conList) {
			String condition = this.ifParameter(con);
			conditions.add(condition);
			List<String> pName = re.parameterName(condition);
			String determine = re.getDetermine(condition);
			boolean b;
			try {
				b = pName.get(0).equals(pName.get(1));
			} catch (Exception e) {
				throw new DefinedException(
						"if title error, miss condition parameter!");
			}
			if (determine.equals(Keyword.getSystemKeyword("equals")))
				temp.add(b);
			if (determine.equals(Keyword.getSystemKeyword("notEquals")))
				temp.add(!b);
		}
		str = re.replaceIfCondition(str, conditions);
		Log.commentStep(str);
		if (temp.size() == 0)
			throw new DefinedException(str
					+ " if title error, miss condition parameter!");
		return this.logicConnector(str, temp);
	}

	public void executeForBlock(String index) throws DefinedException {
		String forName = logicTitle.get(index);
		Log.commentStep(forName);
		List<String> list = re.parameterName(forName);
		String variableName;
		int begin;
		int end;
		try {
			variableName = list.get(0);
			begin = Integer.valueOf(list.get(1));
			if (param.containsKey(list.get(1)))
				begin = Integer.valueOf(param.get(list.get(1)));
			end = Integer.valueOf(list.get(2));
			if (param.containsKey(list.get(2)))
				end = Integer.valueOf(param.get(list.get(2)));
		} catch (Exception e) {
			throw new DefinedException(forName
					+ " for title error, miss parameter!");
		}
		for (int i = begin; i <= end; i++) {
			param.put(variableName, String.valueOf(i));
			this.executeLogicBlock(index);
		}
	}

	private void executeLogicBlock(String index) throws DefinedException {
		ArrayList<String> blockList = logicBlock.get(String.valueOf(index));
		for (int i = 0; i < blockList.size(); i++) {
			if (logicTitle.containsKey(blockList.get(i))) {
				if (re.isIfCase(logicTitle.get(blockList.get(i))))
					this.executeIfBlock(blockList.get(i));
				if (re.isForCase(logicTitle.get(blockList.get(i))))
					this.executeForBlock(blockList.get(i));
			} else {
				excute.setParam(param);
				excute.setDescription(blockList.get(i));
				excute.setReplaceDesc(blockList.get(i));
				excute.excute();
			}
		}
	}

	public void executeIfBlock(String index) throws DefinedException {
		String ifName = logicTitle.get(index);
		boolean b = this.ifCondition(ifName);
		if (b) {
			this.executeLogicBlock(index);
		}
	}

}
