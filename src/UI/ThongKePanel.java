package UI;

import DAO.DiemDAO;
import DAO.SinhVienDAO;
import DAO.ThongKeDAO;
import Model.Diem;
import Model.SinhVien;
import Model.ThongKeHocTap;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ThongKePanel extends JPanel {

    private ThongKeDAO thongKeDAO = new ThongKeDAO();
    private DiemDAO diemDAO = new DiemDAO();
    private SinhVienDAO svDAO = new SinhVienDAO();

    // Tab 1: Thong ke theo lop
    private JTable tblLopStat;
    private DefaultTableModel modelLopStat;

    // Tab 2: Bang diem ca nhan
    private JComboBox<SinhVien> cboSinhVien;
    private JLabel lblTongTinChi, lblDTBTichLuy, lblXepLoaiChung;
    private JTable tblBangDiem;
    private DefaultTableModel modelBangDiem;

    // Tab 3: Hoc bong
    private JTable tblHocBong;
    private DefaultTableModel modelHocBong;

    public ThongKePanel() {
        initComponents();
        loadTab1();
        loadComboSV();
        loadTab3();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("Thống kê Học lực theo Lớp", createTabThongKeLop());
        tabbedPane.addTab("Tra cứu Bảng điểm Cá nhân", createTabBangDiem());
        tabbedPane.addTab("Danh sách xét Học bổng", createTabHocBong());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTabThongKeLop() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnReload = new JButton("Cập nhật số liệu thống kê");
        btnReload.addActionListener(e -> loadTab1());
        pnlTop.add(btnReload);
        pnl.add(pnlTop, BorderLayout.NORTH);

        modelLopStat = new DefaultTableModel(new String[]{
            "Mã Lớp", "Tên Lớp", "Tổng SV", "Xuất Sắc", "Giỏi", "Khá", "Trung Bình", "Yếu", "Tỷ lệ Đạt (%)"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLopStat = new JTable(modelLopStat);
        tblLopStat.setRowHeight(25);

        pnl.add(new JScrollPane(tblLopStat), BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createTabBangDiem() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top: Select student
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        pnlTop.add(new JLabel("Chọn Sinh viên cần tra cứu:"));
        cboSinhVien = new JComboBox<>();
        cboSinhVien.setPreferredSize(new Dimension(300, 28));
        cboSinhVien.addActionListener(e -> traCuuBangDiem());
        pnlTop.add(cboSinhVien);

        JButton btnXem = new JButton("Xem bảng điểm");
        btnXem.addActionListener(e -> traCuuBangDiem());
        pnlTop.add(btnXem);

        pnl.add(pnlTop, BorderLayout.NORTH);

        // Center: Transcript Table
        modelBangDiem = new DefaultTableModel(new String[]{
            "Mã Môn", "Tên Môn Học", "Số Tín Chỉ", "Chuyên cần", "Giữa kỳ", "Thi", "ĐTB Môn", "Xếp Loại", "Trạng Thái"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblBangDiem = new JTable(modelBangDiem);
        tblBangDiem.setRowHeight(25);
        pnl.add(new JScrollPane(tblBangDiem), BorderLayout.CENTER);

        // Bottom: Summary stats
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        lblTongTinChi = new JLabel("Tổng số tín chỉ: 0");
        lblTongTinChi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongTinChi.setForeground(new Color(41, 128, 185));

        lblDTBTichLuy = new JLabel("ĐTB tích lũy: 0.0");
        lblDTBTichLuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDTBTichLuy.setForeground(new Color(39, 174, 96));

        lblXepLoaiChung = new JLabel("Xếp loại học lực: Chưa có");
        lblXepLoaiChung.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblXepLoaiChung.setForeground(new Color(142, 68, 173));

        pnlBottom.add(lblTongTinChi);
        pnlBottom.add(lblDTBTichLuy);
        pnlBottom.add(lblXepLoaiChung);

        pnl.add(pnlBottom, BorderLayout.SOUTH);
        return pnl;
    }

    private JPanel createTabHocBong() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblDesc = new JLabel("Tiêu chí xét học bổng: ĐTB Tích lũy >= 8.0 và không có môn nào bị điểm dưới 5.0");
        lblDesc.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblDesc.setForeground(new Color(113, 128, 150));
        pnlTop.add(lblDesc);

        JButton btnReloadHB = new JButton("Lập danh sách");
        btnReloadHB.addActionListener(e -> loadTab3());
        pnlTop.add(btnReloadHB);

        pnl.add(pnlTop, BorderLayout.NORTH);

        modelHocBong = new DefaultTableModel(new String[]{
            "STT", "Mã SV", "Họ và Tên", "Lớp", "ĐTB Tích Lũy", "Số môn đã học", "Mức Học Bổng Dự Kiến"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblHocBong = new JTable(modelHocBong);
        tblHocBong.setRowHeight(25);
        pnl.add(new JScrollPane(tblHocBong), BorderLayout.CENTER);

        return pnl;
    }

    private void loadTab1() {
        modelLopStat.setRowCount(0);
        for (ThongKeHocTap tk : thongKeDAO.getThongKeTheoLop()) {
            modelLopStat.addRow(new Object[]{
                tk.getMaLop(),
                tk.getTenLop(),
                tk.getTongSV(),
                tk.getSoXuatSac(),
                tk.getSoGioi(),
                tk.getSoKha(),
                tk.getSoTrungBinh(),
                tk.getSoYeu(),
                tk.getTiLeDat() + "%"
            });
        }
    }

    private void loadComboSV() {
        cboSinhVien.removeAllItems();
        for (SinhVien sv : svDAO.getAll()) {
            cboSinhVien.addItem(sv);
        }
        if (cboSinhVien.getItemCount() > 0) {
            traCuuBangDiem();
        }
    }

    private void traCuuBangDiem() {
        SinhVien sv = (SinhVien) cboSinhVien.getSelectedItem();
        if (sv == null) return;

        List<Diem> list = diemDAO.getBangDiemSinhVien(sv.getMaSV());
        modelBangDiem.setRowCount(0);

        int tongTC = 0;
        double tongDiemHeSo = 0;

        for (Diem d : list) {
            modelBangDiem.addRow(new Object[]{
                d.getMaMon(),
                d.getTenMon(),
                d.getSoTinChi(),
                d.getDiemChuyenCan(),
                d.getDiemGiuaKy(),
                d.getDiemThi(),
                d.getDiemTB(),
                d.getXepLoai(),
                d.getTrangThai()
            });

            tongTC += d.getSoTinChi();
            tongDiemHeSo += d.getDiemTB() * d.getSoTinChi();
        }

        double dtbTichLuy = (tongTC > 0) ? Math.round((tongDiemHeSo / tongTC) * 100.0) / 100.0 : 0.0;
        lblTongTinChi.setText("Tổng số tín chỉ: " + tongTC);
        lblDTBTichLuy.setText("ĐTB tích lũy: " + dtbTichLuy);

        String xepLoai;
        if (dtbTichLuy >= 9.0) xepLoai = "Xuất sắc";
        else if (dtbTichLuy >= 8.0) xepLoai = "Giỏi";
        else if (dtbTichLuy >= 6.5) xepLoai = "Khá";
        else if (dtbTichLuy >= 5.0) xepLoai = "Trung bình";
        else xepLoai = (tongTC > 0) ? "Yếu" : "Chưa có điểm";

        lblXepLoaiChung.setText("Xếp loại học lực: " + xepLoai);
    }

    private void loadTab3() {
        modelHocBong.setRowCount(0);
        for (Object[] row : thongKeDAO.getDanhSachHocBong()) {
            modelHocBong.addRow(row);
        }
    }
}
