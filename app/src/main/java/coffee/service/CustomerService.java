package coffee.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
}
