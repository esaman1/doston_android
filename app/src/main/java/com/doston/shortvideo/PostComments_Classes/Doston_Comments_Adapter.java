package com.doston.shortvideo.PostComments_Classes;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doston.shortvideo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Doston_Comments_Adapter extends RecyclerView.Adapter<Doston_Comments_Adapter.CustomViewHolder > {

    public Context context;
    private Doston_Comments_Adapter.OnItemClickListener listener;
    private ArrayList<Doston_Comment_Get_Set> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Doston_Comment_Get_Set item, View view);
    }

    public Doston_Comments_Adapter(Context context, ArrayList<Doston_Comment_Get_Set> dataList, Doston_Comments_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Doston_Comments_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Doston_Comments_Adapter.CustomViewHolder viewHolder = new Doston_Comments_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }


    @Override
    public void onBindViewHolder(final Doston_Comments_Adapter.CustomViewHolder holder, final int i) {
        final Doston_Comment_Get_Set item= dataList.get(i);


        holder.username.setText(item.first_name+" "+item.last_name);

        try{
        Picasso.with(context).
                load(item.profile_pic)
                .resize(50,50)
                .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                .into(holder.user_pic);

       }catch (Exception e){

       }

        holder.message.setText(item.comments);


        holder.bind(i,item,listener);

   }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username,message;
        ImageView user_pic;


        public CustomViewHolder(View view) {
            super(view);

            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            message=view.findViewById(R.id.message);

        }

        public void bind(final int postion, final Doston_Comment_Get_Set item, final Doston_Comments_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });

        }


    }





}