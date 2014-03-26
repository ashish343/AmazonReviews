package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectUtil {

	public static String server = "us-cdbr-east-05.cleardb.net";
	public static String user = "b174f8f64c67e5";
	public static String pass = "d2a1f516";
	public static String db = "heroku_e3f1160c4f489ca";

	public static Object[] connect(Connection connect, Statement statement)
			throws SQLException {
		connect = DriverManager.getConnection("jdbc:mysql://" + server + "/"
				+ db + "?" + "user=" + user + "&" + "password=" + pass);
		statement = connect.createStatement();
		return new Object[] { connect, statement };

	}
}
