package coffee.service;

import java.io.*;
import java.util.*;
import coffee.model.Customer;

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
     public void suaThongTinKhachHang() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập mã khách hàng cần sửa: ");
        String maKH = sc.nextLine();

        List<String> lines = docTatCaDong();
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("Mã KH: " + maKH + " ")) {
                System.out.println("Khách hàng tìm thấy: " + lines.get(i));
                System.out.print("Nhập tên mới: ");
                String tenMoi = sc.nextLine();
                System.out.print("Nhập SĐT mới: ");
                String sdtMoi = sc.nextLine();
                System.out.print("Nhập email mới: ");
                String emailMoi = sc.nextLine();
                System.out.print("Nhập địa chỉ mới: ");
                String diaChiMoi = sc.nextLine();

                String capNhat = String.format(
                    "Mã KH: %s | Tên: %s | SĐT: %s | Email: %s | Địa chỉ: %s | Điểm: %d",
                    maKH,
                    tenMoi.isEmpty() ? layGiaTriCu(lines.get(i), "Tên") : tenMoi,
                    sdtMoi.isEmpty() ? layGiaTriCu(lines.get(i), "SĐT") : sdtMoi,
                    emailMoi.isEmpty() ? layGiaTriCu(lines.get(i), "Email") : emailMoi,
                    diaChiMoi.isEmpty() ? layGiaTriCu(lines.get(i), "Địa chỉ") : diaChiMoi,
                    Integer.parseInt(layGiaTriCu(lines.get(i), "Điểm"))
                );

                lines.set(i, capNhat);
                found = true;
                break;
            }
        }

        if (found) {
            ghiLaiFile(lines);
            System.out.println("✅ Cập nhật thông tin khách hàng thành công!");
        } else {
            System.out.println("❌ Không tìm thấy khách hàng!");
        }
    }
     // 🔹 Hàm phụ trợ: đọc file
    private List<String> docTatCaDong() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            // file có thể chưa tồn tại
        }
        return lines;
    }
    // 🔹 Hàm phụ trợ: ghi lại file
    private void ghiLaiFile(List<String> lines) {
        try (FileWriter fw = new FileWriter("customers.txt")) {
            for (String line : lines) {
                fw.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi lại file: " + e.getMessage());
        }
    }
    // 🔹 Hàm phụ trợ: lấy giá trị cũ từ chuỗi
    private String layGiaTriCu(String line, String truong) {
        try {
            String[] parts = line.split("\\|");
            for (String p : parts) {
                if (p.trim().startsWith(truong + ":")) {
                    return p.split(":", 2)[1].trim();
                }
            }
        } catch (Exception e) {
            // bỏ qua
        }
        return "";
    }

}
