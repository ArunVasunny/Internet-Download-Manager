import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddUrlController {
    @FXML
    private TextField textField;
    @FXML
    private TextField pathField;
    @FXML
    private Button downloadButton;
    @FXML
    private Button pathButton;

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

        setButtonImage(downloadButton, "/Images/download.png", 22.0, 22.0);
        setButtonImage(pathButton, "/Images/path.png", 22.0, 22.0);

        // textField.setStyle("-fx-focus-color: transparent");
        textField.setFocusTraversable(false);
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
            stage.close(); // To close url window after User downloads a file

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

    public void setButtonImage(Button button, String imgPath, Double width, Double height)
    {
        ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
        imgView.setFitHeight(height);
        imgView.setFitWidth(width);
        button.setGraphic(imgView);
        button.setStyle("-fx-background-color: transparent; -fx-border-width: 1.5px; -fx-border-radius: 80px; -fx-border-color: white; -fx-text-fill: white");
    }
}
