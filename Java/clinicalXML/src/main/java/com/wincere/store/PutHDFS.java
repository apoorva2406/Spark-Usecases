package com.wincere.store;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PutHDFS {

	public static void main(String[] args) throws IOException {
		  
	  	Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy-hh-mm-ss-a");
		String formattedDate = sdf.format(date);  

		SparkConf conf = new SparkConf().setAppName("PutHDFS").set("spark.yarn.jar","hdfs://172.16.1.218:9000/user/spark-assembly-1.1.1-hadoop2.4.0.jar");
	    JavaSparkContext sc = new JavaSparkContext(conf);

	    JavaRDD<String> file = sc.textFile( "hdfs://172.16.1.218:9000/test_doc");
	  
		JavaRDD<String> filteredRDD = file.filter(new Function<String, Boolean>() {
		  public Boolean call(String s) { return s.contains("hown");}});
		System.out.println(filteredRDD.collect());
		
		filteredRDD.saveAsTextFile("hdfs://172.16.1.218:9000/filterredRDD"+formattedDate);
		
  	}
}
