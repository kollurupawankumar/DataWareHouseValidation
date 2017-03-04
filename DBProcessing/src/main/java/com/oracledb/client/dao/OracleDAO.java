package com.oracledb.client.dao;

import java.util.List;

public interface OracleDAO {
	
	public List<String> getAllRecords(String tableName, String fileName);

}
