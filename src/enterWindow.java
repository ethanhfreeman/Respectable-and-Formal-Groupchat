import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class enterWindow extends JFrame{
    static String currentUser = "";

    private static JTextField usernameField;
    private static JPasswordField passwordField;
    public static class userRegisterWindow extends JFrame {

        public userRegisterWindow() {
            super("User Registration");
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
                    new choiceMenu().newMenu();
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
            } else if (username.contains(" ") || password.contains(" ")) {
                String errorString = "ERROR : Username or password used illegal character. Please try again without a space.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                //insert into database
                Database.insert("users", username, password);
                String successMessage = (username + " successfully added to registry!") ;
                JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close window and login after user register
                dispose();
                new enterWindow.userLoginWindow();
            }
        }
    }
    public static class userLoginWindow extends JFrame {
        public userLoginWindow() {
            super("User Login");
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
                       new choiceMenu().newMenu();
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

            if (Database.select("users", "name", username)!= null) {
                JOptionPane.showMessageDialog(null, "DEBUG MESSAGE: User Found in DB", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (password.equals(Database.selectPassword("users", "name", username).toString().trim())) {
                    String successMessage = ("Welcome " + username + "!") ;
                    JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                    //SET LOGGED IN USER AS CURRENT USER
                    currentUser = username;
                    //TODO go to main view
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
            Object[] options = {"Join a room", "Create a room", "Update account information","Logout"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Please select from the following options:",
                    "Deez Nutz inc ChatRoom.",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
            //display list of options
            switch (choice){
                case 0:
                    // Join room option selected
                    //TODO: Join Room with given room name (kinda same as register user), find way to transition into chatWindow
                    break;
                case 1:
                    // Create room option selected
                    //TODO: Create Room with given room name (kinda same as register user)
                    break;
                case 2:
                    // Change username & password option selected
                    //TODO manage acc info (change username & password)
                    break;
                case 3:
                    // Logout option selected
                    // CLEAR CURRENT USER
                    currentUser = "";
                    new choiceMenu().newMenu();
                    break;
            }



        }
    }
}
