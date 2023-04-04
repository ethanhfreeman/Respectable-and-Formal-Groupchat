
public class Main {
    public static void main(String[] args) throws Exception {


        Database.connect("postgres");
        Database.select("company");
    }
}