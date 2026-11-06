
package pharmacare.model;

/**
 * Class untuk merepresentasikan obat keras yang memerlukan resep dokter.
 */
public class ObatKeras extends Obat {

    /**
     * Constructor untuk menginisialisasi data obat keras.
     */
    public ObatKeras(String id, String nama, double harga, int stok) {
        super(id, nama, harga, stok);
    }

    /**
     * Memvalidasi pembelian berdasarkan stok dan keabsahan resep.
     */
    @Override
    public boolean validasiPembelian(int jumlah, boolean resepValid) {
        return jumlah > 0
                && jumlah <= getStok()
                && resepValid;
    }

    /**
     * Mengembalikan kategori obat.
     */
    @Override
    public String getJenisObat() {
        return "Obat Keras";
    }
}