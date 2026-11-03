package DAO;

import Connection.JdbcHelper;
import Model.Lop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LopDAO {

    public List<Lop> getAll() {
        List<Lop> list = new ArrayList<>();
        String sql = "SELECT * FROM lop ORDER BY MaLop";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Lop(
                    rs.getString("MaLop"),
                    rs.getString("TenLop"),
                    rs.getString("MaKhoa"),
                    rs.getString("NienKhoa")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Lop> getByKhoa(String maKhoa) {
        List<Lop> list = new ArrayList<>();
        String sql = "SELECT * FROM lop WHERE MaKhoa = ? ORDER BY MaLop";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKhoa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Lop(
                        rs.getString("MaLop"),
                        rs.getString("TenLop"),
                        rs.getString("MaKhoa"),
                        rs.getString("NienKhoa")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Lop l) {
        String sql = "INSERT INTO lop (MaLop, TenLop, MaKhoa, NienKhoa) VALUES (?, ?, ?, ?)";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getMaLop());
            ps.setString(2, l.getTenLop());
            ps.setString(3, l.getMaKhoa());
            ps.setString(4, l.getNienKhoa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Lop l) {
        String sql = "UPDATE lop SET TenLop = ?, MaKhoa = ?, NienKhoa = ? WHERE MaLop = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getTenLop());
            ps.setString(2, l.getMaKhoa());
            ps.setString(3, l.getNienKhoa());
            ps.setString(4, l.getMaLop());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maLop) {
        String sql = "DELETE FROM lop WHERE MaLop = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
