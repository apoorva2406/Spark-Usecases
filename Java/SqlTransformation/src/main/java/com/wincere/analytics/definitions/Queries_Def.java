package com.wincere.analytics.definitions;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Queries_Def {

	public String getQueriesDef() throws SQLException, ClassNotFoundException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager
				.getConnection("jdbc:sqlserver://172.16.1.29:1433;database=Rave_ReportDB;user=TM_Analytics;password=password");
		Statement sta = conn.createStatement();
		String Sql = "select * from Queries";
		ResultSet rs = sta.executeQuery(Sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		String result = "";

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String temp=rsmd.getColumnName(i);
			temp=Character.toLowerCase(
					temp.charAt(0)) + (temp.length() > 1 ? temp.substring(1) : "");
			result = result + temp + ":"
					+ rsmd.getColumnTypeName(i);
			if (i != rsmd.getColumnCount())
				result = result + ",";

		}
		return result;
	}

}
