-- phpMyAdmin SQL Dump
-- Hệ thống Quản lý Sinh viên và Điểm số
-- Cơ sở dữ liệu: `baitaplon-javaswing`

CREATE DATABASE IF NOT EXISTS `baitaplon-javaswing` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `baitaplon-javaswing`;

-- 1. Bảng Tài khoản người dùng
DROP TABLE IF EXISTS `diem`;
DROP TABLE IF EXISTS `sinhvien`;
DROP TABLE IF EXISTS `monhoc`;
DROP TABLE IF EXISTS `lop`;
DROP TABLE IF EXISTS `khoa`;
DROP TABLE IF EXISTS `taikhoan`;

CREATE TABLE `taikhoan` (
  `Tendangnhap` VARCHAR(50) NOT NULL PRIMARY KEY,
  `Matkhau` VARCHAR(100) NOT NULL,
  `Hoten` VARCHAR(100) NOT NULL,
  `Vaitro` VARCHAR(20) NOT NULL DEFAULT 'Giảng viên'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `taikhoan` (`Tendangnhap`, `Matkhau`, `Hoten`, `Vaitro`) VALUES
('admin', '123', 'Quản trị viên', 'Admin'),
('giangvien', '123', 'ThS. Nguyễn Văn A', 'Giảng viên');

-- 2. Bảng Khoa
CREATE TABLE `khoa` (
  `MaKhoa` VARCHAR(20) NOT NULL PRIMARY KEY,
  `TenKhoa` VARCHAR(100) NOT NULL,
  `SDT` VARCHAR(20) DEFAULT NULL,
  `Email` VARCHAR(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `khoa` (`MaKhoa`, `TenKhoa`, `SDT`, `Email`) VALUES
('CNTT', 'Công nghệ thông tin', '024.3869.1111', 'cntt@university.edu.vn'),
('KT', 'Kinh tế & Quản lý', '024.3869.2222', 'kt@university.edu.vn'),
('NNA', 'Ngôn ngữ Anh', '024.3869.3333', 'nna@university.edu.vn'),
('DTVT', 'Điện tử viễn thông', '024.3869.4444', 'dtvt@university.edu.vn');

-- 3. Bảng Lớp học
CREATE TABLE `lop` (
  `MaLop` VARCHAR(20) NOT NULL PRIMARY KEY,
  `TenLop` VARCHAR(100) NOT NULL,
  `MaKhoa` VARCHAR(20) NOT NULL,
  `NienKhoa` VARCHAR(20) NOT NULL,
  CONSTRAINT `fk_lop_khoa` FOREIGN KEY (`MaKhoa`) REFERENCES `khoa` (`MaKhoa`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `lop` (`MaLop`, `TenLop`, `MaKhoa`, `NienKhoa`) VALUES
('DHKTPM18A', 'Kỹ thuật phần mềm K18A', 'CNTT', '2022-2026'),
('DHKHMT18A', 'Khoa học máy tính K18A', 'CNTT', '2022-2026'),
('DHQTKD18B', 'Quản trị kinh doanh K18B', 'KT', '2022-2026'),
('DHNNA18C', 'Ngôn ngữ Anh K18C', 'NNA', '2022-2026');

-- 4. Bảng Sinh viên
CREATE TABLE `sinhvien` (
  `MaSV` VARCHAR(20) NOT NULL PRIMARY KEY,
  `HoTen` VARCHAR(100) NOT NULL,
  `NgaySinh` DATE NOT NULL,
  `GioiTinh` VARCHAR(10) NOT NULL,
  `DiaChi` VARCHAR(255) DEFAULT NULL,
  `SDT` VARCHAR(20) DEFAULT NULL,
  `Email` VARCHAR(100) DEFAULT NULL,
  `MaLop` VARCHAR(20) NOT NULL,
  CONSTRAINT `fk_sv_lop` FOREIGN KEY (`MaLop`) REFERENCES `lop` (`MaLop`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `sinhvien` (`MaSV`, `HoTen`, `NgaySinh`, `GioiTinh`, `DiaChi`, `SDT`, `Email`, `MaLop`) VALUES
('SV001', 'Nguyễn Quang Huy', '2004-03-15', 'Nam', 'Hà Nội', '0912345678', 'huy.nq@sinhvien.edu.vn', 'DHKTPM18A'),
('SV002', 'Trần Thị Mai', '2004-06-20', 'Nữ', 'Đà Nẵng', '0912345679', 'mai.tt@sinhvien.edu.vn', 'DHKTPM18A'),
('SV003', 'Lê Hoàng Long', '2004-09-10', 'Nam', 'TP. Hồ Chí Minh', '0912345680', 'long.lh@sinhvien.edu.vn', 'DHKHMT18A'),
('SV004', 'Phạm Minh Trang', '2004-12-05', 'Nữ', 'Hải Phòng', '0912345681', 'trang.pm@sinhvien.edu.vn', 'DHQTKD18B'),
('SV005', 'Hoàng Gia Bảo', '2004-04-25', 'Nam', 'Cần Thơ', '0912345682', 'bao.hg@sinhvien.edu.vn', 'DHNNA18C');

-- 5. Bảng Môn học
CREATE TABLE `monhoc` (
  `MaMon` VARCHAR(20) NOT NULL PRIMARY KEY,
  `TenMon` VARCHAR(100) NOT NULL,
  `SoTinChi` INT NOT NULL CHECK (`SoTinChi` > 0),
  `HeSo` DECIMAL(3,2) NOT NULL DEFAULT 1.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `monhoc` (`MaMon`, `TenMon`, `SoTinChi`, `HeSo`) VALUES
('CSD01', 'Cấu trúc dữ liệu & Giải thuật', 3, 1.00),
('JAVA02', 'Lập trình Java nâng cao', 4, 1.00),
('CSDL03', 'Cơ sở dữ liệu nâng cao', 3, 1.00),
('MMT04', 'Mạng máy tính', 3, 1.00),
('TA05', 'Tiếng Anh chuyên ngành', 2, 1.00);

-- 6. Bảng Điểm thi
CREATE TABLE `diem` (
  `ID` INT AUTO_INCREMENT PRIMARY KEY,
  `MaSV` VARCHAR(20) NOT NULL,
  `MaMon` VARCHAR(20) NOT NULL,
  `DiemChuyenCan` DECIMAL(4,2) NOT NULL DEFAULT 0.00 CHECK (`DiemChuyenCan` BETWEEN 0 AND 10),
  `DiemGiuaKy` DECIMAL(4,2) NOT NULL DEFAULT 0.00 CHECK (`DiemGiuaKy` BETWEEN 0 AND 10),
  `DiemThi` DECIMAL(4,2) NOT NULL DEFAULT 0.00 CHECK (`DiemThi` BETWEEN 0 AND 10),
  `DiemTB` DECIMAL(4,2) NOT NULL DEFAULT 0.00,
  `XepLoai` VARCHAR(20) DEFAULT 'Yếu',
  `TrangThai` VARCHAR(20) DEFAULT 'Học lại',
  UNIQUE KEY `uq_sv_mon` (`MaSV`, `MaMon`),
  CONSTRAINT `fk_diem_sv` FOREIGN KEY (`MaSV`) REFERENCES `sinhvien` (`MaSV`) ON DELETE CASCADE,
  CONSTRAINT `fk_diem_mon` FOREIGN KEY (`MaMon`) REFERENCES `monhoc` (`MaMon`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `diem` (`MaSV`, `MaMon`, `DiemChuyenCan`, `DiemGiuaKy`, `DiemThi`, `DiemTB`, `XepLoai`, `TrangThai`) VALUES
('SV001', 'JAVA02', 9.0, 8.5, 9.0, 8.85, 'Giỏi', 'Đạt'),
('SV001', 'CSDL03', 8.5, 7.5, 8.0, 7.90, 'Khá', 'Đạt'),
('SV002', 'JAVA02', 10.0, 9.0, 9.5, 9.40, 'Xuất sắc', 'Đạt'),
('SV002', 'CSD01', 8.0, 6.5, 7.0, 6.95, 'Trung bình', 'Đạt'),
('SV003', 'JAVA02', 7.0, 5.0, 4.0, 4.60, 'Yếu', 'Học lại'),
('SV004', 'TA05', 9.0, 8.0, 8.5, 8.40, 'Khá', 'Đạt');
