import javax.swing.*;

public class mainGUIRuntime {

    public static void main(String[] args) {
        try{
//            Database.connect("usersdb");
            Database.connectOnline();
            JFrame firstWindow = new JFrame();
            firstWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int dialogResult = JOptionPane.showConfirmDialog(firstWindow, "<html><font color='red'>DEBUG WARNING: Do you want to reset the internal database?</font></html>", "Confirmation", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                Database.deleteUsersChatroom();
                Database.deleteUserMessages();
                Database.deleteChatroom();
                Database.deleteUsers();

                Database.createUsers();
                Database.createChatroom();
                Database.createUserMessages();
                Database.createUsersChatroom();
            }
        }
        catch(Exception e) {
            String errorString = "Cannot connect to and/or modify local PSQL Server, please check your connection";
            JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        finally {
            new choiceMenu();
        }

    }
}
