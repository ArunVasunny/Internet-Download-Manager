import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class AddUrlController {
    @FXML
    private TextField textField;
    @FXML
    private TextField pathField;
    private dmController dmController;
    private Stage stage;
    public String defaultDownloadPath = "C:" + File.separator + "Users" + File.separator + "arunv" + File.separator + "Downloads"; //Default download path raka hu
    

    public void setDmController(dmController dmController) {
        this.dmController = dmController;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    @FXML
    void initialize() 
    {
        location.setDownloadPath(defaultDownloadPath);
        pathField.setText(defaultDownloadPath);
    }

    @FXML
    void startDownload(ActionEvent event) 
    {
        String url = textField.getText().trim();
        String path = pathField.getText().trim();

        if (path.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Path Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a download path before downloading.");
            alert.showAndWait();
            return; // Exit the method if no path is selected
        }

        if(url.isEmpty())
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Empty URL");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a URL before downloading.");
            alert.showAndWait();
        }
        else{
            dmController.downloadButtonClicked(event);
            textField.setText("");
            pathField.setText("");
            // stage.close();

        }    
    }

    @FXML
    void browsePath(ActionEvent event)
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Download File Location");
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {

            String path = selectedDirectory.getAbsolutePath();
            location.setDownloadPath(path);
            pathField.setText(path);

        } 
        else 
        {
            System.out.println("No directory selected");
        }
    }


    public String getUrl()
    {
        String url = textField.getText().trim();
        return url;
    }
}
