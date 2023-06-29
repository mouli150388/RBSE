package com.tutorix.tutorialspoint.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private  List<QuizModel> data;
    private String basepath="";
    String mathLib;
    String mathLib0="";
    String mathLib1="";
    String mathLib2="";
    String mathLib3="";
    String mathLib4="";
    Context context;
    public ReviewAdapter(List<QuizModel> data, Context context, String basepath) {
        this.data = data;
        this.basepath=basepath;
this.context=context;

          String assets= "file:///android_asset";
        mathLib= Constants.MathJax_Offline;
        try {
            String[] userInfo = SessionManager.getUserInfo(context);
           String
            classId = userInfo[4];
            int currrentClsId = Integer.parseInt(classId);
            if (currrentClsId <=5) {
                mathLib=Constants.MATH_SCRIBE;
            } else  {
                mathLib=Constants.MathJax_Offline;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

/*

        mathLib0="<script type='text/javascript' src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'> MathJax.Hub.Register.StartupHook(\"End\",function () { AndroidInterface.showAndroidToast('0')});</script>";

        mathLib="<script type='text/x-mathjax-config'>MathJax.Hub.Config({tex2jax: { inlineMath: [['\\(','\\)']] },'HTML-CSS': {linebreaks: { automatic: true, width: 'container' }}});</script>" ;
        mathLib0="<script type='text/javascript' src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'> MathJax.Hub.Register.StartupHook(\"End\",function () { AndroidInterface.showAndroidToast('0')});</script>";
        mathLib1="<script type='text/javascript' src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'> MathJax.Hub.Register.StartupHook(\"End\",function () { AndroidInterface.showAndroidToast('1')});</script>";
        mathLib2="<script type='text/javascript' src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'> MathJax.Hub.Register.StartupHook(\"End\",function () { AndroidInterface.showAndroidToast('2')});</script>";
        mathLib3="<script type='text/javascript' src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'> MathJax.Hub.Register.StartupHook(\"End\",function () { AndroidInterface.showAndroidToast('3')});</script>";
        mathLib4="<script type='text/javascript' src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'> MathJax.Hub.Register.StartupHook(\"End\",function () { AndroidInterface.showAndroidToast('4')});</script>";
*/

    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_main, parent, false);
            return new MyViewHolder(itemView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Review mObject = data.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).marks.setText(data.get(position).total_correct + "/" + String.valueOf(data.get(position).total));
            ((HeaderViewHolder) holder).attempt.setText(data.get(position).total_correct + ((HeaderViewHolder) holder).attempt.getContext().getString(R.string.correct));
            ((HeaderViewHolder) holder).notattempt.setText(data.get(position).total_wrong + ((HeaderViewHolder) holder).attempt.getContext().getString(R.string.wrong));
        } else {
            final String quesValue = data.get(position - 1).question;
            //Log.d("quesValue", quesValue);
            String selected = data.get(position - 1).option_selected;
            switch (selected) {
                case "1":
                    holder.a0.setBackgroundColor(Color.parseColor("#f2af98"));
                    break;
                case "2":
                    holder.a1.setBackgroundColor(Color.parseColor("#f2af98"));
                    break;
                case "3":
                    holder.a2.setBackgroundColor(Color.parseColor("#f2af98"));
                    break;
                case "4":
                    holder.a3.setBackgroundColor(Color.parseColor("#f2af98"));
                    break;
            }
            String correct = data.get(position - 1).option_right;
            switch (correct) {
                case "1":
                    holder.a0.setBackgroundColor(Color.parseColor("#C5FFB8"));
                    break;
                case "2":
                    holder.a1.setBackgroundColor(Color.parseColor("#C5FFB8"));
                    break;
                case "3":
                    holder.a2.setBackgroundColor(Color.parseColor("#C5FFB8"));
                    break;
                case "4":
                    holder.a3.setBackgroundColor(Color.parseColor("#C5FFB8"));
                    break;
            }


            //holder.question.loadData(position + "Q : " + quesValue, "text/html; charset=utf-8", "utf-8");
            holder.question.getSettings().setAllowFileAccess(true);
            holder.question.getSettings().setJavaScriptEnabled(true);
            holder.question.getSettings().setBuiltInZoomControls(false);

            holder.question.loadDataWithBaseURL(basepath, "Q : " + quesValue, "text/html", "utf-8", "");


            holder.a0.getSettings().setAllowFileAccess(true);
            holder.a0.getSettings().setJavaScriptEnabled(true);
            holder.a0.getSettings().setBuiltInZoomControls(false);
            final String a0 = data.get(position - 1).option_1;
            //holder.a0.loadData(getHtmlData("<div><span>" + "A" + "</span> " + a0 + "</div>"), "text/html; charset=utf-8", "utf-8");


            holder.a0.loadDataWithBaseURL(basepath, getHtmlData(getIntialTable(1,a0 )), "text/html", "utf-8", "");


            holder.a1.getSettings().setAllowFileAccess(true);
            holder.a1.getSettings().setJavaScriptEnabled(true);
            holder.a1.getSettings().setBuiltInZoomControls(false);
            final String a1 = data.get(position - 1).option_2;
            //holder.a1.loadData(getHtmlData("<div><span>" + "B" + "</span> " + a1 + "</div>"), "text/html; charset=utf-8", "utf-8");

            holder.a1.loadDataWithBaseURL(basepath, getHtmlData(getIntialTable(1,a1 )), "text/html", "utf-8", "");


            holder.a2.getSettings().setAllowFileAccess(true);
            holder.a2.getSettings().setJavaScriptEnabled(true);
            holder.a2.getSettings().setBuiltInZoomControls(false);
            final String a2 = data.get(position - 1).option_3;
            // holder.a2.loadData(getHtmlData("<div><span>" + "C" + "</span> " + a2 + "</div>"), "text/html; charset=utf-8", "utf-8");
            holder.a2.loadDataWithBaseURL(basepath, getHtmlData(getIntialTable(2,a2 )), "text/html", "utf-8", "");


            holder.a3.getSettings().setAllowFileAccess(true);
            holder.a3.getSettings().setJavaScriptEnabled(true);
            holder.a3.getSettings().setBuiltInZoomControls(false);
            final String a3 = data.get(position - 1).option_4;
            //holder.a3.loadData(getHtmlData("<div><span>" + "D" + "</span> " + a3 + "</div>"), "text/html; charset=utf-8", "utf-8");
            holder.a3.loadDataWithBaseURL(basepath, getHtmlData(getIntialTable(3,a3 )), "text/html", "utf-8", "");

            String correctwrongString = "";
            if (selected.equalsIgnoreCase(correct)) {
                correctwrongString = "<b>"+context.getString(R.string.you_have_selected_the_option_as)+"</b> " + "<span class ='a'>" + selected + "</span>" + " <b>"+context.getString(R.string.wchic_is_absolute_right)+"</b><br>";
            } else {
                if(selected.isEmpty()|| selected.equals("0"))
                    correctwrongString = "<b>"+context.getString(R.string.you_did_not_attempt_this_question)+", </b> " + context.getString(R.string.the_answer_is)+" " + "<span class ='a'>" + correct + "</span><br>";
                    else
                correctwrongString = "<b>"+context.getString(R.string.you_have_selected_the_option_as)+"</b> " + "<span class ='a'>" + selected + "</span>" + " <b>+"+ context.getString(R.string.whic_is_wrong_)+"</b> " + "<span class ='a'>" + correct + "</span><br>";
            }

            holder.explain.getSettings().setAllowFileAccess(true);
            holder.explain.getSettings().setJavaScriptEnabled(true);
            holder.explain.getSettings().setBuiltInZoomControls(true);
            final String explanation = data.get(position - 1).explanation;
            //holder.explain.loadData(getHtmlData("<div>" + "<b>Explanation :</b><br>"+correctwrongString + " " + explanation + "</div>"), "text/html; charset=utf-8", "utf-8");

            holder.explain.loadDataWithBaseURL(basepath, getHtmlData("<div>" + "<b>"+context.getString(R.string.explanation)+":</b><br>" + correctwrongString + " " + explanation + "</div>"), "text/html", "utf-8", "");
        }

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
                "h2 {" +
                "    font-size: 1.7em;" +
                "    line-height: 1.5em;" +
                "    font-weight: normal;" +
                "    position: relative;" +
                "    left: 0;" +
                "}" +
                "img{" +
                "    height: 200px  !important;" +
                "}" +
                ".a {" +
                "  font-size: 18px;" +
                "  width: 1.5em;" +
                "  height: 1.5em;" +
                "  line-height: 1.5em;" +
                "  display: inline-block;" +
                "  vertical-align: middle;" +
                "  background-color: rgb(44, 44, 44);" +
                "  border-radius: 50%;" +
                "  color: #fff;" +
                "  text-align: center;" +
                "  margin-right: .1em" +
                "}" +

                "h3 {" +
                "    font-size: 1.3em;" +
                "    line-height: 1.5em;" +
                "    text-transform: none;" +
                "    color: #000;" +
                "    position: relative;" +
                "    left: 0;" +
                "    border: 0;" +
                "}" +
                "ul li {" +
                "    text-align: justify;" +
                "    font-size: 15px;" +
                "    color: #000;" +
                "    line-height: 21px;" +
                "    margin: 0px;" +
                "    padding: 2px 0px 2px 20px;" +
                "}" +
                "p{" +
                "color: #000;" +
                "font-size: 15px!important;}" +
                "pre{" +
                "    background: #484848;" +
                "    padding:10px;" +
                "    color: #CCCCCC;" +
                "    font-family: Open Sans;" +
                "    margin: 1.5em 0px;" +
                "    height: auto;" +
                "    white-space:pre-line;" +
                "    word-wrap:pre;" +
                "}" +
                "h3 {" +
                "font-size: 1.1em !important;}" +
                "img{" +
                "max-width: 100%; " +
                "width:auto; height: " +
                "auto;}" +
                "body{" +
                "font-size: 14px;" +
                "line-height:150%;}" +
                "blockquote {" +
                "margin: 10px 0px;" +
                "padding: 0px 15px 0px 15px;" +
                "background: #f5f5f5;" +
                "border: 1px solid #eee;" +
                "border-width: 1px 0px;" +
                "font-family: Open Sans,Georgia,Times New Roman,Times,serif;}" +
                "</style>" +

                "</head>";
        return "<html>" + head + "<body text=\"#595959\">" + bodyHTML + "</body></html>";
    }

    public void addData(QuizModel quizModel) {
        if(data==null)
            data=new ArrayList<>();
        data.add(quizModel);
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final WebView question;
        final WebView a0;
        final WebView a1;
        final WebView a2;
        final WebView a3;
        final WebView explain;

        MyViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.question);
            a0 = view.findViewById(R.id.a0);
            a1 = view.findViewById(R.id.a1);
            a2 = view.findViewById(R.id.a2);
            a3 = view.findViewById(R.id.a3);
            explain = view.findViewById(R.id.explain);


        }
    }

    class HeaderViewHolder extends MyViewHolder {
        final Button marks;
        final TextView attempt;
        final TextView notattempt;

        HeaderViewHolder(View view) {
            super(view);
            marks = view.findViewById(R.id.marks);
            attempt = view.findViewById(R.id.attempt);
            notattempt = view.findViewById(R.id.notattempt);
        }
    }

    private String getIntialTable(int number,String content)
    {
        return "<table>\n" +
                "     <tr>\n" +
                "        <td valign=\"center\"><span class ='a'>" +
                number+"</td>\n" +
                "        <td>" +
                content+"</td>\n" +
                "     </tr>\n" +
                "  </table>";
    }
}
