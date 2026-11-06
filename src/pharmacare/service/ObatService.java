
package pharmacare.service;

import java.util.ArrayList;
import java.util.List;
import pharmacare.model.Obat;

/**
 * Service untuk mengelola data dan persediaan obat.
 */
public class ObatService {

    private final List<Obat> daftarObat = new ArrayList<>();

    private static final int BATAS_STOK_MINIMUM = 5;

    /**
     * Menambahkan obat baru ke dalam daftar.
     */
    public boolean tambahObat(Obat obat) {
        if (obat == null || cariObat(obat.getId()) != null) {
            return false;
        }

        daftarObat.add(obat);
        return true;
    }

    /**
     * Menampilkan seluruh daftar obat.
     */
    public void tampilkanObat() {
        if (daftarObat.isEmpty()) {
            System.out.println("Belum ada data obat.");
            return;
        }

        for (Obat obat : daftarObat) {
            System.out.println(
                obat.getId() + " | "
                + obat.getNama() + " | "
                + obat.getJenisObat() + " | Rp"
                + obat.getHarga() + " | Stok: "
                + obat.getStok()
            );
        }
    }

    /**
     * Mencari obat berdasarkan ID.
     */
    public Obat cariObat(String id) {
        for (Obat obat : daftarObat) {
            if (obat.getId().equalsIgnoreCase(id)) {
                return obat;
            }
        }

        return null;
    }

    /**
     * Menambahkan stok obat berdasarkan ID.
     */
    public boolean restockObat(String id, int jumlah) {
        Obat obat = cariObat(id);

        if (obat == null || jumlah <= 0) {
            return false;
        }

        obat.setStok(obat.getStok() + jumlah);
        return true;
    }

    /**
     * Menampilkan obat yang stoknya mencapai batas minimum.
     */
    public void tampilkanStokMenipis() {
        boolean ditemukan = false;

        for (Obat obat : daftarObat) {
            if (obat.getStok() <= BATAS_STOK_MINIMUM) {
                System.out.println(
                    obat.getNama() + " - Sisa stok: " + obat.getStok()
                );
                ditemukan = true;
            }
        }

        if (!ditemukan) {
            System.out.println("Tidak ada obat dengan stok menipis.");
        }
    }
}
