package com.doston.shortvideo.VideoAction;

import android.content.Context;
import android.content.pm.ResolveInfo;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doston.shortvideo.R;

import java.util.List;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Doston_VideoSharingApps_Adapter extends RecyclerView.Adapter<Doston_VideoSharingApps_Adapter.CustomViewHolder > {

    public Context context;
    private Doston_VideoSharingApps_Adapter.OnItemClickListener listener;
    private List<ResolveInfo> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, ResolveInfo item, View view);
    }



    public Doston_VideoSharingApps_Adapter(Context context, List<ResolveInfo> dataList, Doston_VideoSharingApps_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Doston_VideoSharingApps_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_videosharingapps_layout,null);
        Doston_VideoSharingApps_Adapter.CustomViewHolder viewHolder = new Doston_VideoSharingApps_Adapter.CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
       return dataList.size();
    }



    @Override
    public void onBindViewHolder(final Doston_VideoSharingApps_Adapter.CustomViewHolder holder, final int i) {
        final ResolveInfo item= dataList.get(i);
        holder.setIsRecyclable(false);

        try {

        holder.bind(i,item,listener);

        holder.image.setImageDrawable(item.loadIcon(context.getPackageManager()));


        }catch (Exception e){

        }
   }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView image;


        public CustomViewHolder(View view) {
            super(view);


            image=view.findViewById(R.id.image);
        }

        public void bind(final int postion,final ResolveInfo item, final Doston_VideoSharingApps_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });




        }


    }


}