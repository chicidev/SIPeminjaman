package com.K1.sipeminjaman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.Menu;
import android.widget.TextView;

public class Detail_Riwayat extends AppCompatActivity {
    private String nama,keperluan,tanggal,durasi,tambahan,status;
    private TextView a1, a2, a3, a4, a5, a6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail__riwayat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        keperluan = intent.getStringExtra("keperluan");
        tanggal = intent.getStringExtra("tanggal");
        durasi = intent.getStringExtra("lama");
        tambahan = intent.getStringExtra("tambahan");

        a1 = findViewById(R.id.dt_gedung);
        a2 = findViewById(R.id.dt_dura);
        a3 = findViewById(R.id.dt_kp);
        a4 = findViewById(R.id.dt_tg);
        a5 = findViewById(R.id.dt_tambah);
        a6 = findViewById(R.id.rw_status);

        a1.setText(nama);
        a2.setText(durasi);
        a3.setText(keperluan);
        a4.setText(tanggal);
        a5.setText(tambahan);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : finish();
                                     break;
        }
        return true;
    }
}
