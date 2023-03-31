public class Main {
    public static void main(String[] args) throws Exception {
        Database.Connect();
        Database.select("company");
    }
}