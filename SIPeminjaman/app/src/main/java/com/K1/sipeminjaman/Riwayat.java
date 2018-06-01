package com.K1.sipeminjaman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.Menu;
import android.widget.LinearLayout;

public class Riwayat extends AppCompatActivity {
    private LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat);

        parent = (LinearLayout)findViewById(R.id.rwyt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_riwayat,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bersihkan :       parent.removeAllViews();
                                        break;
            case android.R.id.home :    finish();
                                        break;
        }
        return true;
    }
}
