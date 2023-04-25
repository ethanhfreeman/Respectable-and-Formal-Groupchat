import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class chatWindow extends JFrame {

    String currentChatroom;
    public static int currentKnownMessages;
    String currentUser;
    private JTextArea messageArea;
    private JTextField inputField;

    public static class ChatroomCreater extends JFrame {
        private final String currentUser;
        private static JTextField nameField;

        public ChatroomCreater(String incomingUser) {
            super("Deez Nutz Inc - Chatroom Creater");
            currentUser = incomingUser;
            setSize(400, 125);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            JLabel nameLabel = new JLabel("Enter a Unique Chatroom Name:");
            nameField = new JTextField(30);

            panel.add(nameLabel);
            panel.add(nameField);

            JButton addButton = new JButton("Add");
            addButton.addActionListener(e -> addRoom());
            panel.add(addButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(new ActionListener() {
                                               public void actionPerformed(ActionEvent e) {
                                                   dispose();
                                               }
                                           }
            );
            panel.add(goBackButton);

            add(panel);
            setVisible(true);

        }

        private void addRoom() {
            String chatroomName = nameField.getText();

            if (chatroomName.equals("")) {
                String errorString = "ERROR : Please fill out the field.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!chatroomName.matches("[a-zA-Z0-9]+")) {
                String errorString = "ERROR: Please, no weird symbols. Try another name.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Database.select("chatroom", "name", chatroomName) != null) {
                String errorString = "ERROR: Chatroom name already exists. Please try another name.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Database.insertChatroom("chatroom", chatroomName);
            Database.insertUserToChatroom("users_chatroom", currentUser, chatroomName);
            String successMessage = "Success! Welcome to " + chatroomName + "!";
            JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            chatroomList.refresh();
            new chatWindow(chatroomName, currentUser);

        }

    }

    public chatWindow(String currentChatroom, String currentUser) {


        super(currentChatroom + " Chat Window");

        this.currentKnownMessages = 0;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.currentChatroom = currentChatroom;
        //text area to display messages
        messageArea = new JTextArea(10, 30);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // input field for user to enter messages
        inputField = new JTextField(30);
        inputField.addActionListener(e -> send(currentUser));
        add(inputField, BorderLayout.LINE_END);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> reset());
        add(resetButton, BorderLayout.SOUTH);

        init();
        // Create a new timer with an interval of 1 second
        Timer timer = new Timer(500, e -> {
            // refresh
            activate();
        });

        // Start the timer
        timer.start();


        pack();
        setVisible(true);
    }

    public void send(String currentUser) {
        // Get text from input field and add to message area
        String message = inputField.getText();
        inputField.setText("");

//        messageArea.append(currentUser + ": " + message + "\n");
        Database.insertMessage("users_messages", currentUser, message, currentChatroom);
        activate();
    }

    public void init() {
        for (String message : Database.getMessages(currentChatroom)) {
            messageArea.append(message + "\n");
        }

    }

    public void activate() {
        for (String message : Database.printNewMessages(currentChatroom)) {
            messageArea.append(message + "\n");
        }

    }

    public void reset() {
         chatWindow.currentKnownMessages = 0;
    }

}