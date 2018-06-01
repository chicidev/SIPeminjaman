package com.K1.sipeminjaman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import static com.K1.sipeminjaman.Res.*;

public class Menu extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout : new AlertDialog.Builder(this)
                                .setTitle("Logout")
                                .setMessage("Anda Yakin ?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        logout();
                                    }
                                })
                                .setNegativeButton("Tidak",null)
                                .show();
        }
        return true;
    }

    public void logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        finish();
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
            case "riwayat" :
                intent = new Intent(this,Riwayat.class);
                startActivity(intent);
                break;
            case "info" :
                intent = new Intent(this,InfoJadwal.class);
                startActivity(intent);
            default         :
                break;

        }
    }

}
