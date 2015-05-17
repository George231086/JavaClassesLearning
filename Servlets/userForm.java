import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.ClassNotFoundException;

// We extend HttpServlet class
public class userForm extends HttpServlet {

	// Handle get request.
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set response content type
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		String userExists = "Welcome";
		String userDoesNotExist = "No access";
		String title = "default";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "
				+ "transitional//en\">\n";
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		try {
			if (userExists(email, password))
				title = userExists;
			else
				title = userDoesNotExist;
		} catch (ClassNotFoundException e1) {
			title = "ClassNotFoundException";
		} catch (SQLException e2) {
			title = "SQLException";
		}

		out.println(docType + "<html>\n" + "<head><title>" + title
				+ "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n"
				+ "<h1 align=\"center\">" + title + "</h1>\n" + "<ul>\n"
				+ "  <li><b>email</b>: " + email + "\n"
				+ "  <li><b>password</b>: " + password + "\n" + "</ul>\n"
				+ "</body></html>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public static boolean userExists(String email, String password)
			throws ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/";
		String database = "training";
		String userName = "george";
		String dbPassword = "georgepswd";
		Class.forName("com.mysql.jdbc.Driver");
		try (Connection conn = DriverManager.getConnection(url + database,
				userName, dbPassword);
				PreparedStatement statement = conn
						.prepareStatement("Select * FROM users WHERE email = ? AND password = ?")) {
			statement.setString(1, email);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();

			return resultSet.next();

		}

	}

}
