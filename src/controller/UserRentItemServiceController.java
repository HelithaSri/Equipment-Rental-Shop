package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import model.OrderDetail;
import model.Rent;
import model.RentDetail;
import model.RentItem;
import view.tm.RentDetailTM;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRentItemServiceController {
    public static List<RentDetail> searchRentHistory(String value) throws SQLException, ClassNotFoundException {
        List<RentDetail> rentDetails = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rentdetail WHERE CONCAT(rentId,RentDate,Status,serialnumber,rentDate) LIKE '%" + value + "%'").executeQuery();
        while (rst.next()) {
            rentDetails.add(new RentDetail(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6)
            ));
        }
        return rentDetails;
    }

    public static TextField searchCustomer(TextField value) throws SQLException, ClassNotFoundException {
        TextField field = new TextField();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM customer WHERE Nic Like '%" + value.getText() + "%'").executeQuery();
        while (rst.next()) {
            field.setText(rst.getString(1));
        }
        return field;
    }

    public String getRentId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rent ORDER BY RentId DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId += 1;
            if (tempId < 10) {
                return "R-00" + tempId;
            } else if (tempId < 100) {
                return "R-0" + tempId;
            } else {
                return "R-" + tempId;
            }
        } else {
            return "R-000";
        }
    }

    /*  load all serial numbers to cmb where status is "Available"   */
    public List<String> getAllSerialNumbers() throws ClassNotFoundException, SQLException {
       // ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rent_item WHERE Status = '" + "Available" + "'").executeQuery();
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rent_item WHERE Status = ?");
        stm.setObject(1,"Available");
        ResultSet rst = stm.executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    public RentItem passItems(String sn) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rent_item WHERE SerialNumber ='" + sn + "'").executeQuery();
        if (rst.next()) {
            return new RentItem(
                    rst.getString(1),
                    rst.getString(2),
                    new BigDecimal(rst.getString(3)),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        } else {
            return null;
        }
    }

    public boolean saveRent(Rent rent) {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement("INSERT INTO rent VALUES(?,?,?,?)");
            stm.setObject(1, rent.getRentId());
            stm.setObject(2, rent.getRentDate());
            stm.setObject(3, rent.getCusId());
            stm.setObject(4, rent.getTotal());

            if (stm.executeUpdate() > 0) {
                if (saveOrderDetails(rent.getRentId(), rent.getRentDetails())) {
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

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean saveOrderDetails(String id, ArrayList<RentDetail> rentDetails) throws SQLException, ClassNotFoundException {

        for (RentDetail temp : rentDetails) {

            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO rentdetail VALUES (?,?,?,?,?,?)");
            stm.setObject(1, temp.getSerialNumber());
            stm.setObject(2, id);
            stm.setObject(3, temp.getRentDate());
            stm.setObject(4, temp.getReturnDate());
            stm.setObject(5, temp.getTotal());
            stm.setObject(6, temp.getStatus());
            if (stm.executeUpdate() > 0) {

                if (updateStatus(temp.getSerialNumber(), "Rent")) {

                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean updateStatus(String serialNumber, String status) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE rent_item SET Status ='" + status + "' WHERE SerialNumber='" + serialNumber + "'");
        return stm.executeUpdate() > 0;
    }

    public ObservableList<RentDetailTM> getRentHistory() throws SQLException, ClassNotFoundException {
        ObservableList<RentDetailTM> list = FXCollections.observableArrayList();
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rentdetail");
        ResultSet rst = stm.executeQuery();

        while (rst.next()) {
            list.add(
                    new RentDetailTM(
                            rst.getString(1),
                            rst.getString(2),
                            rst.getString(3),
                            rst.getString(4),
                            Double.parseDouble(rst.getString(5)),
                            rst.getString(6)
                    ));
        }
        return list;

    }

    public ObservableList<RentDetail> getMostPopulerRentItem() throws SQLException, ClassNotFoundException {
        ObservableList<RentDetail> list = FXCollections.observableArrayList();
        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement("select r.Description,count(d.SerialNumber) as count from rent_item r, rentdetail d where r.serialnumber=d.serialnumber group by d.serialNumber ORDER BY count desc ;").executeQuery();
        while (set.next()) {
            list.add(new RentDetail(
                    set.getString(1),
                    set.getInt(2)
            ));
        }
        return list;
    }

    public ObservableList<OrderDetail> getMostPopulerSellItem() throws SQLException, ClassNotFoundException {
        ObservableList<OrderDetail> list = FXCollections.observableArrayList();
        ResultSet set = DbConnection.getInstance().getConnection().prepareStatement("select i.description,sum(o.orderQty) as qty from orderDetail o, item i where i.itemId=o.itemid group by i.description ORDER BY o.orderQty ASC ;").executeQuery();
        while (set.next()) {
            list.add(new OrderDetail(
                    set.getString(1),
                    set.getInt(2)
            ));
        }
        return list;
    }

    public String getCountOfAvailableRentalItem() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT count(serialNumber) FROM rent_item WHERE status='" + "Available" + "'").executeQuery();
        while (rst.next()) {
            count = rst.getString(1);
        }
        return count;
    }

    public String getCountOfRentalItem() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT count(serialNumber) FROM rent_item").executeQuery();
        while (rst.next()) {
            count = rst.getString(1);
        }
        return count;
    }

    public String getCountOfSellingItem() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT count(ItemId) FROM item").executeQuery();
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

    public String getMonthlyRentIncome() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("select sum(total) from rentdetail").executeQuery();
        while (rst.next()) {
            count = rst.getString(1);
        }
        return count;
    }

    public String getMonthlySellingIncome() throws SQLException, ClassNotFoundException {
        String count = null;
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("select sum(total) from orderdetail").executeQuery();
        while (rst.next()) {
            count = rst.getString(1);
        }
        return count;
    }

}
