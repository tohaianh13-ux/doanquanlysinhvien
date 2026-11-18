public class KetQua {
    private int id;
    private String maSV;
    private String maMH;
    private String hocKy;
    private float diemQuaTrinh;
    private float diemThi;
    private float diemTongKet;
    private String diemChu;
    private String ketQua;

    public KetQua() {}

    public KetQua(int id, String maSV, String maMH, String hocKy, float diemQuaTrinh, float diemThi, float diemTongKet, String diemChu, String ketQua) {
        this.id = id;
        this.maSV = maSV;
        this.maMH = maMH;
        this.hocKy = hocKy;
        this.diemQuaTrinh = diemQuaTrinh;
        this.diemThi = diemThi;
        this.diemTongKet = diemTongKet;
        this.diemChu = diemChu;
        this.ketQua = ketQua;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public String getHocKy() { return hocKy; }
    public void setHocKy(String hocKy) { this.hocKy = hocKy; }
    public float getDiemQuaTrinh() { return diemQuaTrinh; }
    public void setDiemQuaTrinh(float diemQuaTrinh) { this.diemQuaTrinh = diemQuaTrinh; }
    public float getDiemThi() { return diemThi; }
    public void setDiemThi(float diemThi) { this.diemThi = diemThi; }
    public float getDiemTongKet() { return diemTongKet; }
    public void setDiemTongKet(float diemTongKet) { this.diemTongKet = diemTongKet; }
    public String getDiemChu() { return diemChu; }
    public void setDiemChu(String diemChu) { this.diemChu = diemChu; }
    public String getKetQua() { return ketQua; }
    public void setKetQua(String ketQua) { this.ketQua = ketQua; }
}
