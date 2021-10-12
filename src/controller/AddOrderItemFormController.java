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
import model.OrderItem;
import util.PushNotification;
import util.Validation;
import view.tm.OrderItemTM;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddOrderItemFormController {
    public JFXTextField txtItemId;
    public JFXTextField txtDescription;
    public JFXTextField txtPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtDiscount;

    public JFXComboBox<String> cmbCategory;
    public JFXComboBox<String> cmbModel;

    public TableView<OrderItemTM> tblOrderItem;
    public TableColumn colItemId;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colCategory;
    public TableColumn colModel;
    public TableColumn colDiscount;
    public JFXButton btnAdd;
    public JFXButton btnClear;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;


    String selectedModel;   //  last selected model value
    String selectedCategory;    //  last selected category value

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    Pattern itemId = Pattern.compile("^(I-)[0-9]{3,}$");
    Pattern description = Pattern.compile("^[A-z-0-9 ]{3,50}$");
    Pattern unitPrice = Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$");
    Pattern qtyOnHand = Pattern.compile("^[0-9]{1,}$");
    Pattern discount = Pattern.compile("^[0-100]{1,3}([.][0-9]{2})?$");

    private void storeValidation() {
        map.put(txtItemId, itemId);
        map.put(txtDescription, description);
        map.put(txtPrice, unitPrice);
        map.put(txtQtyOnHand, qtyOnHand);
        map.put(txtDiscount, discount);
    }

    public void addBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (txtItemId.getText().isEmpty() && txtDescription.getText().isEmpty() && txtQtyOnHand.getText().isEmpty() && txtPrice.getText().isEmpty() && cmbCategory.getSelectionModel().isEmpty() && cmbModel.getSelectionModel().isEmpty()) {
            new PushNotification().set("WARNING", "Please Fill Data & Try Again", 0, 1);
            return;
        }
        OrderItem item = new OrderItem(
                txtItemId.getText(),
                txtDescription.getText(),
                Double.parseDouble(txtPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText()),
                selectedCategory,
                selectedModel,
                Double.parseDouble(txtDiscount.getText())
        );

        if (new SellItemServiceController().addSellItem(item)) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Successfully added Item", 0, 0);

        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (txtItemId.getText().isEmpty()) {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
            return;
        }
        OrderItem item = new OrderItem(
                txtItemId.getText(),
                txtDescription.getText(),
                Double.parseDouble(txtPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText()),
                selectedCategory,
                selectedModel,
                Double.parseDouble(txtDiscount.getText())
        );
        if (new SellItemServiceController().updateItem(item)) {
            showItemOnTable();
            clear();
            new PushNotification().set("SUCCESS", "Successfully Update Item", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void clearBtnOnAction(ActionEvent actionEvent) {
        clear();
    }

    private void clear() {
        txtItemId.clear();
        txtDescription.clear();
        txtPrice.clear();
        txtQtyOnHand.clear();
        cmbCategory.setValue("");
        cmbModel.setValue("");
        tblOrderItem.getSelectionModel().clearSelection();
        txtDiscount.setText("0.00");
        txtItemId.requestFocus();
        txtItemId.setEditable(true);
        btnAdd.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String itemIds = null;
        try {
            OrderItemTM itemTM = tblOrderItem.getSelectionModel().getSelectedItem();
            itemIds = itemTM.getItemId();
        } catch (Exception e) {
        }
        if (new SellItemServiceController().deleteItem(itemIds)) {
            new PushNotification().set("SUCCESS", "Delete Selected Item", 0, 2);
            showItemOnTable();
        } else {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        }

    }

    /*  Load data to Model & Category combo box */
    private void initializeComboBox() throws SQLException, ClassNotFoundException {
        cmbModel.getItems().setAll(new SellItemServiceController().getAllModels());
        cmbCategory.getItems().setAll(new SellItemServiceController().getAllCategory());
    }

    /*  Show Data in table & Refresh Table   */
    public void showItemOnTable() throws SQLException, ClassNotFoundException {
        ObservableList<OrderItemTM> itemList = new SellItemServiceController().getAllSellItems();

        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("modelId"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        tblOrderItem.setItems(itemList);

    }

    /*  Load selected row data to textField & combo box */
    public void tableRowSelectOnAction(MouseEvent mouseEvent) {
        try {
            OrderItemTM orderItemTM = tblOrderItem.getSelectionModel().getSelectedItem();
            txtItemId.setEditable(false);
            txtItemId.setText(orderItemTM.getItemId());
            txtDescription.setText(orderItemTM.getDescription());
            txtPrice.setText(String.valueOf(orderItemTM.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(orderItemTM.getQtyOnHand()));
            cmbModel.setValue(orderItemTM.getModelId());
            cmbCategory.setValue(orderItemTM.getCategoryId());
            txtDiscount.setText(String.valueOf(orderItemTM.getDiscount()));
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        } catch (Exception e) {

        }
    }

    public void initialize() {

        try {
            /*  load Model & loadCategory   */
            initializeComboBox();

            showItemOnTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        cmbModel.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedModel = newValue;
        });

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedCategory = newValue;
        });
        storeValidation();
        btnAdd.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
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
