import java.util.Scanner;
public class Main {
	public static Scanner scnr = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
    	
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
    }
    
    public static void login() {
    	
    }
    
    public static void quit() {
    	
    }
}