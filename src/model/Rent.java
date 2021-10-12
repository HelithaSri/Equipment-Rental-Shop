package model;

import java.util.ArrayList;

public class Rent {
    private String rentId;
    private String rentDate;
    private String cusId;
    private double Total;
    private ArrayList<RentDetail> rentDetails;

    public Rent() {
    }

    public Rent(String rentId, String rentDate, String cusId, double total, ArrayList<RentDetail> rentDetails) {
        this.setRentId(rentId);
        this.setRentDate(rentDate);
        this.setCusId(cusId);
        setTotal(total);
        this.setRentDetails(rentDetails);
    }

    public Rent(String rentId, double total, ArrayList<RentDetail> rentDetails) {
        this.rentId = rentId;
        Total = total;
        this.rentDetails = rentDetails;
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public ArrayList<RentDetail> getRentDetails() {
        return rentDetails;
    }

    public void setRentDetails(ArrayList<RentDetail> rentDetails) {
        this.rentDetails = rentDetails;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "rentId='" + rentId + '\'' +
                ", rentDate='" + rentDate + '\'' +
                ", cusId='" + cusId + '\'' +
                ", Total=" + Total +
                ", rentDetails=" + rentDetails +
                '}';
    }
}
