import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class dmController implements Initializable{
    
    @FXML
    private TableView<FileInfo> tableView;

    @FXML
    private Button pauseButton;
    @FXML
    private Button urlButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button browserButton;

    @FXML
    private MenuItem addUrlMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private MenuItem disclaimerMenuItem;

    private DownloadThread currentDownloadThread;
    private AddUrlController urlController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo,String> filename = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(0);
        filename.setCellValueFactory(p -> {
            return p.getValue().nameProperty();
        });

        TableColumn<FileInfo,String> fileurl = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(1);
        fileurl.setCellValueFactory(p -> {
            return p.getValue().urlProperty();
        });

        TableColumn<FileInfo,String> status = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(2);
        status.setCellValueFactory(p -> {
            return p.getValue().statusProperty();
        });

        TableColumn<FileInfo, String> size = (TableColumn<FileInfo, String>)this.tableView.getColumns().get(3);
        size.setCellValueFactory(p->{
            SimpleStringProperty simppleStringProperty = new SimpleStringProperty();
            simppleStringProperty.set(p.getValue().getSize());
            return simppleStringProperty;
        });

        TableColumn<FileInfo,String> percentage = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(4);
        percentage.setCellValueFactory(p -> {
            //To display symbol
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
            simpleStringProperty.set(p.getValue().getPercent() + " %");
            return simpleStringProperty;
        });

        TableColumn<FileInfo,String> speed = (TableColumn<FileInfo,String>)this.tableView.getColumns().get(5);
        speed.setCellValueFactory(p -> {
            return p.getValue().speedProperty();
        });

        List<FileInfo> data = ConnectionClass.getAllFiles();
        tableView.getItems().addAll(data);

        tableView.setStyle("-fx-focus-color: transparent");

        setButtonImage(urlButton, "/Images/link.png", 22.0, 22.0);
        setButtonImage(pauseButton, "/Images/pause.png", 22.0, 22.0);
        setButtonImage(resumeButton, "/Images/play.png", 22.0, 22.0);
        setButtonImage(deleteButton, "/Images/delete.png", 22.0, 22.0);
        setButtonImage(browserButton, "/Images/internet.png", 22.0, 22.0);

        

    }
    
    @FXML
    void downloadButtonClicked(ActionEvent event) {

        String url = urlController.getUrl();
        String filename = url.substring(url.lastIndexOf("/")+1);
        String status = "STARTING";
        String size = getSizeFromURL(url);
        String path = location.getDownloadPath() + File.separator+filename; //Path method
        String speed = "Calculating ...";
        FileInfo file = new FileInfo(filename, url, status, size, path,"0", speed); 
        DownloadThread thread = new DownloadThread(file, this);
        file.setDownloadThread(thread);
        this.tableView.getItems().add(file);
        ConnectionClass.insert(file);
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
            scene.getStylesheets().add(getClass().getResource("/CSS/web.css").toExternalForm());
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
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Are you sure you want to delete the file?");
            confirm.setContentText("This action cannot be undone.");
            if(confirm.showAndWait().get() == ButtonType.OK)
            {
                tableView.getItems().remove(selectedFile);
                File fileToDelete = new File(selectedFile.getPath());
                
                System.out.println(fileToDelete);
    
                if(fileToDelete.exists())
                {
                    DownloadThread downloadThread = selectedFile.getDownloadThread();
                    if (downloadThread != null) {
                        downloadThread.closeFile();
                    }
                    try
                    {
                        if(fileToDelete.delete())
                        {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("File Deleted");
                            alert.setHeaderText(null);
                            alert.setContentText("The selected file has been deleted successfully");
                            alert.showAndWait();
                            ConnectionClass.delete(selectedFile);
                        }
                        else
                        {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Deletetion Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Failed to delete the file");
                            alert.showAndWait();
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Deletion Error");
                        alert.setHeaderText(null);
                        alert.setContentText("An error occurred while deleting the file: " + ex.getMessage());
                        alert.showAndWait();
                    }
                }
                else
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Deletion Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The file does not exist at the specified path: " + fileToDelete.getAbsolutePath());
                    alert.showAndWait();
                    ConnectionClass.delete(selectedFile);
                }
            }

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
                ConnectionClass.update(selectedFile); //Updates file when download is paused 
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
        metaFile.setStatus(metaFile.getStatus());
        DecimalFormat decimcalFormat = new DecimalFormat("0.0");
        metaFile.setPercent(decimcalFormat.format(Double.parseDouble(metaFile.getPercent())));
        this.tableView.refresh();
        // ConnectionClass.update(metaFile);
        // System.out.println(metaFile);
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
            return "File size not available";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

        //Menu item event handlers
    @FXML
    void onAboutClicked(ActionEvent event) {
        // Display information about the download manager
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Download Manager is developed by Arun Vasunny.\nThis download manager allows you to easily download files from the internet, it is a tool for managing and monitoring downloads.");
        alert.showAndWait();
    }

    @FXML
    void onAddUrlClicked(ActionEvent event) {
        onUrl(event);
    }
    
    @FXML
    void onDisclaimerClicked(ActionEvent event) {
        // Display a disclaimer message regarding piracy
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Disclaimer");
        // alert.setHeaderText("Disclaimer Regarding Piracy");
        alert.setContentText("We do not support or condone piracy.\nDownloading and distributing copyrighted material without authorization is illegal, and we do not condone or support such activities.\nWe are not responsible for any misuse of this software.\nPlease use this download manager responsibly.");
        alert.showAndWait();
    }

    public void setButtonImage(Button button, String imgPath, Double width, Double height)
    {
        ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
        imgView.setFitHeight(height);
        imgView.setFitWidth(width);
        button.setGraphic(imgView);
        button.setStyle("-fx-background-color: transparent; -fx-border-width: 1.5px; -fx-border-radius: 80px; -fx-border-color: white; -fx-text-fill: white");
    }

}
