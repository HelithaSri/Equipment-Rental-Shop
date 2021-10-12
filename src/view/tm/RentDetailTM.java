package view.tm;

public class RentDetailTM {
    private String serialNumber;
    private String rentId;
    private String rentDate;
    private String returnDate;
    private Double total;
    private String status;

    public RentDetailTM() {
    }

    public RentDetailTM(String serialNumber, String rentId, String rentDate, String returnDate, Double total, String status) {
        this.setSerialNumber(serialNumber);
        this.setRentId(rentId);
        this.setRentDate(rentDate);
        this.setReturnDate(returnDate);
        this.setTotal(total);
        this.setStatus(status);
    }

    public RentDetailTM(String serialNumber, String rentId, String rentDate, String returnDate) {
        this.serialNumber = serialNumber;
        this.rentId = rentId;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
    }

    public RentDetailTM(String serialNumber, String rentId, String rentDate, String returnDate, Double total) {
        this.serialNumber = serialNumber;
        this.rentId = rentId;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.total = total;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RentDetailTM{" +
                "serialNumber='" + serialNumber + '\'' +
                ", rentId='" + rentId + '\'' +
                ", rentDate='" + rentDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", total=" + total +
                ", status='" + status + '\'' +
                '}';
    }
}
