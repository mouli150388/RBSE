package com.tutorix.tutorialspoint.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.WelcomepageFragmentAdapter;
import com.tutorix.tutorialspoint.fragments.WelcomepageFragment;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;

import java.util.Timer;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager view_pager;
    WelcomepageFragmentAdapter welcomePageAdapter;
    int[] welcomePages;


    int previousPosition;
    boolean reverse = false;
    TextView txt_start;
    TextView btn_skip;

    Timer timer;
    boolean isCanceled;
    LinearLayout lnr_bottom;

    RadioGroup radio_grp;
    RadioButton radio_one;
    RadioButton radio_two;
    RadioButton radio_three;
//    RadioButton radio_four;
//    RadioButton radio_five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        initUI();
    }

    RadioButton button = null;
    ObjectAnimator animator;

    private void initUI() {
        welcomePages = new int[]{R.layout.welcome_slider_page, R.layout.welcome_slide_page_two, R.layout.welcome_slide_page_three};
        welcomePageAdapter = new WelcomepageFragmentAdapter(getSupportFragmentManager());

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setAdapter(welcomePageAdapter);
        view_pager.setOffscreenPageLimit(1);
        view_pager.setPageTransformer(true,new ParallaxPageTransformer());
        view_pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        lnr_bottom=(LinearLayout)findViewById(R.id.lnr_bottom);

        radio_grp=(RadioGroup) findViewById(R.id.radio_grp);
        radio_one=(RadioButton) findViewById(R.id.radio_one);
        radio_two=(RadioButton) findViewById(R.id.radio_two);
        radio_three=(RadioButton) findViewById(R.id.radio_three);
//        radio_four=(RadioButton) findViewById(R.id.radio_four);
//        radio_five=(RadioButton) findViewById(R.id.radio_five);
        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radio_one:
                        view_pager.setCurrentItem(0);
                        radio_grp.setVisibility(View.VISIBLE);
                        radio_one.setChecked(true);
                        break;
                    case R.id.radio_two:
                        view_pager.setCurrentItem(1);
                        radio_grp.setVisibility(View.VISIBLE);
                        radio_two.setChecked(true);
                        break;
                    case R.id.radio_three:
                        view_pager.setCurrentItem(2);
                        radio_grp.setVisibility(View.VISIBLE);
                        radio_three.setChecked(true);
                        break;
//                    case R.id.radio_four:
//                        view_pager.setCurrentItem(3);
//                        radio_grp.setVisibility(View.VISIBLE);
//                        radio_four.setChecked(true);
//                        break;
//                    case R.id.radio_five:
//                        view_pager.setCurrentItem(4);
//                        radio_grp.setVisibility(View.GONE);
//                        radio_five.setChecked(true);
//                        break;
                }
            }
        });
        previousPosition = 0;


        /*In welcome screen 1: For 'Next'(footer) background color should be blue(#3F95F1).
        In welcome screen 2: For 'Next'(footer) background color should be blue(#FEFEFE).
        In welcome screen 3: For 'Get Started'(footer) background color should be blue(#FEFEFE).
*/
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(final int position) {


                txt_start.setVisibility(View.GONE);
                btn_skip.setVisibility(View.VISIBLE);
                switch (position)
                {
                    case 0:
                       // txt_start.setBackgroundColor(Color.parseColor("#3F95F1"));
                       // txt_start.setTextColor(getResources().getColor(R.color.white));
                        radio_grp.setVisibility(View.VISIBLE);
                        radio_one.setChecked(true);
                        break;
                    case 1:
                       // txt_start.setBackgroundColor(Color.parseColor("#FEFEFE"));
                       // txt_start.setTextColor(getResources().getColor(R.color.app_color));
                        radio_grp.setVisibility(View.VISIBLE);
                        radio_two.setChecked(true);
                        break;
                    case 2:
                        //txt_start.setBackgroundColor(Color.parseColor("#FEFEFE"));
                       // txt_start.setTextColor(getResources().getColor(R.color.app_color));
                        //txt_start.setText("Get Started");
                        radio_three.setChecked(true);
                        txt_start.setText(getString(R.string.get_started));
                        txt_start.setVisibility(View.VISIBLE);
                        radio_grp.setVisibility(View.GONE);
                        btn_skip.setVisibility(View.GONE);
                        break;
//                    case 3:
//                      //  txt_start.setBackgroundColor(Color.parseColor("#FEFEFE"));
//                       // txt_start.setTextColor(getResources().getColor(R.color.app_color));
//                        //txt_start.setText("Get Started");
//                        radio_grp.setVisibility(View.VISIBLE);
//                        radio_four.setChecked(true);
//                        txt_start.setText(getString(R.string.get_started));
//                        txt_start.setVisibility(View.VISIBLE);
//                        radio_grp.setVisibility(View.GONE);
//                        btn_skip.setVisibility(View.GONE);
//                        break;
//                    case 4:
//                       // txt_start.setBackgroundColor(Color.parseColor("#FEFEFE"));
//                       // txt_start.setTextColor(getResources().getColor(R.color.app_color));
//                       // cardview.setCardBackgroundColor(getResources().getColor(R.color.app_color));
//                        txt_start.setText(getString(R.string.get_started));
//                        txt_start.setVisibility(View.VISIBLE);
//                        radio_grp.setVisibility(View.GONE);
//                        radio_five.setChecked(true);
//                        btn_skip.setVisibility(View.GONE);
//                        break;
                }

                if (previousPosition > position)
                    reverse = true;
                else reverse = false;

                previousPosition = position;

                if(position==0)
                    ((WelcomepageFragment)welcomePageAdapter.getItem(position)).showAnimation();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        txt_start = (TextView) findViewById(R.id.txt_start);
        btn_skip = (TextView) findViewById(R.id.btn_skip);

        txt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(view_pager.getCurrentItem()>1)
                    callLogin();
                else
                 view_pager.setCurrentItem(view_pager.getCurrentItem()+1);

            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    callLogin();


            }
        });

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    view_pager.setCurrentItem(1);
            }
        },2000);
*/

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(position+1<=3)
                    view_pager.setCurrentItem(position+1);

                if(position>=2)
                    txt_start.setText("NEXT");
                else txt_start.setText("SKIP");
            }
        },2000);*/
        view_pager.setCurrentItem(0);
       /* timer = new Timer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (timer != null)
                        timer.scheduleAtFixedRate(new TimerTask() {

                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (view_pager.getCurrentItem() + 1 <= 3)
                                            view_pager.setCurrentItem(view_pager.getCurrentItem() + 1);


                                    }
                                });
                            }
                        }, 0, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);*/


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null)
                timer.cancel();
            isCanceled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callLogin() {
        new SessionManager().viewedWelcomPages(getApplicationContext());
        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        if (new SessionManager().isLoggedIn(WelcomeActivity.this))
            startActivity(new Intent(getApplicationContext(), HomeTabActivity.class));
        else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        }

        finish();
    }
    private static final float MIN_SCALE = 0.65f;
    private static final float MIN_ALPHA = 0.3f;
    class ParallaxPageTransformer implements  ViewPager.PageTransformer

    {

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();

            /*if (position <-1){  // [-Infinity,-1)
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

            }*/

            page.setTranslationX(-position*page.getWidth());



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

            }

        }
        }

}
