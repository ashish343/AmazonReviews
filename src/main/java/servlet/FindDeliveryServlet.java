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

@WebServlet(name = "FindDeliveryServlet", urlPatterns = { "/findDeliveryTag" })
public class FindDeliveryServlet extends HttpServlet {

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
			if (tag == null) {
				response.sendRedirect("/delivery");
			} else {
				Object[] arr = getDeliveryReviews(tag);
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				ArrayList<Map<String, String>> pos = (ArrayList<Map<String, String>>) arr[0];
				ArrayList<Map<String, String>> neg = (ArrayList<Map<String, String>>) arr[1];
				ArrayList<Map<String, String>> neu = (ArrayList<Map<String, String>>) arr[2];

				request.setAttribute("positive_reviews", pos);
				request.setAttribute("negative_reviews", neg);
				request.setAttribute("neutral_reviews", neu);
				map.put("neutral", neu.size());
				map.put("positive", pos.size());
				map.put("negative", neg.size());
				request.setAttribute("tag", tag);
				request.setAttribute("map", map);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/findDelivery.jsp").forward(
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

	public Object[] getDeliveryReviews(String tag) throws SQLException,
			InstantiationException, IllegalAccessException {
		String query = "select * from review_classification where display_text!='' and reason like '%"
				+ tag + "%' order by polarity desc";
		resultSet = statement.executeQuery(query);
		Object arr[] = new Object[3];
		ArrayList<Map<String, String>> positiveReviews = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> neutral = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> negReviews = new ArrayList<Map<String, String>>();
		while (resultSet.next()) {
			float polarity = resultSet.getFloat("polarity");

			String review = resultSet.getString("review");
			review = review.replaceAll(tag, "<span style='font-weight:bold;'>"
					+ tag + "</span>");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("review", review);

			String review_title = resultSet.getString("display_text");
			if (review_title == null)
				review_title = review.substring(0,
						Math.min(150, review.length()));

			map.put("display_text", review_title);

			if (polarity > ProjectConstants.k2)
				positiveReviews.add(map);
			else if (polarity < ProjectConstants.k1)
				negReviews.add(map);
			else
				neutral.add(map);
		}
		arr[0] = positiveReviews;
		arr[1] = negReviews;
		arr[2] = neutral;

		return arr;
	}
}