package com.pawan.gzip.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {
	
	public boolean writeToExcecl(List<Text> testText,String folderName ){
		XSSFWorkbook workbook = new XSSFWorkbook();
		String fileName = folderName + "\\output.xls";
		XSSFSheet sheet = workbook.createSheet("File Sheet");
		int lastrowNumber = 0;
		for (Text text : testText){
			Row row = sheet.createRow(lastrowNumber++);
			String[] objArr = text.toString().split("\\|"); 
			int cellNum = 0;
			for (String obj : objArr){
				Cell cell = row.createCell(cellNum++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.trim());
			}
		}
		
		try{
			FileOutputStream  out = new FileOutputStream(new File(fileName));
			workbook.write(out);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

}
