package UI;

import DAO.DiemDAO;
import DAO.LopDAO;
import DAO.MonHocDAO;
import DAO.SinhVienDAO;
import Model.Diem;
import Model.Lop;
import Model.MonHoc;
import Model.SinhVien;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DiemPanel extends JPanel {

    private DiemDAO diemDAO = new DiemDAO();
    private SinhVienDAO svDAO = new SinhVienDAO();
    private MonHocDAO monDAO = new MonHocDAO();
    private LopDAO lopDAO = new LopDAO();

    // Form
    private JComboBox<SinhVien> cboSinhVien;
    private JComboBox<MonHoc> cboMonHoc;
    private JTextField txtChuyenCan, txtGiuaKy, txtThiCuoiKy, txtDiemTB, txtXepLoai, txtTrangThai;

    // Filters
    private JComboBox<String> cboLocMon, cboLocLop;
    private JTextField txtTimKiem;

    // Table
    private JTable tblDiem;
    private DefaultTableModel modelDiem;
    private int currentDiemId = -1;

    public DiemPanel() {
        initComponents();
        loadCombos();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        // Top Filter
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        pnlFilter.add(new JLabel("Môn học:"));
        cboLocMon = new JComboBox<>();
        cboLocMon.addItem("Tất cả");
        pnlFilter.add(cboLocMon);

        pnlFilter.add(new JLabel("Lớp:"));
        cboLocLop = new JComboBox<>();
        cboLocLop.addItem("Tất cả");
        pnlFilter.add(cboLocLop);

        pnlFilter.add(new JLabel("Mã SV / Tên:"));
        txtTimKiem = new JTextField(12);
        pnlFilter.add(txtTimKiem);

        JButton btnLoc = new JButton("Lọc điểm");
        btnLoc.addActionListener(e -> locDiem());
        pnlFilter.add(btnLoc);

        JButton btnResetLoc = new JButton("Tất cả");
        btnResetLoc.addActionListener(e -> {
            cboLocMon.setSelectedIndex(0);
            cboLocLop.setSelectedIndex(0);
            txtTimKiem.setText("");
            loadTableData();
        });
        pnlFilter.add(btnResetLoc);

        add(pnlFilter, BorderLayout.NORTH);

        // Center Split (Form left, Table right)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Nhập điểm thi"),
            new EmptyBorder(10, 15, 10, 15)
        ));
        pnlForm.setPreferredSize(new Dimension(380, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboSinhVien = new JComboBox<>();
        cboMonHoc = new JComboBox<>();
        txtChuyenCan = new JTextField("10.0");
        txtGiuaKy = new JTextField("8.0");
        txtThiCuoiKy = new JTextField("7.5");
        txtDiemTB = new JTextField();
        txtDiemTB.setEditable(false);
        txtDiemTB.setBackground(new Color(240, 244, 248));
        txtXepLoai = new JTextField();
        txtXepLoai.setEditable(false);
        txtXepLoai.setBackground(new Color(240, 244, 248));
        txtTrangThai = new JTextField();
        txtTrangThai.setEditable(false);
        txtTrangThai.setBackground(new Color(240, 244, 248));

        // Auto calculate preview when typing scores
        java.awt.event.KeyAdapter keyCalc = new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhDiemPreview();
            }
        };
        txtChuyenCan.addKeyListener(keyCalc);
        txtGiuaKy.addKeyListener(keyCalc);
        txtThiCuoiKy.addKeyListener(keyCalc);

        addField(pnlForm, gbc, 0, "Sinh viên (*):", cboSinhVien);
        addField(pnlForm, gbc, 1, "Môn học (*):", cboMonHoc);
        addField(pnlForm, gbc, 2, "Chuyên cần (10%):", txtChuyenCan);
        addField(pnlForm, gbc, 3, "Giữa kỳ (30%):", txtGiuaKy);
        addField(pnlForm, gbc, 4, "Thi cuối kỳ (60%):", txtThiCuoiKy);
        addField(pnlForm, gbc, 5, "Điểm TB (Tự động):", txtDiemTB);
        addField(pnlForm, gbc, 6, "Xếp loại (Tự động):", txtXepLoai);
        addField(pnlForm, gbc, 7, "Trạng thái:", txtTrangThai);

        // Buttons
        JPanel pnlBtns = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlBtns.setOpaque(false);
        JButton btnLuu = new JButton("Lưu điểm");
        JButton btnXoa = new JButton("Xóa điểm");
        JButton btnTinh = new JButton("Tính ĐTB");
        JButton btnLamMoi = new JButton("Làm mới");

        btnLuu.addActionListener(e -> luuDiem());
        btnXoa.addActionListener(e -> xoaDiem());
        btnTinh.addActionListener(e -> tinhDiemPreview());
        btnLamMoi.addActionListener(e -> clearForm());

        pnlBtns.add(btnLuu);
        pnlBtns.add(btnXoa);
        pnlBtns.add(btnTinh);
        pnlBtns.add(btnLamMoi);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        pnlForm.add(pnlBtns, gbc);

        // Table
        modelDiem = new DefaultTableModel(new String[]{
            "ID", "Mã SV", "Họ Tên", "Mã Môn", "Tên Môn", "Chuyên cần", "Giữa kỳ", "Thi", "ĐTB", "Xếp loại", "Trạng thái"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblDiem = new JTable(modelDiem);
        tblDiem.setRowHeight(25);
        tblDiem.getSelectionModel().addListSelectionListener(e -> {
            int row = tblDiem.getSelectedRow();
            if (row >= 0) {
                fillForm(row);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlForm, new JScrollPane(tblDiem));
        splitPane.setDividerLocation(380);
        add(splitPane, BorderLayout.CENTER);
    }

    private void addField(JPanel pnl, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.35;
        pnl.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        pnl.add(comp, gbc);
    }

    private void loadCombos() {
        cboSinhVien.removeAllItems();
        for (SinhVien sv : svDAO.getAll()) {
            cboSinhVien.addItem(sv);
        }

        cboMonHoc.removeAllItems();
        cboLocMon.removeAllItems();
        cboLocMon.addItem("Tất cả");
        for (MonHoc mh : monDAO.getAll()) {
            cboMonHoc.addItem(mh);
            cboLocMon.addItem(mh.getMaMon());
        }

        cboLocLop.removeAllItems();
        cboLocLop.addItem("Tất cả");
        for (Lop l : lopDAO.getAll()) {
            cboLocLop.addItem(l.getMaLop());
        }
    }

    public void loadTableData() {
        modelDiem.setRowCount(0);
        for (Diem d : diemDAO.getAll()) {
            modelDiem.addRow(new Object[]{
                d.getId(),
                d.getMaSV(),
                d.getHoTenSV(),
                d.getMaMon(),
                d.getTenMon(),
                d.getDiemChuyenCan(),
                d.getDiemGiuaKy(),
                d.getDiemThi(),
                d.getDiemTB(),
                d.getXepLoai(),
                d.getTrangThai()
            });
        }
    }

    private void locDiem() {
        String mon = (String) cboLocMon.getSelectedItem();
        String lop = (String) cboLocLop.getSelectedItem();
        String kw = txtTimKiem.getText();

        List<Diem> list = diemDAO.filter(mon, lop, kw);
        modelDiem.setRowCount(0);
        for (Diem d : list) {
            modelDiem.addRow(new Object[]{
                d.getId(),
                d.getMaSV(),
                d.getHoTenSV(),
                d.getMaMon(),
                d.getTenMon(),
                d.getDiemChuyenCan(),
                d.getDiemGiuaKy(),
                d.getDiemThi(),
                d.getDiemTB(),
                d.getXepLoai(),
                d.getTrangThai()
            });
        }
    }

    private void fillForm(int row) {
        currentDiemId = Integer.parseInt(modelDiem.getValueAt(row, 0).toString());
        String maSV = modelDiem.getValueAt(row, 1).toString();
        String maMon = modelDiem.getValueAt(row, 3).toString();

        for (int i = 0; i < cboSinhVien.getItemCount(); i++) {
            if (cboSinhVien.getItemAt(i).getMaSV().equals(maSV)) {
                cboSinhVien.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < cboMonHoc.getItemCount(); i++) {
            if (cboMonHoc.getItemAt(i).getMaMon().equals(maMon)) {
                cboMonHoc.setSelectedIndex(i);
                break;
            }
        }

        txtChuyenCan.setText(modelDiem.getValueAt(row, 5).toString());
        txtGiuaKy.setText(modelDiem.getValueAt(row, 6).toString());
        txtThiCuoiKy.setText(modelDiem.getValueAt(row, 7).toString());
        txtDiemTB.setText(modelDiem.getValueAt(row, 8).toString());
        txtXepLoai.setText(modelDiem.getValueAt(row, 9).toString());
        txtTrangThai.setText(modelDiem.getValueAt(row, 10).toString());
    }

    private void tinhDiemPreview() {
        try {
            double cc = Double.parseDouble(txtChuyenCan.getText().trim());
            double gk = Double.parseDouble(txtGiuaKy.getText().trim());
            double thi = Double.parseDouble(txtThiCuoiKy.getText().trim());

            Diem d = new Diem(0, "", "", cc, gk, thi);
            txtDiemTB.setText(String.valueOf(d.getDiemTB()));
            txtXepLoai.setText(d.getXepLoai());
            txtTrangThai.setText(d.getTrangThai());
        } catch (Exception ignored) {}
    }

    private void luuDiem() {
        SinhVien sv = (SinhVien) cboSinhVien.getSelectedItem();
        MonHoc mh = (MonHoc) cboMonHoc.getSelectedItem();
        if (sv == null || mh == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Sinh viên và Môn học!");
            return;
        }

        try {
            double cc = Double.parseDouble(txtChuyenCan.getText().trim());
            double gk = Double.parseDouble(txtGiuaKy.getText().trim());
            double thi = Double.parseDouble(txtThiCuoiKy.getText().trim());

            if (cc < 0 || cc > 10 || gk < 0 || gk > 10 || thi < 0 || thi > 10) {
                JOptionPane.showMessageDialog(this, "Điểm phải nằm trong thang điểm từ 0 đến 10!");
                return;
            }

            Diem d = new Diem(currentDiemId, sv.getMaSV(), mh.getMaMon(), cc, gk, thi);
            if (diemDAO.insertOrUpdate(d)) {
                JOptionPane.showMessageDialog(this, "Lưu điểm thi thành công!");
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu điểm thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm hợp lệ dạng số!");
        }
    }

    private void xoaDiem() {
        if (currentDiemId < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi điểm cần xóa từ bảng!");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa điểm thi này?") == JOptionPane.YES_OPTION) {
            if (diemDAO.delete(currentDiemId)) {
                JOptionPane.showMessageDialog(this, "Xóa điểm thành công!");
                loadTableData();
                clearForm();
            }
        }
    }

    private void clearForm() {
        currentDiemId = -1;
        txtChuyenCan.setText("10.0");
        txtGiuaKy.setText("8.0");
        txtThiCuoiKy.setText("7.5");
        txtDiemTB.setText("");
        txtXepLoai.setText("");
        txtTrangThai.setText("");
        tblDiem.clearSelection();
    }
}
