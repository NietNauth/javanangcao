package UI;

import Model.TaiKhoan;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;

public class MainForm extends javax.swing.JFrame {

    private TaiKhoan currentUser;
    private JPanel mainContentPanel;
    private JPanel pnlMenu;
    private JPanel pnlHeader;
    private JLabel lblHeaderTitle;

    public MainForm() {
        this(new TaiKhoan("admin", "123", "Quản trị viên", "Admin"));
    }

    public MainForm(TaiKhoan user) {
        this.currentUser = (user != null) ? user : new TaiKhoan("admin", "123", "Quản trị viên", "Admin");
        initComponents();
        showHome();
    }

    private void initComponents() {
        setTitle("HỆ THỐNG QUẢN LÝ SINH VIÊN & ĐÀO TẠO");
        setSize(1200, 750);
        setMinimumSize(new java.awt.Dimension(1000, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(25, 42, 86));
        pnlHeader.setPreferredSize(new java.awt.Dimension(1200, 65));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        lblHeaderTitle = new JLabel("HỆ THỐNG QUẢN LÝ SINH VIÊN");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeaderTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblHeaderTitle, BorderLayout.WEST);

        String roleText = " (" + currentUser.getVaiTro() + ")";
        JLabel lblUser = new JLabel("Xin chào, " + currentUser.getHoTen() + roleText);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(new Color(220, 225, 235));
        pnlHeader.add(lblUser, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // Sidebar Menu
        boolean isAdmin = "Admin".equalsIgnoreCase(currentUser.getVaiTro());
        
        pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(new Color(34, 45, 80));
        pnlMenu.setPreferredSize(new java.awt.Dimension(250, 680));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JPanel pnlNav = new JPanel(new GridLayout(0, 1, 0, 6));
        pnlNav.setOpaque(false);

        JButton btnTrangChu = createMenuButton("Trang chủ");
        btnTrangChu.addActionListener(e -> showHome());
        pnlNav.add(btnTrangChu);

        if (isAdmin) {
            JButton btnKhoaLop = createMenuButton("Khoa & Lớp học");
            btnKhoaLop.addActionListener(e -> setContent(new KhoaLopPanel()));
            pnlNav.add(btnKhoaLop);

            JButton btnSinhVien = createMenuButton("Hồ sơ Sinh viên");
            btnSinhVien.addActionListener(e -> setContent(new SinhVienPanel(true)));
            pnlNav.add(btnSinhVien);

            JButton btnMonHoc = createMenuButton("Môn học");
            btnMonHoc.addActionListener(e -> setContent(new MonHocPanel()));
            pnlNav.add(btnMonHoc);
        } else {
            JButton btnSinhVien = createMenuButton("Hồ sơ Sinh viên");
            btnSinhVien.addActionListener(e -> setContent(new SinhVienPanel(false)));
            pnlNav.add(btnSinhVien);
        }

        JButton btnDiem = createMenuButton("Điểm số & Thi");
        btnDiem.addActionListener(e -> setContent(new DiemPanel()));
        pnlNav.add(btnDiem);

        JButton btnThongKe = createMenuButton("Báo cáo & Thống kê");
        btnThongKe.addActionListener(e -> setContent(new ThongKePanel()));
        pnlNav.add(btnThongKe);

        // Chức năng quản trị tài khoản chỉ hiển thị cho Admin
        if (isAdmin) {
            JButton btnTaiKhoan = createMenuButton("Quản trị Tài khoản");
            btnTaiKhoan.addActionListener(e -> setContent(new TaiKhoanPanel(currentUser)));
            pnlNav.add(btnTaiKhoan);
        }

        JButton btnDangXuat = createMenuButton("Đăng xuất");
        btnDangXuat.addActionListener(e -> dangXuat());

        pnlMenu.add(pnlNav, BorderLayout.NORTH);
        pnlMenu.add(btnDangXuat, BorderLayout.SOUTH);

        add(pnlMenu, BorderLayout.WEST);

        // Main Content Area
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(245, 247, 250));
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new java.awt.Dimension(230, 44));
        btn.setBackground(new Color(34, 45, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(34, 45, 80));
            }
        });
        return btn;
    }

    private void showHome() {
        setContent(new TrangChuPanel());
    }

    private void setContent(JPanel panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void dangXuat() {
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất không?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Không thể nạp FlatLaf");
        }
        java.awt.EventQueue.invokeLater(() -> new MainForm().setVisible(true));
    }
}
