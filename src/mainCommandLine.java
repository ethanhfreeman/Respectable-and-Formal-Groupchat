import java.util.Scanner;
public class mainCommandLine {
	public static Scanner scnr = new Scanner(System.in);
	static String currentChatroom = "";
	static String currentUser = "";
	static int currentKnownMessages = 0;
	
    public static void main(String[] args) throws Exception {
    	Database.connectLocal("usersdb", "usersdb", localHostInfo.getLocalUserName(), localHostInfo.getLocalPassword());
        printMainMenu();
    }
    
    public static void printMainMenu() {
    	System.out.println("-----------------------------------------");
    	System.out.println("Welcome To the Deez Nutz inc ChatRoom.");
    	System.out.println();
    	System.out.println("Please select from the following options:");
    	System.out.println("(R)egister, (L)ogin, (Q)uit");
    	System.out.println("-----------------------------------------");
    	System.out.println();
    	System.out.print("-");
    	String input = scnr.nextLine().toUpperCase();
    	
    	if (input.equals("R")) {
    		register();
    	} else if (input.equals("L")) {
    		login();
    	} else if (input.equals("Q")) {
    		quit();
    	} else {
			//temp
			System.out.println("Not a valid choice, restarting application");
			printMainMenu();
		}
    }
    
    public static void register() {
    	String username = "";
    	String password = "";
    	
    	System.out.println();
    	System.out.println("------------------------------------------");
    	System.out.println("              REGISTRATION");
    	System.out.print("Username: ");
    	username = scnr.nextLine();
    	System.out.print("Password: ");
    	password = scnr.nextLine();
    	if (username.equals("") || password.equals("")) {
    		while (username.equals("") || password.equals("")) {
    			System.out.println("ERROR : Please fill out both fields.");
    			System.out.print("Username: ");
    	    	username = scnr.nextLine();
    	    	System.out.print("Password: ");
    	    	password = scnr.nextLine();
    		}
    		
    	}
    	
    	if (Database.select("users", "name", username)!= null) {
    		System.out.println("ERROR : Username unavailable, please try again.");
    		register();
    	} else if (username.trim().equals("") || password.trim().equals("")) {
    		System.out.println("ERROR : Username and/or password fields must be filled in. Please try again.");
    		register();
    	} else if (username.contains(" ") || password.contains(" ")) {
    		System.out.println("ERROR : Username or password used illegal character \" \", please try again without a space.");
    		register();
    	} else {
    		Database.insert("users", username, password);
    		System.out.println("Please type (L) to LOGIN");
    		System.out.println();
    		System.out.print("-");
    		String input = scnr.nextLine().toUpperCase();
    		while (!input.equals("L")) {
    			System.out.println("ERROR : Unrecognzied character, please try again.");
    			System.out.print("-");
    			input = scnr.nextLine().toUpperCase();
    		}
    		
    		login();
    	}
    	
    	
    }
    
    
    /*NEWID DOCUMENTATION
     * this will generate the next valid id number for 
     * the use of inserting messages into users_messages
     */
    
 
    public static int newId() {
    	int startingId = 1;
    	
    	while (Database.select("users_messages", "id", Integer.toString(startingId)) != null) {
    	Database.select("users_messages", "id", Integer.toString(startingId++));
    	}
    	
    	return startingId;
    }
    
    
    public static int totalMessages(String chatName) {
    	int startingId = 0;
    	
    	
    	//while users_messages has a new id 
    	while (Database.select("users_messages", "id", Integer.toString(startingId)) != null) {
    	Database.select("users_messages", "id", Integer.toString(startingId++));
    	}
    	
    	return startingId;
    }
    
    
    
