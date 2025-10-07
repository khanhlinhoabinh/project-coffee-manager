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
    
}
