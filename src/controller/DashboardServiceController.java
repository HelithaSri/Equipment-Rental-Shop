package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.OrderItem;
import model.RentItem;
import view.tm.OrderItemTM;
import view.tm.RentItemTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardServiceController {

    public static List<RentItem> searchAvailableHistory(String value) throws SQLException, ClassNotFoundException {
        List<RentItem> rentItems = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT i.serialnumber, i.description, c.categoryName, m.modelName, i.RentPrice from category c, brandmodel m, rent_item i WHERE (i.status='Available' AND c.categoryid=i.categoryid AND m.modelid=i.modelid) and concat(i.serialnumber, c.categoryName, i.description, m.modelName, i.RentPrice )like'%" + value + "%'").executeQuery();
        while (rst.next()) {
            rentItems.add(new RentItem(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getBigDecimal(5),
                    rst.getString(4),
                    rst.getString(3)
            ));
        }
        return rentItems;
    }

    public static List<OrderItem> searchAvailableSellingHistory(String value) throws SQLException, ClassNotFoundException {
        List<OrderItem> orderItem = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT i.itemId, i.description, c.categoryName, m.modelName, i.qtyOnHand, i.unitPrice FROM item i, category c, brandModel m WHERE (i.qtyOnHand > 0 AND c.categoryId=i.categoryid AND m.modelId=i.modelId) AND concat(i.itemId, i.description, c.categoryName, m.modelName, i.qtyOnHand, i.unitPrice) like'%" + value + "%' GROUP BY i.itemId").executeQuery();
        while (rst.next()) {
            orderItem.add(new OrderItem(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(6),
                    rst.getInt(5),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return orderItem;
    }

    public ObservableList<RentItemTM> getAvailableRentedItems() throws SQLException, ClassNotFoundException {
        ObservableList<RentItemTM> list = FXCollections.observableArrayList();
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT i.serialnumber, i.description, c.categoryName, m.modelName, i.RentPrice from category c, brandmodel m, rent_item i WHERE i.status='" + "Available" + "' AND c.categoryid=i.categoryid AND m.modelid=i.modelid GROUP BY i.SerialNumber");
        ResultSet rst = stm.executeQuery();
        while (rst.next()) {
            list.add(new RentItemTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getBigDecimal(5),
                    rst.getString(4),
                    rst.getString(3)
            ));
        }
        return list;
    }

    public ObservableList<OrderItemTM> getAvailableSellingItems() throws SQLException, ClassNotFoundException {
        ObservableList<OrderItemTM> list = FXCollections.observableArrayList();
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement
                ("SELECT i.itemId, i.description, c.categoryName, m.modelName, i.qtyOnHand, i.unitPrice FROM item i, category c, brandModel m WHERE i.qtyOnHand > 0 AND c.categoryId=i.categoryid AND m.modelId=i.modelId GROUP BY i.itemId");
        ResultSet rst = stm.executeQuery();
        while (rst.next()) {
            list.add(new OrderItemTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(6),
                    rst.getInt(5),
                    rst.getString(4),
                    rst.getString(3)

            ));
        }
        return list;
    }

    public String getCountOfSellingItem() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT count(ItemId) FROM item WHERE qtyOnHand > 0").executeQuery();
        while (rst.next()) {
            count = rst.getString(1);
        }
        return count;
    }

    public String getCountOfRentalItem() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT count(serialNumber) FROM rent_item WHERE status='" + "Available" + "'").executeQuery();
        while (rst.next()) {
            count = rst.getString(1);
        }
        return count;
    }

    public String getCountOfCustomers() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT count(CustId) FROM Customer").executeQuery();
        while (rst.next()) {
            count = rst.getString(1);
        }
        return count;
    }


}