    public static void login() {
    	
    	
    	System.out.println("------------------------------------------");
    	System.out.println("                  LOGIN");
    	System.out.print("Username: ");
    	String username = scnr.nextLine();
    	System.out.print("Password: ");
    	String password = scnr.nextLine();
    	
    	if (Database.select("users", "name", username)!= null) {
    		if (password.equals(Database.selectPassword("users", "name", username).toString().trim())) {
    			System.out.println("Welcome " + username + "!");
    			System.out.println();
    			currentUser = username;
    			chatmenu();
    			//this branch of the if statement will need to be updated for the chat room implementation.
    		} else { //password was wrong, so they need to try again.
    			System.out.println("ERROR : Incorrect password, please press (T) to try again,");
    			System.out.print("        or (M) to return back to the main menu.");
    			System.out.println();
    			System.out.print("-");
    			String input = scnr.nextLine().toUpperCase();
        		while (!(input.equals("T") || input.equals("M"))) {
        			System.out.println("ERROR : Unrecognzied character, either push (T) to");
        			System.out.println("      try again or (M) to return to the main menu.");
        			System.out.print("-");
        			input = scnr.next().toUpperCase();
        		}
        		if (input.toUpperCase().equals("T")) {
        			scnr.nextLine();
        			login();
        		} else if (input.toUpperCase().equals("M")) {
        			scnr.nextLine();
        			printMainMenu();
        		}
        		
    		}
    	} else { 
    		System.out.println("ERROR : Username doesn't exist. Press either (T) to");
    		System.out.print("      try again or (M) to return to the main menu.");
    		System.out.println();
			System.out.print("-");
			String input = scnr.nextLine().toUpperCase();
    		while (!(input.equals("T") || input.equals("M"))) {
    			System.out.println("ERROR : Unrecognzied character, either push (T) to");
    			System.out.println("      try again or (M) to return to the main menu.");
    			System.out.print("-");
    			input = scnr.nextLine().toUpperCase();
    		}
    		if (input.toUpperCase().equals("T")) {
    			login();
    		} else if (input.toUpperCase().equals("M")) {
    			printMainMenu();
    		}
    	}
    	
    	
    }
    
    public static void quit() {
    	
    }
    
    public static void chatmenu() {
    	System.out.println("Please select from the following options:");
    	System.out.println("(J)oin, (C)reate, (A)ccount, (L)ogout");
    	System.out.println("-----------------------------------------");
    	System.out.println();
    	System.out.print("-");
    	String input = scnr.nextLine().toUpperCase();
    	
    	while (!(input.equals("J") || input.equals("C") || input.equals("A") || input.equals("L"))) {
			System.out.println("ERROR : Unrecognzied character, either push (J) to");
			System.out.println("        join, (C) to create, (A) for account info");
			System.out.println("        or (L) to logout. ");
			System.out.print("-");
			input = scnr.nextLine().toUpperCase();
		}
		if (input.toUpperCase().equals("J")) {
//			scnr.nextLine();
			joinmenu();
		} else if (input.toUpperCase().equals("C")) {
//			scnr.nextLine();
			createmenu();
		} else if (input.toUpperCase().equals("A")) {
//			scnr.nextLine();
			accountmenu();
		} else if (input.toUpperCase().equals("L")) {
//			scnr.nextLine();
			printMainMenu();
		}
    }

	private static void accountmenu() {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------------------");
		System.out.println("          ACCOUNT MENU");
		System.out.println("Change (U)sername or (P)assword");
		System.out.println();
		System.out.print("-");
		String input = scnr.nextLine().toUpperCase();
		while (!(input.equals("U") || input.equals("P"))) {
			System.out.println("ERROR : Unrecognzied character, either push (U) to");
			System.out.println("        change username or (P) to change password.");
			System.out.print("-");
			input = scnr.nextLine().toUpperCase();
		}
		if (input.toUpperCase().equals("U")) {
			System.out.print("[NEW USERNAME]: ");
			input = scnr.nextLine();
			//this while loop makes sure that the input is only A-Z and 0-9
			while (!input.matches("[a-zA-Z0-9]+")) {
				System.out.println("ERROR: Please, no weird symbols. Try another username.");
				System.out.println();
				System.out.print("[NEW USERNAME]: ");
				input = scnr.nextLine();
			}
			//this while loop checks to see if the username is taken
			while (Database.select("users", "name", input) != null) {
				System.out.println("ERROR: Username already exists. Please try another username.");
				System.out.println();
				System.out.print("[NEW USERNAME]: ");
				input = scnr.nextLine();
			}
			//Updates the users table to match the new username
			Database.updateWithoutID("users", "name", input, currentUser);
			//this currentUser variable update must come after the database update.
			currentUser = input;
			System.out.println("Success! Username has been changed to " + currentUser + "!");
			System.out.println();
			chatmenu();
			
		} else if (input.toUpperCase().equals("P")) {
			
			System.out.print("[NEW PASSWORD]: ");
			input = scnr.nextLine();
			//this while loop makes sure that the input is only A-Z and 0-9
			while (!input.matches("[a-zA-Z0-9]+")) {
				System.out.println("ERROR: Please, no weird symbols. Try another password.");
				System.out.println();
				System.out.print("[NEW PASSWORD]: ");
				currentChatroom = scnr.nextLine();
			}	
			//Updates the users table to match the new password
			Database.updateWithoutID("users", "password", input, currentUser);
			//this currentUser variable update must come after the database update.
			currentUser = input;
			System.out.println("Success! Password has been changed!");
			System.out.println();
			chatmenu();
		}
	}

