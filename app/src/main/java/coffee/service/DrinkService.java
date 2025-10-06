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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DrinkService {
    private List<Drink> drinks = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    // Thêm đồ uống
    public void addDrink() {
        System.out.print("Nhập ID đồ uống: ");
        String id = scanner.nextLine();

        System.out.print("Nhập tên đồ uống: ");
        String name = scanner.nextLine();

        System.out.print("Nhập loại đồ uống (category): ");
        String category = scanner.nextLine();

        System.out.print("Nhập giá: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Nhập size: ");
        String size = scanner.nextLine();

        System.out.print("Nhập trạng thái (available/out of stock): ");
        String status = scanner.nextLine();

        Drink drink = new Drink(id, name, category, price, size, status);
        drinks.add(drink);

        System.out.println("✅ Đã thêm đồ uống: " + drink);
    }

    // Hiển thị danh sách
    public void displayAll() {
        if (drinks.isEmpty()) {
            System.out.println("❌ Danh sách đồ uống trống.");
            return;
        }
        System.out.println("📋 Danh sách đồ uống:");
        for (Drink d : drinks) {
            System.out.println(d);
        }
    }

    // Tìm kiếm đồ uống theo tên
    public void searchDrink() {
        System.out.print("Nhập tên đồ uống cần tìm: ");
        String keyword = scanner.nextLine().toLowerCase();
        boolean found = false;

        for (Drink d : drinks) {
            if (d.getName().toLowerCase().contains(keyword)) {
                System.out.println("🔎 Tìm thấy: " + d);
                found = true;
            }
        }

        if (!found) {
            System.out.println("❌ Không tìm thấy đồ uống nào với từ khóa: " + keyword);
        }
    }

    // Cập nhật thông tin đồ uống
    public void updateDrink() {
        System.out.print("Nhập ID đồ uống cần sửa: ");
        String id = scanner.nextLine();
        Drink drink = findById(id);

        if (drink == null) {
            System.out.println("❌ Không tìm thấy đồ uống với ID: " + id);
            return;
        }

        System.out.print("Nhập tên mới (bỏ trống nếu không đổi): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) drink.setName(name);

        System.out.print("Nhập category mới (bỏ trống nếu không đổi): ");
        String category = scanner.nextLine();
        if (!category.isEmpty()) drink.setCategory(category);

        System.out.print("Nhập giá mới (bỏ trống nếu không đổi): ");
        String priceStr = scanner.nextLine();
        if (!priceStr.isEmpty()) drink.setPrice(Double.parseDouble(priceStr));

        System.out.print("Nhập size mới (bỏ trống nếu không đổi): ");
        String size = scanner.nextLine();
        if (!size.isEmpty()) drink.setSize(size);

        System.out.print("Nhập trạng thái mới (bỏ trống nếu không đổi): ");
        String status = scanner.nextLine();
        if (!status.isEmpty()) drink.setStatus(status);

        System.out.println("✅ Đã cập nhật đồ uống: " + drink);
    }

    // Xóa đồ uống
    public void deleteDrink() {
        System.out.print("Nhập ID đồ uống cần xóa: ");
        String id = scanner.nextLine();
        Drink drink = findById(id);

        if (drink == null) {
            System.out.println("❌ Không tìm thấy đồ uống với ID: " + id);
            return;
        }

        drinks.remove(drink);
        System.out.println("🗑️ Đã xóa đồ uống: " + drink.getName());
    }

    // Tìm theo ID (hàm tiện ích)
    private Drink findById(String id) {
        for (Drink d : drinks) {
            if (d.getDrinkId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    // Menu quản lý đồ uống
    public void menu() {
        while (true) {
            System.out.println("\n===== MENU QUẢN LÝ ĐỒ UỐNG =====");
            System.out.println("1. Thêm đồ uống");
            System.out.println("2. Hiển thị danh sách");
            System.out.println("3. Tìm kiếm đồ uống");
            System.out.println("4. Cập nhật đồ uống");
            System.out.println("5. Xóa đồ uống");
            System.out.println("0. Thoát");
            System.out.print("👉 Chọn chức năng: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addDrink(); break;
                case "2": displayAll(); break;
                case "3": searchDrink(); break;
                case "4": updateDrink(); break;
                case "5": deleteDrink(); break;
                case "0": return;
                default: System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }
    }
}
