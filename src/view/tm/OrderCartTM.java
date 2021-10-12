package view.tm;

public class OrderCartTM {
    private String itemId;
    private String description;
    private Double unitPrice;
    private String model;
    private int qty;
    private Double price;
    private Double discount;

    public OrderCartTM() {
    }

    public OrderCartTM(String itemId, String description, Double unitPrice, String model, int qty, Double price, Double discount) {
        this.setItemId(itemId);
        this.setDescription(description);
        this.setUnitPrice(unitPrice);
        this.setModel(model);
        this.setQty(qty);
        this.setPrice(price);
        this.setDiscount(discount);
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderCartTM{" +
                "itemId='" + getItemId() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", unitPrice=" + getUnitPrice() +
                ", model='" + getModel() + '\'' +
                ", qty=" + getQty() +
                ", price=" + getPrice() +
                '}';
    }
}
