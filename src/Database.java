
import java.sql.*;
public class Database {

    private static java.sql.Connection c = null;
	Statement stmt = null;

    public static void Connect() throws Exception {
        try {
            String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=123&ssl=false";
            //TODO change these parameters to suit your localhost username and password
            c = DriverManager.getConnection(url);
            System.out.println("Connected :)");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
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
