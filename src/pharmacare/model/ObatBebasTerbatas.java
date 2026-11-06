
package pharmacare.model;

/**
 * Class untuk merepresentasikan obat bebas terbatas.
 */
public class ObatBebasTerbatas extends Obat {

    // Batas maksimal pembelian dalam satu transaksi.
    private static final int BATAS_MAKSIMAL = 5;

    /**
     * Constructor untuk menginisialisasi data obat bebas terbatas.
     */
    public ObatBebasTerbatas(String id, String nama, double harga, int stok) {
        super(id, nama, harga, stok);
    }

    /**
     * Memvalidasi pembelian berdasarkan stok dan batas maksimal.
     */
    @Override
    public boolean validasiPembelian(int jumlah, boolean resepValid) {
        return jumlah > 0
                && jumlah <= getStok()
                && jumlah <= BATAS_MAKSIMAL;
    }

    /**
     * Mengembalikan kategori obat.
     */
    @Override
    public String getJenisObat() {
        return "Obat Bebas Terbatas";
    }

    /**
     * Mengembalikan peringatan penggunaan obat.
     */
    public String getPeringatanPenggunaan() {
        return "Gunakan sesuai aturan pakai dan perhatikan dosis.";
    }
}