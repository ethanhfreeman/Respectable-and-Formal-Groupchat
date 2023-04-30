
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main{
    public static void main(String[] args) {
            new mainGUIRuntime.onlineOrLocalWindow();
    }
}

class mainGUIRuntime {
    mainGUIRuntime() {
            JFrame secondWindow = new JFrame();
            secondWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int dialogResult = JOptionPane.showConfirmDialog(secondWindow, "<html><font color='red'>DEBUG WARNING: Do you want to reset the internal database?</font></html>", "Confirmation", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                Database.deleteUsersOnline();
                Database.deleteUsersChatroom();
                Database.deleteUserMessages();
                Database.deleteChatroom();
                Database.deleteUsers();

                Database.createUsers();
                Database.createChatroom();
                Database.createUserMessages();
                Database.createUsersChatroom();
                Database.createUsersOnline();
            }
            new choiceMenu();
    }

    public static class onlineOrLocalWindow extends JFrame {
        public onlineOrLocalWindow()  {
            Object[] options = {"Connect to Online PSQL server (Available on ASU Internet)", "Connect to local PSQL server"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Select your destination",
                    "Deez Nutz Inc ChatRoom",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);


            switch (choice) {
                case 0 -> {
                    //user wants to connect online
                    try {
                        Database.connectOnline();
                    } catch (Exception e) {
                        String errorString = "Cannot connect to online PSQL Server, please check your connection";
                        JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                    new mainGUIRuntime();
                    dispose();
                }
                case 1 -> {
                    new connection();
                }
            }
        }
    }




    public static class connection extends JFrame {
        public connection() {
                super("Enter PSQL connection parameters");
                setSize(350, 275);
                JPanel urlPanel = new JPanel();
                JLabel urlLabel = new JLabel("Enter URL:");
                JTextField urlField = new JTextField(25);
                JLabel dbLabel = new JLabel("Enter database name");
                JTextField dbField = new JTextField(25);
                JLabel userLabel = new JLabel("Enter database username");
                JTextField userField = new JTextField(25);
                JLabel passwordLabel = new JLabel("Enter database password");
                JTextField passwordField = new JPasswordField(25);


            JButton connectButton = new JButton("Connect");
                connectButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Connect to the PSQL URL here
                        String url = urlField.getText();
                        String dbName = dbField.getText();
                        String user = userField.getText();
                        String password = passwordField.getText();

                        if(url.equals("") || dbName.equals("") || user.equals("") || password.equals("")){
                            String errorString = "Please fill in all fields";
                            JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        try {
                            Database.connectLocal(url,dbName, user, password);
                        } catch (Exception ex) {
                            String errorString = "Cannot connect, please check all fields";
                            JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        dispose();
                        new mainGUIRuntime();
                    }
                });
                JButton goBackButton = new JButton("Go Back");
                goBackButton.addActionListener(e -> {
                            dispose();
                                new onlineOrLocalWindow();
                        }
                );
                urlPanel.add(urlLabel);
                urlPanel.add(urlField);
                urlPanel.add(dbLabel);
                urlPanel.add(dbField);
                urlPanel.add(userLabel);
                urlPanel.add(userField);
                urlPanel.add(passwordLabel);
                urlPanel.add(passwordField);
                urlPanel.add(connectButton);
                urlPanel.add(goBackButton);
                add(urlPanel);
                setVisible(true);

            }
        }
    }
