package com.doston.shortvideo.Notifications_Classes;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doston.shortvideo.Inbox_Message_Classes.Doston_Inbox_F;
import com.doston.shortvideo.Main_Menu_Classes.RelateToFragment_OnBack.Doston_RootFragment;
import com.doston.shortvideo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Doston_Notification_F extends Doston_RootFragment implements View.OnClickListener {

    View view;
    Context context;

    Doston_Notification_Adapter adapter;
    RecyclerView recyclerView;

    ArrayList<Doston_Notification_Get_Set> datalist;


    public Doston_Notification_F() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notification, container, false);
        context=getContext();


        datalist=new ArrayList<>();


        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        adapter=new Doston_Notification_Adapter(context, datalist, new Doston_Notification_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Doston_Notification_Get_Set item) {

            }
        }
    );

        recyclerView.setAdapter(adapter);



        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);

        view.findViewById(R.id.inbox_btn).setOnClickListener(this);

        return view;
    }


    AdView adView;
    @Override
    public void onStart() {
        super.onStart();
        adView = view.findViewById(R.id.bannerad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inbox_btn:
                Open_inbox_F();
                break;
        }
    }

    private void Open_inbox_F() {

        Doston_Inbox_F dostonInbox_f = new Doston_Inbox_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, dostonInbox_f).commit();

    }


}
