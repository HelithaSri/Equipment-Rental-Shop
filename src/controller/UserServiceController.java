package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import view.tm.UserTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceController {

    public static ObservableList<UserTM> getAllUsers() throws SQLException, ClassNotFoundException {
        ObservableList<UserTM> obList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM `User`";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            obList.add(new UserTM(
                    resultSet.getString(1),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return obList;
    }

    public String login(User login) throws SQLException, ClassNotFoundException {
        String username = login.getUserName();
        String password = login.getPassword();

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `user` WHERE userName = ? AND password=md5(?)");
        stm.setObject(1, username);
        stm.setObject(2, password);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            String userRole = rst.getString(6);
            return userRole;
        } else {
            return "";
        }
    }

    public boolean addUser(User user) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO `User` VALUES(?,md5(?),?,?,?,?)";
        PreparedStatement stm = connection.prepareStatement(query);

        stm.setObject(1, user.getUserName());
        stm.setObject(2, user.getPassword());
        stm.setObject(3, user.getName());
        stm.setObject(4, user.getAddress());
        stm.setObject(5, user.getDob());
        stm.setObject(6, user.getRole());

        return stm.executeUpdate() > 0;

    }

    public boolean deleteUsers(String userName) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM `User` WHERE userName='" + userName + "'").executeUpdate() > 0;
    }

    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `User` SET password=md5(?), `name`=?, address=?, dob=?, `role`=? WHERE userName=?");
        statement.setObject(1, user.getPassword());
        statement.setObject(2, user.getName());
        statement.setObject(3, user.getAddress());
        statement.setObject(4, user.getDob());
        statement.setObject(5, user.getRole());
        statement.setObject(6, user.getUserName());
        return statement.executeUpdate() > 0;
    }


}
