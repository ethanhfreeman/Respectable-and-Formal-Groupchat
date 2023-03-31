import java.sql.*;
public class Database {

    private static java.sql.Connection conn = null;

    public static void Connect() throws Exception {
        try {
            String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=123&ssl=false";
            //TODO change these parameters to suit your localhost username and password
            conn = DriverManager.getConnection(url);
            System.out.println("Connected :)");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
}
