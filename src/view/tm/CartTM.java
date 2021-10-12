package view.tm;

public class CartTM {
    private String serialNumber;
    private String description;
    private String model;
    private String customerId;
    private Double oneDayPrice;
    private int totalDay;
    private Double price;
    private String rentDate;
    private String returnDate;

    public CartTM() {
    }

    public CartTM(String serialNumber, String description, String model, String customerId, Double oneDayPrice, int totalDay, Double price, String rentDate, String returnDate) {
        this.serialNumber = serialNumber;
        this.description = description;
        this.model = model;
        this.customerId = customerId;
        this.oneDayPrice = oneDayPrice;
        this.totalDay = totalDay;
        this.price = price;
        this.setRentDate(rentDate);
        this.setReturnDate(returnDate);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getOneDayPrice() {
        return oneDayPrice;
    }

    public void setOneDayPrice(Double oneDayPrice) {
        this.oneDayPrice = oneDayPrice;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    @Override
    public String toString() {
        return "CartTM{" +
                "serialNumber='" + serialNumber + '\'' +
                ", description='" + description + '\'' +
                ", model='" + model + '\'' +
                ", customerId='" + customerId + '\'' +
                ", oneDayPrice=" + oneDayPrice +
                ", totalDay=" + totalDay +
                ", price=" + price +
                ", rentDate='" + rentDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                '}';
    }
}
