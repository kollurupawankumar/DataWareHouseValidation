package com.hive.client.dao;


import java.util.List;

public interface HiveDAO {
	
	public List<String> getAllRecords(String tableName, String fileName);

}
