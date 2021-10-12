package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.OrderDetail;
import model.OrderItem;
import model.Orders;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.PushNotification;
import view.tm.OrderCartTM;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserOrderItemFormController {

    public JFXComboBox<String> cmbItemId;
    public JFXTextField txtDescription;
    public JFXTextField txtPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtModel;
    public JFXTextField txtQTY;
    public JFXTextField txtDiscount;

    public TableView<OrderCartTM> tblorderCart;
    public TableColumn colItemId;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colModel;
    public TableColumn colQtyOnHand;
    public TableColumn colPrice;
    public TableColumn colDiscount;

    public Label lblDate;
    public Label lblTime;
    public Label lblTotalPrice;
    public Label lblTxtOrderId;

    ObservableList<OrderCartTM> oblist = FXCollections.observableArrayList();
    private String itemId;
    private double tot;
    private int selectedRowIndex = -1;

    public void addBtnOnAction(ActionEvent actionEvent) {

        if (txtQTY.getText().isEmpty() && cmbItemId.getSelectionModel().isEmpty()) {
            return;
        }

        String description = txtDescription.getText();
        Double unitPrice = Double.valueOf(txtPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        String model = txtModel.getText();

        if (txtQTY.getText().equals("0") || txtQTY.getText().isEmpty()) {
            new PushNotification().set("WARNING", "Invalid QTY", 0, 1);
            return;
        }

        int qty = Integer.parseInt(txtQTY.getText());
        double discount = Double.parseDouble(txtDiscount.getText());
        double price = calDiscount(qty, unitPrice, discount);


        if (qtyOnHand < qty) {
            new PushNotification().set("WARNING", "Invalid QTY", 0, 1);
            return;
        }

        OrderCartTM tm = new OrderCartTM(
                itemId,
                description,
                unitPrice,
                model,
                qty,
                price,
                discount
        );

        int exists = isExists(tm);
        if (exists == -1) {
            oblist.add(tm);
        } else {
            OrderCartTM temp = oblist.get(exists);
            OrderCartTM newTm = new OrderCartTM(
                    temp.getItemId(),
                    temp.getDescription(),
                    temp.getUnitPrice(),
                    temp.getModel(),
                    temp.getQty() + qty,
                    temp.getPrice() + price,
                    temp.getDiscount()
            );
            if (!(txtQTY.getText().equals(0) || txtQTY.getText().isEmpty())) {
                if (qtyOnHand < temp.getQty() + qty) {
                    new PushNotification().set("WARNING", "Invalid QTY", 0, 1);
                    return;
                }
            }

            oblist.remove(exists);
            oblist.add(newTm);
        }
        tblorderCart.setItems(oblist);
        calculateCost();
        clear();
    }

    private int isExists(OrderCartTM cartTM) {
        for (int i = 0; i < oblist.size(); i++) {
            if (cartTM.getItemId().equals(oblist.get(i).getItemId())) {
                return i;
            }
        }
        return -1;
    }

    private void calculateCost() {
        double ttl = 0;
        for (OrderCartTM tm : oblist) {
            ttl += tm.getPrice();
        }
        String format = new DecimalFormat("0.00").format(ttl);
        lblTotalPrice.setText(format + " /=");
        tot = Double.parseDouble(format);
    }

    private double calDiscount(int qtyForCustomer, double unitPrice, double discount) {
        double total = qtyForCustomer * unitPrice;
        double s = 100 - discount;
        double price = (s * total) / 100;
        return price;
    }

    public void clearBtnOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {

        if (selectedRowIndex == -1) {
            lblTotalPrice.setText("");
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        } else {
            oblist.remove(selectedRowIndex);
            calculateCost();
            tblorderCart.refresh();
            new PushNotification().set("SUCCESS", "Delete Selected Item", 0, 2);
        }
        clear();
    }

    public void cancleOrderBtnOnAction(ActionEvent actionEvent) {
        oblist.clear();
        lblTotalPrice.setText("");
        clear();
    }

    private void clear() {
        txtQTY.clear();
        txtDiscount.clear();
        txtModel.clear();
        txtPrice.clear();
        txtQtyOnHand.clear();
        txtDescription.clear();
        tblorderCart.getSelectionModel().clearSelection();
        cmbItemId.setValue("");
        cmbItemId.requestFocus();


    }

    public void orderBtnOnActon(ActionEvent actionEvent) {

        ArrayList<OrderDetail> list = new ArrayList<>();

        for (OrderCartTM temp : oblist) {
            list.add(new OrderDetail(
                    lblTxtOrderId.getText(),
                    temp.getItemId(),
                    temp.getQty(),
                    temp.getDiscount(),
                    temp.getPrice()
            ));
        }

        Orders orders = new Orders(
                lblTxtOrderId.getText(),
                lblDate.getText(),
                tot,
                list
        );

        /*  Check Cart Is Empty */
        if (list.size() == 0) {
            return;
        }

        if (new UserOrderItemServiceController().saveOrders(orders)) {

            try {
                JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/Report/SellingReceipt.jrxml"));
                JasperReport compileReport = JasperCompileManager.compileReport(design);
                ObservableList<OrderCartTM> items = tblorderCart.getItems();
                HashMap map = new HashMap();
                String total = lblTotalPrice.getText();
                String orderId = lblTxtOrderId.getText();
                map.put("Total", total);
                map.put("OrderId", orderId);
                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JRBeanArrayDataSource(items.toArray()));
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                e.printStackTrace();
            }

            setOrderId();
            oblist.clear();
            cmbItemId.getItems().clear();
            try {
                cmbItemId.getItems().setAll(new UserOrderItemServiceController().getAllItemID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            new PushNotification().set("COMPLETE", "Order has Successful", 0, 4);
        } else {
            new PushNotification().set("NOT COMPLETE", "Order has Not Successful", 0, 1);
        }

    }

    private void loadData(String id) throws SQLException, ClassNotFoundException {
        OrderItem item = new UserOrderItemServiceController().passItems(id);
        if (item == null) {
        } else {
            txtDescription.setText(item.getDescription());
            txtModel.setText(String.valueOf(item.getModelId()));
            txtPrice.setText(String.valueOf(item.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
            txtDiscount.setText(String.valueOf(item.getDiscount()));
        }
    }

    private void setOrderId() {
        try {
            lblTxtOrderId.setText(new UserOrderItemServiceController().getOrderId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadDateAndTime() {
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void initialize() {
        setOrderId();

        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            itemId = newValue;
            try {
                loadData(newValue);
                itemId = newValue;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblorderCart.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedRowIndex = (int) newValue;
        });

        /*   Load Item Id's to cmb   */
        try {
            cmbItemId.getItems().setAll(new UserOrderItemServiceController().getAllItemID());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("Discount"));

        oblist.clear();
        loadDateAndTime();


    }


}
