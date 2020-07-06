package com.desibitz.shortvideo.Profile_Classes;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.desibitz.shortvideo.BuildConfig;
import com.desibitz.shortvideo.Doston_See_Full_Image_F;
import com.desibitz.shortvideo.Following_Classes.Doston_Following_F;
import com.desibitz.shortvideo.Main_Menu_Classes.Doston_MainMenuActivity;
import com.desibitz.shortvideo.Main_Menu_Classes.RelateToFragment_OnBack.Doston_RootFragment;
import com.desibitz.shortvideo.Privacy_Policy_Classes.Privacy_Policy;
import com.desibitz.shortvideo.Privacy_Policy_Classes.TermsOfUse;
import com.desibitz.shortvideo.Profile_Classes.Liked_Videos.Doston_Liked_Video_F;
import com.desibitz.shortvideo.Profile_Classes.UserVideos.Doston_UserVideo_F;
import com.desibitz.shortvideo.R;
import com.desibitz.shortvideo.SimpleClasses.Callback;
import com.desibitz.shortvideo.SimpleClasses.Doston_ApiRequest;
import com.desibitz.shortvideo.SimpleClasses.Doston_Functions;
import com.desibitz.shortvideo.SimpleClasses.Fragment_Callback;
import com.desibitz.shortvideo.SimpleClasses.Variables;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Doston_Profile_Tab_F extends Doston_RootFragment implements View.OnClickListener {
    public static String pic_url;
    public TextView username, fullname, video_count_txt;
    public ImageView imageView;
    public TextView follow_count_txt, fans_count_txt, heart_count_txt, user_bios;
    public boolean isdataload = false;
    public LinearLayout create_popup_layout;
    protected TabLayout tabLayout;
    protected ViewPager pager;
    View view;
    Context context;
    ImageView setting_btn;
    //    LinearLayout follow_unfollow_ll;
//    ImageView follow_unfollow_img;
    TextView follow_unfollow_txt;
    Bundle bundle;
    RelativeLayout tabs_main_layout;
    LinearLayout top_layout;
    private ViewPagerAdapter adapter;

    public Doston_Profile_Tab_F() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        context = getContext();


        return init();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:
                OpenfullsizeImage(pic_url);
                break;

            case R.id.setting_btn:
                Open_Setting();
                break;

            case R.id.following_layout:
                Open_Following();
                break;

            case R.id.fans_layout:
                Open_Followers();
                break;

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if ((view != null && isVisibleToUser) && !isdataload) {
            if (Variables.sharedPreferences.getBoolean(Variables.islogin, false))
                init();
        }
        if ((view != null && isVisibleToUser) && isdataload) {

            Call_Api_For_get_Allvideos();

        }


    }


    public View init() {

        username = view.findViewById(R.id.username);
        fullname = view.findViewById(R.id.fullname);
        imageView = view.findViewById(R.id.user_image);
        imageView.setOnClickListener(this);

        video_count_txt = view.findViewById(R.id.video_count_txt);

        follow_count_txt = view.findViewById(R.id.follow_count_txt);
        fans_count_txt = view.findViewById(R.id.fan_count_txt);
        heart_count_txt = view.findViewById(R.id.heart_count_txt);
        user_bios = view.findViewById(R.id.user_bios);

//        follow_unfollow_ll = view.findViewById(R.id.follow_unfollow_ll);
//        follow_unfollow_img = view.findViewById(R.id.follow_unfollow_img);
        follow_unfollow_txt = view.findViewById(R.id.follow_unfollow_txt);


        setting_btn = view.findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        setupTabIcons();


        tabs_main_layout = view.findViewById(R.id.tabs_main_layout);
        top_layout = view.findViewById(R.id.top_layout);


        ViewTreeObserver observer = top_layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int height = top_layout.getMeasuredHeight();

                top_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                ViewTreeObserver observer = tabs_main_layout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabs_main_layout.getLayoutParams();
                        params.height = (int) (tabs_main_layout.getMeasuredHeight() + height);
                        tabs_main_layout.setLayoutParams(params);
                        tabs_main_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);

                    }
                });

            }
        });


        create_popup_layout = view.findViewById(R.id.create_popup_layout);


        view.findViewById(R.id.following_layout).setOnClickListener(this);
        view.findViewById(R.id.fans_layout).setOnClickListener(this);

        isdataload = true;


        update_profile();

        Call_Api_For_get_Allvideos();

        return view;
    }


    public void update_profile() {
        username.setText(Variables.sharedPreferences.getString(Variables.f_name, "") + " " + Variables.sharedPreferences.getString(Variables.l_name, ""));
        pic_url = Variables.sharedPreferences.getString(Variables.u_pic, "null");

        try {
            Picasso.with(context).load(pic_url)
                    .resize(200, 200)
                    .placeholder(R.drawable.profile_image_placeholder)
                    .centerCrop()
                    .into(imageView);

        } catch (Exception e) {

        }
    }


    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        TextView imageView1 = view1.findViewById(R.id.image);
        imageView1.setText("Videos");
        imageView1.setTextColor(Color.parseColor("#FFFFFF"));
