package model;

import java.math.BigDecimal;

public class RentItem {
    private String serialNumber;
    private String description;
    private BigDecimal rentPrice;
    private String status;
    private String modelId;
    private String categoryId;

    public RentItem() {
    }

    public RentItem(String serialNumber, String description, BigDecimal rentPrice, String status, String modelId, String categoryId) {
        this.setSerialNumber(serialNumber);
        this.setDescription(description);
        this.setRentPrice(rentPrice);
        this.setStatus(status);
        this.setModelId(modelId);
        this.setCategoryId(categoryId);
    }

    public RentItem(String serialNumber, String description, BigDecimal rentPrice, String modelId, String categoryId) {
        this.serialNumber = serialNumber;
        this.description = description;
        this.rentPrice = rentPrice;
        this.modelId = modelId;
        this.categoryId = categoryId;
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

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(BigDecimal rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "RentItem{" +
                "serialNumber='" + serialNumber + '\'' +
                ", description='" + description + '\'' +
                ", rentPrice=" + rentPrice +
                ", status='" + status + '\'' +
                ", modelId='" + modelId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
