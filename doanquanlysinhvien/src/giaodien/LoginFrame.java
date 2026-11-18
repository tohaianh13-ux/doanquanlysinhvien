import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private TaiKhoanDAO dao;

    public LoginFrame() {
        dao = new TaiKhoanDAO();
        setTitle("Đăng Nhập - Hệ Thống Quản Lý Sinh Viên");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelTitle = new JPanel();
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitle.add(lblTitle);
        add(panelTitle, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelForm.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField();
        panelForm.add(txtUsername);

        panelForm.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelButton = new JPanel();
        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnExit = new JButton("Thoát");

        panelButton.add(btnLogin);
        panelButton.add(btnExit);
        add(panelButton, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            TaiKhoan tk = dao.login(user, pass);
            if (tk != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công với quyền: " + tk.getRole());
                new MainFrame(tk).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
