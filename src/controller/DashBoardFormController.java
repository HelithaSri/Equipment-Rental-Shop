package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.OrderDetail;
import model.RentDetail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DashBoardFormController {

    public PieChart pieChart;
    public AnchorPane mainPane;
    public PieChart pieChart2;
    public JFXButton btnRentalItemInventory;
    public JFXButton btnCustmerReport;
    public JFXButton sellingItem;

    public Label lblAvailableRentalItem;
    public Label lblSellingItem;
    public Label lblRentalItem;
    public Label lblCustomers;


    public Label lblRentItems;
    public Label lblSellingItems;

    public JFXDatePicker datePickerFrom;
    public JFXDatePicker datePickerTo;


    ObservableList<RentDetail> list = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pie = FXCollections.observableArrayList();

    ObservableList<OrderDetail> list2 = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pie2 = FXCollections.observableArrayList();


    public void initialize() throws SQLException, ClassNotFoundException {
        list = new UserRentItemServiceController().getMostPopulerRentItem();
        for (RentDetail detail : list) {
            pie.add(new PieChart.Data(detail.getSerialNumber(), detail.getCount()));
        }
        pieChart.setData(pie);

        list2 = new UserRentItemServiceController().getMostPopulerSellItem();
        for (OrderDetail detail : list2) {
            pie2.add(new PieChart.Data(detail.getItemId(), detail.getOrderQty()));
        }
        pieChart2.setData(pie2);

        lblAvailableRentalItem.setText(new UserRentItemServiceController().getCountOfAvailableRentalItem());
        lblRentalItem.setText(new UserRentItemServiceController().getCountOfRentalItem());
        lblSellingItem.setText(new UserRentItemServiceController().getCountOfSellingItem());
        lblCustomers.setText(new UserRentItemServiceController().getCountOfCustomers());
        lblRentItems.setText(new UserRentItemServiceController().getMonthlyRentIncome());
        lblSellingItems.setText(new UserRentItemServiceController().getMonthlySellingIncome());

    }

    public void sellingOnAction(MouseEvent mouseEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/Report/SellItemReport.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void rentalItemInventoryReportOnAction(MouseEvent mouseEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/Report/RentalItemReport.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void customerReport(MouseEvent mouseEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/Report/CustomerReport.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void btnFinancialReportOnAction(MouseEvent mouseEvent) {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFrom = datePickerFrom.getValue();
        LocalDate dateTo = datePickerTo.getValue();
        String to = String.valueOf(dateTo);
        String from = String.valueOf(dateFrom);
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/Report/FinancialReport.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            HashMap map = new HashMap();
            map.put("fromDate",dateFrom);
            map.put("toDate",dateTo);
            map.put("to",to);
            map.put("from",from);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
