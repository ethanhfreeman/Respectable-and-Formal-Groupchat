import java.sql.*;
public class Connection {
        java.sql.Connection conn = null;
        Statement stat = null;

        public static void Connect() throws Exception{
        try {
            String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=123&ssl=false";
            conn = DriverManager.getConnection(url);
            System.out.println("Connected :)");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
