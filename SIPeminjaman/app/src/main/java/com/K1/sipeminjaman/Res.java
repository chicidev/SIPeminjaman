package com.K1.sipeminjaman;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Res {

    //Data Sementara
    public static String username = "";
    public static String nim = "";

    //Data Program
    public static final String session_status = "session_status";
    public final static String tag_json_obj = "json_obj_req";

    //URL server
    private static final String URL = "http://tif16d.simodist.com/Peminjaman/android/";
    public static final String URL_getData = URL+"getData.php";
    public static final String URL_getRiwayat = URL+"getRiwayat.php";
    public static final String URL_login = URL+"login.php";
    public static final String URL_profil = URL+"ubahProfil.php";
    public static final String URL_register = URL+"register.php";
    public static final String URL_ajukan = URL+"pengajuan.php";
    public static final String URL_info = URL+"info.php";
    public static final String URL_delRiwayat = URL+"delRiwayat.php";
    public static final String URL_delRiwayatAll = URL+"delRiwayatAll.php";

    //Shared Preferences
    public static final String my_shared_preferences = "profil" ;
    public static final String data_preferences = "data_preferences";

    // Data Profil
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "nim";
    public final static String TAG_HP = "noHP";
    public final static String TAG_JK = "jk";
    public final static String TAG_ALAMAT = "alamat";

    //Data Fasilitas
    public static final String TAG_DATA_FASILITAS = "data_fasilitas";
    public static final String FASILITAS_ID = "ID_Fasilitas";
    public static final String FASILITAS_NAMA = "Nama_Fasilitas";

    //Data Gedung Item
    public static final String TAG_DATA_GEDUNG = "data_gedung";
    public static final String GEDUNG_ID = "ID_Gedung";
    public static final String GEDUNG_NAMA = "Nama_Gedung";
    public static final String GEDUNG_DESKRIPSI = "Deskripsi";
    public static final String GEDUNG_NIP = "NIP";

    // Data Peminjaman
    public static final String PINJAM_ID = "ID_Peminjam";
    public static final String PINJAM_KEPERLUAN = "Keperluan";
    public static final String PINJAM_LAMA = "Lama_pinjam";
    public static final String PINJAM_TANGGAL = "Tanggal_pinjam";
    public static final String PINJAM_TAMBAHAN = "Tambahan";
    public static final String PINJAM_STATUS = "Status";
    public static final String PINJAM_ARRAY_FASILITAS = "array_fasilitas";

    //Peminjaman Intent Tag
    public static final String INTENT_ARRAY_FAS = "arrayFas";
    public static final String INTENT_ARRAY_FAS_ID = "arrayFas";
    public static final String INTENT_GEDUNG_ID = "id_gedung";
    public static final String INTENT_GEDUNG = "gedung";
    public static final String INTENT_KEPERLUAN = "keperluan";
    public static final String INTENT_DURASI = "lama";
    public static final String INTENT_TANGGAL = "tanggal";
    public static final String INTENT_CATATAN = "tambahan";
    public static final String INTENT_ARRAY = "array";

    //Data Riwayat
    public static final String TAG_DATA_RIWAYAT = "data_riwayat";
    public static final String RIWAYAT_TANGGAL = PINJAM_TANGGAL;
    public static final String RIWAYAT_GEDUNG = GEDUNG_NAMA;

    //


    public static void getRiwayat(final String nim, final Context context) {
        final String TAG = context.getClass().getSimpleName();


        StringRequest strReq = new StringRequest(Request.Method.POST, URL_getRiwayat, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e(TAG, "Get Data Response: " + response.toString());

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

                            Toast.makeText(context, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            String jsonRiwayat = gson.toJson(riwayatJson);
                            SharedPreferences dataPreferences = context.getSharedPreferences(data_preferences, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = dataPreferences.edit();
                            Log.e(TAG, "onResponse: "+jsonRiwayat);
                            editor.putString(TAG_DATA_RIWAYAT, jsonRiwayat);
                            editor.commit();
                            success++;

                        }catch (JSONException e){
                            Log.e(TAG, "ERROR data Riwayat: "+e.getMessage() );

                        }

                        // Jika Data Kosong
                    } else {
                        SharedPreferences dataPreferences = context.getSharedPreferences(data_preferences, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = dataPreferences.edit();
                        Log.e(TAG, "onResponse: Data Riwayat Kosong");
                        editor.putString(TAG_DATA_RIWAYAT, "");
                        editor.commit();
                        Toast.makeText(context,
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(context, "JSON ERROR "+e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Riwayat Error: " + error.getMessage());
                Toast.makeText(context, "", Toast.LENGTH_LONG).show();

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
