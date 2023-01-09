package com.nttd.picmarker;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        webView=findViewById(R.id.webview1);
        webView.loadUrl("https://vi.m.wikipedia.org/wiki/K%E1%BB%B9_thu%E1%BA%ADt_gi%E1%BA%A5u_tin");

    }
}
