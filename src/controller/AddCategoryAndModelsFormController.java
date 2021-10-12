package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Category;
import model.Model;
import util.PushNotification;
import util.Validation;
import view.tm.CategoryTM;
import view.tm.ModelTM;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AddCategoryAndModelsFormController {

    public JFXTextField txtCategoryId;
    public JFXTextField txtCategoryName;
    public JFXTextField txtModelId;
    public JFXTextField txtCModelName;
    public JFXTextField txtCategorySearch;
    public JFXTextField txtModelSearch;

    public TableView<CategoryTM> tblCategory;
    public TableColumn colCategoryId;
    public TableColumn colCategoryName;

    public TableView<ModelTM> tblModel;
    public TableColumn colModelId;
    public TableColumn colModelName;
    public JFXButton btnCategoryAdd;
    public JFXButton btnCategoryDelete;
    public JFXButton btnModelAdd;

    LinkedHashMap<TextField, Pattern> map1 = new LinkedHashMap<>();
    Pattern categoryId = Pattern.compile("^(C-)[0-9]{3,}$");
    Pattern categoryName = Pattern.compile("^[A-z-0-9 ]{3,30}$");

    LinkedHashMap<TextField, Pattern> map2 = new LinkedHashMap<>();
    Pattern modelId = Pattern.compile("^(M-)[0-9]{3,}$");
    Pattern modelName = Pattern.compile("^[A-z-0-9 ]{3,30}$");

    private void storeValidation1() {
        map1.put(txtCategoryId, categoryId);
        map1.put(txtCategoryName, categoryName);
    }

    private void storeValidation2() {
        map2.put(txtModelId, modelId);
        map2.put(txtCModelName, modelName);
    }

    public void addCategoryBtn(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Category category = new Category(txtCategoryId.getText(), txtCategoryName.getText());

        if (new CategoryAndModelService().addCategory(category)) {
            showCategoryOnTable();
            categoryClear();
            btnCategoryAdd.setDisable(true);
            new PushNotification().set("", "Successfully added Category", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void deleteCategoryBtn(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = null;
        CategoryTM selectedItem = tblCategory.getSelectionModel().getSelectedItem();
        try {
            id = selectedItem.getCategoryId();
        } catch (Exception e) {
        }

        if (new CategoryAndModelService().deleteCategory(id)) {
            showCategoryOnTable();
            categoryClear();
            new PushNotification().set("SUCCESS", "Delete Selected Category", 0, 2);

        } else {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        }
    }

    public void showCategoryOnTable() throws SQLException, ClassNotFoundException {
        ObservableList<CategoryTM> cList = new CategoryAndModelService().getAllCategory();

        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        tblCategory.setItems(cList);

    }

    public void addModelBtn(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Model model = new Model(txtModelId.getText(), txtCModelName.getText());

        if (new CategoryAndModelService().addModel(model)) {
            showModelOnTable();
            modelClear();
            btnModelAdd.setDisable(true);
            new PushNotification().set("SUCCESS", "Successfully added Model", 0, 0);
        } else {
            new PushNotification().set("WARNING", "Please Try Again", 0, 1);
        }
    }

    public void showModelOnTable() throws SQLException, ClassNotFoundException {
        ObservableList<ModelTM> mList = new CategoryAndModelService().getAllModel();

        colModelId.setCellValueFactory(new PropertyValueFactory<>("modelId"));
        colModelName.setCellValueFactory(new PropertyValueFactory<>("modelName"));

        tblModel.setItems(mList);

    }

    public void deleteModelBtn(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ModelTM selectedItem = tblModel.getSelectionModel().getSelectedItem();
        String id = null;
        try {
            id = selectedItem.getModelId();
        } catch (Exception e) {
        }

        if (new CategoryAndModelService().deleteModel(id)) {
            showModelOnTable();
            modelClear();
            new PushNotification().set("SUCCESS", "Delete Selected Model", 0, 2);
        } else {
            new PushNotification().set("WARNING", "Please Select a Row & Try Again", 0, 3);
        }
    }

    public void initialize() throws SQLException, ClassNotFoundException {
        showCategoryOnTable();
        showModelOnTable();
        storeValidation1();
        storeValidation2();
        btnCategoryAdd.setDisable(true);
        btnModelAdd.setDisable(true);
    }

    public void categoryClearFieldOnAction(ActionEvent actionEvent) {
        categoryClear();
    }

    private void categoryClear() {
        txtCategoryId.clear();
        txtCategoryName.clear();
        tblCategory.getSelectionModel().clearSelection();
        txtCategorySearch.clear();
        txtCategoryId.requestFocus();
    }

    public void modelClearFieldOnAction(ActionEvent actionEvent) {
        modelClear();
    }

    private void modelClear() {
        txtModelId.clear();
        txtCModelName.clear();
        tblModel.getSelectionModel().clearSelection();
        txtModelSearch.clear();
        txtModelId.requestFocus();
    }

    public void searchModel(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        List<Model> models = new CategoryAndModelService().searchModel(txtModelSearch.getText());
        ObservableList<ModelTM> modelTMS = FXCollections.observableArrayList();
        for (Model temp : models) {
            modelTMS.add(new ModelTM(
                    temp.getModelId(),
                    temp.getModelName()
            ));
        }
        tblModel.getItems().setAll(modelTMS);
    }

    public void searchCategory(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        List<Category> category = new CategoryAndModelService().searchCategory(txtCategorySearch.getText());
        ObservableList<CategoryTM> categoryTMS = FXCollections.observableArrayList();
        for (Category temp : category) {
            categoryTMS.add(new CategoryTM(
                    temp.getCategoryId(),
                    temp.getCategoryName()
            ));
        }
        tblCategory.getItems().setAll(categoryTMS);
    }

    public void categoryOnKeyReleased(KeyEvent keyEvent) {
        btnCategoryAdd.setDisable(true);
        Object validate = Validation.validate(map1, btnCategoryAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (validate instanceof TextField) {
                TextField error = (TextField) validate;
                error.requestFocus();
            } else {
            }
        }
    }

    public void modelOnKeyReleased(KeyEvent keyEvent) {
        btnModelAdd.setDisable(true);
        Object validate = Validation.validate(map2, btnModelAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (validate instanceof TextField) {
                TextField error = (TextField) validate;
                error.requestFocus();
            } else {
            }
        }
    }
}
