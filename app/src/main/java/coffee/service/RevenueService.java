package coffee.service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class RevenueService {
     private final String PAYMENT_FILE = "payments.txt";
    private final String REVENUE_FILE = "revenue.txt";
    //Chức năng 1
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

// 🟨 Chức năng 2: Tính doanh thu theo khoảng thời gian A - B
    public void tinhDoanhThuTheoKhoangThoiGian() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập ngày bắt đầu (yyyy-MM-dd): ");
        String startStr = sc.nextLine().trim();
        System.out.print("Nhập ngày kết thúc (yyyy-MM-dd): ");
        String endStr = sc.nextLine().trim();

        LocalDate start, end;
        try {
            start = LocalDate.parse(startStr);
            end = LocalDate.parse(endStr);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Định dạng ngày không hợp lệ!");
            return;
        }

        File file = new File(REVENUE_FILE);
        if (!file.exists()) {
            System.out.println("⚠️ File revenue.txt chưa tồn tại! Hãy chạy tính doanh thu hằng ngày trước.");
            return;
        }

        double tongDoanhThu = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n📅 Doanh thu từ " + start + " đến " + end + ":");
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("Ngày:")) continue;
                String[] parts = line.split("\\|");
                String ngayStr = parts[0].replace("Ngày:", "").trim();
                LocalDate ngay = LocalDate.parse(ngayStr);

                if (!ngay.isBefore(start) && !ngay.isAfter(end)) {
                    double doanhThu = Double.parseDouble(parts[1].replaceAll("[^0-9]", ""));
                    tongDoanhThu += doanhThu;
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
        }

        System.out.printf("\n📈 Tổng doanh thu trong khoảng: %.0f VNĐ\n", tongDoanhThu);
    }

     // 🟦 Chức năng 3: Thống kê món bán chạy
    public void thongKeMonBanChay() {
        File file = new File("orders.txt");
        if (!file.exists()) {
            System.out.println("⚠️ File orders.txt chưa tồn tại! (giả lập dữ liệu đơn hàng)");
            return;
        }

        Map<String, Integer> thongKe = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Mỗi dòng: orderId, drinkName, quantity, price
                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String drinkName = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());

                thongKe.put(drinkName, thongKe.getOrDefault(drinkName, 0) + quantity);
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file orders.txt: " + e.getMessage());
            return;
        }

        System.out.println("\n🥇 THỐNG KÊ MÓN BÁN CHẠY:");
        thongKe.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> System.out.printf("%s: %d ly\n", entry.getKey(), entry.getValue()));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("best_seller.txt"))) {
            for (var entry : thongKe.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
            System.out.println("\n✅ Đã lưu kết quả vào file best_seller.txt!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

}
