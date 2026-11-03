package DAO;

import Connection.JdbcHelper;
import Model.Diem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DiemDAO {

    public List<Diem> getAll() {
        List<Diem> list = new ArrayList<>();
        String sql = """
            SELECT d.*, sv.HoTen AS HoTenSV, mh.TenMon, mh.SoTinChi
            FROM diem d
            JOIN sinhvien sv ON d.MaSV = sv.MaSV
            JOIN monhoc mh ON d.MaMon = mh.MaMon
            ORDER BY d.ID DESC
        """;
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

    public List<Diem> filter(String maMon, String maLop, String keyword) {
        List<Diem> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT d.*, sv.HoTen AS HoTenSV, mh.TenMon, mh.SoTinChi
            FROM diem d
            JOIN sinhvien sv ON d.MaSV = sv.MaSV
            JOIN monhoc mh ON d.MaMon = mh.MaMon
            WHERE 1=1
        """);

        if (maMon != null && !maMon.isEmpty() && !maMon.equals("Tất cả")) {
            sql.append(" AND d.MaMon = ?");
        }
        if (maLop != null && !maLop.isEmpty() && !maLop.equals("Tất cả")) {
            sql.append(" AND sv.MaLop = ?");
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (d.MaSV LIKE ? OR sv.HoTen LIKE ?)");
        }
        sql.append(" ORDER BY d.ID DESC");

        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (maMon != null && !maMon.isEmpty() && !maMon.equals("Tất cả")) {
                ps.setString(idx++, maMon);
            }
            if (maLop != null && !maLop.isEmpty() && !maLop.equals("Tất cả")) {
                ps.setString(idx++, maLop);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                String pat = "%" + keyword.trim() + "%";
                ps.setString(idx++, pat);
                ps.setString(idx++, pat);
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

    public List<Diem> getBangDiemSinhVien(String maSV) {
        List<Diem> list = new ArrayList<>();
        String sql = """
            SELECT d.*, sv.HoTen AS HoTenSV, mh.TenMon, mh.SoTinChi
            FROM diem d
            JOIN sinhvien sv ON d.MaSV = sv.MaSV
            JOIN monhoc mh ON d.MaMon = mh.MaMon
            WHERE d.MaSV = ?
            ORDER BY mh.MaMon
        """;
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSV);
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

    public boolean insertOrUpdate(Diem d) {
        d.tinhDiemTBVaXepLoai();
        String sql = """
            INSERT INTO diem (MaSV, MaMon, DiemChuyenCan, DiemGiuaKy, DiemThi, DiemTB, XepLoai, TrangThai)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                DiemChuyenCan = VALUES(DiemChuyenCan),
                DiemGiuaKy = VALUES(DiemGiuaKy),
                DiemThi = VALUES(DiemThi),
                DiemTB = VALUES(DiemTB),
                XepLoai = VALUES(XepLoai),
                TrangThai = VALUES(TrangThai)
        """;
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getMaSV());
            ps.setString(2, d.getMaMon());
            ps.setDouble(3, d.getDiemChuyenCan());
            ps.setDouble(4, d.getDiemGiuaKy());
            ps.setDouble(5, d.getDiemThi());
            ps.setDouble(6, d.getDiemTB());
            ps.setString(7, d.getXepLoai());
            ps.setString(8, d.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM diem WHERE ID = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Diem extractFromResultSet(ResultSet rs) throws Exception {
        Diem d = new Diem();
        d.setId(rs.getInt("ID"));
        d.setMaSV(rs.getString("MaSV"));
        d.setMaMon(rs.getString("MaMon"));
        d.setDiemChuyenCan(rs.getDouble("DiemChuyenCan"));
        d.setDiemGiuaKy(rs.getDouble("DiemGiuaKy"));
        d.setDiemThi(rs.getDouble("DiemThi"));
        d.setDiemTB(rs.getDouble("DiemTB"));
        d.setXepLoai(rs.getString("XepLoai"));
        d.setTrangThai(rs.getString("TrangThai"));
        d.setHoTenSV(rs.getString("HoTenSV"));
        d.setTenMon(rs.getString("TenMon"));
        d.setSoTinChi(rs.getInt("SoTinChi"));
        return d;
    }
}
