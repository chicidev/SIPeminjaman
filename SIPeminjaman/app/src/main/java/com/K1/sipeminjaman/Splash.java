package com.K1.sipeminjaman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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
import java.util.List;
import java.util.Map;

import static com.K1.sipeminjaman.Res.*;

public class Splash extends AppCompatActivity {
    private static int interval = 3000;
    private SharedPreferences sharedPreferences;
    private ConnectivityManager conMgr;
    private static final String TAG = Splash.class.getSimpleName();
    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(data_preferences, Context.MODE_PRIVATE);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            getData();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void transisi(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();

            }
        },interval);
    }


    private void getData() {

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_getData, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Get Data Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject jObj2,jObj3;

                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Gson gson = new Gson();
                        JSONArray data_gedung = jObj.getJSONArray(TAG_DATA_GEDUNG);
                        JSONArray data_fasilitas = jObj.getJSONArray(TAG_DATA_FASILITAS);
                        String[][] gedungJson= new String[data_gedung.length()][4];
                        String[][] fasilitasJson = new String[data_fasilitas.length()][2];

                        for(int i = 0;i<data_gedung.length();i++){
                            for(int j = 0;j<4;j++){
                                jObj2 = data_gedung.getJSONObject(i);
                                Log.e(TAG, "gedung: "+i+j);
                                switch (j){
                                    case 0 : gedungJson[i][j] = jObj2.getString(GEDUNG_ID);
                                             break;
                                    case 1 : gedungJson[i][j] = jObj2.getString(GEDUNG_NAMA);
                                             break;
                                    case 2 : gedungJson[i][j] = jObj2.getString(GEDUNG_DESKRIPSI);
                                             break;
                                    case 3 : gedungJson[i][j] = jObj2.getString(GEDUNG_NIP);
                                             break;
                                }
                            }
                        }

                        for(int i=0;i<data_fasilitas.length();i++){
                            for(int j=0;j<2;j++){
                                jObj3 = data_fasilitas.getJSONObject(i);
                                Log.e(TAG, "fasilitas: "+i+j);
                                switch (j){
                                    case 0 : fasilitasJson[i][j] = jObj3.getString(FASILITAS_ID);
                                             break;
                                    case 1 : fasilitasJson[i][j] = jObj3.getString(FASILITAS_NAMA);
                                             break;
                                }
                            }
                        }


                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        String jsonGedung = gson.toJson(gedungJson);
                        String jsonFasilitas = gson.toJson(fasilitasJson);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Log.e(TAG, "onResponse: "+jsonGedung);
                        Log.e(TAG, "onResponse: "+jsonFasilitas);
                        editor.putString(TAG_DATA_GEDUNG, jsonGedung);
                        editor.putString(TAG_DATA_FASILITAS, jsonFasilitas);
                        editor.commit();

                        // Memanggil main activity
                        transisi();
                    } else {
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Gagal Memperbarui Data", Toast.LENGTH_LONG).show();
                transisi();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
