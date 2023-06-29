package com.tutorix.tutorialspoint.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;

public class SpoinnerSubAdapter extends BaseAdapter {
    String[]subjects=new String[]{"Select Subject","Physics","Chemistry","Biology","Mathematics"};

    @Override
    public int getCount() {
        return subjects.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_subject_items,null);

        TextView txt_title=view.findViewById(R.id.txt_title);
        txt_title.setText(subjects[position]);
        return view;
    }
}
