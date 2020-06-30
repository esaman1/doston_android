package com.doston.shortvideo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.doston.shortvideo.Main_Menu_Classes.Doston_MainMenuActivity;
import com.doston.shortvideo.SimpleClasses.Variables;

public class Doston_Splash_A extends AppCompatActivity {


    CountDownTimer countDownTimer;
    TextView title,title_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Change font style of app title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        title = findViewById(R.id.title);
        title_next = findViewById(R.id.title_next);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/bahamans_bold.ttf");
        Typeface custom_next_font = Typeface.createFromAsset(getAssets(), "fonts/baha.ttf");
        title.setTypeface(custom_font);
        title_next.setTypeface(custom_next_font);


        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);

        countDownTimer = new CountDownTimer(2500, 500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                Intent intent=new Intent(Doston_Splash_A.this, Doston_MainMenuActivity.class);

                if(getIntent().getExtras()!=null) {
                    intent.putExtras(getIntent().getExtras());
                    setIntent(null);
                }

                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();

            }
        }.start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

}
