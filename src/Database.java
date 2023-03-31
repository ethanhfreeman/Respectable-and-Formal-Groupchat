import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class Database {
	Connection c = null;
	Statement stmt = null;
	
	public void select(String tableName) {
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + tableName + ";");
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				
				System.out.println("ID: " + id);
				System.out.println("USER: " + username);
				System.out.println("PASS: " + password);
			}
			System.out.println("Done...");
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}
