package com.wincere.extract;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import kafka.javaapi.producer.Producer;

public class HDFStoKafka {

	public static void main(final String[] args) throws Exception {

		SparkConf conf = new SparkConf().setAppName("HDFStoKafka").setMaster(
				"local[1]");
		// conf1.set("spark.serializer",
		// "org.apache.spark.serializer.KryoSerializer");
		JavaSparkContext sc = new JavaSparkContext(conf);
		// JavaStreamingContext ssc = new JavaStreamingContext(sc, new
		// Duration(1000));

		Configuration config = new Configuration();
		config.set("xmlinput.start", "<ODM");
		config.set("xmlinput.end", "</ODM>");
		JavaRDD<Text> clinicalRDD = sc.newAPIHadoopFile(
				"hdfs://172.16.1.64:9000/gileadSandBox/GS-US-337-0121/Audit__.xml",
				XMLInputFormat.class, LongWritable.class, Text.class, config)
				.values();

		JavaRDD<Text> delay = clinicalRDD.map(new Function<Text, Text>() {
			public Text call(Text s) throws IOException {

				Date date = new Date();
				long l = date.getTime();
				// for (int i = 1; i < 5; i++)
				for (int j = 1; j < 100000; j++)
					for (int k = 1; k < 100000; k++)
						l = l + 1;

				/*
				 * ProducerSpeedLayer pl = new ProducerSpeedLayer();
				 * Producer<String, String> p; p = pl.SetProducerProperties();
				 * String newLineRemoveResponse = s.toString(); pl.AddData(p,
				 * newLineRemoveResponse);
				 */

				KafkaConfig kt = new KafkaConfig();
			//	try {
				//	kt.createtopic("xmlDump4");
			//	} catch (kafka.common.TopicExistsException a) {
			//	}
				Producer<String, String> p = kt.SetProducerProperties();
				kt.AddData(p, s.toString(), "kstest4", "partitionKey");

				System.out.println("hi" + l);

				return s;
			}
		});

		System.out.println(delay.count());

	}
}