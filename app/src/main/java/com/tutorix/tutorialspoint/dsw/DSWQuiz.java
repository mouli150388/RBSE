package com.tutorix.tutorialspoint.dsw;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.OnSwipeTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;

public class DSWQuiz extends AppCompatActivity implements OnChartValueSelectedListener

{

    String URL="https://cdn.tutorix.com/tutorix/class6/2/3/15/quiz.json";

        DSWQuiz _this;
        private CountdownView mCvCountdownView;
        private  int quesIndex = 0;
        private final ArrayList<CheckedTextView> mButtonList = new ArrayList<>();
        private HorizontalScrollView scroll;
        private long mLastClickTime = 0;
        private WebView question;
        private String start;
        private WebView a0;
        private WebView a1;
        private WebView a2;
        private WebView a3;

        //private LinearLayout lnr_top;
        private FrameLayout lnr_4;
        private FrameLayout lnr_3;
        private FrameLayout lnr_2;
        private FrameLayout lnr_1;

        private RadioButton radio_1;
        private RadioButton radio_2;
        private RadioButton radio_3;
        private RadioButton radio_4;


        private String userid;

        private String classid;

        private Button Next;
        private Button Prev;
        Button finish ;
        private int[] selected = null;
        private int[] correctAns = null;
        private ScrollView scrollview;
        private FrameLayout lnr_swipe;
        private View lnr_touch_right;
        private View lnr_touch_left;
        private View quiz;
        private Map<String, String> map;
        private int ColorButton;
        private int wrongAnswers;
        private String access_token;

        String loginType;

        String currentTime;
        String mathLib;
        String mathLib0="";
        String mathLib1="";
        String mathLib2="";
        String mathLib3="";
        String mathLib4="";

        int color_theme;
        int color_theme_card;
        int color_theme_selected;//f2af98
        String quiz_id="";

        ImageView img_logo_subj;

        JSONArray quizJson;

        int TOTAL_TIME;
        int background_drawable=R.drawable.retake_button;
        int background_top_drawable=R.drawable.ic_chemistry_bg_green;
        int background_top_score_drawable=R.drawable.quiz_score_che;

        RelativeLayout rel_top_main;
        ImageView img_header;
        TextView txt_header;
        private int progressDrawable;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            _this = DSWQuiz.this;
            setContentView(R.layout.activity_dswquiz);
            String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

            access_token = userInfo[1];
            userid = userInfo[0];
            loginType = userInfo[2];
            classid = userInfo[4];
            String assets= "file:///android_asset";
            mathLib= Constants.MathJax_Offline;


            rel_top_main = findViewById(R.id.rel_top_main);
            img_header = findViewById(R.id.img_header);
            txt_header = findViewById(R.id.txt_header);
            lnr_swipe = findViewById(R.id.lnr_swipe);
            lnr_touch_right = findViewById(R.id.lnr_touch_right);
            lnr_touch_left = findViewById(R.id.lnr_touch_left);

            ImageButton backScroll = findViewById(R.id.backscroll);
            ImageButton fullScroll = findViewById(R.id.fullscroll);
            //lnr_top = findViewById(R.id.lnr_top);





            radio_1 = findViewById(R.id.radio_1);
            radio_2 = findViewById(R.id.radio_2);
            radio_3 = findViewById(R.id.radio_3);
            radio_4 = findViewById(R.id.radio_4);

            radio_1.setChecked(false);
            radio_2.setChecked(false);
            radio_3.setChecked(false);
            radio_4.setChecked(false);



