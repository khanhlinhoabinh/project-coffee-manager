package coffee.service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RevenueService {
     private final String PAYMENT_FILE = "payments.txt";
    private final String REVENUE_FILE = "revenue.txt";

    public void tinhDoanhThuHangNgay() {
        File file = new File(PAYMENT_FILE);
        if (!file.exists()) {
            System.out.println("⚠️ File payments.txt chưa tồn tại! Hãy tạo thanh toán trước.");
            return;
        }

        Map<String, Double> doanhThuTheoNgay = new TreeMap<>(); // sắp xếp theo ngày tăng dần

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Mỗi dòng: paymentId, orderId, amount, method, date
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String ngayThanhToan = parts[4].trim();
                double soTien;

                try {
                    soTien = Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException e) {
                    continue; // bỏ qua dòng lỗi định dạng
                }

                doanhThuTheoNgay.put(
                    ngayThanhToan,
                    doanhThuTheoNgay.getOrDefault(ngayThanhToan, 0.0) + soTien
                );
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi đọc file payments.txt: " + e.getMessage());
            return;
        }

        // Ghi doanh thu vào file revenue.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REVENUE_FILE, false))) {
            System.out.println("\n📊 DOANH THU THEO NGÀY:");
            for (Map.Entry<String, Double> entry : doanhThuTheoNgay.entrySet()) {
                String line = String.format("Ngày: %s | Tổng doanh thu: %.0f VNĐ", entry.getKey(), entry.getValue());
                System.out.println(line);
                bw.write(line);
                bw.newLine();
            }
            System.out.println("\n✅ Đã lưu doanh thu hằng ngày vào file revenue.txt!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file revenue.txt: " + e.getMessage());
        }
    }

    // 🟦 Chức năng 4: Thống kê đơn hàng tổng quan
    public void thongKeDonHang() {
        File file = new File(PAYMENT_FILE);
        if (!file.exists()) {
            System.out.println("⚠️ Chưa có dữ liệu thanh toán để thống kê!");
            return;
        }

        int tongSoDon = 0;
        double tongDoanhThu = 0;
        Map<String, Integer> thongKePhuongThuc = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                tongSoDon++;

                try {
                    tongDoanhThu += Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException e) {
                    // bỏ qua nếu lỗi
                }

                String method = parts[3].trim();
                thongKePhuongThuc.put(method, thongKePhuongThuc.getOrDefault(method, 0) + 1);
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi đọc file payments.txt: " + e.getMessage());
            return;
        }

        System.out.println("\n📊 THỐNG KÊ TỔNG QUAN GIAO DỊCH:");
        System.out.println("Tổng số đơn hàng: " + tongSoDon);
        System.out.println("Tổng doanh thu: " + String.format("%.0f VNĐ", tongDoanhThu));

        System.out.println("\n💳 Thống kê theo phương thức thanh toán:");
        for (Map.Entry<String, Integer> entry : thongKePhuongThuc.entrySet()) {
            System.out.printf("- %s: %d đơn%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\n✅ Hoàn tất thống kê đơn hàng!");
    }
}
