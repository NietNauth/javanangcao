package DAO;

import Connection.JdbcHelper;
import Model.ThongKeHocTap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDAO {

    public Map<String, Integer> getTongQuan() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("soSV", 0);
        stats.put("soLop", 0);
        stats.put("soKhoa", 0);
        stats.put("soMon", 0);

        try (Connection con = JdbcHelper.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM sinhvien");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("soSV", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM lop");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("soLop", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM khoa");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("soKhoa", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM monhoc");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("soMon", rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public List<ThongKeHocTap> getThongKeTheoLop() {
        List<ThongKeHocTap> list = new ArrayList<>();
        String sql = """
            SELECT l.MaLop, l.TenLop,
                   COUNT(DISTINCT sv.MaSV) AS TongSV,
                   SUM(CASE WHEN d.XepLoai = 'Xuất sắc' THEN 1 ELSE 0 END) AS SoXuatSac,
                   SUM(CASE WHEN d.XepLoai = 'Giỏi' THEN 1 ELSE 0 END) AS SoGioi,
                   SUM(CASE WHEN d.XepLoai = 'Khá' THEN 1 ELSE 0 END) AS SoKha,
                   SUM(CASE WHEN d.XepLoai = 'Trung bình' THEN 1 ELSE 0 END) AS SoTrungBinh,
                   SUM(CASE WHEN d.XepLoai = 'Yếu' THEN 1 ELSE 0 END) AS SoYeu
            FROM lop l
            LEFT JOIN sinhvien sv ON l.MaLop = sv.MaLop
            LEFT JOIN diem d ON sv.MaSV = d.MaSV
            GROUP BY l.MaLop, l.TenLop
            ORDER BY l.MaLop
        """;

        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ThongKeHocTap tk = new ThongKeHocTap();
                tk.setMaLop(rs.getString("MaLop"));
                tk.setTenLop(rs.getString("TenLop"));
                tk.setTongSV(rs.getInt("TongSV"));
                tk.setSoXuatSac(rs.getInt("SoXuatSac"));
                tk.setSoGioi(rs.getInt("SoGioi"));
                tk.setSoKha(rs.getInt("SoKha"));
                tk.setSoTrungBinh(rs.getInt("SoTrungBinh"));
                tk.setSoYeu(rs.getInt("SoYeu"));

                int tongDiem = tk.getSoXuatSac() + tk.getSoGioi() + tk.getSoKha() + tk.getSoTrungBinh() + tk.getSoYeu();
                if (tongDiem > 0) {
                    double dat = (double) (tongDiem - tk.getSoYeu()) / tongDiem * 100.0;
                    tk.setTiLeDat(Math.round(dat * 10.0) / 10.0);
                } else {
                    tk.setTiLeDat(100.0);
                }
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getDanhSachHocBong() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT sv.MaSV, sv.HoTen, sv.MaLop,
                   ROUND(AVG(d.DiemTB), 2) AS DTBTichLuy,
                   COUNT(d.ID) AS SoMonDaHoc
            FROM sinhvien sv
            JOIN diem d ON sv.MaSV = d.MaSV
            GROUP BY sv.MaSV, sv.HoTen, sv.MaLop
            HAVING AVG(d.DiemTB) >= 8.0 AND MIN(d.DiemTB) >= 5.0
            ORDER BY DTBTichLuy DESC
        """;
        try (Connection con = JdbcHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int stt = 1;
            while (rs.next()) {
                double dtb = rs.getDouble("DTBTichLuy");
                String loaiHB = (dtb >= 9.0) ? "Học bổng Xuất sắc" : "Học bổng Khá/Giỏi";
                list.add(new Object[]{
                    stt++,
                    rs.getString("MaSV"),
                    rs.getString("HoTen"),
                    rs.getString("MaLop"),
                    dtb,
                    rs.getInt("SoMonDaHoc"),
                    loaiHB
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
