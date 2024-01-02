package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    public static Connection getConnection(String props)
	{
		Connection connection=null;
		String[] properties=props.split(" ");
		try {
			Class.forName(properties[3]);
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Established");
		}
		
		try {
			connection = DriverManager.getConnection(properties[0],properties[1],properties[2]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}