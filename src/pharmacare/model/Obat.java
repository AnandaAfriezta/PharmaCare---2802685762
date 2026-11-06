
package pharmacare.model;

/**
 * Abstract class yang menjadi dasar seluruh jenis obat di PharmaCare.
 */
public abstract class Obat {

    // Encapsulation: atribut dibuat private.
    private String id;
    private String nama;
    private double harga;
    private int stok;

    /**
     * Constructor untuk menginisialisasi data obat.
     */
    public Obat(String id, String nama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    /**
     * Mengembalikan ID obat.
     */
    public String getId() {
        return id;
    }

    /**
     * Mengembalikan nama obat.
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengembalikan harga obat.
     */
    public double getHarga() {
        return harga;
    }

    /**
     * Mengubah nama obat.
     */
    public void setNama(String nama) {
        if (nama != null && !nama.trim().isEmpty()) {
            this.nama = nama;
        }
    }

    /**
     * Mengubah harga obat.
     */
    public void setHarga(double harga) {
        if (harga > 0) {
            this.harga = harga;
        }
    }

    /**
     * Mengembalikan jumlah stok obat.
     */
    public int getStok() {
        return stok;
    }

    /**
     * Mengubah stok obat dengan validasi jumlah.
     */
    public void setStok(int stok) {
        if (stok >= 0) {
            this.stok = stok;
        }
    }

    /**
     * Memvalidasi pembelian sesuai kategori obat.
     */
    public abstract boolean validasiPembelian(int jumlah, boolean resepValid);

    /**
     * Mengembalikan jenis kategori obat.
     */
    public abstract String getJenisObat();

    /**
     * Mengurangi stok setelah pembelian berhasil.
     */
    public boolean kurangiStok(int jumlah) {
        if (jumlah > 0 && jumlah <= stok) {
            stok -= jumlah;
            return true;
        }
        return false;
    }
}