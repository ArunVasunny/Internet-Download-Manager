import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Fxml loading
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLS/dm.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/styles.css").toExternalForm());
        stage.getIcons().add(new Image("/Images/dmIcon.png"));

        stage.setTitle("Download Manager");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    
}
