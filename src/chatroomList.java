
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
public class chatroomList extends JFrame {

    public static JList<String> chatroomList;

    public static JList<String> onlineList;

    public static chatWindow currentRoomView;

    public static JFrame currentWindow;
    // list of chatrooms to display

    public chatroomList(String currentUser) {
        currentWindow = this;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (currentRoomView != null) {
                    currentRoomView.exit(currentUser);
                    currentRoomView.dispose();
                }
                //exit
                System.exit(0);
            }
        });

        onlineList = new JList<>(new DefaultListModel<>());
        JScrollPane userPane = new JScrollPane(onlineList);
        chatroomList = new JList<>(new DefaultListModel<>());
        JScrollPane chatPane = new JScrollPane(chatroomList);
        chatroomList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected item from the list
                    String selected = chatroomList.getSelectedValue();

                    // Update the label based on the selection
                    if (selected != null) {
                        String [] usersList = Database.printActiveUsers(chatroomList.getSelectedValue()).toArray(new String[0]);

                        for (int i = 0; i < usersList.length; ++i){
                            if (usersList[i].equals(currentUser)){
                                usersList[i] += " (me)";
                            }
                        }

                        onlineList.setListData(usersList);

                    }
                }


            }
        });

        //Button and panel grid
        JPanel windowSouth = new JPanel(new GridLayout(1, 3, 10, 20));
        JPanel windowNorth = new JPanel(new GridLayout(2, 2, 10, 20));
        JPanel windowCenter = new JPanel(new GridLayout(1,2,10,10));

        //Button to allow the user to join the selected chatroom
        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(e -> {
            String selectedChatroom = chatroomList.getSelectedValue();
            if (selectedChatroom != null) {
                System.out.println("DEBUG Joining");
                if (currentRoomView != null) {
                    currentRoomView.exit(currentUser);
                    currentRoomView.dispose();
                }
                Database.deleteUser("users_chatroom", currentUser);
                Database.insertUserToChatroom("users_chatroom", currentUser, selectedChatroom);
                refresh();
                currentRoomView = new chatWindow(selectedChatroom, currentUser);
            }
        });

        //Button to delete selected room
        //WON'T DELETE IF USERS INSIDE
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            String selectedChatroom = chatroomList.getSelectedValue();
            if (selectedChatroom != null) {
                if (Database.select("users_chatroom", "chatname", selectedChatroom) != null) {
                    String errorString = "ERROR: Cannot delete chatroom user is currently in";
                    JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Database.deleteRoomReference("users_chatroom", selectedChatroom);
                    Database.deleteRoomReference("users_messages", selectedChatroom);
                    Database.deleteRoom(selectedChatroom);
                    refresh();
                }
            }
        });

        JButton refreshButton = new JButton("Refresh List");
        refreshButton.addActionListener(e -> refresh());

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   if (currentRoomView != null) {
                       currentRoomView.exit(currentUser);
                       currentRoomView.dispose();
                   }
                   dispose();
                   new enterWindow.mainView();
               }
           }
        );

        JButton createButton = new JButton("Create New Room");
        createButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   new chatWindow.ChatroomCreater(currentUser);
                   refresh();
               }
           }
        );
        JLabel userLabel = new JLabel("Users Inside:");
        JLabel roomLabel = new JLabel("Rooms:");

        windowSouth.add(joinButton);
        windowSouth.add(createButton);
        windowSouth.add(deleteButton);


        windowNorth.add(roomLabel);
        windowNorth.add(userLabel);
        windowNorth.add(refreshButton);
        windowNorth.add(goBackButton);

//        JPanel userLayout = new JPanel();
//        userLayout.add(userLabel, BorderLayout.PAGE_START);
//        userLayout.add(userPane, BorderLayout.PAGE_END);

//        JPanel chatroomLayout = new JPanel();
//        chatroomLayout.add(roomLabel, BorderLayout.PAGE_START);
//        chatroomLayout.add(chatPane, BorderLayout.PAGE_END);
        windowCenter.add(chatPane);
        windowCenter.add(userPane);

        add(windowCenter,BorderLayout.CENTER);
        add(windowSouth, BorderLayout.NORTH);
        add(windowNorth, BorderLayout.SOUTH);

        // Display the JFrame
        refresh();
        setVisible(true);
    }

    public static void refresh() {
        chatroomList.setListData(Database.getAllChatroomNames().toArray(new String[0]));
        chatroomList.setCellRenderer(new ChatroomListRenderer());
        System.out.println("DEBUG: Finished Refreshing");

        int onlineNum = 0;
        String userNum;
        //TODO INCORPORATE ALT METHOD USING AN ADDITIONAL TABLE IN THE DATABASE LISTING WHICH USERS ARE ONLINE
//        for (String user : Database.printOnlineUsers()){
//            onlineNum += 1;
//        }
        for (String chatroom: Database.getAllChatroomNames()){
            onlineNum += Database.printActiveUsers(chatroom).size();
        }

        switch (onlineNum) {
            case (0) : {
                userNum = "No one in a room :(";
                break;
            }
            case (1) : {
                userNum = "[" + onlineNum + "] user in a room";
                break;
            }
            default: {
                userNum = "[" + onlineNum + "] users in a room";
                break;
            }
        }

        currentWindow.setTitle("Room Browser - " + userNum);

    }

    private static class ChatroomListRenderer extends DefaultListCellRenderer {

        int[] userCounts;

        public ChatroomListRenderer() {
            userCounts = new int[Database.getAllChatroomNames().size()];

            for (int i = 0; i < Database.getAllChatroomNames().size(); ++i) {
                userCounts[i] = Database.printActiveUsers(Database.getAllChatroomNames().get(i)).size();
            }

            this.userCounts = userCounts;
        }

        public Component getListCellRendererComponent(JList<?> list, Object value, int i, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, i, isSelected, cellHasFocus);


            if (userCounts[i] == 0){
                label.setText(value.toString());
            }
            else if (userCounts[i] != 1) {
                label.setText(value.toString() + " (" + userCounts[i] + " online users)");
            }
            else {
                label.setText(value.toString() + " (" + userCounts[i] + " online user)");
            }
            return label;

        }
    }
}