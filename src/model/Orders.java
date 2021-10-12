package model;

import java.util.ArrayList;

public class Orders {
    private String orderId;
    private String date;
    private Double total;
    private ArrayList<OrderDetail> orderDetailArrayList;

    public Orders() {
    }

    public Orders(String orderId, String date, Double total, ArrayList<OrderDetail> orderDetailArrayList) {
        this.setOrderId(orderId);
        this.setDate(date);
        this.setTotal(total);
        this.setOrderDetailArrayList(orderDetailArrayList);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ArrayList<OrderDetail> getOrderDetailArrayList() {
        return orderDetailArrayList;
    }

    public void setOrderDetailArrayList(ArrayList<OrderDetail> orderDetailArrayList) {
        this.orderDetailArrayList = orderDetailArrayList;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                ", total=" + total +
                ", orderDetailArrayList=" + orderDetailArrayList +
                '}';
    }
}
