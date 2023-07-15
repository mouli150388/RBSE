package com.tutorix.tutorialspoint.quiz;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import android.view.LayoutInflater;
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
import android.widget.EditText;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.MockTestModelTable;
import com.tutorix.tutorialspoint.models.QuestionAnsDeatils;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.models.RecomandedModel;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.OnSwipeTouchListener;
import com.tutorix.tutorialspoint.utility.ProgressBarAnimation;
import com.tutorix.tutorialspoint.video.VideoPlayerActivity;
import com.tutorix.tutorialspoint.views.CircularProgressBarThumb;
import com.tutorix.tutorialspoint.views.CustomBarChartRender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import cn.iwgang.countdownview.CountdownView;


public class QuizQuestionActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    QuizQuestionActivity _this;
    private CountdownView mCvCountdownView;
    private int quesIndex = 0;
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

    private String lectureId;
    private String subjectId;
    private String lecture_name;
    private String section_name;
    private String mock_test;
    private String userid;
    private String section_id;
    private String classid;

    private Button Next;
    private Button Prev;
    Button finish;
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
    String mathLib0 = "";
    String mathLib1 = "";
    String mathLib2 = "";
    String mathLib3 = "";
    String mathLib4 = "";

    int color_theme;
    int color_theme_card;
    int color_theme_selected;//f2af98
    String quiz_id = "";

    ImageView img_logo_subj;

    JSONArray quizJson;
    boolean ismock;
    int TOTAL_TIME;
    int background_drawable = R.drawable.retake_button;
    int background_top_drawable = R.drawable.ic_chemistry_bg_green;
    int background_top_score_drawable = R.drawable.quiz_score_che;

    RelativeLayout rel_top_main;
    ImageView img_header;
    TextView txt_header;
    private int progressDrawable;
    boolean isActiveExpired;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        _this = QuizQuestionActivity.this;
        setContentView(R.layout.activity_quiz_question);
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        String assets = "file:///android_asset";

        mathLib = Constants.MathJax_Offline;
        SharedPref sh = new SharedPref();
        if (sh.isExpired(getApplicationContext())) {
            isActiveExpired = true;
        }

        rel_top_main = findViewById(R.id.rel_top_main);
        img_header = findViewById(R.id.img_header);
        txt_header = findViewById(R.id.txt_header);
        lnr_swipe = findViewById(R.id.lnr_swipe);
        lnr_touch_right = findViewById(R.id.lnr_touch_right);
        lnr_touch_left = findViewById(R.id.lnr_touch_left);

        ImageButton backScroll = findViewById(R.id.backscroll);
        ImageButton fullScroll = findViewById(R.id.fullscroll);
        //lnr_top = findViewById(R.id.lnr_top);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                quizJson = new JSONArray(extras.getString("json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (quizJson == null)
                return;
            lectureId = extras.getString(Constants.lectureId);
            lecture_name = extras.getString(Constants.lectureName);
            section_name = extras.getString(Constants.sectionName);
            subjectId = extras.getString(Constants.subjectId);
            classid = extras.getString(Constants.classId);
            userid = extras.getString(Constants.userId);
            section_id = extras.getString(Constants.sectionId);
            mock_test = extras.getString(Constants.mockTest);
            ismock = extras.getBoolean("ismock");

            try {
                int currrentClsId = Integer.parseInt(classid);
                if (currrentClsId <= 5) {
                    mathLib = Constants.MATH_SCRIBE;
                } else {
                    mathLib = Constants.MathJax_Offline;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadDataBaseURL();

            if (subjectId.equals("4")) {
                if (quizJson.length() % 2 == 0)
                    TOTAL_TIME = (int) (quizJson.length() * 1.5);
                else TOTAL_TIME = (int) ((quizJson.length() * 1.5) + 0.5);
            } else {
                TOTAL_TIME = quizJson.length() * 1;
            }
        }
        // img_logo_subj=findViewById(R.id.img_logo_subj);


        radio_1 = findViewById(R.id.radio_1);
        radio_2 = findViewById(R.id.radio_2);
        radio_3 = findViewById(R.id.radio_3);
        radio_4 = findViewById(R.id.radio_4);

        radio_1.setChecked(false);
        radio_2.setChecked(false);
        radio_3.setChecked(false);
        radio_4.setChecked(false);

        if (subjectId.equalsIgnoreCase("1")) {

            //Glide.with(getApplicationContext()).load(R.drawable.gif_phy).into(img_logo_subj);

            background_top_drawable = R.drawable.ic_phy_bg_green;
            img_header.setImageResource(R.drawable.quiz_header_phy);
            background_top_score_drawable = R.drawable.quiz_score_phy;
            background_drawable = R.drawable.quiz_quit_btn_phy;
            progressDrawable = R.drawable.circular_progress_phy;
            txt_header.setText(getString(R.string.physics));
            radio_1.setBackgroundResource(R.drawable.webviewquiz_select_one);
            radio_2.setBackgroundResource(R.drawable.webviewquiz_select_one);
            radio_3.setBackgroundResource(R.drawable.webviewquiz_select_one);
            radio_4.setBackgroundResource(R.drawable.webviewquiz_select_one);

        } else if (subjectId.equalsIgnoreCase("2")) {

            //Glide.with(getApplicationContext()).load(R.drawable.gif_che).into(img_logo_subj);
            img_header.setImageResource(R.drawable.quiz_header_che);
            background_top_drawable = R.drawable.ic_chemistry_bg_green;
            background_top_score_drawable = R.drawable.quiz_score_che;
            background_drawable = R.drawable.quiz_quit_btn_che;
            progressDrawable = R.drawable.circular_progress_che;
            txt_header.setText(getString(R.string.chemistry));
            radio_1.setBackgroundResource(R.drawable.webviewquiz_select_two);
            radio_2.setBackgroundResource(R.drawable.webviewquiz_select_two);
            radio_3.setBackgroundResource(R.drawable.webviewquiz_select_two);
            radio_4.setBackgroundResource(R.drawable.webviewquiz_select_two);
        } else if (subjectId.equalsIgnoreCase("3")) {

            //Glide.with(getApplicationContext()).load(R.drawable.gif_bio).into(img_logo_subj);
            img_header.setImageResource(R.drawable.quiz_header_bio);
            background_top_drawable = R.drawable.ic_bio_bg_green;
            background_drawable = R.drawable.quiz_quit_btn_bio;
            background_top_score_drawable = R.drawable.quiz_score_bio;
            progressDrawable = R.drawable.circular_progress_bio;
            txt_header.setText(getString(R.string.biology));
            radio_1.setBackgroundResource(R.drawable.webviewquiz_select_three);
            radio_2.setBackgroundResource(R.drawable.webviewquiz_select_three);
            radio_3.setBackgroundResource(R.drawable.webviewquiz_select_three);
            radio_4.setBackgroundResource(R.drawable.webviewquiz_select_three);

        } else {

            //Glide.with(getApplicationContext()).load(R.drawable.gif_math).into(img_logo_subj);
            img_header.setImageResource(R.drawable.quiz_header_math);
            background_top_drawable = R.drawable.ic_math_bg_green;
            background_drawable = R.drawable.quiz_quit_btn_math;
            background_top_score_drawable = R.drawable.quiz_score_math;
            progressDrawable = R.drawable.circular_progress_math;
            txt_header.setText(getString(R.string.mathematics));

            radio_1.setBackgroundResource(R.drawable.webviewquiz_select_four);
            radio_2.setBackgroundResource(R.drawable.webviewquiz_select_four);
            radio_3.setBackgroundResource(R.drawable.webviewquiz_select_four);
            radio_4.setBackgroundResource(R.drawable.webviewquiz_select_four);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subjectId) {
                case "1":
                    window.setStatusBarColor(getResources().getColor(R.color.phy_background_status));
                    break;
                case "2":
                    window.setStatusBarColor(getResources().getColor(R.color.che_background_status));
                    break;
                case "3":
                    window.setStatusBarColor(getResources().getColor(R.color.bio_background_status));
                    break;
                case "4":
                    window.setStatusBarColor(getResources().getColor(R.color.math_background_status));
                    break;
            }
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
        LinearLayout linearLayout = findViewById(R.id.linear);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.removeAllViews();
        Typeface tf = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
        for (int i = 0; i < quizJson.length(); i++) {
            final int k;
            k = i;
            CheckedTextView btnPage = new CheckedTextView(_this);
            btnPage.setTypeface(tf);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) convertDpToPixel(30), (int) convertDpToPixel(30));
            lp.setMargins(9, 2, 9, 2);
            btnPage.setTextColor(getResources().getColor(R.color.black));
            btnPage.setBackground(ContextCompat.getDrawable(_this, R.drawable.buttonshape));
            btnPage.setTextSize(12.0f);
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

                        SubmitToServer(getTrackTime(), score, wrongAnswers, false);

                        finish();
                    }
                    final int k;
                    quesIndex--;
                    k = quesIndex;
                    for (CheckedTextView button : mButtonList) {
                        if (button.getId() == k) {
                            try {
                                button.getParent().requestChildFocus(button, button);
                            } catch (Exception e) {
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
                            } catch (Exception e) {
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
        mCvCountdownView.start(1000 * 60 * TOTAL_TIME);
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
                click_next_previous = 0;
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
                        } catch (Exception e) {
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
                click_next_previous = 1;
                final int k;
                quesIndex++;
                k = quesIndex;
                for (CheckedTextView button : mButtonList) {
                    if (button.getId() == k) {
                        try {
                            button.getParent().requestChildFocus(button, button);
                        } catch (Exception e) {
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

        selected = new int[quizJson.length()];
        java.util.Arrays.fill(selected, -1);
        correctAns = new int[quizJson.length()];
        java.util.Arrays.fill(correctAns, -1);
        a0.setBackgroundColor(0);
        a1.setBackgroundColor(0);
        a2.setBackgroundColor(0);
        a3.setBackgroundColor(0);
        loadFirstQuestion(quesIndex);

    }


    private int click_next_previous = 1;

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

        if (!timeUp && (map == null | map.isEmpty())) {
            CommonUtils.showToast(getApplicationContext(), "You didn't answer any question, please answer first");
            return;
        }

        if (dialog != null && dialog.isShowing())
            return;

        if (timeUp) {
            mCvCountdownView.stop();
            Minute = TOTAL_TIME - mCvCountdownView.getMinute();

            //Log.v("Minute","Minute "+Minute+" "+mCvCountdownView.getMinute()+" "+mCvCountdownView.getSecond());
               /* 14 0
                    14 52*/

            Sec = 60 - mCvCountdownView.getSecond();
            if (Sec == 60) {
                Minute = TOTAL_TIME - mCvCountdownView.getMinute();
                Sec = 0;
            } else
                Minute = TOTAL_TIME - (mCvCountdownView.getMinute() + 1);


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
            SubmitToServer(getTrackTime(), score, wrongAnswers, true);
            return;
        }
        dialog = new Dialog(_this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert);
        TextView txt_msg = dialog.findViewById(R.id.txt_msg);
        txt_msg.setText("");
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
        //txt_ok.setBackgroundResource(background_drawable);

        //txt_ok.setBackgroundResource(color_theme);
        if (timeUp) {
            txt_cancel.setVisibility(View.GONE);
            txt_ok.setText(getString(R.string.ok));
            dialog.setCancelable(false);
        } else {
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

                Sec = 60 - mCvCountdownView.getSecond();
                if (Sec == 60) {
                    Minute = TOTAL_TIME - mCvCountdownView.getMinute();
                    Sec = 0;
                } else
                    Minute = TOTAL_TIME - (mCvCountdownView.getMinute() + 1);


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
                SubmitToServer(getTrackTime(), score, wrongAnswers, true);

            }
        });


        if (timeUp)
            txt_msg.setText(getString(R.string.timup));
        else if (ismock)
            txt_msg.setText(getString(R.string.do_you_realy_close_mocktest));
        else
            txt_msg.setText(getString(R.string.do_you_realy_close_quiz));

        dialog.show();

    }


    private void SubmitToServer(final String timeTaken, final int score, final int wrongAnswers, boolean showResult) {
        currentTime = String.valueOf(System.currentTimeMillis());


        final JSONObject fjson = new JSONObject();
        final JSONObject quizObj = new JSONObject();
        try {
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.lectureId, lectureId);
            fjson.put(Constants.sectionId, section_id);
            fjson.put(Constants.subjectId, subjectId);
            if (!ismock)
                fjson.put(Constants.lectureName, lecture_name);
            else
                fjson.put(Constants.lectureName, section_name);
            fjson.put("quiz_duration", timeTaken);
            fjson.put("total_marks", score);
            fjson.put("total_correct", score);
            fjson.put("total_wrong", wrongAnswers);
            fjson.put("section_name", section_name);
            fjson.put("mock_test", mock_test);
            fjson.put(Constants.accessToken, access_token);

            fjson.put("total_questions", String.valueOf(quizJson.length()));
            fjson.put("attempted_questions", String.valueOf(map.size()));


            JSONArray postingArray = quizJson;

            for (int i = 0; i < postingArray.length(); i++) {
                if (map.get(postingArray.getJSONObject(i).getString("question_id")) != null) {
                    postingArray.getJSONObject(i).put("option_selected", map.get(postingArray.getJSONObject(i).getString("question_id")));
                } else {
                    postingArray.getJSONObject(i).put("option_selected", "");
                }
            }

            JSONObject questionsObj = new JSONObject();
            questionsObj.put("questions", postingArray);


            quizObj.put("quiz", questionsObj);

            fjson.put("attempted_questions", String.valueOf(map.size()));
            fjson.put("quiz_json", String.valueOf(quizObj));

            //Log.v("Request","Request "+fjson.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* if (loginType.isEmpty() || !(AppConfig.checkSDCardEnabled(_this, userid, classid) && AppConfig.checkSdcard(classid,getApplicationContext()))) {
            if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

                CommonUtils.showToast(getApplicationContext(), getResources().getString(R.string.no_internet));
                return;
            }
        }*/
        //if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {


        if(AppConfig.checkSdcard(classid,getApplicationContext())) {


            MyDatabase database = MyDatabase.getDatabase(_this);
            QuizModel quizModel = new QuizModel();
            quizModel.userId = userid;
            quizModel.classId = classid;
            quizModel.subject_id = subjectId;
            quizModel.section_id = section_id;
            quizModel.lectur_id = lectureId;
            quizModel.QuizDuration = timeTaken;
            quizModel.total_wrong = wrongAnswers + "";
            quizModel.total_correct = score + "";
            quizModel.total = quizJson.length();
            quizModel.attempted_questions = map.size() + "";
            quizModel.QuizCreatedDtm = getDateTime();
            quizModel.quiz_id = "";
            quizModel.question = quizObj + "";
            quizModel.mock_test = mock_test;
            quizModel.sync = "N";
            if (!ismock)
                quizModel.lecture_name = lecture_name;
            else
                quizModel.lecture_name = section_name;

            quizModel.section_name = section_name;
            database.quizModelDAO().addQuiz(quizModel);


            TrackModel chapters = new TrackModel();
            if (!ismock)
                chapters.activity_type = "Q";
            else
                chapters.activity_type = "M";
            chapters.activity_duration = timeTaken;
            chapters.activity_date = getDateTime();
            chapters.quiz_id = "";
            chapters.subject_id = subjectId;
            chapters.section_id = section_id;
            chapters.lecture_id = lectureId;
            chapters.class_id = classid;
            chapters.user_id = userid;

            if (!ismock)
                chapters.lecture_name = lecture_name;
            else
                chapters.lecture_name = section_name;


            chapters.is_sync = false;
            chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
            database.trackDAO().insertTrack(chapters);


            if (ismock) {

                MockTestModelTable mockTestModelTable = database.mockTestDAO().getMockTest(userid, classid, subjectId, section_id, mock_test);

                if (mockTestModelTable == null) {
                    mockTestModelTable = new MockTestModelTable();
                    mockTestModelTable.user_id = userid;
                    mockTestModelTable.class_id = classid;
                    mockTestModelTable.subject_id = subjectId;
                    mockTestModelTable.section_id = section_id;
                    mockTestModelTable.mocktest_type = mock_test;
                    mockTestModelTable.total_attempts = 1;
                    mockTestModelTable.total_marks = score;
                    mockTestModelTable.total_questions = quizJson.length();
                    mockTestModelTable.low_marks = score;
                    mockTestModelTable.high_marks = score;
                    mockTestModelTable.created_dtm = currentTime;
                    mockTestModelTable.sync = "N";
                    database.mockTestDAO().insertRecomanded(mockTestModelTable);
                } else {

                    mockTestModelTable.total_attempts = mockTestModelTable.total_attempts + 1;
                    mockTestModelTable.total_marks = mockTestModelTable.total_marks + score;

                    if (score <= mockTestModelTable.low_marks)
                        mockTestModelTable.low_marks = score;

                    mockTestModelTable.total_questions = mockTestModelTable.total_questions + quizJson.length();

                    if (score >= mockTestModelTable.high_marks)
                        mockTestModelTable.high_marks = score;

                    database.mockTestDAO().updateMockTest(userid, classid, subjectId, section_id, mock_test, mockTestModelTable.total_marks, mockTestModelTable.total_attempts, mockTestModelTable.low_marks, mockTestModelTable.high_marks);

                }


            }
            if (showResult)
                showResult(score, map.size());
        }
        else if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            String tag_string_req = Constants.reqRegister;

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.USER_QUIZ_COMPLETED,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (showResult)
                                CustomDialog.closeDialog();
                            //Log.v("Response","Response "+response);
                            try {


                                JSONObject jObj = new JSONObject(response);


                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    quiz_id = String.valueOf(jObj.getString("quiz_id"));
                                    if (showResult) {
                                        showResult(score, map.size());
                                    }

                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
                                    // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                                if (loginType.equalsIgnoreCase("S")) {


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                                // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String msg = "";

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        msg = getResources().getString(R.string.error_1);
                    } else if (error instanceof AuthFailureError) {
                        msg = getResources().getString(R.string.error_2);
                    } else if (error instanceof ServerError) {
                        msg = getResources().getString(R.string.error_2);
                    } else if (error instanceof NetworkError) {
                        msg = getResources().getString(R.string.error_3);
                    } else if (error instanceof ParseError) {
                        msg = getResources().getString(R.string.error_4);
                    }
                    CommonUtils.showToast(getApplicationContext(), msg);
                    //finish();
                }
            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }


                        cacheEntry.data = response.data;

                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                "UTF-8");
                        return Response.success((jsonString), cacheEntry);
                    } catch (Exception e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    super.deliverError(error);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }

                final String Key = AppConfig.ENC_KEY;
                final String message = fjson.toString();
                final String encryption = Security.encrypt(message, Key);


                @Override
                protected Map<String, String> getParams() {
                    //Log.v("JSON","JSON "+encryption);
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfig.JSON_DATA, encryption);
                    return params;
                }

            };
            if (showResult)
                CustomDialog.showDialog(QuizQuestionActivity.this, false);
            if (!loginType.isEmpty() && AppConfig.checkSDCardEnabled(_this, userid, classid)) {


                MyDatabase database = MyDatabase.getDatabase(_this);
                QuizModel quizModel = new QuizModel();
                quizModel.userId = userid;
                quizModel.classId = classid;
                quizModel.subject_id = subjectId;
                quizModel.section_id = section_id;
                quizModel.lectur_id = lectureId;
                quizModel.QuizDuration = timeTaken;
                quizModel.total_wrong = wrongAnswers + "";
                quizModel.total_correct = score + "";
                quizModel.total = quizJson.length();
                quizModel.attempted_questions = map.size() + "";
                quizModel.QuizCreatedDtm = getDateTime();
                quizModel.quiz_id = "";
                quizModel.question = quizObj + "";
                quizModel.mock_test = mock_test;
                quizModel.sync = "N";
                if (!ismock)
                    quizModel.lecture_name = lecture_name;
                else
                    quizModel.lecture_name = section_name;

                quizModel.section_name = section_name;
                database.quizModelDAO().addQuiz(quizModel);


                TrackModel chapters = new TrackModel();
                if (!ismock)
                    chapters.activity_type = "Q";
                else
                    chapters.activity_type = "M";
                chapters.activity_duration = timeTaken;
                chapters.activity_date = getDateTime();
                chapters.quiz_id = "";
                chapters.subject_id = subjectId;
                chapters.section_id = section_id;
                chapters.lecture_id = lectureId;
                chapters.class_id = classid;
                chapters.user_id = userid;

                if (!ismock)
                    chapters.lecture_name = lecture_name;
                else
                    chapters.lecture_name = section_name;


                chapters.is_sync = false;
                chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                database.trackDAO().insertTrack(chapters);


                if (ismock) {

                    MockTestModelTable mockTestModelTable = database.mockTestDAO().getMockTest(userid, classid, subjectId, section_id, mock_test);

                    if (mockTestModelTable == null) {
                        mockTestModelTable = new MockTestModelTable();
                        mockTestModelTable.user_id = userid;
                        mockTestModelTable.class_id = classid;
                        mockTestModelTable.subject_id = subjectId;
                        mockTestModelTable.section_id = section_id;
                        mockTestModelTable.mocktest_type = mock_test;
                        mockTestModelTable.total_attempts = 1;
                        mockTestModelTable.total_marks = score;
                        mockTestModelTable.total_questions = quizJson.length();
                        mockTestModelTable.low_marks = score;
                        mockTestModelTable.high_marks = score;
                        mockTestModelTable.created_dtm = currentTime;
                        mockTestModelTable.sync = "N";
                        database.mockTestDAO().insertRecomanded(mockTestModelTable);
                    } else {

                        mockTestModelTable.total_attempts = mockTestModelTable.total_attempts + 1;
                        mockTestModelTable.total_marks = mockTestModelTable.total_marks + score;

                        if (score <= mockTestModelTable.low_marks)
                            mockTestModelTable.low_marks = score;

                        mockTestModelTable.total_questions = mockTestModelTable.total_questions + quizJson.length();

                        if (score >= mockTestModelTable.high_marks)
                            mockTestModelTable.high_marks = score;

                        database.mockTestDAO().updateMockTest(userid, classid, subjectId, section_id, mock_test, mockTestModelTable.total_marks, mockTestModelTable.total_attempts, mockTestModelTable.low_marks, mockTestModelTable.high_marks);

                    }


                }
            }

            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }
        else {
            CommonUtils.showToast(getApplicationContext(), getResources().getString(R.string.no_internet));
        }

    }

    String quesValue;

    private String getDateTime() {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(currentTime));
        return CommonUtils.format.format(calendar.getTime());

    }

    public class testClass {
        public testClass() {
        }

        @JavascriptInterface
        public void showAndroidToast(final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    which++;

                    if (msg.equalsIgnoreCase("0"))
                        question.setVisibility(View.VISIBLE);
                    else if (msg.equalsIgnoreCase("1"))
                        lnr_1.setVisibility(View.VISIBLE);
                    else if (msg.equalsIgnoreCase("2"))
                        lnr_2.setVisibility(View.VISIBLE);
                    else if (msg.equalsIgnoreCase("3"))
                        lnr_3.setVisibility(View.VISIBLE);
                    else if (msg.equalsIgnoreCase("4"))
                        lnr_4.setVisibility(View.VISIBLE);

                    if (which > 4)
                        CustomDialog.closeDialog();
                    //ldg_q.setVisibility(View.GONE);
                }
            });

        }
    }

    int which = 0;

    @SuppressLint("ClickableViewAccessibility")
    private void loadFirstQuestion(final int quesIndex) {
        try {
            which = 0;
            JSONObject aQues = quizJson.getJSONObject(quesIndex);
            quesValue = mathLib + aQues.getString("question") + mathLib0;
            //quesValue =aQues.getString("question");
            final String question_id = aQues.getString("question_id");


            question.loadDataWithBaseURL(basePath, getStyle()/*+"<font color='#F79646'>Q " +(quesIndex + 1 )+")</font> "*/ + quesValue, "text/html", "utf-8", "");

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

                    if (ansList.has("option_1")) {
                        lnr_1.setVisibility(View.VISIBLE);
                        aAns = mathLib + ansList.getString("option_1") + mathLib1;
                        a0.loadDataWithBaseURL(basePath, getIntialTable("A", aAns), "text/html", "utf-8", "");
                    }
                    if (ansList.has("option_2")) {
                        lnr_2.setVisibility(View.VISIBLE);
                        aAns = mathLib + ansList.getString("option_2") + mathLib2;
                        a1.loadDataWithBaseURL(basePath, getIntialTable("B", aAns), "text/html", "utf-8", "");

                    }
                    if (ansList.has("option_3")) {
                        lnr_3.setVisibility(View.VISIBLE);
                        aAns = mathLib + ansList.getString("option_3") + mathLib3;
                        a2.loadDataWithBaseURL(basePath, getIntialTable("C", aAns), "text/html", "utf-8", "");

                    }
                    if (ansList.has("option_4")) {
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
        if (click_next_previous == 0)
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_left_to_right);
        else
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_fall_down_slow);
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

        try {
            question.clearHistory();
            question.clearFormData();

            a0.clearHistory();
            a0.clearFormData();

            a1.clearHistory();
            a1.clearFormData();

            a2.clearHistory();
            a2.clearFormData();

            a3.clearHistory();
            a3.clearFormData();
        } catch (Exception e) {

        }

        if (alertDialog != null && alertDialog.isShowing()) {
            quesIndex = 0;
            alertDialog.dismiss();
            mCvCountdownView = null;

            finish();

            return;
        }
        dialog = new Dialog(_this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert);
        TextView txt_msg = dialog.findViewById(R.id.txt_msg);
        txt_msg.setText(getString(R.string.you_want_go_back));
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
        txt_ok.setText(getString(R.string.yes));
        //txt_ok.setBackgroundResource(color_theme);
        txt_cancel.setVisibility(View.VISIBLE);
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
                quesIndex = 0;

                int score = 0;
                for (int i = 0; i < correctAns.length; i++) {
                    if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
                        score++;
                }
                wrongAnswers = quizJson.length() - score;
                SubmitToServer(getTrackTime(), score, wrongAnswers, false);
                finish();
            }
        });
        dialog.show();

       /* if (!isback) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isback = false;
                }
            }, 3000);
            CommonUtils.showToast(getApplicationContext(), "Press again to exit");
            isback = true;
            return;
        }*/
        //super.onBackPressed();

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @SuppressLint("SimpleDateFormat")
    public String getTrackTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String end = dateFormat.format(date);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateFormat.parse(start);
            date2 = dateFormat.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DecimalFormat formatter = new DecimalFormat("00");
        long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
        if (difference < 0)
            difference = -difference;
        long diffSeconds = difference / 1000 % 60;
        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        if (diffHours >= 1 || diffMinutes > TOTAL_TIME) {
            diffHours = 0;
            diffMinutes = 0;
            diffSeconds = 0;

            return formatter.format(0) + ":" + formatter.format(TOTAL_TIME) + ":" + formatter.format(0);
        }
        //Log.d("difference", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);

        return formatter.format(diffHours) + ":" + formatter.format(diffMinutes) + ":" + formatter.format(diffSeconds);
    }

    String basePath = "";

    private void loadDataBaseURL() {
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userid = userInfo[0];
        ;
        loginType = userInfo[2];

        if (/*AppConfig.checkSDCardEnabled(_this, userid, classid) &&*/ AppConfig.checkSdcard(classid,getApplicationContext())) {
            if (lectureId.equalsIgnoreCase("0"))
                basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext()) /*+ subjectId + "/" + section_id*/;
            else
                basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext())/* + subjectId + "/" + section_id + "/" + lectureId*/;

        } else {
            if (lectureId.equalsIgnoreCase("0"))
                basePath = AppConfig.getOnlineURLImage(classid);
            else
                basePath = AppConfig.getOnlineURLImage(classid);
        }

        /*if (loginType.isEmpty()) {
            if (lectureId.equalsIgnoreCase("0"))
                basePath = AppConfig.getOnlineURLImage(classid);
            else
                basePath = AppConfig.getOnlineURLImage(classid);
        } else if (loginType.equalsIgnoreCase("O")) {

            if (AppConfig.checkSDCardEnabled(_this, userid, classid) && AppConfig.checkSdcard(classid,getApplicationContext())) {
                if (lectureId.equalsIgnoreCase("0"))
                    basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext()) *//*+ subjectId + "/" + section_id*//*;
                else
                    basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext())*//* + subjectId + "/" + section_id + "/" + lectureId*//*;

            } else {
                if (lectureId.equalsIgnoreCase("0"))
                    basePath = AppConfig.getOnlineURLImage(classid);
                else
                    basePath = AppConfig.getOnlineURLImage(classid);
            }
        } else {
            if (lectureId.equalsIgnoreCase("0"))
                basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext()) *//*+ subjectId + "/" + section_id*//*;
            else
                basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext())*//* + subjectId + "/" + section_id + "/" + lectureId*//*;

        }*/


      /*  if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            if (lectureId.equalsIgnoreCase("0"))
                basePath = AppConfig.getOnlineURLImage(classid);
            else
                basePath = AppConfig.getOnlineURLImage(classid);
            //basePath="http://tutorix.com/web/tmp/";
        } else {
            if (lectureId.equalsIgnoreCase("0"))
                basePath = "file://" + AppConfig.getSdCardPath(classid) *//*+ subjectId + "/" + section_id*//*;
            else
                basePath = "file://" + AppConfig.getSdCardPath(classid)*//* + subjectId + "/" + section_id + "/" + lectureId*//*;
            //basePath=basePath.replace("class61","class6/1");

        }*/
        if (!basePath.trim().endsWith("/"))
            basePath = basePath + "/";


    }

    private void updateTheme() {
        switch (subjectId) {
            case "1":
                color_theme = R.color.phy_background;
                color_theme_card = R.color.phy_background_card;
                color_theme_selected = R.color.phy_background_status_trns;


                break;
            case "2":
                color_theme = R.color.che_background;
                color_theme_card = R.color.che_background_card;
                color_theme_selected = R.color.che_background_status_trns;

                break;
            case "3":
                color_theme = R.color.bio_background;
                color_theme_card = R.color.bio_background_card;
                color_theme_selected = R.color.bio_background_status_trns;

                break;
            case "4":
                color_theme = R.color.math_background;
                color_theme_card = R.color.math_background_card;
                color_theme_selected = R.color.math_background_status_trns;

                break;
        }

       /* Prev.setTextColor(getResources().getColor(color_theme));
        Next.setTextColor(getResources().getColor(color_theme));*/
        //finish.setTextColor(getResources().getColor(color_theme));

    }

    private String getIntialTable(String number, String content) {

        //Log.v("Content","Content "+content);
        return "<table>\n" +
                "     <tr>\n" +
                "        <td valign=\"center\"><span class ='a' style='border:1px solid #ccc;border-radius:5px; padding:2px 8px 2px 8px;'>" +
                number + "</td>\n" +
                "        <td> " +
                content + "</td>\n" +
                "     </tr>\n" +
                "  </table>";


    }

    public float convertDpToPixel(float dp) {

        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     *           param    get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public float convertPixelsToDp(float px) {
        return px / ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private String getStyle() {
        return "<style>" +
                " table.quiz-table{margin:5px; padding:0px;border-collapse:collapse;}" +
                "table.quiz-table th, table.quiz-table td{padding:5px; border:1px solid #ccc;}" +
                " </style>";
    }

    private void setAccuracy(PieChart piechart, float ansP, float ansW) {
        piechart.getDescription().setText("");
        piechart.setHoleRadius(70);
        piechart.setEntryLabelTextSize(8);
        Typeface tf = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(ansP, getString(R.string.correct)));
        yValues.add(new PieEntry(ansW, getString(R.string.wrong)));
        PieDataSet pidataSet = new PieDataSet(yValues, "");
        pidataSet.setColors(new int[]{R.color.bio_background, R.color.red}, getApplicationContext());
        pidataSet.setSliceSpace(1f);
        pidataSet.setValueTextSize(8f);
        pidataSet.setValueTextColor(getResources().getColor(R.color.white));

        //pidataSet.setValueTextColors(Arrays.asList(new Integer[]{R.color.white,R.color.white}));
        pidataSet.setSelectionShift(1f);
        pidataSet.setValueLineColor(getResources().getColor(R.color.white));
        pidataSet.setValueTypeface(tf);
        // pidataSet.getEntriesForXValue(ansP).setLabel("Answered");
        // pidataSet.getEntriesForXValue(100-ansP).get(0).setLabel("Wrong");


        PieData pidata = new PieData(pidataSet);
        pidata.setValueFormatter(new PercentFormatter());


        piechart.setDrawEntryLabels(false);
        piechart.setEntryLabelColor(getResources().getColor(R.color.white));
        piechart.setNoDataTextTypeface(tf);
        piechart.setNoDataTextTypeface(tf);
        piechart.setEntryLabelTypeface(tf);
        piechart.setCenterTextColor(getResources().getColor(R.color.white));


        piechart.setData(pidata);
    }

    DecimalFormat df = new DecimalFormat("#.##");

    private void showRecommanded(LinearLayout lnr_recommand_items, BarChart barchart) {

        Set<String> videos_type = new HashSet<String>();


        try {
            JSONArray arra = quizJson;
            int TOTLA_UN_EASY = 0;
            int TOTLA_UN_MEDIUM = 0;
            int TOTLA_UN_HARD = 0;

            int WRONG_EASY = 0;
            int WRONG_MEDIUM = 0;
            int WRONG_HARD = 0;

            int ANS_EASY = 0;
            int ANS_MEDIUM = 0;
            int ANS_HARD = 0;


            HashMap<String, Float> hmresult = new HashMap<>();
            HashMap<String, String> hmresultAnsers = new HashMap<>();
            HashMap<String, String> hmTitle = new HashMap<>();
            QuestionAnsDeatils questionAnsDeatils;
            List<QuestionAnsDeatils> list = new ArrayList<>();
            for (int i = 0; i < arra.length(); i++) {
                //Log.v("Resposne","Resposne pos"+i+" data "+arra.getJSONObject(i));
                questionAnsDeatils = new QuestionAnsDeatils();
                questionAnsDeatils.q_id = arra.getJSONObject(i).getString("question_id");
              /*  if(i==3)
                    questionAnsDeatils.lecture_id="3";
                else if(i==6)
                questionAnsDeatils.lecture_id="6";
                else*/
                questionAnsDeatils.lecture_id = arra.getJSONObject(i).getString("lecture_id");

                questionAnsDeatils.question_type = arra.getJSONObject(i).getString("question_type");
                questionAnsDeatils.option_right = arra.getJSONObject(i).getString("option_right");
                if (questionAnsDeatils.question_type.equals("E")) {
                    questionAnsDeatils.question_level = 1;

                } else if (questionAnsDeatils.question_type.equals("M")) {
                    questionAnsDeatils.question_level = 2;

                } else if (questionAnsDeatils.question_type.equals("H")) {
                    questionAnsDeatils.question_level = 3;

                }

                if (map.get(arra.getJSONObject(i).getString("question_id")) != null) {
                    if (map.get(arra.getJSONObject(i).getString("question_id")).equals(questionAnsDeatils.option_right)) {
                        questionAnsDeatils.answer_type = 1;
                        if (questionAnsDeatils.question_type.equals("E")) {
                            ANS_EASY = ANS_EASY + 1;

                        } else if (questionAnsDeatils.question_type.equals("M")) {
                            ANS_MEDIUM = ANS_MEDIUM + 1;
                        } else if (questionAnsDeatils.question_type.equals("H")) {
                            ANS_HARD = ANS_HARD + 1;
                        }
                    } else {
                        questionAnsDeatils.answer_type = 0;
                        if (questionAnsDeatils.question_type.equals("E")) {
                            WRONG_EASY = WRONG_EASY + 1;

                        } else if (questionAnsDeatils.question_type.equals("M")) {
                            WRONG_MEDIUM = WRONG_MEDIUM + 1;
                        } else if (questionAnsDeatils.question_type.equals("H")) {
                            WRONG_HARD = WRONG_HARD + 1;
                        }
                    }

                } else {
                    arra.getJSONObject(i).put("option_selected", "");
                    questionAnsDeatils.answer_type = -1;

                    if (questionAnsDeatils.question_type.equals("E")) {
                        TOTLA_UN_EASY = TOTLA_UN_EASY + 1;

                    } else if (questionAnsDeatils.question_type.equals("M")) {
                        TOTLA_UN_MEDIUM = TOTLA_UN_MEDIUM + 1;
                    } else if (questionAnsDeatils.question_type.equals("H")) {
                        TOTLA_UN_HARD = TOTLA_UN_HARD + 1;
                    }
                }

                questionAnsDeatils.result = questionAnsDeatils.answer_type * questionAnsDeatils.question_level;

                if (hmresult.containsKey(questionAnsDeatils.lecture_id)) {
                    //Log.v("Quiz Result","Quiz Result "+questionAnsDeatils.result+" "+hmresult.get(questionAnsDeatils.lecture_id)+" "+((hmresult.get(questionAnsDeatils.lecture_id) + questionAnsDeatils.result) / 2));
                    try {
                        hmresult.put(questionAnsDeatils.lecture_id, Float.parseFloat(df.format((hmresult.get(questionAnsDeatils.lecture_id) + questionAnsDeatils.result) / 2)));
                    } catch (Exception e) {
                        hmresult.put(questionAnsDeatils.lecture_id, Float.parseFloat(df.format((hmresult.get(questionAnsDeatils.lecture_id) + questionAnsDeatils.result) / 2).replaceAll(",", ".")));

                    }
                    hmresultAnsers.put(questionAnsDeatils.lecture_id, hmresultAnsers.get(questionAnsDeatils.lecture_id) + questionAnsDeatils.answer_type + "");
                } else {
                    try {
                        hmresult.put(questionAnsDeatils.lecture_id, Float.parseFloat(df.format(questionAnsDeatils.result)));
                    } catch (Exception e) {
                        hmresult.put(questionAnsDeatils.lecture_id, Float.parseFloat(df.format(questionAnsDeatils.result).replaceAll(",", ".")));

                    }
                    hmTitle.put(questionAnsDeatils.lecture_id, arra.getJSONObject(i).getString("lecture_title"));
                    hmresultAnsers.put(questionAnsDeatils.lecture_id, questionAnsDeatils.answer_type + "");
                }
                list.add(questionAnsDeatils);
            }

         /*   seekBar_e.setMax(TOTLA_EASY);
            seekBar_e.setProgress(ATTEMPT_EASY);

            seekBar_m.setMax(TOTLA_MEDIUM);
            seekBar_m.setProgress(ATTEMPT_MEDIUM);

            seekBar_h.setMax(TOTLA_HARD);
            seekBar_h.setProgress(ATTEMPT_HARD);*/

            hmresult = sortByValue(hmresult);


            int drawable_play_icon = 0;
            if (subjectId.equalsIgnoreCase("1")) {
                drawable_play_icon = R.drawable.ic_phy_play;

            } else if (subjectId.equalsIgnoreCase("2")) {
                drawable_play_icon = R.drawable.ic_che_play;

            } else if (subjectId.equalsIgnoreCase("3")) {
                drawable_play_icon = R.drawable.ic_bio_play;

            } else if (subjectId.equalsIgnoreCase("4")) {
                drawable_play_icon = R.drawable.ic_math_play;

            }

            JSONArray arrayRecom = new JSONArray();
            JSONObject obj;


            for (String key : hmresult.keySet()) {

                if (arrayRecom.length() < 3) {
                    if (hmresultAnsers.containsKey(key))
                        if (hmresultAnsers.get(key).contains("0") || hmresultAnsers.get(key).contains("-1")) {
                            obj = new JSONObject();
                            obj.put("lecture_id", key);
                            obj.put("lecture_title", hmTitle.get(key));
                            arrayRecom.put(obj);


                            View view = getLayoutInflater().inflate(R.layout.recommanded_item, null);
                            TextView txt_recommanded = view.findViewById(R.id.txt_recommanded);
                            ImageView playButton = view.findViewById(R.id.playButton);
                            playButton.setImageResource(drawable_play_icon);
                            LinearLayout lnr_play = view.findViewById(R.id.lnr_play);
                            txt_recommanded.setText(hmTitle.get(key));
                            txt_recommanded.setTextColor(getResources().getColor(color_theme));
                            lnr_play.setTag(key);
                            lnr_play.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    i.putExtra(Constants.lectureVideoUrl, "");
                                    i.putExtra(Constants.sectionName, section_name);

                                    i.putExtra(Constants.lectureId, lnr_play.getTag().toString());
                                    i.putExtra(Constants.subjectId, subjectId);
                                    i.putExtra(Constants.classId, classid);
                                    i.putExtra(Constants.userId, userid);
                                    i.putExtra(Constants.sectionId, section_id);
                                    i.putExtra(Constants.lectureName, txt_recommanded.getText());
                                    try {
                                        question.clearHistory();
                                        question.clearFormData();

                                        a0.clearHistory();
                                        a0.clearFormData();

                                        a1.clearHistory();
                                        a1.clearFormData();

                                        a2.clearHistory();
                                        a2.clearFormData();

                                        a3.clearHistory();
                                        a3.clearFormData();
                                    } catch (Exception e) {

                                    }
                                    startActivity(i);
                                    AppController.getInstance().playAudio(R.raw.qz_next);

                                }
                            });
                            lnr_recommand_items.addView(view);
                        }
                }
            }


            if (arrayRecom.length() > 0)
                sendRecomanded(arrayRecom);
            //ANSWERED ARRAY
            ArrayList<BarEntry> ans_values = new ArrayList<>();
            if (ANS_EASY == 0)
                ans_values.add(new BarEntry(0, new float[]{0.5f}));
            else
                ans_values.add(new BarEntry(0, new float[]{ANS_EASY}));

            if (ANS_MEDIUM == 0)
                ans_values.add(new BarEntry(1, new float[]{0.5f}));
            else
                ans_values.add(new BarEntry(1, new float[]{ANS_MEDIUM}));

            if (ANS_HARD == 0)
                ans_values.add(new BarEntry(2, new float[]{0.5f}));
            else
                ans_values.add(new BarEntry(2, new float[]{ANS_HARD}));

            //WRONG ANSWERED ARRAY

            ArrayList<BarEntry> wrong_values = new ArrayList<>();
            if (WRONG_EASY == 0)
                wrong_values.add(new BarEntry(0, new float[]{0.5f}));
            else
                wrong_values.add(new BarEntry(0, new float[]{WRONG_EASY}));

            if (WRONG_MEDIUM == 0)
                wrong_values.add(new BarEntry(1, new float[]{0.5f}));
            else
                wrong_values.add(new BarEntry(1, new float[]{WRONG_MEDIUM}));

            if (WRONG_HARD == 0)
                wrong_values.add(new BarEntry(2, new float[]{0.5f}));
            else
                wrong_values.add(new BarEntry(2, new float[]{WRONG_HARD}));


            //UN ATTEMPTED ARRAY

            ArrayList<BarEntry> unatempt_values = new ArrayList<>();
            if (TOTLA_UN_EASY == 0)
                unatempt_values.add(new BarEntry(0, new float[]{0.5f}));
            else
                unatempt_values.add(new BarEntry(0, new float[]{TOTLA_UN_EASY}));

            if (TOTLA_UN_MEDIUM == 0)
                unatempt_values.add(new BarEntry(1, new float[]{0.5f}));
            else
                unatempt_values.add(new BarEntry(1, new float[]{TOTLA_UN_MEDIUM}));

            if (TOTLA_UN_HARD == 0)
                unatempt_values.add(new BarEntry(2, new float[]{0.5f}));
            else
                unatempt_values.add(new BarEntry(2, new float[]{TOTLA_UN_HARD}));


            CustomBarChartRender barChartRender = new CustomBarChartRender(barchart, barchart.getAnimator(), barchart.getViewPortHandler());
            barChartRender.setRadius(5);
            barchart.setRenderer(barChartRender);

            /*BarDataSet set1 = new BarDataSet(values, "");
            set1.setColors(new int[]{R.color.correct_ans_color, R.color.wrong_ans_color},getApplicationContext());

            DecimalFormat mFormat = new DecimalFormat("###,###,###,##0");
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return  mFormat.format(value) ;
                }
            });

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new StackedFormatter(true, "", 0));
            data.setValueTextColor(Color.WHITE);
            data.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return  mFormat.format(value) ;
                }
            });
            data.setBarWidth(0.25f);*/
            //barchart.getAxisLeft().setAxisMinimum(0);


            BarDataSet set1 = new BarDataSet(ans_values, "");
            DecimalFormat mFormat = new DecimalFormat("###,###,###,##0");
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });

            BarDataSet set2 = new BarDataSet(wrong_values, "");
            set2.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });

            BarDataSet set3 = new BarDataSet(unatempt_values, "");
            set3.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });
            /*FE7B5C
            6b49c2
            *F8D59c
            *
            * */
            set1.setColors(new int[]{R.color.ANS_BAR_COLOR}, getApplicationContext());
            set2.setColors(new int[]{R.color.LOW_BAR_COLOR}, getApplicationContext());
            set3.setColors(new int[]{R.color.UNATTMP_BAR_COLOR}, getApplicationContext());

            float groupSpace = 0.3f;

            float barSpace = 0.03f;
            float barWidth = 0.35f;
            int groupCount = 3;

            BarData data = new BarData(set1, set2, set3);
            barchart.setData(data);
            barchart.getBarData().setBarWidth(barWidth);
            barchart.getLegend().setEnabled(false);
            barchart.getDescription().setEnabled(false);
            barchart.getAxisLeft().setDrawGridLines(false);
            barchart.getAxisRight().setDrawGridLines(false);
            barchart.getXAxis().setDrawGridLines(false);
            barchart.setContentDescription("");


            barchart.getXAxis().setAxisMinimum(0);
            barchart.getXAxis().setAxisMaximum(0 + barchart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
            barchart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping


            barchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis rightYAxis = barchart.getAxisRight();
            rightYAxis.setEnabled(false);
            XAxis xAxis = barchart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(true);
            //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //xAxis.setGranularityEnabled(true);

            final ArrayList<String> xVals = new ArrayList<>();
            xVals.add("");
            xVals.add("");
            xVals.add("");
            // Legend l = barchart.getLegend();
            //  int[]colors=new int[]{getResources().getColor(R.color.ANS_BAR_COLOR),getResources().getColor(R.color.LOW_BAR_COLOR),getResources().getColor(R.color.UNATTMP_BAR_COLOR)};

          /*  LegendEntry entry=new LegendEntry();
            entry.label="Correct";
            entry.formColor=getResources().getColor(R.color.ANS_BAR_COLOR);
            LegendEntry entry2=new LegendEntry();
            entry2.label="Wrong";
            entry2.formColor=getResources().getColor(R.color.LOW_BAR_COLOR);

            LegendEntry entry3=new LegendEntry();
            entry3.label="Unanswered";
            entry3.formColor=getResources().getColor(R.color.UNATTMP_BAR_COLOR);

            l.setExtra(colors,new String[]{});
            l.setCustom(new LegendEntry[]{entry,entry2,entry3});*/

            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
           /* xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    //Log.d("asa", "data X" + xVals);
                    return xVals.get((int) value % xVals.size());
                }
            });*/

            barchart.setPinchZoom(false);
            barchart.setTouchEnabled(false);


        } catch (JSONException jex) {
            jex.printStackTrace();
        }
    }

    private void sendRecomanded(JSONArray lectures) {

        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            final JSONObject fjson = new JSONObject();
            try {
                fjson.put(Constants.userId, userid);
                fjson.put("access_token", access_token);
                fjson.put("class_id", classid);
                fjson.put("subject_id", subjectId);
                fjson.put("section_id", section_id);
                fjson.put("mocktest_type", mock_test);
                fjson.put("lectures", lectures.toString());
                fjson.put(Constants.accessToken, access_token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tag_string_req = Constants.reqRegister;

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.QUIZ_ADD_RECOMANDED,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CustomDialog.closeDialog();
                            //Log.v("", "response : " + response);
                            try {
                                JSONObject jObj = new JSONObject(response);
                                //Log.d(Constants.response, response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    //CommonUtils.showToast(getApplicationContext(),"Feedback data added");
                                    // Toasty.success(FeedbackActivity.this, "FeedbackActivity data added", Toast.LENGTH_SHORT, true).show();
                                    // finish();
                                    String errorMsg = jObj.getString(Constants.message);
                                    try {
                                        MyDatabase databse = MyDatabase.getDatabase(getApplicationContext());
                                        for (int k = 0; k < lectures.length(); k++) {
                                            RecomandedModel model = new RecomandedModel();
                                            model.lecture_id = lectures.getJSONObject(k).getString("lecture_id");
                                            model.lecture_title = lectures.getJSONObject(k).getString("lecture_title");
                                            model.user_id = userid;
                                            model.class_id = classid;
                                            model.subject_id = subjectId;
                                            model.section_id = section_id;
                                            model.mocktest_type = mock_test;
                                            model.created_dtm = currentTime;
                                            model.sync = "Y";
                                            databse.recomandedDAO().insertRecomanded(model);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
                                    //Toasty.warning(FeedbackActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                                //Toasty.warning(FeedbackActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String msg = "";

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        msg = getResources().getString(R.string.error_1);
                    } else if (error instanceof AuthFailureError) {
                        msg = getResources().getString(R.string.error_2);
                    } else if (error instanceof ServerError) {
                        msg = getResources().getString(R.string.error_2);
                    } else if (error instanceof NetworkError) {
                        msg = getResources().getString(R.string.error_3);
                    } else if (error instanceof ParseError) {
                        msg = getResources().getString(R.string.error_4);
                    }


                    CommonUtils.showToast(getApplicationContext(), msg);
                    CustomDialog.closeDialog();
                    // Toasty.warning(FeedbackActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                    finish();

                }
            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }


                        cacheEntry.data = response.data;

                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                "UTF-8");
                        return Response.success((jsonString), cacheEntry);
                    } catch (Exception e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    super.deliverError(error);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }

                final String Key = AppConfig.ENC_KEY;
                final String message = fjson.toString();

                final String encryption = Security.encrypt(message, Key);


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfig.JSON_DATA, encryption);
                    return params;
                }
            };
            //Log.v("Request", "Request " + fjson.toString());
            //CustomDialog.showDialog(ge,true);
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            MyDatabase databse = MyDatabase.getDatabase(getApplicationContext());

            try {
                for (int k = 0; k < lectures.length(); k++) {
                    RecomandedModel model = new RecomandedModel();
                    model.lecture_id = lectures.getJSONObject(k).getString("lecture_id");
                    model.lecture_title = lectures.getJSONObject(k).getString("lecture_title");
                    model.user_id = userid;
                    model.class_id = classid;
                    model.subject_id = subjectId;
                    model.section_id = section_id;
                    model.mocktest_type = mock_test;
                    model.created_dtm = currentTime;
                    model.sync = "N";
                    databse.recomandedDAO().insertRecomanded(model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static HashMap<String, Float> sortByValue(HashMap<String, Float> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Float>> list =
                new LinkedList<Map.Entry<String, Float>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            public int compare(Map.Entry<String, Float> o1,
                               Map.Entry<String, Float> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public class StackedFormatter implements IValueFormatter {

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
            values = -1;
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
            values = values + 1;
            if (values % 2 != 0) {
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

    private void showResult(int score, int size) {
        alertDialog = new AlertDialog.Builder(QuizQuestionActivity.this, R.style.DialogTheme_notrans_quiz).create();
        alertDialog.setCancelable(true);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                quesIndex = 0;
                alertDialog.dismiss();
                finish();
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.quiz_result_window, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // int[] colorList = new int[]{Color.GREEN, Color.YELLOW, Color.RED};
        TextView txt_mocktest = dialogView.findViewById(R.id.txt_mocktest);
        if (ismock) {
            txt_mocktest.setVisibility(View.VISIBLE);
            txt_mocktest.setText(mock_test.replaceAll("M", getString(R.string.mock_tests) + " "));
        } else
            txt_mocktest.setVisibility(View.GONE);
        TextView txt_sectionname = dialogView.findViewById(R.id.txt_sectionname);
        TextView txt_chapter_name = dialogView.findViewById(R.id.txt_chapter_name);
        TextView txt_score = dialogView.findViewById(R.id.txt_score);
        TextView txt_score_total = dialogView.findViewById(R.id.txt_score_total);
        TextView txt_score_slash = dialogView.findViewById(R.id.txt_score_slash);
        //TextView txt_speed = dialogView.findViewById(R.id.txt_speed);
        TextView txt_accuracy = dialogView.findViewById(R.id.txt_accuracy);
        CircularProgressBarThumb progress_score = dialogView.findViewById(R.id.progress_score);
        CircularProgressBarThumb progress_time = dialogView.findViewById(R.id.progress_time);
        CircularProgressBarThumb progress_accuracy = dialogView.findViewById(R.id.progress_accuracy);
        // ProgressBar progress_speed =dialogView. findViewById(R.id.progress_speed);
        RelativeLayout rel_top_main = dialogView.findViewById(R.id.rel_top_main);
        TextView wrong = dialogView.findViewById(R.id.wrong);
        TextView correct = dialogView.findViewById(R.id.correct);
        TextView selected = dialogView.findViewById(R.id.selected);
        Button Review = dialogView.findViewById(R.id.Review);
        Button Quit = dialogView.findViewById(R.id.Quit);
        LinearLayout lnr_back = dialogView.findViewById(R.id.lnr_back);
        Button Retake = dialogView.findViewById(R.id.Retake);
        TextView txt_recomanded = dialogView.findViewById(R.id.txt_recomanded);
        LinearLayout lnr_recommand = dialogView.findViewById(R.id.lnr_recommand);
        LinearLayout lnr_recommand_items = dialogView.findViewById(R.id.lnr_recommand_items);
        ImageView img_status = dialogView.findViewById(R.id.img_status);
        CardView lnr_title_header = dialogView.findViewById(R.id.lnr_title_header);

        TextView time = dialogView.findViewById(R.id.time);

        Review.setBackgroundResource(background_drawable);
        Quit.setBackgroundResource(background_drawable);

        Retake.setBackgroundResource(background_drawable);
        txt_score.setTextColor(getResources().getColor(color_theme));
        // txt_score_slash.setBackgroundColor((color_theme));
        txt_score_total.setTextColor(getResources().getColor(color_theme));
        rel_top_main.setBackgroundResource(background_top_score_drawable);
        //  txt_sectionname.setTextColor(getResources().getColor(color_theme));
        // txt_mocktest.setTextColor(getResources().getColor(color_theme));

        lnr_title_header.setCardBackgroundColor(getResources().getColor(color_theme_card));


        RelativeLayout rel_top_main_anim = dialogView.findViewById(R.id.rel_top_main_anim);
        View lnr_alert_one = dialogView.findViewById(R.id.lnr_alert_one);
        View scrollview_alert = dialogView.findViewById(R.id.scrollview_alert);
        View lnr_back_two = dialogView.findViewById(R.id.lnr_back_two);
        Button btn_view_status = dialogView.findViewById(R.id.btn_view_status);
        TextView txt_quiz_msg = dialogView.findViewById(R.id.txt_quiz_msg);
        ImageView img_emoji = dialogView.findViewById(R.id.img_emoji);

        EditText edit_suggestion = dialogView.findViewById(R.id.edit_suggestion);
        BarChart barchart = dialogView.findViewById(R.id.barchart);
        txt_score_slash.setBackgroundResource(color_theme);
        txt_quiz_msg.setTextColor(getResources().getColor(color_theme));

        btn_view_status.setBackgroundResource(background_drawable);

        c = ((float) score / quizJson.length());
        c = c * 100;
        int audi0;

        lnr_back_two.setVisibility(View.INVISIBLE);
        rel_top_main_anim.setBackgroundResource(background_top_drawable);

        lnr_back_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                finish();
                // AppController.getInstance().playAudio(R.raw.button_click);
            }
        });

        btn_view_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_back.setVisibility(View.INVISIBLE);
                lnr_alert_one.setVisibility(View.GONE);
                scrollview_alert.setVisibility(View.VISIBLE);
                Animation animation;
                animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.anim_bottom_to_top);
                scrollview_alert.setAnimation(animation);

                String selectedAnswer = String.format("%02d", size);
                String unselected = String.valueOf(quizJson.length() - size);
                selected.setText((getString(R.string.attemped) + ": " + selectedAnswer));
                txt_recomanded.setTextColor(getResources().getColor(color_theme));
                txt_sectionname.setText(section_name);
                txt_chapter_name.setText(lecture_name);
                String correctAnswer = String.format("%02d", score);
                correct.setText(fromHtml(getString(R.string.correct_answeres) + ": " + correctAnswer));
                String wrongAnswer = String.format("%02d", wrongAnswers);
                wrong.setText(fromHtml(getString(R.string.wrong_answers) + ": " + wrongAnswer));


                if (ismock && !isActiveExpired) {
                    lnr_recommand.setVisibility(View.VISIBLE);
                    XAxis xAxis = barchart.getXAxis();
                    barchart.setVisibility(View.VISIBLE);
                    txt_chapter_name.setVisibility(View.GONE);
                    showRecommanded(lnr_recommand_items, barchart);
                    Resources res = getResources();
                    boolean isTablet = res.getBoolean(R.bool.isTablet);
                    if (!isTablet) {
                        xAxis.setTextSize(12);
                        barchart.getAxisLeft().setTextSize(12);
                        //barchart.getLegend().setTextSize(12);
                    } else {
                        xAxis.setTextSize(14);
                        barchart.getAxisLeft().setTextSize(14);
                        // barchart.getLegend().setTextSize(14);
                    }


                }
                progress_score.setProgressColor(getResources().getColor(color_theme));

                //progress_score.setMax(100);
                progress_score.setProgress(1);


                //progress_accuracy.setMax(100);
                progress_accuracy.setProgress((float) score / quizJson.length());
                //Log.v("Score ","Score "+(float)score/quizJson.length());
                //setAccuracy(pie_accuracy,c,w);

                float mnts = Minute + (float) Sec / 60;

                ProgressBarAnimation anim_score = new ProgressBarAnimation(progress_score, 0, progress_score.getProgress());
                anim_score.setDuration(1000);
                progress_score.startAnimation(anim_score);

                ProgressBarAnimation anim_acc = new ProgressBarAnimation(progress_accuracy, 0, progress_accuracy.getProgress());
                anim_acc.setDuration(1000);
                //progress_accuracy.startAnimation(anim_acc);


                float totalTimeInSec = Minute + ((float) Sec / 60);
                //float speed;
                //if(size==0)
                //    speed=0;
                //  else speed=(totalTimeInSec/size);

                // progress_time.setMax(TOTAL_TIME*60);
                progress_time.setProgress(((float) totalTimeInSec / TOTAL_TIME));

                ProgressBarAnimation anim_time = new ProgressBarAnimation(progress_time, 0, progress_time.getProgress());
                anim_time.setDuration(1000);
                // progress_time.startAnimation(anim_time);

              /*  progress_speed.setMax(10*100);
                try {
                    progress_speed.setProgress((int) (speed*100));
                }catch (Exception e)
                {
                    progress_speed.setProgress((int) (speed*100));
                }*/


                //  String timeTodis = String.format(format, Minute)  + String.format(format, Sec);

                txt_score.setText(score + "");
                txt_score_total.setText("" + quizJson.length());
                // txt_score_slash.setBackgroundResource(R.color.phy_background);
                time.setText((df.format(mnts) + "/" + TOTAL_TIME));
                //  txt_speed.setText(df.format(speed)+"/qtn.");
                txt_accuracy.setText((int) c + " %");

                if (!ismock) {
                    edit_suggestion.setVisibility(View.VISIBLE);
                    img_status.setVisibility(View.VISIBLE);

                }
                Review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        Intent i = new Intent(_this, ReviewQuiz.class);
                        i.putExtra(Constants.userId, userid);
                        i.putExtra("quiz_id", quiz_id);
                        i.putExtra("lecture_time", getDateTime());
                        i.putExtra("subject_id", subjectId);
                        i.putExtra("mock_test", mock_test);
                        try {
                            question.clearHistory();
                            question.clearFormData();

                            a0.clearHistory();
                            a0.clearFormData();

                            a1.clearHistory();
                            a1.clearFormData();

                            a2.clearHistory();
                            a2.clearFormData();

                            a3.clearHistory();
                            a3.clearFormData();
                        } catch (Exception e) {

                        }
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        AppController.getInstance().playAudio(R.raw.button_click);
                    }
                });
                lnr_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quesIndex = 0;
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        finish();
                        AppController.getInstance().playAudio(R.raw.button_click);
                    }
                });
                Quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quesIndex = 0;
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        setResult(200);
                        finish();
                        AppController.getInstance().playAudio(R.raw.button_click);
                    }
                });
                Retake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quesIndex = 0;
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        finish();
                        AppController.getInstance().playAudio(R.raw.button_click);
                    }
                });


            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = alertDialog.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subjectId) {
                case "1":
                    window.setStatusBarColor(getResources().getColor(R.color.phy_background_status));
                    break;
                case "2":
                    window.setStatusBarColor(getResources().getColor(R.color.che_background_status));
                    break;
                case "3":
                    window.setStatusBarColor(getResources().getColor(R.color.bio_background_status));
                    break;
                case "4":
                    window.setStatusBarColor(getResources().getColor(R.color.math_background_status));
                    break;
            }
        }


        if (c >= 90) {
            edit_suggestion.setText(getString(R.string.nighty_percent));
            txt_quiz_msg.setText(getString(R.string.nighty_percent));
            img_status.setImageResource(R.drawable.ic_smily_90);
            audi0 = R.raw.qz_6;
            Glide.with(getApplicationContext()).load(R.drawable.q5).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);

        } else if (c >= 80) {
            txt_quiz_msg.setText(getString(R.string.eight_percent));
            edit_suggestion.setText(getString(R.string.eight_percent));
            img_status.setImageResource(R.drawable.ic_smily_80);
            audi0 = R.raw.qz_5;
            Glide.with(getApplicationContext()).load(R.drawable.q4).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else if (c >= 60) {
            txt_quiz_msg.setText(getString(R.string.sixty_percent));
            edit_suggestion.setText(getString(R.string.sixty_percent));
            img_status.setImageResource(R.drawable.ic_smily_60);
            audi0 = R.raw.qz_4;
            Glide.with(getApplicationContext()).load(R.drawable.q3).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else if (c >= 40) {
            txt_quiz_msg.setText(getString(R.string.fourty_percent));
            edit_suggestion.setText(getString(R.string.fourty_percent));
            img_status.setImageResource(R.drawable.ic_smily_40);
            audi0 = R.raw.qz_3;
            Glide.with(getApplicationContext()).load(R.drawable.q2).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else if (c >= 20) {
            txt_quiz_msg.setText(getString(R.string.twenty_percent));
            edit_suggestion.setText(getString(R.string.twenty_percent));
            img_status.setImageResource(R.drawable.ic_smily_0);
            audi0 = R.raw.qz_2;
            Glide.with(getApplicationContext()).load(R.drawable.q1).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else {
            txt_quiz_msg.setText(getString(R.string.zero_percent));
            edit_suggestion.setText(getString(R.string.zero_percent));
            img_status.setImageResource(R.drawable.ic_smily_0);
            audi0 = R.raw.qz_1;
            Glide.with(getApplicationContext()).load(R.drawable.q1).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        }


        lnr_recommand.setVisibility(View.GONE);
        barchart.setVisibility(View.GONE);
        barchart.setClickable(false);


        alertDialog.setView(dialogView);
        alertDialog.show();
       /* ScrollView.LayoutParams params = (ScrollView.LayoutParams) dialogView.getLayoutParams();
        params.height = getResources().getDisplayMetrics().heightPixels;
        params.width = getResources().getDisplayMetrics().widthPixels;
        dialogView.setLayoutParams(params);*/


        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_bottom_to_top);
        lnr_alert_one.setAnimation(animation);
        AppController.getInstance().playQuizAudio(audi0);

    }

    GestureDetectorCompat mGestureDetector;
    CustomGestureDetector customGestureDetector = new CustomGestureDetector();

    private void setGesture() {
        lnr_swipe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.v("OnFlling", "OnFlling Touch" );
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        lnr_touch_right.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if (quesIndex != quizJson.length() - 1)
                    Next.performClick();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

            }
        });

        lnr_touch_left.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                if (quesIndex != 0)
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
                if (quesIndex != quizJson.length() - 1)
                    Next.performClick();

            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                if (quesIndex != 0)
                    Prev.performClick();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCvCountdownView != null && mCvCountdownView.getRemainTime() > 0)
            mCvCountdownView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCvCountdownView != null && mCvCountdownView.getRemainTime() > 0)
            mCvCountdownView.restart();
    }
}
