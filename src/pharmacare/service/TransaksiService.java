
package pharmacare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pharmacare.model.Obat;
import pharmacare.model.Pelanggan;
import pharmacare.model.Transaksi;
import pharmacare.interfaces.Pembayaran;

/**
 * Service untuk memproses checkout dan mengelola transaksi.
 */
public class TransaksiService {

    private final List<Transaksi> daftarTransaksi = new ArrayList<>();

    /**
     * Memproses checkout dan membuat transaksi pelanggan.
     */
    public Transaksi checkout(
            String idTransaksi,
            Pelanggan pelanggan,
            Keranjang keranjang,
            Pembayaran pembayaran) {

        if (pelanggan == null || keranjang == null || pembayaran == null) {
            System.out.println("Data checkout tidak lengkap.");
            return null;
        }

        Map<Obat, Integer> itemKeranjang = keranjang.getDaftarItem();

        if (itemKeranjang.isEmpty()) {
            System.out.println("Checkout gagal: keranjang masih kosong.");
            return null;
        }

        // Validasi ulang seluruh stok sebelum mengurangi stok.
        for (Map.Entry<Obat, Integer> item : itemKeranjang.entrySet()) {

            Obat obat = item.getKey();
            int jumlah = item.getValue();

            if (obat.getStok() < jumlah) {
                System.out.println(
                    "Checkout gagal: stok " + obat.getNama() + " tidak mencukupi."
                );
                return null;
            }
        }

        // Menghitung biaya transaksi.
        double subtotal = keranjang.hitungSubtotal();
        double biaya = pembayaran.hitungBiayaTambahan(subtotal);
        double total = subtotal + biaya;

        // Pembayaran diasumsikan berhasil sesuai studi kasus.
        // Stok dikurangi setelah seluruh validasi berhasil.
        for (Map.Entry<Obat, Integer> item : itemKeranjang.entrySet()) {
            item.getKey().kurangiStok(item.getValue());
        }

        Transaksi transaksi = new Transaksi(
            idTransaksi,
            pelanggan,
            subtotal,
            biaya,
            total,
            pembayaran.getNamaMetode(),
            itemKeranjang
        );

        daftarTransaksi.add(transaksi);
        keranjang.kosongkanKeranjang();

        return transaksi;
    }

    /**
     * Menampilkan seluruh riwayat transaksi.
     */
    public void tampilkanRiwayatTransaksi() {

        if (daftarTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }

        for (Transaksi transaksi : daftarTransaksi) {
            System.out.println(
                transaksi.getId()
                + " | " + transaksi.getPelanggan().getNama()
                + " | Rp" + transaksi.getTotalBayar()
                + " | " + transaksi.getMetodePembayaran()
                + " | " + transaksi.getStatus()
            );
        }
    }

    
    /**
     * Mencari transaksi berdasarkan ID.
     */
    public Transaksi cariTransaksi(String id) {

        for (Transaksi transaksi : daftarTransaksi) {
            if (transaksi.getId().equalsIgnoreCase(id)) {
                return transaksi;
            }
        }

        return null;
    }

    /**
     * Memperbarui status transaksi berdasarkan ID.
     */
    public boolean ubahStatusTransaksi(String id, String statusBaru) {

        Transaksi transaksi = cariTransaksi(id);

        if (transaksi == null) {
            System.out.println("Transaksi tidak ditemukan.");
            return false;
        }

        return transaksi.ubahStatus(statusBaru);
    }

    public List<Transaksi> getDaftarTransaksi() {
        return new ArrayList<>(daftarTransaksi);
    }
}
