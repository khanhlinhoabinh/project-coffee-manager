package coffee.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private String orderId;
    private String customerId;
    private LocalDateTime orderTime;
    private double totalAmount;
    private String status; // pending, completed, cancelled

    public Order() {}

    public Order(String orderId, String customerId, LocalDateTime orderTime, double totalAmount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return orderId + "," + customerId + "," + orderTime + "," + totalAmount + "," + status;
    }
}