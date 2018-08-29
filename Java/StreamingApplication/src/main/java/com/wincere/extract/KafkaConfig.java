package com.wincere.extract;

import java.util.Properties;
import java.lang.*;

import org.I0Itec.zkclient.ZkClient;

import kafka.admin.AdminUtils;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.utils.ZKStringSerializer$;

public class KafkaConfig {
	public void createtopic(String topicName){
		int sessionTimeoutMs = 10000;
		
		int connectionTimeoutMs = 10000;
		ZkClient zkClient = new ZkClient("172.16.1.83:2181,172.16.1.162:2181,172.16.1.37:2181,172.16.1.54:2181,172.16.1.64:2181",sessionTimeoutMs,connectionTimeoutMs, ZKStringSerializer$.MODULE$);
		
	
		// Create a topic named "myTopic" with 8 partitions and a replication factor of 3
		 
		int numPartitions = 1;
		int replicationFactor = 1;
		Properties topicConfig = new Properties();
		
		
		
		AdminUtils.createTopic(zkClient, topicName, numPartitions, replicationFactor,topicConfig);
		
		
	}
	
	public  Producer<String, String> SetProducerProperties() {
		Producer<String, String> producer;
		Properties props = new Properties();	 
		props.put("metadata.broker.list", "172.16.1.162:9092,172.16.1.83:9092");
			
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		//props.put("zk.connect","172.16.1.161:2181");
		
		
		//props.put("partitioner.class", "example.producer.SimplePartitioner");
		props.put("request.required.acks", "1");	 
				
				ProducerConfig config = new ProducerConfig(props);
				producer = new Producer<String, String>(config);
				return producer;
	}
	
	
	public void AddData(Producer<String, String> producer,String allData,String topicName,String ip) {
		
		
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(topicName, ip,allData);
		producer.send(data);
		producer.close();
	}
	
	public static void main(String []args){
		KafkaConfig kt = new KafkaConfig();
		try{
			kt.createtopic("xmlDump5");
		}catch(kafka.common.TopicExistsException a){}
		//Producer<String, String> p = kt.SetProducerProperties();
		//String ip  = "192.168.2."+ "2";
		//kt.AddData(p, "santacause11111", "santa1", ip);
	}
	

}
