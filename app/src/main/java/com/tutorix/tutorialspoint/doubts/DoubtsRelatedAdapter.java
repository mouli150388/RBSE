package com.tutorix.tutorialspoint.doubts;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.models.RelatedQuestionModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoubtsRelatedAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

private Activity activity;
        List<RelatedQuestionModel> listDoubts;
private static final int ITEM = 0;
private static final int LOADING = 1;

private boolean isLoadingAdded = false;

        Resources res;
      /*  String span_style="<style>.span_text {\n" +
                "    line-height: 20px;color:#898888; \n" +
                "    display: table-cell;font-size:11px;\n" +
                "    vertical-align: middle;\n" +
                "}</style>";
        String eye="<img style='vertical-align:middle' width='16' src='https://www.tutorix.com/images/icon/eye.png'>";
        String timer="<img style='vertical-align:middle' width='12' src='https://www.tutorix.com/images/icon/timer.png'>";
        String un_check="<img style='vertical-align:middle;' width='12' src='https://www.tutorix.com/images/icon/close.png'>";
        String check="<img style='vertical-align:middle' width='12' src='https://www.tutorix.com/images/icon/check.png'>";*/
        String question_asked_time;
        //String span;
public DoubtsRelatedAdapter(Activity activity) {
        this.activity = activity;
        listDoubts=new ArrayList<>();

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
        viewHolder =  new ViewHolder(inflater.inflate(R.layout.doubts_new_layout_item, parent, false));
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
        RelatedQuestionModel model = listDoubts.get(position);
        //holder.tvQuestionDescription.setText(model.question);
                 question_asked_time = model.questionAskedTime;
              /*  span=span_style+"<p><span class='span_text' >"+"<span style='color:#000;'>"+model.class_name+", "+AppConfig.getSubjectNameCapital(model.subjectId+"")+"</span>";
                span=span+"</br>"+timer+" "+CommonUtils.getstringDateAndTime(question_asked_time);
                span=span+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+eye+" &nbsp;"+model.question_view_count+"  Views";

                if (model.questionStatus.equals("U")||model.questionStatus.equals("P")) {
                        span=span+" &nbsp;&nbsp;&nbsp;&nbsp;"+un_check;
                }else{
                        span=span+" &nbsp;&nbsp;&nbsp;&nbsp;"+check;
                }

                span=span+"</span></p>";*/




        if (question_asked_time != null && question_asked_time.trim().length() > 2) {
        holder.tvDate.setText(CommonUtils.getstringDateAndTime(question_asked_time));
        //subject = subject + " " + CommonUtils.getstringDate(question_asked_time);
        }
                holder.txt_view.setText(model.question_view_count+" Views");
                holder.txt_view.setVisibility(View.INVISIBLE);
                holder.tvQuestionDescription.getSettings().setAllowContentAccess(true);
                holder.tvQuestionDescription.getSettings().setAllowFileAccess(true);
                holder.tvQuestionDescription.getSettings().setJavaScriptEnabled(true);
        holder.tvQuestionDescription.loadDataWithBaseURL("",  model.question.replaceAll("<img src=\"","<img src=\"https://www.tutorix.com"),"text/html","utf-8","");
                holder.tvQuestionDescription.setClickable(true);
                holder.tvQuestionDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                holder.itemView.performClick();
                        }
                });
                holder.lnr_webview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                holder.itemView.performClick();
                        }
                });

                String sub="";


                if(model.question_board!=null&&(!model.question_board.isEmpty()))
                        sub=sub+model.question_board;



            if(model.question_marks>0) {
                if (sub.isEmpty())
                    sub = sub + model.question_marks + " Mark(s)";
                else
                    sub = sub + ", " + model.question_marks + " Mark(s)";
            }

            if(sub.trim().isEmpty())
            {
                holder.txt_marks.setVisibility(View.GONE);
            }else
            {
                holder.txt_marks.setVisibility(View.VISIBLE);
                holder.txt_marks.setText(sub);
            }


                holder.txt_subject.setText(", "+AppConfig.getSubjectNameCapital(model.subjectId+""));
        holder.txt_class.setText(model.class_name);
        if (model.questionStatus.equals("U")||model.questionStatus.equals("P")) {
        holder.txt_status.setText("");
                holder.img_status.setImageResource(R.drawable.ic_un_check);
        } else if (model.questionStatus.equals("O")) {
        holder.txt_status.setText("");
                holder.img_status.setImageResource(R.drawable.ic_check_mark);
        } else if (model.questionStatus.equals("C")) {
        holder.txt_status.setText("");
                holder.img_status.setImageResource(R.drawable.ic_check_mark);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        Intent i = new Intent(activity, DoubtsViewActivity.class);
        i.putExtra("q_user_id", listDoubts.get(position).userId);
        i.putExtra("q_user_name", listDoubts.get(position).fullName);
        i.putExtra("q_user_image", listDoubts.get(position).userPhoto);
        i.putExtra("question", listDoubts.get(position).question);
        i.putExtra("question_id", listDoubts.get(position).questionId);
        i.putExtra("question_image", listDoubts.get(position).questionImage);
        i.putExtra("subject_id", listDoubts.get(position).subjectId);
        i.putExtra("question_status", listDoubts.get(position).questionStatus);
        i.putExtra("question_asked_time", listDoubts.get(position).questionAskedTime);
        activity.  startActivityForResult(i, 100);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

        }
        });
        holder.tvQuestionDescription.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                holder.itemView.performClick();
                }
        });

        holder.txt_subject.setOnClickListener(new View.OnClickListener() {
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

public void add(RelatedQuestionModel mc) {
        listDoubts.add(mc);
        notifyItemInserted(listDoubts.size() - 1);
        }

public void remove(RelatedQuestionModel city) {
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
        add(new RelatedQuestionModel());
        }

public void removeLoadingFooter() {
        isLoadingAdded = false;

        if(listDoubts.size()<=0)
        return;
        int position = listDoubts.size() - 1;
        RelatedQuestionModel item = getItem(position);

        if (item != null) {
        listDoubts.remove(position);
        notifyItemRemoved(position);
        }
        }

public void addDoubts(List<RelatedQuestionModel> listDoubt) {
        listDoubts.clear();
        // listDoubts.addAll(listDoubt);
        for (RelatedQuestionModel mc : listDoubt) {
        add(mc);
        }
        notifyDataSetChanged();
        }
public RelatedQuestionModel getItem(int position) {
        return listDoubts.get(position);
        }
class ViewHolder extends RecyclerView.ViewHolder {

    TextView  tvDate;
    TextView txt_class;
    TextView txt_status;
    TextView txt_subject;
    TextView txt_marks;
    TextView txt_view;
    ImageView img_status;

    WebView tvQuestionDescription;
        LinearLayout lnr_webview;
    ViewHolder(View view) {
        super(view);
        tvQuestionDescription = view.findViewById(R.id.tvQuestionDescription);
        txt_status = view.findViewById(R.id.txt_status);
        txt_class = view.findViewById(R.id.txt_class);
        txt_subject = view.findViewById(R.id.txt_subject);
        txt_marks = view.findViewById(R.id.txt_marks);
        tvDate = view.findViewById(R.id.tvDate);
            lnr_webview = view.findViewById(R.id.lnr_webview);
            txt_view = view.findViewById(R.id.txt_view);
            img_status = view.findViewById(R.id.img_status);

    }
}
protected class LoadingVH extends RecyclerView.ViewHolder {

    public LoadingVH(View itemView) {
        super(itemView);
    }
}
}