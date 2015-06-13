import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlExample {

	static class MysqlDb {

		public Connection getConnection() throws ClassNotFoundException,
				SQLException {
			String url = "jdbc:mysql://localhost:3306/";
			String database = "training";
			String userName = "george";
			String dbPassword = "georgepswd";

			Class.forName("com.mysql.jdbc.Driver");

			Connection conn = DriverManager.getConnection(url + database,
					userName, dbPassword);
			return conn;

		}

		public void createTableForExample(Connection conn) throws SQLException {
			try (Statement statement = conn.createStatement()) {
				String tablecreationSQL = "CREATE TABLE accounts"
						+ " (accountNo INTEGER not NULL, name VARCHAR(50),"
						+ " balance FLOAT,PRIMARY KEY (accountNo))";
				statement.executeUpdate(tablecreationSQL);
				statement
						.executeUpdate("INSERT INTO accounts VALUES(1,'Jim',1000.00)");
				statement
						.executeUpdate("INSERT INTO accounts VALUES(2,'Jack',1000.00)");
			}
		}
	}

	public static void main(String[] args) throws SQLException {

		MysqlDb db = new MysqlDb();

		Connection conn = null;
		ResultSet source = null;
		ResultSet dest = null;
		ResultSet result = null;
		Statement statement = null;

		try {

			conn = db.getConnection();

			// Create table for the example, comment out once table is
			// created.
			db.createTableForExample(conn);

			conn.setAutoCommit(false);
			statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			source = statement
					.executeQuery("SELECT * From accounts WHERE accountNo = 1");
			if (source.next()) {
				float balance = source.getFloat("balance");
				source.updateFloat("balance", balance - 20);
				source.updateRow();
			}

			// Line to cause SQL error to test rollback, uncomment to try
			// out.
			// statement.execute("UPDATE accounts SET balance = a WHERE name = Jack");

			dest = statement
					.executeQuery("Select * From accounts WHERE accountNo = 2");

			if (dest.next()) {
				float balance = dest.getFloat("balance");
				dest.updateFloat("balance", balance + 20);
				dest.updateRow();
			}

			result = statement.executeQuery("SELECT * FROM accounts");
			while (result.next()) {
				System.out.println("name: " + result.getString("name")
						+ ", balance: " + result.getFloat("balance"));
			}
			conn.commit();
		} catch (ClassNotFoundException eCNFE) {
			System.out.println("Class not found");
		} catch (SQLException eSQL1) {
			System.out.println("SQL error!");
			eSQL1.printStackTrace();
			conn.rollback();
			System.out.println("Rolled back!");
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (source != null) {
				source.close();
			}
			if (dest != null) {
				dest.close();
			}
			if (result != null) {
				result.close();
			}
			if (statement != null) {
				statement.close();
			}
		}

	}
}
