
package pharmacare.model;

/**
 * Class untuk menyimpan informasi pelanggan PharmaCare.
 */
public class Pelanggan {

    private String id;
    private String nama;
    private String email;

    /**
     * Constructor untuk menginisialisasi data pelanggan.
     */
    public Pelanggan(String id, String nama, String email) {
        this.id = id;
        this.nama = nama;
        this.email = email;
    }

    /**
     * Mengembalikan ID pelanggan.
     */
    public String getId() {
        return id;
    }

    /**
     * Mengembalikan nama pelanggan.
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengembalikan email pelanggan.
     */
    public String getEmail() {
        return email;
    }
}
