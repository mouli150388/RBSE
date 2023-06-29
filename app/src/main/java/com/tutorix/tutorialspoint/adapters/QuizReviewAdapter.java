package com.tutorix.tutorialspoint.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.video.VideoPlayerActivity;
import com.tutorix.tutorialspoint.views.CircularProgressBarThumb;

import java.util.ArrayList;
import java.util.List;

public class QuizReviewAdapter extends RecyclerView.Adapter<QuizReviewAdapter.MyViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<QuizModel> data;
    private String basepath="";
    String mathLib;
    String content="";
    String subject_id;
    Activity context;
    Resources res;
    int color_theme;
    boolean isActiveExpired;
    public QuizReviewAdapter(List<QuizModel> data, Activity context, String basepath, String subject_id) {
        this.data = data;
        this.basepath=basepath;
        this.context=context;
        this.subject_id=subject_id;
        res=context.getResources();

        String assets= "file:///android_asset";
        mathLib=Constants.MathJax_Offline;


        if(subject_id.equals("1"))
            color_theme=res.getColor(R.color.phy_background);
        else  if(subject_id.equals("2"))
            color_theme=res.getColor(R.color.che_background);
        else  if(subject_id.equals("3"))
            color_theme=res.getColor(R.color.bio_background);
        else  if(subject_id.equals("4"))
            color_theme=res.getColor(R.color.math_background);


        SharedPref sh=new SharedPref();
        if(sh.isExpired(context))
        {
            isActiveExpired=true;
        }
    }

    @NonNull
    @Override
    public QuizReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            return new QuizReviewAdapter.HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
            return new QuizReviewAdapter.MyViewHolder(itemView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull QuizReviewAdapter.MyViewHolder holder, int position) {
        //Review mObject = data.get(position);


        if (holder instanceof QuizReviewAdapter.HeaderViewHolder) {
            ((HeaderViewHolder) holder).marks.setText(data.get(position).total_correct + "/" + String.valueOf(data.get(position).total));
            ((HeaderViewHolder) holder).correct.setText(context.getString(R.string.correct_answeres)+": "+((data.get(position).total_correct.length()==1)?"0"+data.get(position).total_correct:data.get(position).total_correct ));
            ((HeaderViewHolder) holder).attempt.setText(context.getString(R.string.attemped)+": "+((data.get(position).attempted_questions.length()==1)?"0"+data.get(position).attempted_questions:data.get(position).attempted_questions ) );
            ((HeaderViewHolder) holder).notattempt.setText(context.getString(R.string.wrong_answers)+": "+((data.get(position).total_wrong.length()==1)?"0"+data.get(position).total_wrong:data.get(position).total_wrong ));
            ((HeaderViewHolder) holder).marks.setTextColor(color_theme);
           // ((HeaderViewHolder) holder).progress_score.setMax(data.get(position).total);
            ((HeaderViewHolder) holder).holoCircularProgressBar.setProgressColor(color_theme);
           // ((HeaderViewHolder) holder).holoCircularProgressBar.setProgressBackgroundColor(Color.parseColor("#FF0000"));
            if(data.get(position).total_correct!=null&&!data.get(position).total_correct.trim().equals("0")) {
                //((HeaderViewHolder) holder).progress_score.setProgress(Integer.parseInt(data.get(position).total_correct));
                ((HeaderViewHolder) holder).holoCircularProgressBar.setProgress(((float)Integer.parseInt(data.get(position).total_correct))/data.get(position).total);
            }
            else{
                //((HeaderViewHolder) holder).progress_score.setProgress(0);
                ((HeaderViewHolder) holder).holoCircularProgressBar.setProgress(0);
                ((HeaderViewHolder) holder).holoCircularProgressBar.setMarkerEnabled(false);

            }

        } else {

             String a0 = data.get(position - 1).option_1;
             String a1 = data.get(position - 1).option_2;
             String a2 = data.get(position - 1).option_3;
             String a3 = data.get(position - 1).option_4;
             String quesValue = data.get(position - 1).question;
            //Log.d("quesValue", quesValue);
            String selected = data.get(position - 1).option_selected;
            String selected_alpha="";
            String answer_alpha="";
            a0=getIntialTable("A",a0);
            if(a1!=null)
            a1=getIntialTable("B",a1);
            if(a2!=null)
            a2=getIntialTable("C",a2);
            if(a3!=null)
            a3=getIntialTable("D",a3);
            String correct = data.get(position - 1).option_right;
            boolean is_correct_1=false;
            boolean is_correct_2=false;
            boolean is_correct_3=false;
            boolean is_correct_4=false;
            switch (correct) {
                case "1":
                    a0="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #009245; margin-top:5px;'>"+a0+"</div>";
                    answer_alpha="A";
                    is_correct_1=true;
                    break;
                case "2":
                    a1="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #009245; margin-top:5px;'>"+a1+"</div>";
                    answer_alpha="B";
                    is_correct_2=true;
                    break;
                case "3":
                    a2="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #009245; margin-top:5px;'>"+a2+"</div>";
                    answer_alpha="C";
                    is_correct_3=true;
                    break;
                case "4":
                    a3="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #009245; margin-top:5px;'>"+a3+"</div>";
                    answer_alpha="D";
                    is_correct_4=true;
                    break;
            }
            switch (selected) {
                case "1":
                    if(!is_correct_1)
                    a0="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #ef816c; margin-top:5px;'>"+a0+"</div>";
                    selected_alpha="A";
                    break;
                case "2":
                    if(!is_correct_2)
                    a1="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #ef816c; margin-top:5px;'>"+a1+"</div>";
                    selected_alpha="B";
                    break;
                case "3":
                    if(!is_correct_3)
                    a2="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #ef816c; margin-top:5px;'>"+a2+"</div>";
                    selected_alpha="C";
                    break;
                case "4":
                    if(!is_correct_4)
                    a3="<div style='background-color:#FFFF;border-radius: 18px;border: 1px solid #ef816c; margin-top:5px;'>"+a3+"</div>";
                    selected_alpha="D";
                    break;
            }



            //holder.question.loadData(position + "Q : " + quesValue, "text/html; charset=utf-8", "utf-8");
            holder.question.getSettings().setAllowFileAccess(true);
            holder.question.getSettings().setJavaScriptEnabled(true);
            holder.question.getSettings().setBuiltInZoomControls(false);
            holder.webview_explanation.getSettings().setAllowFileAccess(true);
            holder.webview_explanation.getSettings().setJavaScriptEnabled(true);
            holder.webview_explanation.getSettings().setBuiltInZoomControls(false);
            holder.question.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if(data.get(position-1).mock_test!=null&&data.get(position-1).mock_test.contains("M")) {
                        holder.lnr_watchvideo.setVisibility(View.VISIBLE);
                        holder.txt_qnt_type.setVisibility(View.VISIBLE);
                        if(data.get(position - 1).question_type.equalsIgnoreCase("e"))
                        holder.txt_qnt_type.setText("Easy");
                        else if(data.get(position - 1).question_type.equalsIgnoreCase("m"))
                            holder.txt_qnt_type.setText("Medium");
                        else if(data.get(position - 1).question_type.equalsIgnoreCase("h"))
                            holder.txt_qnt_type.setText("Hard");
                    }
                    else {
                        holder.lnr_watchvideo.setVisibility(View.GONE);
                        holder.txt_qnt_type.setVisibility(View.GONE);
                    }
                }
            });

            String correctwrongString = "";
            if (selected.equalsIgnoreCase(correct)) {
               // correctwrongString = "</br><b>You have selected the option as</b> " + "<span class ='a'>" + selected_alpha + "</span>" + " <b>which is absolutely right</b><br>";
                correctwrongString = "<font color='#009245'>"+context.getString(R.string.you_have_selected_the_correct_option)+"</font>";
            } else {
                if(selected.isEmpty()|| selected.equals("0"))
                    correctwrongString = "<font color='#FF7058'>"+context.getString(R.string.you_did_not_attempt_this_question)+". " +context.getString(R.string.the_answer_is)+ " " + "<span class ='a'>" + answer_alpha + "</span></font>";
                else {
                   // correctwrongString = "</br><b>You have selected the option as</b> " + "<span class ='a'>" + selected_alpha + "</span>" + " <b>which is wrong. The correct answer is</b> " + "<span class ='a'>" + correct + "</span><br>";
                    correctwrongString = "<font color='#FF7058'>"+context.getString(R.string.you_didnot_attempt_the_correct_option)+" " + "<span class ='a'>" + answer_alpha + "</span></font>";
                }
            }


            final String explanation = data.get(position - 1).explanation;
            //holder.explain.loadData(getHtmlData("<div>" + "<b>Explanation :</b><br>"+correctwrongString + " " + explanation + "</div>"), "text/html; charset=utf-8", "utf-8");
         String s="";
           if(a0!=null)
               s=s+""+a0;
            if(a1!=null)
                s=s+""+a1;
            if(a2!=null)
                s=s+""+a2;
            if(a3!=null)
                s=s+""+a3;


          content=getQtnTable("<font  color='#009245'><b>"+(position)+"Q) </b></font><font  color='#4e4e4f'>" + quesValue+"</font>")+s+getExplanation(""  + correctwrongString );
          //  Log.v("quesValue ","quesValue "+content);

            holder.question.loadDataWithBaseURL(basepath, getHtmlData(content) , "text/html", "utf-8", "");
            holder.webview_explanation.loadDataWithBaseURL(basepath, getHtmlData(explanation) , "text/html", "utf-8", "");
           // holder.txt_watchvideo.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
       /* if(data.get(position-1).mock_test!=null&&data.get(position-1).mock_test.contains("T"))
            holder.txt_watchvideo.setVisibility(View.VISIBLE);
        else
            holder.txt_watchvideo.setVisibility(View.GONE);*/
        holder.lnr_watchvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                if (new SharedPref().isExpired(context)) {
                    Intent i = new Intent(context, SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    context.startActivity(i);
                    return;
                }
                QuizModel model=  data.get(position-1);
                Intent i = new Intent(context, VideoPlayerActivity.class);
                i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Constants.lectureVideoUrl, "");
                i.putExtra(Constants.sectionName,model.section_name);

                i.putExtra(Constants.lectureId, model.lectur_id);
                i.putExtra(Constants.subjectId, model.subject_id);
                i.putExtra(Constants.classId, model.classId);
                i.putExtra(Constants.userId, model.userId);
                i.putExtra(Constants.sectionId, model.section_id);
                i.putExtra(Constants.lectureName, model.lecture_name);
                context. startActivity(i);

            }
        });
            holder.txt_watchexplanation.setText(context.getString(R.string.view_explanation));
        holder.txt_watchexplanation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(((TextView)v).getText().toString().contains("View"))
                        ((TextView)v).setText(context.getString(R.string.hide_explanation));
                        else
                        ((TextView)v).setText(context.getString(R.string.view_explanation));

                        if(holder.lnr_explanation.getVisibility()==View.VISIBLE)
                        holder.lnr_explanation.setVisibility(View.GONE);
                        else
                        holder.lnr_explanation.setVisibility(View.VISIBLE);
                    }
                }
        );

        }

    }
    private String loadData()
    {
        return "";
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return (data.size()==0)?0:data.size() + 1;
    }

    private String getHtmlData(String bodyHTML) {
        String head = mathLib+"<head>" +
                "<style>" +
               /* "h2 {" +
                "    font-size: 1.7em;" +
                "    line-height: 1.5em;" +
                "    font-weight: normal;" +
                "    position: relative;" +
                "    left: 0;" +
                "}" +*/
                /*"img{" +
                "    height: 200px  !important;" +
                "}" +*/
                ".a {" +
                "  font-size: 15px;" +
                "  width: 1.3em;" +
                "  height: 1.3em;" +
                "  line-height: 1.3em;" +
                "  display: inline-block;" +
                "  vertical-align: middle;" +
                "  background-color: #FF7058;" +
                "  border-radius: 50%;" +
                "  color: #fff;" +
                "  text-align: center;" +
                "  margin-right: .1em" +
                "}" +

               /* "h3 {" +
                "    font-size: 1.3em;" +
                "    line-height: 1.5em;" +
                "    text-transform: none;" +
                "    color: #000;" +
                "    position: relative;" +
                "    left: 0;" +
                "    border: 0;" +
                "}" +*/
               /* "ul li {" +
                "    text-align: justify;" +
                "    font-size: 15px;" +
                "    color: #000;" +
                "    line-height: 21px;" +
                "    margin: 0px;" +
                "    padding: 2px 0px 2px 20px;" +
                "}" +*/
                "p{" +
                "color: #000;" +
                "font-size: 14px!important;}" +
               /* "pre{" +
               *//* "    background: #484848;" +*//*
                "    padding:10px;" +
                *//*"    color: #CCCCCC;" +*//*
                "    font-family: monospace;" +
                "    margin: 1.5em 0px;" +
                "    height: auto;" +
                "    white-space:pre-line;" +
                "    word-wrap:pre;" +
                "}" +*/
               /* "h3 {" +
                "font-size: 1.1em !important;}" +
                "img{" +
                "max-width: 100%; " +
                "width:auto; height: " +
                "auto;}" +*/
                "body{" +
                "font-size: 14px!important;" +
                "line-height:21px;" +
                "color: #000;}" +
                "blockquote {" +
                "margin: 0px 0px;" +
                "padding: 0px 0px 0px 0px;" +
                "background: #ff0000;" +
                "border: 1px solid #eee;" +
                "border-width: 1px 0px;" +
                "font-family: Georgia,Times New Roman,Times,serif;}" +
                "table{font-size: 14px!important;}"+
                "</style>" +

                "</head>";
        return "<html>" + head + "<body >" + bodyHTML + "</body></html>";
    }

    public void addData(QuizModel quizModel) {
        if(data==null)
            data=new ArrayList<>();
        data.add(quizModel);
        notifyDataSetChanged();

    }

    public QuizModel getItems(int i) {
       return data.get(i);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         WebView question;
         WebView webview_explanation;
         TextView txt_watchvideo;
         View lnr_watchvideo;
         TextView txt_watchexplanation;
         TextView txt_qnt_type;
         View lnr_explanation;


        MyViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.question);
            webview_explanation = view.findViewById(R.id.webview_explanation);
            txt_watchexplanation = view.findViewById(R.id.txt_watchexplanation);
            txt_watchvideo = view.findViewById(R.id.txt_watchvideo);
            lnr_watchvideo = view.findViewById(R.id.lnr_watchvideo);
            txt_qnt_type = view.findViewById(R.id.txt_qnt_type);
            lnr_explanation = view.findViewById(R.id.lnr_explanation);



        }
    }

    class HeaderViewHolder extends QuizReviewAdapter.MyViewHolder {
         Button marks;
         TextView correct;
         TextView attempt;
         TextView notattempt;
         //ProgressBar progress_score;
         CircularProgressBarThumb holoCircularProgressBar;

        HeaderViewHolder(View view) {
            super(view);
            marks = view.findViewById(R.id.marks);
            correct = view.findViewById(R.id.correct);
            attempt = view.findViewById(R.id.attempt);
            notattempt = view.findViewById(R.id.notattempt);
            //progress_score = view.findViewById(R.id.progress_score);
            holoCircularProgressBar = view.findViewById(R.id.holoCircularProgressBar);
        }
    }

    private String getIntialTable(String number,String content)
    {
        return "<table style='margin:3px;'>\n " +
                "     <tr>\n" +
                "        <td  valign=\"center\"><span ><font  color='#686868'>" +
                number+") </font></td>\n" +
                "        <td> <font  color='#686868'>" +
                content+"</font></td>\n" +
                "     </tr>\n" +
                "  </table>";
    }

    private String getQtnTable(String content)
    {
        return "<table>\n" +
                "     <tr>\n" +
                "        <td >" +
                content+"</td>\n" +
                "     </tr>\n" +
                "  </table>" ;
    }
    private String getExplanation(String content)
    {
        return "<table >\n" +
                "     <tr>\n" +
                "        <td >" +
                content+"</td>\n" +
                "     </tr>\n" +
                "  </table>" ;
    }

    private String getStyle()
    {
        return "<style>"  +
                " table.quiz-table{margin:5px; padding:0px;border-collapse:collapse;}"+
                "table.quiz-table th, table.quiz-table td{padding:5px; border:1px solid #ccc;}" +

                " </style>";
    }
}