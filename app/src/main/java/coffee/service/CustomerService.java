package coffee.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import coffee.model.Customer;

public class CustomerService {
    public void exportCustomersToTxt(List<Customer> customers, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Customer customer : customers) {
                writer.write("Họ tên: " + customer.getName() +
                             " | SĐT: " + customer.getPhone() +
                             " | Email: " + customer.getEmail() +
                             " | Địa chỉ: " + customer.getAddress() +
                             " | Loại thành viên: " + customer.getMembershipLevel() + "\n");
            }
            System.out.println("✅ Đã xuất danh sách khách hàng ra file: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi file: " + e.getMessage());
        }
    }
    public static void themKhachHangVaLuuFile() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập họ tên: ");
        String name = scanner.nextLine();

        System.out.print("Nhập số điện thoại: ");
        String phone = scanner.nextLine();

        System.out.print("Nhập email: ");
        String email = scanner.nextLine();

        System.out.print("Nhập địa chỉ: ");
        String address = scanner.nextLine();

        System.out.print("Nhập loại thành viên (thường/VIP/thân thiết): ");
        String membershipLevel = scanner.nextLine();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Họ tên và số điện thoại là bắt buộc!");
            return;
        }

        try (FileWriter writer = new FileWriter("customers.txt", true)) {
            String line = String.format("Họ tên: %s | SĐT: %s | Email: %s | Địa chỉ: %s | Loại thành viên: %s\n",
                    name, phone, email, address, membershipLevel);
            writer.write(line);
            System.out.println("Lưu khách hàng thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }

        
    }
}
