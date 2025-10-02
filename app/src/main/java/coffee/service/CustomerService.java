import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CustomerManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập họ tên: ");
        String name = scanner.nextLine();

        System.out.print("Nhập số điện thoại: ");
        String phone = scanner.nextLine();

        System.out.print("Nhập email: ");
        String email = scanner.nextLine();

        System.out.print("Nhập địa chỉ: ");
        String address = scanner.nextLine();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Họ tên và số điện thoại là bắt buộc!");
            return;
        }

        try (FileWriter writer = new FileWriter("customers.txt", true)) {
            String line = String.format("Họ tên: %s | SĐT: %s | Email: %s | Địa chỉ: %s\n",
                    name, phone, email, address);
            writer.write(line);
            System.out.println("Lưu khách hàng thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
}
