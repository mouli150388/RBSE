package com.tutorix.tutorialspoint.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewholder> {
    ArrayList<Integer> sliderDataArrayList = new ArrayList<>();
    ArrayList<String> sliderDataName = new ArrayList<>();
    ArrayList<String> sliderDataDesi = new ArrayList<>();

    public SliderAdapter(ArrayList<Integer> sliderDataArrayLists) {
        sliderDataArrayList.clear();
        sliderDataArrayList.addAll(sliderDataArrayLists);
        sliderDataName.add("Shri Ashok Gehlot");
        sliderDataName.add("Dr. B.D. Kalla");
        sliderDataName.add("Smrt. Zahida Khan");

        sliderDataDesi.add("Hon'ble Chief Minister\nRajasthan");
        sliderDataDesi.add("Hon'ble Cabinet Minister\nEducation(Primary & Secondary)\nRajasthan");
        sliderDataDesi.add("Hon'ble State Minister\nEducation(Primary & Secondary)\nRajasthan");
    }

    @Override
    public SliderViewholder onCreateViewHolder(ViewGroup parent) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slider, null);
        // on below line we are passing that view to view holder class.
        return new SliderViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(SliderViewholder viewHolder, int position) {

        Picasso.with(viewHolder.itemView.getContext()).load(sliderDataArrayList.get(position)).into(viewHolder.idIVSliderItem);
        viewHolder.txt_name.setText(sliderDataName.get(position));
        viewHolder.txt_desc.setText(sliderDataDesi.get(position));
    }

    @Override
    public int getCount() {
        return sliderDataArrayList.size();
    }

    class SliderViewholder extends ViewHolder {
        ImageView idIVSliderItem;
        TextView txt_name;
        TextView txt_desc;

        public SliderViewholder(View itemView) {
            super(itemView);
            idIVSliderItem = itemView.findViewById(R.id.idIVSliderItem);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_desc = itemView.findViewById(R.id.txt_desc);
        }
    }
}
