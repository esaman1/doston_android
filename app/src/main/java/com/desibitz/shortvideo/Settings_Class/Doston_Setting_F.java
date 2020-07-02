package com.desibitz.shortvideo.Settings_Class;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desibitz.shortvideo.Main_Menu_Classes.Doston_MainMenuActivity;
import com.desibitz.shortvideo.Main_Menu_Classes.RelateToFragment_OnBack.Doston_RootFragment;
import com.desibitz.shortvideo.R;
import com.desibitz.shortvideo.SimpleClasses.Variables;

/**
 * A simple {@link Fragment} subclass.
 */
public class Doston_Setting_F extends Doston_RootFragment implements View.OnClickListener {

    View view;
    Context context;

    public Doston_Setting_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_setting, container, false);


        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.logout_txt).setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.logout_txt:
                new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                        .setTitle("Alert")
                        .setMessage("Are you Sure want to logout?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                Logout();

                            }
                        }).show();

                break;
        }
    }



    // this will erase all the user info store in locally and logout the user
    public void Logout(){
        SharedPreferences.Editor editor= Variables.sharedPreferences.edit();
        editor.putString(Variables.u_id,"").clear();
        editor.putString(Variables.u_name,"").clear();
        editor.putString(Variables.u_pic,"").clear();
        editor.putBoolean(Variables.islogin,false).clear();
        editor.commit();
        getActivity().finish();
        startActivity(new Intent(getActivity(), Doston_MainMenuActivity.class));
    }


}
