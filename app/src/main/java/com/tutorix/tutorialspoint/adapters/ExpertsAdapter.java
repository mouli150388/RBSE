package com.tutorix.tutorialspoint.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.ExpertsProfileActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.Data;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


@SuppressWarnings("ALL")
public class ExpertsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Data> data = Collections.emptyList();
    MyHolder myHolder;

    private Context context;
    private ProgressDialog pDialog;
    private LayoutInflater inflater;
    int height;
    public ExpertsAdapter(Context context, List<Data> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        setHasStableIds(true);
        height = (int) convertDpToPixel(150,context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_menu_new, parent, false);
        return new MyHolder(view);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        myHolder = (MyHolder) holder;
        final Data current = data.get(position);
        /*if(position%2==0) {
            myHolder.lnr_left.setVisibility(View.VISIBLE);
            myHolder.lnr_right.setVisibility(View.GONE);
        }else
        {
            myHolder.lnr_left.setVisibility(View.GONE);
            myHolder.lnr_right.setVisibility(View.VISIBLE);
        }*/
        myHolder.txt_name_left.setText(current.full_name);
        myHolder.txt_sbjct_left.setText(current.expertise);
        myHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current.video_url==null||current.video_url.trim().length()<4)
                {
                    return;
                }
                if (AppStatus.getInstance(context).isOnline()) {
                    Intent i = new Intent(context, ExpertsProfileActivity.class);
                    i.putExtra(Constants.facultyName, current.full_name);
                    i.putExtra(Constants.introduction, current.introduction);
                    i.putExtra(Constants.photoUrl, current.photo_url);
                    i.putExtra(Constants.videoUrl, current.video_url);
                    i.putExtra(Constants.expertise, current.expertise);
                    context.startActivity(i);
                } else {
                    CommonUtils.showToast(context,context.getString(R.string.no_internet));
                   // Toasty.info(context, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
       // String videourl = current.video_thumb_url;
        Picasso.with(context).load(current.photo_url).resize(height,height).placeholder(R.drawable.circle_default_load).into(myHolder.img_left, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView txt_name_left;
        TextView txt_sbjct_left;
        ImageView img;
        ImageView img_left;
        LinearLayout layout;
        LinearLayout lnr_left;
        LinearLayout lnr_right;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            lnr_left = (LinearLayout) itemView.findViewById(R.id.lnr_left);
            lnr_right = (LinearLayout) itemView.findViewById(R.id.lnr_right);
            name = (TextView) itemView.findViewById(R.id.txtview);
            txt_name_left = (TextView) itemView.findViewById(R.id.txt_name_left);
            txt_sbjct_left = (TextView) itemView.findViewById(R.id.txt_sbjct_left);
            img = (ImageView) itemView.findViewById(R.id.imageview);
            img_left = (ImageView) itemView.findViewById(R.id.img_left);

        }
    }
    public  float convertDpToPixel(float dp,Context ctx){

        return dp * ((float) ctx.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param   to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public  float convertPixelsToDp(float px,Context ctx){
        return px / ((float) ctx.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}