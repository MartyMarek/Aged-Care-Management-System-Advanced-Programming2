/**
 * Class: Database (Singleton)
 * Description: Database uses the singleton pattern to create a single instance. The connection method
 * can be called multiple times to create multiple connections to the same database. 
 * 
 * @author Marton Marek
 */

package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
	
	//Establish connection to the database 
	final public static String DB_NAME = "AuditDatabase";
	
	//creates a singleton class for Database (not the connections)
	private static volatile Database dbInstance;
		
	/**
	 * Default private constructor forcing singleton pattern
	 */
	private Database() {
	}
	
	/**
	 * singleton pattern getInstance method (to get the instance of this class)
	 * @return Database
	 */
	public static Database getInstance() {
		//if no instance yet exists..
		if (dbInstance == null) {
			
			//create synchronized so multiple threads can't create and access at the same time..
			synchronized (Database.class) {
				if (dbInstance == null) {
					dbInstance = new Database();
				}
			}
		}
		return dbInstance;
	}
	
	/**
	 * opens a new connection to the database
	 * @return
	 */
	public Connection getNewConnection() {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			//default login SA and no password
			Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/" + DB_NAME, "SA", ""); 
			return con;
		}
		catch (ClassNotFoundException cnfe) {
			return null;
		}
		catch (SQLException se) {
			return null;
		}

	}
	

}