//        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_color));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        TextView imageView2 = view2.findViewById(R.id.image);
        imageView2.setText("Liked Videos");
        imageView2.setTextColor(Color.parseColor("#848484"));
        tabLayout.getTabAt(1).setCustomView(view2);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                TextView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:

                        if (Doston_UserVideo_F.myvideo_count > 0) {
                            create_popup_layout.setVisibility(View.GONE);
                        } else {
                            create_popup_layout.setVisibility(View.VISIBLE);
                            Animation aniRotate = AnimationUtils.loadAnimation(context, R.anim.up_and_down_animation);
                            create_popup_layout.startAnimation(aniRotate);
                        }

                        image.setTextColor(Color.parseColor("#FFFFFF"));
                        break;

                    case 1:
                        create_popup_layout.clearAnimation();
                        create_popup_layout.setVisibility(View.GONE);
                        image.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                TextView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:
                        image.setTextColor(Color.parseColor("#848484"));
                        break;
                    case 1:
                        image.setTextColor(Color.parseColor("#848484"));
                        break;
                }

                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }

    //this will get the all videos data of user and then parse the data
    private void Call_Api_For_get_Allvideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("my_fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));
//            Log.d("LOG_my_fb_id",""+parameters);
//            Log.d("LOG_fb_id",""+Variables.sharedPreferences.getString(Variables.u_id,""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Doston_ApiRequest.Call_Api(context, Variables.showMyAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
            }
        });


    }

    public void Parse_data(String responce) {

        Log.d("ProfileEdit", "" + responce);
        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");

                JSONObject data = msgArray.getJSONObject(0);
                JSONObject user_info = data.optJSONObject("user_info");
                fullname.setText(user_info.optString("first_name") + " " + user_info.optString("last_name"));
                username.setText(user_info.optString("username"));
                user_bios.setText(user_info.optString("bio"));
                Doston_Profile_F.pic_url = user_info.optString("profile_pic");
                Picasso.with(context)
                        .load(Doston_Profile_F.pic_url)
                        .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                        .resize(200, 200).centerCrop().into(imageView);

                follow_count_txt.setText(data.optString("total_following"));
                fans_count_txt.setText(data.optString("total_fans"));
                heart_count_txt.setText(data.optString("total_heart"));


                JSONArray user_videos = data.getJSONArray("user_videos");
                if (!user_videos.toString().equals("[" + "0" + "]")) {
                    video_count_txt.setText(Doston_Functions.prettyCount(user_videos.length() + ""));
                    create_popup_layout.setVisibility(View.GONE);

                } else {

                    create_popup_layout.setVisibility(View.VISIBLE);
                    Animation aniRotate = AnimationUtils.loadAnimation(context, R.anim.up_and_down_animation);
                    create_popup_layout.startAnimation(aniRotate);

                }


            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void Open_Setting() {

        Open_menu_tab(setting_btn);


    }

    public void Open_Edit_profile() {
        Doston_Edit_Profile_F dostonEdit_profile_f = new Doston_Edit_Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                update_profile();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, dostonEdit_profile_f).commit();
    }

    //this method will get the big size of profile image.
    public void OpenfullsizeImage(String url) {
        Doston_See_Full_Image_F see_image_f = new Doston_See_Full_Image_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        Bundle args = new Bundle();
        args.putSerializable("image_url", url);
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
    }

    public void Open_menu_tab(View anchor_view) {
        Context wrapper = new ContextThemeWrapper(context, R.style.AlertDialogCustom);
        PopupMenu popup = new PopupMenu(wrapper, anchor_view);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popup.setGravity(Gravity.TOP | Gravity.RIGHT);
        }
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.edit_Profile_id:
                        Open_Edit_profile();
                        break;

                    case R.id.shar_app:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                getString(R.string.app_name) +"\n https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;

                    case R.id.terms_of_use_id:
                        Intent termsofuse = new Intent(context, TermsOfUse.class);
                        startActivity(termsofuse);
                        break;

                    case R.id.privacy_id:
                        Intent privacyPolicy = new Intent(context, Privacy_Policy.class);
                        startActivity(privacyPolicy);
                        break;

                    case R.id.logout_id:
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
                return true;
            }
        });

    }

    public void Open_Following() {

        Doston_Following_F dostonFollowing_f = new Doston_Following_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                Call_Api_For_get_Allvideos();

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "following");
        dostonFollowing_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, dostonFollowing_f).commit();

    }

    public void Open_Followers() {
        Doston_Following_F dostonFollowing_f = new Doston_Following_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {
                Call_Api_For_get_Allvideos();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "fan");
        dostonFollowing_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, dostonFollowing_f).commit();

    }

    // this will erase all the user info store in locally and logout the user
    public void Logout() {
        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
        editor.putString(Variables.u_id, "");
        editor.putString(Variables.u_name, "");
        editor.putString(Variables.u_pic, "");
        editor.putBoolean(Variables.islogin, false);
        editor.commit();

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().signOut(); // very important if you are using firebase.
                    Intent login_intent = new Intent(getActivity(), Doston_MainMenuActivity.class);
                    login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // clear previous task (optional)
                    startActivity(login_intent);
                    getActivity().finish();
                }
            }
        });
//        startActivity(new Intent(getActivity(), Doston_MainMenuActivity.class));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Doston_Functions.deleteCache(context);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final Resources resources;

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
            this.resources = resources;
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new Doston_UserVideo_F(Variables.sharedPreferences.getString(Variables.u_id, ""));
                    break;
                case 1:
                    result = new Doston_Liked_Video_F(Variables.sharedPreferences.getString(Variables.u_id, ""));
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(final int position) {
            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }


        /**
         * Get the Fragment by position
         *
         * @param position tab position of the fragment
         * @return
         */
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }


    }


}
