package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.RentItem;
import util.PushNotification;
import util.Validation;
import view.tm.RentItemTM;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddRentItemFormController implements Initializable {


    public JFXTextField txtSerialNumber;
    public JFXTextField txtDescription;
    public JFXTextField txtRentPrice;
    public JFXComboBox<String> cmbCategory;
    public JFXComboBox<String> cmbModel;
    public JFXTextField txtStatus;

    public TableView<RentItemTM> tblRent;
    public TableColumn colSerialNumber;
    public TableColumn colDescription;
    public TableColumn colRentPrice;
    public TableColumn colCategory;
    public TableColumn colModel;
    public TableColumn colStatus;
    public JFXButton btnAdd;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    Pattern serialNum = Pattern.compile("^^(SN-)[0-9]{3,}$");
    Pattern description = Pattern.compile("^[A-z-0-9 ]{3,50}$");
    Pattern unitPrice = Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$");
    private String category;
    private String model;

    private void storeValidation() {
        map.put(txtSerialNumber, serialNum);
        map.put(txtDescription, description);
        map.put(txtRentPrice, unitPrice);
    }

    public void addItemBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (txtSerialNumber.getText().isEmpty() && txtDescription.getText().isEmpty() && txtRentPrice.getText().isEmpty() && cmbModel.getSelectionModel().isEmpty() && cmbCategory.getSelectionModel().isEmpty()) {
            new PushNotification().set("WARNING", "Please Insert Data & Try Again", 0, 1);
            return;
        }
        RentItem rentItem = new RentItem(
                txtSerialNumber.getText(),
                txtDescription.getText(),
                new BigDecimal(txtRentPrice.getText()),
                txtStatus.getText(),
                model,
                category
        );
        if (new ReturnItemServiceController().addItem(rentItem)) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Successfully added Item", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }

    }

    private void initializeComboBox() throws SQLException, ClassNotFoundException {
        cmbModel.getItems().setAll(new ReturnItemServiceController().getAllModels());
        cmbCategory.getItems().setAll(new ReturnItemServiceController().getAllCategory());
    }

    private void showItemOnTable() throws SQLException, ClassNotFoundException {
        ObservableList<RentItemTM> itemlist = new ReturnItemServiceController().getAllRentItems();

        colSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colRentPrice.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("modelId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblRent.setItems(itemlist);
    }

    public void tableRowSelectOnAction(MouseEvent mouseEvent) {
        try {
            txtSerialNumber.setEditable(false);
            RentItemTM itemTM = tblRent.getSelectionModel().getSelectedItem();
            txtSerialNumber.setText(itemTM.getSerialNumber());
            txtDescription.setText(itemTM.getDescription());
            txtRentPrice.setText(String.valueOf(itemTM.getRentPrice()));
            cmbCategory.setValue(itemTM.getCategoryId());
            cmbModel.setValue(itemTM.getModelId());
            txtStatus.setText(itemTM.getStatus());
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        } catch (Exception e) {
        }
    }

    public void updateBtn(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (txtSerialNumber.getText().isEmpty()) {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
            return;
        }
        RentItem rentItem = new RentItem(
                txtSerialNumber.getText(),
                txtDescription.getText(),
                new BigDecimal(txtRentPrice.getText()),
                txtStatus.getText(),
                model,
                category
        );
        if (new ReturnItemServiceController().updateRentItem(rentItem)) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Successfully Update Item", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void deleteBtn(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String serialNumber = null;
        try {
            RentItemTM itemTM = tblRent.getSelectionModel().getSelectedItem();
            serialNumber = itemTM.getSerialNumber();
        } catch (Exception e) {
        }
        if (new ReturnItemServiceController().deleteRentItem(serialNumber)) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Delete Selected Item", 0, 2);
        } else {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        }

    }

    public void clearBtnOnAction(ActionEvent actionEvent) {
        clear();
    }

    private void clear() {
        txtDescription.clear();
        txtRentPrice.clear();
        txtSerialNumber.clear();
        txtStatus.setText("Available");
        cmbCategory.setValue("");
        cmbModel.setValue("");
        tblRent.getSelectionModel().clearSelection();
        txtSerialNumber.requestFocus();
        txtSerialNumber.setEditable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnAdd.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            initializeComboBox();
            showItemOnTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            category = newValue;
        });
        cmbModel.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            model = newValue;
        });
        storeValidation();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnAdd.setDisable(true);
    }

    public void onKeyReleased(KeyEvent keyEvent) {
        btnAdd.setDisable(true);
        Object validate = Validation.validate(map, btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (validate instanceof TextField) {
                TextField error = (TextField) validate;
                error.requestFocus();
            } else {
            }
        }
    }
}
