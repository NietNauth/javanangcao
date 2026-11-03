package DAO;

import Connection.JdbcHelper;
import Model.SinhVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {

    public List<SinhVien> getAll() {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT * FROM sinhvien ORDER BY MaSV";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SinhVien> search(String keyword, String maLop) {
        List<SinhVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM sinhvien WHERE 1=1 ");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (MaSV LIKE ? OR HoTen LIKE ? OR SDT LIKE ? OR Email LIKE ?) ");
        }
        if (maLop != null && !maLop.isEmpty() && !maLop.equals("Tất cả")) {
            sql.append("AND MaLop = ? ");
        }
        sql.append("ORDER BY MaSV");

        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            if (maLop != null && !maLop.isEmpty() && !maLop.equals("Tất cả")) {
                ps.setString(paramIndex++, maLop);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public SinhVien getByMaSV(String maSV) {
        String sql = "SELECT * FROM sinhvien WHERE MaSV = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(SinhVien sv) {
        String sql = "INSERT INTO sinhvien (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SDT, Email, MaLop) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sv.getMaSV());
            ps.setString(2, sv.getHoTen());
            ps.setDate(3, sv.getNgaySinh());
            ps.setString(4, sv.getGioiTinh());
            ps.setString(5, sv.getDiaChi());
            ps.setString(6, sv.getSdt());
            ps.setString(7, sv.getEmail());
            ps.setString(8, sv.getMaLop());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(SinhVien sv) {
        String sql = "UPDATE sinhvien SET HoTen = ?, NgaySinh = ?, GioiTinh = ?, DiaChi = ?, SDT = ?, Email = ?, MaLop = ? WHERE MaSV = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sv.getHoTen());
            ps.setDate(2, sv.getNgaySinh());
            ps.setString(3, sv.getGioiTinh());
            ps.setString(4, sv.getDiaChi());
            ps.setString(5, sv.getSdt());
            ps.setString(6, sv.getEmail());
            ps.setString(7, sv.getMaLop());
            ps.setString(8, sv.getMaSV());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maSV) {
        String sql = "DELETE FROM sinhvien WHERE MaSV = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private SinhVien extractFromResultSet(ResultSet rs) throws Exception {
        return new SinhVien(
            rs.getString("MaSV"),
            rs.getString("HoTen"),
            rs.getDate("NgaySinh"),
            rs.getString("GioiTinh"),
            rs.getString("DiaChi"),
            rs.getString("SDT"),
            rs.getString("Email"),
            rs.getString("MaLop")
        );
    }
}
