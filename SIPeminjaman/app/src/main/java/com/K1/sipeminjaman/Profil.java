package com.K1.sipeminjaman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static com.K1.sipeminjaman.Login.TAG_ID;
import static com.K1.sipeminjaman.Login.TAG_USERNAME;
import static com.K1.sipeminjaman.Login.TAG_ALAMAT;
import static com.K1.sipeminjaman.Login.TAG_HP;
import static com.K1.sipeminjaman.Login.TAG_JK;
import static com.K1.sipeminjaman.Login.my_shared_preferences;
import static com.K1.sipeminjaman.Login.session_status;

public class Profil extends AppCompatActivity {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private EditText nim,nama,hp,alamat;
    private RadioButton pria,wanita;
    private RadioGroup radioGroup;
    private SharedPreferences sharedpreferences;
    private Boolean session = false;
    private String NIM,NAMA,HP,JK,ALAMAT;
    private ProgressDialog pDialog;
    private String url = server.URL + "ubahProfil.php";
    private String TAG;
    private ConnectivityManager conMgr;
    private String tag_json_obj = "json_obj_req";
    private Button ubah,simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        nim = (EditText)findViewById(R.id.NIM);
        nama = (EditText)findViewById(R.id.Name);
        hp = (EditText)findViewById(R.id.Hp);
        alamat = (EditText)findViewById(R.id.Alamat);
        pria = (RadioButton)findViewById(R.id.rdP);
        wanita = (RadioButton)findViewById(R.id.rdW);
        radioGroup = (RadioGroup)findViewById(R.id.rdG);
        ubah = (Button)findViewById(R.id.btn_ubah);
        simpan = (Button)findViewById(R.id.btn_simpan);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        sharedpreferences = getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        NIM = sharedpreferences.getString(TAG_ID, null);
        NAMA = sharedpreferences.getString(TAG_USERNAME, null);
        HP = sharedpreferences.getString(TAG_HP, null);
        ALAMAT = sharedpreferences.getString(TAG_ALAMAT, null);
        JK = sharedpreferences.getString(TAG_JK, null);

        nim.setText(NIM);
        nama.setText(NAMA);
        hp.setText(HP);
        alamat.setText(ALAMAT);

        switch (JK){
            case "Pria"     : pria.setChecked(true);
                                break;
            case "Wanita"   : wanita.setChecked(true);
                                break;
        }
    }

    public void ubah(View v){
        nim.setEnabled(true);
        nama.setEnabled(true);
        hp.setEnabled(true);
        alamat.setEnabled(true);
        pria.setClickable(true);
        wanita.setClickable(true);
        ubah.setText("Batal");
        simpan.setEnabled(true);
        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revert();
            }
        });

    }
    public void revert(){

        NIM = sharedpreferences.getString(TAG_ID, null);
        NAMA = sharedpreferences.getString(TAG_USERNAME, null);
        HP = sharedpreferences.getString(TAG_HP, null);
        ALAMAT = sharedpreferences.getString(TAG_ALAMAT, null);
        JK = sharedpreferences.getString(TAG_JK, null);

        nim.setText(NIM);
        nama.setText(NAMA);
        hp.setText(HP);
        alamat.setText(ALAMAT);

        switch (JK){
            case "Pria"     : pria.setChecked(true);
                break;
            case "Wanita"   : wanita.setChecked(true);
                break;
        }

        nim.setEnabled(false);
        nama.setEnabled(false);
        hp.setEnabled(false);
        alamat.setEnabled(false);
        pria.setClickable(false);
        wanita.setClickable(false);
        ubah.setText("Ubah");
        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubah(view);
            }
        });
    }

    public String rdPilih(RadioGroup rd){
        String jk = "";
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rdP : jk = "Pria";
                            break;
            case R.id.rdW : jk = "Wanita";
                            break;
        }
        return jk;
    }

    public void simpan(View v) {
        String NIM = nim.getText().toString();
        String NAMA = nama.getText().toString();
        String HP = hp.getText().toString();
        String ALAMAT = alamat.getText().toString();
        String JK = rdPilih(radioGroup);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            konek(this.NIM,NIM,NAMA,HP,ALAMAT,JK);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(TAG_ID, NIM);
            editor.putString(TAG_USERNAME, NAMA);
            editor.putString(TAG_HP, HP);
            editor.putString(TAG_JK, JK);
            editor.putString(TAG_ALAMAT, ALAMAT);
            editor.commit();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void konek (final String nimOld,final String nim,final String nama,final String hp,final String alamat,final String jk) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Sedang menyimpan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Simpan berhasil", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        revert();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {

                    // JSON error
                    Toast.makeText(getApplicationContext(),"JSON ERROR "+e,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Simpan Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage()+"Tidak ada respon", Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nimOld",nimOld);
                params.put("nim", nim);
                params.put("name", nama);
                params.put("noHP", hp);
                params.put("alamat", alamat);
                params.put("jk",jk);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }




}
