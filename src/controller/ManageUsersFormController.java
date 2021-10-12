package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.User;
import util.PushNotification;
import util.Validation;
import view.tm.UserTM;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageUsersFormController {


    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtDob;
    public JFXComboBox<String> cmbUserType;
    public JFXButton btnAddUser;
    public TableView<UserTM> tblUser;
    public TableColumn colUserName;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colDob;
    public TableColumn colRole;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;

    String userType;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    Pattern userName = Pattern.compile("^[A-z]{3,10}$");
    Pattern password = Pattern.compile("^[A-z-0-9 @#$%]{4,50}$");
    Pattern name = Pattern.compile("^[A-z-0-9 ]{3,10}$");
    Pattern address = Pattern.compile("^[A-z 0-9 \\/\\,]{2,50}[A-z 0-9]{1,50}$");
    Pattern dob = Pattern.compile("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");

    private void storeValidation() {
        map.put(txtUserName, userName);
        map.put(txtPassword, password);
        map.put(txtName, name);
        map.put(txtAddress, address);
        map.put(txtDob, dob);
    }

    public void addUserBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        User login = new User(
                txtUserName.getText(),
                txtPassword.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtDob.getText(),
                userType
        );

        if (new UserServiceController().addUser(login)) {
            showItemsOnTable(); // Refresh Table
            new PushNotification().set("SUCCESS", "Successfully added new User", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    /*  Table Refresh   */
    public void showItemsOnTable() throws SQLException, ClassNotFoundException {
        ObservableList<UserTM> userTMS = UserServiceController.getAllUsers();
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        tblUser.setItems(userTMS);
    }

    /*  Delete Selected Row */
    public void deleteUserBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        UserTM getSelectedRowData = tblUser.getSelectionModel().getSelectedItem();
        String userName = getSelectedRowData.getUserName();

        if (new UserServiceController().deleteUsers(userName)) {
            new PushNotification().set("SUCCESS", "Delete Selected User", 0, 2);
            showItemsOnTable();
        } else {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        }
    }

    public void updateUserBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        User user = new User(
                txtUserName.getText(),
                txtPassword.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtDob.getText(),
                userType
        );
        if (new UserServiceController().updateUser(user)) {
            showItemsOnTable();
            new PushNotification().set("SUCCESS", "Successfully Update User", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void tableRowSelectOnAction(MouseEvent mouseEvent) {
        try {
            UserTM selectedItem = tblUser.getSelectionModel().getSelectedItem();

            txtUserName.setText(selectedItem.getUserName());
            txtName.setText(selectedItem.getName());
            txtAddress.setText(selectedItem.getAddress());
            txtDob.setText(selectedItem.getDob());
            cmbUserType.setValue(selectedItem.getRole());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        } catch (Exception e) {

        }
    }

    public void initialize() throws SQLException, ClassNotFoundException {

        cmbUserType.getItems().setAll("Admin", "User");  //  Load User Type to Cmb
        /*  Get Selected Value From Cmb */
        cmbUserType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            userType = newValue;
        });
        showItemsOnTable();
        storeValidation();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnAddUser.setDisable(true);
    }

    public void clearOnAction(ActionEvent actionEvent) {
        txtDob.clear();
        txtAddress.clear();
        txtName.clear();
        txtUserName.clear();
        txtPassword.clear();
        cmbUserType.setValue("");
        tblUser.getSelectionModel().clearSelection();
        txtUserName.requestFocus();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnAddUser.setDisable(true);
    }

    public void onKeyReleased(KeyEvent keyEvent) {
        btnAddUser.setDisable(true);
        Object validate = Validation.validate(map, btnAddUser);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (validate instanceof TextField) {
                TextField error = (TextField) validate;
                error.requestFocus();
            } else {
            }
        }
    }
}
