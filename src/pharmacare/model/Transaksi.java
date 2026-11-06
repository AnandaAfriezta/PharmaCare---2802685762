
package pharmacare.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class untuk menyimpan informasi transaksi dan rincian obat.
 */
public class Transaksi {

    private String id;
    private Pelanggan pelanggan;
    private double subtotal;
    private double biayaTambahan;
    private double totalBayar;
    private String metodePembayaran;
    private String status;
    private LocalDateTime tanggalTransaksi;
    private Map<Obat, Integer> daftarItem;

    /**
     * Constructor untuk menginisialisasi data transaksi.
     */
    public Transaksi(
            String id,
            Pelanggan pelanggan,
            double subtotal,
            double biayaTambahan,
            double totalBayar,
            String metodePembayaran,
            Map<Obat, Integer> daftarItem) {

        this.id = id;
        this.pelanggan = pelanggan;
        this.subtotal = subtotal;
        this.biayaTambahan = biayaTambahan;
        this.totalBayar = totalBayar;
        this.metodePembayaran = metodePembayaran;
        this.status = "Diproses";
        this.tanggalTransaksi = LocalDateTime.now();
        this.daftarItem = new LinkedHashMap<>(daftarItem);
    }

    /**
     * Mengembalikan ID transaksi.
     */
    public String getId() {
        return id;
    }

    /**
     * Mengembalikan pelanggan transaksi.
     */
    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    /**
     * Mengembalikan subtotal transaksi.
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Mengembalikan biaya tambahan.
     */
    public double getBiayaTambahan() {
        return biayaTambahan;
    }

    /**
     * Mengembalikan total pembayaran.
     */
    public double getTotalBayar() {
        return totalBayar;
    }

    /**
     * Mengembalikan metode pembayaran.
     */
    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    /**
     * Mengembalikan status transaksi.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Mengembalikan tanggal transaksi.
     */
    public LocalDateTime getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    /**
     * Mengembalikan rincian obat dalam transaksi.
     */
    public Map<Obat, Integer> getDaftarItem() {
        return new LinkedHashMap<>(daftarItem);
    }

    /**
     * Mengubah status transaksi berdasarkan aturan.
     */
    public boolean ubahStatus(String statusBaru) {

        if (status.equals("Diproses")) {

            if (statusBaru.equals("Selesai")
                    || statusBaru.equals("Dibatalkan")) {

                this.status = statusBaru;
                return true;
            }
        }

        return false;
    }
}
