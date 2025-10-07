package coffee.service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PaymentService {
    private final String FILE_NAME = "payments.txt";
    private Scanner sc = new Scanner(System.in);

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

    // 🟦 Chức năng 2: Cập nhật thông tin thanh toán (số tiền hoặc phương thức)
    public void capNhatThanhToan() {
        System.out.print("Nhập mã thanh toán cần cập nhật: ");
        String paymentId = sc.nextLine().trim();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("⚠️ File chưa tồn tại!");
            return;
        }

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(paymentId)) {
                    System.out.println("🔍 Thông tin hiện tại: " + line);
                    System.out.print("Nhập số tiền mới (bỏ qua nếu không đổi): ");
                    String newAmount = sc.nextLine().trim();
                    System.out.print("Nhập phương thức mới (bỏ qua nếu không đổi): ");
                    String newMethod = sc.nextLine().trim();

                    if (!newAmount.isEmpty()) parts[2] = newAmount;
                    if (!newMethod.isEmpty()) parts[3] = newMethod;

                    String newLine = String.join(",", parts);
                    lines.add(newLine);
                    found = true;
                    System.out.println("✅ Đã cập nhật: " + newLine);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("❌ Không tìm thấy mã thanh toán!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            System.out.println("💾 File payments.txt đã được cập nhật!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

    // 🟥 Chức năng 3: Hủy giao dịch thanh toán
    public void huyThanhToan() {
        System.out.print("Nhập mã thanh toán cần hủy: ");
        String paymentId = sc.nextLine().trim();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("⚠️ File chưa tồn tại!");
            return;
        }

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // So khớp mã thanh toán cần xóa
                if (parts.length >= 5 && parts[0].equalsIgnoreCase(paymentId)) {
                    found = true;
                    System.out.println("⚠️ Xác nhận hủy thanh toán này: " + line);
                    System.out.print("Bạn có chắc chắn muốn hủy? (y/n): ");
                    String confirm = sc.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        lines.add(line); // giữ lại nếu không đồng ý
                        System.out.println("✅ Đã hủy thao tác xóa.");
                    } else {
                        System.out.println("🗑️ Đã xóa thanh toán: " + paymentId);
                    }
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("❌ Không tìm thấy mã thanh toán cần hủy!");
            return;
        }

        // Ghi lại file (sau khi đã xóa dòng)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            System.out.println("💾 File payments.txt đã được cập nhật sau khi hủy.");
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi file: " + e.getMessage());
        }
    }

    // 🟦 Chức Năng 4: Tìm kiếm thanh toán theo mã TT, mã đơn hoặc ngày
    public void timKiemThanhToan() {
        System.out.print("Nhập từ khóa tìm kiếm (mã TT, mã đơn, hoặc ngày): ");
        String keyword = sc.nextLine().trim().toLowerCase();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("⚠️ File chưa tồn tại!");
            return;
        }

        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println("🔍 " + line);
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
        }

        if (!found)
            System.out.println("❌ Không tìm thấy kết quả phù hợp!");
    }

    // 🟩 Feature 5: Quản lý phương thức thanh toán
    public void quanLyPhuongThucThanhToan() {
        System.out.println("\n===== Quản lý phương thức thanh toán =====");
        System.out.println("1. Tiền mặt");
        System.out.println("2. Thẻ ngân hàng");
        System.out.println("3. QR Code");
        System.out.println("4. Ví điện tử (Momo/ZaloPay)");
        System.out.println("5. Khác");
        System.out.print("➡️ Chọn phương thức: ");
        String chon = sc.nextLine();

        String method;
        switch (chon) {
            case "1" -> method = "Tiền mặt";
            case "2" -> method = "Thẻ ngân hàng";
            case "3" -> method = "QR Code";
            case "4" -> method = "Ví điện tử";
            default -> method = "Khác";
        }

        System.out.println("✅ Phương thức thanh toán đã chọn: " + method);
    }

    // 🟧 Chức năng 6: In hóa đơn thanh toán
    public void inHoaDon() {
        System.out.print("Nhập mã thanh toán cần in hóa đơn: ");
        String paymentId = sc.nextLine().trim();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("⚠️ Chưa có dữ liệu thanh toán!");
            return;
        }

        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equalsIgnoreCase(paymentId)) {
                    found = true;
                    System.out.println("\n===============================");
                    System.out.println("         🧾 HÓA ĐƠN THANH TOÁN");
                    System.out.println("===============================");
                    System.out.println("Mã thanh toán : " + parts[0]);
                    System.out.println("Mã đơn hàng   : " + parts[1]);
                    System.out.println("Số tiền       : " + parts[2] + " VND");
                    System.out.println("Phương thức   : " + parts[3]);
                    System.out.println("Ngày thanh toán: " + parts[4]);
                    System.out.println("===============================");
                    System.out.println(" Cảm ơn quý khách đã mua hàng!");
                    System.out.println("===============================\n");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi đọc file: " + e.getMessage());
        }

        if (!found) {
            System.out.println("❌ Không tìm thấy mã thanh toán: " + paymentId);
        }
    }
}