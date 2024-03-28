import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class dmController implements Initializable{
    
    @FXML
    private TableView<FileInfo> tableView;

    @FXML
    private Button pauseButton;

    public int index = 0; //0
    private DownloadThread currentDownloadThread;
    private AddUrlController urlController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo,String> srno = (TableColumn<FileInfo,String>) this.tableView.getColumns().get(0);
        srno.setCellValueFactory(p -> {
            return p.getValue().indexProperty();
        });

        TableColumn<FileInfo,String> filename = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(1);
        filename.setCellValueFactory(p -> {
            return p.getValue().nameProperty();
        });

        TableColumn<FileInfo,String> fileurl = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(2);
        fileurl.setCellValueFactory(p -> {
            return p.getValue().urlProperty();
        });

        TableColumn<FileInfo,String> status = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(3);
        status.setCellValueFactory(p -> {
            return p.getValue().statusProperty();
        });

        TableColumn<FileInfo, String> size = (TableColumn<FileInfo, String>)this.tableView.getColumns().get(4);
        size.setCellValueFactory(p->{
            SimpleStringProperty simppleStringProperty = new SimpleStringProperty();
            simppleStringProperty.set(p.getValue().getSize());
            return simppleStringProperty;
        });

        TableColumn<FileInfo,String> percentage = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(5);
        percentage.setCellValueFactory(p -> {
            //To display symbol
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
            simpleStringProperty.set(p.getValue().getPercent() + " %");
            return simpleStringProperty;
        });

        TableColumn<FileInfo,String> speed = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(6);
        speed.setCellValueFactory(p -> {
            return p.getValue().speedProperty();
        });

    }
    
    @FXML
    void downloadButtonClicked(ActionEvent event) {

        String url = urlController.getUrl();
        String filename = url.substring(url.lastIndexOf("/")+1);
        String status = "STARTING";
        String size = getSizeFromURL(url);
        String path = location.getDownloadPath() + File.separator+filename; //Path method
        String speed = "Calculating ...";
        FileInfo file = new FileInfo((index+1) +"", filename, url, status, size, path,"0", speed); //1
        this.index = index + 1; //2
        DownloadThread thread = new DownloadThread(file, this);
        file.setDownloadThread(thread);
        this.tableView.getItems().add(Integer.parseInt(file.getIndex()) - 1,file); //2-1 = 1
        thread.start();
        System.out.println("File Downloaded Successfully");
        
    }

    @FXML
    void onBrowser(ActionEvent event)
    {
        try {

            Stage browserStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLS/webview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            browserStage.setTitle("NetPulse Browser");
            browserStage.getIcons().add(new Image("/Images/icon.png"));
            browserStage.setScene(scene);
            browserStage.show();
            browserStage.setResizable(false);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @FXML
    void onUrl(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLS/AddURL.fxml"));
            Parent root = loader.load();
            AddUrlController addUrlController = loader.getController(); // Get the controller instance
            addUrlController.setDmController(this); // Set the dmController instance in AddUrlController
            urlController = addUrlController; // Set the urlController field in dmController
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add URL");
            stage.show();
            
            urlController.setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onDelete(ActionEvent event)
    {
        FileInfo selectedFile = tableView.getSelectionModel().getSelectedItem();

        if(selectedFile != null)
        {
            tableView.getItems().remove(selectedFile);
        }
        else
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file to delete");
            alert.showAndWait();
        }
    }

    @FXML
    void pauseButtonClicked(ActionEvent event)
    {
        FileInfo selectedFile = tableView.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            currentDownloadThread = selectedFile.getDownloadThread();
            if (currentDownloadThread != null) {
                currentDownloadThread.pauseDownload();
            }
        }
        else
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file to pause");
            alert.showAndWait();
        }
    }

    @FXML
    void resumeButtonClicked(ActionEvent event)
    {
        FileInfo selectedFile = tableView.getSelectionModel().getSelectedItem();
        if(selectedFile != null)
        {
            currentDownloadThread = selectedFile.getDownloadThread();
            if(currentDownloadThread != null)
            {
                currentDownloadThread.resumeDownload();
            }
        }
        else
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file to resume");
            alert.showAndWait();
        }
    }

    public void updateUI(FileInfo metaFile)
    {
        FileInfo fileInfo = this.tableView.getItems().get(Integer.parseInt(metaFile.getIndex())-1); //1-1 = 0
        fileInfo.setStatus(metaFile.getStatus());
        DecimalFormat decimcalFormat = new DecimalFormat("0.0");
        fileInfo.setPercent(decimcalFormat.format(Double.parseDouble(metaFile.getPercent())));
        this.tableView.refresh();
        System.out.println(metaFile);
    }

    public String getSizeFromURL(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            int fileSize = connection.getContentLength();
            return formatFileSize(fileSize);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public String formatFileSize(long size)
    {
        if (size <= 0)
        {
            return "0 B";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
