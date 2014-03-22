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

@WebServlet(name = "ProductServlet", urlPatterns = { "/product" })
public class ProductServlet extends HttpServlet {

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
			String id = request.getParameter("rId");
			System.out.println(id);
			request.setAttribute("map", getData());
			request.setAttribute("positive_reviews", getPositiveReviews(id)
					.subList(0, 3));
			request.setAttribute("negative_reviews", getNegativeReviews(id)
					.subList(0, 3));
			request.setAttribute("product_name",
					"Canon EOS 60D 18 MP CMOS Digital SLR Camera with 3.0-Inch LCD (Body Only)");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/product.jsp").forward(
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

	public ArrayList<String> getPositiveReviews(String id) throws SQLException {
		String query = reviews + " where positivity>" + 0.5
				+ "and retailer_id='" + id + "'";
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

	public ArrayList<String> getNegativeReviews(String id) throws SQLException {
		String query = reviews + " where positivity<" + 0.5
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
}