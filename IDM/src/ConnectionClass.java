import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectionClass{

    public static final String url = "jdbc:mysql://localhost:3306/";
    public static final String dbName = "filehistory";
    public static final String uname = "root";
    public static final String pass  = "1234";

    public static void insert(FileInfo file)
    {
        try 
        {
            Connection connection = DriverManager.getConnection(url+dbName, uname, pass);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO downloads (fileName, fileUrl, fileStatus, fileSize, filePercent, fileSpeed) VALUES(?,?,?,?,?,?)");
            statement.setString(1, file.getName());
            statement.setString(2, file.getUrl());
            statement.setString(3, file.getStatus());
            statement.setString(4, file.getSize());
            statement.setString(5, file.getPercent());
            statement.setString(6, file.getSpeed());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully");
            }
            statement.close();
            connection.close();

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Updates the Info after Download gets completed
    public static void update(FileInfo file)
    {
        try {
            Connection connection = DriverManager.getConnection(url + dbName, uname, pass);
            PreparedStatement statement = connection.prepareStatement("UPDATE downloads SET fileStatus=?, filePercent=?, fileSpeed=? WHERE fileName=?");
            statement.setString(1, file.getStatus());
            statement.setString(2, file.getPercent());
            statement.setString(3, file.getSpeed());
            statement.setString(4, file.getName());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully");
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(FileInfo file)
    {
        try {
            Connection connection = DriverManager.getConnection(url + dbName, uname, pass);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM downloads WHERE fileName=?");
            statement.setString(1, file.getName());
    
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data deleted successfully");
            } else {
                System.out.println("No data deleted");
            }
            
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<FileInfo> getAllFiles() {
        List<FileInfo> fileList = new ArrayList<>();
        try {
                Connection connection = DriverManager.getConnection(url + dbName, uname, pass);
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM downloads");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String fileName = resultSet.getString("fileName");
                    String fileUrl = resultSet.getString("fileUrl");
                    String fileStatus = resultSet.getString("fileStatus");
                    String fileSize = resultSet.getString("fileSize");
                    String filePercent = resultSet.getString("filePercent");
                    String fileSpeed = resultSet.getString("fileSpeed");
                    FileInfo fileInfo = new FileInfo(fileName, fileUrl, fileStatus, fileSize, "",filePercent, fileSpeed);
                    fileList.add(fileInfo);
                }
                resultSet.close();
                statement.close();
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

}
