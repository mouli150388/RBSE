package com.tutorix.tutorialspoint.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.doubts.DoubtDetailsActivity;
import com.tutorix.tutorialspoint.models.DoubtModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    List<DoubtModel>listDoubts;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private String subject = "";
    private boolean isLoadingAdded = false;
  /*  private int status_1;
    private int status_2;
    private int status_3;
    private int status_4;*/
  Resources res;

    public QuestionsAdapter(Activity activity) {
        this.activity = activity;
        listDoubts=new ArrayList<>();
       /* status_1 = activity.getResources().getColor(R.color.status_1);
        status_2 = activity.getResources().getColor(R.color.status_2);
        status_3 = activity.getResources().getColor(R.color.status_3);
        status_4 = activity.getResources().getColor(R.color.status_4);*/
        res=activity.getResources();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //return new ViewHolder(inflater.inflate(R.layout.questions_layout_update, parent, false));
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM:
                viewHolder =  new ViewHolder(inflater.inflate(R.layout.questions_layout_update, parent, false));
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holders, int position) {


           // final JSONObject questionDetails = (JSONObject) questions.get(holder.getAdapterPosition());
        switch (getItemViewType(position)) {
            case ITEM:
                ViewHolder holder= (ViewHolder) holders;
                DoubtModel model = listDoubts.get(position);
                //holder.tvQuestionDescription.setText(model.question);


                switch (model.subject_id) {

                    case 1:
                        subject = "Physics";
                        holder.tvQuestionTitle.setTextColor(res.getColor(R.color.phy_background));
                        holder.tvQ.setImageResource(R.drawable.physics_quiz);
                        break;
                    case 2:
                        subject = "Chemistry";
                        holder.tvQuestionTitle.setTextColor(res.getColor(R.color.che_background));
                        holder.tvQ.setImageResource(R.drawable.chemistry_quiz);
                        break;
                    case 3:
                        subject = "Biology";
                        holder.tvQuestionTitle.setTextColor(res.getColor(R.color.bio_background));
                        holder.tvQ.setImageResource(R.drawable.biolog_quiz);
                        break;
                    case 4:
                        holder.tvQuestionTitle.setTextColor(res.getColor(R.color.math_background));
                        holder.tvQ.setImageResource(R.drawable.math_quiz);
                        subject = "Maths";
                        break;
                }

                String question_asked_time = model.question_asked_time;
                if (question_asked_time != null && question_asked_time.trim().length() > 2) {
                    holder.tvDate.setText(CommonUtils.getstringDateAndTime(question_asked_time));
                    //subject = subject + " " + CommonUtils.getstringDate(question_asked_time);
                }
            /*if(model.question_image!=null&&model.question_image.length()>0)
            {
                holder.img_img.setVisibility(View.VISIBLE);
            }else
            {*/
                holder.img_img.setVisibility(View.GONE);
                // }
                holder.tvQuestionDescription.loadDataWithBaseURL("", Constants.JS_FILES+  model.question.replaceAll("<img src=\"","<img src=\"https://www.tutorix.com"),"text/html","utf-8","");

                holder.tvQuestionTitle.setText(subject);
                if (model.question_status.equals("U")) {
                    holder.tvQuestionStatus.setText(activity.getString(R.string.view_answer));

                } else if (model.question_status.equals("O")) {
                    holder.tvQuestionStatus.setText(activity.getString(R.string.view_answer));

                } else if (model.question_status.equals("C")) {
                    holder.tvQuestionStatus.setText(activity.getString(R.string.closed));
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppController.getInstance().playAudio(R.raw.button_click);
                        Intent i = new Intent(activity, DoubtDetailsActivity.class);
                        i.putExtra("q_user_id", listDoubts.get(position).user_id);
                        i.putExtra("q_user_name", listDoubts.get(position).full_name);
                        i.putExtra("q_user_image", listDoubts.get(position).user_photo);
                        i.putExtra("question", listDoubts.get(position).question);
                        i.putExtra("question_id", listDoubts.get(position).question_id);
                        i.putExtra("question_image", listDoubts.get(position).question_image);
                        i.putExtra("subject_id", listDoubts.get(position).subject_id);
                        i.putExtra("question_status", listDoubts.get(position).question_status);
                        i.putExtra("question_asked_time", listDoubts.get(position).question_asked_time);
                        activity.  startActivityForResult(i, 100);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    }
                });
                holder.tvQuestionDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.performClick();
                    }
                });holder.tvQuestionTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.performClick();
                    }
                });
                break;
            case LOADING:
                break;
        }


    }

    @Override
    public int getItemCount() {
        return listDoubts.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == listDoubts.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(DoubtModel mc) {
        listDoubts.add(mc);
        notifyItemInserted(listDoubts.size() - 1);
    }

    public void remove(DoubtModel city) {
        int position = listDoubts.indexOf(city);
        if (position > -1) {
            listDoubts.remove(position);
            notifyItemRemoved(position);
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
        add(new DoubtModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if(listDoubts.size()<=0)
            return;
        int position = listDoubts.size() - 1;
        DoubtModel item = getItem(position);

        if (item != null) {
            listDoubts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addDoubts(List<DoubtModel> listDoubt) {
        listDoubts.clear();
       // listDoubts.addAll(listDoubt);
        for (DoubtModel mc : listDoubt) {
            add(mc);
        }
        notifyDataSetChanged();
    }
    public DoubtModel getItem(int position) {
        return listDoubts.get(position);
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView  tvQuestionTitle, tvQuestionStatus, tvDate;

        WebView tvQuestionDescription;
        ImageView img_img;
        ImageView tvQ;

        ViewHolder(View view) {
            super(view);
            tvQuestionTitle = view.findViewById(R.id.tvQuestionTitle);
            tvQuestionDescription = view.findViewById(R.id.tvQuestionDescription);
            tvQuestionStatus = view.findViewById(R.id.tvQuestionStatus);
            tvDate = view.findViewById(R.id.tvDate);
            img_img = view.findViewById(R.id.img_img);
            tvQ = view.findViewById(R.id.tvQ);

        }
    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
