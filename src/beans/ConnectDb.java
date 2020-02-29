package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* This class is for DataBase connection
 * When a database connection is needed, object of this is created
 * in respective method or a class
*/
public class ConnectDb {
	
	private static Connection conn = null;
	private static String url = "jdbc:mysql://localhost:3306/csvdata";
	private static String user="root";
	private static String password = "root";
	
	// Static method to call getConnection using the class name ConnectDb
	public static Connection getConnection()
	{
		try 
		{
			// to load Driver class from below package
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection(url,user,password);
			//System.out.println("Connection done!!!");
			
		} 
		catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}

}
