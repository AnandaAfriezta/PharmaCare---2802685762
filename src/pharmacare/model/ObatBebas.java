
package pharmacare.model;

/**
 * Class untuk merepresentasikan obat yang dapat dibeli tanpa syarat khusus.
 */
public class ObatBebas extends Obat {

    /**
     * Constructor untuk menginisialisasi data obat bebas.
     */
    public ObatBebas(String id, String nama, double harga, int stok) {
        super(id, nama, harga, stok);
    }

    /**
     * Memvalidasi pembelian obat bebas berdasarkan ketersediaan stok.
     */
    @Override
    public boolean validasiPembelian(int jumlah, boolean resepValid) {
        return jumlah > 0 && jumlah <= getStok();
    }

    /**
     * Mengembalikan kategori obat.
     */
    @Override
    public String getJenisObat() {
        return "Obat Bebas";
    }
}