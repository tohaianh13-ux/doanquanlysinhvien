import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class MainFrame extends JFrame {
    private TaiKhoan currentUser;

    public MainFrame(TaiKhoan user) {
        this.currentUser = user;
        setTitle("HỆ THỐNG QUẢN LÝ SINH VIÊN - Quyền: " + user.getRole());
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Tab Sinh Viên
        tabbedPane.addTab("Quản Lý Sinh Viên", createSinhVienPanel());

        // 2. Tab Môn Học
        tabbedPane.addTab("Quản Lý Môn Học", createMonHocPanel());

        // 3. Tab Nhập Điểm / Kết Quả
        tabbedPane.addTab("Quản Lý Điểm / Kết Quả", createKetQuaPanel());

        // 4. Tab Lớp Học
        tabbedPane.addTab("Quản Lý Lớp", createLopPanel());

        // 5. Tab Khoa (Chỉ ADMIN)
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            tabbedPane.addTab("Quản Lý Khoa", createKhoaPanel());
        }

        add(tabbedPane);
    }

    // ================= 1. TAB QUẢN LÝ SINH VIÊN =================
    private JPanel createSinhVienPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        SinhVienDAO dao = new SinhVienDAO();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã SV", "Họ Tên", "Giới Tính", "Ngày Sinh", "Địa Chỉ", "Email", "SĐT", "Mã Lớp"}, 0);
        JTable table = new JTable(model);

        Runnable loadAllData = () -> {
            model.setRowCount(0);
            for (SinhVien sv : dao.getAllSinhVien()) {
                model.addRow(new Object[]{
                        sv.getMaSV(), sv.getHoTen(), sv.getGioiTinh(), sv.getNgaySinh(),
                        sv.getDiaChi(), sv.getEmail(), sv.getSoDienThoai(), sv.getMaLop()
                });
            }
        };
        loadAllData.run();

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtMaSV = new JTextField(15);
        JTextField txtHoTen = new JTextField(15);
        JTextField txtGioiTinh = new JTextField(15);
        JTextField txtNgaySinh = new JTextField(15);
        JTextField txtDiaChi = new JTextField(15);
        JTextField txtEmail = new JTextField(15);
        JTextField txtSDT = new JTextField(15);
        JTextField txtMaLop = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Mã SV:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(txtMaSV, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Giới Tính:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(txtGioiTinh, gbc);
        gbc.gridx = 2; gbc.gridy = 1; inputPanel.add(new JLabel("Ngày Sinh (DD/MM/YYYY):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; inputPanel.add(txtNgaySinh, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Địa Chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(txtDiaChi, gbc);
        gbc.gridx = 2; gbc.gridy = 2; inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; inputPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("Số Điện Thoại:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; inputPanel.add(txtSDT, gbc);
        gbc.gridx = 2; gbc.gridy = 3; inputPanel.add(new JLabel("Mã Lớp:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; inputPanel.add(txtMaLop, gbc);

        // Bộ lọc theo Lớp
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc danh sách theo Lớp"));
        JComboBox<String> cbLop = new JComboBox<>();
        cbLop.addItem("-- Chọn Lớp để lọc --");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ma_lop FROM lop")) {
            while (rs.next()) {
                cbLop.addItem(rs.getString("ma_lop"));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        JButton btnFilter = new JButton("Lọc Theo Lớp");
        JButton btnShowAll = new JButton("Hiện Tất Cả");
        filterPanel.add(new JLabel("Chọn lớp:"));
        filterPanel.add(cbLop);
        filterPanel.add(btnFilter);
        filterPanel.add(btnShowAll);

        JPanel northGroup = new JPanel(new BorderLayout());
        northGroup.add(inputPanel, BorderLayout.CENTER);
        northGroup.add(filterPanel, BorderLayout.SOUTH);

        btnFilter.addActionListener(e -> {
            String selectedLop = (String) cbLop.getSelectedItem();
            if (selectedLop == null || selectedLop.startsWith("--")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp cụ thể!");
                return;
            }
            model.setRowCount(0);
            for (SinhVien sv : dao.getAllSinhVien()) {
                if (sv.getMaLop().equalsIgnoreCase(selectedLop)) {
                    model.addRow(new Object[]{sv.getMaSV(), sv.getHoTen(), sv.getGioiTinh(), sv.getNgaySinh(), sv.getDiaChi(), sv.getEmail(), sv.getSoDienThoai(), sv.getMaLop()});
                }
            }
        });

        btnShowAll.addActionListener(e -> {
            cbLop.setSelectedIndex(0);
            loadAllData.run();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && !e.getValueIsAdjusting()) {
                txtMaSV.setText(model.getValueAt(row, 0).toString());
                txtMaSV.setEnabled(false);
                txtHoTen.setText(model.getValueAt(row, 1).toString());
                txtGioiTinh.setText(model.getValueAt(row, 2).toString());
                String dbDate = model.getValueAt(row, 3).toString();
                if (dbDate != null && dbDate.contains("-")) {
                    String[] parts = dbDate.split("-");
                    if (parts.length == 3) txtNgaySinh.setText(parts[2] + "/" + parts[1] + "/" + parts[0]);
                } else { txtNgaySinh.setText(dbDate); }
                txtDiaChi.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                txtEmail.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
                txtSDT.setText(model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "");
                txtMaLop.setText(model.getValueAt(row, 7).toString());
            }
        });

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnReset = new JButton("Làm mới");
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnReset);

        btnAdd.addActionListener(e -> {
            try {
                String ngayNhap = txtNgaySinh.getText().trim();
                String ngaySql = ngayNhap.contains("/") ? ngayNhap.split("/")[2] + "-" + ngayNhap.split("/")[1] + "-" + ngayNhap.split("/")[0] : ngayNhap;
                SinhVien sv = new SinhVien(txtMaSV.getText().trim(), txtHoTen.getText().trim(), txtGioiTinh.getText().trim(), ngaySql, txtDiaChi.getText().trim(), txtEmail.getText().trim(), txtSDT.getText().trim(), txtMaLop.getText().trim());
                if (dao.insertSinhVien(sv)) {
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
                    loadAllData.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Kiểm tra lại Mã Lớp hoặc Email bị trùng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi định dạng dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                String ngayNhap = txtNgaySinh.getText().trim();
                String ngaySql = ngayNhap.contains("/") ? ngayNhap.split("/")[2] + "-" + ngayNhap.split("/")[1] + "-" + ngayNhap.split("/")[0] : ngayNhap;
                String sql = "UPDATE sinh_vien SET ho_ten=?, gioi_tinh=?, ngay_sinh=?, dia_chi=?, email=?, so_dien_thoai=?, ma_lop=? WHERE ma_sv=?";
                try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, txtHoTen.getText().trim());
                    pstmt.setString(2, txtGioiTinh.getText().trim());
                    pstmt.setString(3, ngaySql);
                    pstmt.setString(4, txtDiaChi.getText().trim());
                    pstmt.setString(5, txtEmail.getText().trim());
                    pstmt.setString(6, txtSDT.getText().trim());
                    pstmt.setString(7, txtMaLop.getText().trim());
                    pstmt.setString(8, txtMaSV.getText().trim());
                    if (pstmt.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công!");
                        loadAllData.run();
                    }
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && dao.deleteSinhVien(model.getValueAt(row, 0).toString())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadAllData.run();
            } else { JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!"); }
        });

        btnReset.addActionListener(e -> {
            txtMaSV.setText(""); txtMaSV.setEnabled(true);
            txtHoTen.setText(""); txtGioiTinh.setText(""); txtNgaySinh.setText("");
            txtDiaChi.setText(""); txtEmail.setText(""); txtSDT.setText(""); txtMaLop.setText("");
        });

        panel.add(northGroup, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= 2. TAB QUẢN LÝ MÔN HỌC (CÓ LỌC THEO KHOA & HIỆN TẤT CẢ) =================
    private JPanel createMonHocPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        MonHocDAO dao = new MonHocDAO();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã MH", "Tên Môn Học", "Số Tín Chỉ", "Mã Khoa"}, 0);
        JTable table = new JTable(model);

        Runnable loadAllData = () -> {
            model.setRowCount(0);
            for (MonHoc mh : dao.getAllMonHoc()) {
                model.addRow(new Object[]{mh.getMaMH(), mh.getTenMH(), mh.getSoTinChi(), mh.getMaKhoa()});
            }
        };
        loadAllData.run();

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtMaMH = new JTextField(15);
        JTextField txtTenMH = new JTextField(15);
        JTextField txtTinChi = new JTextField(15);
        JTextField txtMaKhoa = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Mã MH:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(txtMaMH, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(new JLabel("Tên Môn Học:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(txtTenMH, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Số Tín Chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(txtTinChi, gbc);
        gbc.gridx = 2; gbc.gridy = 1; inputPanel.add(new JLabel("Mã Khoa:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; inputPanel.add(txtMaKhoa, gbc);

        // Bộ lọc theo Khoa cho Môn học
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc môn học theo Khoa"));
        JComboBox<String> cbKhoa = new JComboBox<>();
        cbKhoa.addItem("-- Chọn Khoa để lọc --");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ma_khoa FROM khoa")) {
            while (rs.next()) { cbKhoa.addItem(rs.getString("ma_khoa")); }
        } catch (SQLException e) { e.printStackTrace(); }

        JButton btnFilter = new JButton("Lọc Theo Khoa");
        JButton btnShowAll = new JButton("Hiện Tất Cả Môn Học");
        filterPanel.add(new JLabel("Chọn khoa:"));
        filterPanel.add(cbKhoa);
        filterPanel.add(btnFilter);
        filterPanel.add(btnShowAll);

        JPanel northGroup = new JPanel(new BorderLayout());
        northGroup.add(inputPanel, BorderLayout.CENTER);
        northGroup.add(filterPanel, BorderLayout.SOUTH);

        btnFilter.addActionListener(e -> {
            String selectedKhoa = (String) cbKhoa.getSelectedItem();
            if (selectedKhoa == null || selectedKhoa.startsWith("--")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khoa!");
                return;
            }
            model.setRowCount(0);
            for (MonHoc mh : dao.getAllMonHoc()) {
                if (mh.getMaKhoa().equalsIgnoreCase(selectedKhoa)) {
                    model.addRow(new Object[]{mh.getMaMH(), mh.getTenMH(), mh.getSoTinChi(), mh.getMaKhoa()});
                }
            }
        });

        btnShowAll.addActionListener(e -> {
            cbKhoa.setSelectedIndex(0);
            loadAllData.run();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && !e.getValueIsAdjusting()) {
                txtMaMH.setText(model.getValueAt(row, 0).toString());
                txtMaMH.setEnabled(false);
                txtTenMH.setText(model.getValueAt(row, 1).toString());
                txtTinChi.setText(model.getValueAt(row, 2).toString());
                txtMaKhoa.setText(model.getValueAt(row, 3).toString());
            }
        });

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnReset = new JButton("Làm mới");
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnReset);

        btnAdd.addActionListener(e -> {
            try {
                MonHoc mh = new MonHoc(txtMaMH.getText().trim(), txtTenMH.getText().trim(), Integer.parseInt(txtTinChi.getText().trim()), txtMaKhoa.getText().trim());
                if (dao.insertMonHoc(mh)) {
                    JOptionPane.showMessageDialog(this, "Thêm môn học thành công!");
                    loadAllData.run();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi nhập liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                MonHoc mh = new MonHoc(txtMaMH.getText().trim(), txtTenMH.getText().trim(), Integer.parseInt(txtTinChi.getText().trim()), txtMaKhoa.getText().trim());
                if (dao.updateMonHoc(mh)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadAllData.run();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && dao.deleteMonHoc(model.getValueAt(row, 0).toString())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadAllData.run();
            }
        });

        btnReset.addActionListener(e -> {
            txtMaMH.setText(""); txtMaMH.setEnabled(true);
            txtTenMH.setText(""); txtTinChi.setText(""); txtMaKhoa.setText("");
        });

        panel.add(northGroup, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= 3. TAB QUẢN LÝ ĐIỂM / KẾT QUẢ =================
    private JPanel createKetQuaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        KetQuaDAO dao = new KetQuaDAO();

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Mã SV", "Mã MH", "Học Kỳ", "Điểm QT", "Điểm Thi", "Tổng Kết", "ĐD Chữ", "Kết Quả"}, 0);
        JTable table = new JTable(model);

        Runnable loadAllData = () -> {
            model.setRowCount(0);
            for (KetQua kq : dao.getAllKetQua()) {
                model.addRow(new Object[]{kq.getId(), kq.getMaSV(), kq.getMaMH(), kq.getHocKy(), kq.getDiemQuaTrinh(), kq.getDiemThi(), kq.getDiemTongKet(), kq.getDiemChu(), kq.getKetQua()});
            }
        };
        loadAllData.run();

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtID = new JTextField(5); txtID.setEnabled(false);
        JTextField txtMaSV = new JTextField(12);
        JTextField txtMaMH = new JTextField(12);
        JTextField txtHocKy = new JTextField(12);
        JTextField txtDiemQT = new JTextField(12);
        JTextField txtDiemThi = new JTextField(12);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Mã SV:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(txtMaSV, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(new JLabel("Mã MH:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(txtMaMH, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Học Kỳ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(txtHocKy, gbc);
        gbc.gridx = 2; gbc.gridy = 1; inputPanel.add(new JLabel("Điểm QT:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; inputPanel.add(txtDiemQT, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Điểm Thi:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(txtDiemThi, gbc);

        // Bộ lọc điểm theo Sinh Viên
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc bảng điểm theo Sinh Viên"));
        JTextField txtFilterSV = new JTextField(12);
        JButton btnFilter = new JButton("Lọc Theo Mã SV");
        JButton btnShowAll = new JButton("Hiện Tất Cả Bảng Điểm");
        filterPanel.add(new JLabel("Nhập Mã SV:"));
        filterPanel.add(txtFilterSV);
        filterPanel.add(btnFilter);
        filterPanel.add(btnShowAll);

        JPanel northGroup = new JPanel(new BorderLayout());
        northGroup.add(inputPanel, BorderLayout.CENTER);
        northGroup.add(filterPanel, BorderLayout.SOUTH);

        btnFilter.addActionListener(e -> {
            String svFind = txtFilterSV.getText().trim();
            if (svFind.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sinh viên!");
                return;
            }
            model.setRowCount(0);
            for (KetQua kq : dao.getAllKetQua()) {
                if (kq.getMaSV().equalsIgnoreCase(svFind)) {
                    model.addRow(new Object[]{kq.getId(), kq.getMaSV(), kq.getMaMH(), kq.getHocKy(), kq.getDiemQuaTrinh(), kq.getDiemThi(), kq.getDiemTongKet(), kq.getDiemChu(), kq.getKetQua()});
                }
            }
        });

        btnShowAll.addActionListener(e -> {
            txtFilterSV.setText("");
            loadAllData.run();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && !e.getValueIsAdjusting()) {
                txtID.setText(model.getValueAt(row, 0).toString());
                txtMaSV.setText(model.getValueAt(row, 1).toString());
                txtMaMH.setText(model.getValueAt(row, 2).toString());
                txtHocKy.setText(model.getValueAt(row, 3).toString());
                txtDiemQT.setText(model.getValueAt(row, 4).toString());
                txtDiemThi.setText(model.getValueAt(row, 5).toString());
            }
        });

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnReset = new JButton("Làm mới");
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnReset);

        btnAdd.addActionListener(e -> {
            try {
                KetQua kq = new KetQua(0, txtMaSV.getText().trim(), txtMaMH.getText().trim(), txtHocKy.getText().trim(), Float.parseFloat(txtDiemQT.getText().trim()), Float.parseFloat(txtDiemThi.getText().trim()), 0, "", "");
                if (dao.insertKetQua(kq)) {
                    JOptionPane.showMessageDialog(this, "Nhập điểm thành công!");
                    loadAllData.run();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi định dạng điểm!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtID.getText().trim());
                KetQua kq = new KetQua(id, txtMaSV.getText().trim(), txtMaMH.getText().trim(), txtHocKy.getText().trim(), Float.parseFloat(txtDiemQT.getText().trim()), Float.parseFloat(txtDiemThi.getText().trim()), 0, "", "");
                if (dao.updateKetQua(kq)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật điểm thành công!");
                    loadAllData.run();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && dao.deleteKetQua(Integer.parseInt(model.getValueAt(row, 0).toString()))) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadAllData.run();
            }
        });

        btnReset.addActionListener(e -> {
            txtID.setText(""); txtMaSV.setText(""); txtMaMH.setText(""); txtHocKy.setText(""); txtDiemQT.setText(""); txtDiemThi.setText("");
        });

        panel.add(northGroup, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= 4. TAB QUẢN LÝ LỚP =================
    private JPanel createLopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã Lớp", "Tên Lớp", "Mã Khoa"}, 0);
        JTable table = new JTable(model);

        Runnable loadAllData = () -> {
            model.setRowCount(0);
            String sql = "SELECT * FROM lop";
            try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString("ma_lop"), rs.getString("ten_lop"), rs.getString("ma_khoa")});
                }
            } catch (SQLException e) { e.printStackTrace(); }
        };
        loadAllData.run();

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtMaLop = new JTextField(15);
        JTextField txtTenLop = new JTextField(15);
        JTextField txtMaKhoa = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Mã Lớp:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(txtMaLop, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(new JLabel("Tên Lớp:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(txtTenLop, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Mã Khoa:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(txtMaKhoa, gbc);

        // Bộ lọc Lớp theo Khoa
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc lớp học theo Khoa"));
        JComboBox<String> cbKhoa = new JComboBox<>();
        cbKhoa.addItem("-- Chọn Khoa --");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ma_khoa FROM khoa")) {
            while (rs.next()) { cbKhoa.addItem(rs.getString("ma_khoa")); }
        } catch (SQLException e) { e.printStackTrace(); }

        JButton btnFilter = new JButton("Lọc Lớp Theo Khoa");
        JButton btnShowAll = new JButton("Hiện Tất Cả Lớp");
        filterPanel.add(new JLabel("Chọn khoa:"));
        filterPanel.add(cbKhoa);
        filterPanel.add(btnFilter);
        filterPanel.add(btnShowAll);

        JPanel northGroup = new JPanel(new BorderLayout());
        northGroup.add(inputPanel, BorderLayout.CENTER);
        northGroup.add(filterPanel, BorderLayout.SOUTH);

        btnFilter.addActionListener(e -> {
            String khoaSel = (String) cbKhoa.getSelectedItem();
            if (khoaSel == null || khoaSel.startsWith("--")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khoa!");
                return;
            }
            model.setRowCount(0);
            String sql = "SELECT * FROM lop WHERE ma_khoa = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, khoaSel);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString("ma_lop"), rs.getString("ten_lop"), rs.getString("ma_khoa")});
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        });

        btnShowAll.addActionListener(e -> {
            cbKhoa.setSelectedIndex(0);
            loadAllData.run();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && !e.getValueIsAdjusting()) {
                txtMaLop.setText(model.getValueAt(row, 0).toString());
                txtMaLop.setEnabled(false);
                txtTenLop.setText(model.getValueAt(row, 1).toString());
                txtMaKhoa.setText(model.getValueAt(row, 2).toString());
            }
        });

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnReset = new JButton("Làm mới");
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnReset);

        btnAdd.addActionListener(e -> {
            String sql = "INSERT INTO lop (ma_lop, ten_lop, ma_khoa) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtMaLop.getText().trim());
                pstmt.setString(2, txtTenLop.getText().trim());
                pstmt.setString(3, txtMaKhoa.getText().trim());
                if (pstmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm lớp thành công!");
                    loadAllData.run();
                }
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Thêm lớp thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnUpdate.addActionListener(e -> {
            String sql = "UPDATE lop SET ten_lop=?, ma_khoa=? WHERE ma_lop=?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtTenLop.getText().trim());
                pstmt.setString(2, txtMaKhoa.getText().trim());
                pstmt.setString(3, txtMaLop.getText().trim());
                if (pstmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật lớp thành công!");
                    loadAllData.run();
                }
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String sql = "DELETE FROM lop WHERE ma_lop = ?";
                try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, model.getValueAt(row, 0).toString());
                    if (pstmt.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa lớp thành công!");
                        loadAllData.run();
                    }
                } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Không thể xóa lớp đang chứa sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
            }
        });

        btnReset.addActionListener(e -> {
            txtMaLop.setText(""); txtMaLop.setEnabled(true);
            txtTenLop.setText(""); txtMaKhoa.setText("");
        });

        panel.add(northGroup, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= 5. TAB QUẢN LÝ KHOA =================
    private JPanel createKhoaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        KhoaDAO dao = new KhoaDAO();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã Khoa", "Tên Khoa"}, 0);
        JTable table = new JTable(model);

        Runnable loadAllData = () -> {
            model.setRowCount(0);
            for (Khoa k : dao.getAllKhoa()) {
                model.addRow(new Object[]{k.getMaKhoa(), k.getTenKhoa()});
            }
        };
        loadAllData.run();

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtMaKhoa = new JTextField(15);
        JTextField txtTenKhoa = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Mã Khoa:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(txtMaKhoa, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(new JLabel("Tên Khoa:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(txtTenKhoa, gbc);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && !e.getValueIsAdjusting()) {
                txtMaKhoa.setText(model.getValueAt(row, 0).toString());
                txtMaKhoa.setEnabled(false);
                txtTenKhoa.setText(model.getValueAt(row, 1).toString());
            }
        });

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnReset = new JButton("Làm mới");
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnReset);

        btnAdd.addActionListener(e -> {
            try {
                Khoa k = new Khoa(txtMaKhoa.getText().trim(), txtTenKhoa.getText().trim());
                if (dao.insertKhoa(k)) {
                    JOptionPane.showMessageDialog(this, "Thêm khoa thành công!");
                    loadAllData.run();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                Khoa k = new Khoa(txtMaKhoa.getText().trim(), txtTenKhoa.getText().trim());
                if (dao.updateKhoa(k)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadAllData.run();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE); }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && dao.deleteKhoa(model.getValueAt(row, 0).toString())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadAllData.run();
            }
        });

        btnReset.addActionListener(e -> {
            txtMaKhoa.setText(""); txtMaKhoa.setEnabled(true);
            txtTenKhoa.setText("");
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
}