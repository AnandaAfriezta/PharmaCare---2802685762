package pharmacare;

import pharmacare.model.*;
import pharmacare.service.*;
import pharmacare.payment.*;
import pharmacare.interfaces.Pembayaran;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class sebagai pusat navigasi aplikasi PharmaCare berbasis terminal.
 */
public class Main {

    private static final Scanner input = new Scanner(System.in);
    private static final ObatService obatService = new ObatService();
    private static final TransaksiService transaksiService = new TransaksiService();
    private static final ApotekerService apotekerService = new ApotekerService();
    private static final Keranjang keranjangPelanggan = new Keranjang();
    private static final LaporanService laporanService =
            new LaporanService(transaksiService);
    private static final List<Resep> daftarResep = new ArrayList<>();

    private static Pelanggan pelangganAktif;
    private static int nomorTransaksi = 1;
    private static int nomorResep = 1;

    /**
     * Menyiapkan data awal dan menjalankan menu utama aplikasi.
     */
    public static void main(String[] args) {
        siapkanDataAwal();

        int pilihan;
        do {
            tampilkanHeader("MENU UTAMA");
            System.out.println("1. Menu Pelanggan");
            System.out.println("2. Menu Apoteker / Admin");
            System.out.println("3. Laporan Penjualan");
            System.out.println("4. Riwayat Transaksi");
            System.out.println("5. Demonstrasi OOP");
            System.out.println("0. Keluar");
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    menuPelanggan();
                    break;
                case 2:
                    menuApoteker();
                    break;
                case 3:
                    menuLaporan();
                    break;
                case 4:
                    tampilkanHeader("RIWAYAT TRANSAKSI");
                    transaksiService.tampilkanRiwayatTransaksi();
                    jeda();
                    break;
                case 5:
                    demonstrasiOOP();
                    jeda();
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan PharmaCare.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia.");
                    jeda();
            }
        } while (pilihan != 0);
    }

    /**
     * Mengisi katalog dengan beberapa data obat contoh agar aplikasi dapat langsung diuji.
     */
    private static void siapkanDataAwal() {
        obatService.tambahObat(
                new ObatBebas("OB001", "Paracetamol", 5000, 20));
        obatService.tambahObat(
                new ObatBebasTerbatas("OB002", "CTM", 8000, 15));
        obatService.tambahObat(
                new ObatKeras("OB003", "Amoxicillin", 15000, 10));
    }

    /**
     * Menampilkan menu yang dapat digunakan pelanggan.
     */
    private static void menuPelanggan() {
        if (pelangganAktif == null) {
            tampilkanHeader("REGISTRASI PELANGGAN");
            String nama = bacaTeks("Nama pelanggan: ");
            String email = bacaTeks("Email pelanggan: ");
            pelangganAktif = new Pelanggan("C001", nama, email);
        }

        int pilihan;
        do {
            tampilkanHeader("MENU PELANGGAN");
            System.out.println("Pelanggan: " + pelangganAktif.getNama());
            System.out.println("1. Lihat katalog obat");
            System.out.println("2. Tambah obat ke keranjang");
            System.out.println("3. Lihat keranjang");
            System.out.println("4. Checkout / pembayaran");
            System.out.println("5. Riwayat transaksi");
            System.out.println("0. Kembali ke menu utama");
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    tampilkanHeader("KATALOG OBAT");
                    obatService.tampilkanObat();
                    jeda();
                    break;
                case 2:
                    tambahObatKeKeranjang(keranjangPelanggan);
                    break;
                case 3:
                    tampilkanHeader("KERANJANG BELANJA");
                    keranjangPelanggan.tampilkanKeranjang();
                    jeda();
                    break;
                case 4:
                    prosesCheckout(keranjangPelanggan);
                    break;
                case 5:
                    tampilkanHeader("RIWAYAT TRANSAKSI");
                    transaksiService.tampilkanRiwayatTransaksi();
                    jeda();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia.");
                    jeda();
            }
        } while (pilihan != 0);
    }

    /**
     * Memilih obat dan jumlah pembelian, termasuk memeriksa kebutuhan resep dokter.
     */
    private static void tambahObatKeKeranjang(Keranjang keranjang) {
        tampilkanHeader("TAMBAH OBAT KE KERANJANG");
        obatService.tampilkanObat();
        String id = bacaTeks("Masukkan ID obat: ");
        Obat obat = obatService.cariObat(id);

        if (obat == null) {
            System.out.println("Obat tidak ditemukan.");
            jeda();
            return;
        }

        int jumlah = bacaInt("Jumlah pembelian: ");
        boolean resepValid = false;

        if (obat instanceof ObatKeras) {
            Resep resep = cariResepValidPelanggan();
            if (resep == null) {
                System.out.println("Obat keras memerlukan resep dokter yang divalidasi.");
                String buat = bacaTeks("Ajukan resep sekarang? (Y/N): ");
                if (buat.equalsIgnoreCase("Y")) {
                    String dokter = bacaTeks("Nama dokter: ");
                    Resep resepBaru = new Resep(
                            String.format("RSP%03d", nomorResep++),
                            dokter,
                            pelangganAktif.getNama());
                    daftarResep.add(resepBaru);
                    System.out.println("Resep " + resepBaru.getId()
                            + " dibuat dengan status Menunggu Validasi.");
                    System.out.println("Silakan minta apoteker memvalidasi resep melalui menu Admin.");
                }
                jeda();
                return;
            }
            resepValid = true;
        }

        boolean berhasil = keranjang.tambahObat(obat, jumlah, resepValid);
        if (berhasil) {
            System.out.println(obat.getNama() + " berhasil ditambahkan ke keranjang.");
            if (obat instanceof ObatBebasTerbatas) {
                System.out.println("Peringatan: "
                        + ((ObatBebasTerbatas) obat).getPeringatanPenggunaan());
            }
        } else {
            System.out.println("Obat tidak dapat ditambahkan. Periksa jumlah, stok, "
                    + "atau batas maksimal pembelian (5 unit untuk obat bebas terbatas).");
        }
        jeda();
    }

    /**
     * Mencari resep valid milik pelanggan yang sedang menggunakan aplikasi.
     */
    private static Resep cariResepValidPelanggan() {
        for (Resep resep : daftarResep) {
            if (resep.getNamaPasien().equalsIgnoreCase(pelangganAktif.getNama())
                    && resep.isValid()) {
                return resep;
            }
        }
        return null;
    }

    /**
     * Memilih metode pembayaran dan memproses checkout keranjang pelanggan.
     */
    private static void prosesCheckout(Keranjang keranjang) {
        tampilkanHeader("CHECKOUT");
        if (keranjang.getDaftarItem().isEmpty()) {
            System.out.println("Keranjang masih kosong.");
            jeda();
            return;
        }

        keranjangPelanggan.tampilkanKeranjang();
        System.out.println("\nMetode pembayaran:");
        System.out.println("1. Tunai (tanpa biaya tambahan)");
        System.out.println("2. Debit/Kredit (biaya 1%)");
        System.out.println("3. QRIS (biaya 0,7%)");
        int pilihan = bacaInt("Pilih metode pembayaran: ");

        Pembayaran pembayaran;
        switch (pilihan) {
            case 1:
                pembayaran = new Tunai();
                break;
            case 2:
                pembayaran = new DebitKredit();
                break;
            case 3:
                pembayaran = new QRIS();
                break;
            default:
                System.out.println("Metode pembayaran tidak tersedia. Checkout dibatalkan.");
                jeda();
                return;
        }

        String idTransaksi = String.format("TRX%03d", nomorTransaksi++);
        Transaksi transaksi = transaksiService.checkout(
                idTransaksi, pelangganAktif, keranjang, pembayaran);

        if (transaksi != null) {
            System.out.println("\n--- PEMBAYARAN BERHASIL ---");
            System.out.println("ID Transaksi : " + transaksi.getId());
            System.out.println("Subtotal     : Rp" + transaksi.getSubtotal());
            System.out.println("Biaya Tambah : Rp" + transaksi.getBiayaTambahan());
            System.out.println("Total Bayar  : Rp" + transaksi.getTotalBayar());
            System.out.println("Metode       : " + transaksi.getMetodePembayaran());
            System.out.println("Status       : " + transaksi.getStatus());
        }
        jeda();
    }

    /**
     * Menampilkan menu pengelolaan obat, validasi resep, dan status transaksi untuk apoteker.
     */
    private static void menuApoteker() {
        int pilihan;
        do {
            tampilkanHeader("MENU APOTEKER / ADMIN");
            System.out.println("1. Lihat daftar obat");
            System.out.println("2. Tambah obat baru");
            System.out.println("3. Edit data obat");
            System.out.println("4. Hapus obat");
            System.out.println("5. Restock obat");
            System.out.println("6. Lihat stok menipis");
            System.out.println("7. Lihat dan validasi resep");
            System.out.println("8. Perbarui status transaksi");
            System.out.println("0. Kembali ke menu utama");
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    tampilkanHeader("DAFTAR OBAT");
                    obatService.tampilkanObat();
                    jeda();
                    break;

                case 2:
                    tambahObatBaru();
                    break;

                case 3:
                    editObat();
                    break;

                case 4:
                    hapusObat();
                    break;

                case 5:
                    restockObat();
                    break;

                case 6:
                    tampilkanHeader("STOK MENIPIS");
                    obatService.tampilkanStokMenipis();
                    jeda();
                    break;

                case 7:
                    menuValidasiResep();
                    break;

                case 8:
                    perbaruiStatusTransaksi();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Pilihan tidak tersedia.");
                    jeda();
            }
        } while (pilihan != 0);
    }

    /**
     * Memasukkan obat baru berdasarkan kategori obat yang dipilih apoteker.
     */
    private static void tambahObatBaru() {
        tampilkanHeader("TAMBAH OBAT BARU");
        String id = bacaTeks("ID obat: ");
        String nama = bacaTeks("Nama obat: ");
        double harga = bacaDouble("Harga obat: Rp");
        int stok = bacaInt("Stok awal: ");

        System.out.println("Kategori: 1. Obat Bebas  2. Obat Bebas Terbatas  3. Obat Keras");
        int kategori = bacaInt("Pilih kategori: ");
        Obat obat;
        switch (kategori) {
            case 1:
                obat = new ObatBebas(id, nama, harga, stok);
                break;
            case 2:
                obat = new ObatBebasTerbatas(id, nama, harga, stok);
                break;
            case 3:
                obat = new ObatKeras(id, nama, harga, stok);
                break;
            default:
                System.out.println("Kategori tidak tersedia.");
                jeda();
                return;
        }

        System.out.println(obatService.tambahObat(obat)
                ? "Obat berhasil ditambahkan."
                : "Gagal menambahkan obat. Pastikan ID belum digunakan.");
        jeda();
    }

    /**
     * Mengedit nama dan harga obat berdasarkan ID yang dipilih.
     */
    private static void editObat() {
        tampilkanHeader("EDIT DATA OBAT");
        obatService.tampilkanObat();

        String id = bacaTeks("\nMasukkan ID obat yang akan diedit: ");

        Obat obat = obatService.cariObat(id);

        if (obat == null) {
            System.out.println("Obat tidak ditemukan.");
            jeda();
            return;
        }

        System.out.println("\nData obat saat ini:");
        System.out.println("Nama  : " + obat.getNama());
        System.out.println("Harga : Rp" + obat.getHarga());

        String nama = bacaTeks("Nama obat baru: ");
        double harga = bacaDouble("Harga obat baru: Rp");

        boolean berhasil = obatService.perbaruiObat(
            id, nama, harga
        );

        System.out.println(berhasil
            ? "Data obat berhasil diperbarui."
            : "Gagal memperbarui data obat."
        );

        jeda();
    }

    /**
     * Menghapus obat dari katalog berdasarkan ID yang dipilih.
     */
    private static void hapusObat() {
        tampilkanHeader("HAPUS DATA OBAT");
        obatService.tampilkanObat();

        String id = bacaTeks("\nMasukkan ID obat yang akan dihapus: ");

        Obat obat = obatService.cariObat(id);

        if (obat == null) {
            System.out.println("Obat tidak ditemukan.");
            jeda();
            return;
        }

        System.out.println("Obat yang akan dihapus: " + obat.getNama());

        String konfirmasi = bacaTeks(
            "Apakah Anda yakin ingin menghapus obat ini? (Y/N): "
        );

        if (konfirmasi.equalsIgnoreCase("Y")) {
            System.out.println(obatService.hapusObat(id)
                ? "Obat berhasil dihapus."
                : "Gagal menghapus obat."
            );
        } else {
            System.out.println("Penghapusan obat dibatalkan.");
        }

        jeda();
    }

    /**
     * Menambahkan jumlah stok obat yang sudah terdaftar.
     */
    private static void restockObat() {
        tampilkanHeader("RESTOCK OBAT");
        obatService.tampilkanObat();
        String id = bacaTeks("ID obat yang akan direstock: ");
        int jumlah = bacaInt("Jumlah stok tambahan: ");
        System.out.println(obatService.restockObat(id, jumlah)
                ? "Restock berhasil."
                : "Restock gagal. Periksa ID obat dan jumlah stok.");
        jeda();
    }

    /**
     * Menampilkan resep yang tersimpan dan memproses keputusan validasi apoteker.
     */
    private static void menuValidasiResep() {
        tampilkanHeader("VALIDASI RESEP");
        if (daftarResep.isEmpty()) {
            System.out.println("Belum ada resep yang diajukan.");
            jeda();
            return;
        }

        for (Resep resep : daftarResep) {
            apotekerService.tampilkanResep(resep);
            System.out.println("------------------------------");
        }

        String id = bacaTeks("Masukkan ID resep yang akan divalidasi: ");
        Resep resepDipilih = null;
        for (Resep resep : daftarResep) {
            if (resep.getId().equalsIgnoreCase(id)) {
                resepDipilih = resep;
                break;
            }
        }

        if (resepDipilih == null) {
            System.out.println("Resep tidak ditemukan.");
        } else if (!resepDipilih.getStatus().equals("Menunggu Validasi")) {
            System.out.println("Resep sudah diproses dengan status: " + resepDipilih.getStatus());
        } else {
            String keputusan = bacaTeks("Setujui resep? (Y/N): ");
            apotekerService.validasiResep(
                    resepDipilih, keputusan.equalsIgnoreCase("Y"));
        }
        jeda();
    }

    /**
     * Memperbarui status transaksi menjadi selesai atau dibatalkan sesuai aturan transaksi.
     */
    private static void perbaruiStatusTransaksi() {
        tampilkanHeader("UPDATE STATUS TRANSAKSI");
        transaksiService.tampilkanRiwayatTransaksi();
        String id = bacaTeks("ID transaksi: ");
        System.out.println("1. Selesai");
        System.out.println("2. Dibatalkan");
        int pilihan = bacaInt("Pilih status baru: ");
        String status;
        if (pilihan == 1) {
            status = "Selesai";
        } else if (pilihan == 2) {
            status = "Dibatalkan";
        } else {
            System.out.println("Pilihan status tidak tersedia.");
            jeda();
            return;
        }

        System.out.println(transaksiService.ubahStatusTransaksi(id, status)
                ? "Status transaksi berhasil diperbarui."
                : "Perubahan status ditolak atau transaksi tidak ditemukan.");
        jeda();
    }

    /**
     * Menampilkan pilihan laporan penjualan harian atau bulanan.
     */
    private static void menuLaporan() {
        int pilihan;
        do {
            tampilkanHeader("LAPORAN PENJUALAN");
            System.out.println("1. Laporan harian (hari ini)");
            System.out.println("2. Laporan bulanan (bulan ini)");
            System.out.println("0. Kembali ke menu utama");
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    laporanService.laporanHarian(LocalDate.now());
                    jeda();
                    break;
                case 2:
                    laporanService.laporanBulanan(YearMonth.now());
                    jeda();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia.");
                    jeda();
            }
        } while (pilihan != 0);
    }

    /**
     * Memperlihatkan polymorphism pada abstract class Obat dan interface Pembayaran.
     */
    private static void demonstrasiOOP() {
        tampilkanHeader("DEMONSTRASI OOP - POLYMORPHISM");
        Obat obatBebas = new ObatBebas("D001", "Paracetamol", 5000, 20);
        Obat obatKeras = new ObatKeras("D002", "Amoxicillin", 15000, 10);
        System.out.println("Referensi Obat pertama : " + obatBebas.getJenisObat());
        System.out.println("Referensi Obat kedua   : " + obatKeras.getJenisObat());

        Pembayaran[] metodePembayaran = {
            new Tunai(), new DebitKredit(), new QRIS()
        };
        double subtotalContoh = 100000;
        System.out.println("\nPolymorphism interface Pembayaran:");
        for (Pembayaran metode : metodePembayaran) {
            double biaya = metode.hitungBiayaTambahan(subtotalContoh);
            System.out.println(metode.getNamaMetode()
                    + " | Biaya tambahan: Rp" + biaya);
        }
        jeda();
    }

    /**
     * Menampilkan judul halaman dengan format konsisten.
     */
    private static void tampilkanHeader(String judul) {
        System.out.println("\n========================================");
        System.out.println("             PHARMACARE SYSTEM");
        System.out.println("             " + judul);
        System.out.println("========================================");
    }

    /**
     * Membaca input teks dari pengguna.
     */
    private static String bacaTeks(String pesan) {
        System.out.print(pesan);
        return input.nextLine().trim();
    }

    /**
     * Membaca input bilangan bulat dan mengulang jika format input tidak valid.
     */
    private static int bacaInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            String nilai = input.nextLine().trim();
            try {
                return Integer.parseInt(nilai);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang valid.");
            }
        }
    }

    /**
     * Membaca input angka desimal dan mengulang jika format input tidak valid.
     */
    private static double bacaDouble(String pesan) {
        while (true) {
            System.out.print(pesan);
            String nilai = input.nextLine().trim().replace(',', '.');
            try {
                double angka = Double.parseDouble(nilai);
                if (angka >= 0) {
                    return angka;
                }
                System.out.println("Nilai tidak boleh negatif.");
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang valid.");
            }
        }
    }

    /**
     * Menunggu pengguna menekan Enter sebelum kembali ke menu sebelumnya.
     */
    private static void jeda() {
        System.out.println("\nTekan Enter untuk melanjutkan...");
        input.nextLine();
    }
}