package com.desibitz.shortvideo.Privacy_Policy_Classes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.desibitz.shortvideo.R;

public class TermsOfUse extends AppCompatActivity {
    ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        WebView wv;
        wv = (WebView) findViewById(R.id.webView1);
        back_btn = findViewById(R.id.back_btn);
        wv.loadUrl("file:///android_asset/html/termsofuse.html");
        wv.getSettings().setJavaScriptEnabled(true);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
