package com.wincere.analytics.extract;

import java.sql.SQLException;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.bson.BSONObject;

import com.mongodb.hadoop.MongoInputFormat;


public class ExtractFromMongo {

	public void run(JavaSparkContext sc, JavaSQLContext sqlc) throws ClassNotFoundException, SQLException
	{
		Configuration config = new Configuration();
		config.set("mongo.input.uri",
				"mongodb://172.16.1.37:27017/ViewsInStorm.QueryResponseReport");
		JavaPairRDD<Object,BSONObject> inputRDD=sc.newAPIHadoopRDD(config, MongoInputFormat.class, Object.class, BSONObject.class);
		System.out.println(inputRDD.count());
		
	}
}
