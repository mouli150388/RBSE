package com.tutorix.tutorialspoint.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.LecturesActivity;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.models.Chapters;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import java.io.File;
import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.MyViewHolder> {
    private final Activity context;
    private final List<Chapters> data;
    //private Activity mContext;
    private String BaseURL;
    private boolean isGride;
    int height;
    //int colors[]=new int[]{R.color.phy_background,R.color.che_background,R.color.bio_background,R.color.math_background};

    Resources res;
    String lectures="Lectures";
    String duration="Duration";
    public ChaptersAdapter(List<Chapters> data, Activity context, String BaseURL,boolean isGride) {
        this.data = data;
        this.context = context;
        this.BaseURL = BaseURL;
        this.isGride = isGride;
         height = (int) convertDpToPixel(90,context);
        res=context.getResources();
        lectures=res.getString(R.string.lectures);
        duration=res.getString(R.string.duration);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=null;
        if(!isGride)
         itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapters_main, parent, false);
        else
         itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapters_main_gride_item, parent, false);

        return new MyViewHolder(itemView);
    }
     String imageUrl;

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Chapters chapters = data.get(holder.getAdapterPosition());
        String section_status = chapters.section_status;
        if(chapters.subjectid.equals("1"))
        {
            holder.lnr_card.setBackgroundResource(R.drawable.ic_phy_card_background);
            holder.textView.setTextColor(res.getColor(R.color.phy_background));
        }else if(chapters.subjectid.equals("2"))
        {
            holder.lnr_card.setBackgroundResource(R.drawable.ic_chemistry_card_background);
            holder.textView.setTextColor(res.getColor(R.color.che_background));
        }else if(chapters.subjectid.equals("3"))
        {
            holder.lnr_card.setBackgroundResource(R.drawable.ic_bio_card_background);
            holder.textView.setTextColor(res.getColor(R.color.bio_background));
        }else if(chapters.subjectid.equals("4"))
        {
            holder.lnr_card.setBackgroundResource(R.drawable.ic_math_card_background);
            holder.textView.setTextColor(res.getColor(R.color.math_background));
        }
        if (section_status.equals("P")) {
            holder.textView.setText(chapters.txt.trim());

             imageUrl = BaseURL + chapters.subjectid + "/" + chapters.section_id + "/" + chapters.section_image;

            //Log.v("Image Path","Image Path "+imageUrl);
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

               /* if(height>200)
                Picasso.with(context).load(imageUrl).resize(height,height).placeholder(R.drawable.circle_default_load).into(holder.logo);
                else*/ Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).override(height,height).placeholder(R.drawable.circle_default_load).into(holder.logo);

                //Log.v("Image Path","Image Path "+imageUrl);
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
                Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.circle_default_load).into(holder.logo);



            }


            String description = chapters.lecture_count + " "+lectures+"  |  " +

                    chapters.total_duration + "  "+duration;


            /*String description = chapters.lecture_count + " lectures  |  " +

                    ((chapters.calculated_time.isEmpty())?chapters.total_duration:chapters.calculated_time)+ "  Duration";


*/




            if(isGride) {
                holder.description.setText(chapters.lecture_count );
                holder.chapter_quize.setText("");
            }else
            {
                holder.description.setText(description);
                holder.chapter_quize.setText("");
            }



            //holder.textView.setSelected(true);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.performClick();
                }
            });
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.performClick();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    AppController.getInstance().startItemAnimation(view);
                    Intent i = new Intent(context, LecturesActivity.class);
                    i.putExtra(Constants.chapterIntent, chapters);
                    i.putExtra(Constants.subjectName, imageUrl);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivityForResult(i,200);
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                }
            });
        } else {
            holder.textView.setText(R.string.locked);
            holder.description.setText(R.string.pls_subscribe);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppStatus.getInstance(context).isOnline()) {
                        Intent i = new Intent(context, SubscribePrePage.class);
                        i.putExtra("flag", "P");
                        //i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                      //  context.finish();
                        context.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    } else {
                        CommonUtils.showToast(context, context.getString(R.string.no_internet));
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView textView;
         ImageView logo;
         TextView description;
         TextView chapter_quize;
         LinearLayout lnr_card;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.chapter_name);
            logo = view.findViewById(R.id.logo);
            description = view.findViewById(R.id.chapter_desc);
            chapter_quize = view.findViewById(R.id.chapter_quize);
            lnr_card = view.findViewById(R.id.lnr_card);
        }
    }
    public static int getResourceId(Context context,String pVariableName, String pResourcename)
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
       if(dencity==DisplayMetrics.DENSITY_DEFAULT)
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
}
