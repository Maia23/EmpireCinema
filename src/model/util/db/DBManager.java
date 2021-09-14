package model.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.TreeMap;

public abstract class DBManager {

	public TreeMap<String, HashMap<String, String>> select(Connection conn, String selectQuery) {

		TreeMap<String, HashMap<String, String>> selectResult = new TreeMap<String, HashMap<String, String>>();

		String[] keys = selectQuery.replace(" ", "").replace("SELECT", "").split("FROM")[0].split(",");

		try {

			PreparedStatement stmt = conn.prepareStatement(selectQuery);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				HashMap<String, String> eachResult = new HashMap<String, String>();
				for (int i = 1; i < keys.length; i++) {
					eachResult.put(keys[i], rs.getString(i + 1));
				}
				selectResult.put(rs.getString(1), eachResult);
			}

		} catch (SQLException sqlExc) {
			System.out.println(sqlExc.getMessage());
		}

		return selectResult;
	}

	public void insertValues(Connection conn, String insertValuesQuery) {

		try {

			PreparedStatement stmt = conn.prepareStatement(insertValuesQuery);
			stmt.executeUpdate(insertValuesQuery);

		} catch (SQLException sqlExc) {
			System.out.println(sqlExc.getMessage());
		}

	}

	public void updateValues(Connection conn, String updateQuery) {
		try {

			PreparedStatement stmt = conn.prepareStatement(updateQuery);
			stmt.executeUpdate(updateQuery);

		} catch (SQLException sqlExc) {
			System.out.println(sqlExc.getMessage());
		}
	}

	public void dropValues(Connection conn, String deleteQuery) {
		try {

			PreparedStatement stmt = conn.prepareStatement(deleteQuery);
			stmt.executeUpdate(deleteQuery);

		} catch (SQLException sqlExc) {
			System.out.println("SDKJFGHSDHFK");
			System.out.println(sqlExc.getMessage());
		}
	}

}
