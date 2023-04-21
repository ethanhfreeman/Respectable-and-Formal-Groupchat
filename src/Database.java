
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

	public static void createUsers(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS users(name varchar(30) primary key, password varchar(30));" ;
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void createChatroom(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql =  "CREATE TABLE IF NOT EXISTS chatroom(name varchar(30) primary key);";
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static void createUsersChatroom(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS users_chatroom(username varchar(30), chatname varchar(30), " +
					"FOREIGN KEY (username) REFERENCES users(name), FOREIGN KEY (chatname) REFERENCES chatroom(name));";
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static void createUserMessages(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS users_messages (username varchar(30), " +
					"chatname varchar(30), content varchar(50), id int primary key, FOREIGN KEY (username) " +
					"REFERENCES users(name), FOREIGN KEY (chatname) references chatroom(name));" ;
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static void createUserTable(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "create table users (name varchar(30) primary key, password varchar(30));" ;
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
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
	
	
	/*
	 * THIS IS USED TO INSERT A MESSAGE INTO users_messages
	 */
	public static void insertMessage(String tableName, String username, String content, String chatName) {
		
		try{
			//this section generates the newest id for insertion
			int startingId = 1;
	    	
	    	while (Database.select("users_messages", "id", Integer.toString(startingId)) != null) {
	    	Database.select("users_messages", "id", Integer.toString(startingId++));
	    	}
	    	//end of first commented section
			c.setAutoCommit(false);
			stmt = c.createStatement();			
			String sql = "INSERT INTO " + tableName + "(username, chatname, content, id)" +
					" VALUES('" + username + "', '" + chatName + "', '" + content + "', " + startingId +");" ;
			stmt.executeUpdate(sql);
			c.commit();
			

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
			
	}
	
	
public static void insertUserToChatroom(String tableName, String username, String chatName) {
		
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();			
			String sql = "INSERT INTO " + tableName + "(username, chatname)" +
					" VALUES('" + username + "', '" + chatName +"');" ;
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
			
	}

public static void removeUserFromChatroom(String tableName, String username, String chatName) {
	
	try{
		c.setAutoCommit(false);
		stmt = c.createStatement();			
		String sql = "delete  from " + tableName + "where username = " + "'" + username + "');" ;
		stmt.executeUpdate(sql);
		c.commit();
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
	
	
	
	public static void updateWithoutID(String tableName, String columnName, String newValue, String oldValue){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "UPDATE " + tableName + " set " + columnName + " = '" + newValue + "' where name = '" + oldValue + "';";
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
	
	public static void insertChatroom(String tableName, String chatName) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO " + tableName + "(name)" +
					"VALUES('" + chatName + "');";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		}
	
	
	public static String selectMessageWithChatname(String chatName, int id){
		String desiredUser = null;
		String desiredMessage = null;
		String returnString = "";

		try {
			stmt = c.createStatement();
			String sql = "SELECT username,content FROM users_messages WHERE id "
					+ " = '" + id + "' and chatname = '" + chatName + "';";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				desiredUser = rs.getString(1);
				desiredMessage = rs.getString(2);
			}
			rs.close();
			stmt.close();
			returnString = desiredUser + "-> " + desiredMessage;
			return returnString;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		finally {
			return returnString;
		}
	}
	
	
	public static void getMessages(String chatName) {
		
		try{
			//this section generates the newest id for insertion
			int currentId =  (int)getFirstMessageId(chatName);
	    	
	    	while (!selectMessageWithChatname(chatName, currentId).equals("null-> null")) {
	    	System.out.println(Database.selectMessageWithChatname(chatName, currentId));
	    	Main.currentKnownMessages++;
	    	
	    	if (getNextMessageId(chatName, currentId) == null) {
	    		currentId++;
	    	} else {
	    		currentId = (int)getNextMessageId(chatName, currentId);
	    	}
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
			
	}
	
	public static Object getFirstMessageId(String chatName) {
		Object desiredObj = null;

		try {
			stmt = c.createStatement();
			String sql = "select id from users_messages where chatname = '" + chatName + "'" +
					"order by id ASC limit 1";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			//MULTIPURPOSE FOR ALL OBJ TYPEs
			//SELECT * FROM users WHERE id = (SELECT MAX(id) FROM users);
			if (rs.next()) {
				desiredObj = rs.getObject("id");
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
	
	
	public static Object getNextMessageId(String chatName, int currentId) {
		Object desiredObj = null;

		try {
			stmt = c.createStatement();
			String sql = "select id from users_messages where chatname = '" + chatName + "'" +
					" and id > " + currentId + " order by id ASC limit 1";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			//MULTIPURPOSE FOR ALL OBJ TYPEs
			//SELECT * FROM users WHERE id = (SELECT MAX(id) FROM users);
			if (rs.next()) {
				desiredObj = rs.getObject("id");
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
