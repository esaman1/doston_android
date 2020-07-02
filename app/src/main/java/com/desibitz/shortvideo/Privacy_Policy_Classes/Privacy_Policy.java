package com.desibitz.shortvideo.Privacy_Policy_Classes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.desibitz.shortvideo.R;

public class Privacy_Policy extends AppCompatActivity {
    ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy__policy);
        WebView wv;
        wv = (WebView) findViewById(R.id.webView1);
        back_btn = findViewById(R.id.back_btn);
        wv.loadUrl("file:///android_asset/html/privacypolicy.html");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
