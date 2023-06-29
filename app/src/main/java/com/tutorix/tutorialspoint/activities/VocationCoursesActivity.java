package com.tutorix.tutorialspoint.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.VocationalCoursesAdapter;
import com.tutorix.tutorialspoint.utility.Data;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VocationCoursesActivity extends AppCompatActivity {

    VocationCoursesActivity _this;
    String class_id;
    String loginType;
    List<Data> data;
    @BindView(R.id.rel_toolbar)
    RelativeLayout relToolbar;
    @BindView(R.id.viewpager_analysis)
    ViewPager viewpagerAnalysis;

    @BindView(R.id.radio_grp)
    RadioGroup radio_grp;
    @BindView(R.id.img_back)
    View img_back;

    VocationalCoursesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);
        ButterKnife.bind(this);
        _this = this;
        initUI();
    }

    private void initUI() {
        String[] userInfo = SessionManager.getUserInfo(_this);
        final String access_token = userInfo[1];
        final String user_id = userInfo[0];
        class_id = userInfo[4];
        loginType = userInfo[2];
        data = new ArrayList<>();
       /* Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setTitle("Subject Matter Experts");*/

        adapter=new VocationalCoursesAdapter(_this);
        viewpagerAnalysis.setAdapter(adapter);
        int height=10;
        height= (int) convertDpToPixel(10,getApplicationContext());
        RadioGroup.LayoutParams param=new RadioGroup.LayoutParams(height,height);
        param.leftMargin=2;
        for(int k=0;k<adapter.getCount();k++)
        {
            RadioButton btn=new RadioButton(_this);
            btn.setBackgroundResource(R.drawable.welcome_selector);
            btn.setButtonDrawable(null);
            btn.setLayoutParams(param);
            radio_grp.addView(btn);
        }
        if(radio_grp.getChildCount()>0)
            ((RadioButton) radio_grp.getChildAt(0)).setChecked(true);
        viewpagerAnalysis.setPageTransformer(true,new ParallaxPageTransformer());


        viewpagerAnalysis.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                AppController.getInstance().playAudio(R.raw.qz_next);
                if(radio_grp.getChildCount()>i)
                    ((RadioButton) radio_grp.getChildAt(i)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.back_sound);
                onBackPressed();
            }
        });
    }




    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private static final float MIN_SCALE = 0.65f;
    private static final float MIN_ALPHA = 0.3f;
    class ParallaxPageTransformer implements  ViewPager.PageTransformer

    {

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            if (position <-1){  // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <=1){ // [-1,1]

                page.setScaleX(Math.max(MIN_SCALE,1-Math.abs(position)));
                page.setScaleY(Math.max(MIN_SCALE,1-Math.abs(position)));
                page.setAlpha(Math.max(MIN_ALPHA,1-Math.abs(position)));

            }
            else {  // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


           /* page.setTranslationX(-position*page.getWidth());



            if (position<-1){    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position<=0){    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(90*Math.abs(position));

            }
            else if (position <=1){    // (0,1]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(-90*Math.abs(position));

            }else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }*/

        }
    }


    public static float convertDpToPixel(float dp, Context context){

        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
