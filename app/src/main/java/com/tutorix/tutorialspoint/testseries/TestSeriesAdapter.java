package com.tutorix.tutorialspoint.testseries;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.testseries.data.TestSeries;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestSeriesAdapter extends RecyclerView.Adapter<TestSeriesAdapter.TestSeriesViewHolder> {

    ArrayList<TestSeries>listSeries;
    Activity activty;
    String disclaimer;
    String marks;
    List<String> rules;
  /*  int pysics_marks;
    int pysics_qtns;
    int chemistry_marks;
    int chemistry_qtns;
    int other_marks;
    int other_qtns;*/
    boolean isActiveExpired;
    public TestSeriesAdapter(ArrayList<TestSeries> listSerie, Activity activty, String disclaimer, List<String> rules, String marks)
    {
        listSeries=new ArrayList<>();
        listSeries.clear();
        listSeries.addAll(listSerie);
        this.activty=activty;
        this.disclaimer=disclaimer;
        this.rules=rules;
        this.marks=marks;
        SharedPref sh=new SharedPref();
        if(sh.isExpired(activty))
        {
            isActiveExpired=true;
        }
       /* this.pysics_marks=pysics_marks;
        this.pysics_qtns=pysics_qtns;
        this.chemistry_marks=chemistry_marks;
        this.chemistry_qtns=chemistry_qtns;
        this.other_marks=other_marks;
        this.other_qtns=other_qtns;*/
    }
    @NonNull
    @Override
    public TestSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_series_item, parent, false);
        return new TestSeriesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull TestSeriesViewHolder holder, int position) {
        holder.chapterName.setText(listSeries.get(position).name);
        holder.durationTVID.setText("");

            holder.txt_hindi.setEnabled(listSeries.get(position).hindi.equalsIgnoreCase("Y"));
            holder.txt_english.setEnabled(listSeries.get(position).english.equalsIgnoreCase("Y"));
        if ((isActiveExpired)&&!listSeries.get(position).demo_flag.equalsIgnoreCase("Y")) {
            holder.lnr_demo.setVisibility(View.VISIBLE);

        } else {

            holder.lnr_demo.setVisibility(View.GONE);
        }
        holder.txt_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // activty.startActivity(new Intent(activty,TestSeriesTrack.class));
                if ((isActiveExpired)&&!listSeries.get(position).demo_flag.equalsIgnoreCase("Y")) {
                    return;
                }
                Intent in=new Intent(activty,TestSeriesTrack.class);
                in.putExtra("test_series_type",listSeries.get(position).id);
                in.putExtra("test_series_name",listSeries.get(position).name);
                in.putExtra("test_series_file_name",listSeries.get(position).title);
                activty.startActivity(in);
            }
        });
        holder.lnr_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);

                Intent i = new Intent(activty, SubscribePrePage.class);
                i.putExtra("flag", "M");
                activty.startActivity(i);
            }
        });
        holder.txt_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(activty,TestSeriesRulesActivity.class);
                in.putExtra("test_series_type",listSeries.get(position).id);
                in.putExtra("test_series_name",listSeries.get(position).name);
                in.putExtra("test_series_file_name",listSeries.get(position).title);
                in.putExtra("disclaimer",disclaimer);
                in.putExtra("marks",marks);
                in.putStringArrayListExtra("rules", (ArrayList<String>) rules);
               /* in.putExtra("pysics_marks",pysics_marks);
                in.putExtra("pysics_qtns",pysics_qtns);
                in.putExtra("chemistry_marks",chemistry_marks);
                in.putExtra("chemistry_qtns",chemistry_qtns);
                in.putExtra("other_marks",other_marks);
                in.putExtra("other_qtns",other_qtns);*/
                activty.startActivity(in);
            }
        }); holder.txt_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(activty,TestSeriesRulesActivity.class);
                in.putExtra("test_series_type",listSeries.get(position).id);
                in.putExtra("test_series_name",listSeries.get(position).name);
                in.putExtra("test_series_file_name",listSeries.get(position).title);
                in.putExtra("disclaimer",disclaimer);
                in.putStringArrayListExtra("rules", (ArrayList<String>) rules);
               /* in.putExtra("pysics_marks",pysics_marks);
                in.putExtra("pysics_qtns",pysics_qtns);
                in.putExtra("chemistry_marks",chemistry_marks);
                in.putExtra("chemistry_qtns",chemistry_qtns);
                in.putExtra("other_marks",other_marks);
                in.putExtra("other_qtns",other_qtns);*/
                in.putExtra("marks",marks);
                activty.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listSeries.size();
    }

    class TestSeriesViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout bodyLayout;
        TextView chapterName, durationTVID;
        TextView txt_hindi, txt_english, txt_track,txt_subscribe;
        LinearLayout lnr_demo;
        public TestSeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            bodyLayout = itemView.findViewById(R.id.bodyLayout);
            chapterName = itemView.findViewById(R.id.chapter_name);
            durationTVID = itemView.findViewById(R.id.durationTVID);

            txt_hindi = itemView.findViewById(R.id.txt_hindi);
            txt_english = itemView.findViewById(R.id.txt_english);
            txt_track = itemView.findViewById(R.id.txt_track);
            txt_subscribe = itemView.findViewById(R.id.txt_subscribe);
            lnr_demo = itemView.findViewById(R.id.lnr_demo);
        }
    }
}
