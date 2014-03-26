package servlet;

import java.io.IOException;
import java.sql.Connection;
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

import util.ConnectUtil;
import util.ProjectConstants;

@WebServlet(name = "ProductReviewTagServlet", urlPatterns = { "/productReviews" })
public class ProductReviewTagServlet extends HttpServlet {

	private static Connection connect = null;
	private static Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final String posNegQuery = "select if(positivity>0,'positive','negative') x, count(*) count from product_reviews  group by x;";
	private static final String reviews = "select review from ProductReviews ";

	public static void getConnection() throws InstantiationException,
			IllegalAccessException {
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Object[] arr = ConnectUtil.connect(connect, statement);
			connect = (Connection) arr[0];
			statement = (Statement) arr[1];

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
			getConnection();
			String tag = request.getParameter("tag");
			String id = request.getParameter("id");

			if (tag == null) {
				response.sendRedirect("/product?id=" + id);
			} else {
				request.setAttribute("map", getData(id, tag));
				ArrayList<Map<String, String>> neg = getProductReviews(id, tag);
				request.setAttribute("negative_reviews", neg);
				request.setAttribute("tag", tag);
				request.setAttribute("id", id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/findProductReviews.jsp")
				.forward(request, response);
	}

	public HashMap<String, Integer> getData(String id, String tag)
			throws ClassNotFoundException, SQLException,
			InstantiationException, IllegalAccessException {
		String posNegReviewCount = "select if(positivity>"
				+ ProjectConstants.attribk2
				+ ", 'positive',if(positivity<"
				+ ProjectConstants.attribk1
				+ ", 'negative', 'neutral')) x, count(*) count from product_reviews where retailer_id='"
				+ id + "' and tags like '%" + tag + "%' group by x;";
		System.out.println(posNegReviewCount);
		resultSet = statement.executeQuery(posNegReviewCount);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("positive", 0);
		map.put("negative", 0);
		map.put("neutral", 0);
		while (resultSet.next()) {
			map.put(resultSet.getString("x"), resultSet.getInt("count"));
		}

		System.out.println(map);
		return map;
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

	public ArrayList<HashMap<String, String>> tokenCountDeliveryReviews()
			throws SQLException {
		String query = "select reason from review_classification where display_text!='' and class='DELIVERY' order by polarity desc";
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		HashMap<String, String> tmp = new HashMap<String, String>();
		resultSet = statement.executeQuery(query);
		while (resultSet.next()) {
			String reason = resultSet.getString("reason");
			reason = reason.toLowerCase().trim();

			String[] splitted = reason.split(",");

			for (String key : splitted) {
				if (key.equals("case"))
					key = "Case";
				else if (key.equals("return"))
					key = "Return";

				if (!tmp.containsKey(key)) {
					tmp.put(key, "0");
				}
				int val = Integer.parseInt(tmp.get(key));
				tmp.put(key, val + 1 + "");
			}

		}
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (String key : tmp.keySet()) {
			HashMap<String, String> t = new HashMap<String, String>();
			t.put("text", "\"" + key + "\"");
			t.put("frequency", tmp.get(key));
			list.add(t);
		}
		return list;
	}

	public ArrayList<Map<String, String>> getProductReviews(String id,
			String tag) throws SQLException, InstantiationException,
			IllegalAccessException {
		String query = "select review_title, review , positivity from product_reviews where tags like '%"
				+ tag + "%' and retailer_id='" + id + "'";
		System.out.println(query);
		resultSet = statement.executeQuery(query);
		ArrayList<Map<String, String>> reviews = new ArrayList<Map<String, String>>();
		while (resultSet.next()) {
			String text = resultSet.getString("review");
			text = text.replaceAll(tag, "<span style='font-weight:bold;'>"
					+ tag + "</span>");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("review", text);
			String review_title = resultSet.getString("review_title");
			if (review_title == null) {
				review_title = text.substring(0, Math.min(150, text.length()));

			}

			map.put("display_text", review_title);
			float pos = resultSet.getFloat("positivity");
			if (pos < ProjectConstants.attribk1) {
				map.put("categ", "-1");
			} else if (pos > ProjectConstants.attribk2) {
				map.put("categ", "1");
			} else
				map.put("categ", "0");

			reviews.add(map);
		}

		return reviews;
	}

}