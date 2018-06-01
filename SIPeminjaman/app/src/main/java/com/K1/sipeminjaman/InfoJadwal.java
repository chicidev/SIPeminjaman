package com.K1.sipeminjaman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import static com.K1.sipeminjaman.Res.*;

public class InfoJadwal extends AppCompatActivity {
    WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_jadwal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(new MyBrowser());
        view.loadUrl("http://youtube.com");
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
