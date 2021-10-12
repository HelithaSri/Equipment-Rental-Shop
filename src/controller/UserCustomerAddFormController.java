package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Customer;
import util.PushNotification;
import util.Validation;
import view.tm.CustomerTM;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class UserCustomerAddFormController {

    public JFXTextField txtCusId;
    public JFXTextField txtCusName;
    public JFXTextField txtCusAddress;
    public JFXTextField txtPhoneNumber;
    public JFXTextField txtNic;
    public JFXTextField txtSearch;

    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCuId;
    public TableColumn colCusName;
    public TableColumn colCusNic;
    public TableColumn colCusAddress;
    public TableColumn colCusPhoneNumber;
    public JFXButton btnAdd;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    Pattern cusId = Pattern.compile("^(Cust-|cust-)[0-9]{3,}$");
    Pattern userName = Pattern.compile("^[A-z ]{3,30}$");
    Pattern address = Pattern.compile("^[A-z 0-9 \\/\\,]{2,50}[A-z 0-9]{1,50}$");
    Pattern phoneNo = Pattern.compile("^(077|071|078|075|076|072)[-]?[0-9]{7}$");
    Pattern nic = Pattern.compile("^[0-9]{9}[v]|[0-9]{12}$");

    private void storeValidation() {
        map.put(txtCusId, cusId);
        map.put(txtCusName, userName);
        map.put(txtCusAddress, address);
        map.put(txtPhoneNumber, phoneNo);
        map.put(txtNic, nic);
    }

    public void addBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(
                txtCusId.getText(),
                txtCusName.getText(),
                txtCusAddress.getText(),
                txtPhoneNumber.getText(),
                txtNic.getText()
        );
        if (new UserCustomerServiceFormController().addCustomer(customer)) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Successfully added Customer", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void clearBtnOnAction(ActionEvent actionEvent) {
        clear();
    }

    private void clear() {
        txtPhoneNumber.clear();
        txtNic.clear();
        txtCusAddress.clear();
        txtCusId.clear();
        txtCusName.clear();
        tblCustomer.getSelectionModel().clearSelection();
        txtCusId.requestFocus();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnAdd.setDisable(true);
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        CustomerTM customerTM = tblCustomer.getSelectionModel().getSelectedItem();

        if (new UserCustomerServiceFormController().deleteCustomer(customerTM.getCusId())) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Delete Selected Customer", 0, 2);
        } else {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(
                txtCusId.getText(),
                txtCusName.getText(),
                txtCusAddress.getText(),
                txtPhoneNumber.getText(),
                txtNic.getText()
        );
        if (new UserCustomerServiceFormController().updateCustomer(customer)) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Successfully Update Customer", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void tableRowSelectOnAction(MouseEvent mouseEvent) {
        try {
            CustomerTM customerTM = tblCustomer.getSelectionModel().getSelectedItem();
            txtCusId.setText(customerTM.getCusId());
            txtCusName.setText(customerTM.getCusName());
            txtCusAddress.setText(customerTM.getCusAddress());
            txtPhoneNumber.setText(customerTM.getCusNumber());
            txtNic.setText(customerTM.getCusNic());
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnAdd.setDisable(true);
        } catch (Exception e) {
        }
    }

    private void showItemOnTable() throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTM> customerTM = new UserCustomerServiceFormController().getAllCustomer();

        colCuId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        colCusNic.setCellValueFactory(new PropertyValueFactory<>("cusNic"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        colCusPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("cusNumber"));

        tblCustomer.setItems(customerTM);
    }

    public void initialize() {
        try {
            showItemOnTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        storeValidation();
        btnAdd.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        List<Customer> searchCustomer = new UserCustomerServiceFormController().searchCustomer(txtSearch.getText());
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        for (Customer customer : searchCustomer) {
            customerTMS.add(new CustomerTM(
                    customer.getCustId(),
                    customer.getCustName(),
                    customer.getCustAddress(),
                    customer.getCustNumber(),
                    customer.getCustNic()
            ));
        }
        tblCustomer.getItems().setAll(customerTMS);
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
