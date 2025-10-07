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

    // 🟧 Chức năng 2: Cập nhật đơn hàng (sửa thông tin sản phẩm hoặc số lượng)
public void capNhatDonHang() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Nhập mã đơn hàng cần cập nhật: ");
    String maDH = sc.nextLine().trim();

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
                found = true;
                System.out.println("🔹 Dòng cần cập nhật:\n" + line);

                System.out.print("Nhập tên sản phẩm mới (Enter nếu giữ nguyên): ");
                String tenSP = sc.nextLine().trim();
                if (!tenSP.isEmpty()) {
                    line = line.replaceAll("\\| SP: [^|]*", "| SP: " + tenSP);
                }

                System.out.print("Nhập số lượng mới (Enter nếu giữ nguyên): ");
                String soLuongStr = sc.nextLine().trim();
                if (!soLuongStr.isEmpty()) {
                    try {
                        int soLuong = Integer.parseInt(soLuongStr);
                        line = line.replaceAll("\\| SL: \\d+", "| SL: " + soLuong);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Số lượng không hợp lệ!");
                    }
                }

                System.out.print("Nhập đơn giá mới (Enter nếu giữ nguyên): ");
                String donGiaStr = sc.nextLine().trim();
                if (!donGiaStr.isEmpty()) {
                    try {
                        double donGia = Double.parseDouble(donGiaStr);
                        line = line.replaceAll("\\| Đơn giá: [^|]*", "| Đơn giá: " + donGia);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Đơn giá không hợp lệ!");
                    }
                }

                ds.add(line);
                System.out.println("✅ Đã cập nhật đơn hàng!");
            } else {
                ds.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("❌ Lỗi đọc file: " + e.getMessage());
        return;
    }

    if (!found) {
        System.out.println("⚠️ Không tìm thấy mã đơn hàng!");
        return;
    }

    try (FileWriter fw = new FileWriter(file, false)) {
        for (String l : ds) fw.write(l + System.lineSeparator());
        System.out.println("💾 File orders.txt đã được cập nhật!");
    } catch (IOException e) {
        System.out.println("❌ Lỗi ghi file: " + e.getMessage());
    }
}

// 🟥 Chức năng 3: Hủy đơn hàng (xóa dòng chưa thanh toán)
public void huyDonHang() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Nhập mã đơn hàng cần hủy: ");
    String maDH = sc.nextLine().trim();

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
                found = true;
                System.out.println("🗑️ Đơn hàng sau sẽ bị hủy:\n" + line);
                System.out.print("Xác nhận hủy? (y/n): ");
                String confirm = sc.nextLine().trim().toLowerCase();
                if (!confirm.equals("y")) {
                    ds.add(line);
                    System.out.println("✅ Đơn hàng chưa bị xóa.");
                } else {
                    System.out.println("❌ Đã hủy đơn hàng!");
                }
            } else {
                ds.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("❌ Lỗi đọc file: " + e.getMessage());
        return;
    }

    if (!found) {
        System.out.println("⚠️ Không tìm thấy đơn hàng cần hủy!");
        return;
    }

    try (FileWriter fw = new FileWriter(file, false)) {
        for (String l : ds) fw.write(l + System.lineSeparator());
        System.out.println("💾 File orders.txt đã được cập nhật sau khi hủy!");
    } catch (IOException e) {
        System.out.println("❌ Lỗi ghi file: " + e.getMessage());
    }
}

    // 🟩 Chức năng 4: Tìm kiếm đơn hàng theo mã đơn, tên khách hàng hoặc ngày đặt
    public void timKiemDonHang() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập từ khóa tìm kiếm (Mã đơn / Tên KH / Ngày đặt): ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            System.out.println("\n🔎 KẾT QUẢ TÌM KIẾM:");
            while ((line = reader.readLine()) != null) {
                // chuyển dòng sang chữ thường để tìm kiếm không phân biệt hoa thường
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
                    // Thay đổi trạng thái trong dòng đơn hàng
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

    // 🟨 chức năng 6: Xuất danh sách đơn hàng ra file để lưu trữ hoặc tham khảo
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

    // Chức năng 7: Giao diện quản lý đơn hàng 
public void hienThiGiaoDienQuanLyDonHang() {
    Scanner sc = new Scanner(System.in);
    int choice = -1;

    do {
        System.out.println("\n============================");
        System.out.println("     QUẢN LÝ ĐƠN HÀNG");
        System.out.println("============================");
        System.out.println("1. Tạo đơn hàng mới");
        System.out.println("2. Cập nhật đơn hàng");
        System.out.println("3. Hủy đơn hàng");
        System.out.println("4. Tìm kiếm đơn hàng");
        System.out.println("5. Cập nhật trạng thái đơn hàng");
        System.out.println("6. Xuất danh sách đơn hàng ra file");
        System.out.println("0. Thoát");
        System.out.print("➡️  Nhập lựa chọn của bạn: ");

        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Lỗi: Vui lòng nhập số hợp lệ!");
            continue;
        }

        switch (choice) {
            case 1:
                this.taoDonHangMoi();
                break;
            case 2:
                this.capNhatDonHang();
                break;
            case 3:
                this.huyDonHang();
                break;
            case 4:
                this.timKiemDonHang();
                break;
            case 5:
                this.capNhatTrangThaiDonHang();
                break;
            case 6:
                this.xuatDanhSachDonHang();
                break;
            case 0:
                System.out.println("👋 Thoát quản lý đơn hàng.");
                break;
            default:
                System.out.println("⚠️  Lựa chọn không hợp lệ. Vui lòng thử lại!");
        }
    } while (choice != 0);
}

}
