public class localHostInfo {
    private static String localUserName = postgres;
    private static String localPassword = 1234;
    //set your local username and password here for your computer
    //updates to this file, by default, will not upload to GitHub
    public static String getLocalUserName() {
        return localUserName;
    }

    public static String getLocalPassword() {
        return localPassword;
    }
}
