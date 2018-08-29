package com.wincere.extract;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.Accumulator;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.*;
import kafka.serializer.StringDecoder;
import org.apache.spark.storage.StorageLevel;

public class ExtractFromKafka {

	public JavaPairDStream<String, String> run(JavaSparkContext sc,
			JavaStreamingContext ssc, int sparkProcessingParallelism) {

		
		  Map<String, String> kafkaParam = new HashMap<String, String>();
		  kafkaParam.put("zookeeper.connect",
		  "172.16.1.83:2181,172.16.1.162:2181,172.16.1.37:2181,172.16.1.54:2181,172.16.1.64:2181"
		//		  "172.16.1.151:2181"
		  ); kafkaParam.put("group.id", "FinalPOC1");
		  kafkaParam.put("zookeeper.connection.timeout.ms", "10000");
		  kafkaParam.put("fetch.message.max.bytes", "40000000");
		  
		 

		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		topicMap.put("FinalTopic1", 1);

		/*JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils				
				.createStream(
						ssc,
						"172.16.1.83:2181,172.16.1.162:2181,172.16.1.37:2181,172.16.1.54:2181,172.16.1.64:2181",
						"spark-streaming-test11", topicMap);
*/
		
		JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils				
				.createStream(ssc, java.lang.String.class , java.lang.String.class, kafka.serializer.StringDecoder.class, kafka.serializer.StringDecoder.class, kafkaParam, topicMap, StorageLevel.MEMORY_AND_DISK_SER_2());
						
		
		
		
		JavaPairDStream<String, String> repartitionedKafkaStream = kafkaStream
				.repartition(sparkProcessingParallelism);
		
		return repartitionedKafkaStream;
	}

};
