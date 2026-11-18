import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhoaDAO {
    public List<Khoa> getAllKhoa() {
        List<Khoa> list = new ArrayList<>();
        String sql = "SELECT * FROM khoa";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Khoa(rs.getString("ma_khoa"), rs.getString("ten_khoa")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertKhoa(Khoa k) {
        String sql = "INSERT INTO khoa (ma_khoa, ten_khoa) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, k.getMaKhoa());
            pstmt.setString(2, k.getTenKhoa());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateKhoa(Khoa k) {
        String sql = "UPDATE khoa SET ten_khoa = ? WHERE ma_khoa = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, k.getTenKhoa());
            pstmt.setString(2, k.getMaKhoa());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteKhoa(String maKhoa) {
        String sql = "DELETE FROM khoa WHERE ma_khoa = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhoa);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
