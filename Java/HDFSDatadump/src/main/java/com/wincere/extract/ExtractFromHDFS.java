package com.wincere.extract;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.JavaSQLContext;

import com.wincere.analytics.xobject.audit.ClinicalData;
import com.wincere.analytics.xobject.audit.ODM;

public class ExtractFromHDFS implements Serializable {

	public JavaRDD<ClinicalData> run(JavaSparkContext sc)
			throws ClassNotFoundException, SQLException {

		Configuration config = new Configuration();
		config.set("xmlinput.start", "<ODM");
		config.set("xmlinput.end", "</ODM>");

		JavaRDD<Text> xmlRDD = sc
				.newAPIHadoopFile(
						"hdfs://172.16.1.64:9000/gileadSandBox/SampleDataset/*",
						//"hdfs://172.16.1.64:9000/gileadSandBox/GS-US-337-0121/Audit__1000320492.xml",
						XMLInputFormat.class, LongWritable.class, Text.class,
						config).values();

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

						if (studyODM.clinicalData != null)
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

		return clinicalData;

	}
}
