
package pharmacare.service;

import pharmacare.model.Resep;

/**
 * Service untuk menangani validasi resep oleh apoteker.
 */
public class ApotekerService {

    /**
     * Memvalidasi resep dokter berdasarkan keputusan apoteker.
     */
    public boolean validasiResep(Resep resep, boolean disetujui) {

        if (resep == null) {
            System.out.println("Resep tidak ditemukan.");
            return false;
        }

        resep.setHasilValidasi(disetujui);

        if (disetujui) {
            System.out.println("Resep berhasil divalidasi.");
        } else {
            System.out.println("Resep ditolak oleh apoteker.");
        }

        return resep.isValid();
    }

    /**
     * Menampilkan informasi dan status resep.
     */
    public void tampilkanResep(Resep resep) {

        if (resep == null) {
            System.out.println("Data resep tidak tersedia.");
            return;
        }

        System.out.println("ID Resep     : " + resep.getId());
        System.out.println("Nama Dokter  : " + resep.getNamaDokter());
        System.out.println("Nama Pasien  : " + resep.getNamaPasien());
        System.out.println("Status Resep : " + resep.getStatus());
    }
}
