import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonHocDAO {
    public List<MonHoc> getAllMonHoc() {
        List<MonHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM mon_hoc";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MonHoc(
                    rs.getString("ma_mh"),
                    rs.getString("ten_mh"),
                    rs.getInt("so_tin_chi"),
                    rs.getString("ma_khoa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertMonHoc(MonHoc mh) {
        String sql = "INSERT INTO mon_hoc (ma_mh, ten_mh, so_tin_chi, ma_khoa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mh.getMaMH());
            pstmt.setString(2, mh.getTenMH());
            pstmt.setInt(3, mh.getSoTinChi());
            pstmt.setString(4, mh.getMaKhoa());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMonHoc(MonHoc mh) {
        String sql = "UPDATE mon_hoc SET ten_mh=?, so_tin_chi=?, ma_khoa=? WHERE ma_mh=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mh.getTenMH());
            pstmt.setInt(2, mh.getSoTinChi());
            pstmt.setString(3, mh.getMaKhoa());
            pstmt.setString(4, mh.getMaMH());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMonHoc(String maMH) {
        String sql = "DELETE FROM mon_hoc WHERE ma_mh=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maMH);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
