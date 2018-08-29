package com.wincere.analytics.extract;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.DataType;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;
import org.apache.spark.sql.api.java.StructField;
import org.apache.spark.sql.api.java.StructType;

import com.wincere.analytics.definitions.Datapoints_Def;
import com.wincere.analytics.definitions.Queries_Def;
import com.wincere.analytics.load.MongoDBload;
import com.wincere.analytics.schema.mapping.MapSchema;
import com.wincere.analytics.transform.QueriesTable;
import com.wincere.analytics.transform.TestUDF;

public class ExtractFromHDFS {

	public void run(JavaSparkContext sc, JavaSQLContext sqlc) throws ClassNotFoundException, SQLException
	{
		
		JavaRDD<String> queriesRDD = sc
				.textFile("hdfs://172.16.1.64:9000/WincereSQLServer/sample/query.csv");
		JavaRDD<String> datapointsRDD = sc
				.textFile("hdfs://172.16.1.64:9000/datapoints.csv");
	
		final String queryDef = new Queries_Def().getQueriesDef();
		System.out.println(queryDef);
		
		final String datapointsDef = new Datapoints_Def().getDatapointsDef();
		System.out.println(datapointsDef);

		MapSchema m = new MapSchema();
		StructType querySchema = m.createSchema(queryDef);
		StructType datapointsSchema = m.createSchema(datapointsDef);

		JavaRDD<Row> queriesRowRDD = m.createRowRDD(queriesRDD, queryDef);
		JavaRDD<Row> datapointsRowRDD = m.createRowRDD(datapointsRDD, datapointsDef);

		
		JavaSchemaRDD schemaQueries = sqlc.applySchema(queriesRowRDD, querySchema);
		JavaSchemaRDD schemaDatapoints = sqlc.applySchema(datapointsRowRDD, datapointsSchema);

		JavaRDD<HashMap<String, Object>> table=new QueriesTable().createQueriesTable(sqlc, schemaQueries, schemaDatapoints);
		new MongoDBload().load(table);
		
		//new TestUDF().UDFTest(sqlc, schemaDatapoints);


	}
}
