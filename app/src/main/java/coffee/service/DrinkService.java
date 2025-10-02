package coffee.service;

import coffee.model.Drink;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DrinkService {
    private static final String FILE_PATH = "drinks.txt";
    private List<Drink> drinks;

    public DrinkService() {
        drinks = loadFromFile();
    }

    // 1. Thêm món đồ uống
    public void addDrink(Drink drink) {
        drinks.add(drink);
        saveToFile();
    }

    // 2. Sửa món đồ uống (tìm theo id)
    public boolean updateDrink(String drinkId, Drink newDrink) {
        for (int i = 0; i < drinks.size(); i++) {
            if (drinks.get(i).getDrinkId().equals(drinkId)) {
                drinks.set(i, newDrink);
                saveToFile();
                return true;
            }
        }
        return false;
    }
    // ================== PRIVATE SUPPORT METHODS ==================
    private List<Drink> loadFromFile() {
        List<Drink> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                Drink d = Drink.fromString(line);
                if (d != null) list.add(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Drink d : drinks) {
                bw.write(d.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
