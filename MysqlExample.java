import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/***
 * 
 * @author george
 * 
 *         Example to illustrate Java-mysql interaction. In the main method we
 *         simulate a transaction, 20 is subtracted from one account balance and
 *         credited to another. If a sql exception occurs then the changes are
 *         rolled back. The amount and account numbers are hard coded, this is
 *         meant only as a simple example. A static inner class is used to
 *         define an object useful to get a db connection and create a table for
 *         the transaction.
 * 
 */

public class MysqlExample {

	static class MysqlDb {

		// Method to get db connection.
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

		// Create the table used in the example.
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

		// Will use finally block to close resources, as wish to use rollback if
		// we have a sql error.
		Connection conn = null;
		ResultSet source = null;
		ResultSet dest = null;
		ResultSet result = null;
		Statement statement = null;

		try {

			conn = db.getConnection();

			// Create table for the example, comment out once table is created.
			db.createTableForExample(conn);

			// Turn off auto commit. We'll commit when both updates have
			// completed successfully.
			conn.setAutoCommit(false);
			statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			// Get info for account 1 and reduce balance by 20.
			source = statement
					.executeQuery("SELECT * From accounts WHERE accountNo = 1");
			if (source.next()) {
				float balance = source.getFloat("balance");
				source.updateFloat("balance", balance - 20);
				source.updateRow();
			}

			// Line to cause SQL error to test rollback, uncomment to try out.

			// statement.execute("UPDATE accounts SET balance = a WHERE name = Jack");

			// Get info for account 2 and increase balance by 20.
			dest = statement
					.executeQuery("Select * From accounts WHERE accountNo = 2");
			if (dest.next()) {
				float balance = dest.getFloat("balance");
				dest.updateFloat("balance", balance + 20);
				dest.updateRow();
			}

			// Show new account details.
			result = statement.executeQuery("SELECT * FROM accounts");
			while (result.next()) {
				System.out.println("Account No: " + result.getInt("accountNo")
						+ ", name: " + result.getString("name") + ", balance: "
						+ result.getFloat("balance"));
			}

			// Commit the changes.
			conn.commit();
		} catch (ClassNotFoundException eCNFE) {
			System.out.println("Class not found");
		} catch (SQLException eSQL1) {
			System.out.println("SQL error!");
			// eSQL1.printStackTrace(); debug
			// Roll back changes if there is an exception
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
