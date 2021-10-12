package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Rent;
import model.RentDetail;
import view.tm.RentDetailTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserReturnCheckServiceController {

    public static List<RentDetail> searchRentHistory(String value) throws SQLException, ClassNotFoundException {
        List<RentDetail> rentDetails = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rentdetail WHERE Status='" + "Rent" + "' AND CONCAT(rentId,SerialNumber,RentDate) LIKE '%" + value + "%'").executeQuery();
        while (rst.next()) {
            rentDetails.add(new RentDetail(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return rentDetails;
    }

    public ObservableList<RentDetailTM> getRentDetails() throws SQLException, ClassNotFoundException {
        ObservableList<RentDetailTM> list = FXCollections.observableArrayList();
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rentdetail WHERE Status='" + "Rent" + "'");
        ResultSet rst = stm.executeQuery();
        while (rst.next()) {
            list.add(new RentDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return list;
    }

    public boolean updateToReturned(Rent rent) {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            PreparedStatement stm = con.prepareStatement("UPDATE rent SET Total =(Total+?) WHERE RentId=?");
            stm.setObject(1, rent.getTotal());
            stm.setObject(2, rent.getRentId());

            if (stm.executeUpdate() > 0) {
                if (updateDetail(rent.getRentId(), rent.getRentDetails())) {
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

    public boolean updateDetail(String rId, ArrayList<RentDetail> list) throws SQLException, ClassNotFoundException {
        for (RentDetail tm : list) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE rentdetail SET Status=?, Total=(Total+?) WHERE RentId=? AND SerialNumber=? AND RentId=?");
            stm.setObject(1, tm.getStatus());
            stm.setObject(2, tm.getTotal());
            stm.setObject(3, tm.getRentId());
            stm.setObject(4, tm.getSerialNumber());
            stm.setObject(5, rId);
            if (stm.executeUpdate() > 0) {
                if (updateStatus(tm.getSerialNumber(), "Available")) {

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
}
