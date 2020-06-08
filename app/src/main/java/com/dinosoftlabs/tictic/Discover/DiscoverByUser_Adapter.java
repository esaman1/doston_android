package com.dinosoftlabs.tictic.Discover;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dinosoftlabs.tictic.Profile.Profile_F;
import com.dinosoftlabs.tictic.R;
import com.dinosoftlabs.tictic.SimpleClasses.Fragment_Callback;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
//test
public class DiscoverByUser_Adapter extends RecyclerView.Adapter<DiscoverByUser_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Discover_user_Model> user_list;
    FragmentManager fragmentManager;

    public DiscoverByUser_Adapter(Context context,FragmentManager fragmentManager, ArrayList<Discover_user_Model> user_list) {
        this.context = context;
        this.user_list = user_list;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_by_user_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide.with(context).load(user_list.get(position).getProfile_pic()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(holder.profile_icon);
        holder.username.setText(user_list.get(position).getUsername());
        holder.fullname.setText(user_list.get(position).getFirst_name()+" "+user_list.get(position).getLast_name());
        holder.followers_videos.setText(prettyCount(user_list.get(position).getFollowersCount())+" followers \u00b7 "+prettyCount(user_list.get(position).getVideoCount())+" videos");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Profile_F profile_f = new Profile_F(new Fragment_Callback() {
                    @Override
                    public void Responce(Bundle bundle) {

//                        Call_Api_For_Singlevideos(currentPage);

                    }
                });
                FragmentTransaction transaction = fragmentManager.beginTransaction();

//                if(from_right_to_left)
//                    transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
//                else
                    transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);

                Bundle args = new Bundle();
                args.putString("user_id",user_list.get(position).getFb_id());
                args.putString("user_name",user_list.get(position).getUsername());
                args.putString("user_pic",user_list.get(position).getProfile_pic());
                profile_f.setArguments(args);
                transaction.addToBackStack(null);
                transaction.replace(R.id.MainMenuFragment, profile_f).commit();
            }
        });
    }

    public String prettyCount(String number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = Long.parseLong(number);
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }


    @Override
    public int getItemCount() {
        return user_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_icon;
        TextView username,fullname,followers_videos;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_icon = itemView.findViewById(R.id.profile_icon);
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            followers_videos = itemView.findViewById(R.id.followers_videos);
        }
    }
}
