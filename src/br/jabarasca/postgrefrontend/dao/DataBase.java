package br.jabarasca.postgrefrontend.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	private String connectedDBName;
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
	
	public List<String> getAllDataBases() {
		final String SQL_GET_DATABASES = "SELECT datname FROM pg_database;";
		List<String> dbNames = new ArrayList<String>();
		try {
			ResultSet result = dbConn.prepareStatement(SQL_GET_DATABASES).executeQuery();
			while(result.next()) {
				dbNames.add(result.getString(1));
			}
			result.close();
			return dbNames;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void closeConnection() {
		try {
			dbConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private List<String> getAllTablesFromConnectedDB() {
		final String SQL_GET_DB_TABLES = "SELECT table_name FROM information_schema.tables " + 
				"WHERE table_schema = 'public' "+
				"AND table_type LIKE '%TABLE%';";
		List<String> dbTables = new ArrayList<String>();
		
		try {
			ResultSet result = dbConn.prepareStatement(SQL_GET_DB_TABLES).executeQuery();
			while(result.next()) {
				dbTables.add(result.getString(1));
			}
			return dbTables;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * CREATE TABLE <TABLE_NAME> (
	 * 		<COLUMN_DEFINITIONS>,
	 * 		<PRIMARY_KEY_CONSTRAINT>,
	 * 		<FOREIGN_KEY_CONSTRAINT>
	 * )
	 * 
	 * <COLUMN_NAME><DATATYPE>
	 * 
	 */
	
	public String getTablesDDLFromConnectedDB() {
		final String COLUMN_DEFINITIONS_TAG = "<COL_DEF>";
		String tableDefFormat = "CREATE TABLE %s (<>);";
		String alterTableConstraint = "";
		
		List<String> allTables = getAllTablesFromConnectedDB();
		if(allTables != null) {
			for(int i = 0; i < allTables.size(); i++) {
				alterTableConstraint += getTablePKConstraintDef(allTables.get(i))+"\n";
			}
			return alterTableConstraint;
		} else {
			return null;
		}
	}
	
	private String getTablePKConstraintDef(String tableName) {
		final String SQL_COLUMN_NAME_PK_TABLE = "SELECT key_usa.column_name FROM information_schema.table_constraints tb_cons " +
												"JOIN information_schema.key_column_usage key_usa " +
												"ON upper(tb_cons.constraint_name) = upper(key_usa.constraint_name) " +
												"AND upper(tb_cons.table_name) = upper('%s') " +
												"AND upper(tb_cons.constraint_type) = 'PRIMARY KEY';";
		final String PK_CONSTRAINT_DEF = "PRIMARY KEY (%s)";
		String tableDefPKs = "";
		try {
			ResultSet result = dbConn.prepareStatement(String.format(SQL_COLUMN_NAME_PK_TABLE, tableName))
					.executeQuery();
			for(int i = 1; result.next(); i++) {
				if(i == 1) {
					tableDefPKs += result.getString(1);
				} else {
					tableDefPKs += ", "+result.getString(1);
				}
			}
			if(tableDefPKs.length() == 0) {
				return ""; 
			} else {
				return String.format(PK_CONSTRAINT_DEF, tableDefPKs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Get table columns definitions.
	//SELECT * FROM information_schema.columns WHERE table_name = '';
	
	public boolean connectToSpecificDB(String dbName) {
		connectedDBName = dbName;
		closeConnection();
		String formattedUrl = String.format(formatUrlDb, dbAddress, dbPort, dbName);
		try {
			dbConn = DriverManager.getConnection(formattedUrl, dbAdminName, dbAdminPwd);
			isDbConnEstablished = true;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
