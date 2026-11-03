package UI;

import DAO.TaiKhoanDAO;
import Model.TaiKhoan;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TaiKhoanPanel extends JPanel {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private TaiKhoan currentUser;

    private JTextField txtUser, txtPass, txtHoTen;
    private JComboBox<String> cboVaiTro;
    private JTable tblTaiKhoan;
    private DefaultTableModel modelTK;

    public TaiKhoanPanel(TaiKhoan currentUser) {
        this.currentUser = currentUser;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        // Top Header
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("QUẢN TRỊ TÀI KHOẢN NGƯỜI DÙNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(25, 42, 86));

        JLabel lblSub = new JLabel("Hệ thống chỉ cho phép duy nhất 1 tài khoản Admin - Admin chỉ cấp tài khoản mới cho Giảng viên");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(113, 128, 150));

        pnlHeader.add(lblTitle);
        pnlHeader.add(lblSub);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Split (Form left, Table right)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin tài khoản"),
            new EmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setPreferredSize(new Dimension(360, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUser = new JTextField(15);
        txtPass = new JTextField(15);
        txtHoTen = new JTextField(15);
        cboVaiTro = new JComboBox<>(new String[]{"Giảng viên"});
        cboVaiTro.setEnabled(false); // Cố định vai trò là Giảng viên khi tạo mới

        addField(pnlForm, gbc, 0, "Tên đăng nhập (*):", txtUser);
        addField(pnlForm, gbc, 1, "Mật khẩu (*):", txtPass);
        addField(pnlForm, gbc, 2, "Họ và tên (*):", txtHoTen);
        addField(pnlForm, gbc, 3, "Vai trò cấp mới:", cboVaiTro);

        // Buttons
        JPanel pnlBtns = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlBtns.setOpaque(false);
        JButton btnThem = new JButton("Thêm tài khoản");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa tài khoản");
        JButton btnLamMoi = new JButton("Làm mới");

        btnThem.addActionListener(e -> themTaiKhoan());
        btnSua.addActionListener(e -> suaTaiKhoan());
        btnXoa.addActionListener(e -> xoaTaiKhoan());
        btnLamMoi.addActionListener(e -> clearForm());

        pnlBtns.add(btnThem);
        pnlBtns.add(btnSua);
        pnlBtns.add(btnXoa);
        pnlBtns.add(btnLamMoi);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        pnlForm.add(pnlBtns, gbc);

        // Right: Table
        modelTK = new DefaultTableModel(new String[]{"Tên đăng nhập", "Họ và Tên", "Vai trò", "Mật khẩu"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblTaiKhoan = new JTable(modelTK);
        tblTaiKhoan.setRowHeight(25);
        tblTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            int row = tblTaiKhoan.getSelectedRow();
            if (row >= 0) {
                txtUser.setText(modelTK.getValueAt(row, 0).toString());
                txtHoTen.setText(modelTK.getValueAt(row, 1).toString());
                String role = modelTK.getValueAt(row, 2).toString();
                cboVaiTro.setModel(new DefaultComboBoxModel<>(new String[]{role}));
                cboVaiTro.setEnabled(false);
                txtPass.setText(modelTK.getValueAt(row, 3).toString());
                txtUser.setEditable(false);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlForm, new JScrollPane(tblTaiKhoan));
        splitPane.setDividerLocation(360);
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

    public void loadData() {
        modelTK.setRowCount(0);
        for (TaiKhoan tk : taiKhoanDAO.getAll()) {
            modelTK.addRow(new Object[]{
                tk.getTenDangNhap(),
                tk.getHoTen(),
                tk.getVaiTro(),
                tk.getMatKhau()
            });
        }
    }

    private void themTaiKhoan() {
        String user = txtUser.getText().trim();
        String pass = txtPass.getText().trim();
        String hoTen = txtHoTen.getText().trim();

        if (user.isEmpty() || pass.isEmpty() || hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập, mật khẩu và họ tên!");
            return;
        }

        // Tài khoản tạo mới luôn luôn là Giảng viên (chỉ duy nhất 1 Admin)
        TaiKhoan tk = new TaiKhoan(user, pass, hoTen, "Giảng viên");
        if (taiKhoanDAO.insert(tk)) {
            JOptionPane.showMessageDialog(this, "Tạo tài khoản Giảng viên thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Tạo thất bại (Tên đăng nhập đã tồn tại hoặc vi phạm chính sách)!");
        }
    }

    private void suaTaiKhoan() {
        String user = txtUser.getText().trim();
        String pass = txtPass.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String vaiTro = (String) cboVaiTro.getSelectedItem();

        if (user.isEmpty() || pass.isEmpty() || hoTen.isEmpty()) return;

        TaiKhoan tk = new TaiKhoan(user, pass, hoTen, vaiTro);
        if (taiKhoanDAO.update(tk)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin tài khoản thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void xoaTaiKhoan() {
        int selectedRow = tblTaiKhoan.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa trong bảng!");
            return;
        }

        String user = modelTK.getValueAt(selectedRow, 0).toString();
        String vaiTro = modelTK.getValueAt(selectedRow, 2).toString();

        // Bảo vệ tuyệt đối: Không cho phép xóa tài khoản Admin duy nhất
        if ("Admin".equalsIgnoreCase(vaiTro)) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản Quản trị viên (Admin) duy nhất của hệ thống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa tài khoản Giảng viên [" + user + "]?") == JOptionPane.YES_OPTION) {
            if (taiKhoanDAO.delete(user)) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
                loadData();
                clearForm();
            }
        }
    }

    private void clearForm() {
        txtUser.setText("");
        txtPass.setText("");
        txtHoTen.setText("");
        txtUser.setEditable(true);
        cboVaiTro.setModel(new DefaultComboBoxModel<>(new String[]{"Giảng viên"}));
        cboVaiTro.setEnabled(false);
        tblTaiKhoan.clearSelection();
    }
}
