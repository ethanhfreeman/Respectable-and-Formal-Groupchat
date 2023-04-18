
import java.sql.*;
import java.util.Locale;

public class Database {

    private static java.sql.Connection c = null;
	private static Statement stmt = null;

    public static void connect(String tableName) throws Exception {
        try {
            String url = "jdbc:postgresql://localhost/" + tableName + "?user=" + localHostInfo.getLocalUserName() + "&password=" + localHostInfo.getLocalPassword() +"&ssl=false";
            //TODO change parameters in localHostInfo.java to suit your localhost username and password
            /*
             * Above, you need to insert the "tableName" portion of the url
             * to specify the database in which you are looking to connect to
             * previously this was just postgres, which just connects you to postgresql.
             * This being specified to tableName, essentially does  "\c tableName" and then the subsequent
             * methods like 'select()' work.
             */
            c = DriverManager.getConnection(url);
           // System.out.println("Connected :)"); this is annoying asl
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
/*
 * ****CREATETABLE DOCUMENTATION ****
 * Currently this method will create the userinfo table
 * without any issues. If the table exists, no changes
 * will be made to the userinfo table. If it does not
 * already exist, it will create a new table called
 * "userinfo"
 */
	public static void createTable(){
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS userinfo" +
			"(ID INT PRIMARY KEY NOT NULL, " +
				"USERNAME CHAR(25) NOT NULL," +
					"PASSWORD CHAR(25) NOT NULL);";
			stmt.executeUpdate(sql);
			stmt.close();
			//System.out.println("Table has been created");
		} catch(Exception e) {
			e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
		}


	}

	public static void insert(String tableName, String username, String password){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO " + tableName + "(name, password)" +
					"VALUES('" + username + "', '" + password +"');" ;
			stmt.executeUpdate(sql);
			c.commit();
			//System.out.println("User created :)");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static void delete(String tableName, int id){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE FROM " + tableName + " WHERE id = " + id;
			stmt.executeUpdate(sql);
			c.commit();

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
			String sql = "UPDATE " + tableName + " set " + columnName + " = '" + newValue + "' where ID = " + id + ";";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			c.commit();


		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

/* **************SELECT METHOD DOCUMENTATION*****************
 * Select method works as long as you have the database created
 * named "usersdb" and that you have a table called "userinfo"
 * which has columns (ID int primary key, username char(20), password char(20))
 * 
 */
	public static Object select(String tableName, String columnName, String selection){
		Object desiredObj = null;

		try {
			stmt = c.createStatement();
			String sql = "SELECT * FROM " + tableName + " WHERE "
					+ columnName + " = '" + selection + "';";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			//MULTIPURPOSE FOR ALL OBJ TYPEs
			//SELECT * FROM users WHERE id = (SELECT MAX(id) FROM users);
			if (rs.next()) {
				desiredObj = rs.getObject(columnName);
			}
			rs.close();
			stmt.close();
			return desiredObj;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		finally {
			return desiredObj;
		}
	}
	
	
	//this method below will return the password of a given username
	public static Object selectPassword(String tableName, String columnName, String selection){
		Object desiredObj = null;

		try {
			stmt = c.createStatement();
			String sql = "SELECT password FROM " + tableName + " WHERE "
					+ columnName + " = '" + selection + "';";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			//MULTIPURPOSE FOR ALL OBJ TYPEs
			//SELECT * FROM users WHERE id = (SELECT MAX(id) FROM users);
			if (rs.next()) {
				desiredObj = rs.getObject("password");
			}
			rs.close();
			stmt.close();
			return desiredObj;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		finally {
			return desiredObj;
		}
	}
}
