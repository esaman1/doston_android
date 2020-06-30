package com.doston.shortvideo.Notifications_Classes;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.doston.shortvideo.R;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Doston_Notification_Adapter extends RecyclerView.Adapter<Doston_Notification_Adapter.CustomViewHolder > {
    public Context context;

    ArrayList<Doston_Notification_Get_Set> datalist;
    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Doston_Notification_Get_Set item);
    }

    public Doston_Notification_Adapter.OnItemClickListener listener;

    public Doston_Notification_Adapter(Context context, ArrayList<Doston_Notification_Get_Set> arrayList, Doston_Notification_Adapter.OnItemClickListener listener) {
        this.context = context;
        datalist= arrayList;
        this.listener=listener;
    }

    @Override
    public Doston_Notification_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Doston_Notification_Adapter.CustomViewHolder viewHolder = new Doston_Notification_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return datalist.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageButton done;

        public CustomViewHolder(View view) {
            super(view);
          //  image=view.findViewById(R.id.image);
            done=view.findViewById(R.id.done);

        }

        public void bind(final int pos , final Doston_Notification_Get_Set item, final Doston_Notification_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });


        }


    }

    @Override
    public void onBindViewHolder(final Doston_Notification_Adapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        holder.bind(i,datalist.get(i),listener);

}

}