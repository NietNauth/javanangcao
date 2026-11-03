package Model;

public class Diem {
    private int id;
    private String maSV;
    private String maMon;
    private double diemChuyenCan;
    private double diemGiuaKy;
    private double diemThi;
    private double diemTB;
    private String xepLoai;
    private String trangThai;

    // Các trường hiển thị bổ trợ (JOIN)
    private String hoTenSV;
    private String tenMon;
    private int soTinChi;

    public Diem() {}

    public Diem(int id, String maSV, String maMon, double diemChuyenCan, double diemGiuaKy, double diemThi) {
        this.id = id;
        this.maSV = maSV;
        this.maMon = maMon;
        this.diemChuyenCan = diemChuyenCan;
        this.diemGiuaKy = diemGiuaKy;
        this.diemThi = diemThi;
        tinhDiemTBVaXepLoai();
    }

    public void tinhDiemTBVaXepLoai() {
        // Công thức: Chuyên cần 10%, Giữa kỳ 30%, Thi kết thúc 60%
        this.diemTB = Math.round((this.diemChuyenCan * 0.1 + this.diemGiuaKy * 0.3 + this.diemThi * 0.6) * 100.0) / 100.0;
        
        if (this.diemTB >= 9.0) {
            this.xepLoai = "Xuất sắc";
        } else if (this.diemTB >= 8.0) {
            this.xepLoai = "Giỏi";
        } else if (this.diemTB >= 6.5) {
            this.xepLoai = "Khá";
        } else if (this.diemTB >= 5.0) {
            this.xepLoai = "Trung bình";
        } else {
            this.xepLoai = "Yếu";
        }

        this.trangThai = (this.diemTB >= 5.0 && this.diemThi >= 4.0) ? "Đạt" : "Học lại";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public double getDiemChuyenCan() {
        return diemChuyenCan;
    }

    public void setDiemChuyenCan(double diemChuyenCan) {
        this.diemChuyenCan = diemChuyenCan;
    }

    public double getDiemGiuaKy() {
        return diemGiuaKy;
    }

    public void setDiemGiuaKy(double diemGiuaKy) {
        this.diemGiuaKy = diemGiuaKy;
    }

    public double getDiemThi() {
        return diemThi;
    }

    public void setDiemThi(double diemThi) {
        this.diemThi = diemThi;
    }

    public double getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(double diemTB) {
        this.diemTB = diemTB;
    }

    public String getXepLoai() {
        return xepLoai;
    }

    public void setXepLoai(String xepLoai) {
        this.xepLoai = xepLoai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getHoTenSV() {
        return hoTenSV;
    }

    public void setHoTenSV(String hoTenSV) {
        this.hoTenSV = hoTenSV;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }
}
