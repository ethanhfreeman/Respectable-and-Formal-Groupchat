import javax.swing.*;
public class choiceMenu {
    public void newMenu(){
        Object[] options = {"Register", "Login", "Quit"};
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
                // Register option selected
                new enterWindow.userRegisterWindow();
                break;
            case 1:
                // Login option selected
                new enterWindow.userLoginWindow();
                break;
            case 2:
                // Quit option selected
                System.exit(0);
                break;
        }


    }
}