package com.wincere.analytics;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

import com.wincere.extract.ExtractFromKafka;
import com.wincere.transform.MasterTable;

public class StreamingApplication {
	public static void main(final String[] args) throws Exception {

		SparkConf conf = new SparkConf().setAppName("T2");
				//.setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(5000));
		
		int sparkProcessingParallelism=1;
		
		JavaPairDStream<String, String> repartitionedKafkaStream=new ExtractFromKafka().run(sc, ssc, sparkProcessingParallelism);
		System.out.println(repartitionedKafkaStream.count());
		
		
		new MasterTable().createMasterTable(sc, ssc, repartitionedKafkaStream);
		
		
		ssc.start();
		ssc.awaitTermination();
	}
}
