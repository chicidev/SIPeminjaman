package com.K1.sipeminjaman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void pilihMenu(View v){
        String menu = v.getTag().toString();
        Intent intent;

        switch (menu){
            case "profil" :
                intent = new Intent(this,Profil.class);
                startActivity(intent);
                break;
            case "pinjam" :
                intent = new Intent(this,Peminjaman.class);
                startActivity(intent);
                break;
            case "register" :
                intent = new Intent(this,Peminjaman.class);
                startActivity(intent);
                break;
            default         :
                break;

        }
    }
}
