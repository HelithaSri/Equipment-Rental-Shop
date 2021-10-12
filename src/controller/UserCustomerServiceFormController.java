package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import view.tm.CustomerTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCustomerServiceFormController {

    public static List<Customer> searchCustomer(String value) throws SQLException, ClassNotFoundException {
        List<Customer> list = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM customer WHERE CONCAT(CustId,CustName,Nic,CustAddress) Like '%" + value + "%'").executeQuery();
        while (rst.next()) {
            list.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return list;
    }

    public ObservableList<CustomerTM> getAllCustomer() throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM customer").executeQuery();
        while (rst.next()) {
            customerTMS.add(new CustomerTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return customerTMS;
    }

    public boolean addCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?)");
        stm.setObject(1, customer.getCustId());
        stm.setObject(2, customer.getCustName());
        stm.setObject(3, customer.getCustAddress());
        stm.setObject(4, customer.getCustNumber());
        stm.setObject(5, customer.getCustNic());
        return stm.executeUpdate() > 0;
    }

    public boolean updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE customer SET CustName=?, CustAddress=?, PhoneNumber=?, Nic=? WHERE CustId=?");
        stm.setObject(1, customer.getCustName());
        stm.setObject(2, customer.getCustAddress());
        stm.setObject(3, customer.getCustNumber());
        stm.setObject(4, customer.getCustNic());
        stm.setObject(5, customer.getCustId());

        return stm.executeUpdate() > 0;
    }

    public boolean deleteCustomer(String cusId) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM customer WHERE custId='" + cusId + "'").executeUpdate() > 0;
    }
}
