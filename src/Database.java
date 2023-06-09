
import java.sql.*;
import java.util.ArrayList;
public class Database {

    private static java.sql.Connection c = null;
	private static Statement stmt = null;

	public static int loaded;


	public static void connectOnline() throws Exception{
			String url = "jdbc:postgresql://dpg-ch4l47ss3fvjtidtvj30-a.oregon-postgres.render.com:5432/usersdb_8irl";
			String username = "usersdb_8irl_user";
			String password = "zpmuiLSPLUw6sO8B0qojjHa3lB42bhLD";
			c = DriverManager.getConnection(url, username, password);

	}


    public static void connectLocal(String url, String databaseName, String user, String password) throws Exception{
            String completeUrl = "jdbc:postgresql://" + url +"/" + databaseName + "?user=" + user + "&password=" + password +"&ssl=false";
            /*
             * Above, you need to insert the "tableName" portion of the url
             * to specify the database in which you are looking to connect to
             * previously this was just postgres, which just connects you to postgresql.
             * This being specified to tableName, essentially does  "\c tableName" and then the subsequent
             * methods like 'select()' work.
             */
            c = DriverManager.getConnection(completeUrl);
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
		finally {
			deletedUserInit();
		}

	}
	public static void deletedUserInit(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO USERS(name)VALUES('[DELETED USER]');" ;
			stmt.executeUpdate(sql);
			c.commit();
			//System.out.println("User created :)");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void deleteUsers(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DROP TABLE IF EXISTS users;" ;
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void createUsersOnline(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS users_online(username varchar(30), FOREIGN KEY (username) REFERENCES users(name) ON UPDATE CASCADE);";
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void deleteUsersOnline(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DROP TABLE IF EXISTS users_online;";
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
	public static void deleteChatroom(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DROP TABLE IF EXISTS chatroom;" ;
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
					"FOREIGN KEY (username) REFERENCES users(name) ON UPDATE CASCADE, FOREIGN KEY (chatname) REFERENCES chatroom(name));";
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void deleteUsersChatroom(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DROP TABLE IF EXISTS users_chatroom;" ;
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
					"REFERENCES users(name) ON UPDATE CASCADE, FOREIGN KEY (chatname) references chatroom(name));" ;
			stmt.executeUpdate(sql);
			c.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void deleteUserMessages(){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DROP TABLE IF EXISTS users_messages;" ;
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
					"VALUES('" + encodeVal(username) + "', '" + encodeVal(password) +"');" ;
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
		//ENCODE SINGE QUOTES TO PREVENT SQL CONFUSION

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
					" VALUES('" + encodeVal(username) + "', '" + encodeVal(chatName) + "', '" + encodeVal(content) + "', " + startingId +");" ;
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
					" VALUES('" + encodeVal(username) + "', '" + encodeVal(chatName) +"');" ;
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
			
	}

	public static void addUserOnline(String username){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO users_online(username)" +
					" VALUES('" + encodeVal(username) + "');" ;
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static void deleteUser(String tableName, String username){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE FROM " + tableName + " WHERE username = '" + encodeVal(username) + "';";
			stmt.executeUpdate(sql);
			c.commit();
			System.out.println("DEBUG: Left Room Reference");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void deleteActualUser(String username){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE FROM users WHERE name = '" + encodeVal(username) + "';";
			stmt.executeUpdate(sql);
			c.commit();
			System.out.println("DEBUG: Left Room");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void deleteRoomReference(String tableName, String roomname){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE FROM " + tableName + " WHERE chatname = '" + encodeVal(roomname) + "';";
			stmt.executeUpdate(sql);
			c.commit();
			System.out.println("DEBUG: Deleted Room");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static void deleteRoom(String roomname){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE FROM chatroom WHERE name = '" + encodeVal(roomname) + "';";
			stmt.executeUpdate(sql);
			c.commit();
			System.out.println("DEBUG: Deleted Room");

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
			String sql = "UPDATE " + tableName + " set " + columnName + " = '" + encodeVal(newValue) + "' where name = '" + encodeVal(oldValue) + "';";
			stmt.executeUpdate(sql);
			c.commit();


		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static void updateOtherWithoutID(String tableName, String columnName, String newValue, String oldValue){
		try{
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "UPDATE " + tableName + " set " + columnName + " = '" + encodeVal(newValue) + "' where username = '" + encodeVal(oldValue) + "';";
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
					+ columnName + " = '" + encodeVal(selection) + "';";
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
					+ columnName + " = '" + encodeVal(selection) + "';";
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
					"VALUES('" + encodeVal(chatName) + "');";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	public static ArrayList<String> getAllChatroomNames() {
		ArrayList<String> chatroomNames = new ArrayList<>();
		try {

			String sql = "SELECT name FROM chatroom;";
			stmt = c.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);

			while (resultSet.next()) {
				String chatroomName = resultSet.getString("name");
				chatroomNames.add(chatroomName);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("DEBUG: LOADING ROOMS");
		return chatroomNames;
	}
	
	
	public static String selectMessageWithChatname(String chatName, int id){
		String desiredUser = null;
		String desiredMessage = null;
		String returnString = "";

		try {
			stmt = c.createStatement();
			String sql = "SELECT username,content FROM users_messages WHERE id "
					+ " = '" + id + "' and chatname = '" + encodeVal(chatName) + "';";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				desiredUser = rs.getString(1);
				desiredMessage = rs.getString(2);
			}
			rs.close();
			stmt.close();
			if (desiredMessage == null) {
				returnString = desiredUser + "-> " + desiredMessage;
			}
			else {
				returnString = desiredMessage.substring(0,6) + desiredUser + "-> " + desiredMessage.substring(6);
			}
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
	
	
	
	public static ArrayList<String> printActiveUsers(String chatName) {
		String desiredUser = null;
		ArrayList<String> userList = new ArrayList<>();

		try {
			stmt = c.createStatement();
			String sql = "SELECT username FROM users_chatroom WHERE chatname "
					+ " = '" + encodeVal(chatName) + "';";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				desiredUser = rs.getString(1);
				userList.add(desiredUser);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		finally {
			return userList;
		}
	}
	public static ArrayList<String> printOnlineUsers() {
		String desiredUser = null;
		ArrayList<String> userList = new ArrayList<>();

		try {
			stmt = c.createStatement();
			String sql = "SELECT username FROM users_online;";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				desiredUser = rs.getString(1);
				userList.add(desiredUser);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		finally {
			return userList;
		}
	}
	public static ArrayList<String> getMessages(String chatName) {
		
		ArrayList<String> msgHistory = new ArrayList<>();
		try{
			//this section generates the newest id for insertion
			
			if (getFirstMessageId(chatName) == null) {
				return msgHistory;
			}
			int currentId =  (int)getFirstMessageId(chatName);
	    	
	    	while (!selectMessageWithChatname(chatName, currentId).contains("null-> null")) {
	    	msgHistory.add(Database.selectMessageWithChatname(chatName, currentId));
			loaded++;
	    	
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
		return msgHistory;
			
	}
	
	
	
	
	
	public static Object getFirstMessageId(String chatName) {
		Object desiredObj = null;

		try {
			stmt = c.createStatement();
			String sql = "select id from users_messages where chatname = '" + encodeVal(chatName) + "'" +
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
			String sql = "select id from users_messages where chatname = '" + encodeVal(chatName) + "'" +
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
	public static String encodeVal (String ogString){
		String encodedContent = "";

		for (int i = 0; i < ogString.length(); i++) {
			if (ogString.charAt(i) == '\'') { // Check if the character is found in the string
				encodedContent += "'" + '\'';
			} else {
				encodedContent += ogString.charAt(i);
			}
		}
		return  encodedContent;
	}
	
	public static ArrayList<String> printNewMessages(String chatName) {
		//example current known messages is 5 but there are 6 total messages in baller2
		String totalMessagesStr = "";
		int totalMessages = 0;
		ArrayList<String> messagesToGive = new ArrayList<String>();

		try {
			stmt = c.createStatement();
			String sql = "SELECT count(content) from users_messages where chatname = '" + encodeVal(chatName) + "';";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			//MULTIPURPOSE FOR ALL OBJ TYPEs
			//SELECT * FROM users WHERE id = (SELECT MAX(id) FROM users);
			if (rs.next()) {
				totalMessagesStr = "" + rs.getObject("count");
				totalMessagesStr.replaceAll("\\s", "");
				totalMessages = Integer.parseInt(totalMessagesStr);
			}
//			rs.close();
//			stmt.close();
			
			
			ArrayList<Integer> messages = new ArrayList<Integer>();	
			
				stmt = null;
				stmt = c.createStatement();
				String sql2 = "select id from users_messages where chatname = '" + encodeVal(chatName) + "';";
				rs = stmt.executeQuery(sql2);	
				while (rs.next()) {
					messages.add( (Integer)rs.getObject("id")  );
					//this adds all of the ids of the messages from the column to a list
				}
				rs.close();
				stmt.close();
			
//			int idOfListForCurrentMessage = 0;
//			for (int i = 0; i <= chatWindow.currentKnownMessages; i++) {
//				idOfListForCurrentMessage++; // now we should  have the index of the next chat message that has not been seen by the user
//			}

			
			//REVIEW THIS PART UNDER HERE FOR LOGIC ERRORS ------ HASNT BEEN TESTED
			int newMessages = 0;
			for (int j = loaded; j <= messages.size() - 1; j++  ) {
				messagesToGive.add(Database.selectMessageWithChatname(chatName, messages.get(j)));
				newMessages++;
			}
			loaded += newMessages;


		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return messagesToGive;
		
	}
	
}
