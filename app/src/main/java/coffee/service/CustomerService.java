package coffee.service;
import coffee.model.Customer;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;



public class CustomerService {
    private static final String FILE_PATH = "customers.txt";
    private static final Scanner sc = new Scanner(System.in);


    // 🟩 Chức năng 1: Thêm khách hàng mới và lưu vào file .txt
    public void themKhachHangVaLuuFile() {

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
        // 🟩 Chức năng 2: Xóa khách hàng theo Mã KH
    public void xoaKhachHang() {
        Scanner sc = new Scanner(System.in);
        System.out.print("🗑️ Nhập mã khách hàng cần xóa: ");
        String maKH = sc.nextLine().trim();

        File file = new File("customers.txt");
        if (!file.exists()) {
            System.out.println("⚠️ File customers.txt chưa tồn tại!");
            return;
        }

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Kiểm tra xem dòng có chứa mã KH cần xóa không
                if (line.contains("Mã KH: " + maKH)) {
                    found = true;
                    continue; // bỏ qua dòng này (tức là xóa)
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("❗ Không tìm thấy khách hàng có mã " + maKH);
            return;
        }

        // Ghi lại file sau khi xóa
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("✅ Đã xóa khách hàng có mã " + maKH + " thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }


        // 🟦 Chức năng 3: Cập nhật thông tin khách hàng (tên, SĐT, loại thành viên)
    public void capNhatThongTinKhachHang() {
        System.out.print("Nhập mã khách hàng cần sửa: ");
        String maKH = sc.nextLine().trim();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("⚠️ File customers.txt chưa tồn tại!");
            return;
        }

        List<String> ds = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Mã KH: " + maKH)) {
                    found = true;
                    System.out.println("🔍 Thông tin cũ: " + line);
                    System.out.println("=== Nhập thông tin mới ===");

                    System.out.print("Tên mới (Enter để giữ nguyên): ");
                    String tenMoi = sc.nextLine().trim();

                    System.out.print("SĐT mới (Enter để giữ nguyên): ");
                    String sdtMoi = sc.nextLine().trim();

                    System.out.print("Loại thành viên (Vàng/Bạc/Đồng - Enter để giữ nguyên): ");
                    String loaiMoi = sc.nextLine().trim();

                    // Thay đổi từng phần thông tin trong chuỗi
                    if (!tenMoi.isEmpty()) {
                        line = line.replaceFirst("\\| Tên: [^|]+", "| Tên: " + tenMoi);
                    }
                    if (!sdtMoi.isEmpty()) {
                        line = line.replaceFirst("\\| SĐT: [^|]+", "| SĐT: " + sdtMoi);
                    }

                    // Nếu chưa có loại thành viên thì thêm vào cuối
                    if (line.contains("| Loại:")) {
                        if (!loaiMoi.isEmpty()) {
                            line = line.replaceFirst("\\| Loại: [^|]+", "| Loại: " + loaiMoi);
                        }
                    } else if (!loaiMoi.isEmpty()) {
                        line += " | Loại: " + loaiMoi;
                    }

                    System.out.println("✅ Đã cập nhật thông tin khách hàng!");
                }
                ds.add(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("⚠️ Không tìm thấy khách hàng có mã: " + maKH);
            return;
        }

        try (FileWriter fw = new FileWriter(file, false)) {
            for (String l : ds) fw.write(l + System.lineSeparator());
            System.out.println("💾 File customers.txt đã được cập nhật!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi file: " + e.getMessage());
        }
    }
        // 🟩 Chức năng 4: Tìm kiếm khách hàng theo Mã KH, Tên hoặc SĐT
    public void timKiemKhachHang() {
        System.out.print("🔍 Nhập từ khóa tìm kiếm (Mã KH / Tên / SĐT): ");
        String keyword = sc.nextLine().trim().toLowerCase();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("⚠️ File customers.txt chưa tồn tại!");
            return;
        }

        boolean found = false;
        System.out.println("\n===== KẾT QUẢ TÌM KIẾM =====");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String lineLower = line.toLowerCase();
                if (lineLower.contains(keyword)) {
                    System.out.println(line);
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("❗ Không tìm thấy khách hàng nào phù hợp với từ khóa \"" + keyword + "\".");
        } else {
            System.out.println("===============================");
        }
    }


    // 🟦 Chức năng 6: Cập nhật điểm tích lũy sau thanh toán
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

