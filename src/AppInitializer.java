import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL resource = getClass().getResource("view/SplashScreenForm.fxml");
        //URL resource = getClass().getResource("view/LoginForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        scene.getStylesheets().add("styles/Style.css");
        primaryStage.initStyle(StageStyle.TRANSPARENT); //Use For Boarder TRANSPARENT
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
