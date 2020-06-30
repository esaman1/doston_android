package com.doston.shortvideo.Notification_Receive_Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;

import com.doston.shortvideo.MessageChat_Classes.Doston_Chat_Activity;
import com.doston.shortvideo.Main_Menu_Classes.Doston_MainMenuActivity;
import com.doston.shortvideo.Main_Menu_Classes.Doston_MainMenuFragmentDoston;
import com.doston.shortvideo.R;
import com.doston.shortvideo.SimpleClasses.Variables;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by AQEEL on 5/22/2018.
 */

public class Doston_Notification_Receive extends FirebaseMessagingService {


    SharedPreferences sharedPreferences;
    String  pic;
    String  title;
    String  message;
    String senderid;
    String receiverid;
    String action_type;



    Handler handler=new Handler();
    Runnable runnable;

    Snackbar snackbar;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getData().size() > 0) {
            sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("body");
            pic=remoteMessage.getData().get("icon");
            senderid=remoteMessage.getData().get("senderid");
            receiverid=remoteMessage.getData().get("receiverid");
            action_type=remoteMessage.getData().get("action_type");
            if(!Doston_Chat_Activity.senderid_for_check_notification.equals(senderid)){
                sendNotification sendNotification=new sendNotification(this);
                sendNotification.execute(pic);

            }

        }


    }


        // this will store the user firebase token in local storage
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

        if(s==null){

        }else if(s.equals("null")){

        }
        else if(s.equals("")){

        }
        else if(s.length()<6){

        }
        else {
            sharedPreferences.edit().putString(Variables.device_token, s).commit();
        }

    }




    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;


        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            // in notification first we will get the image of the user and then we will show the notification to user
            // in onPostExecute
            InputStream in;
            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);


            if(Doston_MainMenuActivity.dostonMainMenuActivity !=null){


                if(snackbar !=null){
                    snackbar.getView().setVisibility(View.INVISIBLE);
                    snackbar.dismiss();
                }

                if(handler!=null && runnable!=null) {
                    handler.removeCallbacks(runnable);
                }


                View layout = Doston_MainMenuActivity.dostonMainMenuActivity.getLayoutInflater().inflate(R.layout.item_layout_custom_notification,null);
                TextView titletxt= layout.findViewById(R.id.username);
                TextView messagetxt=layout.findViewById(R.id.message);
                ImageView imageView=layout.findViewById(R.id.user_image);
                titletxt.setText(title);
                messagetxt.setText(message);
//                layout.setBackgroundColor(Color.parseColor("#00000000"));

                if(result!=null) {
                    imageView.setImageBitmap(result);
                }


                snackbar = Snackbar.make(Doston_MainMenuActivity.dostonMainMenuActivity.findViewById(R.id.container), "", Snackbar.LENGTH_LONG);

                Snackbar.SnackbarLayout snackbarLayout= (Snackbar.SnackbarLayout) snackbar.getView();
                TextView textView = (TextView) snackbarLayout.findViewById(R.id.snackbar_text);
                textView.setVisibility(View.INVISIBLE);

                final ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
                if (params instanceof CoordinatorLayout.LayoutParams) {
                    ((CoordinatorLayout.LayoutParams) params).gravity = Gravity.TOP;
                } else {
                    ((FrameLayout.LayoutParams) params).gravity = Gravity.TOP;
                }

                snackbarLayout.setPadding(0,0,0,0);
                snackbarLayout.addView(layout, 0);

                snackbarLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
                snackbar.getView().setVisibility(View.INVISIBLE);

                snackbar.setCallback(new Snackbar.Callback(){
                    @Override
                    public void onShown(Snackbar sb) {
                        super.onShown(sb);
                        snackbar.getView().setVisibility(View.VISIBLE);
                    }

                });


                runnable=new Runnable() {
                    @Override
                    public void run() {
                        snackbar.getView().setVisibility(View.INVISIBLE);

                    }
                };

                handler.postDelayed(runnable, 2750);


                snackbar.setDuration(Snackbar.LENGTH_LONG);
                snackbar.show();



                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        snackbar.dismiss();
                        snackbar.getView().setVisibility(View.INVISIBLE);

                        if(action_type.equals("message"))
                        chatFragment(senderid,title,pic);

                    }
                });


            }


        }

    }



    public void chatFragment(String receiverid, String name, String picture){

        if(sharedPreferences.getBoolean(Variables.islogin,false)) {

            if (Doston_MainMenuFragmentDoston.tabLayout != null) {
                TabLayout.Tab tab3 = Doston_MainMenuFragmentDoston.tabLayout.getTabAt(3);
                tab3.select();
            }

            Doston_Chat_Activity dostonChat_activity = new Doston_Chat_Activity();
            FragmentTransaction transaction = Doston_MainMenuActivity.dostonMainMenuActivity.getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

            Bundle args = new Bundle();
            args.putString("user_id", receiverid);
            args.putString("user_name", name);
            args.putString("user_pic", picture);

            dostonChat_activity.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, dostonChat_activity).commit();

        }


    }




}
