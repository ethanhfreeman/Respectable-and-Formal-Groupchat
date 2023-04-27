import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class chatWindow extends JFrame {

    String currentChatroom;
    String currentUser;

    Timer chatTimer;

    Clip chime;
    private JTextArea messageArea;
    private JTextField inputField;

    JList<String> list;

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

        private void addRoom(String incomingUser) {
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


        super("" + currentChatroom + " Chat Window");
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
        list = new JList<>(Database.printActiveUsers(currentChatroom).toArray(new String[0]));
        userPanel.add(list);
        add(userPanel, BorderLayout.NORTH);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> reset());

        //DEBUG
        //add(resetButton, BorderLayout.SOUTH);

        init();
        // Create a new timer with an interval of 1 second
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
    }


    public void send(String currentUser) {
        // Get text from input field and add to message area
        String message = inputField.getText();
        inputField.setText("");

//        messageArea.append(currentUser + ": " + message + "\n");
        if (message.equals("") || message.replaceAll("\\s", "").equals("")){
            messageArea.append("ERROR: Please send an actual message and not blank space.\n");
            return;
        }
        else {
            if (message.length() > 50) {
                messageArea.append("ERROR: Your message is too long, please shorten it.\n");
            }
            else if (message.charAt(0) == '/') {
                if (message.equals("/help")) {
                    messageArea.append("Valid chat commands include:\n     /help\n     /list\n     /history\n     /leave\n");
                } else if (message.equals("/list")) {
                    messageArea.append("1\n");
                } else if (message.equals("/history")) {
                    messageArea.append("2\n");
                } else if (message.equals("/leave")) {
                    exit(currentUser);
                } else {
                    messageArea.append("ERROR: Unknown command.\n Use /help for a list of commands.\n");
                }
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

    }

    public void activate() {
        for (String message : Database.printNewMessages(currentChatroom)) {
            //PLAY SOUND
            messageArea.append(message + "\n");
        }

        list.setListData(Database.printActiveUsers(currentChatroom).toArray(new String[0]));
        list.repaint();

    }

    public void reset() {
    }

}