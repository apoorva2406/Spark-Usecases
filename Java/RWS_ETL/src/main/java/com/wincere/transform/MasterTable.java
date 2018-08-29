package com.wincere.transform;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import org.apache.spark.Accumulator;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.wincere.map.ClinicalMap;

import scala.Tuple2;

public class MasterTable implements Serializable {

	public void createMasterTable(JavaSparkContext sc,
			JavaStreamingContext ssc,
			JavaPairDStream<String, String> repartitionedKafkaStreamIdMessage) {
		
		final Accumulator<Integer> numInputMessages = ssc.sparkContext().accumulator(
				0, "Kafka messages consumed");
		
		JavaDStream<String> KafkaStreamMessage = repartitionedKafkaStreamIdMessage
				.map(new Function<Tuple2<String, String>, String>() {
					public String call(Tuple2<String, String> t) {
						return t._2;
					}

				});

		KafkaStreamMessage.foreachRDD(new Function<JavaRDD<String>, Void>() {
			public Void call(JavaRDD<String> rdd) throws JAXBException {

				numInputMessages.add(1);
				System.out.println(numInputMessages);
				System.out.println("Partition Count in a RDD: "+rdd.partitions().size());
				List<String> xmlList = rdd.collect();
				System.out.println("Number of element in a RDD: " + rdd.count());
				if (xmlList != null && xmlList.size()>0) {

					try {
						MongoClient mongoClient = new MongoClient(
								new ServerAddress("203.122.26.109", 27017));

						System.out.println("hello");
						
						DB db = mongoClient.getDB("SparkStreaming");

						DBCollection coll = db
								.getCollection("FinalPOC1");
						
						for (int i = 0; i < xmlList.size(); i++) {
							Map<String, String> m = new ClinicalMap()
									.parseXLMDoc(xmlList.get(i));
							BasicDBObject dbObject = new BasicDBObject();
							Iterator<Entry<String, String>> entries = m
									.entrySet().iterator();

							while (entries.hasNext()) {

								Entry<String, String> entry = entries.next();
								if (entry.getValue() != null && !entry.getValue().equals(""))
									dbObject.append(entry.getKey(),
											entry.getValue());

							}
							System.out.println("insert in mongo");
							coll.insert(dbObject);

						}
						System.out.println("bye");
						mongoClient.close();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				return null;
			}
		});

	}

}
