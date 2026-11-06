
package pharmacare.payment;

import pharmacare.interfaces.Pembayaran;

/**
 * Implementasi pembayaran menggunakan kartu debit atau kredit.
 */
public class DebitKredit implements Pembayaran {

    private static final double BIAYA_TAMBAHAN = 0.01;

    /**
     * Mengembalikan nama metode pembayaran.
     */
    @Override
    public String getNamaMetode() {
        return "Debit/Kredit";
    }

    /**
     * Menghitung biaya tambahan sebesar 1 persen.
     */
    @Override
    public double hitungBiayaTambahan(double subtotal) {
        return subtotal * BIAYA_TAMBAHAN;
    }
}
