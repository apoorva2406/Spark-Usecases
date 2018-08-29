package com.wincere.analytics.transform;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.DataType;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;
import org.apache.spark.sql.api.java.UDF1;


public class TestUDF implements Serializable {

	public void UDFTest(
			JavaSQLContext sqlc, JavaSchemaRDD schemaDatapoints) {

		schemaDatapoints.registerTempTable("Datapoints");
		
		
		UDF1<Integer,Integer> u=new UDF1<Integer,Integer>() {
			public Integer call(Integer i) {
				return 1;
			}};
		
		sqlc.registerFunction("tF", u,DataType.IntegerType);
		
	
		JavaSchemaRDD queryTable = sqlc
				.sql("SELECT * FROM Datapoints limit 100");
		
		List<Integer> user2 = queryTable.map(new Function<Row, Integer>() {
	  		  public Integer call(Row row) {
	  			System.out.println("hello");
	  		    return row.getInt(0);
	 
	  		  }
	  		}).collect();
	    	System.out.println(user2);

	}

}
