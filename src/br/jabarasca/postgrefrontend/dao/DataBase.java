package br.jabarasca.postgrefrontend.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
	
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
	
	public String getTablesDDLFromConnectedDB() {
		final String TABLE_DDL_FORMAT = "CREATE TABLE %s (%s%s%s);\n\n";
		String ddlScript = "";
		String pkConstraintDDL;
		String fkConstraintDDL;
		String columnsDDL;
		
		List<String> allTables = getAllTablesFromConnectedDB();
		if(allTables != null) {
			for(int i = 0; i < allTables.size(); i++) {
				String tableName = allTables.get(i);
				pkConstraintDDL = getTablePKConstraintDef(tableName);
				if(pkConstraintDDL == null) {
					return null;
				} else {
					fkConstraintDDL = getTableFKConstraintDef(tableName);
					if(fkConstraintDDL == null) {
						return null;
					} else {
						columnsDDL = getTableColumnsDef(tableName);
						if(columnsDDL == null) {
							return null;
						} else {
							if(pkConstraintDDL.length() > 0) {
								pkConstraintDDL = ",\n" + pkConstraintDDL; 
							}
							if(fkConstraintDDL.length() > 0) {
								fkConstraintDDL = ",\n" + fkConstraintDDL;
							}
							ddlScript += String.format(TABLE_DDL_FORMAT, tableName, columnsDDL, pkConstraintDDL, fkConstraintDDL);
						}
					}
				}
			}
			return ddlScript;
		} else {
			return null;
		}
	}
	
	private String getTableColumnsDef(String tableName) {
		final String SQL_TABLE_INFO = "SELECT column_name, data_type, is_nullable, character_maximum_length, column_default " + 
									  "FROM information_schema.columns " +
									  "WHERE upper(table_name) = upper('%s');";
		final String COLUMN_DEF_FORMAT = "%s %s%s%s";
		String columnsDef = "";
		String nullableValue;
		String defaultValue;
		String dataType;
		
		try {
			ResultSet result = dbConn.prepareStatement(String.format(SQL_TABLE_INFO, tableName))
					.executeQuery();
			
			boolean isFirstRow = true;
			while(result.next()) {
				if(result.getString(3).equals("YES")) {
					nullableValue = "";
				} else {
					nullableValue = " NOT NULL";
				}
				
				dataType = result.getString(2);
				if(dataType.toUpperCase().contains("USER")) {
					dataType = "varchar(255)";
					defaultValue = null;
				} else {
					if(dataType.toUpperCase().contains("CHAR")) {
						dataType += "(" + result.getInt(4) + ")";
					}
					defaultValue = result.getString(5);
				}
				
				if(defaultValue == null || defaultValue.contains("nextval")) {
					defaultValue = "";
				} else {
					defaultValue = " DEFAULT " + defaultValue;
				}
				
				if(isFirstRow) {
					isFirstRow = false;
					columnsDef += String.format(COLUMN_DEF_FORMAT, result.getString(1), dataType, 
							nullableValue, defaultValue);
				} else {
					columnsDef += ", \n" + String.format(COLUMN_DEF_FORMAT, result.getString(1), dataType, 
							nullableValue, defaultValue);
				}
			}
			return columnsDef;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getTableFKConstraintDef(String tableName) {
		final String SQL_FK_INFO = 	"SELECT ref_cons.constraint_name, subqry3.fk_column_name, " + 
									"subqry3.referenced_table_name, subqry3.referenced_column_name, " +
									"ref_cons.update_rule, ref_cons.delete_rule " +  
									"FROM information_schema.referential_constraints AS ref_cons " +
									"JOIN (" +
											"SELECT subqry2.fk_table_name, subqry2.fk_column_name, " +
											"cons_usa.table_name AS referenced_table_name, cons_usa.column_name AS referenced_column_name, " +
											"cons_usa.constraint_name " + 
											"FROM information_schema.constraint_column_usage cons_usa " +
											"JOIN (" +
													"SELECT kcol_usa.table_name AS fk_table_name, kcol_usa.column_name AS fk_column_name, kcol_usa.constraint_name " +
													"FROM information_schema.key_column_usage AS kcol_usa " +
													"JOIN ( " +
															"SELECT constraint_name FROM information_schema.table_constraints AS tb_cons " +
															"WHERE constraint_type = 'FOREIGN KEY' " +
															"AND table_name = '%s' " +
													") AS subqry1 " +
													"ON upper(kcol_usa.constraint_name) = upper(subqry1.constraint_name) " +
											") AS subqry2 " +
											"ON upper(cons_usa.constraint_name) = upper(subqry2.constraint_name) " +
									") AS subqry3 " +
									"ON upper(ref_cons.constraint_name) = upper(subqry3.constraint_name);";

		final String FOREIGN_KEY_DEF_FORMAT = "CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s (%s) ON DELETE %s ON UPDATE %s";
		String tableDefFKs = "";
		
		try {
			ResultSet result = dbConn.prepareStatement(String.format(SQL_FK_INFO, tableName))
					.executeQuery();
			
			boolean isFirstRow = true;
			while(result.next()) {
				if(isFirstRow) {
					isFirstRow = false;
					tableDefFKs += String.format(FOREIGN_KEY_DEF_FORMAT, result.getString(1), result.getString(2),
							result.getString(3), result.getString(4), result.getString(6), result.getString(5));
				} else  {
					tableDefFKs += ",\n" + String.format(FOREIGN_KEY_DEF_FORMAT, result.getString(1), result.getString(2),
							result.getString(3), result.getString(4), result.getString(6), result.getString(5));
				}
			}
			
			return tableDefFKs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getTablePKConstraintDef(String tableName) {
		final String SQL_COLUMN_NAME_PK_TABLE = "SELECT key_usa.column_name, tb_cons.constraint_name " +
												"FROM information_schema.table_constraints tb_cons " +
												"JOIN information_schema.key_column_usage key_usa " +
												"ON upper(tb_cons.constraint_name) = upper(key_usa.constraint_name) " +
												"AND upper(tb_cons.table_name) = upper('%s') " +
												"AND upper(tb_cons.constraint_type) = 'PRIMARY KEY';";
		
		final String PK_CONSTRAINT_DEF = "CONSTRAINT %s PRIMARY KEY (%s)";
		try {
			ResultSet result = dbConn.prepareStatement(String.format(SQL_COLUMN_NAME_PK_TABLE, tableName))
					.executeQuery();
			
			String pkColumns = "";
			String constraintName = "";
			boolean isFirstRow = true;
			
			while(result.next()) {
				if(isFirstRow) {
					isFirstRow = false;
					constraintName = result.getString(2);
					pkColumns += result.getString(1);
				} else {
					pkColumns += ", " + result.getString(1);
				}
			}
			
			return String.format(PK_CONSTRAINT_DEF, constraintName, pkColumns);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
