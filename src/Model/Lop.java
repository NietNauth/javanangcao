package Model;

public class Lop {
    private String maLop;
    private String tenLop;
    private String maKhoa;
    private String nienKhoa;

    public Lop() {}

    public Lop(String maLop, String tenLop, String maKhoa, String nienKhoa) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maKhoa = maKhoa;
        this.nienKhoa = nienKhoa;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getNienKhoa() {
        return nienKhoa;
    }

    public void setNienKhoa(String nienKhoa) {
        this.nienKhoa = nienKhoa;
    }

    @Override
    public String toString() {
        return maLop + " (" + tenLop + ")";
    }
}
