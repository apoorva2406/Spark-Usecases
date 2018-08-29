package com.wincere.analytics.schema.mapping;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			String[] parts = fieldName.split(":");
			if (parts[1].trim().equals("int") || parts[1].trim().equals("bit"))
				fields.add(DataType.createStructField(parts[0].trim(),
						DataType.IntegerType, true));
			else if (parts[1].trim().equals("nvarchar")
			// || parts[1].trim().equals("datetime")
					|| parts[1].trim().equals("varchar"))
				fields.add(DataType.createStructField(parts[0].trim(),
						DataType.StringType, true));
			else if (parts[1].trim().equals("datetime"))
				fields.add(DataType.createStructField(parts[0].trim(),
						DataType.DateType, true));
			else
				fields.add(DataType.createStructField(parts[0].trim(),
						DataType.StringType, true));
		}
		StructType schema = DataType.createStructType(fields);
		return schema;
	}

	public JavaRDD<Row> createRowRDD(JavaRDD<String> rdd, final String def) {

		JavaRDD<Row> rowRDD = rdd.map(new Function<String, Row>() {
			public Row call(String record) throws Exception {

				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-dd-MM HH:mm:ss.SSS");

				String[] fields = record.split("\t");
				List<Object> list = new ArrayList<Object>();
				int j = -1;
				for (String i : def.split(",")) {
					j++;
					if (j < fields.length && !fields[j].trim().equals("")) {
						String[] parts = i.split(":");
						if (parts[1].trim().equals("int")
								|| parts[1].trim().equals("bit")) {
							list.add(Integer.parseInt(fields[j].trim()));
						} else if (parts[1].trim().equals("nvarchar")
						// || parts[1].trim().equals("datetime")
								|| parts[1].trim().equals("varchar"))
							list.add(fields[j].trim());
						else if (parts[1].trim().equals("datetime"))
							list.add(sdf.parse(fields[j].trim()));
						else
							list.add(fields[j].trim());
					} else
						list.add(null);
				}

				return Row.create(list.toArray());
			}
		});
		return rowRDD;
	}

}
