package test.pack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

public class TestClass {
	public static void main(final String[] args) throws Exception {

		SparkConf conf = new SparkConf().setAppName("T1");
		// .setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(
				5000));

		int sparkProcessingParallelism = 1;

		Map<String, String> kafkaParam = new HashMap<String, String>();
		kafkaParam.put("zookeeper.connect",
		 "172.16.1.83:2181,172.16.1.162:2181,172.16.1.37:2181,172.16.1.54:2181,172.16.1.64:2181"
				//"172.16.1.151:2181"
				);
		kafkaParam.put("group.id", "testGroup1");
		kafkaParam.put("zookeeper.connection.timeout.ms", "10000");
		kafkaParam.put("fetch.message.max.bytes", "40000000");

		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		topicMap.put("testTopic1", 1);

		/*
		 * JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils
		 * .createStream( ssc,
		 * "172.16.1.83:2181,172.16.1.162:2181,172.16.1.37:2181,172.16.1.54:2181,172.16.1.64:2181"
		 * , "spark-streaming-test11", topicMap);
		 */

		JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils
				.createStream(ssc, java.lang.String.class,
						java.lang.String.class,
						kafka.serializer.StringDecoder.class,
						kafka.serializer.StringDecoder.class, kafkaParam,
						topicMap, StorageLevel.MEMORY_AND_DISK_SER_2());

		JavaPairDStream<String, String> repartitionedKafkaStream = kafkaStream
				.repartition(sparkProcessingParallelism);

		
		
		JavaDStream<String> KafkaStreamMessage = repartitionedKafkaStream
				.map(new Function<Tuple2<String, String>, String>() {
					public String call(Tuple2<String, String> t) {
						return t._2;
					}

				});

		KafkaStreamMessage.foreachRDD(new Function<JavaRDD<String>, Void>() {
			public Void call(JavaRDD<String> rdd){

				System.out.println("Partition Count in a RDD: "+rdd.partitions().size());
				List<String> xmlList = rdd.collect();
				System.out.println("Number of element in a RDD: " + rdd.count());
				System.out.println(rdd.collect());
				
				return null;
		
			}});
		
		ssc.start();
		ssc.awaitTermination();
	}
}
