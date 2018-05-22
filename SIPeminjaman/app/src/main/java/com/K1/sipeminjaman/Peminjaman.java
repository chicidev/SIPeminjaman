package com.K1.sipeminjaman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class Peminjaman extends AppCompatActivity {
    NumberPicker np;
    LinearLayout fasilitas,base;
    Spinner edtFas,edtFas1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peminjaman);

        np = findViewById(R.id.numDur);
        base = findViewById(R.id.content);



        np.setMinValue(1);
        np.setMaxValue(24);
        np.setWrapSelectorWheel(false);
    }

    public void layoutSetup(){
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics());
        fasilitas = new LinearLayout(this);
        edtFas = new Spinner(this);
        edtFas1 = new Spinner(this);
        fasilitas.setOrientation(LinearLayout.HORIZONTAL);
        fasilitas.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams mgn1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2f);
        LinearLayout.LayoutParams mgn2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);

        mgn1.setMargins(10,10,10,10);
        mgn2.setMargins(10,10,10,10);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.angka,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtFas.setLayoutParams(mgn1);
        edtFas.setAdapter(adapter);
        fasilitas.addView(edtFas);
        edtFas1.setLayoutParams(mgn2);
        edtFas1.setAdapter(adapter);
        fasilitas.addView(edtFas1);
        base.addView(fasilitas);

    }

    public void ajukan(View v){
        Intent intent = new Intent(this,Menu.class);
        startActivity(intent);
    }

    public void tambahFasilitas(View v){
        layoutSetup();

    }
}
