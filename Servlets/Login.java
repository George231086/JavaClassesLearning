import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.lang.ClassNotFoundException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String destination = null;
		try {
			DbConnection db = new DbConnection();
			if (db.userExists(email, password)) {
				destination = "/userExists.html";
			} else
				destination = "/userDoesNotExist.html";
		} catch (ClassNotFoundException e1) {
			request.setAttribute("error", "ClassNotFoundException");
			destination = "/error.jsp";
		} catch (SQLException e2) {
			request.setAttribute("error", "SQLException");
			destination = "/error.jsp";
		}
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(destination);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
