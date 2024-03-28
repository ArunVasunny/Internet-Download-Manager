import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class WebController implements Initializable{
    
    @FXML 
    public WebView myWebView;

    @FXML 
    public TextField textField;

    @FXML
    public WebEngine webEngine;
    public WebHistory history;
    public double webZoom,currentZoom;

    @FXML
    public Button backButton;
    public Button nextButton;
    public Button refreshButton;
    public Button searchButton;
    public Button zoomInButton;
    public Button zoomOutButton;

    @FXML
    public ProgressBar loadingBar;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        webEngine = myWebView.getEngine();
        history = webEngine.getHistory();
        webEngine.load(getClass().getClassLoader().getResource("file.html").toExternalForm());

        setButtonImage(backButton, "/Images/back.png", 22.0, 22.0);
        setButtonImage(nextButton, "/Images/next.png", 22.0, 22.0);
        setButtonImage(refreshButton, "/Images/refresh.png", 22.0, 22.0);
        setButtonImage(searchButton,"/Images/search.png", 22.0, 22.0);
        setButtonImage(zoomInButton, "/Images/in.png", 22.0, 22.0);
        setButtonImage(zoomOutButton, "/Images/out.png", 22.0, 22.0);

    }

    public void setButtonImage(Button button, String imgPath, Double width, Double height)
    {
        ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
        imgView.setFitHeight(height);
        imgView.setFitWidth(width);
        button.setGraphic(imgView);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 50%; -fx-border-color: white;");
    }

    public void loadPage()
    {
        loadingProgress();
        String url = ("https://www."+textField.getText()+".com");
        System.out.println(url);
        webEngine.load(url);
    }

    //display loading progress of a website
    public void loadingProgress()
    {
        loadingBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
        BooleanBinding isRunning = webEngine.getLoadWorker().stateProperty().isEqualTo(Worker.State.RUNNING);
        loadingBar.visibleProperty().bind(isRunning);
    }

    public void refreshPage()
    {
        webEngine.reload();
    }

    public void zoomIn()
    {
        currentZoom = myWebView.getZoom();
        webZoom = currentZoom + 0.1;
        myWebView.setZoom(webZoom);
    }

    public void zoomOut()
    {
        currentZoom = myWebView.getZoom();
        webZoom = currentZoom - 0.1;
        myWebView.setZoom(webZoom);
    }

    public void displayHistory()
    {
        history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        for (WebHistory.Entry entry : entries) {
            System.out.println(entry.getUrl()+" "+entry.getLastVisitedDate());
        }
    }

    public void back()
    {
        history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        if (currentIndex > 0) {
            history.go(-1);
        } else {
            System.out.println("No previous page in history.");
        }

        textField.setText(entries.get(history.getCurrentIndex()).getUrl());

    }
    
    public void next()
    {
        history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        if (currentIndex < entries.size() - 1) {
            history.go(1);
        } else {
            System.out.println("No next page in history.");
        }

        textField.setText(entries.get(history.getCurrentIndex()).getUrl());
        
    }
    
    //Textfield Gets executed when enter pressed
    public void onEnter(ActionEvent event){
        loadPage();
    }
}
