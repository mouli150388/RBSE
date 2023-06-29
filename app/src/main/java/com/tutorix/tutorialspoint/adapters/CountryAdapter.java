package com.tutorix.tutorialspoint.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.models.CountryModel;

import java.util.ArrayList;
import java.util.List;;

public class CountryAdapter extends BaseAdapter{
   List<CountryModel>listCountry;
    public CountryAdapter()
    {
        listCountry=new ArrayList<>();
    }

    public void setCountryList(List<CountryModel>listCountry)
    {
        this.listCountry.clear();
        this.listCountry.addAll(listCountry);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return listCountry.size();
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_country,null);
        TextView txt_title=view.findViewById(R.id.txt_title);
        txt_title.setText(listCountry.get(position).name);

        return view;
    }

}
