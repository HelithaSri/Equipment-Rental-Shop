package view.tm;

public class OrderItemTM {
    private String itemId;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private String modelId;
    private String categoryId;
    private double discount;


    public OrderItemTM() {
    }

    public OrderItemTM(String itemId, String description, double unitPrice, int qtyOnHand, String modelId, String categoryId, double discount) {
        this.setItemId(itemId);
        this.setDescription(description);
        this.setUnitPrice(unitPrice);
        this.setQtyOnHand(qtyOnHand);
        this.setModelId(modelId);
        this.setCategoryId(categoryId);
        this.setDiscount(discount);
    }

    public OrderItemTM(String itemId, String description, double unitPrice, int qtyOnHand, String modelId, String categoryId) {
        this.itemId = itemId;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.modelId = modelId;
        this.categoryId = categoryId;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OrderItemTM{" +
                "itemId='" + itemId + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", qtyOnHand=" + qtyOnHand +
                ", modelId='" + modelId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", discount=" + discount +
                '}';
    }
}
