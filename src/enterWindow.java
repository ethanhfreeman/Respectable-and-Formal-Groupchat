import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class enterWindow {
    static String currentUser = "";

    private static JTextField usernameField;
    private static JPasswordField passwordField;

    private static JPasswordField confirmPasswordField;


    public static class accountDeletionWindow extends JFrame {
        public accountDeletionWindow(){
            Object[] options = {"No, Do Not Delete My Account", "Yes, Delete My Account"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Are you sure you want to delete your account?",
                    "Respectable And Formal Groupchat",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);


            switch (choice) {
                case 0 -> {
                    //user changes mind
                    new accountView();
                    dispose();
                }
                case 1 -> {
                    //delete option selected
                    Database.deleteUser("users_online", currentUser);
                    Database.updateOtherWithoutID("users_messages", "username", "[DELETED USER]", currentUser);
                    Database.deleteUser("users_chatroom", currentUser);
                    Database.deleteActualUser(currentUser);
                    currentUser = "";
                    dispose();
                    String successMessage = "Account has been deleted. Returning to login window...";
                    JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                    new choiceMenu();
                    dispose();
                }
            }
        }
    }
    public static class passwordChangeWindow extends JFrame{
        public passwordChangeWindow(){
            super("RAFG - Password Change");
            setSize(300, 175);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);


            JPanel panel = new JPanel();
            JLabel passwordLabel = new JLabel("Enter a New Password:");
            passwordField = new JPasswordField(20);
            JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
            confirmPasswordField = new JPasswordField(20);


            panel.add(passwordLabel);
            panel.add(passwordField);
            panel.add(confirmPasswordLabel);
            panel.add(confirmPasswordField);


            JButton changeButton = new JButton("Change Password");
            changeButton.addActionListener(e -> changePassword());
            panel.add(changeButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(e -> {
                dispose();
                new accountView();
            }
            );
            panel.add(goBackButton);


            add(panel);
            setVisible(true);
        }
        private void changePassword(){
            String newPassword = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            //this while loop makes sure that the input is only A-Z and 0-9
            if (newPassword.equals("") || confirmPassword.equals("")) {
                String errorString = "ERROR : Please fill out all fields.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!confirmPassword.equals(newPassword)){
                String errorString = "ERROR : Password fields do not match.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newPassword.length() > 30) {
                String errorString = "ERROR : Changed username cannot be greater than 30 characters.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newPassword.contains(" ")){
                String errorString = "ERROR : Password cannot have spaces.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Updates the users table to match the new password
            Database.updateWithoutID("users", "password", newPassword, currentUser);
            String successMessage = "Success! Password has been changed!";
            JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new enterWindow.accountView();

        }
    }

    public static class usernameChangeWindow extends JFrame{
        public usernameChangeWindow(){
                super("RAFG - Username Change");
                setSize(300, 125);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setLocationRelativeTo(null);



                JPanel panel = new JPanel();
                JLabel usernameLabel = new JLabel("Enter a Unique Username:");
                usernameField = new JTextField(20);

                panel.add(usernameLabel);
                panel.add(usernameField);

            JButton changeButton = new JButton("Change Name");
            changeButton.addActionListener(e -> changeName());
            panel.add(changeButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(e -> {
                dispose();
                new accountView();
            }
            );
            panel.add(goBackButton);



            add(panel);
            setVisible(true);




        }
        private void changeName(){
            String username = usernameField.getText();

            if (username.equals("")) {
                String errorString = "ERROR : Please fill out the field.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (username.equals(currentUser)) {
                String errorString = "ERROR : Changed username cannot be same as current username.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (username.length() > 30) {
                String errorString = "ERROR : Changed username cannot be greater than 30 characters.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (username.equals("[DELETED USER]")) {
                String errorString = "ERROR: Name not allowed";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (username.contains(" ")){
                String errorString = "ERROR : Changed username cannot have spaces.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //this checks to see if the username is taken
            if (Database.select("users", "name", username) != null) {
                String errorString = "ERROR: Username already exists. Please try another username.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;

            }

            Database.updateWithoutID("users", "name", username, currentUser);


            //Updates the users table to match the new username
            currentUser = username;
            //this currentUser variable update must come after the database update.
            String successMessage = "Success! Username has been changed to " + currentUser + "!";
            JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new enterWindow.accountView();

        }


    }
    public static class userRegisterWindow extends JFrame {

        public userRegisterWindow() {
            super("RAFG - User Registration");
            setSize(300, 225);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            JLabel usernameLabel = new JLabel("Enter a Unique Username:");
            usernameField = new JTextField(20);
            JLabel passwordLabel = new JLabel("Enter Password:");
            passwordField = new JPasswordField(20);
            JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
            confirmPasswordField = new JPasswordField(20);

            panel.add(usernameLabel);
            panel.add(usernameField);
            panel.add(passwordLabel);
            panel.add(passwordField);
            panel.add(confirmPasswordLabel);
            panel.add(confirmPasswordField);

            JButton registerButton = new JButton("Register");
            registerButton.addActionListener(e -> register());
            panel.add(registerButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(e -> {
                dispose();
                new choiceMenu();
            }
            );

            panel.add(goBackButton);

            add(panel);
            setVisible(true);
        }

        private void register(){
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.equals("") || password.equals("") || confirmPassword.equals("")) {
                String errorString = "ERROR : Please fill out all fields.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!confirmPassword.equals(password)){
                String errorString = "ERROR : Password fields do not match.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (username.length() > 30 || password.length() > 30 ) {
                String errorString = "ERROR : Both username and password cannot be greater than 30 characters.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (username.equals("[DELETED USER]")) {
                String errorString = "ERROR: Name not allowed";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (username.contains(" ") || password.contains(" ")){
                String errorString = "ERROR : Spaces are not allowed in username or password.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Database.select("users", "name", username)!= null) {
                String errorString = "ERROR : Username unavailable, please try again.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                //insert into database
                Database.insert("users", username, password);
                String successMessage = (username + " successfully added to registry!") ;
                JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close window and go to user menu
                currentUser = username;
                Database.addUserOnline(currentUser);
                dispose();
                new enterWindow.mainView();
            }
        }
    }
    public static class userLoginWindow extends JFrame {
        public userLoginWindow() {
            super("RAFG - User Login");
            setSize(300, 175);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            JLabel usernameLabel = new JLabel("Username:");
            usernameField = new JTextField(20);
            JLabel passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField(20);

            panel.add(usernameLabel);
            panel.add(usernameField);
            panel.add(passwordLabel);
            panel.add(passwordField);

            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(e -> login());
            panel.add(loginButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(e -> {
                dispose();
                new choiceMenu();
            }
            );

            panel.add(goBackButton);

            add(panel);
            setVisible(true);
        }
        private void login(){
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if(username.equals("") || password.equals("")){
                String errorString = "ERROR : Please fill both fields.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Database.select("users", "name", username)!= null) {
                if (password.equals(Database.selectPassword("users", "name", username).toString().trim())) {
                    //SET LOGGED IN USER AS CURRENT USER
                    currentUser = username;
                    Database.addUserOnline(currentUser);
                    dispose();
                    new enterWindow.mainView();
                } else { //password was wrong, so they need to try again.
                    String errorString = "ERROR : Incorrect password";
                    JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else { //username NOT in database table
                String errorString = "ERROR : " + username + " not found in the Database.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
            }

        }

    }
    public static class mainView extends JFrame{
        public mainView(){
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Object[] options = {"Room Browser", "Manage Account","Logout"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Welcome, " + currentUser + "!\nPlease select from the following options:",
                    "Respectable And Formal Groupchat - Main View.",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    imageCollection.mainIcon,
                    options,
                    options[0]);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Custom action on JOptionPane closure
                    System.exit(0);
                }
            });

            //display list of options
            switch (choice) {
                case 0 -> {
                    // room browser option selected
                    new chatroomList(currentUser);
                    dispose();
                }
                case 1 -> {
                    // Account management option selected
                    new accountView();
                    dispose();
                }
                case 2 -> {
                    // Logout option selected
                    // CLEAR CURRENT USER
                    Database.deleteUser("users_online", currentUser);
                    currentUser = "";
                    new choiceMenu();
                    dispose();
                }
            }



        }
    }
    public static class accountView extends JFrame{
    public accountView() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Object[] options = {"Back To Main View","Change Username","Change Password","Delete Account"};
        int choice = JOptionPane.showOptionDialog(this,
                "Please select from the following options:",
                "Respectable And Formal Groupchat - Account Management",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                imageCollection.settingsIcon,
                options,
                options[0]);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Custom action on JOptionPane closure
                new enterWindow.mainView();
                dispose();
//                System.exit(0);
            }
        });


        pack();
        //display list of options
        switch (choice) {
            case 0 -> {
                //Go Back
                new mainView();
                dispose();
            }
            case 1 -> {
                // Change username
                new usernameChangeWindow();
                dispose();
            }
            case 2 -> {
                // Change password
                new passwordChangeWindow();
                dispose();
            }
            case 3 -> {
                //delete Account
                new accountDeletionWindow();
                dispose();
            }
        }
        }
    }
}
