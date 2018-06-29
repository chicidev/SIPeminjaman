package com.K1.sipeminjaman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import static com.K1.sipeminjaman.Res.*;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Konfirmasi extends AppCompatActivity {
    private Context context;
    private TextView nama,gedung,durasi,tanggal,tambahan,keperluan;
    private TableLayout tableLayout;
    private RelativeLayout relative;
    private String [][] fas;
    private SharedPreferences sharedPrefrenences;
    private ProgressDialog pDialog;
    private String TAG;
    private String Nama,ID;
    private String GEDUNG;
    private String KEPERLUAN;
    private int DURASI;
    private String TANGGAL;
    private String CATATAN;
    private int ID_GEDUNG;
    private ConnectivityManager conMgr;
    private String[] FASID ;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.konfirmasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //inisialisasi
        nama = findViewById(R.id.km_nama);
        durasi = findViewById(R.id.km_lama);
        gedung = findViewById(R.id.km_gedung);
        tanggal = findViewById(R.id.km_tanggal);
        tambahan = findViewById(R.id.km_tambahan);
        keperluan = findViewById(R.id.km_keperluan);
        tableLayout = findViewById(R.id.tab_parent);
        relative = findViewById(R.id.km_R_layout);
        TAG = getClass().getSimpleName();

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        sharedPrefrenences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        Nama = sharedPrefrenences.getString(TAG_USERNAME,null);
        ID = sharedPrefrenences.getString(TAG_ID,null);

        gson = new Gson();
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        relative.setMinimumHeight(screenHeight-dpSize(80));

        fas = null;
        Intent intent = getIntent();
        Object[] objects = (Object[])getIntent().getExtras().getSerializable(INTENT_ARRAY);
        if(objects!=null){
            fas = new String[objects.length][];
            for(int i=0;i<objects.length;i++){
                fas[i]=(String[])objects[i];
            }
        }
        ID_GEDUNG = intent.getIntExtra(INTENT_GEDUNG_ID,0);
        GEDUNG = intent.getStringExtra(INTENT_GEDUNG);
        KEPERLUAN = intent.getStringExtra(INTENT_KEPERLUAN);
        DURASI = intent.getIntExtra(INTENT_DURASI,0);
        TANGGAL = intent.getStringExtra(INTENT_TANGGAL);
        CATATAN = intent.getStringExtra(INTENT_CATATAN);
        Bundle bundle = getIntent().getExtras();
        FASID = bundle.getStringArray(INTENT_ARRAY_FAS_ID);
        //boolean fasi = intent.getBooleanExtra(INTENT_ARRAY_FAS,false);

        Log.e(this.getClass().getSimpleName(), "GEDUNG : "+GEDUNG );
        Log.e(this.getClass().getSimpleName(), "KEPERLUAN : "+KEPERLUAN );
        Log.e(this.getClass().getSimpleName(), "DURASI : "+DURASI );
        Log.e(this.getClass().getSimpleName(), "TANGGAL : "+TANGGAL );
        Log.e(this.getClass().getSimpleName(), "TAMBAHAN : "+CATATAN );
        //Log.e(this.getClass().getSimpleName(), "fasi : ");

        nama.setText(Nama);
        gedung.setText(GEDUNG);
        durasi.setText(DURASI+" Jam");
        keperluan.setText(KEPERLUAN);
        tanggal.setText(TANGGAL);
        tambahan.setText(CATATAN);

            for(int i=0;i<fas.length;i++){
                TableRow tb = new TableRow(this);
                TableRow.LayoutParams fasParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tb.setLayoutParams(fasParam);
                tb.setBackground(getResources().getDrawable(R.color.colorPrimary));
                String a = "";
                String b = "";
                for(int j =0;j<fas[i].length;j++ ){
                    if(j==0){
                        Log.e(this.getClass().getSimpleName(), "a: "+fas[i][j] );
                        a = fas[i][j];
                    }else{
                        Log.e(this.getClass().getSimpleName(), "b: "+fas[i][j] );
                        b = fas[i][j];
                    }
                }

                int ab = (int)(getResources().getDimension(R.dimen.kf_item)/getResources().getDisplayMetrics().density);
                TableRow.LayoutParams txt1Param = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,3f);
                TableRow.LayoutParams txt2Param = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1f);

                TextView text1 = new TextView(this);
                text1.setBackground(getResources().getDrawable(R.drawable.tabel_child));
                text1.setLayoutParams(txt1Param);
                text1.setPadding(ab,ab,ab,ab);
                text1.setTextAppearance(this,android.R.style.TextAppearance_Material_Medium);
                text1.setTextColor(getResources().getColor(R.color.defaultText));
                text1.setText(a);

                TextView text2 = new TextView(this);
                text2.setBackground(getResources().getDrawable(R.drawable.tabel_child));
                text2.setPadding(ab,ab,ab,ab);
                text2.setLayoutParams(txt2Param);
                text2.setTextAppearance(this,android.R.style.TextAppearance_Material_Medium);
                text2.setTextColor(getResources().getColor(R.color.defaultText));
                text2.setText(b);
                text2.setGravity(Gravity.CENTER_HORIZONTAL);

                tb.addView(text1);
                tb.addView(text2);
                tableLayout.addView(tb);

            }
    }

    public int dpSize(int size){
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,size,getResources().getDisplayMetrics());
        return margin;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home : finish();
                                     break;
        }
        return true;
    }

    public void km_Batal(View view){
    finish();
    }

    public void tmbAjukan(View view){
        String nim = ID;
        String keperluan = KEPERLUAN;
        int id_gedung = ID_GEDUNG;
        String tanggal = TANGGAL;
        int durasi = DURASI;
        String tambahan = CATATAN;
        String[][] fasilitas = fas;

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            pengajuan(nim,id_gedung,keperluan,tanggal,durasi,tambahan,fasilitas,FASID);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void pengajuan(final String nim, final int id_gedung, final String keperluan, final String tanggal, final int durasi, final String tambahan, final String[][] fasilitas, final String[] fasId) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mengajukan . . .");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_ajukan, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Pengajuan Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Sedang Di Ajukan!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        getRiwayat(Res.nim,context);


                        // menyimpan login ke session

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                String[] fas3 = new String[2];
                Map<String, String> params = new HashMap<String, String>();
                params.put(TAG_ID, nim);
                params.put(GEDUNG_ID, id_gedung+"");
                params.put(PINJAM_KEPERLUAN, keperluan);
                params.put(PINJAM_TANGGAL, tanggal);
                params.put(PINJAM_LAMA, durasi+"");
                params.put(PINJAM_TAMBAHAN, tambahan);
                for (int i=0;i<fasilitas.length;i++){
                    for (int j=0;j<2;j++){
                        switch (j){
                            case 0 : fas3[j]=fasId[i];
                                     break;
                            case 1 : fas3[j]=fasilitas[i][1];
                                     break;
                        }

                    }
                    params.put(PINJAM_ARRAY_FASILITAS+"["+i+"]",gson.toJson(fas3));
                }

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
