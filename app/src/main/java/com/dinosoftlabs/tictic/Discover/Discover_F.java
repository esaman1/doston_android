package com.dinosoftlabs.tictic.Discover;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dinosoftlabs.tictic.Home.Home_Get_Set;
import com.dinosoftlabs.tictic.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.dinosoftlabs.tictic.R;
import com.dinosoftlabs.tictic.SimpleClasses.ApiRequest;
import com.dinosoftlabs.tictic.SimpleClasses.Callback;
import com.dinosoftlabs.tictic.SimpleClasses.Variables;
import com.dinosoftlabs.tictic.WatchVideos.WatchVideos_F;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Discover_F extends RootFragment {

    View view;
    Context context;

    RecyclerView recyclerView;
    EditText search_edit;


    SwipeRefreshLayout swiperefresh;

    public Discover_F() {
        // Required empty public constructor
    }

    ArrayList<Discover_Get_Set> datalist;
    ArrayList<Discover_user_Model> dataUserList;

    Discover_Adapter adapter;
    DiscoverByUser_Adapter adapter_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_discover, container, false);
        context=getContext();


        datalist=new ArrayList<>();
        dataUserList=new ArrayList<>();


        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter=new Discover_Adapter(context, datalist, new Discover_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<Home_Get_Set> datalist, int postion) {
                OpenWatchVideo(postion,datalist);
            }
        });
        adapter_user = new DiscoverByUser_Adapter(context,getFragmentManager(),dataUserList);





        recyclerView.setAdapter(adapter);


        search_edit=view.findViewById(R.id.search_edit);
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                String query=search_edit.getText().toString();
//                if(adapter!=null) {
//                    adapter.getFilter().filter(query);
//                }
                if(!search_edit.getText().toString().toLowerCase().equals(""))
                {
                    Call_Api_For_SerachUser(search_edit.getText().toString().toLowerCase());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
//                Call_Api_For_SerachUser(search_edit.getText().toString().toLowerCase());
            }
        });
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!search_edit.getText().toString().toLowerCase().equals("")) {
                        Call_Api_For_SerachUser(search_edit.getText().toString().toLowerCase());
                    }
                    return true;
                }
                return false;
            }
        });

        swiperefresh=view.findViewById(R.id.swiperefresh);
        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Call_Api_For_get_Allvideos();
            }
        });


        Call_Api_For_get_Allvideos();

        return view;
    }



    // Bottom two function will get the Discover videos
    // from api and parse the json data which is shown in Discover tab

    private void Call_Api_For_SerachUser(String stringToFind){
//        dataUserList = new ArrayList<>();

        dataUserList.clear();
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", stringToFind);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("resp",parameters.toString());

        ApiRequest.Call_Api(context, Variables.searchByUsername, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Log.d("Response_byUser",""+resp);
                Parse_data_user(resp);
                swiperefresh.setRefreshing(false);
            }
        });


    }

    private void Call_Api_For_get_Allvideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("resp",parameters.toString());

        ApiRequest.Call_Api(context, Variables.discover, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
                swiperefresh.setRefreshing(false);
            }
        });



    }



    public void Parse_data_user(String responce){

//        dataUserList.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            Discover_user_Model discover_user_model = null;
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                for (int d=0;d<msgArray.length();d++) {

                    discover_user_model=new Discover_user_Model();
//                    JSONObject discover_object=msgArray.optJSONObject(d);
                    JSONArray discoverArray=msgArray.getJSONArray(d);
                    for (int j=0;j<discoverArray.length();j++) {
                        JSONObject discover_object = discoverArray.optJSONObject(j);
                        discover_user_model.setFb_id(discover_object.getString("fb_id"));
                        discover_user_model.setUsername(discover_object.getString("username"));
                        discover_user_model.setFirst_name(discover_object.getString("first_name"));
                        discover_user_model.setLast_name(discover_object.getString("last_name"));
                        discover_user_model.setProfile_pic(discover_object.getString("profile_pic"));
                        discover_user_model.setVideoCount(discover_object.getString("videoCount"));
                        discover_user_model.setFollowersCount(discover_object.getString("followersCount"));
                        dataUserList.add(discover_user_model);
                    }

                    }

//                    discover_get_set.arrayList=video_list;


                    recyclerView.setAdapter(adapter_user);
                adapter_user.notifyDataSetChanged();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void Parse_data(String responce){

        datalist.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                for (int d=0;d<msgArray.length();d++) {

                    Discover_Get_Set discover_get_set=new Discover_Get_Set();
                    JSONObject discover_object=msgArray.optJSONObject(d);
                    discover_get_set.title=discover_object.optString("section_name");

                    JSONArray video_array=discover_object.optJSONArray("sections_videos");

                    ArrayList<Home_Get_Set> video_list = new ArrayList<>();
                    for (int i = 0; i < video_array.length(); i++) {
                        JSONObject itemdata = video_array.optJSONObject(i);
                        Home_Get_Set item = new Home_Get_Set();


                        JSONObject user_info = itemdata.optJSONObject("user_info");
                        item.fb_id = user_info.optString("fb_id");
                        item.first_name = user_info.optString("first_name");
                        item.last_name = user_info.optString("last_name");
                        item.profile_pic = user_info.optString("profile_pic");
                        //test
                        JSONObject count = itemdata.optJSONObject("count");
                        item.like_count = count.optString("like_count");
                        item.video_comment_count = count.optString("video_comment_count");

                        JSONObject sound_data=itemdata.optJSONObject("sound");
                        item.sound_id=sound_data.optString("id");
                        item.sound_name=sound_data.optString("sound_name");
                        item.sound_pic=sound_data.optString("thum");


                        item.video_id = itemdata.optString("id");
                        item.liked = itemdata.optString("liked");
                        item.video_url = Variables.base_url + itemdata.optString("video");
                        item.thum = Variables.base_url + itemdata.optString("thum");
                        item.gif =Variables.base_url+itemdata.optString("gif");
                        item.created_date = itemdata.optString("created");
                        item.video_description=itemdata.optString("description");

                        video_list.add(item);
                    }

                    discover_get_set.arrayList=video_list;

                    datalist.add(discover_get_set);

                }

                adapter.notifyDataSetChanged();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    // When you click on any Video a new activity is open which will play the Clicked video
    private void OpenWatchVideo(int postion,ArrayList<Home_Get_Set> data_list) {

        Intent intent=new Intent(getActivity(),WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position",postion);
        startActivity(intent);

    }




}
