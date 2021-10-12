package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Rent;
import model.RentDetail;
import model.RentItem;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.PushNotification;
import view.tm.CartTM;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserRentItemFormController {

    static ObservableList<CartTM> parentCartObList = FXCollections.observableArrayList();
    public JFXDatePicker rentDate;
    public JFXComboBox<String> cmbSerialNumber;
    public JFXTextField txtDescription;
    public JFXTextField txtPrice;
    public JFXTextField txtModel;
    public JFXDatePicker returnDate;
    public JFXTextField txtCusNic;
    public JFXTextField txtCusId;
    public Label lblRentID;
    public Label lblDate;
    public Label lblTotalPrice;
    public TableView<CartTM> tblRent;
    public TableColumn colSerialNumber;
    public TableColumn colDescription;
    public TableColumn colModel;
    public TableColumn colCustomer;
    public TableColumn colOneDayPrice;
    public TableColumn colTotalDays;
    public TableColumn colPrice;
    public Label lblTime;

    double tot;
    private String sn;
    private int selectedRowIndex = -1;
    public void addBtnOnAction(ActionEvent actionEvent) {

        long dateOne = rentDate.getValue().toEpochDay();
        long dateTwo = returnDate.getValue().toEpochDay();

        String description = txtDescription.getText();
        String model = txtModel.getText();
        String cusId = txtCusId.getText();
        Double oneDayPrice = Double.valueOf(txtPrice.getText());
        int totalDay = (int) Math.abs(dateOne - dateTwo);
        Double price = oneDayPrice * totalDay;
        String rentDate = this.rentDate.getValue().toString();
        String returnDate = this.returnDate.getValue().toString();

        CartTM tm = new CartTM(
                sn,
                description,
                model,
                cusId,
                oneDayPrice,
                totalDay,
                price,
                rentDate,
                returnDate
        );

        int rowNumber = isExists(tm);
        if (rowNumber == -1) {
            parentCartObList.add(tm);
        } else {
            CartTM newTm = new CartTM(
                    sn,
                    description,
                    model,
                    cusId,
                    oneDayPrice,
                    totalDay,
                    price,
                    rentDate,
                    returnDate
            );
            parentCartObList.remove(rowNumber);
            parentCartObList.add(newTm);
        }
        tblRent.setItems(parentCartObList);
        calculateCost();
    }

    private void calculateCost() {

        double ttl = 0;
        for (CartTM tm : parentCartObList) {
            ttl += tm.getPrice();
        }
        String format = new DecimalFormat("0.00").format(ttl);
        lblTotalPrice.setText(format + " /=");
        tot = Double.parseDouble(format);
    }

    private int isExists(CartTM cartTM) {
        for (int i = 0; i < parentCartObList.size(); i++) {
            if (cartTM.getSerialNumber().equals(parentCartObList.get(i).getSerialNumber())) {
                return i;
            }
        }
        return -1;
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        if (selectedRowIndex == -1) {
            lblTotalPrice.setText("");
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        } else {
            parentCartObList.remove(selectedRowIndex);
            calculateCost();
            tblRent.refresh();
            new PushNotification().set("SUCCESS", "Delete Selected Item", 0, 2);
        }
        clear();
    }

    public void cancleOrderBtnOnAction(ActionEvent actionEvent) {
        clear();
        parentCartObList.clear();
    }

    private void clear() {
        txtCusId.clear();
        txtPrice.clear();
        txtModel.clear();
        txtDescription.clear();
        txtCusNic.clear();
        cmbSerialNumber.setValue("");
        returnDate.setValue(null);
        rentDate.setValue(LocalDate.now());
        tblRent.getSelectionModel().clearSelection();
        cmbSerialNumber.requestFocus();

    }

    public void orderBtnOnActon(ActionEvent actionEvent) {
        ArrayList<RentDetail> list = new ArrayList<>();

        for (CartTM temp : parentCartObList) {
            list.add(
                    new RentDetail(
                            temp.getSerialNumber(),
                            lblRentID.getText(),
                            temp.getRentDate(),
                            temp.getReturnDate(),
                            temp.getPrice(),
                            "Rent"
                    ));
        }
        Rent rent = new Rent(
                lblRentID.getText(),
                lblDate.getText(),
                txtCusId.getText(),
                tot,
                list
        );

        /*  Check Cart Is Empty */
        if (list.size() == 0) {
            return;
        }

        if (new UserRentItemServiceController().saveRent(rent)) {
            try {
                JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/Report/RentReceipt.jrxml"));
                JasperReport compileReport = JasperCompileManager.compileReport(design);
                ObservableList<CartTM> items = tblRent.getItems();
                HashMap map = new HashMap();
                String total = lblTotalPrice.getText();
                String rentId = lblRentID.getText();
                map.put("Total", total);
                map.put("Rent ID", rentId);
                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JRBeanArrayDataSource(items.toArray()));
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                e.printStackTrace();
            }
            setOrderId();
            clear();
            lblTotalPrice.setText("");
            parentCartObList.clear();
            cmbSerialNumber.getItems().clear();
            /*  Refresh Cmb & Load Serial   */
            try {
                cmbSerialNumber.getItems().setAll(new UserRentItemServiceController().getAllSerialNumbers());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            new PushNotification().set("COMPLETE", "Order has Successful", 0, 4);
        } else {
            new PushNotification().set("NOT COMPLETE", "Order has Not Successful", 0, 1);
        }

    }

    private void setOrderId() {
        try {
            lblRentID.setText(new UserRentItemServiceController().getRentId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void load(String sn) throws SQLException, ClassNotFoundException {
        RentItem rentItem = new UserRentItemServiceController().passItems(sn);
        if (rentItem == null) {
        } else {
            txtDescription.setText(rentItem.getDescription());
            txtPrice.setText(String.valueOf(rentItem.getRentPrice()));
            txtModel.setText(rentItem.getModelId());
        }
    }

    public void searchCustomerIdOnKeyAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        TextField textField = new UserRentItemServiceController().searchCustomer(txtCusNic);
        txtCusId.setText(textField.getText());
        if (txtCusNic.getText().isEmpty() || txtCusNic.getText().equals("")) {
            txtCusId.clear();
        }
    }

    /*  Disable Past Dates in DatePicker  */
    private void disablePastDates(JFXDatePicker datePicker) {
        /*  This piece of code was taken from the internet & Modified  */
        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate today = LocalDate.now();
                        setDisable(empty || item.compareTo(today) < 0);
                    }

                };
            }
        };
        datePicker.setDayCellFactory(callB);
    }

    public void clearBtnOnAction(ActionEvent actionEvent) {
        clear();
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

    public void initialize() throws SQLException, ClassNotFoundException {
        loadDateAndTime();
        setOrderId();
        cmbSerialNumber.getItems().setAll(new UserRentItemServiceController().getAllSerialNumbers());
        cmbSerialNumber.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                load(newValue);
                sn = newValue;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblRent.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedRowIndex = (int) newValue;
        });

        colSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOneDayPrice.setCellValueFactory(new PropertyValueFactory<>("oneDayPrice"));
        colTotalDays.setCellValueFactory(new PropertyValueFactory<>("totalDay"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        parentCartObList.clear();
        rentDate.setValue(LocalDate.now());
        disablePastDates(rentDate);
        disablePastDates(returnDate);

    }
}



