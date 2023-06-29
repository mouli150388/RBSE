package com.tutorix.tutorialspoint.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.VocationCoursesActivity;
import com.tutorix.tutorialspoint.models.VocationCourses;
import com.tutorix.tutorialspoint.toc.TOCActivity;

import java.util.ArrayList;
import java.util.List;

public class VocationalCoursesAdapter extends PagerAdapter {

    List<VocationCourses> data;
    VocationCourses courses;
    VocationCoursesActivity activity;
    int touch = -1;
    int touchPrevious = -1;

    public VocationalCoursesAdapter(VocationCoursesActivity activity) {
        data = new ArrayList<>();
        this.activity = activity;

        data.add(new VocationCourses("1", "AI-ML", R.drawable.ai_ml, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/prerequisites-to-machine-learning-a-beginners-guide.json"));
        data.add(new VocationCourses("2", "C Programming", R.drawable.c_programming, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/programming-techniques-in-c.json"));
        data.add(new VocationCourses("3", "Computer Fundamentals", R.drawable.computer_fundamentals, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/computer-fundamentals-online-training.json"));
        data.add(new VocationCourses("4", "Computer Graphics", R.drawable.computer_graphics, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/computer-graphics-course.json"));
        data.add(new VocationCourses("5", "English Grammar", R.drawable.english_grammar, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/english_grammar.json"));
        data.add(new VocationCourses("6", "Python Programming", R.drawable.python_programming, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/python-programming-for-beginners-in-hindi.json"));
        data.add(new VocationCourses("7", "Robotics", R.drawable.robotics, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/arduino-based-robotics-design-on-tinkercad-platform.json"));
        data.add(new VocationCourses("8", "Soft Skills", R.drawable.soft_skills, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/soft-skills-online-training.json"));
        data.add(new VocationCourses("9", "MS Word", R.drawable.ms_word, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/ms-word-online-training.json"));
        data.add(new VocationCourses("10", "MS Excel", R.drawable.ms_excel, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/ms-excel-online-training.json"));
        data.add(new VocationCourses("11", "MS Power Point", R.drawable.ms_power_point, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/powerpoint-online-training.json"));
        data.add(new VocationCourses("12", "English Grammar In Hindi", R.drawable.eng_gram_hi, "https://d2vgb5tug4mj1f.cloudfront.net/vocational-courses/english-grammar-for-beginners-in-hindi.json"));

    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.faculty_items, null);
        ImageView img = view.findViewById(R.id.img);
        TextView txt_original = view.findViewById(R.id.txt_original);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        final VocationCourses current = data.get(position);
        txt_original.setVisibility(View.INVISIBLE);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setScaleX(1.0f);
                img.setScaleY(1.0f);
                txt_original.setVisibility(View.INVISIBLE);
                Intent in = new Intent(activity, TOCActivity.class);
                in.putExtra("toc_json", current.toc);
                activity.startActivity(in);

            }
        });

        // String videourl = current.video_thumb_url;

        Picasso.with(activity).load(current.img).into(img, new Callback() {
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
        ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(activity, new ScaleListener(img, txt_original));

        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchPrevious = 1;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {


                        Intent in = new Intent(activity, TOCActivity.class);
                        in.putExtra("toc_json", current.toc);
                        activity.startActivity(in);



                } else {
                    touchPrevious = -1;
                }
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

        public ScaleListener(ImageView ivPreview, TextView txt_original) {
            this.ivPreview = ivPreview;
            this.txt_original = txt_original;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            ivPreview.setScaleX(mScaleFactor);
            ivPreview.setScaleY(mScaleFactor);
            txt_original.setVisibility(View.VISIBLE);
            return true;
        }
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
