
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public final class CreateMasterTable implements Serializable {

	public static void main(final String[] args) throws Exception {

		/*
		 * Date date = new Date(); SimpleDateFormat sdf = new
		 * SimpleDateFormat("MM-dd-yyyy-hh-mm-ss-a"); String formattedDate =
		 * sdf.format(date);
		 */

		SparkConf conf = new SparkConf().setAppName("Master Table").setMaster(
				"local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		Configuration config = new Configuration();
		config.set("xmlinput.start", "<ODM");
		config.set("xmlinput.end", "</ODM>");

		JavaRDD<Text> xmlRDD = sc
				.newAPIHadoopFile(
						"hdfs://172.16.1.64:9000/gileadSandBox/GS-US-337-0121/Audit__1000320492.xml",
						XMLInputFormat.class, LongWritable.class, Text.class,
						config).values();
		//System.out.println(xmlRDD.count());

		JavaRDD<List<BasicDBObject>> masterRDD = xmlRDD.map(new Function<Text,List<BasicDBObject>>(){
			public List<BasicDBObject> call (Text xml) throws ParserConfigurationException, SAXException, IOException{
				
				
				List<BasicDBObject> bsonList = new ArrayList<BasicDBObject>();
				
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setValidating(false);
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource();
				String str = xml.toString();
				is.setCharacterStream(new StringReader(str));
				Document doc = builder.parse(is);

				
				NodeList nl = doc.getElementsByTagName("*");
				int count = 0;

				for (int i = 0; i < nl.getLength(); i++)

				{
					if (nl.item(i).getNodeName().equals("ClinicalData")) {
						count++;

						BasicDBObject bson = new BasicDBObject();
						Element clinical = (Element) nl.item(i);

						if (clinical.getElementsByTagName("ItemData")
								.getLength() > 0) {
							bson.put("Level", "Item");
						}

						else if (clinical.getElementsByTagName(
								"ItemGroupData").getLength() > 0) {
							bson.put("Level", "ItemGroup");
						}

						else if (clinical.getElementsByTagName(
								"FormData").getLength() > 0) {
							bson.put("Level", "Form");
						}

						else if (clinical.getElementsByTagName(
								"StudyEventData").getLength() > 0) {
							bson.put("Level", "StudyEvent");
						}

						else {
							bson.put("Level", "Subject");
						}

						if (count >= 1) {
							bsonList.add(bson);
						}

						do {
							NamedNodeMap nnm = nl.item(i)
									.getAttributes();

							if (nnm != null) {
								if (nnm.getLength() == 0) {
									if (nl.item(i).getTextContent() != null) {
										System.out
												.println(nl.item(i)
														.getNodeName()
														+ nl.item(i)
																.getTextContent());

										bson.put(nl.item(i)
												.getNodeName(), nl
												.item(i)
												.getTextContent());
									}
								}

								for (int j = 0; j < nnm.getLength(); j++) {
									Node nsAttr = nnm.item(j);

									bson.put(nsAttr.getNodeName(),
											nsAttr.getNodeValue());

								}
							}
							i++;
						} while (nl.item(i) != null
								&& nl.item(i).getNodeName() != null
								&& !nl.item(i).getNodeName()
										.equals("ClinicalData"));
						i--;

					}
				}
				
				
				return bsonList;
				

			}});
		
		
		JavaRDD<BasicDBObject> masterRDD2 = masterRDD
				.flatMap(new FlatMapFunction<List<BasicDBObject>, BasicDBObject>() {
					public List<BasicDBObject> call(List<BasicDBObject> q) {
						return q;
					}
				});
		
		JavaRDD<BasicDBObject> masterRDD3=masterRDD2.map(new Function<BasicDBObject,BasicDBObject>(){
			public BasicDBObject call(BasicDBObject b){
			
				try {
					MongoClient mongoClient = new MongoClient(
							new ServerAddress("203.122.26.109", 27017));

				
					DB db = mongoClient.getDB("SparkBatch");

					DBCollection coll = db
							.getCollection("Master1");
					coll.insert(b);
					mongoClient.close();
				}
				catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				return b;
			}
		});
		
		System.out.println(masterRDD3.count());

			

	}
}
