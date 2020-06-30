package com.doston.shortvideo.Inbox_Message_Classes;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.doston.shortvideo.R;
import com.doston.shortvideo.SimpleClasses.Variables;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Doston_Inbox_Adapter extends RecyclerView.Adapter<Doston_Inbox_Adapter.CustomViewHolder > implements Filterable {
    public Context context;
    ArrayList<Doston_Inbox_Get_Set> inbox_dataList = new ArrayList<>();
    ArrayList<Doston_Inbox_Get_Set> inbox_dataList_filter = new ArrayList<>();
    private Doston_Inbox_Adapter.OnItemClickListener listener;
    private Doston_Inbox_Adapter.OnLongItemClickListener longlistener;

    Integer today_day=0;

    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(Doston_Inbox_Get_Set item);
    }
    public interface OnLongItemClickListener{
        void onLongItemClick(Doston_Inbox_Get_Set item);
    }

    public Doston_Inbox_Adapter(Context context, ArrayList<Doston_Inbox_Get_Set> user_dataList, Doston_Inbox_Adapter.OnItemClickListener listener, Doston_Inbox_Adapter.OnLongItemClickListener longlistener) {
        this.context = context;
        this.inbox_dataList=user_dataList;
        this.inbox_dataList_filter=user_dataList;
        this.listener = listener;
        this.longlistener=longlistener;

        // get the today as a integer number to make the dicision the chat date is today or yesterday
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public Doston_Inbox_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inbox_list,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Doston_Inbox_Adapter.CustomViewHolder viewHolder = new Doston_Inbox_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return inbox_dataList_filter.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView username,last_message,date_created;
        ImageView user_image;
        View bubble_message;

        public CustomViewHolder(View view) {
            super(view);
            user_image=itemView.findViewById(R.id.user_image);
            username=itemView.findViewById(R.id.username);
            last_message=itemView.findViewById(R.id.message);
            date_created=itemView.findViewById(R.id.datetxt);
            bubble_message=itemView.findViewById(R.id.bubble_message);
        }

        public void bind(final Doston_Inbox_Get_Set item, final Doston_Inbox_Adapter.OnItemClickListener listener, final  Doston_Inbox_Adapter.OnLongItemClickListener longItemClickListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });


        }




    }


    @Override
    public void onBindViewHolder(final Doston_Inbox_Adapter.CustomViewHolder holder, final int i) {

        final Doston_Inbox_Get_Set item=inbox_dataList_filter.get(i);
        holder.username.setText(item.getName());
        holder.last_message.setText(item.getMsg());
        holder.date_created.setText(ChangeDate(item.getDate()));

        if(!item.getPic().equals("") && item.getPic()!=null)
        Picasso.with(context).
                load(item.getPic())
                .resize(100,100)
                .placeholder(R.drawable.profile_image_placeholder).into(holder.user_image);

        // check the status like if the message is seen by the receiver or not
        String status = "" + item.getStatus();
        if (status.equals("0")) {
            holder.last_message.setTypeface(null, Typeface.BOLD);
            holder.last_message.setTextColor(context.getResources().getColor(R.color.white));
            holder.bubble_message.setVisibility(View.VISIBLE);
        } else {
            holder.last_message.setTypeface(null, Typeface.NORMAL);
            holder.last_message.setTextColor(context.getResources().getColor(R.color.dark_gray));
            holder.bubble_message.setVisibility(View.GONE);
        }


        holder.bind(item,listener,longlistener);

   }



    // this method will cahnge the date to  "today", "yesterday" or date
    public String ChangeDate(String date){
        //current date in millisecond
        long currenttime= System.currentTimeMillis();

        //database date in millisecond
        long databasedate = 0;
        Date d = null;
        try {
            d = Variables.df.parse(date);
            databasedate = d.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference=currenttime-databasedate;
        if(difference<86400000){
            int chatday= Integer.parseInt(date.substring(0,2));
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            if(today_day==chatday)
                return sdf.format(d);
            else if((today_day-chatday)==1)
                return "Yesterday";
        }
        else if(difference<172800000){
            int chatday= Integer.parseInt(date.substring(0,2));
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            if((today_day-chatday)==1)
                return "Yesterday";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");

        if(d!=null)
        return sdf.format(d);
        else
            return "";

    }


    // that function will filter the result
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inbox_dataList_filter = inbox_dataList;
                } else {
                    ArrayList<Doston_Inbox_Get_Set> filteredList = new ArrayList<>();
                    for (Doston_Inbox_Get_Set row : inbox_dataList) {

                          if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    inbox_dataList_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inbox_dataList_filter;
                return filterResults;

            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inbox_dataList_filter = (ArrayList<Doston_Inbox_Get_Set>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }





}