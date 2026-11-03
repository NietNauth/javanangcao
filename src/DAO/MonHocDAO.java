package DAO;

import Connection.JdbcHelper;
import Model.MonHoc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MonHocDAO {

    public List<MonHoc> getAll() {
        List<MonHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM monhoc ORDER BY MaMon";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MonHoc(
                    rs.getString("MaMon"),
                    rs.getString("TenMon"),
                    rs.getInt("SoTinChi"),
                    rs.getDouble("HeSo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(MonHoc mh) {
        String sql = "INSERT INTO monhoc (MaMon, TenMon, SoTinChi, HeSo) VALUES (?, ?, ?, ?)";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mh.getMaMon());
            ps.setString(2, mh.getTenMon());
            ps.setInt(3, mh.getSoTinChi());
            ps.setDouble(4, mh.getHeSo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(MonHoc mh) {
        String sql = "UPDATE monhoc SET TenMon = ?, SoTinChi = ?, HeSo = ? WHERE MaMon = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mh.getTenMon());
            ps.setInt(2, mh.getSoTinChi());
            ps.setDouble(3, mh.getHeSo());
            ps.setString(4, mh.getMaMon());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maMon) {
        String sql = "DELETE FROM monhoc WHERE MaMon = ?";
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maMon);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
