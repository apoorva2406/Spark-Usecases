package com.wincere.analytics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;

public class Read_parquet {
	public static void main(final String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("Parquet_read").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaSQLContext sqlContext = new JavaSQLContext(sc);
		JavaSchemaRDD parquetFile = sqlContext.parquetFile("hdfs://172.16.1.64:9000/testClinicalXML/subjectDump9.parquet");
    	parquetFile.registerTempTable("parquetFile");
    	//System.out.println(parquetFile.schemaString());
    	//System.out.println(parquetFile.count());
    	
    	JavaSchemaRDD s = sqlContext.sql("SELECT subjectStatus from parquetFile");
    	
    	System.out.println(s.collect());
    	/*
    	List<String> result = s.map(new Function<Row, String>() {
    		  public String call(Row row) {

    			String val=row.getString(8)+"----"+row.getString(9)+"----"+row.getString(38);

    		    return val;

   
    		  }
    		}).collect();
    	System.out.println(result);
    	
    	JavaSchemaRDD s1 = s.filter(new Function<Row, Boolean>() {
    		  public Boolean call(Row row) {
    			
    			  return row.isNullAt(15);
    		    				
    		  }
    		});
      	
      	System.out.println(s1.collect());
      	*/
    	
    	
    /*
    	JavaSchemaRDD s = sqlContext.sql("SELECT created,lastEnteredDate from parquetFile");
    	System.out.println("Total number of rows in the table"+s.count());
    	JavaSchemaRDD s1 = s.filter(new Function<Row, Boolean>() {
  		  public Boolean call(Row row) {
  			
  			if (!(row.isNullAt(0) || row.isNullAt(1)))  
  				{
  					System.out.println("*");
  					return row.getString(1).contains("2008-03-27");
  				}
  			else
  				{
					System.out.println("^");
					return false;
				}
  				
  		  }
  		});
    	
    	System.out.println("No. of rows created on 2008-03-27 :"+s1.count());
    	
    	List<String> result = s1.map(new Function<Row, String>() {
  		  public String call(Row row) throws ParseException {

  			String val1=row.getString(0);
  			String val2=row.getString(1);
  			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
  		    Date d1 =  format.parse(val1); 
  		    Date d2 =  format.parse(val2);
  		    long diff = d1.getTime() - d2.getTime();
  		    return diff+"";

 
  		  }
  		}).collect();
    	System.out.println("Time difference:"+result);*/
    	
	}
}