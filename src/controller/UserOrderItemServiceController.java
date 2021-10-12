package controller;

import db.DbConnection;
import model.OrderDetail;
import model.OrderItem;
import model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserOrderItemServiceController {

    /*   Genarate Order Id   */
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM orders ORDER BY OrderId DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId += 1;
            if (tempId < 10) {
                return "O-00" + tempId;
            } else if (tempId < 100) {
                return "O-0" + tempId;
            } else {
                return "O-" + tempId;
            }
        } else {
            return "O-000";
        }
    }

    /*   Load all item id's to cmb where qtyOnHand greater than zero (qty>0)   */
    public List<String> getAllItemID() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE QtyOnHand > 0").executeQuery();
        while (rst.next()) {
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    /*   Load fields & cmb to selected id data   */
    public OrderItem passItems(String orderId) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE ItemId='" + orderId + "'").executeQuery();
        if (rst.next()) {
            return new OrderItem(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getDouble(7)
            );
        } else {
            return null;
        }
    }

    /*  Start Transaction Part  */
    public boolean saveOrders(Orders orders) {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement("INSERT INTO orders VALUES (?,?,?)");
            stm.setObject(1, orders.getOrderId());
            stm.setObject(2, orders.getDate());
            stm.setObject(3, orders.getTotal());

            if (stm.executeUpdate() > 0) {
                if (saveOrderDetails(orders.getOrderId(), orders.getOrderDetailArrayList())) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            } else {
                con.rollback();
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean saveOrderDetails(String orderId, ArrayList<OrderDetail> orderDetailArrayList) throws SQLException, ClassNotFoundException {

        for (OrderDetail temp : orderDetailArrayList) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO orderDetail VALUES (?,?,?,?,?)");
            stm.setObject(1, orderId);
            stm.setObject(2, temp.getItemId());
            stm.setObject(3, temp.getOrderQty());
            stm.setObject(4, temp.getDiscount());
            stm.setObject(5, temp.getTotal());
            if (stm.executeUpdate() > 0) {
                if (updateItem(temp.getItemId(), temp.getOrderQty())) {

                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean updateItem(String itemId, int qty) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE item SET QtyOnHand=(QtyOnHand-'" + qty + "') WHERE ItemId='" + itemId + "'");
        return stm.executeUpdate() > 0;
    }

}
