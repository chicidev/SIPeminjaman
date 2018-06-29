package com.K1.sipeminjaman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.K1.sipeminjaman.Res.*;

public class Riwayat extends AppCompatActivity {
    private ArrayList<ListItem> listItems;
    private TextView txtNoData;
    private ListView listView;
    private LinearLayout parent;
    private SharedPreferences sharedPreferences;
    private String[][] list;
    private static RiwayatAdapter adapter;
    private String TAG = Riwayat.class.getSimpleName();
    private ProgressDialog pDialog;
    private  String jsonRiwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.rw_listView);
        txtNoData = findViewById(R.id.rw_noData);

        sharedPreferences = getSharedPreferences(data_preferences, Context.MODE_PRIVATE);
        jsonRiwayat = sharedPreferences.getString(TAG_DATA_RIWAYAT, null);
        if(jsonRiwayat==""){
            txtNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            setList();

        }

    }

    public void setList(){
        Gson gson= new Gson();
        list = gson.fromJson(jsonRiwayat, String[][].class);

        Log.e(TAG, "Register Response: " +jsonRiwayat);
        listItems = new ArrayList<>();
        setListItems();
        adapter = new RiwayatAdapter(listItems,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItem listItem = listItems.get(i);
                Intent intent = new Intent(Riwayat.this, Detail_Riwayat.class);
                intent.putExtra("nama",listItem.getNama_Gedung());
                intent.putExtra("keperluan",listItem.getKeperluan());
                intent.putExtra("tanggal",listItem.getTanggal());
                intent.putExtra("lama",listItem.getLama());
                intent.putExtra("tambahan",listItem.getTambahan());
                intent.putExtra("status",listItem.getStatus());
                startActivity(intent);

            }
        });
    }
    public void setListItems(){

        for (int i=0;i<list.length;i++){
            String id = list[i][0];
            String gedung = list[i][1];
            String keperluan = list[i][2];
            String lama = list[i][3];
            String tanggal = list[i][4];
            String tambahan = list[i][5];
            String status = list[i][6];
            listItems.add(new ListItem(id,gedung,keperluan,lama,tanggal,tambahan,status));
        }
    }

    public void refreshListView(){
        getRiwayat(nim);
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
            case R.id.bersihkan :       new AlertDialog.Builder(this)
                                        .setTitle("Logout")
                                        .setMessage("Anda Yakin ?")
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                            bersihkan();
                                            }
                                        })
                                        .setNegativeButton("Tidak",null)
                                        .show();
                                        break;
            case android.R.id.home :    finish();
                                        break;
        }
        return true;
    }
    private void bersihkan() {
        TAG = this.getClass().getSimpleName();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memproses . . .");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_delRiwayatAll, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Riwayat Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Riwayat Berhasil Dihapus", jObj.toString());

                        //Toast.makeText(mContext, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Snackbar.make(listView, "Riwayat berhasil dihapus ", Snackbar.LENGTH_LONG)
                                .setAction("No action", null).show();
                        getRiwayat(nim);




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
                Toast.makeText(getApplicationContext(),"Response Error"+
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(TAG_ID, Res.nim);

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

    public void getRiwayat(final String nim) {
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_getRiwayat, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e(TAG, "Get Data Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject jObj2;

                    int success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Gson gson = new Gson();
                        try {
                            JSONArray data_riwayat = jObj.getJSONArray(TAG_DATA_RIWAYAT);
                            String[][] riwayatJson= new String[data_riwayat.length()][7];

                            for(int i = 0;i<data_riwayat.length();i++){
                                for(int j = 0;j<7;j++){
                                    jObj2 = data_riwayat.getJSONObject(i);
                                    Log.e(TAG, "gedung: "+i+j);
                                    switch (j){
                                        case 0 : riwayatJson[i][j] = jObj2.getString(PINJAM_ID);
                                            break;
                                        case 1 : riwayatJson[i][j] = jObj2.getString(GEDUNG_NAMA);
                                            break;
                                        case 2 : riwayatJson[i][j] = jObj2.getString(PINJAM_KEPERLUAN);
                                            break;
                                        case 3 : riwayatJson[i][j] = jObj2.getString(PINJAM_LAMA);
                                            break;
                                        case 4 : riwayatJson[i][j] = jObj2.getString(PINJAM_TANGGAL);
                                            break;
                                        case 5 : riwayatJson[i][j] = jObj2.getString(PINJAM_TAMBAHAN);
                                            break;
                                        case 6 : riwayatJson[i][j] = jObj2.getString(PINJAM_STATUS);
                                            break;
                                    }
                                }
                            }

                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            String jsonRiwayat = gson.toJson(riwayatJson);
                            SharedPreferences dataPreferences = getSharedPreferences(data_preferences, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = dataPreferences.edit();
                            Log.e(TAG, "onResponse: "+jsonRiwayat);
                            editor.putString(TAG_DATA_RIWAYAT, jsonRiwayat);
                            editor.commit();
                            listItems = new ArrayList<>();
                            if(jsonRiwayat==""){
                                txtNoData.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            }else{
                                list = gson.fromJson(jsonRiwayat, String[][].class);
                                setListItems();
                                adapter = new RiwayatAdapter(listItems,getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.invalidateViews();
                            }
                            success++;

                        }catch (JSONException e){
                            Log.e(TAG, "ERROR data Riwayat: "+e.getMessage() );

                        }

                        // Jika Data Kosong
                    } else {
                        SharedPreferences dataPreferences =getSharedPreferences(data_preferences, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = dataPreferences.edit();
                        Log.e(TAG, "onResponse: Data Riwayat Kosong");
                        editor.putString(TAG_DATA_RIWAYAT, "");
                        editor.commit();
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(getApplicationContext(), "JSON ERROR "+e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Riwayat Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(TAG_ID,nim);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
