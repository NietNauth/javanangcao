package Model;

public class MonHoc {
    private String maMon;
    private String tenMon;
    private int soTinChi;
    private double heSo;

    public MonHoc() {}

    public MonHoc(String maMon, String tenMon, int soTinChi, double heSo) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
        this.heSo = heSo;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
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

    public double getHeSo() {
        return heSo;
    }

    public void setHeSo(double heSo) {
        this.heSo = heSo;
    }

    @Override
    public String toString() {
        return maMon + " - " + tenMon + " (" + soTinChi + " TC)";
    }
}
