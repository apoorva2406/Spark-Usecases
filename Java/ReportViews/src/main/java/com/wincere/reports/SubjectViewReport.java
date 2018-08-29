package com.wincere.reports;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.api.java.DataType;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;
import org.apache.spark.sql.api.java.StructField;
import org.apache.spark.sql.api.java.StructType;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import scala.Tuple2;

public class SubjectViewReport {
	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("Subject_View_Report")
				.setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaSQLContext sqlContext = new JavaSQLContext(sc);

		JavaSchemaRDD datapointsDump = sqlContext
				.parquetFile("hdfs://172.16.1.64:9000/testClinicalXML/DatapointsDump3.parquet");
		JavaSchemaRDD subjectDump = sqlContext
				.parquetFile("hdfs://172.16.1.64:9000/testClinicalXML/subjectDump9.parquet");

		datapointsDump.registerTempTable("Datapoints");
		subjectDump.registerTempTable("Subjects");

		JavaSchemaRDD filteredDatapoint = sqlContext
				.sql("SELECT subjectKey,itemOID,itemValue from Datapoints WHERE itemOID = 'DM.AGE' or itemOID = 'DM.RACE' or itemOID = 'DM.SEX' or itemOID = 'DM.GEN' or itemOID = 'DM.ETHNIC' or itemOID = 'IC_ELIG.DSSTDAT'"
						+ "or itemOID = 'IC_ELIG.IEMETALL' or itemOID = 'IC_ELIG.IEREAS' or itemOID = 'IC_ELIG.SFREAS' or itemOID = 'DEATH.DEATHDAT' or itemOID = 'DEATH.DEATHCAU' or itemOID = 'DEATH.AUTOPSY'");

		JavaPairRDD<String, Iterable<Row>> groupByColumnRDD = filteredDatapoint
				.groupBy(new Function<Row, String>() {
					public String call(Row r) {
						return r.getString(0);
					}
				});

		final String tempDef = "subjectKey,age,race,sex,gender,ethnicity,informedConsentDate,subjectMetIECriteria,enrolledReason,screenFailureReason,deathDate,deathCause,deathAutopsy,death";

		JavaRDD<Map<String, String>> aggrRDD = groupByColumnRDD
				.map(new Function<Tuple2<String, Iterable<Row>>, Map<String, String>>() {
					public Map<String, String> call(
							Tuple2<String, Iterable<Row>> t) {

						Map<String, String> m = new HashMap<String, String>();
						Iterable<Row> r = t._2;
						Iterator<Row> itr = r.iterator();

						m.put("subjectKey", null);
						m.put("age", null);
						m.put("race", null);
						m.put("sex", null);
						m.put("gender", null);
						m.put("ethnicity", null);
						m.put("informedConsentDate", null);
						m.put("subjectMetIECriteria", null);
						m.put("enrolledReason", null);
						m.put("screenFailureReason", null);
						m.put("deathDate", null);
						m.put("deathCause", null);
						m.put("deathAutopsy", null);
						m.put("death", "0");

						while (itr.hasNext()) {
							Row row = itr.next();
							m.put("subjectKey", row.getString(0));
							if (row.getString(1).equals("DM.AGE"))
								m.put("age", row.getString(2));
							if (row.getString(1).equals("DM.RACE"))
								m.put("race", row.getString(2));
							if (row.getString(1).equals("DM.SEX"))
								m.put("sex", row.getString(2));
							if (row.getString(1).equals("DM.GEN"))
								m.put("gender", row.getString(2));
							if (row.getString(1).equals("DM.ETHNIC"))
								m.put("ethnicity", row.getString(2));
							if (row.getString(1).equals("IC_ELIG.DSSTDAT"))
								m.put("informedConsentDate", row.getString(2));
							if (row.getString(1).equals("IC_ELIG.IEMETALL"))
								m.put("subjectMetIECriteria", row.getString(2));
							if (row.getString(1).equals("IC_ELIG.IEREAS"))
								m.put("enrolledReason", row.getString(2));
							if (row.getString(1).equals("IC_ELIG.SFREAS"))
								m.put("screenFailureReason", row.getString(2));
							if (row.getString(1).equals("DEATH.DEATHDAT"))
								m.put("deathDate", row.getString(2));
							if (row.getString(1).equals("DEATH.DEATHCAU"))
								m.put("deathCause", row.getString(2));
							if (row.getString(1).equals("DEATH.AUTOPSY"))
								m.put("deathAutopsy", row.getString(2));
							if (row.getString(1).startsWith("DEATH."))
								m.put("death", "1");

						}
						return m;
					}
				});

		List<StructField> fields = new ArrayList<StructField>();

		for (String fieldName : tempDef.split(",")) {
			fields.add(DataType.createStructField(fieldName.trim(),
					DataType.StringType, true));
		}
		StructType schema = DataType.createStructType(fields);

		JavaRDD<Row> rowRDD = aggrRDD
				.map(new Function<Map<String, String>, Row>() {
					public Row call(Map<String, String> record)
							throws Exception {

						List<String> list = new ArrayList<String>();
						for (String i : tempDef.split(",")) {
							if (record.containsKey(i)) {
								list.add(record.get(i));
							} else
								list.add(null);
						}

						return Row.create(list.toArray());
					}
				});

		JavaSchemaRDD schemaTempTable = sqlContext.applySchema(rowRDD, schema);
		schemaTempTable.registerTempTable("modifiedDatapoints");

		JavaSchemaRDD subView = sqlContext
				.sql("SELECT * FROM Subjects left join modifiedDatapoints on Subjects.subjectKey=modifiedDatapoints.subjectKey");

		final String viewSchema = subView.schemaString();

		subView.foreachPartition(new VoidFunction<Iterator<Row>>() {
			public void call(Iterator<Row> itr) {

				try {
					MongoClient mongoClient = new MongoClient(
							new ServerAddress("203.122.26.109", 27017));

					DB db = mongoClient.getDB("SparkBatch");

					DBCollection coll = db.getCollection("SubjectView");

					while (itr.hasNext()) {
						Row row = itr.next();
						BasicDBObject dbObject = new BasicDBObject();

						int j = -1;
						String[] parts = viewSchema.split("\n");
						for (String s : parts) {
							String[] keyValues = s.split(" ");

							if (keyValues.length == 1)
								continue;

							j++;
							String key = keyValues[2].substring(0,
									keyValues[2].length() - 1);
							;
							String value = keyValues[3];

							if (row.isNullAt(j))
								dbObject.put(key, null);
							else
								dbObject.put(key, row.get(j));

						}

						coll.insert(dbObject);

					}
					mongoClient.close();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		
		

	}
}
