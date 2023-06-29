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
import com.tutorix.tutorialspoint.models.DoubtModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoubtsNewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    List<DoubtModel> listDoubts;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    /*String main_div="<div style='border-style: groove; border-radius:4px;border-width:2px;border-color:#eee;'>";*/
   /* String span_style="<style>.span_text {\n" +
            "    line-height: 20px;color:#898888; \n" +
            "    display: table-cell;font-size:11px;\n" +
            "    vertical-align: middle;\n" +
            "}</style>";
    String eye="<img style='vertical-align:middle' width='16' src='https://www.tutorix.com/images/icon/eye.png'>";
    String timer="<img style='vertical-align:middle' width='12' src='https://www.tutorix.com/images/icon/timer.png'>";
    String un_check="<img style='vertical-align:middle;' width='12' src='https://www.tutorix.com/images/icon/close.png'>";
    String check="<img style='vertical-align:middle' width='12' src='https://www.tutorix.com/images/icon/check.png'>";*/

    private boolean isLoadingAdded = false;
    /*  private int status_1;
      private int status_2;
      private int status_3;
      private int status_4;*/
    Resources res;

    public DoubtsNewAdapter(Activity activity) {
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
                DoubtModel model = listDoubts.get(position);
                //holder.tvQuestionDescription.setText(model.question);




                String question_asked_time = model.question_asked_time;
                if (question_asked_time != null && question_asked_time.trim().length() > 2) {
                    holder.tvDate.setText(CommonUtils.getstringDateAndTime(question_asked_time));
                    //subject = subject + " " + CommonUtils.getstringDate(question_asked_time);
                }

              /*  String span=span_style+"<p><span class='span_text' >"+"<span style='color:#000;'>"+model.class_name+", "+AppConfig.getSubjectNameCapital(model.subject_id+"")+"</span>";
                span=span+"</br>"+timer+" "+CommonUtils.getstringDateAndTime(question_asked_time);
                span=span+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+eye+" &nbsp;"+model.question_view_count+"  Views";

                if (model.question_status.equals("A")) {
                    span=span+" &nbsp;&nbsp;&nbsp;&nbsp;"+check;
                }else{
                    span=span+" &nbsp;&nbsp;&nbsp;&nbsp;"+un_check;
                }

                span=span+"</span></p>";*/

                holder.tvQuestionDescription.getSettings().setUseWideViewPort(false);
                holder.tvQuestionDescription.getSettings().setSupportZoom(true);
                holder.tvQuestionDescription.getSettings().setJavaScriptEnabled(true);

                holder.tvQuestionDescription.loadDataWithBaseURL("",model.question+"","text/html","utf-8","");
                holder.tvQuestionDescription.setClickable(true);
                holder.tvQuestionDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.performClick();
                    }
                }); holder.lnr_webview.setOnClickListener(new View.OnClickListener() {
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

                holder.txt_subject.setText(", "+ AppConfig.getSubjectNameCapital(model.subject_id+""));

                holder.txt_view.setText(model.question_view_count+" Views");
                holder.txt_class.setText(model.class_name);
                holder.img_status.setImageResource(R.drawable.ic_un_check);
                if (model.question_status.equals("A")) {
                    holder.txt_status.setText("");
                    holder.img_status.setImageResource(R.drawable.ic_check_mark);

                } else if (model.question_status.equals("U")||model.question_status.equals("D")||model.question_status.equals("P")) {
                    holder.txt_status.setText("");
                } else if (model.question_status.equals("C")) {
                    holder.txt_status.setText("");
                }else if (model.question_status.equals("R")) {
                    holder.txt_status.setText("");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppController.getInstance().playAudio(R.raw.button_click);
                        Intent i = new Intent(activity, DoubtsViewActivity.class);
                        if(activity instanceof LatestDoubtsActivity)
                            i.putExtra("own_doubt", false);
                        else
                            i.putExtra("own_doubt", true);
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

        TextView  tvDate;
        TextView txt_class;
        TextView txt_status;
        TextView txt_subject;
        TextView txt_marks;
        TextView txt_view;
        LinearLayout lnr_webview;
        ImageView img_status;

        WebView tvQuestionDescription;

        ViewHolder(View view) {
            super(view);
            tvQuestionDescription = view.findViewById(R.id.tvQuestionDescription);
            txt_status = view.findViewById(R.id.txt_status);
            txt_class = view.findViewById(R.id.txt_class);
            txt_subject = view.findViewById(R.id.txt_subject);
            txt_marks = view.findViewById(R.id.txt_marks);
            tvDate = view.findViewById(R.id.tvDate);
            txt_view = view.findViewById(R.id.txt_view);
            lnr_webview = view.findViewById(R.id.lnr_webview);
            img_status = view.findViewById(R.id.img_status);

        }
    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}