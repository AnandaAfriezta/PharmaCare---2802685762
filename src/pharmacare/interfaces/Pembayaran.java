
package pharmacare.interfaces;

/**
 * Interface yang menjadi kontrak untuk seluruh metode pembayaran.
 */
public interface Pembayaran {

    /**
     * Mengembalikan nama metode pembayaran.
     */
    String getNamaMetode();

    /**
     * Menghitung biaya tambahan berdasarkan subtotal transaksi.
     */
    double hitungBiayaTambahan(double subtotal);
}