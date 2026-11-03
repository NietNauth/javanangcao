package UI;

import DAO.MonHocDAO;
import Model.MonHoc;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MonHocPanel extends JPanel {

    private MonHocDAO monHocDAO = new MonHocDAO();

    private JTextField txtMaMon, txtTenMon, txtSoTinChi, txtHeSo;
    private JTable tblMonHoc;
    private DefaultTableModel modelMon;

    public MonHocPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        // Form
        JPanel pnlForm = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin Môn học"),
            new EmptyBorder(10, 15, 10, 15)
        ));

        txtMaMon = new JTextField();
        txtTenMon = new JTextField();
        txtSoTinChi = new JTextField("3");
        txtHeSo = new JTextField("1.0");

        pnlForm.add(new JLabel("Mã Môn học (*):"));
        pnlForm.add(txtMaMon);
        pnlForm.add(new JLabel("Tên Môn học (*):"));
        pnlForm.add(txtTenMon);
        pnlForm.add(new JLabel("Số tín chỉ:"));
        pnlForm.add(txtSoTinChi);
        pnlForm.add(new JLabel("Hệ số môn:"));
        pnlForm.add(txtHeSo);

        // Buttons
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlBtns.setOpaque(false);
        JButton btnThem = new JButton("Thêm Môn");
        JButton btnSua = new JButton("Sửa Môn");
        JButton btnXoa = new JButton("Xóa Môn");
        JButton btnLamMoi = new JButton("Làm mới");

        btnThem.addActionListener(e -> themMon());
        btnSua.addActionListener(e -> suaMon());
        btnXoa.addActionListener(e -> xoaMon());
        btnLamMoi.addActionListener(e -> clearForm());

        pnlBtns.add(btnThem);
        pnlBtns.add(btnSua);
        pnlBtns.add(btnXoa);
        pnlBtns.add(btnLamMoi);

        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setOpaque(false);
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlBtns, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // Table
        modelMon = new DefaultTableModel(new String[]{"Mã Môn", "Tên Môn Học", "Số Tín Chỉ", "Hệ Số"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblMonHoc = new JTable(modelMon);
        tblMonHoc.setRowHeight(25);
        tblMonHoc.getSelectionModel().addListSelectionListener(e -> {
            int row = tblMonHoc.getSelectedRow();
            if (row >= 0) {
                txtMaMon.setText(modelMon.getValueAt(row, 0).toString());
                txtTenMon.setText(modelMon.getValueAt(row, 1).toString());
                txtSoTinChi.setText(modelMon.getValueAt(row, 2).toString());
                txtHeSo.setText(modelMon.getValueAt(row, 3).toString());
                txtMaMon.setEditable(false);
            }
        });

        add(new JScrollPane(tblMonHoc), BorderLayout.CENTER);
    }

    public void loadData() {
        modelMon.setRowCount(0);
        for (MonHoc mh : monHocDAO.getAll()) {
            modelMon.addRow(new Object[]{mh.getMaMon(), mh.getTenMon(), mh.getSoTinChi(), mh.getHeSo()});
        }
    }

    private void themMon() {
        String ma = txtMaMon.getText().trim();
        String ten = txtTenMon.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã môn và Tên môn!");
            return;
        }

        try {
            int tc = Integer.parseInt(txtSoTinChi.getText().trim());
            double hs = Double.parseDouble(txtHeSo.getText().trim());
            if (monHocDAO.insert(new MonHoc(ma, ten, tc, hs))) {
                JOptionPane.showMessageDialog(this, "Thêm môn học thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại (Trùng mã môn)!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tín chỉ và Hệ số phải là số hợp lệ!");
        }
    }

    private void suaMon() {
        String ma = txtMaMon.getText().trim();
        String ten = txtTenMon.getText().trim();
        if (ma.isEmpty()) return;

        try {
            int tc = Integer.parseInt(txtSoTinChi.getText().trim());
            double hs = Double.parseDouble(txtHeSo.getText().trim());
            if (monHocDAO.update(new MonHoc(ma, ten, tc, hs))) {
                JOptionPane.showMessageDialog(this, "Cập nhật môn học thành công!");
                loadData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tín chỉ và Hệ số phải là số hợp lệ!");
        }
    }

    private void xoaMon() {
        String ma = txtMaMon.getText().trim();
        if (ma.isEmpty()) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa môn học " + ma + "? Toàn bộ điểm liên quan sẽ bị xóa.") == JOptionPane.YES_OPTION) {
            if (monHocDAO.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa môn học thành công!");
                loadData();
                clearForm();
            }
        }
    }

    private void clearForm() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtSoTinChi.setText("3");
        txtHeSo.setText("1.0");
        txtMaMon.setEditable(true);
        tblMonHoc.clearSelection();
    }
}
