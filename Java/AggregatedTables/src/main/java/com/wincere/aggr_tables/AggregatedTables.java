package com.wincere.aggr_tables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.DataType;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;
import org.apache.spark.sql.api.java.StructField;
import org.apache.spark.sql.api.java.StructType;

import scala.Tuple2;

public class AggregatedTables implements Serializable {

	public JavaPairRDD<String, Iterable<Row>> createGroupRDD(JavaSparkContext sc,
			JavaSQLContext sqlContext, String masterTablePath, String query,
			final int groupByIndex) {

		JavaSchemaRDD parquetFile = sqlContext.parquetFile(masterTablePath);
		parquetFile.registerTempTable("parquetFile");
		JavaSchemaRDD s = sqlContext.sql(query);
		// System.out.println(s.collect());

		JavaPairRDD<String, Iterable<Row>> groupByColumnRDD = s
				.groupBy(new Function<Row, String>() {
					public String call(Row r) {
						return r.getString(groupByIndex);
					}
				});
		// System.out.println(groupBySubjectKey.collect());

		return groupByColumnRDD;

	}

	public void createAggrTable(JavaSQLContext sqlContext,
			JavaRDD<Map<String, String>> aggrRDD, final String targetDef,
			String destinationPath) {

		List<StructField> fields = new ArrayList<StructField>();

		for (String fieldName : targetDef.split(",")) {
			fields.add(DataType.createStructField(fieldName.trim(),
					DataType.StringType, true));
		}
		StructType schema = DataType.createStructType(fields);

		JavaRDD<Row> rowRDD = aggrRDD
				.map(new Function<Map<String, String>, Row>() {
					public Row call(Map<String, String> record)
							throws Exception {

						List<String> list = new ArrayList<String>();
						for (String i : targetDef.split(",")) {
							if (record.containsKey(i)) {
								list.add(record.get(i));
							} else
								list.add(null);
						}

						return Row.create(list.toArray());
					}
				});

		JavaSchemaRDD schemaTempTable = sqlContext.applySchema(rowRDD, schema);
		schemaTempTable.saveAsParquetFile(destinationPath);
	}

}
