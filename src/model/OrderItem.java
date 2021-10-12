package model;

public class OrderItem {
    private String itemId;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private String categoryId;
    private String modelId;
    private double discount;

    public OrderItem() {
    }

    public OrderItem(String itemId, String description, double unitPrice, int qtyOnHand, String categoryId, String modelId, double discount) {
        this.setItemId(itemId);
        this.setDescription(description);
        this.setUnitPrice(unitPrice);
        this.setQtyOnHand(qtyOnHand);
        this.setCategoryId(categoryId);
        this.setModelId(modelId);
        this.setDiscount(discount);
    }

    public OrderItem(String itemId, String description, double unitPrice, int qtyOnHand, String categoryId, String modelId) {
        this.itemId = itemId;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.categoryId = categoryId;
        this.modelId = modelId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "itemId='" + itemId + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", qtyOnHand=" + qtyOnHand +
                ", categoryId='" + categoryId + '\'' +
                ", modelId='" + modelId + '\'' +
                ", discount=" + discount +
                '}';
    }
}
