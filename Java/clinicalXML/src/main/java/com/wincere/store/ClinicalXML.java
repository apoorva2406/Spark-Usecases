package com.wincere.store;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import com.wincere.analytics.xobject.audit.ClinicalData;
import com.wincere.analytics.xobject.audit.ODM;
import com.wincere.common.KafkaConfig;
import com.wincere.common.XMLInputFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import kafka.javaapi.producer.Producer;

public final class ClinicalXML extends Configured implements Serializable {

	public static void main(final String[] args) throws Exception {

		SparkConf conf1 = new SparkConf().setAppName("CLinical XML")
		.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf1);

		Configuration conf = new Configuration();
		conf.set("xmlinput.start", "<ODM");
		conf.set("xmlinput.end", "</ODM>");
		conf.set(
				"io.serializations",
				"org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");

		/*
		JavaPairRDD<LongWritable, Text> xRDD5M = sc.newAPIHadoopFile(
				"hdfs://172.16.1.64:9000/gileadSandBox/GS-US-337-0121/Audit__5*", XMLInputFormat.class,
				LongWritable.class, Text.class, conf);
		JavaPairRDD<LongWritable, Text> xRDD6M = sc.newAPIHadoopFile(
				"hdfs://172.16.1.64:9000/gileadSandBox/GS-US-337-0121/Audit__6*", XMLInputFormat.class,
				LongWritable.class, Text.class, conf);
		
		JavaRDD<Text> xmlRDDUnion = xRDD5M.values().union(xRDD6M.values());
		JavaRDD<Text> xmlRDD = xmlRDDUnion.repartition(10);
		*/
		JavaPairRDD<LongWritable, Text> xRDD = sc.newAPIHadoopFile(
				"hdfs://172.16.1.64:9000/gileadSandBox/GS-US-337-0131/Audit*", XMLInputFormat.class,
				LongWritable.class, Text.class, conf);
		JavaRDD<Text> xmlRDD = xRDD.values();

		// xmlRDD.cache();
		JavaRDD<ODM> odmObject = xmlRDD.map(new Function<Text, ODM>() {
			public ODM call(Text s) throws IOException, JAXBException {

				JAXBContext jaxbContext = JAXBContext.newInstance(ODM.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();

				StringReader reader = new StringReader(s.toString());
				ODM studyODM = (ODM) jaxbUnmarshaller.unmarshal(reader);
				System.out.println(studyODM.FileOID);
				System.out.println(studyODM.clinicalData==null);
				return studyODM;
			}
		});


		JavaRDD<List<ClinicalData>> cData = xmlRDD
				.map(new Function<Text, List<ClinicalData>>() {
					public List<ClinicalData> call(Text s) throws IOException,
							JAXBException {

						List<ClinicalData> cData = new ArrayList<ClinicalData>();

						JAXBContext jaxbContext = JAXBContext
								.newInstance(ODM.class);
						Unmarshaller jaxbUnmarshaller = jaxbContext
								.createUnmarshaller();

						StringReader reader = new StringReader(s.toString());

						ODM studyODM = (ODM) jaxbUnmarshaller.unmarshal(reader);
						
						if(studyODM.clinicalData!=null)
							cData = studyODM.clinicalData;

						return cData;
					}
				});

		
		JavaRDD<ClinicalData> clinicalData = cData
				.flatMap(new FlatMapFunction<List<ClinicalData>, ClinicalData>() {
					public List<ClinicalData> call(List<ClinicalData> c) {
						return c;
					}
				});


	/*	JavaRDD<ClinicalData> c = clinicalData
				.map(new Function<ClinicalData, ClinicalData>() {
					public ClinicalData call(ClinicalData s)
							throws IOException, JAXBException {

						if (s.getSubjectData() != null
								&& s.getSubjectData().getStudies() != null) {
							if (s.getSubjectData().getStudies()
									.getFormData() != null)
								System.out.println(s.getSubjectData()
										.getStudies().getFormData()
										.getFormOID());
						}
						
						return s;
					}
				});

		System.out.println(c.count());
		
		*/
		
		JavaRDD<String> c=clinicalData.map(new Function<ClinicalData, String>() {
			public String call(ClinicalData s)
					throws IOException, JAXBException {
				
				
		           JAXBContext context = JAXBContext.newInstance(ODM.class);
		           java.io.StringWriter sw = new StringWriter();

				    Marshaller marshaller = context.createMarshaller();
				    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				    marshaller.marshal(s, sw);
				    //System.out.println(sw.toString());
	        
		            //Send to Kafka
				    
		            KafkaConfig kt = new KafkaConfig();
						Producer<String, String> p = kt.SetProducerProperties();
						kt.AddData(p, sw.toString(), "TestIndex6", "partitionKey");
						
						
		            
			return  sw.toString();
				
			}});
		
		System.out.println(c.count());

	}
}
