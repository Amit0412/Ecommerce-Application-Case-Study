package util;


import java.sql.Connection;

public class DBConnection {
    static Connection connection = null;

	public static Connection getConnection()
	{
		String propstr=DBPropertyUtil.getConnectionString("C:\\Users\\HP\\Desktop\\Final_Case\\Case-Study---Ecomms-main\\Ecomms-Case Study\\util\\db.properties");
		connection=DBConnUtil.getConnection(propstr);
		return connection;
	}
}