
package pharmacare.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pharmacare.model.Obat;
import pharmacare.model.Transaksi;

/**
 * Service untuk menghasilkan laporan penjualan PharmaCare.
 */
public class LaporanService {

    private final TransaksiService transaksiService;

    /**
     * Constructor untuk menerima data transaksi.
     */
    public LaporanService(TransaksiService transaksiService) {
        this.transaksiService = transaksiService;
    }

    /**
     * Menampilkan laporan penjualan harian.
     */
    public void laporanHarian(LocalDate tanggal) {
        cetakLaporan(
            "LAPORAN PENJUALAN HARIAN",
            tanggal,
            tanggal
        );
    }

    /**
     * Menampilkan laporan penjualan bulanan.
     */
    public void laporanBulanan(YearMonth bulan) {
        cetakLaporan(
            "LAPORAN PENJUALAN BULANAN",
            bulan.atDay(1),
            bulan.atEndOfMonth()
        );
    }

    /**
     * Mengolah dan menampilkan laporan berdasarkan rentang tanggal.
     */
    private void cetakLaporan(
            String judul,
            LocalDate tanggalMulai,
            LocalDate tanggalSelesai) {

        List<Transaksi> daftarTransaksi =
            transaksiService.getDaftarTransaksi();

        int jumlahTransaksi = 0;
        int totalUnitTerjual = 0;
        double totalPendapatan = 0;

        Map<String, Integer> penjualanObat = new HashMap<>();
        Map<String, Double> pendapatanMetode = new HashMap<>();

        for (Transaksi transaksi : daftarTransaksi) {

            LocalDate tanggalTransaksi =
                transaksi.getTanggalTransaksi().toLocalDate();

            // Memeriksa periode dan mengecualikan transaksi dibatalkan.
            if (tanggalTransaksi.isBefore(tanggalMulai)
                    || tanggalTransaksi.isAfter(tanggalSelesai)
                    || transaksi.getStatus().equals("Dibatalkan")) {
                continue;
            }

            jumlahTransaksi++;
            totalPendapatan += transaksi.getTotalBayar();

            // Menghitung penjualan setiap obat.
            for (Map.Entry<Obat, Integer> item :
                    transaksi.getDaftarItem().entrySet()) {

                String namaObat = item.getKey().getNama();
                int jumlah = item.getValue();

                penjualanObat.put(
                    namaObat,
                    penjualanObat.getOrDefault(namaObat, 0) + jumlah
                );

                totalUnitTerjual += jumlah;
            }

            // Menghitung pendapatan berdasarkan metode pembayaran.
            String metode = transaksi.getMetodePembayaran();

            pendapatanMetode.put(
                metode,
                pendapatanMetode.getOrDefault(metode, 0.0)
                    + transaksi.getTotalBayar()
            );
        }

        System.out.println("\n========================================");
        System.out.println("       " + judul);
        System.out.println("========================================");

        System.out.println("Periode          : "
            + tanggalMulai + " s.d. " + tanggalSelesai);

        System.out.println("Jumlah Transaksi : " + jumlahTransaksi);
        System.out.println("Unit Terjual     : " + totalUnitTerjual);
        System.out.println("Total Pendapatan : Rp"
            + String.format("%.0f", totalPendapatan));

        System.out.println("\n--- OBAT TERLARIS ---");

        if (penjualanObat.isEmpty()) {
            System.out.println("Belum ada penjualan obat.");
        } else {

            String obatTerlaris = "";
            int jumlahTerlaris = 0;

            for (Map.Entry<String, Integer> item :
                    penjualanObat.entrySet()) {

                System.out.println(
                    item.getKey() + " : " + item.getValue() + " unit"
                );

                if (item.getValue() > jumlahTerlaris) {
                    obatTerlaris = item.getKey();
                    jumlahTerlaris = item.getValue();
                }
            }

            System.out.println("Obat Terlaris    : "
                + obatTerlaris + " (" + jumlahTerlaris + " unit)");
        }

        System.out.println("\n--- PENDAPATAN PER METODE PEMBAYARAN ---");

        if (pendapatanMetode.isEmpty()) {
            System.out.println("Belum ada transaksi.");
        } else {

            for (Map.Entry<String, Double> item :
                    pendapatanMetode.entrySet()) {

                System.out.println(
                    item.getKey() + " : Rp"
                    + String.format("%.0f", item.getValue())
                );
            }
        }

        System.out.println("========================================");
    }
}
