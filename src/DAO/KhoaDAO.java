package DAO;

import Connection.JdbcHelper;
import Model.Khoa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhoaDAO {

    public List<Khoa> getAll() {
        List<Khoa> list = new ArrayList<>();
        String sql = "SELECT * FROM khoa ORDER BY MaKhoa";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Khoa(
                    rs.getString("MaKhoa"),
                    rs.getString("TenKhoa"),
                    rs.getString("SDT"),
                    rs.getString("Email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Khoa k) {
        String sql = "INSERT INTO khoa (MaKhoa, TenKhoa, SDT, Email) VALUES (?, ?, ?, ?)";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, k.getMaKhoa());
            ps.setString(2, k.getTenKhoa());
            ps.setString(3, k.getSdt());
            ps.setString(4, k.getEmail());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Khoa k) {
        String sql = "UPDATE khoa SET TenKhoa = ?, SDT = ?, Email = ? WHERE MaKhoa = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, k.getTenKhoa());
            ps.setString(2, k.getSdt());
            ps.setString(3, k.getEmail());
            ps.setString(4, k.getMaKhoa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maKhoa) {
        String sql = "DELETE FROM khoa WHERE MaKhoa = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKhoa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
