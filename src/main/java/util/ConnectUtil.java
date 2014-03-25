package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectUtil {

	public static String server = "127.0.0.1";
	public static String user = "root";
	public static String pass = "";
	public static String db = "hack14";

	public static Object[] connect(Connection connect, Statement statement)
			throws SQLException {
		connect = DriverManager.getConnection("jdbc:mysql://" + server + "/"
				+ db + "?" + "user=" + user + "&" + "password=" + pass);
		statement = connect.createStatement();
		return new Object[] { connect, statement };

	}
}
