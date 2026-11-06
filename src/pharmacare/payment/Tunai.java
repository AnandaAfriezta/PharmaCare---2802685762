
package pharmacare.payment;

import pharmacare.interfaces.Pembayaran;

/**
 * Implementasi pembayaran menggunakan uang tunai.
 */
public class Tunai implements Pembayaran {

    /**
     * Mengembalikan nama metode pembayaran.
     */
    @Override
    public String getNamaMetode() {
        return "Tunai";
    }

    /**
     * Menghitung biaya tambahan pembayaran tunai.
     */
    @Override
    public double hitungBiayaTambahan(double subtotal) {
        return 0;
    }
}
