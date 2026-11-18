import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {
    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT * FROM sinh_vien";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new SinhVien(
                        rs.getString("ma_sv"),
                        rs.getString("ho_ten"),
                        rs.getString("gioi_tinh"),
                        rs.getString("ngay_sinh"),
                        rs.getString("dia_chi"),
                        rs.getString("email"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("ma_lop")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertSinhVien(SinhVien sv) {
        String sql = "INSERT INTO sinh_vien (ma_sv, ho_ten, gioi_tinh, ngay_sinh, dia_chi, email, so_dien_thoai, ma_lop) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sv.getMaSV());
            pstmt.setString(2, sv.getHoTen());
            pstmt.setString(3, sv.getGioiTinh());
            pstmt.setString(4, sv.getNgaySinh());
            pstmt.setString(5, sv.getDiaChi());
            pstmt.setString(6, sv.getEmail());
            pstmt.setString(7, sv.getSoDienThoai());
            pstmt.setString(8, sv.getMaLop());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSinhVien(String maSV) {
        String sql = "DELETE FROM sinh_vien WHERE ma_sv=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maSV);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}