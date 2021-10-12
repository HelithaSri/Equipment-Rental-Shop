package model;

public class OrderDetail {
    private String orderId;
    private String itemId;
    private int orderQty;
    private double discount;
    private double total;

    public OrderDetail(String itemId, int orderQty) {
        this.itemId = itemId;
        this.orderQty = orderQty;
    }

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String itemId, int orderQty, double discount, double total) {
        this.setOrderId(orderId);
        this.setItemId(itemId);
        this.setOrderQty(orderQty);
        this.setDiscount(discount);
        this.setTotal(total);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId='" + orderId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", orderQty=" + orderQty +
                ", discount=" + discount +
                ", total=" + total +
                '}';
    }
}
