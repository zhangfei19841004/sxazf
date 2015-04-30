package com.test.implement;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;

import com.test.interfaces.TestCases;

public class TestCaseExcel implements TestCases {

	private String filePath;
	private String sheetName;
	private HSSFWorkbook workBook;
	private List<String> columnHeaderList;

	public TestCaseExcel(String filePath, String sheetName) {
		this.filePath = filePath;
		this.sheetName = sheetName;
	}

	@Override
	public ArrayList<String> getTestCase(String testCaseName) {
		this.load();
		return this.getExcelCase();
	}

	private void load() {
		try {
			FileInputStream inStream = new FileInputStream(new File(filePath));
			workBook = new HSSFWorkbook(inStream);
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getCellValue(HSSFCell cell) {
		String cellValue = "";
		DataFormatter formatter = new DataFormatter();

		if (cell != null) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:

				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					cellValue = formatter.formatCellValue(cell);
				} else {
					double value = cell.getNumericCellValue();
					int intValue = (int) value;
					cellValue = value - intValue == 0 ? String
							.valueOf(intValue) : String.valueOf(value);
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
			}
		}
		return cellValue.trim();
	}

	private List<Map<String, String>> getSheetDataWithHeader() {

		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		HSSFSheet sheet = workBook.getSheet(sheetName);

		columnHeaderList = new ArrayList<String>();

		int numOfRows = sheet.getLastRowNum() + 1;
		for (int i = 0; i < numOfRows; i++) {
			HSSFRow row = sheet.getRow(i);
			Map<String, String> rowData = new HashMap<String, String>();
			if (row != null) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					HSSFCell cell = row.getCell(j);
					if (i == 0)
						columnHeaderList.add(getCellValue(cell));
					else
						rowData.put(columnHeaderList.get(j), getCellValue(cell));
				}
			}
			if (i > 0)
				sheetData.add(rowData);
		}
		return sheetData;
	}

	private ArrayList<String> getExcelCase() {
		ArrayList<String> allTestCase = null;
		List<Map<String, String>> allData = this.getSheetDataWithHeader();
		for (int i = 0; i < allData.size(); i++) {
			Map<String, String> map = allData.get(i);
			String step = "";
			String object = map.get("页面|对象");
			String preOp = map.get("预置操作");
			String element = map.get("元素对象");
			String elementParam = map.get("元素对象参数");
			String action = map.get("操作");
			String param = map.get("参数");
			String returnValue = map.get("返回值");
			if (object != null && !object.equals(""))
				step += "\"对象\"" + "{" + object + "}";
			if (preOp != null && !preOp.equals(""))
				step += "\"" + preOp + "\"";
			if (element != null && !element.equals("")) {
				step += "(" + element + ")";
				if (elementParam != null && !elementParam.equals(""))
					step += "{" + elementParam + "}";
			}
			if (action != null && !action.equals(""))
				step += "[" + action + "]";
			if (param != null && !param.equals(""))
				step += "{" + param + "}";
			if (returnValue != null && !returnValue.equals(""))
				step += "\"返回值\"" + "{" + returnValue + "}";
			if (allTestCase == null)
				allTestCase = new ArrayList<String>();
			else
				allTestCase.add(step);
		}
		return allTestCase;
	}

	public static void main(String[] args) {
		TestCaseExcel tc = new TestCaseExcel(
				"C:\\Users\\IBM_ADMIN\\Desktop\\test-case.xls", "Sheet1");
		tc.load();
		tc.getExcelCase();
	}

}
