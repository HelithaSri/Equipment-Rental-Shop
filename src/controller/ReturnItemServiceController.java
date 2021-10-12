package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.RentItem;
import view.tm.RentItemTM;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReturnItemServiceController {
    public boolean addItem(RentItem rentItem) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO rent_item VALUES (?,?,?,?,?,?)");
        stm.setObject(1, rentItem.getSerialNumber());
        stm.setObject(2, rentItem.getDescription());
        stm.setObject(3, rentItem.getRentPrice());
        stm.setObject(4, rentItem.getStatus());
        stm.setObject(5, rentItem.getModelId());
        stm.setObject(6, rentItem.getCategoryId());

        return stm.executeUpdate() > 0;
    }

    /*  Model eken cmbo ekata data ganna query eka  */
    public List<String> getAllModels() throws ClassNotFoundException, SQLException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM brandmodel").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    /*  Category eken cmbo ekata data ganna query eka  */
    public List<String> getAllCategory() throws ClassNotFoundException, SQLException {
        ResultSet rst = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT * FROM category").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    public ObservableList<RentItemTM> getAllRentItems() throws SQLException, ClassNotFoundException {
        ObservableList<RentItemTM> list = FXCollections.observableArrayList();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rent_item").executeQuery();
        while (rst.next()) {
            list.add(new RentItemTM(
                    rst.getString(1),
                    rst.getString(2),
                    new BigDecimal(rst.getString(3)),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return list;
    }

    public boolean updateRentItem(RentItem item) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE rent_item SET Description=?, RentPrice=? ,Status=?,ModelId=?,CategoryId=? WHERE SerialNumber=?");
        stm.setObject(1, item.getDescription());
        stm.setObject(2, item.getRentPrice());
        stm.setObject(3, item.getStatus());
        stm.setObject(4, item.getModelId());
        stm.setObject(5, item.getCategoryId());
        stm.setObject(6, item.getSerialNumber());
        return stm.executeUpdate() > 0;
    }

    public boolean deleteRentItem(String serialNumber) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM rent_item WHERE serialNumber='" + serialNumber + "'").executeUpdate() > 0;
    }
}
