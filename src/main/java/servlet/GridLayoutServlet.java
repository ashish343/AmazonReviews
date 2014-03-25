package servlet;

import java.io.IOException;
import java.sql.Connection;
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

@WebServlet(name = "GridLayoutServlet", urlPatterns = { "/grid" })
public class GridLayoutServlet extends HttpServlet {

	private static Connection connect = null;
	private static Statement statement = null;
	private ResultSet resultSet = null;
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
			ArrayList<Map<String, String>> map = getItems();
			request.setAttribute("itemMap", map);
			connect.close();

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/grid-layout.jsp").forward(
				request, response);
	}

	public ArrayList<Map<String, String>> getItems() throws SQLException,
			InstantiationException, IllegalAccessException {

		String query = "Select title , a.retailer_id, img_url , b.score from product_details a, product_attribute_stats b  where a.source_id='walmart' and a.retailer_id=b.retailer_id and b.attribute='*' and b.scoring_method='count' order by b.score desc;";
		System.out.println(query);
		resultSet = statement.executeQuery(query);

		String title = null;
		String img = null;
		String retailerId = null;
		ArrayList<Map<String, String>> products = new ArrayList<Map<String, String>>();
		while (resultSet.next()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", resultSet.getString("title"));
			map.put("img_url", resultSet.getString("img_url"));
			map.put("id", resultSet.getString("retailer_id"));

			products.add(map);
		}
		resultSet.close();
		connect.close();
		return products;
	}

}