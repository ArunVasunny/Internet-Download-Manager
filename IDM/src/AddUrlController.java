import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class AddUrlController {
    @FXML
    private TextField textField;
    private dmController dmController;

    public void setDmController(dmController dmController) {
        this.dmController = dmController;
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
        }    
    }

    public String getUrl()
    {
        String url = textField.getText().trim();
        return url;
    }
}
