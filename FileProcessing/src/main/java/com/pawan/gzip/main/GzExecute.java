package com.pawan.gzip.main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.pawan.gzip.mr.GzMapper;
import com.pawan.gzip.mr.GzReduce;

public class GzExecute extends Configured implements Tool {
	
	
	public static int main(String[] args) throws Exception {
		int exitcode = ToolRunner.run(new GzExecute(), args);
		if (exitcode != 0){
			throw new Exception("MR : Exception in parsing the file details");
		}
		return exitcode;
	}

	public int run(String[] args) throws Exception {
		if (args.length != 2){
			System.err.printf("Usage: %s [generic options] <input> <output>",getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		Configuration conf = getConf();
		conf.set("fs.default.name", "file:///");
		conf.set("mapred.job.tracker", "local");
		conf.set("fs.file.impl", "com.pawan.gzip.filesystem.PawanFileSystem");
		conf.set("io.serializations", "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
		conf.set("pawan_output_file", args[1]);
		
		try {
			Job job = new Job(conf,"File processing");
			job.setJarByClass(GzExecute.class);
			job.setJobName("File Processing");
			
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			FileInputFormat.addInputPath(job, new Path(args[0]));
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(Text.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			job.setMapperClass(GzMapper.class);
			job.setReducerClass(GzReduce.class);
			int ret = job.waitForCompletion(true) ? 0 : 1;
			System.out.println("Job successful");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 255;
	}

}
