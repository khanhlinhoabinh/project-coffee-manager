package coffee.service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class OrderService {

    // 🟩 Chức năng 1: Tạo đơn hàng mới và lưu vào file .txt
    public void taoDonHangMoi() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập mã đơn hàng: ");
        String maDH = sc.nextLine().trim();

        System.out.print("Nhập tên khách hàng: ");
        String tenKH = sc.nextLine().trim();

        System.out.print("Nhập số điện thoại: ");
        String sdt = sc.nextLine().trim();

        System.out.print("Nhập tên sản phẩm: ");
        String tenSP = sc.nextLine().trim();

        System.out.print("Nhập số lượng: ");
        int soLuong;
        try {
            soLuong = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Số lượng không hợp lệ!");
            return;
        }

        System.out.print("Nhập đơn giá (VNĐ): ");
        double donGia;
        try {
            donGia = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Đơn giá không hợp lệ!");
            return;
        }

        double tongTien = soLuong * donGia;
        String ngayDat = LocalDate.now().toString();

        // Kiểm tra các trường bắt buộc
        if (maDH.isEmpty() || tenKH.isEmpty() || tenSP.isEmpty()) {
            System.out.println("⚠️ Mã đơn, tên KH, tên sản phẩm là bắt buộc!");
            return;
        }

        String thongTin = String.format(
            "Mã đơn: %s | Tên KH: %s | SĐT: %s | SP: %s | SL: %d | Đơn giá: %.0f | Tổng: %.0f | Ngày: %s\n",
            maDH, tenKH, sdt, tenSP, soLuong, donGia, tongTien, ngayDat
        );

        try (FileWriter fw = new FileWriter("orders.txt", true)) {
            fw.write(thongTin);
            System.out.println("✅ Đã lưu đơn hàng thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi file: " + e.getMessage());
        }
    }

    // 🟩 Chức năng 2: Tìm kiếm đơn hàng theo mã đơn, tên khách hàng hoặc ngày đặt
    public void timKiemDonHang() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập từ khóa tìm kiếm (Mã đơn / Tên KH / Ngày đặt): ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            System.out.println("\n🔎 KẾT QUẢ TÌM KIẾM:");
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("❌ Không tìm thấy đơn hàng phù hợp!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("⚠️ File đơn hàng chưa tồn tại. Hãy tạo đơn hàng trước!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi đọc file: " + e.getMessage());
        }
    }

    // 🟦 Chức năng 5: Cập nhật trạng thái đơn hàng (chờ/đang phục vụ/xong)
    public void capNhatTrangThaiDonHang() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập mã đơn hàng cần cập nhật trạng thái: ");
        String maDH = sc.nextLine().trim();

        System.out.print("Nhập trạng thái mới (chờ/đang phục vụ/xong): ");
        String trangThaiMoi = sc.nextLine().trim();

        File file = new File("orders.txt");
        if (!file.exists()) {
            System.out.println("⚠️ File orders.txt chưa tồn tại!");
            return;
        }

        List<String> ds = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Mã đơn: " + maDH + " ")) {
                    String newLine;
                    if (line.contains("| Trạng thái:")) {
                        newLine = line.replaceAll("\\| Trạng thái: [^|]*", "| Trạng thái: " + trangThaiMoi);
                    } else {
                        newLine = line.trim() + " | Trạng thái: " + trangThaiMoi;
                    }
                    ds.add(newLine);
                    found = true;
                    System.out.println("✅ Đã cập nhật trạng thái: " + newLine);
                } else {
                    ds.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("⚠️ Không tìm thấy đơn hàng!");
            return;
        }

        try (FileWriter fw = new FileWriter(file, false)) {
            for (String l : ds) fw.write(l + System.lineSeparator());
            System.out.println("💾 File orders.txt đã được cập nhật!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

    // 🟨🆕 Module 6: Xuất danh sách đơn hàng ra file để lưu trữ hoặc tham khảo
    public void xuatDanhSachDonHang() {
        File file = new File("orders.txt");
        if (!file.exists()) {
            System.out.println("⚠️ Chưa có đơn hàng nào để xuất!");
            return;
        }

        System.out.print("Nhập tên file muốn xuất (vd: backup_orders.txt): ");
        Scanner sc = new Scanner(System.in);
        String tenFileXuat = sc.nextLine().trim();

        if (tenFileXuat.isEmpty()) {
            tenFileXuat = "orders_backup_" + LocalDate.now() + ".txt";
        }

        try (
            BufferedReader reader = new BufferedReader(new FileReader(file));
            FileWriter writer = new FileWriter(tenFileXuat)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + System.lineSeparator());
            }
            System.out.println("✅ Đã xuất danh sách đơn hàng ra file: " + tenFileXuat);
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi xuất file: " + e.getMessage());
        }
    }

    // 🟦🆕 Module 7: Hiển thị giao diện quản lý đơn hàng trực quan (console-based)
    public void giaoDienQuanLyDonHang() {
        File file = new File("orders.txt");
        if (!file.exists()) {
            System.out.println("⚠️ Chưa có đơn hàng nào!");
            return;
        }

        System.out.println("\n===== 🧾 DANH SÁCH ĐƠN HÀNG =====");
        System.out.printf("%-10s | %-15s | %-10s | %-15s | %-5s | %-10s | %-10s | %-12s | %-15s\n",
                "Mã đơn", "Tên KH", "SĐT", "Sản phẩm", "SL", "Đơn giá", "Tổng", "Ngày", "Trạng thái");
        System.out.println("-----------------------------------------------------------------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8) {
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }

                    System.out.printf("%-10s | %-15s | %-10s | %-15s | %-5s | %-10s | %-10s | %-12s | %-15s\n",
                            parts[0].replace("Mã đơn:", "").trim(),
                            parts[1].replace("Tên KH:", "").trim(),
                            parts[2].replace("SĐT:", "").trim(),
                            parts[3].replace("SP:", "").trim(),
                            parts[4].replace("SL:", "").trim(),
                            parts[5].replace("Đơn giá:", "").trim(),
                            parts[6].replace("Tổng:", "").trim(),
                            parts[7].replace("Ngày:", "").trim(),
                            (parts.length > 8 ? parts[8].replace("Trạng thái:", "").trim() : "chưa rõ")
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
        }

        System.out.println("==============================================================");
    }
}
