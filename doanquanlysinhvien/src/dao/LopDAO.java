import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopDAO {

    public List<Lop> getAllLop() {
        List<Lop> list = new ArrayList<>();
        String sql = "SELECT * FROM lop";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Lop(
                        rs.getString("ma_lop"),
                        rs.getString("ten_lop"),
                        rs.getString("ma_khoa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertLop(Lop l) {
        String sql = "INSERT INTO lop (ma_lop, ten_lop, ma_khoa) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, l.getMaLop());
            pstmt.setString(2, l.getTenLop());
            pstmt.setString(3, l.getMaKhoa());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteLop(String maLop) {
        String sql = "DELETE FROM lop WHERE ma_lop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLop);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
