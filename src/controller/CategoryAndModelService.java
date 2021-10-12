package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Category;
import model.Model;
import view.tm.CategoryTM;
import view.tm.ModelTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryAndModelService {
    public static List<Model> searchModel(String value) throws SQLException, ClassNotFoundException {
        List<Model> model = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM brandmodel WHERE CONCAT(ModelId,ModelName) LIKE '%" + value + "%'").executeQuery();
        while (rst.next()) {
            model.add(new Model(
                    rst.getString(1),
                    rst.getString(2)
            ));
        }
        return model;
    }

    public static List<Category> searchCategory(String value) throws SQLException, ClassNotFoundException {
        List<Category> category = new ArrayList<>();
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM category WHERE CONCAT(CategoryId,CategoryName) LIKE '%" + value + "%'").executeQuery();
        while (rst.next()) {
            category.add(new Category(
                    rst.getString(1),
                    rst.getString(2)
            ));
        }
        return category;
    }

    public boolean addCategory(Category category) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO category VALUES(?,?)";
        PreparedStatement stm = connection.prepareStatement(query);

        stm.setObject(1, category.getCategoryId());
        stm.setObject(2, category.getCategoryName());

        return stm.executeUpdate() > 0;
    }

    public ObservableList<CategoryTM> getAllCategory() throws SQLException, ClassNotFoundException {
        ObservableList<CategoryTM> categoryObList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM category";
        PreparedStatement stm = connection.prepareStatement(query);
        ResultSet rst = stm.executeQuery();

        while (rst.next()) {
            categoryObList.add(new CategoryTM(
                    rst.getString(1),
                    rst.getString(2)
            ));
        }
        return categoryObList;
    }

    public boolean deleteCategory(String categoryId) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM category WHERE CategoryId='" + categoryId + "'").executeUpdate() > 0;
    }

    public boolean addModel(Model model) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO brandmodel VALUES(?,?)";
        PreparedStatement stm = connection.prepareStatement(query);

        stm.setObject(1, model.getModelId());
        stm.setObject(2, model.getModelName());

        return stm.executeUpdate() > 0;
    }

    public ObservableList<ModelTM> getAllModel() throws SQLException, ClassNotFoundException {

        ObservableList<ModelTM> categoryObList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM brandModel";
        PreparedStatement stm = connection.prepareStatement(query);
        ResultSet rst = stm.executeQuery();

        while (rst.next()) {
            categoryObList.add(new ModelTM(
                    rst.getString(1),
                    rst.getString(2)
            ));
        }
        return categoryObList;
    }

    public boolean deleteModel(String modelId) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement
                ("DELETE FROM brandmodel WHERE ModelId='" + modelId + "'").executeUpdate() > 0;
    }
}
