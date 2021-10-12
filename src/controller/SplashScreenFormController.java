package controller;

import animatefx.animation.Bounce;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenFormController implements Initializable {

    public AnchorPane splashPane;
    public Circle circle1;
    public Circle circle2;
    public Circle circle3;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new Bounce(circle1).setCycleCount(5).setDelay(Duration.valueOf("100ms")).play();
        new Bounce(circle2).setCycleCount(5).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(circle3).setCycleCount(5).setDelay(Duration.valueOf("1100ms")).play();

        new SplashScreen().start();

    }

    class SplashScreen extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(6200);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new Scene(root);
                        scene.setFill(Color.TRANSPARENT);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.initStyle(StageStyle.TRANSPARENT); //Use For Boarder TRANSPARENT
                        stage.show();
                        splashPane.getScene().getWindow().hide();
                    }
                });


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
