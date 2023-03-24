public class User {

    private String uniqueID;
    private String password;
    private boolean onlineStatus;
    
    public boolean isOnlineStatus() {
        return onlineStatus;
    }
    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
    public String getUniqueID() {
        return uniqueID;
    }
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        System.out.println("Please set a real password");
        if (password == null){
            System.out.println("Not a real password");
            return;
        }
        this.password = password;
    }
    
}
