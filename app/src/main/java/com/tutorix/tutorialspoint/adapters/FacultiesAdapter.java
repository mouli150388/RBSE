package com.tutorix.tutorialspoint.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.FacultiesActivity;
import com.tutorix.tutorialspoint.utility.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class FacultiesAdapter extends PagerAdapter {

    List<Data> data;
    FacultiesActivity activity;
    public FacultiesAdapter(FacultiesActivity activity)
    {
        data=new ArrayList<>();
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View view= LayoutInflater.from(container.getContext()).inflate(R.layout.faculty_items,null);
        ImageView img=view.findViewById(R.id.img);
        TextView txt_original=view.findViewById(R.id.txt_original);
        ProgressBar progressBar=view.findViewById(R.id.progressBar);
        final Data current = data.get(position);
        txt_original.setVisibility(View.INVISIBLE);
        txt_original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img.setScaleX(1.0f);
                img.setScaleY(1.0f);
                txt_original.setVisibility(View.INVISIBLE);
                /*if(current.video_url==null||current.video_url.trim().length()<4)
                {
                    return;
                }
                if (AppStatus.getInstance(activity).isOnline()) {
                    Intent i = new Intent(activity, ExpertsProfileActivity.class);
                    i.putExtra(Constants.facultyName, current.full_name);
                    i.putExtra(Constants.introduction, current.introduction);
                    i.putExtra(Constants.photoUrl, current.photo_url);
                    i.putExtra(Constants.videoUrl, current.video_url);
                    i.putExtra(Constants.expertise, current.expertise);
                    activity.startActivity(i);
                } else {
                    CommonUtils.showToast(activity,activity.getString(R.string.no_internet));
                    // Toasty.info(context, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }*/
            }
        });

        // String videourl = current.video_thumb_url;

      Picasso.with(activity).load(current.photo_url).into(img,new Callback() {
          @Override
          public void onSuccess() {
              progressBar.setVisibility(View.GONE);
          }

          @Override
          public void onError() {
              // TODO Auto-generated method stub
              progressBar.setVisibility(View.GONE);
          }
      });
        ScaleGestureDetector  mScaleGestureDetector = new ScaleGestureDetector(activity, new ScaleListener(img,txt_original));
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        container.addView(view);
        return view;
    }
    private float mScaleFactor = 1.0f;
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        ImageView ivPreview;
        TextView txt_original;
        public ScaleListener(ImageView ivPreview,TextView txt_original)
        {
            this.ivPreview=ivPreview;
            this.txt_original=txt_original;
        }
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            ivPreview.setScaleX(mScaleFactor);
            ivPreview.setScaleY(mScaleFactor);
            txt_original.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public void addFaculties(List<Data> list)
    {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();

    }





    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
