package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MyServlet", urlPatterns = { "/hello" })
public class HelloServlet extends HttpServlet {

	private static Connection connect = null;
	private static Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final String posNegQuery = "select if(positivity>=0.5,'positive','negative') x, count(*) count from ProductReviews  group by x;";
	private static final String reviews = "select review from ProductReviews ";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/test?" + "user=root");
			statement = connect.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			HashMap<String, Integer> map = getData();
			// handler to avoid empty data
			if (map.keySet().size() == 0) {
				map.put("positive", 200);
				map.put("negative", 100);
			}
			request.setAttribute("map", map);
			request.setAttribute("positive_reviews", getPositiveReviews()
					.subList(0, 3));
			request.setAttribute("negative_reviews", getNegativeReviews()
					.subList(0, 3));

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(
				request, response);
	}

	public HashMap<String, Integer> getData() throws ClassNotFoundException,
			SQLException {

		resultSet = statement.executeQuery(posNegQuery);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		while (resultSet.next()) {
			map.put(resultSet.getString("x"), resultSet.getInt("count"));
		}
		return map;
	}

	public ArrayList<String> getPositiveReviews() throws SQLException {
		String query = reviews + " where positivity>" + 0.5;
		resultSet = statement.executeQuery(query);
		ArrayList<String> arr = new ArrayList<String>();
		while (resultSet.next()) {
			String text = resultSet.getString("review");
			text = text.replace("\'", "");
			text = text.replace("\"", "");
			arr.add(text);
		}
		return arr;
	}

	public ArrayList<String> getNegativeReviews() throws SQLException {
		String query = reviews + " where positivity<" + 0.5;
		resultSet = statement.executeQuery(query);
		ArrayList<String> arr = new ArrayList<String>();
		while (resultSet.next()) {
			String text = resultSet.getString("review");
			text = text.replace("\'", "");
			text = text.replace("\"", "");
			arr.add(text);
		}
		return arr;
	}
}