package com.tutorix.tutorialspoint.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.models.Notifications;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class AllNotificationAdapter extends RecyclerView.Adapter<AllNotificationAdapter.MyViewHolder> {
    private final List<Notifications> data;
    Context context;
    public AllNotificationAdapter(List<Notifications> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_main, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.textView.setText(data.get(position).title.trim());
        holder.txt_time.setText(CommonUtils.daysBetween(data.get(position).time));
        // holder.logo.setImageResource(R.mipmap.ic_launcher);
        String descritption = data.get(position).message;
        holder.description.setText(descritption);
        String imagePath=data.get(position).image;
        if(imagePath!=null&&imagePath.trim().length()>0)
        Picasso.with(context).load(imagePath).into(holder.image);
        else holder.image.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final TextView description;
        final ImageView image;
        final TextView txt_time;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.chapter_name);
            description = view.findViewById(R.id.chapter_desc);
            image = view.findViewById(R.id.image);
            txt_time = view.findViewById(R.id.txt_time);

        }
    }
}
