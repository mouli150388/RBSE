package com.tutorix.tutorialspoint.testseries;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.testseries.data.TestTrackModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestSeriesTrackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<TestTrackModel>listSeries;
    Activity activty;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public TestSeriesTrackAdapter( Activity activty)
    {
        listSeries=new ArrayList<>();
        listSeries.clear();
        this.activty=activty;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
       /* View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_series_track_item, parent, false);
        return new TestSeriesViewHolder(itemView);*/

    }
    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.test_series_track_item, parent, false);
        viewHolder = new TestSeriesViewHolder(v1);
        return viewHolder;
    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holders, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                TestSeriesViewHolder holder=(TestSeriesViewHolder)holders;
        holder.chapter_name.setText(activty.getString(R.string.attempted_on)+" "+listSeries.get(position).activity_date);

        holder.durationTVID.setText(String.format("%.2f", ((float)listSeries.get(position).total_correct/listSeries.get(position).total_questions))+"% marks secured in : "+toMins(listSeries.get(position).activity_duration)+" minuts");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(activty,TestSeriesReviewActivity.class);
                in.putExtra("test_series_id",listSeries.get(position).test_series_id);
                activty.startActivity(in);
            }
        });
                break;
            case LOADING:
//                Do nothing
                break;
        }

    }


    @Override
    public int getItemCount() {
        return listSeries == null ? 0 : listSeries.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == listSeries.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    class TestSeriesViewHolder extends RecyclerView.ViewHolder{
        LinearLayout bodyLayout;
        TextView chapter_name, durationTVID;
        TextView txt_test_type;
        public TestSeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            bodyLayout = itemView.findViewById(R.id.bodyLayout);
            chapter_name = itemView.findViewById(R.id.chapter_name);
            durationTVID = itemView.findViewById(R.id.durationTVID);

            txt_test_type = itemView.findViewById(R.id.txt_test_type);

        }
    }

    private static int toMins(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = (hour * 60)+mins;
        if(hoursInMins<=0)
            hoursInMins=1;
        return hoursInMins;
    }

    public void add(TestTrackModel mc) {
        try{


            listSeries.add(mc);
            notifyItemInserted(listSeries.size() - 1);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addAll(List<TestTrackModel> mcList) {
        for (TestTrackModel mc : mcList) {
            add(mc);
        }

    }

    public void remove(TestTrackModel city) {
        try{
            int position = listSeries.indexOf(city);
            if (position > -1&&listSeries.size()>position) {
                listSeries.remove(position);
                notifyItemRemoved(position);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new TestTrackModel());
    }
    public TestTrackModel getItem(int position) {
        return listSeries.get(position);
    }
    public void removeLoadingFooter() {
        try{
            isLoadingAdded = false;

            int position = listSeries.size() - 1;
            TestTrackModel item = getItem(position);

            if (item != null) {
                listSeries.remove(position);
                notifyItemRemoved(position);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
