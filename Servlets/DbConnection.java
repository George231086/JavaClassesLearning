import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {

	public Connection getConnection() throws ClassNotFoundException,
			SQLException {
		String url = "jdbc:mysql://localhost:3306/";
		String database = "training";
		String userName = "george";
		String dbPassword = "georgepswd";
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url + database, userName,
				dbPassword);
		return conn;

	}

	public boolean emailExists(String email) throws ClassNotFoundException,
			SQLException {
		try (Connection conn = getConnection();
				PreparedStatement check = conn
						.prepareStatement("select * from users where email=?")) {
			check.setString(1, email);
			return check.executeQuery().next();

		}
	}

	public boolean updateDatabase(String name, String email, String password)
			throws ClassNotFoundException, SQLException {

		try (Connection conn = getConnection();
				PreparedStatement update = conn
						.prepareStatement("INSERT INTO users VALUES(?,?,?)")) {
			update.setString(1, name);
			update.setString(2, email);
			update.setString(3, password);
			int rowsUpdated = update.executeUpdate();
			return rowsUpdated == 1;

		}
	}

	public boolean userExists(String email, String password)
			throws ClassNotFoundException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement statement = conn
						.prepareStatement("Select * FROM users WHERE email = ? AND password = ?")) {
			statement.setString(1, email);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		}
	}

}
