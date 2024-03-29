import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectionClass {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "filehistory";
        String uname = "root";
        String pass  = "1234";
        try {
            
            Connection con = DriverManager.getConnection(url + dbName, uname, pass);

            String insertDownload = "Create database " + dbName + ";";

            Statement stm = con.createStatement();
            stm.execute(insertDownload);
            System.out.println("DB created");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
