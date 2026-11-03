# HỆ THỐNG QUẢN LÝ SINH VIÊN & ĐÀO TẠO (JAVA SWING)

Ứng dụng desktop xây dựng bằng **Java Swing** kết hợp bộ giao diện hiện đại **FlatLaf** và cơ sở dữ liệu **MySQL / MariaDB**. Ứng dụng áp dụng chuẩn kiến trúc phân tầng **MVC + DAO**.

---

## 1. Giới thiệu tổng quan

Hệ thống Quản lý Sinh viên và Đào tạo hỗ trợ công tác quản trị học vụ và giảng dạy tại các trường đại học, cao đẳng:
- Quản lý cơ cấu đào tạo: Khoa/Viện -> Lớp học -> Hồ sơ sinh viên.
- Quản lý chương trình học: Danh mục môn học, học phần, số tín chỉ.
- Quản lý kết quả học tập: Nhập điểm thành phần (chuyên cần, giữa kỳ, thi cuối kỳ), tự động tính ĐTB môn, tự động xếp loại học lực và xét điều kiện Đạt/Học lại.
- Báo cáo và thống kê: Phổ điểm theo lớp, bảng điểm cá nhân tích lũy (GPA) và danh sách xét học bổng.
- Phân quyền người dùng: Quản trị viên (Admin) và Giảng viên.

---

## 2. Yêu cầu hệ thống

- **Hệ điều hành:** Windows 10/11, macOS hoặc Linux.
- **Java:** JDK / JRE 21 LTS trở lên.
- **Cơ sở dữ liệu:** MySQL / MariaDB (thông qua XAMPP hoặc MySQL Service) chạy tại cổng `3306`.
- **Thư viện đi kèm (`lib/`):**
  - `mysql-connector-j-8.0.33.jar`
  - `flatlaf-3.5.4.jar`

---

## 3. Khởi động nhanh

1. Bật dịch vụ **MySQL** trong XAMPP (hoặc MySQL Server).
2. Nhập file CSDL `quanlysinhvien.sql` vào MySQL (tự động tạo database `baitaplon-javaswing`).
3. Chạy ứng dụng bằng một trong các cách sau:
   - **Cách 1:** Nhấp đúp chuột vào file `run.bat`.
   - **Cách 2:** Mở Terminal tại thư mục dự án và chạy:
     ```bash
     java -jar dist/Baitaplon-JavaSwing.jar
     ```

---

## 4. Tài khoản mặc định

| Tên đăng nhập | Mật khẩu | Vai trò | Quyền hạn |
| :--- | :--- | :--- | :--- |
| **admin** | **123** | Quản trị viên (Admin) | Toàn quyền quản trị cơ sở dữ liệu và quản trị tài khoản giảng viên. |
| **giangvien** | **123** | Giảng viên | Tra cứu hồ sơ sinh viên, nhập điểm thi, xem báo cáo và thống kê. |

---

## 5. Cấu trúc thư mục dự án

```text
JavaNangCao/
├── dist/
│   └── Baitaplon-JavaSwing.jar   # File thực thi chương trình
├── lib/                          # Các thư viện phụ thuộc (FlatLaf, MySQL Connector)
├── src/
│   ├── Connection/               # Kết nối CSDL (JdbcHelper)
│   ├── DAO/                      # Lớp thao tác dữ liệu CSDL (Data Access Object)
│   ├── Model/                    # Các đối tượng thực thể (Entity Models)
│   └── UI/                       # Giao diện người dùng FlatLaf (Login, MainForm, Panels)
├── build/                        # File class sau khi biên dịch
├── quanlysinhvien.sql            # Script khởi tạo cơ sở dữ liệu MySQL
├── HUONG_DAN_SU_DUNG.md          # Tài liệu hướng dẫn sử dụng chi tiết (RBAC)
├── README.md                     # Tài liệu giới thiệu dự án
└── run.bat                       # Script chạy nhanh ứng dụng
```
