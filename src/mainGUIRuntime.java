import javax.swing.*;

public class mainGUIRuntime {

    public static void main(String[] args) {
        try{
            Database.connect("usersdb");
        }
        catch(Exception e) {
            String errorString = "Cannot connect to local PSQL Server, please check your connection";
            JOptionPane.showMessageDialog(null, errorString, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        finally {
            new choiceMenu().newMenu();
        }

    }
}
