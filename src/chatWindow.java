import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class chatWindow extends JFrame {

    String currentChatroom;
    String currentUser;

    Timer chatTimer;

    private final JTextArea messageArea;
    private final JTextField inputField;

    JList<String> userList;

    public static class ChatroomCreater extends JFrame {
        private static JTextField nameField;

        public ChatroomCreater(String incomingUser) {
            super("Deez Nutz Inc - Chatroom Creater");
            setSize(400, 125);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            JLabel nameLabel = new JLabel("Enter a Unique Chatroom Name:");
            nameField = new JTextField(30);

            panel.add(nameLabel);
            panel.add(nameField);

            JButton addButton = new JButton("Add");
            addButton.addActionListener(e -> addRoom(incomingUser));
            panel.add(addButton);

            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(e -> dispose()
            );
            panel.add(goBackButton);

            add(panel);
            setVisible(true);

        }

        private void addRoom(String incomingUser) {
            String chatroomName = nameField.getText();

            if (chatroomName.equals("")) {
                String errorString = "ERROR : Please fill out the field.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (chatroomName.length() > 30) {
                String errorString = "ERROR : Name cannot be greater than 30 characters.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!chatroomName.matches("[a-z0-9]+")) {
                String errorString = "ERROR: The chat room name must only contain lowercase letters and numbers.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Database.select("chatroom", "name", chatroomName) != null) {
                String errorString = "ERROR: Chatroom name already exists. Please try another name.";
                JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (chatroomList.currentRoomView != null) {
                chatroomList.currentRoomView.exit(incomingUser);
                chatroomList.currentRoomView.dispose();
            }
            Database.insertChatroom("chatroom", chatroomName);
            Database.deleteUser("users_chatroom", incomingUser);
            Database.insertUserToChatroom("users_chatroom", incomingUser, chatroomName);
            System.out.println("Joining");
            String successMessage = "Success! Welcome to " + chatroomName + "!";
            JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            chatroomList.refresh();
            chatroomList.currentRoomView = new chatWindow(chatroomName, incomingUser);

        }

    }

    public chatWindow(String currentChatroom, String currentUser) {


        setTitle(currentUser + " - " + currentChatroom + " - LOADING");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.currentChatroom = currentChatroom;
        this.currentUser = currentUser;

        Database.loaded = 0;
        //insert user when joining

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exit(currentUser);
            }
        });



        //text area to display messages
        messageArea = new JTextArea(10, 30);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // input field for user to enter messages
        inputField = new JTextField(30);
        inputField.addActionListener(e -> send(currentUser));
        add(inputField, BorderLayout.SOUTH);

        //list for user view
        JPanel userPanel = new JPanel();
        userList = new JList<>();
        userPanel.add(userList);
        add(userPanel, BorderLayout.NORTH);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> reset());

        //DEBUG
        //add(resetButton, BorderLayout.SOUTH);

        init();
        // New timer that will go every second
        chatTimer = new Timer(1000, e -> {
            // refresh
            activate();
        });

        // Start the timer
        chatTimer.start();
        setVisible(true);
    }

    public void exit(String currentUser){
        chatTimer.stop();
        Database.deleteUser("users_chatroom", currentUser);
        Database.loaded = 0;
        this.currentChatroom = "";
        this.currentUser = "";
        dispose();
        chatroomList.refresh();
    }


    public void send(String currentUser) {
        // Get text from input field and add to message area
        String message = inputField.getText();
        inputField.setText("");

//        messageArea.append(currentUser + ": " + message + "\n");
        if (message.equals("") || message.replaceAll("\\s", "").equals("")){
            messageArea.append("ERROR: Please send an actual message and not blank space.\n");
        }
        else {
            if (message.charAt(0) == '/') {
                switch (message) {
                    case "/help" -> {
                        messageArea.append("Valid chat commands include:\n/help\n/list\n/history\n/leave\n");

                    }
                    case "/list" -> {
                        messageArea.append("-----USERLIST-----\n");
                        for (String userName : Database.printActiveUsers(currentChatroom)) {
                            messageArea.append(userName + "\n");
                        }
                    }
                    case "/history" -> {
                        messageArea.append("-----HISTORY------\n");
                        for (String chatMessage : Database.getMessages(currentChatroom)) {
                            Database.loaded++;
                            messageArea.append(chatMessage + "\n");
                        }
                    }
                    case "/leave" -> {
                        exit(currentUser);
                    }
                    default -> {
                        messageArea.append("ERROR: " + message + " is NOT a valid command. Use /help for a list of commands.\n");
                    }
                }
            }
            else if (message.length() > 50) {
                messageArea.append("ERROR: Your message is too long, please shorten it.\n");
            }
            else {
                LocalTime currentTime = LocalTime.now();
                String time = currentTime.format(DateTimeFormatter.ofPattern("hh:mm "));
                String timeMessage = time + " " + message;
                Database.insertMessage("users_messages", currentUser, timeMessage, currentChatroom);
                activate();
            }
        }
    }

    public void init() {
        for (String message : Database.getMessages(currentChatroom)) {
            messageArea.append(message + "\n");
        }
        messageArea.append("----- [ Welcome To " + currentChatroom + ", " + currentUser + " ] -----\n");

    }

    public void activate() {
        for (String message : Database.printNewMessages(currentChatroom)) {
            //PLAY SOUND
            messageArea.append(message + "\n");
        }

        int activeUsers = Database.printActiveUsers(currentChatroom).size();
        String userNum;
        if(activeUsers != 1 ){
            userNum = "[" + activeUsers + "] users in room";
        }
        else {
            userNum = "[" + activeUsers + "] user in room";
        }
        this.setTitle(currentUser + " - " + currentChatroom + " - " + userNum);

        String [] usersList = Database.printActiveUsers(currentChatroom).toArray(new String[0]);

        for (int i = 0; i < usersList.length; i++){
            if (usersList[i].equals(currentUser)){
                usersList[i] += " (me)";
            }
        }
        userList.setListData(usersList);

    }

    public void reset() {
    }

}