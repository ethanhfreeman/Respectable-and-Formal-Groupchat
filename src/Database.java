
import java.sql.*;
import java.util.Locale;

public class Database {

    private static java.sql.Connection c = null;
	private static Statement stmt = null;

    public static void connect(String tableName) throws Exception {
        try {
            String url = "jdbc:postgresql://localhost/" + tableName + "?user=" + localHostInfo.getLocalUserName() + "&password=" + localHostInfo.getLocalPassword() +"&ssl=false";
            //TODO change these parameters to suit your localhost username and password
            /*
             * Above, you need to insert the "tableName" portion of the url
             * to specify the database in which you are looking to connect to
             * previously this was just postgres, which just connects you to postgresql.
             * This being specified to tableName, essentially does  "\c tableName" and then the subsequent
             * methods like 'select()' work.
             */
            c = DriverManager.getConnection(url);
            System.out.println("Connected :)");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

	public static void update(String tableName, String columnName, int id, String newValue){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "UPDATE " + tableName + " set " + columnName + " = " + newValue + " where ID = " + id + ";";
			stmt.executeUpdate(sql);
			c.commit();


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