        File file = new File(FILE_PATH);
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
                    line = line.substring(0, idx).trim() + " | Điểm: " + diemMoi;
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
            for (String l : ds) fw.write(l.strip() + System.lineSeparator());
            System.out.println("💾 Cập nhật điểm tích lũy thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }
    // 🟥 Chức năng 5: Cập nhật loại thành viên (Thường / Thân thiết / VIP)
    public void capNhatLoaiThanhVien() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập số điện thoại khách hàng cần cập nhật: ");
        String sdt = sc.nextLine().trim();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("⚠️ File customers.txt chưa tồn tại!");
            return;
        }

        // Gợi ý hạng thành viên
        System.out.println("Chọn loại thành viên mới:");
        System.out.println("1. Thường");
        System.out.println("2. Thân thiết");
        System.out.println("3. VIP");
        System.out.print("Nhập lựa chọn (1-3): ");
        String chon = sc.nextLine().trim();

        String loaiMoi;
        switch (chon) {
            case "1":
                loaiMoi = "Thường";
                break;
            case "2":
                loaiMoi = "Thân thiết";
                break;
            case "3":
                loaiMoi = "VIP";
                break;
            default:
                System.out.println("❌ Lựa chọn không hợp lệ!");
                return;
        }

        List<String> ds = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("SĐT: " + sdt)) {
                    found = true;
                    // Nếu dòng đã có "Loại:", cập nhật lại
                    if (line.contains("Loại:")) {
                        int idx = line.indexOf("Loại:");
                        line = line.substring(0, idx).trim() + " | Loại: " + loaiMoi;
                    } else {
                        // Nếu chưa có, thêm vào cuối dòng
                        line = line.strip() + " | Loại: " + loaiMoi;
                    }
                    System.out.println("✅ Đã cập nhật loại thành viên: " + loaiMoi);
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
            for (String l : ds) fw.write(l.strip() + System.lineSeparator());
            System.out.println("💾 Cập nhật loại thành viên thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }
    // 🟦 Chức năng 7: Xuất toàn bộ danh sách khách hàng ra file .txt
    public void xuatDanhSachKhachHang() {
        File inputFile = new File("customers.txt");
        File outputFile = new File("export_customers.txt");

        if (!inputFile.exists()) {
            System.out.println("⚠️ File customers.txt chưa tồn tại, không thể xuất!");
            return;
        }

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            FileWriter fw = new FileWriter(outputFile, false)
        ) {
            fw.write("====== DANH SÁCH KHÁCH HÀNG ======\n");
            fw.write("Ngày xuất: " + java.time.LocalDateTime.now() + "\n");
            fw.write("----------------------------------\n");

            String line;
            while ((line = br.readLine()) != null) {
                fw.write(line + System.lineSeparator());
            }

            fw.write("==================================\n");
            System.out.println("💾 Đã xuất danh sách khách hàng ra file: export_customers.txt");

        } catch (IOException e) {
            System.out.println("❌ Lỗi xuất file: " + e.getMessage());
        }
    }

    // 🟨 Chức năng 8: Quản lý khách hàng (phiên bản console)
public void hienThiGiaoDienQuanLy() {
    Scanner sc = new Scanner(System.in);
    int choice;

    do {
        System.out.println("\n===============================");
        System.out.println(" 📋 QUẢN LÝ KHÁCH HÀNG (Console)");
        System.out.println("===============================");
        System.out.println("1️⃣  Thêm khách hàng mới");
        System.out.println("2️⃣  Cập nhật thông tin khách hàng");
        System.out.println("3️⃣  Cập nhật điểm tích lũy");
        System.out.println("4️⃣  Cập nhật loại thành viên");
        System.out.println("5️⃣  Tìm kiếm khách hàng");
        System.out.println("6️⃣  Xóa khách hàng");
        System.out.println("7️⃣  Xuất danh sách khách hàng");
        System.out.println("0️⃣  Thoát");
        System.out.print("➡️  Chọn chức năng: ");

        choice = Integer.parseInt(sc.nextLine().trim());
        System.out.println();

        switch (choice) {
            case 1:
                themKhachHangVaLuuFile();
                break;
            case 2:
                capNhatThongTinKhachHang();
                break;
            case 3:
                capNhatDiemTichLuy();
                break;
            case 4:
                capNhatLoaiThanhVien();
                break;
            case 5:
                timKiemKhachHang();
                break;
            case 6:
                xoaKhachHang();
                break;
            case 7:
                xuatDanhSachKhachHang();
                break;
            case 0:
                System.out.println("🔚 Thoát quản lý khách hàng.");
                break;
            default:
                System.out.println("⚠️  Lựa chọn không hợp lệ, vui lòng thử lại.");
        }
    } while (choice != 0);
}

}
