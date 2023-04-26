import org.postgresql.jdbc.SslMode;

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

        setTitle("Chatroom List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        //List to display the chatrooms
        chatroomList = new JList<>(chatrooms.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(chatroomList);
        add(scrollPane, BorderLayout.CENTER);

        //Button to allow the user to join the selected chatroom
        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(e -> {
            String selectedChatroom = chatroomList.getSelectedValue();
            if (selectedChatroom != null) {
                Database.loaded = 0;
                System.out.println("Joining");
                Database.insertUserToChatroom("users_chatroom", currentUser, selectedChatroom);
                new chatWindow(selectedChatroom, currentUser);


            }
        });

        JButton createButton = new JButton("Create Room");
        createButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   Database.loaded = 0;
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