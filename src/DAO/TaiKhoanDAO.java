package DAO;

import Connection.JdbcHelper;
import Model.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public boolean checkLogin(String user, String pass) {
        String sql = "SELECT * FROM taikhoan WHERE Tendangnhap = ? AND Matkhau = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, user);
            pst.setString(2, pass);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public TaiKhoan getTaiKhoan(String user, String pass) {
        String sql = "SELECT * FROM taikhoan WHERE Tendangnhap = ? AND Matkhau = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, user);
            pst.setString(2, pass);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoan(
                        rs.getString("Tendangnhap"),
                        rs.getString("Matkhau"),
                        rs.getString("Hoten"),
                        rs.getString("Vaitro")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan ORDER BY Vaitro, Tendangnhap";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TaiKhoan(
                    rs.getString("Tendangnhap"),
                    rs.getString("Matkhau"),
                    rs.getString("Hoten"),
                    rs.getString("Vaitro")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean hasAdminAccount() {
        String sql = "SELECT COUNT(*) FROM taikhoan WHERE Vaitro = 'Admin'";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(TaiKhoan tk) {
        // Chỉ cho phép duy nhất 1 tài khoản Admin trong toàn bộ hệ thống
        if ("Admin".equalsIgnoreCase(tk.getVaiTro()) && hasAdminAccount()) {
            return false;
        }

        String sql = "INSERT INTO taikhoan (Tendangnhap, Matkhau, Hoten, Vaitro) VALUES (?, ?, ?, ?)";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getHoTen());
            ps.setString(4, tk.getVaiTro());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(TaiKhoan tk) {
        // Không cho phép chuyển tài khoản Giảng viên thành Admin nếu đã có Admin
        String sqlCheck = "SELECT Vaitro FROM taikhoan WHERE Tendangnhap = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setString(1, tk.getTenDangNhap());
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    String oldRole = rs.getString("Vaitro");
                    if ("Admin".equalsIgnoreCase(oldRole) && !"Admin".equalsIgnoreCase(tk.getVaiTro())) {
                        // Không cho phép hạ quyền của Admin duy nhất
                        return false;
                    }
                    if (!"Admin".equalsIgnoreCase(oldRole) && "Admin".equalsIgnoreCase(tk.getVaiTro())) {
                        // Không cho phép thăng quyền thành Admin thứ 2
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String sql = "UPDATE taikhoan SET Matkhau = ?, Hoten = ?, Vaitro = ? WHERE Tendangnhap = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getMatKhau());
            ps.setString(2, tk.getHoTen());
            ps.setString(3, tk.getVaiTro());
            ps.setString(4, tk.getTenDangNhap());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String tenDangNhap) {
        // Bảo vệ tài khoản Admin: Không cho phép xóa tài khoản Admin
        String sqlCheck = "SELECT Vaitro FROM taikhoan WHERE Tendangnhap = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setString(1, tenDangNhap);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && "Admin".equalsIgnoreCase(rs.getString("Vaitro"))) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String sql = "DELETE FROM taikhoan WHERE Tendangnhap = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
