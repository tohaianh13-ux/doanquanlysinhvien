import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Chú ý đúng tên server instance SQLEXPRESS và tên database doanqlsinhvien của bạn
    private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=doanqlsinhvien;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "quanlysv_user";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
