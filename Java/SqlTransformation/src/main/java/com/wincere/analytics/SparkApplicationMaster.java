package com.wincere.analytics;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;

import com.wincere.analytics.extract.ExtractFromHDFS;
import com.wincere.analytics.extract.ExtractFromMongo;

public class SparkApplicationMaster {
	public static void main(final String[] args) throws Exception {

		SparkConf conf = new SparkConf().setAppName("ETL Transformations");
				//.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaSQLContext sqlc = new JavaSQLContext(sc);

		new ExtractFromHDFS().run(sc, sqlc);
		//new ExtractFromMongo().run(sc, sqlc);
	}
}
