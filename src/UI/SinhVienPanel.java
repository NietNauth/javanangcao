package UI;

import DAO.LopDAO;
import DAO.SinhVienDAO;
import Model.Lop;
import Model.SinhVien;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SinhVienPanel extends JPanel {

    private boolean isAdmin = true;
    private SinhVienDAO svDAO = new SinhVienDAO();
    private LopDAO lopDAO = new LopDAO();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Form inputs
    private JTextField txtMaSV, txtHoTen, txtNgaySinh, txtDiaChi, txtSDT, txtEmail;
    private JRadioButton rdoNam, rdoNu;
    private ButtonGroup bgGioiTinh;
    private JComboBox<String> cboLop, cboLocLop;
    private JTextField txtTimKiem;

    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    // Table
    private JTable tblSinhVien;
    private DefaultTableModel modelSV;

    public SinhVienPanel() {
        this(true);
    }

    public SinhVienPanel(boolean isAdmin) {
        this.isAdmin = isAdmin;
        initComponents();
        loadComboLop();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        // TOP: Filter & Search Bar
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        pnlFilter.add(new JLabel("Tìm kiếm (Tên/Mã SV/SĐT):"));
        txtTimKiem = new JTextField(15);
        pnlFilter.add(txtTimKiem);

        pnlFilter.add(new JLabel("Lọc theo lớp:"));
        cboLocLop = new JComboBox<>();
        cboLocLop.addItem("Tất cả");
        pnlFilter.add(cboLocLop);

        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.addActionListener(e -> timKiem());
        pnlFilter.add(btnTim);

        JButton btnTatCa = new JButton("Hiển thị tất cả");
        btnTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            cboLocLop.setSelectedIndex(0);
            loadTableData();
        });
        pnlFilter.add(btnTatCa);

        add(pnlFilter, BorderLayout.NORTH);

        // CENTER: SplitPane (Left: Form, Right: Table)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        String formTitle = isAdmin ? "Thông tin sinh viên" : "Thông tin chi tiết (Chỉ xem)";
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(formTitle),
            new EmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setPreferredSize(new Dimension(360, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaSV = new JTextField(15);
        txtHoTen = new JTextField(15);
        txtNgaySinh = new JTextField(15);
        txtNgaySinh.setToolTipText("Định dạng: YYYY-MM-DD (Ví dụ: 2004-05-15)");
        txtDiaChi = new JTextField(15);
        txtSDT = new JTextField(15);
        txtEmail = new JTextField(15);
        cboLop = new JComboBox<>();

        rdoNam = new JRadioButton("Nam", true);
        rdoNu = new JRadioButton("Nữ");
        rdoNam.setOpaque(false);
        rdoNu.setOpaque(false);
        bgGioiTinh = new ButtonGroup();
        bgGioiTinh.add(rdoNam);
        bgGioiTinh.add(rdoNu);
        JPanel pnlGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlGender.setOpaque(false);
        pnlGender.add(rdoNam);
        pnlGender.add(rdoNu);

        // Add fields
        addField(pnlForm, gbc, 0, "Mã Sinh viên (*):", txtMaSV);
        addField(pnlForm, gbc, 1, "Họ và Tên (*):", txtHoTen);
        addField(pnlForm, gbc, 2, "Ngày sinh (yyyy-MM-dd):", txtNgaySinh);
        addField(pnlForm, gbc, 3, "Giới tính:", pnlGender);
        addField(pnlForm, gbc, 4, "Lớp học (*):", cboLop);
        addField(pnlForm, gbc, 5, "Số điện thoại:", txtSDT);
        addField(pnlForm, gbc, 6, "Email:", txtEmail);
        addField(pnlForm, gbc, 7, "Địa chỉ / Quê quán:", txtDiaChi);

        // Form Buttons
        JPanel pnlBtns;
        if (isAdmin) {
            pnlBtns = new JPanel(new GridLayout(2, 2, 8, 8));
            pnlBtns.setOpaque(false);
            btnThem = new JButton("Thêm mới");
            btnSua = new JButton("Cập nhật");
            btnXoa = new JButton("Xóa");
            btnLamMoi = new JButton("Làm mới");

            btnThem.addActionListener(e -> themSV());
            btnSua.addActionListener(e -> suaSV());
            btnXoa.addActionListener(e -> xoaSV());
            btnLamMoi.addActionListener(e -> clearForm());

            pnlBtns.add(btnThem);
            pnlBtns.add(btnSua);
            pnlBtns.add(btnXoa);
            pnlBtns.add(btnLamMoi);
        } else {
            // Giảng viên chỉ có chế độ xem và làm mới
            pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            pnlBtns.setOpaque(false);
            btnLamMoi = new JButton("Làm mới biểu mẫu");
            btnLamMoi.addActionListener(e -> clearForm());
            pnlBtns.add(btnLamMoi);

            // Khóa không cho sửa
            setFieldsEditable(false);
        }

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        pnlForm.add(pnlBtns, gbc);

        // Right Table
        modelSV = new DefaultTableModel(new String[]{
            "Mã SV", "Họ Tên", "Ngày Sinh", "Giới Tính", "Lớp", "SĐT", "Email", "Quê Quán"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSinhVien = new JTable(modelSV);
        tblSinhVien.setRowHeight(25);
        tblSinhVien.getSelectionModel().addListSelectionListener(e -> {
            int row = tblSinhVien.getSelectedRow();
            if (row >= 0) {
                fillForm(row);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlForm, new JScrollPane(tblSinhVien));
        splitPane.setDividerLocation(360);
        add(splitPane, BorderLayout.CENTER);
    }

    private void addField(JPanel pnl, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        pnl.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        pnl.add(comp, gbc);
    }

    private void setFieldsEditable(boolean editable) {
        txtMaSV.setEditable(editable);
        txtHoTen.setEditable(editable);
        txtNgaySinh.setEditable(editable);
        txtDiaChi.setEditable(editable);
        txtSDT.setEditable(editable);
        txtEmail.setEditable(editable);
        cboLop.setEnabled(editable);
        rdoNam.setEnabled(editable);
        rdoNu.setEnabled(editable);
    }

    private void loadComboLop() {
        cboLop.removeAllItems();
        cboLocLop.removeAllItems();
        cboLocLop.addItem("Tất cả");

        for (Lop l : lopDAO.getAll()) {
            cboLop.addItem(l.getMaLop());
            cboLocLop.addItem(l.getMaLop());
        }
    }

    public void loadTableData() {
        modelSV.setRowCount(0);
        for (SinhVien sv : svDAO.getAll()) {
            modelSV.addRow(new Object[]{
                sv.getMaSV(),
                sv.getHoTen(),
                sv.getNgaySinh(),
                sv.getGioiTinh(),
                sv.getMaLop(),
                sv.getSdt(),
                sv.getEmail(),
                sv.getDiaChi()
            });
        }
    }

    private void timKiem() {
        String kw = txtTimKiem.getText();
        String lop = (String) cboLocLop.getSelectedItem();
        List<SinhVien> list = svDAO.search(kw, lop);
        modelSV.setRowCount(0);
        for (SinhVien sv : list) {
            modelSV.addRow(new Object[]{
                sv.getMaSV(),
                sv.getHoTen(),
                sv.getNgaySinh(),
                sv.getGioiTinh(),
                sv.getMaLop(),
                sv.getSdt(),
                sv.getEmail(),
                sv.getDiaChi()
            });
        }
    }

    private void fillForm(int row) {
        txtMaSV.setText(modelSV.getValueAt(row, 0).toString());
        txtHoTen.setText(modelSV.getValueAt(row, 1).toString());
        txtNgaySinh.setText(modelSV.getValueAt(row, 2) != null ? modelSV.getValueAt(row, 2).toString() : "");
        String gt = modelSV.getValueAt(row, 3).toString();
        if ("Nữ".equalsIgnoreCase(gt)) {
            rdoNu.setSelected(true);
        } else {
            rdoNam.setSelected(true);
        }
        cboLop.setSelectedItem(modelSV.getValueAt(row, 4).toString());
        txtSDT.setText(modelSV.getValueAt(row, 5) != null ? modelSV.getValueAt(row, 5).toString() : "");
        txtEmail.setText(modelSV.getValueAt(row, 6) != null ? modelSV.getValueAt(row, 6).toString() : "");
        txtDiaChi.setText(modelSV.getValueAt(row, 7) != null ? modelSV.getValueAt(row, 7).toString() : "");
        txtMaSV.setEditable(false);
    }

    private void themSV() {
        if (!isAdmin) return;
        SinhVien sv = getModelFromForm();
        if (sv == null) return;
        if (svDAO.insert(sv)) {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            loadTableData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại (Trùng mã sinh viên)!");
        }
    }

    private void suaSV() {
        if (!isAdmin) return;
        SinhVien sv = getModelFromForm();
        if (sv == null) return;
        if (svDAO.update(sv)) {
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công!");
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void xoaSV() {
        if (!isAdmin) return;
        String ma = txtMaSV.getText().trim();
        if (ma.isEmpty()) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa sinh viên " + ma + "? Toàn bộ điểm thi liên quan cũng sẽ bị xóa.") == JOptionPane.YES_OPTION) {
            if (svDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!");
                loadTableData();
                clearForm();
            }
        }
    }

    private SinhVien getModelFromForm() {
        String ma = txtMaSV.getText().trim();
        String ten = txtHoTen.getText().trim();
        String lop = (String) cboLop.getSelectedItem();
        if (ma.isEmpty() || ten.isEmpty() || lop == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã SV, Họ Tên và chọn Lớp!");
            return null;
        }

        Date ns;
        try {
            ns = Date.valueOf(txtNgaySinh.getText().trim());
        } catch (Exception e) {
            ns = new Date(System.currentTimeMillis());
        }

        String gt = rdoNam.isSelected() ? "Nam" : "Nữ";
        return new SinhVien(ma, ten, ns, gt, txtDiaChi.getText().trim(), txtSDT.getText().trim(), txtEmail.getText().trim(), lop);
    }

    private void clearForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        if (isAdmin) {
            txtMaSV.setEditable(true);
        }
        tblSinhVien.clearSelection();
    }
}
