package com.tutorix.tutorialspoint.toc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.R;

import java.util.ArrayList;

public class TOCAdapter extends RecyclerView.Adapter<TOCAdapter.MyViewHolder> {
    ArrayList<TOCSection> tocSectionList;

    public TOCAdapter() {
        tocSectionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toc_item_layout, parent, false);

        return new TOCAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TOCSection tocSection = tocSectionList.get(position);
        holder.textView.setText(tocSection.getSectionName());
        holder.description.setText(holder.textView.getContext().getString(R.string.duration) + " " + tocSection.getLectureDuration());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context ctx = view.getContext();
                Intent in = new Intent(ctx, TOCLecturesActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", tocSection);
                in.putExtra("BUNDLE", args);
//in.putParcelableArrayListExtra("lecture", (ArrayList<? extends Parcelable>) tocSection.getLectures())
                ctx.startActivity(in);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tocSectionList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView description;


        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.chapter_name);
            description = view.findViewById(R.id.chapter_desc);

        }
    }


    public void setList(ArrayList<TOCSection> tocSectionLists) {

        tocSectionList.clear();
        tocSectionList.addAll(tocSectionLists);
        notifyDataSetChanged();
    }
}
