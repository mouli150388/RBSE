package com.tutorix.tutorialspoint.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.anaylysis.MockTestGraphActivity;
import com.tutorix.tutorialspoint.models.Chapters;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionPerFormanceAdapter extends RecyclerView.Adapter<SectionPerFormanceAdapter.SectionViewHolder> {
    List<Chapters> data;
    String imageUrl;
    String BaseURL;
    String subjectId;
    Activity activity;
    int height;
    Drawable progress_drawable;
    int color_theme;
    Resources res;
    String selected_user_id;
    String selected_class_id;
    public SectionPerFormanceAdapter(String baseurl,Activity activit,String subjectId,String selected_user_id,String selected_class_id)
    {
        data=new ArrayList();
        BaseURL=baseurl;
        activity=activit;
        this.subjectId=subjectId;
        this.selected_user_id=selected_user_id;
        this.selected_class_id=selected_class_id;
        if(activity==null)
            return;
        res=activity.getResources();
        height = (int) convertDpToPixel(90,this.activity);


    }
    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.section_performance_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {

        Chapters chapters=data.get(position);
        holder.chapterName.setText(data.get(position).txt);
        imageUrl = BaseURL + chapters.subjectid + "/" + chapters.section_id + "/" + chapters.section_image;
        if (subjectId.equalsIgnoreCase("1")) {
            color_theme=R.color.phy_background;
            //holder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.line_progress_one));
        }else   if (subjectId.equalsIgnoreCase("2")) {
            color_theme=R.color.che_background;
            //holder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.line_progress_two));
        }else   if (subjectId.equalsIgnoreCase("3")) {
            color_theme=R.color.bio_background;
           // holder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.line_progress_three));
        }else   if (subjectId.equalsIgnoreCase("4")) {
            color_theme=R.color.math_background;
            //holder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.line_progress_four));
        }

        holder.chapterName.setTextColor(res.getColor(color_theme));
       // holder.progressbar.setMax(100);
        if(chapters.avg!=null&&!chapters.avg.isEmpty()) {
            //holder.progressbar.setProgress(Integer.parseInt(chapters.avg));
            if(chapters.attrmpted_count.equalsIgnoreCase("1"))
            holder.txtPercentage.setText(activity.getString(R.string.avaerage_marks)+" "+chapters.avg+"% "+activity.getString(R.string.in)+" "+chapters.attrmpted_count+" "+activity.getString(R.string.attempts));
            else
            holder.txtPercentage.setText(activity.getString(R.string.avaerage_marks)+" "+chapters.avg+"% "+activity.getString(R.string.in)+" "+chapters.attrmpted_count+" "+activity.getString(R.string.attempts));
        }
        else {
            holder.txtPercentage.setText(activity.getString(R.string.not_attempted));
           // holder.progressbar.setProgress(0);
        }
        //holder.progressbar.setProgress(40);
        if (imageUrl.contains("http")) {
            if(chapters.subjectid.equals("1"))
                imageUrl = BaseURL+"images/"+"physics/"+chapters.section_id+".png" ;
            else if(chapters.subjectid.equals("2"))
                imageUrl = BaseURL+"images/"+"chemistry/"+chapters.section_id+".png" ;
            else if(chapters.subjectid.equals("3"))
                imageUrl = BaseURL+"images/"+"biology/"+chapters.section_id+".png" ;
            else if(chapters.subjectid.equals("4"))
                imageUrl = BaseURL+"images/"+"mathematics/"+chapters.section_id+".png" ;
            else
                imageUrl = BaseURL+"images/"+chapters.section_image ;


            Picasso.with(activity).load(imageUrl).resize(height,height).placeholder(R.drawable.circle_default_load).into(holder.logo);

        } else {
            if(chapters.subjectid.equals("1"))
                imageUrl = BaseURL+"images/"+"physics/"+chapters.section_id+".png" ;
            else if(chapters.subjectid.equals("2"))
                imageUrl = BaseURL+"images/"+"chemistry/"+chapters.section_id+".png" ;
            else if(chapters.subjectid.equals("3"))
                imageUrl = BaseURL+"images/"+"biology/"+chapters.section_id+".png" ;
            else if(chapters.subjectid.equals("4"))
                imageUrl = BaseURL+"images/"+"mathematics/"+chapters.section_id+".png" ;
            else
                imageUrl = BaseURL+"images/"+chapters.section_image ;

            Uri uri = Uri.fromFile(new File(imageUrl));
            Picasso.with(activity).load(uri).resize(height,height).placeholder(R.drawable.circle_default_load).into(holder.logo);



        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                AppController.getInstance().startItemAnimation(v);
                if(chapters.avg==null||chapters.avg.isEmpty()) {

                    CommonUtils.showToast(activity,activity.getString(R.string.no_tests_attempted_yet));
                    return;
                }
                Intent in=new Intent(activity, MockTestGraphActivity.class);
                in.putExtra("section_id",chapters.section_id);
                in.putExtra("subject_id",chapters.subjectid);
                in.putExtra("section_name",chapters.txt);
                in.putExtra("selected_user_id",selected_user_id);
                in.putExtra("selected_class_id",selected_class_id);
                activity.startActivity(in);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        holder.lnrCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                AppController.getInstance().startItemAnimation(v);
                if(chapters.avg==null||chapters.avg.isEmpty()) {

                    CommonUtils.showToast(activity,activity.getString(R.string.no_tests_attempted_yet));
                    return;
                }
                Intent in=new Intent(activity, MockTestGraphActivity.class);
                in.putExtra("section_id",chapters.section_id);
                in.putExtra("subject_id",chapters.subjectid);
                in.putExtra("section_name",chapters.txt);
                in.putExtra("selected_user_id",selected_user_id);
                in.putExtra("selected_class_id",selected_class_id);
                activity.startActivity(in);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        holder.chapterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                AppController.getInstance().startItemAnimation(v);
                if(chapters.avg==null||chapters.avg.isEmpty()) {

                    CommonUtils.showToast(activity,activity.getString(R.string.no_tests_attempted_yet));
                    return;
                }
                Intent in=new Intent(activity, MockTestGraphActivity.class);
                in.putExtra("section_id",chapters.section_id);
                in.putExtra("subject_id",chapters.subjectid);
                in.putExtra("section_name",chapters.txt);
                in.putExtra("selected_user_id",selected_user_id);
                in.putExtra("selected_class_id",selected_class_id);
                activity.startActivity(in);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });holder.txtPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                AppController.getInstance().startItemAnimation(v);
                if(chapters.avg==null||chapters.avg.isEmpty()) {

                    CommonUtils.showToast(activity,activity.getString(R.string.no_tests_attempted_yet));
                    return;
                }
                Intent in=new Intent(activity, MockTestGraphActivity.class);
                in.putExtra("section_id",chapters.section_id);
                in.putExtra("subject_id",chapters.subjectid);
                in.putExtra("section_name",chapters.txt);
                in.putExtra("selected_user_id",selected_user_id);
                in.putExtra("selected_class_id",selected_class_id);
                activity.startActivity(in);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<Chapters> datas) {
        data.clear();;
        data.addAll(datas);
        notifyDataSetChanged();
    }


    class SectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.chapter_name)
        TextView chapterName;
       /* @BindView(R.id.progressbar)
        ProgressBar progressbar;*/
        @BindView(R.id.txt_percentage)
        TextView txtPercentage;
        @BindView(R.id.lnr_card)
        LinearLayout lnrCard;
        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }



    }
    public static float convertDpToPixel(float dp, Activity context){

        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
