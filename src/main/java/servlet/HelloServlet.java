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
import java.util.Map;

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
	private static final String posNegQuery = "select if(positivity>0,'positive','negative') x, count(*) count from product_reviews  group by x;";
	private static final String reviews = "select review from ProductReviews ";

	public static void getConnection() throws InstantiationException,
			IllegalAccessException {
		try {

			if (connect == null || connect.isClosed()) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connect = DriverManager
						.getConnection("jdbc:mysql://us-cdbr-east-05.cleardb.net/heroku_e3f1160c4f489ca?"
								+ "user=b174f8f64c67e5&" + "password=d2a1f516");
			}

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

			Object[] arr = getDeliveryReviews();
			ArrayList<Map<String, String>> pos = (ArrayList<Map<String, String>>) arr[0];
			ArrayList<Map<String, String>> neg = (ArrayList<Map<String, String>>) arr[1];
			System.out.println(map.keySet());
			// handler to avoid empty data
			if (map.keySet().size() == 0) {
				map.put("positive", 200);
				map.put("negative", 200);
			}
			request.setAttribute("map", map);
			request.setAttribute("positive_reviews", pos);
			request.setAttribute("negative_reviews", neg);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(
				request, response);
	}

	public HashMap<String, Integer> getData() throws ClassNotFoundException,
			SQLException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		try {
			getConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery(posNegQuery);

			while (resultSet.next()) {
				map.put(resultSet.getString("x"), resultSet.getInt("count"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HelloServlet.getData()");
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

	public Object[] getDeliveryReviews() throws SQLException,
			InstantiationException, IllegalAccessException {
		String query = "select * from review_classification where display_text!='' order by polarity desc";
		getConnection();
		resultSet = statement.executeQuery(query);
		Object arr[] = new Object[2];
		ArrayList<Map<String, String>> positiveReviews = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> negReviews = new ArrayList<Map<String, String>>();
		while (resultSet.next()) {
			float polarity = resultSet.getFloat("polarity");
			String displayText = resultSet.getString("display_text");
			String review = resultSet.getString("review");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("review", review);
			map.put("display_text", displayText);
			if (polarity >= -0.15)
				positiveReviews.add(map);
			else
				negReviews.add(map);
		}
		arr[0] = positiveReviews;
		arr[1] = negReviews;
		return arr;
	}
}