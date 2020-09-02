package com.agilent.cps.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	
	public static Map<String, List<Map<String, String>>> workbookData = new LinkedHashMap<String, List<Map<String,String>>>();
	
	public Map<String, List<Map<String, String>>> getDataFromExcel(String excelPath)
	{
		FileInputStream fs=null;
		XSSFWorkbook wb=null;
		try {
			fs = new FileInputStream(excelPath);
			wb = new XSSFWorkbook(fs);
			XSSFSheet ws ;
			int noOfSheets = wb.getNumberOfSheets();
			for(int s=0;s<noOfSheets;s++)
			{
				ws = wb.getSheetAt(s);
				List<Map<String, String>> sheetData = new ArrayList<Map<String,String>>();
				List<String> headerData = new ArrayList<String>();
				int rownum = ws.getPhysicalNumberOfRows();
				int colnum=0;
				if(rownum>0)
					colnum = ws.getRow(0).getPhysicalNumberOfCells();
				String cellValue;
				for (int i = 0; i < rownum; i++) {
					Map<String, String> rowData = new LinkedHashMap<String, String>();
					if(i==0)
					{
						for (int j = 0; j < colnum; j++) {
							XSSFCell cell = ws.getRow(i).getCell(j);
							headerData.add(cellToString(cell));
						}
					}
					else
					{
					for (int j = 0; j < colnum; j++) {
						XSSFCell cell = ws.getRow(i).getCell(j);
						if(null != (cellValue=cellToString(cell)))
							rowData.put(headerData.get(j), cellValue);
					}
					sheetData.add(rowData);
					}
			}
				workbookData.put(ws.getSheetName(), sheetData);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				wb.close();
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Print Data in Console
		/*
		 * for(String sheet : workbookData.keySet()) {
		 * System.out.println("***************************** " +sheet+
		 * " **************************************"); for(Map<String, String> row :
		 * workbookData.get(sheet)) { for(String key : row.keySet())
		 * System.out.println(key+"---"+row.get(key)); System.out.println(
		 * "------------------------------------------------------------"); } }
		 */		
		return workbookData;
	}

	/*
	 * private String formatCellvalue(String cellValue) {
	 * 
	 * if(cellValue.contains("{{TS}}")) cellValue =
	 * cellValue.replaceAll("\\{\\{TS\\}\\}", ADFBaseScript.timeStamp);
	 * if(cellValue.contains("{{XS}}")) { if("".equals(ADFBaseScript.suffix)) {
	 * cellValue = cellValue.replaceAll("\\{\\{XS\\}\\}", ADFBaseScript.suffix);
	 * cellValue = cellValue.substring(0, cellValue.length()-1); } else cellValue =
	 * cellValue.replaceAll("\\{\\{XS\\}\\}", ADFBaseScript.suffix); }
	 * if(cellValue.equalsIgnoreCase("sysDate")) cellValue =
	 * ADFBaseScript.systemDate;
	 * if(cellValue.matches("sysDate"+"[\\s]*[-+][\\s]*[\\d]*")) {
	 * cellValue=cellValue.replaceAll("\\s", ""); int days =
	 * Integer.parseInt(cellValue.split("[+-]")[1]); Calendar cal =
	 * Calendar.getInstance(); if(cellValue.contains("-")) {
	 * cal.add(Calendar.DAY_OF_MONTH, days*-1); } if(cellValue.contains("+")) {
	 * cal.add(Calendar.DAY_OF_MONTH, days); } cellValue = new
	 * SimpleDateFormat("M/d/yy").format(cal.getTime()); } return cellValue; }
	 */

	private static String cellToString(XSSFCell cell) {
		try{
			int type = cell.getCellType();
			Object result;
			switch (type) {
			case 0:
				result = (long)cell.getNumericCellValue();
				break;
			case 1:
				result = cell.getStringCellValue();
				break;
			case 2:
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK :
				result = null;
				break;
			default:
					result = cell.getRichStringCellValue();
				break;
			}
			return result.toString().trim();
		}catch(NullPointerException e){
			return null;
		}
	}

}

