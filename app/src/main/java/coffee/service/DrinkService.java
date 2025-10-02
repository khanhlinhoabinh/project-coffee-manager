import java.io.*;
import java.util.*;

public class CustomerSearch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập từ khóa tìm kiếm (Mã KH / Tên / SĐT): ");
        String keyword = scanner.nextLine().toLowerCase();

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println("✅ " + line);
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
        }

        if (!found) {
            System.out.println("Không tìm thấy khách hàng nào.");
        }
    }
}
