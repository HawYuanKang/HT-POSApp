package Database_Package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Author of this class : HAW YUAN KANG , B032110034

public class Database 
{
	
	// This is the database class that establish connection to RDBMS (xampp server).
	// Values used to setup the database connection path
	private String databaseName = "ht_db";
	private String username = "root";
	private String password = "";
	private String connectionPath = "jdbc:mysql://localhost:3306/"+ databaseName;

	public Connection getConnection() throws ClassNotFoundException, SQLException 
	{
		// Load database driver.
		Class.forName("com.mysql.jdbc.Driver");
		
		// Get connection object from the database.
		Connection connection = DriverManager.getConnection(connectionPath,username,password);
		
		return connection;
	}
}