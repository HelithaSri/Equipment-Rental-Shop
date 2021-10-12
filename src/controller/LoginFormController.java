package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {

    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public JFXButton btnLogin;
    public AnchorPane logingPane;
    public Label lblInvalid;


    public void loginBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {

        User login = new User(txtUserName.getText(), txtPassword.getText());
        String userType = new UserServiceController().login(login);

        if (userType.equals("Admin")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/AdminDashBoardForm.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.show();
            logingPane.getScene().getWindow().hide();

        } else if (userType.equals("User")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/UserDashBoardForm.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.show();
            logingPane.getScene().getWindow().hide();


        } else {
            lblInvalid.setVisible(true);
        }
    }

    public void closeIcon(MouseEvent mouseEvent) {
        Stage window = (Stage) logingPane.getScene().getWindow();
        window.close();

    }

    public void initialize() {
        txtUserName.requestFocus();
    }
}
