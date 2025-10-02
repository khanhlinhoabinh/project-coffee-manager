package coffee.service;

import coffee.model.Drink;
import java.util.ArrayList;
import java.util.List;

public class DrinkService {
    private List<Drink> drinks = new ArrayList<>();

    // Thêm đồ uống mới
    public void addDrink(Drink drink) {
        drinks.add(drink);
    }

    // Xóa đồ uống theo ID (BAN-22)
    public boolean removeDrink(String drinkId) {
        return drinks.removeIf(d -> d.getDrinkId().equals(drinkId));
    }

    // Tìm đồ uống theo ID
    public Drink findDrink(String drinkId) {
        for (Drink d : drinks) {
            if (d.getDrinkId().equals(drinkId)) {
                return d;
            }
        }
        return null;
    }

    // BAN-25: Bật/Tắt trạng thái phục vụ
    public boolean toggleStatus(String drinkId) {
        Drink drink = findDrink(drinkId);
        if (drink != null) {
            if (drink.getStatus().equalsIgnoreCase("available")) {
                drink.setStatus("out of stock");
            } else {
                drink.setStatus("available");
            }
            return true;
        }
        return false;
    }

    // Lấy toàn bộ danh sách
    public List<Drink> getAllDrinks() {
        return drinks;
    }
}
