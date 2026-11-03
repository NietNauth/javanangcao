package UI;

import DAO.TaiKhoanDAO;
import Model.TaiKhoan;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JButton btnExit;

    public Login() {
        initComponents();
    }

    private void initComponents() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG QUẢN LÝ SINH VIÊN");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(30, 45, 30, 45));

        // Header Title
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlHeader.setOpaque(false);

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(25, 42, 86));

        JLabel lblSub = new JLabel("Hệ thống Quản lý Sinh viên & Đào tạo", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(113, 128, 150));

        pnlHeader.add(lblTitle);
        pnlHeader.add(lblSub);
        mainPanel.add(pnlHeader, BorderLayout.NORTH);

        // Form inputs using GridBagLayout for spacious heights
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setOpaque(false);
        pnlForm.setBorder(new EmptyBorder(10, 0, 15, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Label User
        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(45, 55, 72));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 6, 0);
        pnlForm.add(lblUser, gbc);

        // TextField User (Tall & Spacious)
        txtUser = new JTextField();
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUser.setPreferredSize(new Dimension(380, 42));
        txtUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tài khoản (vd: admin)");
        txtUser.putClientProperty(FlatClientProperties.STYLE, "arc: 8; margin: 4,12,4,12");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0);
        pnlForm.add(txtUser, gbc);

        // Label Password
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(45, 55, 72));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        pnlForm.add(lblPass, gbc);

        // PasswordField (Tall & Spacious)
        txtPass = new JPasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPass.setPreferredSize(new Dimension(380, 42));
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");
        txtPass.putClientProperty(FlatClientProperties.STYLE, "arc: 8; margin: 4,12,4,12");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlForm.add(txtPass, gbc);

        mainPanel.add(pnlForm, BorderLayout.CENTER);

        // Buttons
        JPanel pnlButtons = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlButtons.setOpaque(false);
        pnlButtons.setPreferredSize(new Dimension(380, 44));

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnLogin.addActionListener(e -> xuLyDangNhap());

        btnExit = new JButton("Thoát");
        btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnExit.addActionListener(e -> xuLyThoat());

        pnlButtons.add(btnLogin);
        pnlButtons.add(btnExit);

        mainPanel.add(pnlButtons, BorderLayout.SOUTH);

        // Enter key triggers login
        getRootPane().setDefaultButton(btnLogin);

        setContentPane(mainPanel);
    }

    private void xuLyDangNhap() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoanDAO dao = new TaiKhoanDAO();
        TaiKhoan tk = dao.getTaiKhoan(user, pass);
        if (tk != null) {
            MainForm main = new MainForm(tk);
            main.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLyThoat() {
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát khỏi chương trình?", "Xác nhận thoát", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }
}
