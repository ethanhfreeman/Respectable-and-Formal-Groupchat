
import java.sql.*;
public class Database {

    private static java.sql.Connection c = null;
	private static Statement stmt = null;

    public static void Connect() throws Exception {
        try {
            String url = "jdbc:postgresql://localhost/temp2?user=postgres&password=1234&ssl=false";
            //TODO change these parameters to suit your localhost username and password
            /*
             * Above, you need to change the "temp2" portion of the url
             * to specify the database in which you are looking to connect to
             * previously this was just postgres, which just connects you to postgresql.
             * This being specified to temp2, essentially does  "\c temp2" and then the subsequent
             * methods like 'select()' work.
             * 
             * This probably needs to be parameterized in the long run, as we won't always be
             * connecting to a hard-coded destination such as "temp2"
             */
            c = DriverManager.getConnection(url);
            System.out.println("Connected :)");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
	public static void select(String tableName) {
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + tableName + ";");
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("name");
				//String password = rs.getString("age");
				
				System.out.println("ID: " + id);
				System.out.println("USER: " + username);
				//System.out.println("PASS: " + password);
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
