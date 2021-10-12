package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.OrderItem;
import view.tm.OrderItemTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SellItemServiceController {
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

    public boolean addSellItem(OrderItem item) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO item VALUES (?, ?, ?, ?, ?, ?, ?)");
        stm.setObject(1, item.getItemId());
        stm.setObject(2, item.getDescription());
        stm.setObject(3, item.getUnitPrice());
        stm.setObject(4, item.getQtyOnHand());
        stm.setObject(5, item.getModelId());
        stm.setObject(6, item.getCategoryId());
        stm.setObject(7, item.getDiscount());

        return stm.executeUpdate() > 0;
    }

    public ObservableList<OrderItemTM> getAllSellItems() throws SQLException, ClassNotFoundException {
        ObservableList<OrderItemTM> oblist = FXCollections.observableArrayList();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM item").executeQuery();
        while (rst.next()) {
            oblist.add(new OrderItemTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getDouble(7)
            ));
        }
        return oblist;
    }

    public boolean deleteItem(String itemId) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `item` WHERE itemId='" + itemId + "'").executeUpdate() > 0;
    }

    public boolean updateItem(OrderItem item) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE item SET Description=?, UnitPrice=?, QtyOnHand=?, ModelId=?, CategoryId=?, Discount=? WHERE ItemId=?");
        stm.setObject(1, item.getDescription());
        stm.setObject(2, item.getUnitPrice());
        stm.setObject(3, item.getQtyOnHand());
        stm.setObject(4, item.getModelId());
        stm.setObject(5, item.getCategoryId());
        stm.setObject(6, item.getDiscount());
        stm.setObject(7, item.getItemId());

        return stm.executeUpdate() > 0;
    }

}
