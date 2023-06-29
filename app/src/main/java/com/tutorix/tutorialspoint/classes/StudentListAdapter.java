package com.tutorix.tutorialspoint.classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.classes.model.StudentDetails;
import com.tutorix.tutorialspoint.quiz.ReviewQuiz;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<StudentDetails> trackList;
    private Context context;

    private boolean isLoadingAdded = false;


    public StudentListAdapter(Context context) {
        this.context = context;
        trackList = new ArrayList<>();
    }

    public List<StudentDetails> getMovies() {
        return trackList;
    }

    public void setMovies(List<StudentDetails> movies) {
        this.trackList = movies;
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
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.student_list_item, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final StudentDetails track = trackList.get(position);
        ((MovieVH)holder).image1.setImageResource(R.drawable.profile);
        ((MovieVH)holder).title.setText(track.user_name);
        ((MovieVH)holder).desc.setText(AppConfig.getClassNameDisplay(track.class_id)+", "+track.batch_name+ " ( "+track.branch_name+", "+track.branch_city+" ) ");
       // ((MovieVH)holder).date.setText(AppConfig.getClassNameDisplay(track.class_id));
       // ((MovieVH)holder).date.setCompoundDrawables(null,null,null,null);
        ((MovieVH)holder).lnr_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(v.getContext(),StudentPerformanceActivity.class);
              in.putExtra("selected_user_id",track.user_id);
              in.putExtra("selected_class_id",track.class_id);
                in.putExtra("student_name",track.user_name);
                v.getContext().startActivity(in);
            }
        });

        ((MovieVH)holder).lnr_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(track.studentQuiz==null)
                {
                    CommonUtils.showToast(v.getContext(),context.getString(R.string.no_quiz_attempted));
                    return;
                }
                Intent in=new Intent(v.getContext(), ReviewQuiz.class);
              in.putExtra("selected_user_id",track.user_id);
              in.putExtra("mock_test","");
              in.putExtra("subject_id","");
                in.putExtra("quiz_id", track.studentQuiz.quiz_id);
                in.putExtra("comingfromtrack", "student");
                in.putExtra("lecture_time", "");
                //in.putExtra(Constants.userId, trackList.get(pos).user_id);

                v.getContext().startActivity(in);
            }
        });

        ((MovieVH)holder).lnr_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(v.getContext(), StudentTrackActivity.class);
              in.putExtra("selected_user_id",track.user_id);
                in.putExtra("selected_class_id",track.class_id);
                in.putExtra("student_name",track.user_name);
                v.getContext().startActivity(in);
            }
        });



    }

    @Override
    public int getItemCount() {
        return trackList == null ? 0 : trackList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == trackList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(StudentDetails mc) {
        try{


        trackList.add(mc);
        notifyItemInserted(trackList.size() - 1);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addAll(List<StudentDetails> mcList) {
        for (StudentDetails mc : mcList) {
            add(mc);
        }

    }

    public void remove(StudentDetails city) {
        try{
            int position = trackList.indexOf(city);
            if (position > -1&&trackList.size()>position) {
                trackList.remove(position);
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




    public StudentDetails getItem(int position) {
        return trackList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        ImageView image1;
        LinearLayout layout;
        LinearLayout lnr_performance;
        LinearLayout lnr_quiz;
        LinearLayout lnr_track;
        TextView title;
        TextView date;
        TextView desc;
        //ImageView floataction;


        public MovieVH(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.crowTitle);
            date = itemView.findViewById(R.id.crowDate);
            desc = itemView.findViewById(R.id.crowDesc);
            image1 = itemView.findViewById(R.id.crowImg);
            layout = itemView.findViewById(R.id.layout);
            lnr_performance = itemView.findViewById(R.id.lnr_performance);
            lnr_quiz = itemView.findViewById(R.id.lnr_quiz);
            lnr_track = itemView.findViewById(R.id.lnr_track);
            //floataction = itemView.findViewById(R.id.floataction);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
