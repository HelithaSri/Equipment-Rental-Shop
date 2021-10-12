package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UserDashBoardFormController {

    public StackPane adminStackPane;
    public StackPane rightStackPane;
    public ToggleButton btnOrderItemId;
    public ToggleButton btnDashBoard;

    public void initialize() {
        btnDashBoard.fire();
    }

    public void orderItemBrnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/UserOrdeItemForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void rentItemBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/UserRentItemForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void returnCheckBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/UserReturnCheckForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void rentHistoryBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/UserRentHistoryForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void addCustomerBtnOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/UserCustomerAddForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }

    public void logOutBtnOnAction(ActionEvent actionEvent) throws IOException {
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


    public void dashBoardOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/DashboardUserForm.fxml"));
        pane = fxmlLoader.load();
        rightStackPane.getChildren().setAll(pane);
    }
}
