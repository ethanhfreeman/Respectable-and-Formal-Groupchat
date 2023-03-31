import java.sql.*;
public class Connection {

        public static void Connect() throws Exception{
        try {
            String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=123&ssl=false";
            java.sql.Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected :)");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
