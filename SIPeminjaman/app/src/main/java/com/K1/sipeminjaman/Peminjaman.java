package com.K1.sipeminjaman;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import static com.K1.sipeminjaman.Res.*;

public class Peminjaman extends AppCompatActivity{
    private static final String TAG = Peminjaman.class.getSimpleName();
    private EditText tanggal,lama,keperluan,catatan;
    private LinearLayout base;
    private RelativeLayout relativeLayout;
    private Spinner gd;
    private String[][] list;
    private String[][] listGedung;
    private String[] list2;
    private String[] list3;
    private String[][] inputFas;
    private String[] fasID;
    private boolean fasi = true;
    private int spID = 1, tvID = 11,tmbID = 21,fasJum = 1,layID = 51;
    private int lamaJum = 0,gdIndex;
    private SharedPreferences sharedPreferences;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peminjaman);

        base = findViewById(R.id.content);
        tanggal = findViewById(R.id.pm_tanggal);
        gd = findViewById(R.id.pm_gedung);
        lama = findViewById(R.id.pm_lama);
        relativeLayout = findViewById(R.id.R_layout);
        keperluan = findViewById(R.id.pm_keperluan);
        catatan = findViewById(R.id.pm_catatan);

        Gson gson = new Gson();
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        relativeLayout.setMinimumHeight(screenHeight-dpSize(80));
        sharedPreferences = getSharedPreferences(data_preferences, Context.MODE_PRIVATE);
        String jsonText = sharedPreferences.getString(TAG_DATA_FASILITAS, null);
        String jsonGedung = sharedPreferences.getString(TAG_DATA_GEDUNG,null);

        list = gson.fromJson(jsonText, String[][].class);
        listGedung = gson.fromJson(jsonGedung, String [][].class);

        list2 = new String[list.length+1];
        list3 = new String[listGedung.length+1];
        Log.e(TAG,""+jsonText);

        //List for array fasilitas
        for(int i=0;i<=list.length;i++){
            if (i==0){
                list2[i] = "Pilih Fasilitas";
            }else{
                Log.e(TAG, "fasilitas: "+i+" = "+list[i-1][1]);
                list2[i] = list[i-1][1];
            }
        }

        //List for array Gedung
        for(int i=0;i<=listGedung.length;i++){
            if (i==0){
                list3[i] = "Pilih Gedung";
            }else{
                Log.e(TAG,"Gedung : "+i+" = "+listGedung[i-1][1]);
                list3[i] = listGedung[i-1][1];
            }
        }

        //List for array number
        String[] as = angka();
        for (int i=0; i<as.length;i++){
            Log.e(TAG, "onCreate: "+as[i] );
        }

        gd.setAdapter(new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<>(Arrays.asList(list3))));
        gd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refresh();
                gdIndex = gd.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        layoutSetup();
    }


    public int dpSize(int size){
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,size,getResources().getDisplayMetrics());

        return margin;
    }

    public void layoutSetup(){
        int a = (int)(getResources().getDimension(R.dimen.kf_item)/getResources().getDisplayMetrics().density);
        int textSize = (int)(getResources().getDimension(R.dimen.text)/getResources().getDisplayMetrics().density);

        LinearLayout fasilitas = new LinearLayout(this);
        ImageButton tmbAngka = new ImageButton(this);
        Spinner spFas = new Spinner(this,Spinner.MODE_DIALOG);
        TextView tvJumlah = new TextView(this);

        LinearLayout.LayoutParams fasParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fasParam.setMargins(0,0,0,dpSize(5));
        LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
        LinearLayout.LayoutParams spParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,6f);
        LinearLayout.LayoutParams tmbParam = new LinearLayout.LayoutParams(dpSize(40), dpSize(40));
        tmbParam.setMargins(a,0,a,0);
        tmbParam.gravity = Gravity.CENTER_VERTICAL;

        tmbAngka.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tmbAngka.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add));
        tmbAngka.setId(tmbID);
        tmbAngka.setLayoutParams(tmbParam);
        tmbAngka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                TextView a= (TextView) viewGroup.getChildAt(1);
                tampilAngka(a);
                refresh();


            }
        });

        fasilitas.setBackground(getDrawable(R.drawable.border));
        fasilitas.setOrientation(LinearLayout.HORIZONTAL);
        fasilitas.setLayoutParams(fasParam);
        fasilitas.setId(layID);

        ArrayAdapter fasAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<>(Arrays.asList(list2)));
        spFas.setLayoutParams(spParam);
        spFas.setAdapter(fasAdapter);
        spFas.setId(spID);
        spFas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refresh();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        tvJumlah.setTextSize(textSize);
        tvJumlah.setTextAppearance(this,android.R.style.TextAppearance_Material_Medium);
        tvJumlah.setTextColor(getResources().getColor(R.color.defaultText));
        tvJumlah.setId(tvID);
        tvJumlah.setLayoutParams(tvParam);

        spID++;
        tvID++;
        tmbID++;
        layID++;

        fasilitas.addView(spFas);
        fasilitas.addView(tvJumlah);
        fasilitas.addView(tmbAngka);
        base.addView(fasilitas);

    }
    public String[] angka(){
        int min = 1;
        int max = 31;
        String[] angka = new String[max-min];

        for (int i = min;i<max;i++){
            angka[i-1] = Integer.toString(i);
        }
        return angka;
    }

    public void refresh(){
        int jumlah = base.getChildCount();
        fasID = new String[jumlah];
        inputFas = new String[jumlah][2];
        for(int i=0;i<jumlah;i++){
            ViewGroup a = (ViewGroup)base.getChildAt(i);
            int j = a.getChildCount();
            for (int k=0;k<j;k++){
                View view = a.getChildAt(k);
                if(view instanceof Spinner){
                    inputFas[i][k] = ((Spinner) view).getSelectedItem().toString();
                    fasID[i]= Integer.toString(((Spinner)view).getSelectedItemPosition());
                    Log.e(TAG,inputFas[i][k]);
                }else if(view instanceof TextView){
                    inputFas[i][k] = ((TextView) view).getText().toString();
                    Log.e(TAG,inputFas[i][k]);
                }
            }
        }

    }

    public void ajukan(View v){
        refresh();
        if(gd.getSelectedItem().toString()=="Pilih Gedung"){
            Toast.makeText(this,"Isi Data terlebih dahulu",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(keperluan.getText().toString())){
            keperluan.setError("Isi data terlebih dahulu");
            //Toast.makeText(this,"Isi Data terlebih dahulu",Toast.LENGTH_SHORT).show();
        }else if(lamaJum == 0){
            lama.setError("Isi data terlebih dahulu");
            //Toast.makeText(this,"Isi Data terlebih dahulu",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(tanggal.getText().toString())){
            tanggal.setError("Isi data terlebih dahulu");
            //Toast.makeText(this,"Isi Data terlebih dahulu",Toast.LENGTH_SHORT).show();
        }else if(inputFas[0][0]=="Pilih Fasilitas"){
            fasi = false;
            Intent intent = new Intent(this,Konfirmasi.class);
            intent.putExtra(INTENT_GEDUNG,gd.getSelectedItem().toString());
            intent.putExtra(INTENT_GEDUNG_ID,gdIndex);
            intent.putExtra(INTENT_KEPERLUAN,keperluan.getText().toString());
            intent.putExtra(INTENT_DURASI,lamaJum);
            intent.putExtra(INTENT_TANGGAL,tanggal.getText().toString());
            intent.putExtra(INTENT_CATATAN,catatan.getText().toString());
            startActivity(intent);
        }else{
            fasi = true;
            Intent intent = new Intent(this,Konfirmasi.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(INTENT_ARRAY,inputFas);
            bundle.putStringArray(INTENT_ARRAY_FAS_ID,fasID);
            intent.putExtras(bundle);
            intent.putExtra(INTENT_GEDUNG,gd.getSelectedItem().toString());
            intent.putExtra(INTENT_GEDUNG_ID,gdIndex);
            intent.putExtra(INTENT_KEPERLUAN,keperluan.getText().toString());
            intent.putExtra(INTENT_DURASI,lamaJum);
            intent.putExtra(INTENT_TANGGAL,tanggal.getText().toString());
            intent.putExtra(INTENT_CATATAN,catatan.getText().toString());
            startActivity(intent);
        }


    }
    public void hapus(View v){
        Intent intent = new Intent(Peminjaman.this,Peminjaman.class);
        finish();
        startActivity(intent);
    }

    public void tambahFasilitas(View v){
        layoutSetup();
        refresh();
        fasJum++;

    }
    public void delFasilitas(View v){
        if(fasJum!=0) {
            ViewGroup viewGroup = (ViewGroup) base;
            viewGroup.removeViewAt(fasJum - 1);
            inputFas = kurangiArray(inputFas);
            fasID = kurangiArray(fasID);
            fasJum--;
        }
    }

    public String[][] kurangiArray(String[][] fas ){
        String[][] fasNew = new String[fas.length-1][fas[0].length];
        for (int i=0;i<fasNew.length;i++){
            for (int j=0;j<fasNew[i].length;j++){
                fasNew[i][j] = fas[i][j];
            }
        }
        return fasNew;

    }
    public String[] kurangiArray(String[] fas){
        String[] fasNew = new String[fas.length-1];
        for (int i=0;i<fasNew.length;i++){
            fasNew[i] = fas[i];
        }

        return fasNew;
    }
    public void tampilAs(View v){
        final NumberPicker picker =new NumberPicker(this);
        picker.setMaxValue(31);
        picker.setMinValue(1);

        new AlertDialog.Builder(this)
                .setView(picker)
                .setTitle("Lama Pinjam")
                .setCancelable(false)
                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    lamaJum = picker.getValue();
                    lama.setText(lamaJum+" Jam");
                    }
                })
                .setNegativeButton("Batal",null)
                .show();
                refresh();
    }

    public void tampilAngka(final TextView v){
        final NumberPicker picker =new NumberPicker(this);
        picker.setMaxValue(31);
        picker.setMinValue(1);

        new AlertDialog.Builder(this)
                .setView(picker)
                .setTitle("Lama Pinjam")
                .setCancelable(false)
                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        v.setText(""+picker.getValue());
                    }
                })
                .setNegativeButton("Batal",null)
                .show();
                refresh();

    }
    public void tampilDate(View v){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tanggal.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        refresh();
    }
}
