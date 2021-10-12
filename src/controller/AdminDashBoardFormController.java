package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AdminDashBoardFormController {

    public ToggleGroup adminTogleBtn;
    public ToggleButton btnId;
    public StackPane rightStackPane;
    public ToggleButton addUserBtn;
    public StackPane adminStackPane;
    public ToggleButton addCMBtn;
    public ToggleButton btnRentId;

    public void initialize() {
        btnId.fire();
    }

    public void BtnO0nAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/DashBoardForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void addUserBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/ManageUsersForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void addCMBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/AddCategoryAndModelsForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void logOutBtnOnAction(ActionEvent actionEvent) throws IOException {
        /*Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage =(Stage) this.adminStackPane.getScene().getWindow();
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();*/
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.show();
        adminStackPane.getScene().getWindow().hide();
    }

    public void OrderItemManageBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/AddOrderItemForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);

    }

    public void RentItemManageBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/AddRentItemForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }
}