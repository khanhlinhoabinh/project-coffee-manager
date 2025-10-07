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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import coffee.service.CustomerService;


public class CustomerService {


    // 🟩 Chức năng: Thêm khách hàng mới và lưu vào file .txt
    public void themKhachHangVaLuuFile() {
        Scanner sc = new Scanner(System.in);

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

    public void hienThiGiaoDienQuanLy() {
        JFrame frame = new JFrame("Giao diện quản lý khách hàng");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Bảng dữ liệu
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã KH", "Tên", "SĐT", "Email", "Địa chỉ", "Điểm"}, 0
        );
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Thanh nút chức năng
        JPanel panel = new JPanel();
        JButton btnAdd = new JButton("Thêm KH");
        JButton btnUpdate = new JButton("Cập nhật điểm");
        panel.add(btnAdd);
        panel.add(btnUpdate);
        frame.add(panel, BorderLayout.SOUTH);

        // Nạp dữ liệu từ file
        napDuLieuTuFile(model);

        // Sự kiện nút
        btnAdd.addActionListener(e -> {
            themKhachHangVaLuuFile();
            model.setRowCount(0);
            napDuLieuTuFile(model);
        });

        btnUpdate.addActionListener(e -> {
            capNhatDiemTichLuy();
            model.setRowCount(0);
            napDuLieuTuFile(model);
        });

        frame.setVisible(true);
    }

    // 🧩 Hàm phụ: đọc file customers.txt và đổ dữ liệu lên bảng
    private void napDuLieuTuFile(DefaultTableModel model) {
        File file = new File("customers.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String maKH = parts[0].replace("Mã KH:", "").trim();
                    String ten = parts[1].replace("Tên:", "").trim();
                    String sdt = parts[2].replace("SĐT:", "").trim();
                    String email = parts[3].replace("Email:", "").trim();
                    String diaChi = parts[4].replace("Địa chỉ:", "").trim();
                    String diem = parts[5].replace("Điểm:", "").trim();
                    model.addRow(new Object[]{maKH, ten, sdt, email, diaChi, diem});
                }
            }
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
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
        }

        
    }
}
