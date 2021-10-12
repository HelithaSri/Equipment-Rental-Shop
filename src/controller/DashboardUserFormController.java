package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.OrderItem;
import model.RentItem;
import view.tm.OrderItemTM;
import view.tm.RentItemTM;

import java.sql.SQLException;
import java.util.List;

public class DashboardUserFormController {

    public JFXTextField txtSearch;
    public TableView<RentItemTM> tblRentItem;
    public TableColumn colSerialNumber;
    public TableColumn colDescription;
    public TableColumn colCategory;
    public TableColumn colModel;
    public TableColumn colPrice;

    public TableView tblSellingItems;
    public TableColumn colSellItemId;
    public TableColumn colSellDescription;
    public TableColumn colSellCategory;
    public TableColumn colSellModel;
    public TableColumn colSellQty;
    public TableColumn colSellPrice;

    public Label lblAvailableSellingItems;
    public Label lblAvailableRentalItem;
    public Label lblCustomers;

    public void searchOnAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        List<OrderItem> orderItems = new DashboardServiceController().searchAvailableSellingHistory(txtSearch.getText());
        ObservableList<OrderItem> orderList = FXCollections.observableArrayList();
        for (OrderItem item : orderItems) {
            orderList.add(new OrderItem(
                    item.getItemId(),
                    item.getDescription(),
                    item.getUnitPrice(),
                    item.getQtyOnHand(),
                    item.getCategoryId(),
                    item.getModelId()
            ));
        }

        List<RentItem> rentItems = new DashboardServiceController().searchAvailableHistory(txtSearch.getText());
        ObservableList<RentItemTM> list = FXCollections.observableArrayList();
        for (RentItem temp : rentItems) {
            list.add(new RentItemTM(
                    temp.getSerialNumber(),
                    temp.getDescription(),
                    temp.getRentPrice(),
                    temp.getModelId(),
                    temp.getCategoryId()
            ));
        }
        tblSellingItems.getItems().setAll(orderList);
        tblRentItem.getItems().setAll(list);
    }

    public void initialize() {
        try {
            listOfAvailableRentItem();
            listOfAvailableSellingItem();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /*  Load count to label   */
        try {
            lblAvailableSellingItems.setText(new DashboardServiceController().getCountOfSellingItem());
            lblAvailableRentalItem.setText(new DashboardServiceController().getCountOfRentalItem());
            lblCustomers.setText(new DashboardServiceController().getCountOfCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void listOfAvailableSellingItem() throws SQLException, ClassNotFoundException {
        ObservableList<OrderItemTM> availableSellingItems = new DashboardServiceController().getAvailableSellingItems();
        colSellItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colSellDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSellCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colSellModel.setCellValueFactory(new PropertyValueFactory<>("modelId"));
        colSellQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tblSellingItems.setItems(availableSellingItems);
    }

    private void listOfAvailableRentItem() throws SQLException, ClassNotFoundException {
        ObservableList<RentItemTM> availableRentedItems = new DashboardServiceController().getAvailableRentedItems();

        colSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("modelId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));

        tblRentItem.setItems(availableRentedItems);

    }


}
