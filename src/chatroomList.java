import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
public class chatroomList extends JFrame {
    private String currentUser;

    public static JList<String> chatroomList;
    // list of chatrooms to display

    public chatroomList(ArrayList<String> chatrooms, String currentUser) {
        // Set up the JFrame
        setTitle("Chatroom List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Create a JList to display the chatrooms
        chatroomList = new JList<>(chatrooms.toArray(new String[0]));

        // Add the JList to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(chatroomList);

        // Add the JScrollPane to the center of the JFrame
        add(scrollPane, BorderLayout.CENTER);

        // Add a button to allow the user to join the selected chatroom
        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(e -> {
            String selectedChatroom = chatroomList.getSelectedValue();
            if (selectedChatroom != null) {
                chatWindow.currentKnownMessages = 0;
                new chatWindow(selectedChatroom, currentUser);

            }
        });

        JButton createButton = new JButton("Create Room");
        createButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   chatWindow.currentKnownMessages = 0;
                   new chatWindow.ChatroomCreater(currentUser);
                   refresh();
               }
           }
        );

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   dispose();
                   new enterWindow.mainView();
               }
           }
        );
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refresh());


        // Add the button to the bottom of the JFrame
        add(joinButton, BorderLayout.EAST);
        add(createButton, BorderLayout.WEST);
        add(goBackButton, BorderLayout.NORTH);
        add(refreshButton, BorderLayout.SOUTH);

        // Display the JFrame
        setVisible(true);
    }

    public static void refresh(){
            chatroomList.setListData(Database.getAllChatroomNames().toArray(new String[0]));
        }
    }