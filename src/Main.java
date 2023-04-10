import java.util.Scanner;
public class Main {
	public static Scanner scnr = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
    	Database.connect("usersdb");
    	Database.createTable();
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
    	
    	//if username exists then
    	//prompt the user to try a different username
    	//if the username doesn't exist
    	//let the user create a password and update the table to have both the new user and pass
    	
    	if (username.equals("") || password.equals("")) {
    		while (username.equals("") || password.equals("")) {
    			System.out.println("ERROR : Please fill out both fields.");
    			System.out.print("Username: ");
    	    	username = scnr.nextLine();
    	    	System.out.print("Password: ");
    	    	password = scnr.nextLine();
    		}
    		
    	}
    	
    	if (Database.select("userinfo", "username", username)!= null) {
    		System.out.println("ERROR : Username unavailable, please try again.");
    		register();
    	} else if (username.trim().equals("") || password.trim().equals("")) {
    		System.out.println("ERROR : Username and/or password fields must be filled in. Please try again.");
    		register();
    	} else if (username.contains(" ") || password.contains(" ")) {
    		System.out.println("ERROR : Username or password used illegal character \" \", please try again without a space.");
    		register();
    	} else {
    		Database.insert("userinfo", newId(), username, password);
    		System.out.println("Please type (L) to LOGIN");
    		System.out.println();
    		System.out.print("-");
    		String input = scnr.next().toUpperCase();
    		while (!input.equals("L")) {
    			System.out.println("ERROR : Unrecognzied character, please try again.");
    			System.out.print("-");
    			input = scnr.next().toUpperCase();
    		}
    		login();
    	}
    	
    	
    }
    
    
    /*NEWID DOCUMENTATION
     * this will generate the next valid id number for 
     * the use of inserting in the databse
     */
    public static int newId() {
    	int startingId = 1;
    	
    	while (Database.select("userinfo", "id", Integer.toString(startingId)) != null) {
    	Database.select("userinfo", "id", Integer.toString(startingId++));
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

		//TODO Fix leaving username and password empty being successful logins, it should tell the user the
		//TODO fields can't be blank.
    	
    	
    	
    	
    	if (Database.select("userinfo", "username", username)!= null) {
    		if (password.equals(Database.selectPassword("userinfo", "username", username).toString().trim())) {
    			System.out.println("This was the correct password");
    			//this branch of the if statement will need to be updated for the chat room implementation.
    		} else { //password was wrong, so they need to try again.
    			System.out.println("ERROR : Incorrect password, please press (T) to try again,");
    			System.out.print("        or (M) to return back to the main menu.");
    			System.out.println();
    			System.out.print("-");
    			String input = scnr.next().toUpperCase();
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
    	} else { //username doesn't exit, so they need to try again (unifished)\
    		System.out.println("ERROR : Username doesn't exist. Press either (T) to");
    		System.out.print("      try again or (M) to return to the main menu.");
    		System.out.println();
			System.out.print("-");
			String input = scnr.next().toUpperCase();
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
    	
    	
    }
    
    public static void quit() {
    	
    }
}