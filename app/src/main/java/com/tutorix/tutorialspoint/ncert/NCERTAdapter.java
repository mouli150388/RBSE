package com.tutorix.tutorialspoint.ncert;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.LecturesActivity;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import java.io.File;
import java.util.List;

public class NCERTAdapter extends RecyclerView.Adapter<NCERTAdapter.MyViewHolder> {
    private final Activity context;
    private final List<NcertModel> data;


    Resources res;
    String mathLib="";
    String basePath="";
    String desc="";
    String sectionName="";
    public NCERTAdapter(List<NcertModel> data, Activity context,String basePaths,String sectionName) {
        this.data = data;
        this.context = context;
        mathLib=Constants.JS_FILES;
        basePath=basePaths;
        this.sectionName=sectionName;
        res=context.getResources();
    }

    @NonNull
    @Override
    public NCERTAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=null;

            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ncert_item_list, parent, false);

        return new NCERTAdapter.MyViewHolder(itemView);
    }
   

    @Override
    public void onBindViewHolder(@NonNull final NCERTAdapter.MyViewHolder holder, final int position) {
        final NcertModel ncertModel = data.get(holder.getAdapterPosition());
        

       /* if (Build.VERSION.SDK_INT >= 24) {

            holder.textView.setText(Html.fromHtml(ncertModel.question.trim(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
        } else {
            holder.textView.setText(Html.fromHtml(ncertModel.question.trim()));

        }*/
        holder.question.getSettings().setAllowFileAccess(true);
        holder.question.getSettings().setJavaScriptEnabled(true);
        holder.question.getSettings().setBuiltInZoomControls(true);

        holder.question.loadDataWithBaseURL(basePath,  getStyle()+ mathLib+ncertModel.question, "text/html", "utf-8", "");




        //holder.textView.setSelected(true);
          /*  holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.performClick();
                }
            });*/
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.performClick();
                }
            });  holder.txt_webview_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.performClick();
                }
            });

        desc="";
            if((ncertModel.exercise!=null&&!ncertModel.exercise.isEmpty())&&(!ncertModel.exercise.equals("null")))
        desc="Exercise: "+ncertModel.exercise+" \t\t";
            desc=desc+"Question "+ncertModel.exercise_qnum;
        holder.description.setText(desc);
        holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String descs="";
                    descs="";
                    if((ncertModel.exercise!=null&&!ncertModel.exercise.isEmpty())&&(!ncertModel.exercise.equals("null")))
                        descs="Exercise: "+ncertModel.exercise+" \t\t";
                    descs=descs+"Question : "+ncertModel.exercise_qnum;

                    AppController.getInstance().playAudio(R.raw.button_click);
                    AppController.getInstance().startItemAnimation(view);
                    Intent i = new Intent(context, NCERTQuestionDetailsActivity.class);
                    
                    i.putExtra(Constants.subjectId, ncertModel.subject_id);
                    i.putExtra(Constants.question_id, ncertModel.question_id);
                    i.putExtra(Constants.sectionName, sectionName);
                    i.putExtra("desc_excercise", descs);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                }
            });
        
        
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        WebView question;
        TextView description;
        TextView txt_webview_click;


        MyViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.question);
            description = view.findViewById(R.id.chapter_desc);
            txt_webview_click = view.findViewById(R.id.txt_webview_click);

        }
    }
    public static int getResourceId(Context context, String pVariableName, String pResourcename)
    {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){

        int dencity= context.getResources().getDisplayMetrics().densityDpi;
        if(dencity== DisplayMetrics.DENSITY_DEFAULT)
            return 120;
        float height=dp * (dencity/ DisplayMetrics.DENSITY_DEFAULT);
        //Log.v("height","height density "+height);

      /*  if(height>200)
           height=270;*/

        // Log.v("height","height density "+height);
        return height;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private String getStyle()
    {
        return "<style>"  +
                " table.quiz-table{margin:5px; padding:0px;border-collapse:collapse;}"+
                "table.quiz-table th, table.quiz-table td{padding:5px; border:1px solid #ccc;}" +
                " </style>";
    }
}

