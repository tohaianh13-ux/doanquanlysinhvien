import java.sql.*;

public class TaiKhoanDAO {
    public TaiKhoan login(String username, String password) {
        String sql = "SELECT * FROM tai_khoan WHERE username = ? AND password = ?";
        Connection conn = DatabaseConnection.getConnection();

        // Kiểm tra nếu kết nối bị null thì trả về luôn để tránh NullPointerException
        if (conn == null) {
            System.out.println("Lỗi: Không thể kết nối đến cơ sở dữ liệu!");
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
