
package pharmacare.payment;

import pharmacare.interfaces.Pembayaran;

/**
 * Implementasi pembayaran menggunakan QRIS.
 */
public class QRIS implements Pembayaran {

    private static final double BIAYA_TAMBAHAN = 0.007;

    /**
     * Mengembalikan nama metode pembayaran.
     */
    @Override
    public String getNamaMetode() {
        return "QRIS";
    }

    /**
     * Menghitung biaya tambahan sebesar 0,7 persen.
     */
    @Override
    public double hitungBiayaTambahan(double subtotal) {
        return subtotal * BIAYA_TAMBAHAN;
    }
}
