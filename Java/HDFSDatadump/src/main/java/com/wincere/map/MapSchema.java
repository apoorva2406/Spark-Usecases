package com.wincere.map;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.DataType;
import org.apache.spark.sql.api.java.Row;
import org.apache.spark.sql.api.java.StructField;
import org.apache.spark.sql.api.java.StructType;

public class MapSchema implements Serializable {

	public StructType createSchema(final String s) {
		List<StructField> fields = new ArrayList<StructField>();

		for (String fieldName : s.split(",")) {
			fields.add(DataType.createStructField(fieldName.trim(),
					DataType.StringType, true));
		}
		StructType schema = DataType.createStructType(fields);
		return schema;
	}

	public JavaRDD<Row> createRowRDD(JavaRDD<Map<String, String>> rdd,
			final String def) {

		JavaRDD<Row> rowRDD = rdd.map(new Function<Map<String, String>, Row>() {
			public Row call(Map<String, String> record) throws Exception {

				List<String> list = new ArrayList<String>();
				for (String i : def.split(",")) {
					if (record.containsKey(i)) {
						list.add(record.get(i));
					} else
						list.add(null);
				}

				return Row.create(list.toArray());
			}
		});
		return rowRDD;
	}

}
