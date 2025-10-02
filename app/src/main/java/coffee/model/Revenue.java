package coffee.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Revenue implements Serializable {
    private String revenueId;
    private LocalDate date;
    private double totalSales;
    private int totalOrders;
    private double totalProfit;

    public Revenue() {}

    public Revenue(String revenueId, LocalDate date, double totalSales, int totalOrders, double totalProfit) {
        this.revenueId = revenueId;
        this.date = date;
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
        this.totalProfit = totalProfit;
    }

    public String getRevenueId() { return revenueId; }
    public void setRevenueId(String revenueId) { this.revenueId = revenueId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getTotalSales() { return totalSales; }
    public void setTotalSales(double totalSales) { this.totalSales = totalSales; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public double getTotalProfit() { return totalProfit; }
    public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }

    @Override
    public String toString() {
        return revenueId + "," + date + "," + totalSales + "," + totalOrders + "," + totalProfit;
    }
}