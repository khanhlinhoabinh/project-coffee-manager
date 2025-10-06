package coffee.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.util.*;


public class CustomerService {

    // 🟩 Chức năng: Thêm khách hàng mới và lưu vào file .txt
    public void themKhachHangVaLuuFile() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập mã khách hàng: ");
        String maKH = sc.nextLine();

        System.out.print("Nhập tên khách hàng: ");
        String tenKH = sc.nextLine();

        System.out.print("Nhập số điện thoại: ");
        String sdt = sc.nextLine();

        System.out.print("Nhập email: ");
        String email = sc.nextLine();

        System.out.print("Nhập địa chỉ: ");
        String diaChi = sc.nextLine();

        System.out.print("Nhập điểm tích lũy ban đầu: ");
        int diem = 0;
        try {
            diem = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Điểm nhập không hợp lệ, mặc định là 0.");
        }

        if (maKH.isEmpty() || tenKH.isEmpty() || sdt.isEmpty()) {
            System.out.println("Mã KH, tên và số điện thoại là bắt buộc!");
            return;
        }

        String thongTin = String.format(
            "Mã KH: %s | Tên: %s | SĐT: %s | Email: %s | Địa chỉ: %s | Điểm: %d\n",
            maKH, tenKH, sdt, email, diaChi, diem
        );

        try (FileWriter fw = new FileWriter("customers.txt", true)) {
            fw.write(thongTin);
            System.out.println("✅ Lưu khách hàng thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi file: " + e.getMessage());
        }
    }
    public void capNhatDiemTichLuy() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập số điện thoại khách hàng: ");
        String sdt = sc.nextLine();

        System.out.print("Nhập tổng số tiền thanh toán: ");
        double tongTien;
        try {
            tongTien = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Số tiền không hợp lệ!");
            return;
        }

        File file = new File("customers.txt");
        if (!file.exists()) {
            System.out.println("⚠️ File customers.txt chưa tồn tại!");
            return;
        }

        List<String> ds = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("SĐT: " + sdt)) {
                    found = true;
                    int idx = line.lastIndexOf("Điểm:");
                    int diemCu = Integer.parseInt(line.substring(idx + 6).trim());
                    int diemCong = (int) (tongTien / 10000); // 1 điểm / 10.000đ
                    int diemMoi = diemCu + diemCong;
                    line = line.substring(0, idx) + "Điểm: " + diemMoi;
                    System.out.println("✅ Điểm tích lũy đã được cập nhật: " + diemMoi);
                }
                ds.add(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("⚠️ Không tìm thấy khách hàng!");
            return;
        }

        try (FileWriter fw = new FileWriter(file, false)) {
            for (String l : ds) fw.write(l + "\n");
            System.out.println("💾 Cập nhật điểm tích lũy thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

}
