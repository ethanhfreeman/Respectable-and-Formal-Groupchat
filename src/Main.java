import java.util.Scanner;
public class Main {
	public static Scanner scnr = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
    	Database.connect("usersdb");
    	Database.createTable();
        printMainMenu();
    }
    
    public static void printMainMenu() {
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
    	}
    }
    
    public static void register() {
    	String username = "";
    	String password = "";
    	
    	System.out.println();
    	System.out.print("Username: ");
    	username = scnr.nextLine();
    	System.out.print("Password: ");
    	password = scnr.nextLine();
    	
    	//if username exists then
    	//prompt the user to try a different username
    	//if the username doesn't exist
    	//let the user create a password and update the table to have both the new user and pass
    	
    	
    }
    
    public static void login() {
    	
    }
    
    public static void quit() {
    	
    }
}