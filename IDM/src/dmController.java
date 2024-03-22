import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class dmController implements Initializable{
    


    @FXML
    private TableView<FileInfo> tableView;

    @FXML
    private Button downloadButton;
    
    @FXML
    private TextField urlTextField;

    public int index = 0;

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

        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>)this.tableView.getColumns().get(4);
        action.setCellValueFactory(p->{
            return p.getValue().actionProperty();
        });

    }
    
    @FXML
    void downloadButtonClicked(ActionEvent event) {

        String url = urlTextField.getText().trim();
        String filename = url.substring(url.lastIndexOf("/")+1);
        // System.out.println(filename);
        String status = "STARTING";
        String action = "OPEN";
        String path = location.DOWNLOAD_PATH + File.separator+filename;
        FileInfo file = new FileInfo((index+1) +" ", filename, url, status, action, path);
        DownloadThread thread = new DownloadThread(file, this);
        thread.run();
        this.tableView.getItems().setAll(file);
        System.out.println("File Downloaded Successfull6y");
        
    }

    public void updateUI(FileInfo metaFile)
    {
        System.out.println(metaFile);
    }
    
}
