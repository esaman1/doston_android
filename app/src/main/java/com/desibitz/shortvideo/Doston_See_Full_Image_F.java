package com.desibitz.shortvideo;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gmail.samehadar.iosdialog.CamomileSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class Doston_See_Full_Image_F extends Fragment {


    View view;
    Context context;
    ImageButton close_gallery;


    ImageView single_image;

    String image_url;

//    ProgressBar p_bar;

    CamomileSpinner loader;
    int width,height;

    public Doston_See_Full_Image_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_see_full_image, container, false);
        context=getContext();

         DisplayMetrics displayMetrics = new DisplayMetrics();
         getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels;
         width = displayMetrics.widthPixels;

        image_url=getArguments().getString("image_url");

        close_gallery=view.findViewById(R.id.close_gallery);
        close_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



//        p_bar=view.findViewById(R.id.p_bar);
        loader=view.findViewById(R.id.loader);
        loader.start();

        single_image=view.findViewById(R.id.single_image);


        loader.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(image_url)
                .placeholder(R.drawable.image_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loader.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loader.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(single_image);
//        Glide.with(context).load(image_url).placeholder(R.drawable.image_placeholder).into(single_image);
//            Picasso.with(context).load(image_url).placeholder(R.drawable.image_placeholder)
//                    .into(single_image, new Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                            p_bar.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError() {
//                            // TODO Auto-generated method stub
//                            p_bar.setVisibility(View.GONE);
//                        }
//                    });



        return view;
    }







}


