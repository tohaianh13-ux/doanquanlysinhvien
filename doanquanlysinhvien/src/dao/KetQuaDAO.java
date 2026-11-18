import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KetQuaDAO {
    
    // Tự động tính toán điểm tổng kết, điểm chữ và kết quả
    private void calculateGrade(KetQua kq) {
        float tk = (kq.getDiemQuaTrinh() * 0.4f) + (kq.getDiemThi() * 0.6f);
        kq.setDiemTongKet((float) Math.round(tk * 10) / 10);
        
        if (tk >= 8.5f) kq.setDiemChu("A");
        else if (tk >= 7.0f) kq.setDiemChu("B");
        else if (tk >= 5.5f) kq.setDiemChu("C");
        else if (tk >= 4.0f) kq.setDiemChu("D");
        else kq.setDiemChu("F");

        kq.setKetQua(tk >= 4.0f ? "Đạt" : "Không đạt");
    }

    public List<KetQua> getAllKetQua() {
        List<KetQua> list = new ArrayList<>();
        String sql = "SELECT * FROM ket_qua";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new KetQua(
                    rs.getInt("id"),
                    rs.getString("ma_sv"),
                    rs.getString("ma_mh"),
                    rs.getString("hoc_ky"),
                    rs.getFloat("diem_qua_trinh"),
                    rs.getFloat("diem_thi"),
                    rs.getFloat("diem_tong_ket"),
                    rs.getString("diem_chu"),
                    rs.getString("ket_qua")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertKetQua(KetQua kq) {
        calculateGrade(kq);
        String sql = "INSERT INTO ket_qua (ma_sv, ma_mh, hoc_ky, diem_qua_trinh, diem_thi, diem_tong_ket, diem_chu, ket_qua) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kq.getMaSV());
            pstmt.setString(2, kq.getMaMH());
            pstmt.setString(3, kq.getHocKy());
            pstmt.setFloat(4, kq.getDiemQuaTrinh());
            pstmt.setFloat(5, kq.getDiemThi());
            pstmt.setFloat(6, kq.getDiemTongKet());
            pstmt.setString(7, kq.getDiemChu());
            pstmt.setString(8, kq.getKetQua());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateKetQua(KetQua kq) {
        calculateGrade(kq);
        String sql = "UPDATE ket_qua SET ma_sv=?, ma_mh=?, hoc_ky=?, diem_qua_trinh=?, diem_thi=?, diem_tong_ket=?, diem_chu=?, ket_qua=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kq.getMaSV());
            pstmt.setString(2, kq.getMaMH());
            pstmt.setString(3, kq.getHocKy());
            pstmt.setFloat(4, kq.getDiemQuaTrinh());
            pstmt.setFloat(5, kq.getDiemThi());
            pstmt.setFloat(6, kq.getDiemTongKet());
            pstmt.setString(7, kq.getDiemChu());
            pstmt.setString(8, kq.getKetQua());
            pstmt.setInt(9, kq.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteKetQua(int id) {
        String sql = "DELETE FROM ket_qua WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
