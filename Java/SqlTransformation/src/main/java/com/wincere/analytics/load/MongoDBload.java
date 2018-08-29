package com.wincere.analytics.load;

import java.io.Serializable;
import java.lang.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.hadoop.MongoOutputFormat;

import scala.Tuple2;

public class MongoDBload implements Serializable {

	public void load(JavaRDD<HashMap<String, Object>> table) {

		Configuration config = new Configuration();
		//config.set("mongo.auth.uri", "mongodb://superuser:12345678@172.16.1.171:27017/admin");
		config.set("mongo.output.uri",
				"mongodb://203.122.26.109:27017/SQL_Batch_Demo.queryResponseReport");
		JavaPairRDD<Object, BSONObject> mongoRDD = table
				.mapToPair(new PairFunction<HashMap<String, Object>, Object, BSONObject>() {
					public Tuple2<Object, BSONObject> call(
							HashMap<String, Object> s) throws ParseException {

						BSONObject bson = new BasicBSONObject();
						Iterator<Entry<String, Object>> entries = s.entrySet()
								.iterator();

						while (entries.hasNext()) {

							Entry<String, Object> entry = entries.next();
							bson.put(entry.getKey(), entry.getValue());
						}
						return new Tuple2<>(null, bson);
					}
				});

		mongoRDD.saveAsNewAPIHadoopFile("file:///bogus", Object.class,
				Object.class, MongoOutputFormat.class, config);

	}

}
