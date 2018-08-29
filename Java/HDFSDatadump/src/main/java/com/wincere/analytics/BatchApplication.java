package com.wincere.analytics;

import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;
import org.apache.spark.sql.api.java.StructType;

import com.wincere.analytics.xobject.audit.ClinicalData;
import com.wincere.extract.ExtractFromHDFS;
import com.wincere.map.ClinicalMap;
import com.wincere.map.MapSchema;

public class BatchApplication {
	public static void main(final String[] args) throws Exception {

		SparkConf conf = new SparkConf().setAppName("Creat_Master_Table")
		.setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaSQLContext sqlc = new JavaSQLContext(sc);

		JavaRDD<ClinicalData> clinicalData=new ExtractFromHDFS().run(sc);
		JavaRDD<Map<String, String>> clinicalMap = new ClinicalMap().run(clinicalData);
		
		final String masterDef = "Level,AuditSubCategoryName,studyOID,metaDataVersionOID,siteLocationOID,subjectKey,subjectKeyType,subjectName,subjectStatus,studyEventOID,studyEventRepeatKey,formOID,formRepeatKey,itemGroupOID,itemGroupRepeatKey,measurementUnitRef,itemOID,itemTransactionType,itemValue,itemVerify,itemLock,itemFreeze,queryRepeatKey,queryRecipient,queryStatus,queryValue,commentRepeatKey,commentTransactionType,commentValue,groupName,reviewed,pdTransactionType,pdValue,pdStatus,pdCode,pdclassName,protocolDeviationRepeatKey,auditDateTimeStamp,auditReasonForChange,sourceID,auditUserOID,auditUserLocationOID,signatureUserRef,signatureLocationRef,signatureRef,signatureDateTimeStamp,datapointID";
		
		MapSchema m = new MapSchema();
		StructType masterSchema = m.createSchema(masterDef);

		JavaRDD<Row> masterRowRDD = m.createRowRDD(clinicalMap, masterDef);
		JavaSchemaRDD schemaMasterTable = sqlc.applySchema(masterRowRDD, masterSchema);
		
		schemaMasterTable.saveAsParquetFile("hdfs://172.16.1.64:9000/testClinicalXML/MasterDump5.parquet");
		
		

	}
}
