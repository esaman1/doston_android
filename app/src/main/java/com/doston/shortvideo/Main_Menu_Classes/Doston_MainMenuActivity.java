package com.doston.shortvideo.Main_Menu_Classes;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.doston.shortvideo.Main_Menu_Classes.RelateToFragment_OnBack.Doston_BackPressImplimentationDoston;
import com.doston.shortvideo.R;
import com.doston.shortvideo.Doston_See_Full_Image_F;
import com.doston.shortvideo.SimpleClasses.Variables;
import com.google.firebase.iid.FirebaseInstanceId;


public class Doston_MainMenuActivity extends AppCompatActivity {
    public static Doston_MainMenuActivity dostonMainMenuActivity;
    private Doston_MainMenuFragmentDoston dostonMainMenuFragment;
    long mBackPressed;


    public static String token;

    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dostonMainMenuActivity =this;

        intent=getIntent();

        setIntent(null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Variables.screen_height= displayMetrics.heightPixels;
        Variables.screen_width= displayMetrics.widthPixels;

        Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

        Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");
        Variables.user_name=Variables.sharedPreferences.getString(Variables.u_name,"");
        Variables.user_pic=Variables.sharedPreferences.getString(Variables.u_pic,"");


        token= FirebaseInstanceId.getInstance().getToken();
        if(token==null || (token.equals("")||token.equals("null"))) {
            token = Variables.sharedPreferences.getString(Variables.device_token, "null");
        }

        if (savedInstanceState == null) {

            initScreen();

        } else {
            dostonMainMenuFragment = (Doston_MainMenuFragmentDoston) getSupportFragmentManager().getFragments().get(0);
        }




    }


    private void initScreen() {
        dostonMainMenuFragment = new Doston_MainMenuFragmentDoston();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, dostonMainMenuFragment)
                .commit();

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }



    @Override
    public void onBackPressed() {


//        Toast.makeText(getBaseContext(), "tapped", Toast.LENGTH_SHORT).show();
       new Doston_BackPressImplimentationDoston(dostonMainMenuFragment).onBackPressed();
        if (!new Doston_BackPressImplimentationDoston(dostonMainMenuFragment).onBackPressed()) {
            int count = this.getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                if (mBackPressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return;
                } else {
                    Toast.makeText(getBaseContext(), "Tap Again To Exit", Toast.LENGTH_SHORT).show();
                    mBackPressed = System.currentTimeMillis();

                }
            } else {
                super.onBackPressed();
            }
        }




        Doston_See_Full_Image_F see_image_f = new Doston_See_Full_Image_F();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        if(see_image_f.getUserVisibleHint())
        {

           getSupportFragmentManager().popBackStack();
        }
    }








}
