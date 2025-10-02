package coffee.model;

import java.io.Serializable;

public class Drink implements Serializable {
    private String drinkId;
    private String name;
    private String category;
    private double price;
    private String size;
    private String status; // available, out of stock

    public Drink() {}

    public Drink(String drinkId, String name, String category, double price, String size, String status) {
        this.drinkId = drinkId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.size = size;
        this.status = status;
    }

    public String getDrinkId() { return drinkId; }
    public void setDrinkId(String drinkId) { this.drinkId = drinkId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return drinkId + "," + name + "," + category + "," + price + "," + size + "," + status;
    }

    public static Drink fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) return null;
        return new Drink(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4], parts[5]);
    }
}