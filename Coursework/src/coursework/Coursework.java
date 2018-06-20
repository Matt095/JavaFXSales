package coursework;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Coursework extends Application {    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Coursework.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Coursework.class.getResource("Style.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Coursework");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }    
}
