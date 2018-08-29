package com.wincere.aggr_tables;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.Row;

import scala.Tuple2;

public class AggregationFunctions implements Serializable {

	public JavaRDD<Map<String, String>> subjects(
			JavaPairRDD<String, Iterable<Row>> groupByColumnRDD) {

		JavaRDD<Map<String, String>> aggrRDD = groupByColumnRDD
				.map(new Function<Tuple2<String, Iterable<Row>>, Map<String, String>>() {
					public Map<String, String> call(
							Tuple2<String, Iterable<Row>> t) {

						Map<String, String> m = new HashMap<String, String>();
						Iterable<Row> r = t._2;
						Iterator<Row> itr = r.iterator();

						if (itr.hasNext()) {
							Row row = itr.next();

							m.put("studyOID", row.getString(2));
							m.put("metaDataVersionOID", row.getString(3));
							m.put("siteLocationOID", row.getString(4));
							m.put("subjectKey", row.getString(5));
							m.put("subjectName", row.getString(7));
							if (!row.isNullAt(8))
								m.put("subjectStatus", row.getString(8));
							else
								m.put("subjectStatus", null);
							m.put("sourceID", row.getString(39));
						}

						while (itr.hasNext()) {
							Row row = itr.next();

							if (!row.isNullAt(8)) {
								m.put("subjectStatus", row.getString(8));
								m.put("sourceID", row.getString(39));
							}

						}
						return m;
					}
				});

		return aggrRDD;

	}

	public JavaRDD<Map<String, String>> datapoints(
			JavaPairRDD<String, Iterable<Row>> groupByColumnRDD) {

		JavaRDD<Map<String, String>> aggrRDD = groupByColumnRDD
				.map(new Function<Tuple2<String, Iterable<Row>>, Map<String, String>>() {
					public Map<String, String> call(
							Tuple2<String, Iterable<Row>> t) {

						Map<String, String> m = new HashMap<String, String>();
						Iterable<Row> r = t._2;
						Iterator<Row> itr = r.iterator();

						if (itr.hasNext()) {
							Row row = itr.next();

							m.put("datapointID", row.getString(46));
							m.put("studyOID", row.getString(2));
							m.put("metaDataVersionOID", row.getString(3));
							m.put("siteLocationOID", row.getString(4));
							m.put("subjectKey", row.getString(5));
							m.put("subjectName", row.getString(7));
							m.put("studyEventOID", row.getString(9));

							if (!row.isNullAt(10))
								m.put("studyEventRepeatKey", row.getString(10));
							else
								m.put("studyEventRepeatKey", null);
							m.put("formOID", row.getString(11));
							if (!row.isNullAt(12))
								m.put("formRepeatKey", row.getString(12));
							else
								m.put("formRepeatKey", null);
							m.put("lastUpdatedTimeStamp", row.getString(37));   //wrong
							m.put("lastSourceID", row.getString(39));	//wrong
							m.put("itemGroupOID", row.getString(13));
							if (!row.isNullAt(14))
								m.put("itemGroupRepeatKey", row.getString(14));
							else
								m.put("itemGroupRepeatKey", null);
							m.put("itemOID", row.getString(16));

							if (!row.isNullAt(18)) {
								m.put("itemValue", row.getString(18));
								m.put("itemValueLastSourceID",
										row.getString(39));
							} else {
								m.put("itemValue", null);
								m.put("itemValueLastSourceID", null);
							}
							if (!row.isNullAt(19)) {
								m.put("isVerified", row.getString(19));
								m.put("isVerifiedLastSourceID",
										row.getString(39));
							} else {
								m.put("isVerified", null);
								m.put("isVerifiedLastSourceID", null);
							}
							if (!row.isNullAt(20)) {
								m.put("isLocked", row.getString(20));
								m.put("isLockedLastSourceID", row.getString(39));
							} else {
								m.put("isLocked", null);
								m.put("isLockedLastSourceID", null);
							}
							if (!row.isNullAt(21)) {
								m.put("isFreezed", row.getString(21));
								m.put("isFreezedLastSourceID",
										row.getString(39));
							} else {
								m.put("isFreezed", null);
								m.put("isFreezedLastSourceID", null);
							}
							if (!row.isNullAt(30)) {
								m.put("isReviewed", row.getString(30));
								m.put("isReviewedLastSourceID",
										row.getString(39));
							} else {
								m.put("isReviewed", null);
								m.put("isReviewedLastSourceID", null);
							}

						}

						while (itr.hasNext()) {
							Row row = itr.next();

							if (!row.isNullAt(18)) {
								m.put("itemValue", row.getString(18));
								m.put("itemValueLastSourceID",
										row.getString(39));
							}
							if (!row.isNullAt(19)) {
								m.put("isVerified", row.getString(19));
								m.put("isVerifiedLastSourceID",
										row.getString(39));
							}
							if (!row.isNullAt(20)) {
								m.put("isLocked", row.getString(20));
								m.put("isLockedLastSourceID", row.getString(39));
							} 
							if (!row.isNullAt(21)) {
								m.put("isFreezed", row.getString(21));
								m.put("isFreezedLastSourceID",
										row.getString(39));
							}
							if (!row.isNullAt(30)) {
								m.put("isReviewed", row.getString(30));
								m.put("isReviewedLastSourceID",
										row.getString(39));
							}

						}
						return m;
					}
				});

		return aggrRDD;

	}

}