                //Glide.with(getApplicationContext()).load(R.drawable.gif_bio).into(img_logo_subj);
                img_header.setImageResource(R.drawable.quiz_header_bio);
                background_top_drawable=R.drawable.ic_bio_bg_green;
                background_drawable=R.drawable.quiz_quit_btn_bio;
                background_top_score_drawable=R.drawable.quiz_score_bio;
                progressDrawable=R.drawable.circular_progress_bio;
                txt_header.setText("DSW");
                radio_1.setBackgroundResource(R.drawable.webviewquiz_select_three);
                radio_2.setBackgroundResource(R.drawable.webviewquiz_select_three);
                radio_3.setBackgroundResource(R.drawable.webviewquiz_select_three);
                radio_4.setBackgroundResource(R.drawable.webviewquiz_select_three);





            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                window.setStatusBarColor(getResources().getColor(R.color.bio_background_status));


            }

            ImageView back = findViewById(R.id.back);
            LinearLayout lnr_back = findViewById(R.id.lnr_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    AppController.getInstance().playAudio(R.raw.back_sound);

                }
            });
            lnr_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    AppController.getInstance().playAudio(R.raw.back_sound);

                }
            });
            updateTheme();
            Next = findViewById(R.id.Next);
            Prev = findViewById(R.id.Prev);
            finish = findViewById(R.id.finish);
            Next.setTextColor(getResources().getColor(color_theme));
            Prev.setTextColor(getResources().getColor(color_theme));
            // finish.setBackgroundResource(background_drawable);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            scrollview = findViewById(R.id.license_agree_scrollView);
            setGesture();
            scrollview.setScrollbarFadingEnabled(false);
            quiz = findViewById(R.id.quiz);
            scroll = findViewById(R.id.scroll);



            Date date = new Date();
            start = dateFormat.format(date);
            backScroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quesIndex > 0) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 250) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //scroll.smoothScrollBy(-50, 0);
                        if (quesIndex == 0) {
                            quesIndex = 0;

                            int score = 0;
                            for (int i = 0; i < correctAns.length; i++) {
                                if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
                                    score++;
                            }
                            wrongAnswers = quizJson.length() - score;



                            finish();
                        }
                        final int k;
                        quesIndex--;
                        k = quesIndex;
                        for (CheckedTextView button : mButtonList) {
                            if (button.getId() == k) {
                                try {
                                    button.getParent().requestChildFocus(button, button);
                                }catch (Exception e)
                                {
                                    scroll.smoothScrollBy(-50, 0);
                                }
                                a0.setBackgroundColor(0);
                                a1.setBackgroundColor(0);
                                a2.setBackgroundColor(0);
                                a3.setBackgroundColor(0);
                                loadFirstQuestion(quesIndex);
                                //scrollview.scrollTo(0, 0);
                                //button.setTextColor(Color.parseColor("#ffcc33"));
                                button.setChecked(true);
                            } else {
                                //button.setTextColor(Color.BLACK);
                                button.setChecked(false);
                            }
                        }
                    }
                }
            });
            fullScroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quesIndex < quizJson.length() - 1) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 250) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //scroll.smoothScrollBy(50, 0);
                        final int k;
                        quesIndex++;
                        k = quesIndex;
                        for (CheckedTextView button : mButtonList) {
                            if (button.getId() == k) {
                                try {
                                    button.getParent().requestChildFocus(button, button);
                                }catch (Exception e)
                                {
                                    scroll.smoothScrollBy(50, 0);
                                }
                                a0.setBackgroundColor(0);
                                a1.setBackgroundColor(0);
                                a2.setBackgroundColor(0);
                                a3.setBackgroundColor(0);
                                loadFirstQuestion(quesIndex);
                                //scrollview.scrollTo(0, 0);
                                //button.setTextColor(Color.parseColor("#ffcc33"));
                                button.setChecked(true);
                            } else {
                                //button.setTextColor(Color.BLACK);
                                button.setChecked(false);
                            }
                        }
                    }
                }
            });
            map = new HashMap<>();

            mCvCountdownView = findViewById(R.id.cv_countdownViewTest1);

            mCvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {

                    SubmitButton(true);
                }
            });
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    SubmitButton(false);
                }
            });


            Prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click_next_previous=0;
                    if (quesIndex == 0) {
                        onBackPressed();
                        return;
                    }
                    final int k;
                    quesIndex--;
                    k = quesIndex;
                    for (CheckedTextView button : mButtonList) {
                        if (button.getId() == k) {
                            try {
                                button.getParent().requestChildFocus(button, button);
                            }catch (Exception e)
                            {
                                scroll.smoothScrollBy(-50, 0);
                            }
                            a0.setBackgroundColor(0);
                            a1.setBackgroundColor(0);
                            a2.setBackgroundColor(0);
                            a3.setBackgroundColor(0);
                            loadFirstQuestion(quesIndex);
                            scrollview.scrollTo(0, 0);
                            //button.setTextColor(Color.parseColor("#ffcc33"));
                            button.setChecked(true);
                        } else {
                            //button.setTextColor(Color.BLACK);
                            button.setChecked(false);
                        }
                    }
                }
            });
            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click_next_previous=1;
                    final int k;
                    quesIndex++;
                    k = quesIndex;
                    for (CheckedTextView button : mButtonList) {
                        if (button.getId() == k) {
                            try {
                                button.getParent().requestChildFocus(button, button);
                            }catch (Exception e)
                            {
                                scroll.smoothScrollBy(50, 0);
                            }

                            a0.setBackgroundColor(0);
                            a1.setBackgroundColor(0);
                            a2.setBackgroundColor(0);
                            a3.setBackgroundColor(0);
                            loadFirstQuestion(quesIndex);
                            scrollview.scrollTo(0, 0);
                            //button.setTextColor(Color.parseColor("#ffcc33"));
                            button.setChecked(true);
                        } else {
                            //button.setTextColor(Color.BLACK);
                            button.setChecked(false);
                        }
                    }

                }
            });
            question = findViewById(R.id.question);

            question.getSettings().setAllowFileAccess(true);
            question.getSettings().setJavaScriptEnabled(true);
            //question.getSettings().setBuiltInZoomControls(true);
            question.getSettings().setDisplayZoomControls(false);
            question.setBackgroundColor(0);

            a0 = findViewById(R.id.a0);
            lnr_4 = findViewById(R.id.lnr_4);
            lnr_3 = findViewById(R.id.lnr_3);
            lnr_2 = findViewById(R.id.lnr_2);
            lnr_1 = findViewById(R.id.lnr_1);




            a0.getSettings().setAllowFileAccess(true);
            a0.getSettings().setJavaScriptEnabled(true);
            a0.getSettings().setBuiltInZoomControls(true);


            a1 = findViewById(R.id.a1);
            a1.getSettings().setAllowFileAccess(true);
            a1.getSettings().setJavaScriptEnabled(true);
            a1.getSettings().setBuiltInZoomControls(true);


            a2 = findViewById(R.id.a2);
            a2.getSettings().setAllowFileAccess(true);
            a2.getSettings().setJavaScriptEnabled(true);
            a2.getSettings().setBuiltInZoomControls(true);



            a3 = findViewById(R.id.a3);
            a3.getSettings().setAllowFileAccess(true);
            a3.setVerticalScrollBarEnabled(true);
            a3.setHorizontalScrollBarEnabled(true);
            a3.getSettings().setJavaScriptEnabled(true);
            a3.getSettings().setBuiltInZoomControls(true);


            a0.setBackgroundColor(0);
            a1.setBackgroundColor(0);
            a2.setBackgroundColor(0);
            a3.setBackgroundColor(0);
            fillWithData();
            //loadFirstQuestion(quesIndex);

        }

        private int click_next_previous=1;
        private void DeselectButtons() {
            for (int i = 0; i < quizJson.length(); i++) {
                if (ColorButton == i)
                    this.findViewById(i).setBackground(ContextCompat.getDrawable(_this, R.drawable.colorbuttonshape));
            }
        }

        String format = "%1$02d";

        Dialog dialog;
        AlertDialog alertDialog;
        int Minute;
        int Sec;

        private void SubmitButton(boolean timeUp) {

            if(map==null|map.isEmpty())
            {
                CommonUtils.showToast(getApplicationContext(),"You didn't answer any question, please answer first");
                return;
            }

            if(dialog!=null&&dialog.isShowing())
                return;

            dialog=new Dialog(_this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_alert);
            TextView txt_msg=dialog.findViewById(R.id.txt_msg);
            txt_msg.setText("");
            TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
            TextView txt_ok=dialog.findViewById(R.id.txt_ok);
            //txt_ok.setBackgroundResource(background_drawable);

            //txt_ok.setBackgroundResource(color_theme);
            if (timeUp) {
                txt_cancel.setVisibility(View.GONE);
                txt_ok.setText("Ok");
                dialog.setCancelable(false);
            }
            else {
                txt_cancel.setVisibility(View.VISIBLE);
                txt_ok.setText(getString(R.string.yes));
            }
            txt_cancel.setText(getString(R.string.no));
            txt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    dialog.dismiss();
                }
            });
            txt_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    dialog.dismiss();



                    mCvCountdownView.stop();
                    Minute = TOTAL_TIME - mCvCountdownView.getMinute();

                    //Log.v("Minute","Minute "+Minute+" "+mCvCountdownView.getMinute()+" "+mCvCountdownView.getSecond());
               /* 14 0
                    14 52*/

                    Sec=60-mCvCountdownView.getSecond();
                    if (Sec == 60)
                        Minute =TOTAL_TIME - mCvCountdownView.getMinute();
                    else
                        Minute =  TOTAL_TIME - (mCvCountdownView.getMinute()+1);


                    String timeTaken = Minute + ":" + Sec;
                    int score = 0;
                    for (int i = 0; i < correctAns.length; i++) {
                        if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
                            score++;
                    }
                    List<Integer> list = new ArrayList<>();

                    for (Integer s : selected) {
                        if (s != -1) {
                            list.add(s);
                        }
                    }


                    wrongAnswers = quizJson.length() - score;

                    startActivity(new Intent(getApplicationContext(),DWSPerformanceActivity.class));
                    finish();

                }
            });




            if (timeUp)
                txt_msg.setText("Timeup!");
            else
                txt_msg.setText("Do you really want to finish the quiz?");

            dialog.show();

        }




        String quesValue;
        private String getDateTime() {


            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(currentTime));
            return CommonUtils.format.format(calendar.getTime());

        }
        public class testClass{
            public testClass() {
            }

            @JavascriptInterface
            public void showAndroidToast(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        which++;

                        if(msg.equalsIgnoreCase("0"))
                            question.setVisibility(View.VISIBLE);
                        else if(msg.equalsIgnoreCase("1"))
                            lnr_1.setVisibility(View.VISIBLE);
                        else if(msg.equalsIgnoreCase("2"))
                            lnr_2.setVisibility(View.VISIBLE);
                        else if(msg.equalsIgnoreCase("3"))
                            lnr_3.setVisibility(View.VISIBLE);
                        else if(msg.equalsIgnoreCase("4"))
                            lnr_4.setVisibility(View.VISIBLE);

                        if(which>4)
                            CustomDialog.closeDialog();
                        //ldg_q.setVisibility(View.GONE);
                    }
                });

            }
        }
        int which=0;
        @SuppressLint("ClickableViewAccessibility")
        private void loadFirstQuestion(final int quesIndex) {
            try {
                which=0;
                JSONObject aQues = quizJson.getJSONObject(quesIndex);
                quesValue =mathLib+aQues.getString("question")+mathLib0;
                //quesValue =aQues.getString("question");
                final String question_id = aQues.getString("question_id");



                question.loadDataWithBaseURL(basePath,  getStyle()/*+"<font color='#F79646'>Q " +(quesIndex + 1 )+")</font> "*/+ quesValue, "text/html", "utf-8", "");

                //question.setBackgroundColor(Color.parseColor("#eeeeee"));
                if (correctAns[quesIndex] == -1) {
                    String correctAnsStr = aQues.getString("option_right");
                    correctAns[quesIndex] = Integer.parseInt(correctAnsStr);
                }

                JSONArray ansList1 = quizJson.getJSONObject(quesIndex).getJSONArray("options");



                lnr_1.setVisibility(View.GONE);
                lnr_2.setVisibility(View.GONE);
                lnr_3.setVisibility(View.GONE);
                lnr_4.setVisibility(View.GONE);

                radio_1.setChecked(false);
                radio_2.setChecked(false);
                radio_3.setChecked(false);
                radio_4.setChecked(false);
                //a0.clearCache(true);
                //a1.clearCache(true);
                //a2.clearCache(true);
                //a3.clearCache(true);

                for (int i = 0; i < ansList1.length(); i++) {
                    JSONObject ansList = ansList1.getJSONObject(i);

                    String aAns;


                    try {

                        if(ansList.has("option_1")) {
                            lnr_1.setVisibility(View.VISIBLE);
                            aAns = mathLib + ansList.getString("option_1") + mathLib1;
                            a0.loadDataWithBaseURL(basePath, getIntialTable("A", aAns), "text/html", "utf-8", "");
                        }
                        if(ansList.has("option_2")) {
                            lnr_2.setVisibility(View.VISIBLE);
                            aAns = mathLib + ansList.getString("option_2") + mathLib2;
                            a1.loadDataWithBaseURL(basePath, getIntialTable("B", aAns), "text/html", "utf-8", "");

                        }
                        if(ansList.has("option_3")) {
                            lnr_3.setVisibility(View.VISIBLE);
                            aAns = mathLib + ansList.getString("option_3") + mathLib3;
                            a2.loadDataWithBaseURL(basePath, getIntialTable("C", aAns), "text/html", "utf-8", "");

                        }
                        if(ansList.has("option_4")) {
                            lnr_4.setVisibility(View.VISIBLE);
                            aAns = mathLib + ansList.getString("option_4") + mathLib4;
                            a3.loadDataWithBaseURL(basePath, getIntialTable("D", aAns), "text/html", "utf-8", "");

                        }

                        // String aAns = ansList.getString("option_1");



                        a0.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN: {

                                        break;
                                    }
                                    case MotionEvent.ACTION_UP: {
                                        ColorButton = quesIndex;
                                        DeselectButtons();
                                        // a0.setBackgroundColor(getResources().getColor(color_theme_selected));
                                        // a1.setBackgroundColor(0);
                                        // a2.setBackgroundColor(0);
                                        // a3.setBackgroundColor(0);
                                        a0.setBackgroundColor(0);
                                        a1.setBackgroundColor(0);
                                        a2.setBackgroundColor(0);
                                        a3.setBackgroundColor(0);

                                        radio_2.setChecked(false);
                                        radio_3.setChecked(false);
                                        radio_4.setChecked(false);
                                        radio_1.setChecked(true);
                                        selected[quesIndex] = 1;
                                        storeQuestionAnswer(question_id, "1");
                                        AppController.getInstance().playQuizAudio(R.raw.quiz_btn);
                                        break;
                                    }
                                }
                                return true;

                            }
                        });


                        a1.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN: {

                                        break;
                                    }
                                    case MotionEvent.ACTION_UP: {
                                        ColorButton = quesIndex;
                                        DeselectButtons();
                                        //a1.setBackgroundColor(getResources().getColor(color_theme_selected));
                                        // a0.setBackgroundColor(0);
                                        // a2.setBackgroundColor(0);
                                        // a3.setBackgroundColor(0);
                                        selected[quesIndex] = 2;
                                        a0.setBackgroundColor(0);
                                        a1.setBackgroundColor(0);
                                        a2.setBackgroundColor(0);
                                        a3.setBackgroundColor(0);

                                        radio_1.setChecked(false);
                                        radio_3.setChecked(false);
                                        radio_4.setChecked(false);
                                        radio_2.setChecked(true);
                                        storeQuestionAnswer(question_id, "2");
                                        AppController.getInstance().playQuizAudio(R.raw.quiz_btn);
                                        break;
                                    }
                                }
                                return true;

                            }
                        });
                        a2.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN: {

                                        break;
                                    }
                                    case MotionEvent.ACTION_UP: {
                                        ColorButton = quesIndex;
                                        DeselectButtons();
                                        // a2.setBackgroundColor(getResources().getColor(color_theme_selected));
                                        // a1.setBackgroundColor(0);
                                        // a0.setBackgroundColor(0);
                                        // a3.setBackgroundColor(0);
                                        selected[quesIndex] = 3;
                                        a0.setBackgroundColor(0);
                                        a1.setBackgroundColor(0);
                                        a2.setBackgroundColor(0);
                                        a3.setBackgroundColor(0);

                                        radio_2.setChecked(false);
                                        radio_1.setChecked(false);
                                        radio_4.setChecked(false);
                                        radio_3.setChecked(true);
                                        storeQuestionAnswer(question_id, "3");
                                        AppController.getInstance().playQuizAudio(R.raw.quiz_btn);
                                        break;
                                    }
                                }
                                return true;
                            }
                        });
                        a3.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN: {

                                        break;
                                    }
                                    case MotionEvent.ACTION_UP: {
                                        ColorButton = quesIndex;
                                        DeselectButtons();
                                        //a3.setBackgroundColor(getResources().getColor(color_theme_selected));
                                        //a1.setBackgroundColor(0);
                                        //a2.setBackgroundColor(0);
                                        // a0.setBackgroundColor(0);
                                        selected[quesIndex] = 4;
                                        a0.setBackgroundColor(0);
                                        a1.setBackgroundColor(0);
                                        a2.setBackgroundColor(0);
                                        a3.setBackgroundColor(0);

                                        radio_2.setChecked(false);
                                        radio_3.setChecked(false);
                                        radio_1.setChecked(false);
                                        radio_4.setChecked(true);
                                        storeQuestionAnswer(question_id, "4");
                                        AppController.getInstance().playQuizAudio(R.raw.quiz_btn);
                                        break;
                                    }
                                }
                                return true;

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (quesIndex == quizJson.length() - 1) {
                    Next.setEnabled(false);
                    Next.setTextColor(Color.parseColor("#D2D2D2"));
                    //Next.setBackgroundResource(R.drawable.button_un_select);

                }

                if (quesIndex == 0) {
                    //Prev.setEnabled(false);

                    final int k;
                    k = quesIndex;
                    for (CheckedTextView button : mButtonList) {
                        if (button.getId() == k) {
                            //Prev.setBackgroundColor(getResources().getColor(R.color.notification_icon_bg_color));
                            //Prev.setBackgroundResource(R.drawable.button_un_select);
                            //button.setTextColor(Color.parseColor("#ffcc33"));
                            button.setChecked(true);
                        } else {
                            //button.setTextColor(Color.BLACK);
                            button.setChecked(false);
                        }
                    }


                }


                if (quesIndex > 0) {
                    Prev.setEnabled(true);
                    //Prev.setBackgroundResource(background_drawable);
                }

                if (quesIndex < (quizJson.length() - 1)) {
                    Next.setEnabled(true);
                    //Next.setBackgroundResource(background_drawable);
                    Next.setTextColor(getResources().getColor(color_theme));
                }


                if (selected[quesIndex] == 1) {
                    // a0.setBackgroundColor(getResources().getColor(color_theme_selected));
                    radio_1.setChecked(true);

                }
                if (selected[quesIndex] == 2) {
                    //a1.setBackgroundColor(getResources().getColor(color_theme_selected));
                    radio_2.setChecked(true);
                }
                if (selected[quesIndex] == 3) {
                    //a2.setBackgroundColor(getResources().getColor(color_theme_selected));
                    radio_3.setChecked(true);
                }
                if (selected[quesIndex] == 4) {
                    //a3.setBackgroundColor(getResources().getColor(color_theme_selected));
                    radio_4.setChecked(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            quiz.clearAnimation();
            if(click_next_previous==0)
                anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_left_to_right);
            else anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fall_down_slow);
            quiz.setAnimation(anim);
            anim.start();
       /* ObjectAnimator anim=ObjectAnimator.ofFloat(lnr_main, "translationX", 100f);
        anim.start();*/
      /* new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {*/
            AppController.getInstance().playQuizAudio(R.raw.qz_next);
          /* }
       },200);*/

        }

        Animation anim;
        private void storeQuestionAnswer(String quesValue, String option) {
            if (map.containsKey(quesValue)) {
                map.put(quesValue, option);

            } else {
                map.put(quesValue, option);
            }
        }

        private void setScoreTitle() {
            this.setTitle("SciQuiz3     " + (quesIndex + 1) + "/" + quizJson.length());
        }

        boolean isback = false;

        @Override
        public void onBackPressed() {
             super.onBackPressed();
        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {
        }

        @Override
        public void onNothingSelected() {
            Log.i("PieChart", "nothing selected");
        }



        String basePath = "";

        private void loadDataBaseURL() {
            String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

            access_token = userInfo[1];
            userid = userInfo[0];
            ;
            loginType = userInfo[2];

            if(loginType.isEmpty())
            {
                basePath = AppConfig.getOnlineURLImage(classid);
            }else if (loginType.equalsIgnoreCase("O")){

                if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
                {
                    basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext())/* + subjectId + "/" + section_id + "/" + lectureId*/;
                }else
                {
                    basePath = AppConfig.getOnlineURLImage(classid);
                }
            }else
            {
                basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext())/* + subjectId + "/" + section_id + "/" + lectureId*/;
            }

            /*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {

                    basePath = AppConfig.getOnlineURLImage(classid);
                //basePath="http://tutorix.com/web/tmp/";
            } else {

                    basePath = "file://" + AppConfig.getSdCardPath(classid)*//* + subjectId + "/" + section_id + "/" + lectureId*//*;
                //basePath=basePath.replace("class61","class6/1");

            }*/
            if (!basePath.trim().endsWith("/"))
                basePath = basePath + "/";


        }

        private void updateTheme()
        {
            switch ("")
            {

                default:
                    color_theme=R.color.che_background;
                    color_theme_card=R.color.che_background_card;
                    color_theme_selected=R.color.che_background_status_trns;
                    break;
            }

       /* Prev.setTextColor(getResources().getColor(color_theme));
        Next.setTextColor(getResources().getColor(color_theme));*/
            //finish.setTextColor(getResources().getColor(color_theme));

        }

        private String getIntialTable(String number,String content)
        {

            //Log.v("Content","Content "+content);
            return "<table>\n" +
                    "     <tr>\n" +
                    "        <td valign=\"center\"><span class ='a' style='border:1px solid #ccc;border-radius:5px; padding:2px 8px 2px 8px;'>" +
                    number+"</td>\n" +
                    "        <td> " +
                    content+"</td>\n" +
                    "     </tr>\n" +
                    "  </table>";


        }

        public  float convertDpToPixel(float dp){

            return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }

        /**
         * This method converts device specific pixels to density independent pixels.
         *
         * @param px A value in px (pixels) unit. Which we need to convert into db
         * param    get resources and device specific display metrics
         * @return A float value to represent dp equivalent to px value
         */
        public  float convertPixelsToDp(float px){
            return px / ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }

        private String getStyle()
        {
            return "<style>"  +
                    " table.quiz-table{margin:5px; padding:0px;border-collapse:collapse;}"+
                    "table.quiz-table th, table.quiz-table td{padding:5px; border:1px solid #ccc;}" +
                    " </style>";
        }


        public class StackedFormatter implements IValueFormatter
        {

            int values;
            /**
             * if true, all stack values of the stacked bar entry are drawn, else only top
             */
            private boolean mDrawWholeStack;

            /**
             * a string that should be appended behind the value
             */
            private String mAppendix;

            private DecimalFormat mFormat;

            /**
             * Constructor.
             *
             * @param drawWholeStack if true, all stack values of the stacked bar entry are drawn, else only top
             * @param appendix       a string that should be appended behind the value
             * @param decimals       the number of decimal digits to use
             */
            public StackedFormatter(boolean drawWholeStack, String appendix, int decimals) {
                this.mDrawWholeStack = drawWholeStack;
                this.mAppendix = appendix;
                values=-1;
                StringBuffer b = new StringBuffer();
                for (int i = 0; i < decimals; i++) {
                    if (i == 0)
                        b.append(".");
                    b.append("0");
                }

                this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
            }

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                if (!mDrawWholeStack && entry instanceof BarEntry) {

                    BarEntry barEntry = (BarEntry) entry;
                    float[] vals = barEntry.getYVals();


                    if (vals != null) {

                        // find out if we are on top of the stack
                        if (vals[vals.length - 1] == value) {

                            // return the "sum" across all stack values
                            return mFormat.format(barEntry.getY()) + mAppendix;
                        } else {
                            return ""; // return empty
                        }
                    }
                }
                //Log.v("vals ","vals "+value);
                values=values+1;
                if(values%2!=0) {
                    return "";
                }
                // return the "proposed" value
                return mFormat.format(value) + mAppendix;
            }
        }


        public static Spanned fromHtml(String source) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(source);
            }
        }

        float c;


        GestureDetectorCompat mGestureDetector;
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();

        private void setGesture()
        {
            lnr_swipe.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //Log.v("OnFlling", "OnFlling Touch" );
                    mGestureDetector.onTouchEvent(event);
                    return true;
                }
            });

            lnr_touch_right.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    if(quesIndex!=quizJson.length()-1)
                        Next.performClick();
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();

                }
            });

            lnr_touch_left.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();

                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                    if(quesIndex!=0)
                        Prev.performClick();

                }
            });
            mGestureDetector = new GestureDetectorCompat(getApplicationContext(), customGestureDetector);

        }
        class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //Log.v("OnFlling", "OnFlling " + (e1.getY() > e2.getY()));
                // Swipe left (next)
                if (e1.getX() > e2.getX()) {
                    if(quesIndex!=quizJson.length()-1)
                        Next.performClick();

                }

                // Swipe right (previous)
                if (e1.getX() < e2.getX()) {
                    if(quesIndex!=0)
                        Prev.performClick();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        }

        @Override
        protected void onPause() {
            super.onPause();
            if(mCvCountdownView!=null&&mCvCountdownView.getRemainTime()>0)
                mCvCountdownView.pause();
        }

        @Override
        protected void onResume() {
            super.onResume();
            if(mCvCountdownView!=null&&mCvCountdownView.getRemainTime()>0)
                mCvCountdownView.restart();
        }

    private void fillWithData() {




        //Log.v("URL Quize","URL Quize "+section_id+" url "+URL);

        CustomDialog.showDialog(DSWQuiz.this,true);
        StringRequest strReq = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);
                CustomDialog.closeDialog();
                try {
                    JSONObject json = new JSONObject(response);
                    quizJson = json.getJSONObject("quiz").getJSONArray("questions");

                    if(quizJson==null)
                        return;

                    loadDataBaseURL();
                    TOTAL_TIME= quizJson.length()*1;
                    setData();
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                    //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
                String msg="";

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg=getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg=getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg=getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg=getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg=getResources().getString(R.string.error_4);
                }
                CommonUtils.showToast(getApplicationContext(), msg);
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, "DSW");
        //CustomDialog.showDialog(QuizRulesActivity.this,true);
    }

    private void setData()
    {
        mCvCountdownView.start(1000*60*TOTAL_TIME);
        LinearLayout linearLayout = findViewById(R.id.linear);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.removeAllViews();
        selected = new int[quizJson.length()];
        java.util.Arrays.fill(selected, -1);
        correctAns = new int[quizJson.length()];
        java.util.Arrays.fill(correctAns, -1);
        Typeface tf=Typeface.createFromAsset(getAssets(),"opensans_regular.ttf");
        for (int i = 0; i < quizJson.length(); i++) {
            final int k;
            k = i;
            CheckedTextView btnPage = new CheckedTextView(_this);
            btnPage.setTypeface(tf);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)convertDpToPixel(25),(int)convertDpToPixel(25));
            lp.setMargins(9, 2, 9, 2);
            btnPage.setTextColor(getResources().getColor(R.color.black));
            btnPage.setBackground(ContextCompat.getDrawable(_this, R.drawable.buttonshape));
            btnPage.setTextSize(8.0f);
            btnPage.setId(i);
            btnPage.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            btnPage.setGravity(Gravity.CENTER);
            btnPage.setText(String.valueOf(i + 1));
            linearLayout.addView(btnPage, lp);
            btnPage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (CheckedTextView button : mButtonList) {
                        if (button.getId() == k) {
                            quesIndex = k;
                            a0.setBackgroundColor(0);
                            a1.setBackgroundColor(0);
                            a2.setBackgroundColor(0);
                            a3.setBackgroundColor(0);
                            loadFirstQuestion(quesIndex);
                            scrollview.scrollTo(0, 0);
                            //button.setTextColor(Color.parseColor("#ffcc33"));
                            button.setChecked(true);

                        } else {
                            //button.setTextColor(Color.BLACK);
                            button.setChecked(false);
                        }


                    }
                }
            });

            mButtonList.add(btnPage);
        }
        if(quizJson!=null&&quizJson.length()>quesIndex)
        loadFirstQuestion(quesIndex);
    }
}
