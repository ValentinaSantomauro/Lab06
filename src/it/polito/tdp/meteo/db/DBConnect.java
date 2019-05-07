package it.polito.tdp.meteo.db;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

	static private final String jdbcUrl = "jdbc:mysql://localhost/meteo?useTimeZone=true&serverTimeZone=UTC&user=root&password=Admaiorasemper8";
	

	static private Connection connection = null;

	public static Connection getConnection() {

		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(jdbcUrl);
			}
			return connection;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException("Cannot get a connection " + jdbcUrl, e);
		}
	}
 
	

}
