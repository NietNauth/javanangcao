# TÀI LIỆU HƯỚNG DẪN SỬ DỤNG
## HỆ THỐNG QUẢN LÝ SINH VIÊN VÀ ĐÀO TẠO

> **Phiên bản:** 2.0  
> **Nền tảng:** Java Swing (FlatLaf) & MySQL Database  
> **Kiến trúc:** Model - View - DAO (Data Access Object)  

---

## MỤC LỤC
1. [Giới thiệu tổng quan](#1-giới-thiệu-tổng-quan)
2. [Yêu cầu hệ thống & Khởi động](#2-yêu-cầu-hệ-thống--khởi-động)
3. [Phân quyền người dùng & Tài khoản hệ thống](#3-phân-quyền-người-dùng--tài-khoản-hệ-thống)
   - [3.1. Bảng ma trận phân quyền (RBAC: Admin vs Giảng viên)](#31-bảng-ma-trận-phân-quyền-rbac-admin-vs-giảng-viên)
   - [3.2. Danh sách tài khoản mặc định](#32-danh-sách-tài-khoản-mặc-định)
4. [Hướng dẫn sử dụng chi tiết từng phân hệ](#4-hướng-dẫn-sử-dụng-chi-tiết-từng-phân-hệ)
   - [4.1. Trang chủ / Dashboard](#41-trang-chủ--dashboard)
   - [4.2. Quản lý Khoa & Lớp học (Chỉ Admin)](#42-quản-lý-khoa--lớp-học-chỉ-admin)
   - [4.3. Hồ sơ Sinh viên (Admin CRUD / Giảng viên Tra cứu)](#43-hồ-sơ-sinh-viên-admin-crud--giảng-viên-tra-cứu)
   - [4.4. Quản lý Môn học & Học phần (Chỉ Admin)](#44-quản-lý-môn-học--học-phần-chỉ-admin)
   - [4.5. Quản lý Điểm số & Thi (Admin & Giảng viên)](#45-quản-lý-điểm-số--thi-admin--giảng-viên)
   - [4.6. Báo cáo & Thống kê học tập (Admin & Giảng viên)](#46-báo-cáo--thống-kê-học-tập-admin--giảng-viên)
   - [4.7. Quản trị Tài khoản người dùng (Chỉ Admin)](#47-quản-trị-tài-khoản-người-dùng-chỉ-admin)
5. [Quy chuẩn tính điểm & Xếp loại tự động](#5-quy-chuẩn-tính-điểm--xếp-loại-tự-động)
6. [Xử lý sự cố thường gặp (Troubleshooting)](#6-xử-lý-sự-cố-thường-gặp-troubleshooting)

---

## 1. Giới thiệu tổng quan

**Hệ thống Quản lý Sinh viên và Đào tạo** là ứng dụng desktop hiện đại được xây dựng nhằm hỗ trợ công tác quản trị học vụ và giảng dạy tại các trường đại học, cao đẳng hoặc viện đào tạo.

Hệ thống cung cấp giải pháp trọn gói:
* **Quản lý tổ chức đào tạo:** Cơ cấu Khoa/Viện -> Lớp học -> Hồ sơ sinh viên.
* **Quản lý chương trình học:** Danh mục môn học, học phần, số tín chỉ, hệ số điểm.
* **Quản lý điểm số & Đánh giá học tập:** Nhập điểm thành phần (chuyên cần, giữa kỳ, thi kết thúc), tự động tính điểm trung bình (ĐTB), tự động xếp loại học lực và xét điều kiện Đạt/Học lại.
* **Báo cáo & Thống kê học vụ:** Đánh giá phổ điểm và tỷ lệ đạt theo từng lớp, tra cứu bảng điểm cá nhân toàn khóa tích lũy (GPA) và tự động xếp hạng sinh viên đủ tiêu chuẩn nhận học bổng.
* **Phân quyền chặt chẽ:** Tách biệt rõ ràng vai trò quản trị hệ thống (**Admin**) và vai trò cán bộ chuyên môn (**Giảng viên**).

---

## 2. Yêu cầu hệ thống & Khởi động

### 2.1. Yêu cầu môi trường
* **Hệ điều hành:** Windows 10/11 (hoặc macOS/Linux có hỗ trợ Java).
* **Java:** Đã cài đặt Java Runtime Environment / JDK phiên bản **21 LTS trở lên**.
* **Cơ sở dữ liệu:** MySQL / MariaDB (thông qua XAMPP hoặc MySQL Server độc lập) đang chạy cổng `3306`.

### 2.2. Các bước khởi động phần mềm
1. Khởi động dịch vụ **MySQL** trong phần mềm **XAMPP** (đảm bảo nút MySQL hiển thị màu xanh lá).
2. Mở thư mục dự án `F:\JavaNangCao`.
3. **Nhấp đúp chuột vào file `run.bat`**:
   * Hệ thống sẽ tự động kiểm tra môi trường và bật cửa sổ Đăng nhập trong giây lát.
   * *(Cách khác: Mở Terminal/PowerShell tại thư mục `dist`, gõ lệnh `java -jar Baitaplon-JavaSwing.jar`).*

---

## 3. Phân quyền người dùng & Tài khoản hệ thống

Để đảm bảo tính toàn vẹn dữ liệu học vụ và an ninh hệ thống, quyền hạn giữa **Quản trị viên (Admin)** và **Giảng viên** được thiết kế phân định rõ ràng theo chuẩn nghiệp vụ thực tế của các cơ sở giáo dục.

### 3.1. Bảng ma trận phân quyền (RBAC: Admin vs Giảng viên)

| Phân hệ / Chức năng | Quản trị viên (Admin) | Giảng viên | Mô tả chi tiết phân quyền |
| :--- | :---: | :---: | :--- |
| **Trang chủ (Dashboard)** | Có | Có | Xem toàn bộ số liệu thống kê tổng quan (Tổng số sinh viên, lớp, khoa, môn học). |
| **Khoa & Lớp học** | **Toàn quyền** (Thêm/Sửa/Xóa) | **Không có quyền (Ẩn)** | Cơ cấu tổ chức trường chỉ do phòng Đào tạo / Ban Giám hiệu quản lý. Giảng viên không được can thiệp. |
| **Hồ sơ Sinh viên** | **Toàn quyền** (Thêm/Sửa/Xóa) | **Chỉ xem & Tra cứu** | Giảng viên chỉ được xem danh sách, tra cứu theo lớp/tên để nắm danh sách sinh viên lớp mình; **bị khóa tính năng Thêm, Sửa, Xóa hồ sơ**. |
| **Môn học & Học phần** | **Toàn quyền** (Thêm/Sửa/Xóa) | **Không có quyền (Ẩn)** | Danh mục môn học, số tín chỉ và khung chương trình đào tạo chỉ do Admin thiết lập. |
| **Điểm số & Thi** | **Toàn quyền** | **Toàn quyền** | Nghiệp vụ sư phạm chính: Giảng viên nhập điểm chuyên cần, giữa kỳ, thi kết thúc; sửa đổi điểm và tính điểm trung bình. |
| **Báo cáo & Thống kê** | **Toàn quyền** | **Toàn quyền** | Xem phổ điểm lớp học, tra cứu bảng điểm cá nhân sinh viên và xem danh sách xếp loại học bổng. |
| **Quản trị Tài khoản** | **Toàn quyền** (Cấp/Đổi/Xóa) | **Không có quyền (Ẩn)** | Chỉ duy nhất 1 Admin tối cao có quyền tạo và cấp mới tài khoản cho các Giảng viên. |

### 3.2. Danh sách tài khoản mặc định

| Tên đăng nhập | Mật khẩu | Vai trò | Chức năng khi đăng nhập |
| :--- | :--- | :--- | :--- |
| **admin** | **123** | Quản trị viên | Hiển thị đầy đủ **7 thanh menu chức năng** + Đăng xuất. Toàn quyền quản trị cơ sở dữ liệu. |
| **giangvien** | **123** | Giảng viên | Menu được thu gọn chỉ gồm **4 chức năng nghiệp vụ**: Trang chủ, Hồ sơ Sinh viên (chỉ xem), Điểm số & Thi, Báo cáo & Thống kê + Đăng xuất. |

> **Chính sách an toàn tài khoản:**
> 1. Toàn bộ hệ thống chỉ cho phép duy nhất **1 tài khoản Admin** quản trị tối cao; không thể xóa hoặc hạ quyền tài khoản này.
> 2. Admin chỉ được cấp thêm các tài khoản với vai trò là **Giảng viên**.
> 3. Không có nút "Đăng ký" ngoài màn hình đăng nhập nhằm ngăn chặn việc tự do tạo tài khoản không rõ nguồn gốc.

---

## 4. Hướng dẫn sử dụng chi tiết từng phân hệ

### 4.1. Trang chủ / Dashboard
* **Mục đích:** Cung cấp số liệu thống kê tổng thể theo thời gian thực.
* **Các chỉ số trực quan:**
  * **Tổng số sinh viên:** Số sinh viên đang quản lý trong hệ thống.
  * **Lớp đang đào tạo:** Tổng số lớp sinh hoạt hiện tại.
  * **Khoa / Viện đào tạo:** Số lượng khoa/chuyên ngành.
  * **Môn học / Học phần:** Số môn học trong khung chương trình.
* **Thao tác:** Nhấn nút **Làm mới số liệu** ở góc dưới bên phải để cập nhật dữ liệu mới nhất.

---

### 4.2. Quản lý Khoa & Lớp học *(Chỉ dành cho Admin)*
*(Giảng viên sẽ không thấy mục này trên thanh menu điều hướng).*

Màn hình tích hợp 2 tab quản trị:
* **Tab 1: Quản lý Lớp học:**
  * Các trường thông tin: Mã Lớp (*), Tên Lớp (*), Khoa trực thuộc (*), Niên khóa.
  * Hỗ trợ các nút chức năng: **Thêm Lớp**, **Sửa Lớp**, **Xóa Lớp**, **Làm mới**.
* **Tab 2: Quản lý Khoa / Viện:**
  * Quản lý danh mục các Khoa: Mã Khoa (*), Tên Khoa (*), Số điện thoại, Email.
  * Hỗ trợ đầy đủ Thêm, Sửa, Xóa các Khoa/Viện.

---

### 4.3. Hồ sơ Sinh viên *(Khác biệt theo Vai trò)*

Giao diện tự động thích ứng tùy theo quyền của người dùng:

#### Đối với Quản trị viên (Admin):
* Toàn quyền quản lý: Có đầy đủ các nút **Thêm mới**, **Cập nhật**, **Xóa**, **Làm mới**.
* Cho phép nhập mới hồ sơ sinh viên, đổi lớp, cập nhật quê quán, số điện thoại, ngày sinh.

#### Đối với Giảng viên:
* Giao diện chuyển sang chế độ **"Thông tin chi tiết (Chỉ xem)"**:
  * Các nút Thêm mới, Cập nhật, Xóa bị ẩn hoàn toàn để bảo vệ tính toàn vẹn của hồ sơ sinh viên.
  * Các ô nhập liệu (Mã SV, Họ tên, Ngày sinh, v.v.) được khóa (chỉ đọc).
  * Giảng viên được toàn quyền sử dụng thanh công cụ phía trên để:
    * **Tìm kiếm sinh viên:** Nhập Mã SV, Họ tên hoặc Số điện thoại để tìm nhanh.
    * **Lọc theo lớp:** Chọn lớp giảng dạy để lọc ra danh sách toàn bộ sinh viên trong lớp đó.
    * **Xem chi tiết:** Bấm vào dòng sinh viên bất kỳ trong bảng để xem chi tiết thông tin liên hệ.

---

### 4.4. Quản lý Môn học & Học phần *(Chỉ dành cho Admin)*
*(Giảng viên sẽ không thấy mục này trên thanh menu điều hướng).*

* Quản lý danh mục các môn học trong chương trình đào tạo của nhà trường:
  * **Mã Môn học:** Mã học phần định danh (ví dụ: `JAVA02`, `CSDL03`, `CTDL01`).
  * **Tên Môn học:** Tên học phần đầy đủ.
  * **Số tín chỉ:** Số tín chỉ của học phần (từ 1 đến 10 tín chỉ).
  * **Hệ số môn:** Hệ số nhân điểm (mặc định là `1.0`).
* Hỗ trợ các thao tác Thêm môn mới, Cập nhật thông tin môn học, Xóa môn và Làm mới.

---

### 4.5. Quản lý Điểm số & Thi *(Dành cho cả Admin và Giảng viên)*
Đây là phân hệ làm việc trọng tâm của **Giảng viên**:

* **Quy trình nhập điểm thi:**
  1. Chọn **Sinh viên** cần vào điểm từ danh sách gợi ý.
  2. Chọn **Môn học** cần ghi nhận điểm.
  3. Nhập điểm thành phần (thang điểm chuẩn từ 0.0 đến 10.0):
     * **Chuyên cần:** Tỷ trọng 10%.
     * **Giữa kỳ:** Tỷ trọng 30%.
     * **Thi cuối kỳ:** Tỷ trọng 60%.
  4. Hệ thống sẽ **tự động tính điểm trung bình (ĐTB)**, **tự động xếp loại học lực** và **xét điều kiện Đạt/Học lại** ngay lập tức.
  5. Bấm **Lưu điểm** (Hệ thống tự nhận diện: Nếu sinh viên chưa có điểm môn này sẽ tự động Thêm mới; nếu đã có điểm sẽ tự động Cập nhật lại).
* **Xóa điểm:** Chọn dòng điểm trên bảng -> Bấm **Xóa điểm**.
* **Bộ lọc tra cứu nhanh:**
  * Lọc theo Môn học.
  * Lọc theo Lớp sinh viên.
  * Tìm kiếm tức thời theo Tên hoặc Mã sinh viên.

---

### 4.6. Báo cáo & Thống kê học tập *(Dành cho cả Admin và Giảng viên)*
Cung cấp công cụ phân tích dữ liệu học vụ mạnh mẽ thông qua 3 tab nghiệp vụ:

#### 1. Thống kê Học lực theo Lớp
* Bảng thống kê chi tiết số lượng sinh viên đạt loại: **Xuất sắc**, **Giỏi**, **Khá**, **Trung bình**, **Yếu** của từng lớp học.
* Tự động tính toán **Tỷ lệ Đạt (%)** giúp giảng viên và ban giám hiệu đánh giá chất lượng dạy và học.

#### 2. Tra cứu Bảng điểm Cá nhân
* Cho phép chọn một sinh viên bất kỳ trong trường để tra cứu toàn bộ quá trình học tập:
  * Hiển thị bảng điểm tất cả các môn đã học kèm số tín chỉ, điểm thành phần, ĐTB môn và trạng thái Đạt/Học lại.
  * Thanh tổng kết phía dưới tự động tính toán:
    * **Tổng số tín chỉ đã tích lũy**.
    * **Điểm trung bình tích lũy toàn khóa (GPA theo hệ 10)** có trọng số theo số tín chỉ.
    * **Xếp loại học lực tích lũy chung toàn khóa**.

#### 3. Danh sách xét Học bổng
* Tự động lọc và xếp thứ tự các sinh viên đủ tiêu chuẩn xét học bổng khuyến khích học tập theo quy chế:
  * Điểm trung bình tích lũy toàn khóa đạt từ **8.0 trở lên**.
  * Không có bất kỳ môn học nào bị điểm dưới 5.0 (không thi lại / nợ môn).
* Tự động phân loại danh hiệu: **Học bổng Xuất sắc** (GPA >= 9.0) hoặc **Học bổng Khá/Giỏi** (8.0 <= GPA < 9.0).

---

### 4.7. Quản trị Tài khoản người dùng *(Chỉ dành riêng cho Admin)*
*(Giảng viên sẽ không thấy mục này trên thanh menu điều hướng).*

* **Quy tắc bảo mật:** Hệ thống duy trì duy nhất **1 tài khoản Admin**.
* **Cấp tài khoản mới cho Giảng viên:**
  1. Nhập **Tên đăng nhập** (không được trùng).
  2. Nhập **Mật khẩu** ban đầu.
  3. Nhập **Họ và tên** của giảng viên.
  4. Vai trò được cố định là **Giảng viên**.
  5. Bấm **Thêm tài khoản**.
* **Cập nhật / Đổi mật khẩu:** Chọn tài khoản trong danh sách -> Nhập mật khẩu mới -> Bấm **Cập nhật**.
* **Xóa tài khoản:** Chọn tài khoản giảng viên không còn công tác -> Bấm **Xóa tài khoản** *(Hệ thống có cơ chế an toàn: Tuyệt đối không cho phép xóa tài khoản Admin).*

---

## 5. Quy chuẩn tính điểm & Xếp loại tự động

### Công thức tính điểm trung bình học phần:
```text
Điểm TB môn = (Chuyên cần * 0.1) + (Giữa kỳ * 0.3) + (Thi cuối kỳ * 0.6)
```

### Thang xếp loại học phần:
* **ĐTB >= 9.0:** Xuất sắc
* **8.0 <= ĐTB < 9.0:** Giỏi
* **6.5 <= ĐTB < 8.0:** Khá
* **5.0 <= ĐTB < 6.5:** Trung bình
* **ĐTB < 5.0:** Yếu

### Quy chế xét Đạt môn:
* Sinh viên được công nhận **Đạt** môn học khi đồng thời thỏa mãn 2 điều kiện:
  1. `Điểm TB môn >= 5.0`
  2. `Điểm Thi cuối kỳ >= 4.0` *(không bị điểm liệt)*.
* Nếu không thỏa mãn một trong hai điều kiện trên, sinh viên phải **Học lại**.

---

## 6. Xử lý sự cố thường gặp (Troubleshooting)

| Sự cố | Nguyên nhân | Cách khắc phục |
| :--- | :--- | :--- |
| **Không mở được ứng dụng khi bấm `run.bat`** | Máy tính chưa cài đặt Java hoặc phiên bản Java cũ hơn 21. | Cài đặt JDK 21 LTS từ Oracle hoặc OpenJDK và tích chọn tùy chọn "Add to PATH". |
| **Báo lỗi `Communications link failure` khi đăng nhập** | Dịch vụ MySQL trong XAMPP chưa được bật. | Mở bảng điều khiển XAMPP Control Panel, bấm nút **Start** ở dòng MySQL. |
| **Lỗi `Trùng mã sinh viên / Mã môn / Mã lớp`** | Khóa chính đã tồn tại trong CSDL. | Kiểm tra lại bảng dữ liệu và đổi một mã định danh khác chưa từng sử dụng. |
| **Lỗi định dạng ngày sinh** | Nhập ngày sinh sai quy cách. | Nhập đúng định dạng `yyyy-MM-dd` (ví dụ: `2004-03-15`). |
| **Điểm nhập không hợp lệ** | Nhập điểm là chữ cái hoặc điểm ngoài khoảng 0.0 - 10.0. | Chỉ nhập số thực từ `0.0` đến `10.0` (dùng dấu chấm thập phân `.`). |
| **Giảng viên không thấy nút Thêm/Sửa/Xóa sinh viên** | Đây là thiết kế bảo mật phân quyền. | Giảng viên chỉ có quyền tra cứu hồ sơ sinh viên; chỉ tài khoản **Admin** mới có quyền chỉnh sửa hồ sơ. |
