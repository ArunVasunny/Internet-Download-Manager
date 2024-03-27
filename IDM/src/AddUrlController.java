import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class AddUrlController {
    @FXML
    private TextField textField;
    private dmController dmController;
    private Stage stage;

    public void setDmController(dmController dmController) {
        this.dmController = dmController;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    @FXML
    void startDownload(ActionEvent event) 
    {
        String url = textField.getText().trim();
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
            // stage.close();

        }    
    }


    public String getUrl()
    {
        String url = textField.getText().trim();
        return url;
    }
}
