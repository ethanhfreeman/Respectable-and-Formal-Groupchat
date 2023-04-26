import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class enterWindow extends JFrame{
    static String currentUser = "";

    private static JTextField usernameField;
    private static JPasswordField passwordField;



    public static class passwordChangeWindow extends JFrame{
        public passwordChangeWindow(){
            super("Deez Nutz Inc - Password Change");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            JLabel passwordLabel = new JLabel("Enter a new Password:");
            passwordField = new JPasswordField(20);

            panel.add(passwordLabel);
            panel.add(passwordField);


            JButton changeButton = new JButton("Change Password");
            changeButton.addActionListener(e -> changePassword());
            panel.add(changeButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                       dispose();
                       new accountView();
                   }
               }
            );
            panel.add(goBackButton);


            add(panel);
            setVisible(true);
        }
        private void changePassword(){
            String password = new String(passwordField.getPassword());
            //this while loop makes sure that the input is only A-Z and 0-9
            if (password.equals("")) {
                String errorString = "ERROR : Please fill out the field.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.matches("[a-zA-Z0-9]+")) {
                String errorString = "ERROR: Please, no weird symbols. Try another password.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Updates the users table to match the new password
            Database.updateWithoutID("users", "password", password, currentUser);
            String successMessage = "Success! Password has been changed!";
            JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new enterWindow.accountView();

        }
    }

    public static class usernameChangeWindow extends JFrame{
        public usernameChangeWindow(){
                super("Deez Nutz Inc - Username Change");
                setSize(300, 150);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel();
                JLabel usernameLabel = new JLabel("Enter a Unique Username:");
                usernameField = new JTextField(20);

                panel.add(usernameLabel);
                panel.add(usernameField);

            JButton changeButton = new JButton("Change Name");
            changeButton.addActionListener(e -> changeName());
            panel.add(changeButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                       dispose();
                       new accountView();
                   }
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

            if (!username.matches("[a-zA-Z0-9]+")) {
                String errorString = "ERROR: Please, no weird symbols. Try another username.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //this checks to see if the username is taken
            if (Database.select("users", "name", username) != null) {
                String errorString = "ERROR: Username already exists. Please try another username.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;

            }
            Database.deleteUser("users_chatroom", currentUser);
            Database.deleteUser("users_messages", currentUser);
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
            super("Deez Nutz Inc - User Registration");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            JLabel usernameLabel = new JLabel("Enter a Unique Username:");
            usernameField = new JTextField(20);
            JLabel passwordLabel = new JLabel("Enter Password:");
            passwordField = new JPasswordField(20);

            panel.add(usernameLabel);
            panel.add(usernameField);
            panel.add(passwordLabel);
            panel.add(passwordField);

            JButton registerButton = new JButton("Register");
            registerButton.addActionListener(e -> register());
            panel.add(registerButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new choiceMenu();
                }
            }
            );

            panel.add(goBackButton);

            add(panel);
            setVisible(true);
        }

        private void register(){
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("") || password.equals("")) {
                String errorString = "ERROR : Please fill out both fields.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Database.select("users", "name", username)!= null) {
                String errorString = "ERROR : Username unavailable, please try again.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (username.trim().equals("") || password.trim().equals("")) {
                String errorString = "ERROR : Username and/or password fields must be filled in. Please try again.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!username.matches("[a-zA-Z0-9]+") || !password.matches("[a-zA-Z0-9]+")) {
                String errorString = "ERROR : Username or password used illegal character. Please try again without a space.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                //insert into database
                Database.insert("users", username, password);
                String successMessage = (username + " successfully added to registry!") ;
                JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close window and go to user menu
                currentUser = username;
                dispose();
                new enterWindow.mainView();
            }
        }
    }
    public static class userLoginWindow extends JFrame {
        public userLoginWindow() {
            super("Deez Nutz Inc - User Login");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
            goBackButton.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                       dispose();
                       new choiceMenu();
                   }
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
            Object[] options = {"Room Browser", "Update account information","Logout"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Welcome " + currentUser + "!\nPlease select from the following options:",
                    "Deez Nutz Inc - Main View.",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
            //display list of options
            switch (choice){
                case 0:
                    // Create room option selected
                    new chatroomList(Database.getAllChatroomNames(),currentUser);
                    break;
                case 1:
                    // Change username & password option selected
                    new enterWindow.accountView();
                    break;
                case 2:
                    // Logout option selected
                    // CLEAR CURRENT USER
                    currentUser = "";
                    new choiceMenu();
                    break;
            }



        }
    }
    public static class accountView extends JFrame{
    public accountView() {
        Object[] options = {"Change Username", "Change Password", "Back To Main View"};
        int choice = JOptionPane.showOptionDialog(null,
                "Please select from the following options:",
                "Deez Nutz Inc - Account Management",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        pack();
        //display list of options
        switch (choice) {
            case 0:
                // Change username
                new usernameChangeWindow();
                break;
            case 1:
                // Change password
                new passwordChangeWindow();
                break;
            case 2:
                //Go Back
                new enterWindow.mainView();
                break;
        }
    }
}
}
