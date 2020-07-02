package com.desibitz.shortvideo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.desibitz.shortvideo.Main_Menu_Classes.Doston_MainMenuActivity;
import com.desibitz.shortvideo.SimpleClasses.Variables;

public class Doston_Splash_A extends AppCompatActivity {


//    CountDownTimer countDownTimer;
    TextView title,title_next;
    Handler handler;
    Runnable runnable;
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


        handler  = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(Variables.sharedPreferences.getBoolean("agreed",false))
                {
                    new LoadMain().execute();
                }
                else
                {


                final Dialog dialog = new Dialog(Doston_Splash_A.this);
                dialog.setContentView(R.layout.tou_privacy);
                TextView agree =  dialog.findViewById(R.id.agree);
                TextView disagree =  dialog.findViewById(R.id.disagree);
                agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
                        editor.putBoolean("agreed",true);
                        editor.commit();
                        dialog.dismiss();
                        new LoadMain().execute();

                    }
                });
                disagree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                dialog.show();
                }

            }
        };
        handler.postDelayed(runnable,1500);

//        countDownTimer = new CountDownTimer(2500, 500) {
//
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            public void onFinish() {
//
//
//
//            }
//        }.start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null)
        {
            if(runnable !=null)
            {
                handler.removeCallbacks(runnable);
            }
        }
//        countDownTimer.cancel();
    }

    public class LoadMain extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            Intent intent=new Intent(Doston_Splash_A.this, Doston_MainMenuActivity.class);
            if(getIntent().getExtras()!=null) {
                intent.putExtras(getIntent().getExtras());
                setIntent(null);
            }

            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }

}
