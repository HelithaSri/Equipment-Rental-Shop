package controller;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.Rent;
import model.RentDetail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.PushNotification;
import view.tm.RentDetailTM;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserReturnCheckFormController {

    private final ObservableList<RentDetailTM> obList = FXCollections.observableArrayList();
    public TableView<RentDetailTM> tblRentItems;
    public TableColumn colRentId;
    public TableColumn colSerialNumber;
    public TableColumn colRentDate;
    public TableColumn colReturnDate;
    public JFXTextField txtRentId;
    public JFXTextField txtSerialNumber;
    public JFXTextField txtReturnDate;
    public JFXTextField txtFine;
    public JFXTextField txtRentDate;
    public JFXTextField txtSearch;
    public TableView<RentDetailTM> secondTblRentItem;
    public TableColumn secondColRentId;
    public TableColumn secondColSerialNumber;
    public TableColumn secondColRentDate;
    public TableColumn secondColReturnDate;
    public TableColumn secondColFine;
    public Label lblTotalPrice;
    public Label lblDate;
    public Label lblTime;

    Double tot;
    private int secoundTblRowSelect;
    private Rent rentUpdate;

    /*  Search  */
    public void searchRentIdOnKeyReleased(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {

        List<RentDetail> rentDetails = new UserReturnCheckServiceController().searchRentHistory(txtSearch.getText());
        ObservableList<RentDetailTM> tableDate = FXCollections.observableArrayList();

        for (RentDetail detail : rentDetails) {
            tableDate.add(new RentDetailTM(
                    detail.getSerialNumber(),
                    detail.getRentId(),
                    detail.getRentDate(),
                    detail.getReturnDate()
            ));
        }
        tblRentItems.setItems(tableDate);


    }

    /*   First Table   */
    private void listOfRentItemTable() throws SQLException, ClassNotFoundException {
        ObservableList<RentDetailTM> rentDetails = new UserReturnCheckServiceController().getRentDetails();

        colRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        colSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        colRentDate.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        tblRentItems.setItems(rentDetails);
    }

    /*   Second Table   */
    private void secondListOfRentItemTable() throws SQLException, ClassNotFoundException {

        secondColRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        secondColSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        secondColRentDate.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        secondColReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        secondColFine.setCellValueFactory(new PropertyValueFactory<>("total"));

    }

    /*   selected items in first table load to below Fields & cmb   */
    public void rowSelectOnTable(MouseEvent mouseEvent) {
        try {
            RentDetailTM item = tblRentItems.getSelectionModel().getSelectedItem();
            txtRentId.setText(item.getRentId());
            txtSerialNumber.setText(item.getSerialNumber());
            txtRentDate.setText(item.getRentDate());
            txtReturnDate.setText(item.getReturnDate());
        } catch (Exception e) {
        }
    }

    /*   load items to second table using fields & cmb   */
    public void addBtnOnAction(ActionEvent actionEvent) {

        if (txtRentId.getText().isEmpty()) {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
            return;
        }

        String rentId = txtRentId.getText();
        String serialNumber = txtSerialNumber.getText();
        String rentDate = txtRentDate.getText();
        String returnDate = txtReturnDate.getText();
        Double fine = Double.valueOf(txtFine.getText());

        RentDetailTM tm = new RentDetailTM(
                serialNumber,
                rentId,
                rentDate,
                returnDate,
                fine
        );

        int rowNumber = isExists(tm);
        if (rowNumber == -1) {
            obList.add(tm);
        } else {
            RentDetailTM newTm = new RentDetailTM(
                    serialNumber,
                    rentId,
                    rentDate,
                    returnDate,
                    fine
            );
            obList.remove(rowNumber);
            obList.add(newTm);
        }
        secondTblRentItem.setItems(obList);
        calculateCost();
        clear();
    }

    private void clear() {
        txtReturnDate.clear();
        txtRentDate.clear();
        txtRentId.clear();
        txtSerialNumber.clear();
        txtFine.setText("0.00");
    }

    /*   check Second table on item available   */
    private int isExists(RentDetailTM detailTM) {
        for (int i = 0; i < obList.size(); i++) {
            if (detailTM.getSerialNumber().equals(obList.get(i).getSerialNumber())) {
                return i;
            }
        }
        return -1;
    }

    /*   Calculate the Cost/Total in fine   */
    private void calculateCost() {
        double ttl = 0;
        for (RentDetailTM tm : obList) {
            ttl += tm.getTotal();
        }
        String format = new DecimalFormat("0.00").format(ttl);
        lblTotalPrice.setText(format + " /=");
        tot = Double.valueOf(format);
    }

    /*   Transaction part is hear   */
    public void returnedBtnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ArrayList<RentDetail> list = new ArrayList<>();

        for (RentDetailTM temp : obList) {
            list.add(new RentDetail(
                    temp.getSerialNumber(),
                    temp.getRentId(),
                    temp.getRentDate(),
                    temp.getReturnDate(),
                    temp.getTotal(),
                    "Returned"
            ));
        }

        for (RentDetail tm : list) {
            Rent update = new Rent(
                    tm.getRentId(),
                    tot,
                    list
            );
            rentUpdate = update;
        }

        /*  Check Cart Is Empty */
        if (list.size() == 0) {
            return;
        }
        if (new UserReturnCheckServiceController().updateToReturned(rentUpdate)) {
            try {
                JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/Report/Returned.jrxml"));
                JasperReport compileReport = JasperCompileManager.compileReport(design);
                ObservableList<RentDetailTM> items = secondTblRentItem.getItems();
                HashMap map = new HashMap();
                String total = lblTotalPrice.getText();
                map.put("total", total);
                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JRBeanArrayDataSource(items.toArray()));
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                e.printStackTrace();
            }
            clear();
            obList.clear();
            listOfRentItemTable();
            new PushNotification().set("SUCCESS", "Successfully Returned", 0, 7);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 3);
        }
    }

    /*   Delete Selected Item On Second Table   */
    public void selectAndDelete(KeyEvent keyEvent) {
        try {
            if (secoundTblRowSelect == -1) {
                new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
            } else {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    obList.remove(secoundTblRowSelect);
                    calculateCost();
                    secondTblRentItem.refresh();
                }
            }
        } catch (Exception e) {
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
        loadDateAndTime();
        try {
            listOfRentItemTable();
            secondListOfRentItemTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        secondTblRentItem.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            secoundTblRowSelect = (int) newValue;
        });
    }
}
