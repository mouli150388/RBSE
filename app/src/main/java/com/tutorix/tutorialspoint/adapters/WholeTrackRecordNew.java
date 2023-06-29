package com.tutorix.tutorialspoint.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.NotesActivity;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.quiz.ReviewQuiz;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.video.VideoActivityMVP;

import java.util.ArrayList;
import java.util.List;

public class WholeTrackRecordNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<TrackModel> trackList;
    private Context context;

    private boolean isLoadingAdded = false;
    String selected_user_id;
    String selected_class_id;


    public WholeTrackRecordNew(Context context,String selected_user_id,String selected_class_id) {
        this.context = context;
        trackList = new ArrayList<>();
        this.selected_user_id=selected_user_id;
        this.selected_class_id=selected_class_id;
    }

    public List<TrackModel> getMovies() {
        return trackList;
    }

    public void setMovies(List<TrackModel> movies) {
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
        View v1 = inflater.inflate(R.layout.track_main_row, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final TrackModel track = trackList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;


                if(track.lecture_name==null||track.lecture_name.equalsIgnoreCase("null"))
                movieVH.title.setText("");else
                movieVH.title.setText(track.lecture_name);
                //movieVH.desc.setText("Lecture name");

                    try{
                        movieVH.date.setText(CommonUtils.mntsScndFormat(track.activity_duration) + " minutes on " + CommonUtils.getTimeInAMPM(track.activity_date)+" "+CommonUtils.getstringDate(track.activity_date));

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                movieVH.layout.setTag(position);

                if(track.activity_type==null)
                {

                } else if (track.activity_type.equalsIgnoreCase("N")) {
                    movieVH.desc.setText(" Studied Notes");

                   // movieVH.image1.setImageResource(R.drawable.notesiconpurple);
                    movieVH.image1.setImageResource(R.drawable.ic_notes_new);

                    movieVH.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = (int) v.getTag();

                            AppController.getInstance().playAudio(R.raw.button_click);
                            if (new SharedPref().isExpired(context)) {
                                Intent i = new Intent(context, SubscribePrePage.class);
                                i.putExtra("flag", "M");
                                context.startActivity(i);
                                return;
                            }
                            Intent i = new Intent(context, NotesActivity.class);
                            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra(Constants.lectureId, trackList.get(pos).lecture_id);
                            i.putExtra(Constants.subjectId, trackList.get(pos).subject_id);
                            i.putExtra(Constants.classId, trackList.get(pos).class_id);
                            i.putExtra(Constants.userId, trackList.get(pos).user_id);
                            i.putExtra(Constants.sectionId, trackList.get(pos).section_id);
                            i.putExtra(Constants.lectureName, trackList.get(pos).lecture_name);
                            i.putExtra("selected_user_id", selected_user_id);
                            i.putExtra("selected_class_id", selected_class_id);
                            context.startActivity(i);

                            ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            //AppController.getInstance().playAudio(R.raw.qz_next);
                            AppController.getInstance().startItemAnimation(v);
                        }
                    });
                } else if (track.activity_type.equalsIgnoreCase("V")) {
                    movieVH.desc.setText(" Watched Video");
                    //movieVH.image1.setImageResource(R.drawable.d);
                    //movieVH.image1.setImageResource(R.drawable.vtrackicon);
                    movieVH.image1.setImageResource(R.drawable.ic_video_new);
                    movieVH.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppController.getInstance().playAudio(R.raw.button_click);
                            if (new SharedPref().isExpired(context)) {
                                Intent i = new Intent(context, SubscribePrePage.class);
                                i.putExtra("flag", "M");
                                context.startActivity(i);
                                return;
                            }
                            int pos = (int) v.getTag();
                            Intent i = new Intent(context, VideoActivityMVP.class);
                            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("lecture_video_url", Constants.VIDEO_NAME);
                            i.putExtra(Constants.lectureSRTUrl, Constants.VIDEO_SRT);
                            i.putExtra(Constants.lectureId, trackList.get(pos).lecture_id);
                            i.putExtra(Constants.subjectId, trackList.get(pos).subject_id);
                            i.putExtra(Constants.classId, trackList.get(pos).class_id);
                            i.putExtra(Constants.userId, trackList.get(pos).user_id);
                            i.putExtra(Constants.sectionId, trackList.get(pos).section_id);
                            i.putExtra(Constants.lectureName, trackList.get(pos).lecture_name);
                            i.putExtra(Constants.sectionName, "");
                            i.putExtra("selected_user_id", selected_user_id);
                            i.putExtra("selected_class_id", selected_class_id);
                            context.startActivity(i);
                            ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            AppController.getInstance().startItemAnimation(v);
                           // AppController.getInstance().playAudio(R.raw.qz_next);
                        }

                    });
                } else if(track.activity_type.equalsIgnoreCase("M")){

                        movieVH.desc.setText(" Attended Mock Test ");
                        //movieVH.image1.setImageResource(R.drawable.quiziconpurple);
                        movieVH.image1.setImageResource(R.drawable.ic_quiz_new);
                        movieVH.layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppController.getInstance().playAudio(R.raw.button_click);
                                if (new SharedPref().isExpired(context)) {
                                    Intent i = new Intent(context, SubscribePrePage.class);
                                    i.putExtra("flag", "M");
                                    context.startActivity(i);
                                    return;
                                }
                                int pos = (int) v.getTag();
                                Intent i = new Intent(context, ReviewQuiz.class);
                                i.setFlags(FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("quiz_id", trackList.get(pos).quiz_id);
                                i.putExtra("comingfromtrack", "track");
                                i.putExtra("lecture_time", trackList.get(pos).activity_date);
                                i.putExtra(Constants.userId, trackList.get(pos).user_id);
                                i.putExtra("subject_id", trackList.get(pos).subject_id);
                                i.putExtra("mock_test", "M");
                                i.putExtra("selected_user_id", selected_user_id);
                                i.putExtra("selected_class_id", selected_class_id);
                                Log.v("selected_user_id","selected_user_id 22 "+selected_user_id+" selected_class_id "+selected_class_id);

                                context.startActivity(i);
                                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                               // AppController.getInstance().playAudio(R.raw.qz_next);
                                AppController.getInstance().startItemAnimation(v);
                            }
                        });

                }else{
                    movieVH.desc.setText(" Attended Quiz ");
                    //movieVH.image1.setImageResource(R.drawable.quiziconpurple);
                    movieVH.image1.setImageResource(R.drawable.ic_quiz_new);
                    movieVH.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppController.getInstance().playAudio(R.raw.button_click);
                            if (new SharedPref().isExpired(context)) {
                                Intent i = new Intent(context, SubscribePrePage.class);
                                i.putExtra("flag", "M");
                                context.startActivity(i);
                                return;
                            }
                            int pos = (int) v.getTag();
                            Intent i = new Intent(context, ReviewQuiz.class);
                            i.setFlags(FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("quiz_id", trackList.get(pos).quiz_id);
                            i.putExtra("comingfromtrack", "track");
                            i.putExtra("lecture_time", trackList.get(pos).activity_date);
                            i.putExtra(Constants.userId, trackList.get(pos).user_id);
                            i.putExtra("subject_id", trackList.get(pos).subject_id);
                            i.putExtra("mock_test", "");
                            i.putExtra("selected_user_id", selected_user_id);
                            i.putExtra("selected_class_id", selected_class_id);
                            Log.v("selected_user_id","selected_user_id 22 "+selected_user_id+" selected_class_id "+selected_class_id);

                            context.startActivity(i);
                            ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            //AppController.getInstance().playAudio(R.raw.qz_next);
                            AppController.getInstance().startItemAnimation(v);
                        }
                    });
                }

                break;
            case LOADING:
//                Do nothing
                break;
        }

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

    public void add(TrackModel mc) {
        try{


        trackList.add(mc);
        notifyItemInserted(trackList.size() - 1);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addAll(List<TrackModel> mcList) {
        for (TrackModel mc : mcList) {
            add(mc);
        }

    }

    public void remove(TrackModel city) {
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


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new TrackModel());
    }

    public void removeLoadingFooter() {
        try{
        isLoadingAdded = false;

        int position = trackList.size() - 1;
        TrackModel item = getItem(position);

        if (item != null) {
            trackList.remove(position);
            notifyItemRemoved(position);
        }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public TrackModel getItem(int position) {
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
            //floataction = itemView.findViewById(R.id.floataction);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
