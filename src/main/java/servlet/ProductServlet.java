package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.ConnectUtil;

import com.google.gson.Gson;

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

		String id = request.getParameter("id");
		if (id == null)
			id = "B002NEGTTW";

		try {
			getConnection();
			request.setAttribute("map", getData(id));
			request.setAttribute("attribMap", getAttribReviews(id));

			request.setAttribute("positive_reviews", getPositiveReviews(id));
			request.setAttribute("negative_reviews", getNegativeReviews(id));
			String[] arr = getNameAndImageUrl(id);
			request.setAttribute("product_name", arr[0]);
			request.setAttribute("product_img", arr[1]);
			request.setAttribute("id", id);

			ArrayList<HashMap<String, String>> list = tokenCountDeliveryReviews(id);
			Gson gs = new Gson();
			String txt = gs.toJson(list);
			request.setAttribute("tag_cloud", txt);
			System.out.println(txt);
			connect.close();

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
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
		String query = reviews + " where positivity>" + 0
				+ " and retailer_id='" + id
				+ "' and review_title!='' order by positivity desc limit 10";
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

	public ArrayList<HashMap<String, String>> getNegativeReviews(String id)
			throws SQLException, InstantiationException, IllegalAccessException {
		String query = reviews + " where positivity<" + 0
				+ " and retailer_id='" + id
				+ "' and review_title!='' order by positivity desc limit 10";
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

	public TreeMap<String, Float> getAttribReviews(String id)
			throws SQLException, InstantiationException, IllegalAccessException {
		TreeMap<String, Float> map = new TreeMap<String, Float>();
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

	public String[] getNameAndImageUrl(String id) throws SQLException,
			InstantiationException, IllegalAccessException {

		String query = "Select title , img_url from product_details where retailer_id='"
				+ id + "'";
		resultSet = statement.executeQuery(query);
		String title = null;
		String img = null;
		while (resultSet.next()) {
			title = resultSet.getString("title");
			img = resultSet.getString("img_url");
		}

		return new String[] { title, img };
	}

	public ArrayList<HashMap<String, String>> tokenCountDeliveryReviews(
			String id) throws SQLException {
		String query = "select tags from product_reviews where retailer_id='"
				+ id + "' ";
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		HashMap<String, Integer> tmp = new HashMap<String, Integer>();
		HashSet<String> hash = new HashSet<String>();
		hash.add("slr");
		hash.add("camera");
		hash.add("nikon\u0027s");

		resultSet = statement.executeQuery(query);
		while (resultSet.next()) {
			String review = resultSet.getString("tags");
			review = review.toLowerCase().trim();

			StringTokenizer st = new StringTokenizer(review, ",");

			while (st.hasMoreTokens()) {
				String token = st.nextToken().toLowerCase();
				if (hash.contains(token))
					continue;
				if (!tmp.containsKey(token))
					tmp.put(token, 0);
				tmp.put(token, tmp.get(token) + 1);
			}
		}
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (String key : tmp.keySet()) {
			HashMap<String, String> t = new HashMap<String, String>();
			t.put("text", key);
			t.put("frequency", tmp.get(key) + "");
			list.add(t);
		}
		return list;
	}

}