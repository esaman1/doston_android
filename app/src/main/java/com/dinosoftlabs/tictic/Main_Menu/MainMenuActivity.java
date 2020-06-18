package com.dinosoftlabs.tictic.Main_Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dinosoftlabs.tictic.Chat.Chat_Activity;
import com.dinosoftlabs.tictic.Main_Menu.RelateToFragment_OnBack.BackPressImplimentation;
import com.dinosoftlabs.tictic.R;
import com.dinosoftlabs.tictic.See_Full_Image_F;
import com.dinosoftlabs.tictic.SimpleClasses.Variables;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;


public class MainMenuActivity extends AppCompatActivity {
    public static MainMenuActivity mainMenuActivity;
    private MainMenuFragment mainMenuFragment;
    long mBackPressed;


    public static String token;

    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mainMenuActivity=this;

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
            mainMenuFragment = (MainMenuFragment) getSupportFragmentManager().getFragments().get(0);
        }




    }


    private void initScreen() {
        mainMenuFragment = new MainMenuFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mainMenuFragment)
                .commit();

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }



    @Override
    public void onBackPressed() {


        Toast.makeText(getBaseContext(), "tapped", Toast.LENGTH_SHORT).show();
//       new BackPressImplimentation(mainMenuFragment).onBackPressed();
//        if (!new BackPressImplimentation(mainMenuFragment).onBackPressed()) {
//            int count = this.getSupportFragmentManager().getBackStackEntryCount();
//            if (count == 0) {
//                if (mBackPressed + 2000 > System.currentTimeMillis()) {
//                    super.onBackPressed();
//                    return;
//                } else {
//                    Toast.makeText(getBaseContext(), "Tap Again To Exit12321321", Toast.LENGTH_SHORT).show();
//                    mBackPressed = System.currentTimeMillis();
//
//                }
//            } else {
//                super.onBackPressed();
//            }
//        }




        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        if(see_image_f.getUserVisibleHint())
        {

           getSupportFragmentManager().popBackStack();
        }
    }








}
