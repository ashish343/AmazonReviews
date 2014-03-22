package servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@WebServlet(name = "WeclomeController", urlPatterns = { "/welcome" })
public class WelcomeController extends HttpServlet {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final String posNegQuery = "select if(positivity>=0.5,'positive','negative') x, count(*) count from ProductReviews  group by x;";

	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		HashMap<String, Integer> map = null;
		try {
			map = getData();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		model.addAllAttributes(map);
		return "index";
	}

	public HashMap<String, Integer> getData() throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/test?"
				+ "user=root");
		statement = connect.createStatement();
		resultSet = statement.executeQuery(posNegQuery);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		while (resultSet.next()) {
			map.put(resultSet.getString("x"), resultSet.getInt("count"));
		}
		return map;
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		WelcomeController hc = new WelcomeController();
		System.out.println(hc.getData());
	}

}