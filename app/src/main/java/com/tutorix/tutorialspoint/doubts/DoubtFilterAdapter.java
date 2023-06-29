package com.tutorix.tutorialspoint.doubts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.models.DoubtFilterModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoubtFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<DoubtFilterModel>listFilterModel;
    DoubtFilterAdapter(List<DoubtFilterModel>listFilterModel)
    {
        this.listFilterModel=listFilterModel;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoubtFilterViewhOlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.doubt_filter_sub_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ((DoubtFilterViewhOlder) holder).checkFilter.setText(listFilterModel.get(position).name);
        ((DoubtFilterViewhOlder) holder).checkFilter.setChecked(listFilterModel.get(position).isSelected);
        ((DoubtFilterViewhOlder) holder).txt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listFilterModel.get(position).isSelected=!listFilterModel.get(position).isSelected;
                ((DoubtFilterViewhOlder) holder).checkFilter.setChecked(!((DoubtFilterViewhOlder) holder).checkFilter.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFilterModel.size();
    }

    public String getSelected()
    {
        StringBuffer selected=new StringBuffer();

        for(int k=0;k<listFilterModel.size();k++)
            if(listFilterModel.get(k).isSelected)
                selected.append(listFilterModel.get(k).id+",");
            String s=selected.toString();
        if(s.endsWith(","))
            s=s.substring(0,s.lastIndexOf(","));
        Log.v("Selected Items","Selected Items "+s);
        return s;
    }

    class DoubtFilterViewhOlder extends RecyclerView.ViewHolder {
        @BindView(R.id.check_filter)
        CheckBox checkFilter;
        @BindView(R.id.txt_click)
        TextView txt_click;
        public DoubtFilterViewhOlder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
