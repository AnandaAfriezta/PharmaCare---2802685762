
package pharmacare;

import pharmacare.model.*;
import pharmacare.service.*;
import pharmacare.payment.*;
import pharmacare.interfaces.Pembayaran;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Main class untuk menguji status dan riwayat transaksi.
 */
public class Main {

    /**
     * Menjalankan simulasi perubahan status transaksi.
     */
    public static void main(String[] args) {

        // Membuat pelanggan.
        Pelanggan pelanggan = new Pelanggan(
            "C001", "Andi", "andi@email.com"
        );

        // Menyiapkan data obat.
        ObatService obatService = new ObatService();

        obatService.tambahObat(
            new ObatBebas("OB001", "Paracetamol", 5000, 20)
        );

        // Membuat keranjang.
        Keranjang keranjang = new Keranjang();

        keranjang.tambahObat(
            obatService.cariObat("OB001"), 2, false
        );

        // Memilih pembayaran tunai.
        Pembayaran pembayaran = new Tunai();

        // Membuat transaksi.
        TransaksiService transaksiService = new TransaksiService();

        Transaksi transaksi = transaksiService.checkout(
            "TRX001",
            pelanggan,
            keranjang,
            pembayaran
        );

        System.out.println("==================================");
        System.out.println("       PHARMACARE SYSTEM");
        System.out.println("       STATUS TRANSAKSI");
        System.out.println("==================================");

        if (transaksi != null) {

            System.out.println("\n--- TRANSAKSI DIBUAT ---");
            System.out.println("ID Transaksi : " + transaksi.getId());
            System.out.println("Pelanggan    : "
                    + transaksi.getPelanggan().getNama());
            System.out.println("Total Bayar  : Rp"
                    + transaksi.getTotalBayar());
            System.out.println("Status       : " + transaksi.getStatus());

            // Mengubah status menjadi selesai.
            System.out.println("\n--- UPDATE STATUS ---");

            boolean berhasil = transaksiService.ubahStatusTransaksi(
                "TRX001",
                "Selesai"
            );

            System.out.println(
                berhasil
                ? "Status berhasil diperbarui."
                : "Status gagal diperbarui."
            );

            // Menguji perubahan status yang tidak diperbolehkan.
            boolean perubahanTidakValid =
                transaksiService.ubahStatusTransaksi(
                    "TRX001",
                    "Diproses"
                );

            System.out.println(
                perubahanTidakValid
                ? "Perubahan status berhasil."
                : "Perubahan status ditolak."
            );

            // Menampilkan status terbaru.
            System.out.println("\n--- RIWAYAT TRANSAKSI ---");
            transaksiService.tampilkanRiwayatTransaksi();

            // Membuat laporan penjualan.
            LaporanService laporanService =
                new LaporanService(transaksiService);

            // Menampilkan laporan harian.
            System.out.println("\n");
            laporanService.laporanHarian(LocalDate.now());

            // Menampilkan laporan bulanan.
            System.out.println("\n");
            laporanService.laporanBulanan(YearMonth.now());

            
            // Demonstrasi polymorphism menggunakan abstract class Obat.
            System.out.println("\n--- DEMONSTRASI POLYMORPHISM ---");

            // Referensi superclass Obat untuk objek ObatBebas.
            Obat obatBebas = new ObatBebas(
                "OB001", "Paracetamol", 5000, 20
            );

            // Referensi superclass Obat untuk objek ObatKeras.
            Obat obatKeras = new ObatKeras(
                "OB003", "Amoxicillin", 15000, 10
            );

            // Method yang sama menghasilkan kategori sesuai objeknya.
            System.out.println(
                "Kategori 1 : " + obatBebas.getJenisObat()
            );

            System.out.println(
                "Kategori 2 : " + obatKeras.getJenisObat()
            );

            // Demonstrasi polymorphism menggunakan interface Pembayaran.
            Pembayaran pembayaranQris = new QRIS();

            System.out.println(
                "Pembayaran : " + pembayaranQris.getNamaMetode()
            );
        }

        System.out.println("\n==================================");
    }
}
