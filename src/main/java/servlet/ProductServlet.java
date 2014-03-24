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
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProductServlet", urlPatterns = { "/product" })
public class ProductServlet extends HttpServlet {

	private static Connection connect = null;
	private static Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final String posNegReviewCount = "select if(positivity>0,'positive','negative') x, count(*) count from product_reviews  group by x;";
	private static final String reviews = "select review, review_title from product_reviews ";
	private static final String attrib_reviews = "select attribute, score from product_attribute_stats ";

	public static void getConnection() throws InstantiationException,
			IllegalAccessException {
		try {

			if (connect == null || connect.isClosed()) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connect = DriverManager
						.getConnection("jdbc:mysql://us-cdbr-east-05.cleardb.net/heroku_e3f1160c4f489ca?"
								+ "user=b174f8f64c67e5&" + "password=d2a1f516");
				statement = connect.createStatement();
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

		String id = request.getParameter("id");
		if (id == null)
			id = "B002NEGTTW";

		try {
			request.setAttribute("map", getData(id));
			request.setAttribute("attribMap", getAttribReviews(id));

			request.setAttribute("positive_reviews", getPositiveReviews(id));
			request.setAttribute("negative_reviews", getNegativeReviews(id));
			request.setAttribute("product_name", getName(id));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/product.jsp").forward(
				request, response);
	}

	public HashMap<String, Integer> getData(String id)
			throws ClassNotFoundException, SQLException,
			InstantiationException, IllegalAccessException {
		String posNegReviewCount = "select if(positivity>0,'positive','negative') x, count(*) count from product_reviews where retailer_id='"
				+ id + "' group by x;";
		System.out.println(posNegReviewCount);
		getConnection();
		resultSet = statement.executeQuery(posNegReviewCount);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("positive", 0);
		map.put("negative", 0);
		while (resultSet.next()) {
			map.put(resultSet.getString("x"), resultSet.getInt("count"));
		}
		return map;
	}

	public ArrayList<HashMap<String, String>> getPositiveReviews(String id)
			throws SQLException, InstantiationException, IllegalAccessException {
		getConnection();
		String query = reviews + " where positivity>" + 0
				+ " and retailer_id='" + id
				+ "' order by positivity desc limit 10";
		System.out.println(query);
		resultSet = statement.executeQuery(query);
		ArrayList<HashMap<String, String>> arr = new ArrayList<HashMap<String, String>>();

		while (resultSet.next()) {
			HashMap<String, String> map = new HashMap<String, String>();
			String text = resultSet.getString("review");
			text = text.replace("\'", "");
			text = text.replace("\"", "");
			map.put("review", text);
			map.put("display_text", resultSet.getString("review_title"));
			arr.add(map);
		}
		return arr;
	}

	public ArrayList<String> getNegativeReviews(String id) throws SQLException,
			InstantiationException, IllegalAccessException {
		getConnection();
		String query = reviews + " where positivity<" + 0
				+ " and retailer_id = '" + id + "'";
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

	public TreeMap<String, Float> getAttribReviews(String id)
			throws SQLException, InstantiationException, IllegalAccessException {
		TreeMap<String, Float> map = new TreeMap<String, Float>();
		getConnection();
		String query = attrib_reviews + " where retailer_id='" + id + "'";
		resultSet = statement.executeQuery(query);
		while (resultSet.next()) {
			String text = resultSet.getString("attribute");
			if (text.equals("*")) {
				text = "Overall";
			}
			map.put(text, resultSet.getFloat("score"));
		}
		return map;
	}

	public String getName(String id) throws SQLException,
			InstantiationException, IllegalAccessException {

		getConnection();
		String query = "Select title from product_details where retailer_id='"
				+ id + "'";
		resultSet = statement.executeQuery(query);
		String title = null;
		while (resultSet.next()) {
			title = resultSet.getString("title");
		}

		return title;
	}

}