public class SinhVien {
    private String maSV;
    private String hoTen;
    private String gioiTinh;
    private String ngaySinh;
    private String diaChi;
    private String email;
    private String soDienThoai;
    private String maLop;

    public SinhVien() {}

    public SinhVien(String maSV, String hoTen, String gioiTinh, String ngaySinh, String diaChi, String email, String soDienThoai, String maLop) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.maLop = maLop;
    }

    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
}
