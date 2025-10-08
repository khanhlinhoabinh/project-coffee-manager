package coffee.service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PaymentService {
    private final String FILE_NAME = "payments.txt";
    private final Scanner sc = new Scanner(System.in);

    // 🟩 Chức năng 1: Tạo thanh toán mới và lưu vào file .txt
    public void taoThanhToanMoi() {
        System.out.print("Nhập mã thanh toán: ");
        String paymentId = sc.nextLine().trim();

        System.out.print("Nhập mã đơn hàng: ");
        String orderId = sc.nextLine().trim();

        System.out.print("Nhập số tiền thanh toán: ");
        double amount;
        try {
            amount = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Số tiền không hợp lệ!");
            return;
        }

        System.out.print("Nhập hình thức thanh toán: ");
        String method = sc.nextLine().trim();

        LocalDate paymentDate = LocalDate.now();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(paymentId + "," + orderId + "," + amount + "," + method + "," + paymentDate);
            writer.newLine();
            System.out.println("✅ Thanh toán đã được ghi nhận!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

    // 🟦 Chức năng 2: Cập nhật thông tin thanh toán
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
                if (parts.length >= 5 && parts[0].equalsIgnoreCase(paymentId)) {
                    System.out.println("🔍 Thông tin hiện tại: " + line);
                    System.out.print("Nhập số tiền mới (Enter để giữ nguyên): ");
                    String newAmount = sc.nextLine().trim();
                    System.out.print("Nhập phương thức mới (Enter để giữ nguyên): ");
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

        ghiLaiFile(lines);
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
                if (parts.length >= 5 && parts[0].equalsIgnoreCase(paymentId)) {
                    found = true;
                    System.out.println("⚠️ Xác nhận hủy thanh toán này: " + line);
                    System.out.print("Bạn có chắc chắn muốn hủy? (y/n): ");
                    String confirm = sc.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        lines.add(line); // giữ lại nếu không đồng ý
                        System.out.println("✅ Hủy thao tác xóa.");
                    } else {
                        System.out.println("🗑️ Đã xóa thanh toán: " + paymentId);
                    }
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("❌ Không tìm thấy mã thanh toán cần hủy!");
            return;
        }

        ghiLaiFile(lines);
    }

    // 🟦 Chức năng 4: Tìm kiếm thanh toán
    public void timKiemThanhToan() {
        System.out.print("Nhập từ khóa tìm kiếm: ");
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

    // 🟩 Chức năng 5: Quản lý phương thức thanh toán
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

        if (!found)
            System.out.println("❌ Không tìm thấy mã thanh toán: " + paymentId);
    }

    // 🧾 Giao diện chính cho chức năng thanh toán
    public void hienThiGiaoDienThanhToan() {
        while (true) {
            System.out.println("\n===== GIAO DIỆN THANH TOÁN =====");
            System.out.println("1. Tạo thanh toán mới");
            System.out.println("2. Tìm kiếm thanh toán");
            System.out.println("3. Hủy thanh toán");
            System.out.println("4. Quản lý phương thức thanh toán");
            System.out.println("5. In hóa đơn");
            System.out.println("0. Thoát");
            System.out.print("➡️ Chọn chức năng: ");
            String chon = sc.nextLine();

            switch (chon) {
                case "1" -> taoThanhToanMoi();
                case "2" -> timKiemThanhToan();
                case "3" -> huyThanhToan();
                case "4" -> quanLyPhuongThucThanhToan();
                case "5" -> inHoaDon();
                case "0" -> {
                    System.out.println("Đã thoát giao diện thanh toán.");
                    return;
                }
                default -> System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }
    }

    // 🧩 Hàm tiện ích: ghi lại file sau khi cập nhật/xóa
    private void ghiLaiFile(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            System.out.println("💾 File payments.txt đã được cập nhật!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

    // Main thử
    public static void main(String[] args) {
        new PaymentService().hienThiGiaoDienThanhToan();
    }
}
