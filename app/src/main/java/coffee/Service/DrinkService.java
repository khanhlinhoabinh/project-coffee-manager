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

// 3. Xóa món đồ uống
    public boolean deleteDrink(String drinkId) {
        boolean removed = drinks.removeIf(d -> d.getDrinkId().equals(drinkId));
        if (removed) saveToFile();
        return removed;
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