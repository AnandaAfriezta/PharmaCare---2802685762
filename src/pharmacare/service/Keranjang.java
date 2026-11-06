
package pharmacare.service;

import java.util.LinkedHashMap;
import java.util.Map;
import pharmacare.model.Obat;

/**
 * Service untuk mengelola keranjang belanja pelanggan.
 */
public class Keranjang {

    private final Map<Obat, Integer> daftarItem = new LinkedHashMap<>();

    /**
     * Menambahkan obat ke keranjang setelah validasi pembelian.
     */
    public boolean tambahObat(Obat obat, int jumlah, boolean resepValid) {

        if (obat == null || jumlah <= 0) {
            return false;
        }

        int jumlahSebelumnya = daftarItem.getOrDefault(obat, 0);
        int jumlahTotal = jumlahSebelumnya + jumlah;

        if (!obat.validasiPembelian(jumlahTotal, resepValid)) {
            return false;
        }

        daftarItem.put(obat, jumlahTotal);
        return true;
    }

    /**
     * Menampilkan seluruh isi keranjang.
     */
    public void tampilkanKeranjang() {

        if (daftarItem.isEmpty()) {
            System.out.println("Keranjang masih kosong.");
            return;
        }

        for (Map.Entry<Obat, Integer> item : daftarItem.entrySet()) {

            Obat obat = item.getKey();
            int jumlah = item.getValue();

            System.out.println(
                obat.getNama()
                + " | Jumlah: " + jumlah
                + " | Harga: Rp" + obat.getHarga()
                + " | Subtotal: Rp" + (obat.getHarga() * jumlah)
            );
        }

        System.out.println("Total: Rp" + hitungSubtotal());
    }

    /**
     * Menghitung total harga seluruh obat dalam keranjang.
     */
    public double hitungSubtotal() {

        double subtotal = 0;

        for (Map.Entry<Obat, Integer> item : daftarItem.entrySet()) {
            subtotal += item.getKey().getHarga() * item.getValue();
        }

        return subtotal;
    }

    /**
     * Menghapus seluruh isi keranjang.
     */
    public void kosongkanKeranjang() {
        daftarItem.clear();
    }

    /**
     * Mengembalikan daftar item dalam keranjang.
     */
    public Map<Obat, Integer> getDaftarItem() {
        return new LinkedHashMap<>(daftarItem);
    }
}
