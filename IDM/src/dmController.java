import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class dmController implements Initializable{
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private Button downloadButton;
    
    @FXML
    private TextField urlTextField;

    public int index = 0;
    
    @FXML
    void downloadButtonClicked(ActionEvent event) {
        
        String url = urlTextField.getText().trim();
        String filename = url.substring(url.lastIndexOf("/")+1);
        // System.out.println(filename);
        String status = "STARTING";
        String action = "OPEN";
        String path = location.DOWNLOAD_PATH + File.separator+filename;
        FileInfo file = new FileInfo((index+1), filename, url, status, action, path);
        DownloadThread thread = new DownloadThread(file, this);
        thread.run();
        System.out.println("File Downloaded Successfull6y");
        
    }

    public void updateUI(FileInfo metaFile)
    {
        System.out.println(metaFile);
    }
    
}
