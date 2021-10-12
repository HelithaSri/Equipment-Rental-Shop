package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import model.RentDetail;
import view.tm.RentDetailTM;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class UserRentHistoryFormController {


    public JFXTextField txtSearch;
    public TableView<RentDetailTM> tblRentHistory;
    public TableColumn colRentId;
    public TableColumn colSerialNumber;
    public TableColumn colRentDate;
    public TableColumn colReturnDate;
    public TableColumn colTotal;
    public TableColumn colStatus;
    public Label lblTime;
    public Label lblDate;


    private void showRentHistoryOnTable() throws SQLException, ClassNotFoundException {
        ObservableList<RentDetailTM> rentHistory = new UserRentItemServiceController().getRentHistory();

        colRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        colSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        colRentDate.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblRentHistory.setItems(rentHistory);
    }

    public void txtsearchOnKeyPressed(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        List<RentDetail> rentDetails = new UserRentItemServiceController().searchRentHistory(txtSearch.getText());
        ObservableList<RentDetailTM> tableData = FXCollections.observableArrayList();
        for (RentDetail detail : rentDetails) {
            tableData.add(new RentDetailTM(
                    detail.getSerialNumber(),
                    detail.getRentId(),
                    detail.getRentDate(),
                    detail.getReturnDate(),
                    detail.getTotal(),
                    detail.getStatus()
            ));
        }
        tblRentHistory.getItems().setAll(tableData);
    }

    private void loadDateAndTime() {
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void initialize() {
        try {
            showRentHistoryOnTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadDateAndTime();

    }
}
