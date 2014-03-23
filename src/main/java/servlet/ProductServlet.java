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
	private static final String posNegReviewCount = "select if(positivity>0,'positive','negative') x, count(*) count from product_reviews  group by x;";
	private static final String reviews = "select review from product_reviews ";
	private static final String attrib_reviews = "select attribute, score from product_attribute_stats ";

	public static void getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if (connect == null)
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/test?"
								+ "user=root");
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
			String id = request.getParameter("id");
			if (id != null) {

				request.setAttribute("map", getData(id));
				request.setAttribute("attribMap", getAttribReviews(id));

				request.setAttribute("positive_reviews", getPositiveReviews(id)
						.subList(0, 3));
				request.setAttribute("negative_reviews", getNegativeReviews(id)
						.subList(0, 3));
			} else {
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put("positive", 200);
				map.put("negative", 100);
				request.setAttribute("map", map);
				HashMap<String, Float> map1 = new HashMap<String, Float>();
				map1.put("photo", 0.2f);
				map1.put("video", 0.9f);
				request.setAttribute("attribMap", map1);

				ArrayList<String> tmp = new ArrayList<String>();
				tmp.add("It is not a 5D MkII. It is not a 50D. It is a near-perfect compromise between the two.In the 7D is a combination of resolution (18MP) and speed (8fps) which makes it an extremely versatile camera. Canon improved the high ISO performance, providing a 1 to 1.5 stop improvement. So, ISO 3200 looks like 1600. And with the ISO 6400 and 12800 you can see in the dark. However, ISO 12800 should only be used in dire emergencies. The 18MP allows for more aggressive cropping and more detailed prints at the larger sizes. The per-pixel detail is also excellent.The new AF system is both versatile, sophisticated, fast, and accurate (even at f1.4). I recommend dedicating some time to the manual and practicing with its many features because there is a little learning curve to extract the potential of the new AF. Added bonus with the new system are: ability to use any point for AI Servo, prioritizing focus over speed or visa versa, and AF point expansion.The 100% viewfinder is great and bright, enabling true WYSWYG composition and improved manual focusing. A grid, AF points, and other information can be toggled on and off to help with composition and alignment.A totally new and LONG OVERDUE feature is the ability to use the pop-up flash to control remote flashes. It works very well, especially indoors, triggering your remotes. Only situations I would resort to my 580EX II for triggering is if Im outside in bright sun, flashes are at odd angles, or the flashes are far away. Aside from the those situations, the pop-up flash as a trigger works very well.It looks like Canon did a great job listening to its photographers and trends in photojournalism. This camera has the resolution and image quality for portraiture, but also the speed and ISO range for sports and photojournalism. Speed, resolution, and 1080p HD video makes the EOS 7D an excellent all-in-one and the best pro-sumer camera to date ... yes, even better than the Nikon 300 and Nikon 300s.CONS:- No dual CF slots.- No 1D-level weather sealing (I would gladly have paid extra for it)- Incandescent WB preset still not close enough.Recommended accessories:- Sandisk Extreme 8GB UDMA CF card(s)- BG-E7 battery grip and extra battery- Domke GRIPPER camera strap- LaCIE Rugged 500GB External HDD");
				request.setAttribute("positive_reviews", tmp);
				request.setAttribute("negative_reviews", tmp);

			}
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

	public HashMap<String, Integer> getData(String id)
			throws ClassNotFoundException, SQLException {
		String posNegReviewCount = "select if(positivity>0,'positive','negative') x, count(*) count from product_reviews where retailer_id='"
				+ id + "' group by x;";
		getConnection();
		resultSet = statement.executeQuery(posNegReviewCount);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		while (resultSet.next()) {
			map.put(resultSet.getString("x"), resultSet.getInt("count"));
		}
		return map;
	}

	public ArrayList<String> getPositiveReviews(String id) throws SQLException {
		getConnection();
		String query = reviews + " where positivity>" + 0 + "and retailer_id='"
				+ id + "'";
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

	public HashMap<String, Float> getAttribReviews(String id)
			throws SQLException {
		HashMap<String, Float> map = new HashMap<String, Float>();
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
}