package com.K1.sipeminjaman;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.K1.sipeminjaman.ListItem;
import com.K1.sipeminjaman.R;
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

/**
 * Created by PungkiDwiGiantoro on 07/06/2018.
 */

public class RiwayatAdapter extends ArrayAdapter<ListItem> implements View.OnClickListener{
    private ArrayList<ListItem> data;
    SharedPreferences dataPreferences;
    private Context mContext;
    private int lastPosition = -1;
    private ProgressDialog pDialog;
    private String TAG;
    private Gson gson = new Gson();

    @Override
    public void onClick(View view) {
        final View v = view;
        int position =(Integer) view.getTag();
        Object object = getItem(position);
         final ListItem item = (ListItem)object;

        switch (view.getId())
        {

            case R.id.rw_delete:
                new AlertDialog.Builder(mContext)
                        .setTitle("Hapus Riwayat")
                        .setMessage("Anda Yakin ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(item.getTanggal(),item.getGedung(),v);
                            }
                        })
                        .setNegativeButton("Tidak",null)
                        .show();



                break;


        }

    }

    private static class ViewHolder {
        TextView txtNamaGedung;
        TextView txtKeperluan;
        TextView txtTanggal;
        TextView txtStatus;
        ImageButton btnDelete;
    }

    public RiwayatAdapter(ArrayList<ListItem> array,Context context){
        super(context, R.layout.listview,array);
        this.data = array;
        this.mContext = context;
        dataPreferences = mContext.getSharedPreferences(data_preferences,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListItem listItem = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview,parent,false);
            viewHolder.txtNamaGedung = (TextView) convertView.findViewById(R.id.rw_gedung);
            viewHolder.txtKeperluan = (TextView) convertView.findViewById(R.id.rw_keperluan);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.rw_status);
            viewHolder.txtTanggal = (TextView) convertView.findViewById(R.id.rw_tanggal);
            viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.rw_delete);

            result = convertView;
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,(position > lastPosition)? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtNamaGedung.setText(listItem.getGedung());
        viewHolder.txtKeperluan.setText(listItem.getKeperluan());
        viewHolder.txtStatus.setText(listItem.getStatus());
        viewHolder.txtTanggal.setText(listItem.getTanggal());
        viewHolder.btnDelete.setOnClickListener(this);
        viewHolder.btnDelete.setTag(position);

        return convertView;
    }

    private void delete(final String tanggal, final String gedung,final View v) {
        TAG = mContext.getClass().getSimpleName();
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memproses . . .");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_delRiwayat, new Response.Listener<String>() {

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

                        Toast.makeText(mContext, "Riwayat berhasil dihapus", Toast.LENGTH_LONG).show();



                        ((Riwayat)mContext).refreshListView();

                        // menyimpan login ke session

                    } else {
                        Toast.makeText(mContext,
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
                Toast.makeText(mContext,"Response Error"+
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(RIWAYAT_TANGGAL, tanggal);
                params.put(RIWAYAT_GEDUNG, gedung);
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


}
