package coffee.service;

import coffee.model.Drink;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DrinkService {
    public static void exportDrinksToTxt(List<Drink> drinks, String filePath) {
    try (FileWriter writer = new FileWriter(filePath)) {
        for (Drink drink : drinks) {
            writer.write("ID: " + drink.getDrinkId() +
                         ", Name: " + drink.getName() +
                         ", Category: " + drink.getCategory() +
                         ", Price: " + drink.getPrice() +
                         ", Size: " + drink.getSize() +
                         ", Status: " + drink.getStatus() + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
