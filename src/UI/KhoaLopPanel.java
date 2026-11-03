package UI;

import DAO.KhoaDAO;
import DAO.LopDAO;
import Model.Khoa;
import Model.Lop;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class KhoaLopPanel extends JPanel {

    private KhoaDAO khoaDAO = new KhoaDAO();
    private LopDAO lopDAO = new LopDAO();

    // Components Khoa
    private JTextField txtMaKhoa, txtTenKhoa, txtSDTKhoa, txtEmailKhoa;
    private JTable tblKhoa;
    private DefaultTableModel modelKhoa;

    // Components Lop
    private JTextField txtMaLop, txtTenLop, txtNienKhoa;
    private JComboBox<Khoa> cboKhoaCuaLop;
    private JTable tblLop;
    private DefaultTableModel modelLop;

    public KhoaLopPanel() {
        initComponents();
        loadTableKhoa();
        loadComboKhoa();
        loadTableLop();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("Quản lý Lớp học", createLopTab());
        tabbedPane.addTab("Quản lý Khoa / Viện", createKhoaTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createKhoaTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Form nhập
        JPanel pnlForm = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin Khoa"));

        txtMaKhoa = new JTextField();
        txtTenKhoa = new JTextField();
        txtSDTKhoa = new JTextField();
        txtEmailKhoa = new JTextField();

        pnlForm.add(new JLabel("Mã Khoa:"));
        pnlForm.add(txtMaKhoa);
        pnlForm.add(new JLabel("Tên Khoa:"));
        pnlForm.add(txtTenKhoa);
        pnlForm.add(new JLabel("Số điện thoại:"));
        pnlForm.add(txtSDTKhoa);
        pnlForm.add(new JLabel("Email liên hệ:"));
        pnlForm.add(txtEmailKhoa);

        // Buttons
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnThem = new JButton("Thêm Khoa");
        JButton btnSua = new JButton("Sửa Khoa");
        JButton btnXoa = new JButton("Xóa Khoa");
        JButton btnLamMoi = new JButton("Làm mới");

        btnThem.addActionListener(e -> themKhoa());
        btnSua.addActionListener(e -> suaKhoa());
        btnXoa.addActionListener(e -> xoaKhoa());
        btnLamMoi.addActionListener(e -> clearKhoaForm());

        pnlBtns.add(btnThem);
        pnlBtns.add(btnSua);
        pnlBtns.add(btnXoa);
        pnlBtns.add(btnLamMoi);

        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlBtns, BorderLayout.SOUTH);
        pnl.add(pnlTop, BorderLayout.NORTH);

        // Table
        modelKhoa = new DefaultTableModel(new String[]{"Mã Khoa", "Tên Khoa", "SĐT", "Email"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblKhoa = new JTable(modelKhoa);
        tblKhoa.setRowHeight(25);
        tblKhoa.getSelectionModel().addListSelectionListener(e -> {
            int row = tblKhoa.getSelectedRow();
            if (row >= 0) {
                txtMaKhoa.setText(modelKhoa.getValueAt(row, 0).toString());
                txtTenKhoa.setText(modelKhoa.getValueAt(row, 1).toString());
                txtSDTKhoa.setText(modelKhoa.getValueAt(row, 2) != null ? modelKhoa.getValueAt(row, 2).toString() : "");
                txtEmailKhoa.setText(modelKhoa.getValueAt(row, 3) != null ? modelKhoa.getValueAt(row, 3).toString() : "");
                txtMaKhoa.setEditable(false);
            }
        });

        pnl.add(new JScrollPane(tblKhoa), BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createLopTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Form
        JPanel pnlForm = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin Lớp học"));

        txtMaLop = new JTextField();
        txtTenLop = new JTextField();
        cboKhoaCuaLop = new JComboBox<>();
        txtNienKhoa = new JTextField("2022-2026");

        pnlForm.add(new JLabel("Mã Lớp:"));
        pnlForm.add(txtMaLop);
        pnlForm.add(new JLabel("Tên Lớp:"));
        pnlForm.add(txtTenLop);
        pnlForm.add(new JLabel("Thuộc Khoa:"));
        pnlForm.add(cboKhoaCuaLop);
        pnlForm.add(new JLabel("Niên khóa:"));
        pnlForm.add(txtNienKhoa);

        // Buttons
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnThem = new JButton("Thêm Lớp");
        JButton btnSua = new JButton("Sửa Lớp");
        JButton btnXoa = new JButton("Xóa Lớp");
        JButton btnLamMoi = new JButton("Làm mới");

        btnThem.addActionListener(e -> themLop());
        btnSua.addActionListener(e -> suaLop());
        btnXoa.addActionListener(e -> xoaLop());
        btnLamMoi.addActionListener(e -> clearLopForm());

        pnlBtns.add(btnThem);
        pnlBtns.add(btnSua);
        pnlBtns.add(btnXoa);
        pnlBtns.add(btnLamMoi);

        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlBtns, BorderLayout.SOUTH);
        pnl.add(pnlTop, BorderLayout.NORTH);

        // Table
        modelLop = new DefaultTableModel(new String[]{"Mã Lớp", "Tên Lớp", "Khoa", "Niên Khóa"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLop = new JTable(modelLop);
        tblLop.setRowHeight(25);
        tblLop.getSelectionModel().addListSelectionListener(e -> {
            int row = tblLop.getSelectedRow();
            if (row >= 0) {
                txtMaLop.setText(modelLop.getValueAt(row, 0).toString());
                txtTenLop.setText(modelLop.getValueAt(row, 1).toString());
                String maKhoa = modelLop.getValueAt(row, 2).toString();
                for (int i = 0; i < cboKhoaCuaLop.getItemCount(); i++) {
                    if (cboKhoaCuaLop.getItemAt(i).getMaKhoa().equals(maKhoa)) {
                        cboKhoaCuaLop.setSelectedIndex(i);
                        break;
                    }
                }
                txtNienKhoa.setText(modelLop.getValueAt(row, 3).toString());
                txtMaLop.setEditable(false);
            }
        });

        pnl.add(new JScrollPane(tblLop), BorderLayout.CENTER);
        return pnl;
    }

    private void loadTableKhoa() {
        modelKhoa.setRowCount(0);
        for (Khoa k : khoaDAO.getAll()) {
            modelKhoa.addRow(new Object[]{k.getMaKhoa(), k.getTenKhoa(), k.getSdt(), k.getEmail()});
        }
    }

    private void loadComboKhoa() {
        cboKhoaCuaLop.removeAllItems();
        for (Khoa k : khoaDAO.getAll()) {
            cboKhoaCuaLop.addItem(k);
        }
    }

    private void loadTableLop() {
        modelLop.setRowCount(0);
        for (Lop l : lopDAO.getAll()) {
            modelLop.addRow(new Object[]{l.getMaLop(), l.getTenLop(), l.getMaKhoa(), l.getNienKhoa()});
        }
    }

    private void themKhoa() {
        String ma = txtMaKhoa.getText().trim();
        String ten = txtTenKhoa.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Khoa và Tên Khoa!");
            return;
        }
        if (khoaDAO.insert(new Khoa(ma, ten, txtSDTKhoa.getText().trim(), txtEmailKhoa.getText().trim()))) {
            JOptionPane.showMessageDialog(this, "Thêm Khoa thành công!");
            loadTableKhoa();
            loadComboKhoa();
            clearKhoaForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm Khoa thất bại (Trùng mã Khoa)!");
        }
    }

    private void suaKhoa() {
        String ma = txtMaKhoa.getText().trim();
        String ten = txtTenKhoa.getText().trim();
        if (ma.isEmpty()) return;
        if (khoaDAO.update(new Khoa(ma, ten, txtSDTKhoa.getText().trim(), txtEmailKhoa.getText().trim()))) {
            JOptionPane.showMessageDialog(this, "Cập nhật Khoa thành công!");
            loadTableKhoa();
            loadComboKhoa();
        }
    }

    private void xoaKhoa() {
        String ma = txtMaKhoa.getText().trim();
        if (ma.isEmpty()) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa Khoa " + ma + "?") == JOptionPane.YES_OPTION) {
            if (khoaDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa Khoa thành công!");
                loadTableKhoa();
                loadComboKhoa();
                loadTableLop();
                clearKhoaForm();
            }
        }
    }

    private void clearKhoaForm() {
        txtMaKhoa.setText("");
        txtTenKhoa.setText("");
        txtSDTKhoa.setText("");
        txtEmailKhoa.setText("");
        txtMaKhoa.setEditable(true);
        tblKhoa.clearSelection();
    }

    private void themLop() {
        String ma = txtMaLop.getText().trim();
        String ten = txtTenLop.getText().trim();
        Khoa k = (Khoa) cboKhoaCuaLop.getSelectedItem();
        if (ma.isEmpty() || ten.isEmpty() || k == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin Lớp!");
            return;
        }
        if (lopDAO.insert(new Lop(ma, ten, k.getMaKhoa(), txtNienKhoa.getText().trim()))) {
            JOptionPane.showMessageDialog(this, "Thêm Lớp học thành công!");
            loadTableLop();
            clearLopForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm Lớp thất bại (Trùng mã lớp)!");
        }
    }

    private void suaLop() {
        String ma = txtMaLop.getText().trim();
        String ten = txtTenLop.getText().trim();
        Khoa k = (Khoa) cboKhoaCuaLop.getSelectedItem();
        if (ma.isEmpty() || k == null) return;
        if (lopDAO.update(new Lop(ma, ten, k.getMaKhoa(), txtNienKhoa.getText().trim()))) {
            JOptionPane.showMessageDialog(this, "Cập nhật Lớp học thành công!");
            loadTableLop();
        }
    }

    private void xoaLop() {
        String ma = txtMaLop.getText().trim();
        if (ma.isEmpty()) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa Lớp " + ma + "?") == JOptionPane.YES_OPTION) {
            if (lopDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa Lớp thành công!");
                loadTableLop();
                clearLopForm();
            }
        }
    }

    private void clearLopForm() {
        txtMaLop.setText("");
        txtTenLop.setText("");
        txtMaLop.setEditable(true);
        tblLop.clearSelection();
    }
}
