package com.desibitz.shortvideo.Profile_Classes;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.desibitz.shortvideo.Home_Fragement_Classes.Doston_Home_Get_Set;
import com.desibitz.shortvideo.R;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Doston_MyVideos_Adapter extends RecyclerView.Adapter<Doston_MyVideos_Adapter.CustomViewHolder > {

    public Context context;
    private Doston_MyVideos_Adapter.OnItemClickListener listener;
    private ArrayList<Doston_Home_Get_Set> dataList;


      public interface OnItemClickListener {
        void onItemClick(int postion, Doston_Home_Get_Set item, View view);
    }

    public Doston_MyVideos_Adapter(Context context, ArrayList<Doston_Home_Get_Set> dataList, Doston_MyVideos_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Doston_MyVideos_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_myvideo_layout,null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
//        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        view.setLayoutParams(new RecyclerView.LayoutParams(dpToPx(120), dpToPx(160)));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((width/3)-dpToPx(10), ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
        view.setLayoutParams(layoutParams);
        Doston_MyVideos_Adapter.CustomViewHolder viewHolder = new Doston_MyVideos_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb_image;

        TextView view_txt;

        public CustomViewHolder(View view) {
            super(view);

            thumb_image=view.findViewById(R.id.thumb_image);
            view_txt=view.findViewById(R.id.view_txt);

        }

        public void bind(final int position, final Doston_Home_Get_Set item, final Doston_MyVideos_Adapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,item,v);
                }
            });

        }

    }




    @Override
    public void onBindViewHolder(final Doston_MyVideos_Adapter.CustomViewHolder holder, final int i) {
        final Doston_Home_Get_Set item= dataList.get(i);
        holder.setIsRecyclable(false);



        try {
            Glide.with(context)
                    .asGif()
                    .load(item.gif)
                    .skipMemoryCache(true)
                     .thumbnail(new RequestBuilder[]{Glide
                             .with(context)
                             .load(item.thum)})
                    .apply(RequestOptions.diskCacheStrategyOf( DiskCacheStrategy.RESOURCE)
                            .placeholder(context.getResources().getDrawable(R.drawable.image_placeholder)).centerCrop())

                    .into(holder.thumb_image);

        }catch (Exception e){

        }



        holder.view_txt.setText(item.views);

        holder.bind(i,item,listener);

   }

}