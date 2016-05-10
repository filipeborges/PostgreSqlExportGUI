package br.jabarasca.postgrefrontend.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

	//http://stackoverflow.com/questions/23522400/how-to-connect-to-postgresql-server-to-query-the-database-names-list
	
	//Metodo getMetaData da classe Connection.
	private Connection dbConn;
	private String formatUrlServer = "jdbc:postgresql://%s:%s/?";
	private String formatUrlDb = "jdbc:postgresql://%s:%s/%s";
	private String dbAddress;
	private String dbPort;
	private String dbAdminName;
	private String dbAdminPwd;
	private boolean isDbConnEstablished = false;
	
	public DataBase(String dbAddress, String dbPort, String adminName, String password) {
		this.dbAddress = dbAddress;
		this.dbPort = dbPort;
		dbAdminName = adminName;
		dbAdminPwd = password;
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			dbConn = DriverManager.getConnection(String.format(formatUrlServer, dbAddress, dbPort),
					dbAdminName, dbAdminPwd);
			isDbConnEstablished = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isDbConnEstablished() {
		return isDbConnEstablished;
	}
	
	//public String[] getAllDataBases() {
	//final String SQL_GET_DATABASES = "SELECT datname FROM pg_database;";

	//}

	public void closeConnection() {
		try {
			dbConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean connectToDatabase(String dbName, String userName, String password) {
		closeConnection();
		String formattedUrl = String.format(formatUrlDb, dbAddress, dbPort, dbName);
		try {
			dbConn = DriverManager.getConnection(formattedUrl, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
