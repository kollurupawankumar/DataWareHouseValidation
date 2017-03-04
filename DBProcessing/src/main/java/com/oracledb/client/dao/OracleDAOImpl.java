package com.oracledb.client.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class OracleDAOImpl implements OracleDAO {
	
	
	private static String driverName = "oracle.jdbc.driver.OracleDriver";
	private static Connection con = null;
	private static Statement stmt = null;
	
	
	/**
	 * Create the Data base setup
	 * @param hostName
	 * @param port
	 * @param userName
	 * @param password
	 * @param serviceName
	 */
	public OracleDAOImpl(String hostName, String port, String userName, String password, String serviceName) {
		try {
			Class.forName(driverName);
			DriverManager.getConnection("jdbc:oracle:thin@"+hostName, userName, password);
			stmt = con.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getAllRecords(String tableName, String fileName) {
		String flatFileName = fileName + ".txt";
		String excelFileName = fileName + ".xls";
		HSSFWorkbook new_workbook = null;
		
		try {
			new_workbook = new HSSFWorkbook();
			HSSFSheet sheet = new_workbook.createSheet(" Main Sheet");
			String query = "select * from "+tableName;
			ResultSet query_set = stmt.executeQuery(query);
			
			
			/*Create map for excel data and populate data into Map*/
			Map<String , String> excel_data = new HashMap<String, String>();
			ResultSetMetaData metadata =  query_set.getMetaData();
			PrintWriter out = new PrintWriter(flatFileName);
			int noofColumns = metadata.getColumnCount();
			int i=0;
			int row_num = 0;
			
			while(query_set.next()){
				row_num++;
				String s="";
				while (i < noofColumns) {
					i++;
					s=s+query_set.getObject(i)+"|";
				}
				i=0;
				out.println(s.substring(0, s.length()-1));
				excel_data.put(Integer.toString(row_num), s.substring(0, s.length()-1));
			}
			
			out.close();
			query_set.close();
			stmt.close();
			con.close();
			
			
			/*Load data into logical work sheet*/
			Set<String> keyset = excel_data.keySet();
			int rownum =0 ;
			for(String key:keyset){
				Row row = sheet.createRow(rownum++);
				String objArr = excel_data.get(key);
				int cellnum = 0 ;
				String[] strArray = objArr.split("\\|");
				for(String obj : strArray){
					Cell cell = row.createCell(cellnum++);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(obj);
					if (cellnum > noofColumns-1){
						break;
					}
				}
			}
			
			
			FileOutputStream outputFile = new FileOutputStream(new File(excelFileName));
			new_workbook.write(outputFile);
			outputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (new_workbook != null){
				try {
					new_workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	

}
