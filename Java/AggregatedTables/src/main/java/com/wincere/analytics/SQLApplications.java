package com.wincere.analytics;

import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.Row;

import com.wincere.aggr_tables.AggregatedTables;
import com.wincere.aggr_tables.AggregationFunctions;

public class SQLApplications {
	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("Aggregated_Table").setMaster(
				"local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaSQLContext sqlContext = new JavaSQLContext(sc);
		
		String masterTablePath = "hdfs://172.16.1.64:9000/testClinicalXML/MasterDump5.parquet";
		String query = "SELECT * from parquetFile  WHERE Level = 'Item' ORDER BY sourceID";
		
		final int groupByIndex = 46;
		final String targetDef="datapointID,studyOID,metaDataVersionOID,siteLocationOID,subjectKey,subjectName,studyEventOID,studyEventRepeatKey,formOID,formRepeatKey,lastUpdatedTimeStamp,lastSourceID,itemGroupOID,itemGroupRepeatKey,itemOID,itemValue,isVerified,isLocked,isFreezed,isReviewed,itemValueLastSourceID,isVerifiedLastSourceID,isLockedLastSourceID,isFreezedLastSourceID,isReviewedLastSourceID";
		String destinationPath = "hdfs://172.16.1.64:9000/testClinicalXML/DatapointsDump3.parquet";
		
		AggregatedTables obj =new AggregatedTables();
		
		JavaPairRDD<String,Iterable<Row>> groupByColumnRDD = obj.createGroupRDD(sc, sqlContext, masterTablePath, query, groupByIndex);
		
		JavaRDD<Map<String, String>> aggrRDD = new AggregationFunctions().datapoints(groupByColumnRDD);
		obj.createAggrTable(sqlContext,aggrRDD,targetDef,destinationPath);
		
}
}
