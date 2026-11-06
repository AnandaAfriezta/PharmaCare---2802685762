
package pharmacare.model;

/**
 * Class untuk menyimpan informasi resep dokter pelanggan.
 */
public class Resep {

    private String id;
    private String namaDokter;
    private String namaPasien;
    private boolean valid;
    private String status;

    /**
     * Constructor untuk membuat resep dengan status menunggu validasi.
     */
    public Resep(String id, String namaDokter, String namaPasien) {
        this.id = id;
        this.namaDokter = namaDokter;
        this.namaPasien = namaPasien;
        this.valid = false;
        this.status = "Menunggu Validasi";
    }

    /**
     * Mengembalikan ID resep.
     */
    public String getId() {
        return id;
    }

    /**
     * Mengembalikan nama dokter.
     */
    public String getNamaDokter() {
        return namaDokter;
    }

    /**
     * Mengembalikan nama pasien.
     */
    public String getNamaPasien() {
        return namaPasien;
    }

    /**
     * Mengembalikan hasil validasi resep.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Mengembalikan status resep.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Mengubah hasil validasi resep oleh apoteker.
     */
    public void setHasilValidasi(boolean valid) {
        this.valid = valid;
        this.status = valid ? "Valid" : "Ditolak";
    }
}
