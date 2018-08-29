package com.wincere.extract;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

//https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
public class ProducerSpeedLayer  {

	public  Producer<String, String> SetProducerProperties() {
		Producer<String, String> producer;
		Properties props = new Properties();	 
		props.put("metadata.broker.list", "172.16.1.175:9092");		
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		//props.put("partitioner.class", "example.producer.SimplePartitioner");
		props.put("request.required.acks", "1");	 
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);
		return producer;
	}
	
	
	public void AddData(Producer<String, String> producer,String allData) {
		
		
		KeyedMessage<String, String> data = new KeyedMessage<String, String>("topictest2", allData);
		producer.send(data);
		producer.close();
	}

}