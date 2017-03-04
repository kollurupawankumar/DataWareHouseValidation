package com.pawan.gzip.mr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.pawan.gzip.utility.ExcelUtility;

public class GzReduce extends Reducer<Text, Text, NullWritable, Text> {
	
	@Override
	protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		Iterator<Text> valuesIT = values.iterator();
		List<Text> testText = new ArrayList<Text>();
		
		while(valuesIT.hasNext()){
			Text text = new Text(valuesIT.next());
			String[] objArr = text.toString().split("\\|",-1);
			String result = "" ;
			for (String obj : objArr){
				if (obj != null){
					obj = obj.trim();
				}
				if (obj.matches("-?\\d+\\.\\d+?")){
					result = result + Double.parseDouble(obj)+"|";
				}else{
					result = result + (obj != null ? obj.trim() : obj)+"|";
				}
			}
			
			Text resulText = new Text(result.substring(0,result.length()-1));
			testText.add(resulText);
			context.write(NullWritable.get(), resulText);
		}
		
		String folderName = context.getConfiguration().get("pawan_output_file");
		new ExcelUtility().writeToExcecl(testText, folderName);
		
	}

}
