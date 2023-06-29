package com.tutorix.tutorialspoint.testseries;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.testseries.data.TestQuestions;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.views.CircularProgressBarThumb;

import java.util.ArrayList;
import java.util.List;

public class TestSeriesReviewAdapter extends RecyclerView.Adapter<TestSeriesReviewAdapter.MyViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<TestQuestions> data;
    private String basepath="";
    String mathLib;
    String content="";
    Activity context;
    Resources res;
    boolean isActiveExpired;
    int currrentClsId;
    public TestSeriesReviewAdapter(Activity context, String basepath,int currrentClsId) {
        data =new ArrayList<>();
        this.basepath=basepath;
        this.context=context;
this.currrentClsId=currrentClsId;
        res=context.getResources();

        String assets= "file:///android_asset";
        mathLib= Constants.MathJax_Offline;




        SharedPref sh=new SharedPref();
        if(sh.isExpired(context))
        {
            isActiveExpired=true;
        }
    }

    @NonNull
    @Override
    public TestSeriesReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
            return new TestSeriesReviewAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull TestSeriesReviewAdapter.MyViewHolder holder, int position) {
        //Review mObject = data.get(position);


        if (holder instanceof TestSeriesReviewAdapter.HeaderViewHolder) {
            /*((TestSeriesReviewAdapter.HeaderViewHolder) holder).marks.setText(data.get(position).total_correct + "/" + String.valueOf(data.get(position).total));
            ((TestSeriesReviewAdapter.HeaderViewHolder) holder).correct.setText(" Correct Answers : "+((data.get(position).total_correct.length()==1)?"0"+data.get(position).total_correct:data.get(position).total_correct ));
            ((TestSeriesReviewAdapter.HeaderViewHolder) holder).attempt.setText(" Attempted : "+((data.get(position).attempted_questions.length()==1)?"0"+data.get(position).attempted_questions:data.get(position).attempted_questions ) );
            ((TestSeriesReviewAdapter.HeaderViewHolder) holder).notattempt.setText(" Wrong Answers : "+((data.get(position).total_wrong.length()==1)?"0"+data.get(position).total_wrong:data.get(position).total_wrong ));


            if(data.get(position).total_correct!=null&&!data.get(position).total_correct.trim().equals("0")) {
                 ((TestSeriesReviewAdapter.HeaderViewHolder) holder).holoCircularProgressBar.setProgress(((float)Integer.parseInt(data.get(position).total_correct))/data.get(position).total);
            }
            else{
                ((TestSeriesReviewAdapter.HeaderViewHolder) holder).holoCircularProgressBar.setProgress(0);
                ((TestSeriesReviewAdapter.HeaderViewHolder) holder).holoCircularProgressBar.setMarkerEnabled(false);

            }*/

        } else {
            String quesValue = data.get(position).question;
            String s="";
            String correctwrongString = "";
            if(data.get(position).question_type.equalsIgnoreCase("O")&&(currrentClsId==8))
            {

                if (data.get(position).answer.equalsIgnoreCase(data.get(position).my_answer)) {
                    // correctwrongString = "</br><b>You have selected the option as</b> " + "<span class ='a'>" + selected_alpha + "</span>" + " <b>which is absolutely right</b><br>";
                    correctwrongString = "<font color='#009245'>"+ context.getString(R.string.your_anser)+" "+ "<span >" + data.get(position).my_answer + "</span> "+context.getString(R.string.is_correct)+"</font>";
                } else {
                    if(data.get(position).my_answer.isEmpty())
                        correctwrongString = "<font color='#FF7058'>"+ context.getString(R.string.you_did_not_attempt_this_question)+ ". " + " The answer is " + "<span >" + data.get(position).answer + "</span></font>";
                    else {
                        // correctwrongString = "</br><b>You have selected the option as</b> " + "<span class ='a'>" + selected_alpha + "</span>" + " <b>which is wrong. The correct answer is</b> " + "<span class ='a'>" + correct + "</span><br>";
                        correctwrongString = "<font color='#FF7058'>"+context.getString(R.string.your_anser)+" "+ "<span >" + data.get(position).my_answer + "</span> is wrong. The correct answer is " + "<span >" + data.get(position).answer + "</span></font>";
                    }
                }
            }else
            {
                String a0 = data.get(position).options.get(0).option_1;
                String a1 = data.get(position).options.get(0).option_2;
                String a2 = data.get(position).options.get(0).option_3;
                String a3 = data.get(position).options.get(0).option_4;

                //Log.d("quesValue", quesValue);
                String selected = data.get(position).option_selected+"";
                String selected_alpha="";
                String answer_alpha="";
                a0=getIntialTable("A",a0);
                if(a1!=null)
                    a1=getIntialTable("B",a1);
                if(a2!=null)
                    a2=getIntialTable("C",a2);
                if(a3!=null)
                    a3=getIntialTable("D",a3);
                String correct = data.get(position).option_right+"";
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


                if (selected.equalsIgnoreCase(correct)) {
                    // correctwrongString = "</br><b>You have selected the option as</b> " + "<span class ='a'>" + selected_alpha + "</span>" + " <b>which is absolutely right</b><br>";
                    correctwrongString = "<font color='#009245'>"+context.getString(R.string.you_have_select_correct_option)+"</font>";
                } else {
                    if(selected.isEmpty()|| selected.equals("-1"))
                        correctwrongString = "<font color='#FF7058'>"+context.getString(R.string.you_did_not_attempt_this_question )+"" + " The answer is " + "<span class ='a'>" + answer_alpha + "</span></font>";
                    else {
                        // correctwrongString = "</br><b>You have selected the option as</b> " + "<span class ='a'>" + selected_alpha + "</span>" + " <b>which is wrong. The correct answer is</b> " + "<span class ='a'>" + correct + "</span><br>";
                        correctwrongString = "<font color='#FF7058'>You have selected the wrong option. The correct option is " + "<span class ='a'>" + answer_alpha + "</span></font>";
                    }
                }



                //holder.explain.loadData(getHtmlData("<div>" + "<b>Explanation :</b><br>"+correctwrongString + " " + explanation + "</div>"), "text/html; charset=utf-8", "utf-8");

                if(a0!=null)
                    s=s+""+a0;
                if(a1!=null)
                    s=s+""+a1;
                if(a2!=null)
                    s=s+""+a2;
                if(a3!=null)
                    s=s+""+a3;

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

                        holder.lnr_watchvideo.setVisibility(View.GONE);
                        holder.txt_qnt_type.setVisibility(View.GONE);

                }
            });


            final String explanation = data.get(position).explanation;
            content=getQtnTable("<font  color='#009245'><b>"+(position+1)+"Q) </b></font><font  color='#4e4e4f'>" + quesValue+"</font>")+s+getExplanation(""  + correctwrongString );

              Log.v("quesValue "+position,"quesValue "+content);

            holder.question.loadDataWithBaseURL(basepath, getHtmlData(content) , "text/html", "utf-8", "");
            holder.webview_explanation.loadDataWithBaseURL(basepath, getHtmlData(explanation) , "text/html", "utf-8", "");


            holder.txt_watchexplanation.setText("View Explanation");
            holder.txt_watchexplanation.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(((TextView)v).getText().toString().contains("View"))
                                ((TextView)v).setText("Hide Explanation");
                            else
                                ((TextView)v).setText("View Explanation");

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

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return (data.size()==0)?0:data.size() ;
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

    public void addData(List<TestQuestions> quizModel) {
        if(data==null)
            data=new ArrayList<>();
        data.addAll(quizModel);
        notifyDataSetChanged();

    }

    public void clearAll()
    {
        if(data!=null)
        data.clear();
        notifyDataSetChanged();
    }
    public void addData(TestQuestions quizModel) {
        if(data==null)
            data=new ArrayList<>();
        data.add(quizModel);
        notifyDataSetChanged();

    }

    public TestQuestions getItems(int i) {
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

    class HeaderViewHolder extends TestSeriesReviewAdapter.MyViewHolder {
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