package com.wincere.analytics.transform;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;

public class QueriesTable implements Serializable {

	public JavaRDD<HashMap<String, Object>> createQueriesTable(
			JavaSQLContext sqlc, JavaSchemaRDD schemaQueries,
			JavaSchemaRDD schemaDatapoints) {

		schemaQueries.registerTempTable("Queries");
		System.out.println(schemaQueries.schemaString());
		schemaDatapoints.registerTempTable("Datapoints");
		JavaSchemaRDD queryTable = sqlc
				.sql("SELECT Queries.queryID,Datapoints.studyId,Datapoints.subjectId,Datapoints.dataPageId,Datapoints.instanceId,Datapoints.recordID,Queries.dataPointID,Queries.queryUserID,Queries.updated,Queries.queryText,Queries.answerData,Queries.queryStatus,Queries.resolved,Queries.answered,Queries.created FROM Queries inner join Datapoints on Queries.dataPointID=Datapoints.dataPointID");

		final String schema = queryTable.schemaString();

		JavaRDD<HashMap<String, Object>> queryTableRDD = queryTable
				.map(new Function<Row, HashMap<String, Object>>() {
					public HashMap<String, Object> call(Row row)
							throws ParseException {

						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss.SSS");

						HashMap<String, Object> q;
						q = new HashMap<String, Object>();

						int j = -1;
						String[] parts = schema.split("\n");
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
								q.put(key, null);
							else if (value.equals("integer"))
								q.put(key, row.getInt(j));
							else if (value.equals("string"))
								q.put(key, row.getString(j));
							else
								q.put(key, row.get(j));

						}

						return q;
					}
				});
		return queryTableRDD;
	}

}
