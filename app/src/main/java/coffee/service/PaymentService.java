package coffee.service;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class PaymentService {
    
    // 🟩 Chức năng 1: Tạo thanh toán mới và lưu vào file .txt
    public void taoThanhToanMoi() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập mã thanh toán: ");
        String paymentId = sc.nextLine().trim();

        System.out.print("Nhập mã đơn hàng: ");
        String orderId = sc.nextLine().trim();

        System.out.print("Nhập số tiền thanh toán: ");
        double amount = 0;
        try {
            amount = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Lỗi: Số tiền không hợp lệ!");
            return;
        }

        System.out.print("Nhập hình thức thanh toán (Tiền mặt/Chuyển khoản/Momo...): ");
        String method = sc.nextLine().trim();

        LocalDate paymentDate = LocalDate.now();

        // Ghi vào file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt", true))) {
            writer.write(paymentId + "," + orderId + "," + amount + "," + method + "," + paymentDate);
            writer.newLine();
            System.out.println("✅ Thanh toán đã được ghi nhận thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }
    
}