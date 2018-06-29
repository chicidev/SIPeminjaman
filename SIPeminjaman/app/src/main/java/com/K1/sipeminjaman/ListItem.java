package com.K1.sipeminjaman;

/**
 * Created by PungkiDwiGiantoro on 07/06/2018.
 */

public class ListItem {
    private final String Tambahan;
    private String Nama_Gedung;
    private String ID;
    private String Keperluan;
    private String Lama_Pinjam;
    private String Tanggal;
    private String Status;

    public ListItem (String id,String nama_gedung, String keperluan,String lama_pinjam, String tanggal,String tambahan, String status){
        this.ID = id;
        this.Nama_Gedung = nama_gedung;
        this.Keperluan = keperluan;
        this.Lama_Pinjam = lama_pinjam;
        this.Keperluan = keperluan;
        this.Tanggal = tanggal;
        this.Tambahan = tambahan;
        this.Status = status;
    }

    public String getGedung() {
        return Nama_Gedung;
    }
    public String getKeperluan() {
        return Keperluan;
    }
    public String getStatus() {
        return Status;
    }
    public String getTanggal() {
        return Tanggal;
    }
    public String getNama_Gedung() { return Nama_Gedung; }
    public String getTambahan() { return Tambahan; }
    public String getID() {return ID;}
    public String getLama() {return Lama_Pinjam;}

}
