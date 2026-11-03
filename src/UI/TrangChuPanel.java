package UI;

import DAO.ThongKeDAO;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TrangChuPanel extends JPanel {

    private JLabel lblSV, lblLop, lblKhoa, lblMon;
    private ThongKeDAO thongKeDAO = new ThongKeDAO();

    public TrangChuPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(25, 25, 25, 25));
        setBackground(new Color(245, 247, 250));

        // Header
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ SINH VIÊN & ĐÀO TẠO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(25, 42, 86));

        JLabel lblSub = new JLabel("Tổng quan số liệu thống kê học vụ và hệ thống đào tạo");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(113, 128, 150));

        pnlHeader.add(lblTitle);
        pnlHeader.add(lblSub);
        add(pnlHeader, BorderLayout.NORTH);

        // Center: 4 Stat Cards
        JPanel pnlCards = new JPanel(new GridLayout(2, 2, 20, 20));
        pnlCards.setOpaque(false);

        lblSV = new JLabel("0");
        lblLop = new JLabel("0");
        lblKhoa = new JLabel("0");
        lblMon = new JLabel("0");

        pnlCards.add(createCard("TỔNG SỐ SINH VIÊN", lblSV, new Color(41, 128, 185)));
        pnlCards.add(createCard("LỚP ĐANG ĐÀO TẠO", lblLop, new Color(39, 174, 96)));
        pnlCards.add(createCard("KHOA / VIỆN ĐÀO TẠO", lblKhoa, new Color(142, 68, 173)));
        pnlCards.add(createCard("MÔN HỌC / HỌC PHẦN", lblMon, new Color(211, 84, 0)));

        add(pnlCards, BorderLayout.CENTER);

        // Footer quick actions
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlFooter.setOpaque(false);
        JButton btnReload = new JButton("Làm mới số liệu");
        btnReload.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReload.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnReload.addActionListener(e -> loadData());
        pnlFooter.add(btnReload);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, JLabel lblValue, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        // Content
        JPanel pnlContent = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlContent.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(113, 128, 150));

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(accentColor);

        pnlContent.add(lblTitle);
        pnlContent.add(lblValue);
        card.add(pnlContent, BorderLayout.CENTER);

        return card;
    }

    public void loadData() {
        Map<String, Integer> stats = thongKeDAO.getTongQuan();
        lblSV.setText(String.valueOf(stats.getOrDefault("soSV", 0)));
        lblLop.setText(String.valueOf(stats.getOrDefault("soLop", 0)));
        lblKhoa.setText(String.valueOf(stats.getOrDefault("soKhoa", 0)));
        lblMon.setText(String.valueOf(stats.getOrDefault("soMon", 0)));
    }
}