	private static void createmenu() {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------------------");
		System.out.println("          CHATROOM CREATOR");
		System.out.println();
		System.out.println("What would you like to name your new chatroom?");
		System.out.print("[CHATNAME]: ");
		currentChatroom = scnr.nextLine();
		
		while (!currentChatroom.matches("[a-zA-Z0-9]+")) {
			System.out.println("ERROR: Please, no weird symbols. Try another name.");
			System.out.println();
			System.out.print("[CHATNAME]: ");
			currentChatroom = scnr.nextLine();
		}
		
		while (Database.select("chatroom", "name", currentChatroom) != null) {
			System.out.println("ERROR: Chatname already exists. Please try another name.");
			System.out.println();
			System.out.print("[CHATNAME]: ");
			currentChatroom = scnr.nextLine();
		}
		Database.insertChatroom("chatroom", currentChatroom);
		Database.insertUserToChatroom("users_chatroom", currentUser, currentChatroom);
		System.out.println("Success! Welcome to " + currentChatroom + "!");
		System.out.println("-----------------------------------------");
		chatRoomMessageLoop();

		
	}

	public static void joinmenu() {
		System.out.println("-----------------------------------------");
		System.out.println("          CHATROOM JOINER");
		System.out.println();
		System.out.println("What chatroom would you like to join?");
		System.out.print("[CHATNAME]: ");
		currentChatroom = scnr.nextLine();
		
		while (!currentChatroom.matches("[a-zA-Z0-9]+")) {
			System.out.println("ERROR: Please, no weird symbols. Try another name.");
			System.out.println();
			System.out.print("[CHATNAME]: ");
			currentChatroom = scnr.nextLine();
		}
		
		while (Database.select("chatroom", "name", currentChatroom) == null) {
			System.out.println("ERROR: Chatname doesn't exist. Please try another name.");
			System.out.println();
			System.out.print("[CHATNAME]: ");
			currentChatroom = scnr.nextLine();
		}
		Database.insertUserToChatroom("users_chatroom", currentUser, currentChatroom);
		System.out.println("Success! Welcome to " + currentChatroom + "!");
		System.out.println("-----------------------------------------");
		Database.getMessages(currentChatroom);
		chatRoomMessageLoop();
	}
	
	
		public static void chatRoomMessageLoop() {
			//this does not account for an empty string where the message is "",
			while (true) {
				//TODO getNewMessages()
				String input = scnr.nextLine();
				
				if (input.equals("") || input.replaceAll("\\s", "").equals("")) {
					System.out.println("Please send an actual message and not blank space.");
					
				} else if (input.charAt(0) == '/') {
					
					if (input.equals("/help")) {
						System.out.println("Valid chat commands include:");
						System.out.println("     /help");
						System.out.println("     /list");
						System.out.println("     /history");
						System.out.println("     /leave");
					} else if (input.equals("/list")) {
						Database.printActiveUsers(currentChatroom);
					} else if (input.equals("/history")) {
						Database.getMessages(currentChatroom);
					} else if (input.equals("/leave")) {
						//TODO delete user from users_chatroom
						Database.deleteUser("users_chatroom", currentUser);
						currentChatroom = "";
						chatmenu();
					} else {
						System.out.println("ERROR: Unknown command. Use /help for a list of commands.");

					}
					
				} else {
					    Database.printNewMessages(currentChatroom);
						Database.insertMessage("users_messages", currentUser, input, currentChatroom);
					
				}
				
			}
		}
	
}