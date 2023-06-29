package com.tutorix.tutorialspoint.testseries;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.testseries.data.QuestionOption;
import com.tutorix.tutorialspoint.testseries.data.TestQuestions;
import com.tutorix.tutorialspoint.testseries.data.TestSeriesJson;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.ProgressBarAnimation;
import com.tutorix.tutorialspoint.views.CircularProgressBarThumb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

public class TestQuizActivity extends AppCompatActivity {

    @BindView(R.id.radio_phy)
    RadioButton radioPhy;
    @BindView(R.id.radio_che)
    RadioButton radioChe;
    @BindView(R.id.radio_math)
    RadioButton radioMath;
    @BindView(R.id.radio_bio)
    RadioButton radioBio;
    @BindView(R.id.radio_sub_group)
    RadioGroup radioSubGroup;


    @BindView(R.id.question)
    WebView question;
    @BindView(R.id.a0)
    WebView a0;
    @BindView(R.id.a1)
    WebView a1;
    @BindView(R.id.a2)
    WebView a2;
    @BindView(R.id.a3)
    WebView a3;
    @BindView(R.id.cv_countdownViewTest1)
    CountdownView mCvCountdownView;

    @BindView(R.id.lnr_4)
    FrameLayout lnr_4;
    @BindView(R.id.lnr_3)
    FrameLayout lnr_3;
    @BindView(R.id.lnr_2)
    FrameLayout lnr_2;
    @BindView(R.id.lnr_1)
    FrameLayout lnr_1;
    @BindView(R.id.edit_write)
    EditText edit_write;

    @BindView(R.id.radio_1)
    RadioButton radio_1;
    @BindView(R.id.radio_2) RadioButton radio_2;
    @BindView(R.id.radio_3) RadioButton radio_3;
    @BindView(R.id.radio_4) RadioButton radio_4;
    @BindView(R.id.Next)    Button Next;
    @BindView(R.id.Prev)    Button Prev;
    @BindView(R.id.btn_clear_review)    Button btn_clear_review;
    @BindView(R.id.btn_mark_review)    Button btn_mark_review;
    @BindView(R.id.btn_pause_pay)    Button btn_pause_pay;
    @BindView(R.id.txt_show_q_type)    TextView txt_show_q_type;

    int background_drawable=R.drawable.test_btn_all;
    int background_top_drawable=R.drawable.top_main_background_small;
    int background_top_score_drawable=R.drawable.score_board;
    int color_theme;
    int color_theme_card;
    int color_theme_selected;
    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;
    private long mLastClickTime = 0;

    private  int quesIndex = 0;
    private  boolean isPlay = true;


    String mathLib;
    String mathLib0="";
    String mathLib1="";
    String mathLib2="";
    String mathLib3="";
    String mathLib4="";

    //JSONArray quizJson;
    private int[] selected = null;
    private int[] correctAns = null;
    private View quiz;
    int click_next_previous;

    int SUBJECT_SELECTED=1;
    int currrentClsId;
    public int PHYSICS_COUNT;
    public int CHEMISTRY_COUNT;
    public int OTHER_COUNT;
    public boolean isFromreview=false;
    public ArrayList<TestQuestions>listQuestions=new ArrayList<>();
    TestSeriesJson testSeriesJson;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    String test_series_type;
    String test_series_name;
    String test_series_file_name;
    TestQuizActivity activity;
    int max_phy_options;
    int max_che_options;
    int max_other_options;
    String BaseURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_quiz);
        ButterKnife.bind(this);

        color_theme=R.color.colorPrimary;
        color_theme_card=R.color.colorPrimary;
        color_theme_selected=R.color.colorPrimary;
        Date date = new Date();
        start = dateFormat.format(date);
        String[]userInfo= SessionManager.getUserInfo(this);
        access_token = userInfo[1];
        userid = userInfo[0];;
        loginType = userInfo[2];
        class_id = userInfo[4];
        quiz = findViewById(R.id.quiz);
        String assets= "file:///android_asset";
        mathLib=Constants.MathJax_Offline;
        activity=this;
        Intent in=getIntent();
        if(in==null)
        {
            finish();
        }
        try {
            currrentClsId = Integer.parseInt(class_id);
            if (currrentClsId <=7) {

            } else if (currrentClsId ==8) {
                radioBio.setVisibility(View.GONE);
            } else if (currrentClsId ==9){
                radioMath.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        test_series_type=in.getStringExtra("test_series_type");
        test_series_name=in.getStringExtra("test_series_name");
        test_series_file_name=in.getStringExtra("test_series_file_name");
        basePath = AppConfig.getOnlineURLImage(class_id)+"test-series/";
       /* if (currrentClsId ==9){
            basePath = "https://origin.tutorix.com/dev/neet/test-series/";
        }else if(currrentClsId ==8)
        basePath = "https://origin.tutorix.com/dev/iit-jee/test-series/";*/

        BaseURL=basePath+test_series_file_name+".json";
       // BaseURL="https://origin.tutorix.com/dev/neet/test-series/neet_test.json";

        radioPhy.setChecked(true);
        question.getSettings().setAllowFileAccess(true);
        question.getSettings().setJavaScriptEnabled(true);
        //question.getSettings().setBuiltInZoomControls(true);
        question.getSettings().setDisplayZoomControls(false);
        question.setBackgroundColor(0);

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


        fetchJSON();
        radioSubGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isFromreview=false;
                switch (checkedId)
                {

                    case R.id.radio_phy:
                        quesIndex=0;
                        SUBJECT_SELECTED=1;
                        loadFirstQuestion(quesIndex);

                        break;
                    case R.id.radio_che:
                        quesIndex=PHYSICS_COUNT;
                        SUBJECT_SELECTED=2;
                        loadFirstQuestion(quesIndex);
                        break;
                    case R.id.radio_math:
                        quesIndex=PHYSICS_COUNT+CHEMISTRY_COUNT;
                        SUBJECT_SELECTED=4;
                        loadFirstQuestion(quesIndex);
                        break;
                    case R.id.radio_bio:
                        quesIndex=PHYSICS_COUNT+CHEMISTRY_COUNT;
                        SUBJECT_SELECTED=3;
                        loadFirstQuestion(quesIndex);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.lnr_back,R.id.btn_clear_review,R.id.btn_mark_review,R.id.Next,R.id.Prev,R.id.btn_pause_pay,R.id.img_review})
    public void onViewClicked(View view) {

        switch (view.getId())
        {
            case R.id.lnr_back:
                exitTest(true);
                break;
            case R.id.img_review:


                if(listQuestions.get(quesIndex).question_type.equalsIgnoreCase("O")&&(currrentClsId==8))
                {
                    String s=edit_write.getText().toString().trim();
                    if(s.length()>0)
                    {
                        selected[quesIndex] = 5;
                        if(checkQtnLimit())
                        {
                            CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                            break;
                        }

                        storeQuestionAnswer(s.toString().trim());
                        listQuestions.get(quesIndex).status=2;
                    }else
                    {
                        selected[quesIndex] = -1;
                        storeQuestionAnswer(s.toString().trim());
                        listQuestions.get(quesIndex).status=1;
                    }
                }

                QuestionsReviewFragment questionsReviewFragment =
                        QuestionsReviewFragment.newInstance(SUBJECT_SELECTED);
                questionsReviewFragment.show(getSupportFragmentManager(),
                        "review");
                break;
            case R.id.btn_clear_review:
                correctAns[quesIndex] = -1;
                selected[quesIndex]=-1;
                listQuestions.get(quesIndex).status=1;
                radio_1.setChecked(false);
                radio_2.setChecked(false);
                radio_3.setChecked(false);
                radio_4.setChecked(false);
                a0.setBackgroundColor(0);
                a1.setBackgroundColor(0);
                a2.setBackgroundColor(0);
                a3.setBackgroundColor(0);
                if(listQuestions.get(quesIndex).question_type.equalsIgnoreCase("O")&&(currrentClsId==8))
                {
                    edit_write.setText("");
                    storeQuestionAnswer("");
                    //listQuestions.get(quesIndex).status=-1;
                }else
                storeQuestionAnswer("",-1,true);
                /*if(listQuestions.get(quesIndex).status==4)
                    listQuestions.get(quesIndex).status=2;
                else
                    listQuestions.get(quesIndex).status=1;
                btn_mark_review.setVisibility(View.VISIBLE);*/
                // btn_clear_review.setVisibility(View.GONE);
                break;
            case R.id.btn_mark_review:
                /*if(listQuestions.get(quesIndex).status==3||listQuestions.get(quesIndex).status==4)
                {
                    btn_mark_review.setText("Mark For Review");
                    if(listQuestions.get(quesIndex).status==4)
                        listQuestions.get(quesIndex).status=2;
                    else
                        listQuestions.get(quesIndex).status=1;
                    return;
                }
                */
                if(listQuestions.get(quesIndex).status==2||listQuestions.get(quesIndex).status==4)
                    listQuestions.get(quesIndex).status=4;
                else
                    listQuestions.get(quesIndex).status=3;
                if(listQuestions.get(quesIndex).question_type.equalsIgnoreCase("O")&&(currrentClsId==8))
                {
                    String s=edit_write.getText().toString().trim();
                    if(s.length()>0)
                    {
                        if(checkQtnLimit())
                        {
                            CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                            break;
                        }
                        selected[quesIndex] = 5;
                        storeQuestionAnswer(s.toString().trim());

                            listQuestions.get(quesIndex).status=4;
                    }else
                    {
                        selected[quesIndex] = -1;
                        storeQuestionAnswer(s.toString().trim());

                            listQuestions.get(quesIndex).status=3;
                    }
                }

                // btn_clear_review.setVisibility(View.VISIBLE);
                break;
            case R.id.Next:



                if(isFromreview)
                {
                    if(listQuestions.get(quesIndex).status==4||listQuestions.get(quesIndex).status==2)
                        listQuestions.get(quesIndex).status=2;
                    else
                        listQuestions.get(quesIndex).status=1;
                }
                if(listQuestions.get(quesIndex).question_type.equalsIgnoreCase("O")&&(currrentClsId==8))
                {
                    String s=edit_write.getText().toString().trim();
                     if(s.length()>0)
                        {
                            if(checkQtnLimit())
                            {
                                CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                                break;
                            }
                            selected[quesIndex] = 5;
                            int status=listQuestions.get(quesIndex).status;
                            storeQuestionAnswer(s.toString().trim());

                            if(status==4){
                                if(isFromreview)
                                    listQuestions.get(quesIndex).status=2;
                                else
                                    listQuestions.get(quesIndex).status=4;
                            }else
                                listQuestions.get(quesIndex).status=2;

                        }else
                        {
                            selected[quesIndex] = -1;
                            storeQuestionAnswer(s.toString().trim());
                            if(listQuestions.get(quesIndex).status!=3)
                            listQuestions.get(quesIndex).status=1;
                        }
                }

                click_next_previous=1;
                quesIndex=quesIndex+1;
                a0.setBackgroundColor(0);
                a1.setBackgroundColor(0);
                a2.setBackgroundColor(0);
                a3.setBackgroundColor(0);
                if(quesIndex==0)
                {
                    SUBJECT_SELECTED=1;
                    radioPhy.setChecked(true);
                }else
                if(quesIndex==PHYSICS_COUNT)
                {
                    SUBJECT_SELECTED=2;
                    radioChe.setChecked(true);
                }else if(quesIndex==PHYSICS_COUNT+CHEMISTRY_COUNT)
                {
                    if (currrentClsId <=7) {

                    } else if (currrentClsId ==8) {
                        SUBJECT_SELECTED=4;
                        radioMath.setChecked(true);
                    } else if (currrentClsId ==9){
                        radioBio.setChecked(true);
                        SUBJECT_SELECTED=3;

                    }


                }
                loadFirstQuestion(quesIndex);
                isFromreview=false;
                break;
            case R.id.Prev:


                if(listQuestions.get(quesIndex).question_type.equalsIgnoreCase("O")&&(currrentClsId==8))
                {
                    String s=edit_write.getText().toString().trim();
                    if(s.length()>0)
                    {
                        if(checkQtnLimit())
                        {
                            CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                            return;
                        }
                        selected[quesIndex] = 5;
                        storeQuestionAnswer(s.toString().trim());
                        listQuestions.get(quesIndex).status=2;
                    }else
                    {
                        selected[quesIndex] = -1;
                        storeQuestionAnswer(s.toString().trim());
                        listQuestions.get(quesIndex).status=1;
                    }
                }
                click_next_previous=0;
                quesIndex=quesIndex-1;
                a0.setBackgroundColor(0);
                a1.setBackgroundColor(0);
                a2.setBackgroundColor(0);
                a3.setBackgroundColor(0);
                if(quesIndex==PHYSICS_COUNT-1)
                {
                    SUBJECT_SELECTED=1;
                    //radioPhy.setChecked(true);
                }else
                if(quesIndex==(PHYSICS_COUNT+CHEMISTRY_COUNT)-1)
                {
                    SUBJECT_SELECTED=2;
                    //radioChe.setChecked(true);
                }else if(quesIndex==PHYSICS_COUNT+CHEMISTRY_COUNT)
                {
                    if (currrentClsId <=7) {

                    } else if (currrentClsId ==8) {
                        SUBJECT_SELECTED=4;
                        //radioMath.setChecked(true);
                    } else if (currrentClsId ==9){
                        //radioBio.setChecked(true);
                        SUBJECT_SELECTED=3;

                    }


                }
                loadFirstQuestion(quesIndex);
                isFromreview=false;

                break;
            case R.id.btn_pause_pay:
                if(isPlay)
                {
                    isPlay=false;
                    mCvCountdownView.pause();
                    btn_pause_pay.setText("Resume");
                    //  btn_pause_pay.setBackgroundColor(Color.RED);
                    TestPauseFragment testPauseFragment =
                            TestPauseFragment.newInstance();
                    testPauseFragment.show(getSupportFragmentManager(),
                            "review");
                }else
                {
                    isPlay=true;
                    btn_pause_pay.setText(getString(R.string.pause));
                    // btn_pause_pay.setBackgroundColor(Color.GREEN);
                    mCvCountdownView.restart();
                }
                break;
        }
    }


    public void onPlayQuiz()
    {
        isPlay=true;
        btn_pause_pay.setText("Pause");
        // btn_pause_pay.setBackgroundColor(Color.GREEN);
        mCvCountdownView.restart();
    }
    Dialog dialog;
    public void continueTest()
    {

    }
    public void exitTest(boolean isExit)
    {

        dialog=new Dialog(TestQuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert);
        TextView txt_msg=dialog.findViewById(R.id.txt_msg);
        txt_msg.setText(getString(R.string.you_want_exit));
        TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok=dialog.findViewById(R.id.txt_ok);
        txt_ok.setText(getString(R.string.yes));
        dialog.setCanceledOnTouchOutside(false);
        //txt_ok.setBackgroundResource(color_theme);
        txt_cancel.setVisibility(View.VISIBLE);
        txt_cancel.setText(getString(R.string.no));
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                if(!isExit)
                    onPlayQuiz();
                dialog.dismiss();
            }
        });
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                dialog.dismiss();
                if(!isExit)
                    SubmitButton(true);
                else finish();

            }
        });
        dialog.show();
    }


    int MINUTE;
    int HOUR;
    int SECOUNDS;
    int TOTAL_TIME=3*60*60;
    String currentTime;
    int total_marks=0;
    private void SubmitToServer(final String timeTaken, final int score, final int wrongAnswers,final int attempted,boolean showResult) {
        currentTime = String.valueOf(System.currentTimeMillis());


        Gson gson=new Gson();


        final JSONObject fjson = new JSONObject();
        try {

            int all_correct=hm_phy.size()+hm_che.size()+hm_other.size();

            testSeriesJson.physics.total_gain_marks=(hm_phy.size()*4)-(hm_wrong_phy.size());
            testSeriesJson.chemistry.total_gain_marks=(hm_che.size()*4)-(hm_wrong_che.size());
            int all_wrong=hm_wrong_phy.size()+hm_wrong_che.size()+hm_wrong_other.size();
           total_marks=0;
            if(test_series_type.contains("J"))
            {
                if(all_correct>testSeriesJson.max_questions_attempt)
                {
                    total_marks=testSeriesJson.max_questions_attempt*4;
                }else
                {
                    total_marks=all_correct*4;
                }

                total_marks=total_marks-all_wrong;

                testSeriesJson.mathematics.total_gain_marks=(hm_other.size()*4)-(hm_wrong_other.size());
            }else
            {

                if(all_correct>testSeriesJson.max_questions_attempt)
                {
                    total_marks=testSeriesJson.max_questions_attempt*4;
                }else
                {
                    total_marks=all_correct*4;
                }

                total_marks=total_marks-all_wrong;
                testSeriesJson.biology.total_gain_marks=(hm_other.size()*4)-(hm_wrong_other.size());
            }

            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.userId, userid);
            fjson.put("test_series_name", test_series_name);
            fjson.put("test_series_type", test_series_type);
            fjson.put("quiz_duration", timeTaken);
            fjson.put("total_marks", total_marks);
            fjson.put("total_correct", hm_phy.size()+hm_che.size()+hm_other.size());
            fjson.put("attempted_questions", attempted);


            fjson.put(Constants.accessToken, access_token);

            fjson.put("total_questions", String.valueOf(PHYSICS_COUNT+CHEMISTRY_COUNT+OTHER_COUNT));
            //fjson.put("attempted_questions", String.valueOf(map.size()));


            testSeriesJson.physics.total_correct =hm_phy.size();
            testSeriesJson.physics.total_wrong =testSeriesJson.physics.attempted_questions-testSeriesJson.physics.total_correct;
            testSeriesJson.chemistry.total_correct =hm_che.size();
            testSeriesJson.chemistry.total_wrong =testSeriesJson.chemistry.attempted_questions-testSeriesJson.chemistry.total_correct;



            if(currrentClsId==8)
            {
                testSeriesJson.mathematics.total_correct =hm_other.size();
                testSeriesJson.mathematics.total_wrong =testSeriesJson.mathematics.attempted_questions-testSeriesJson.mathematics.total_correct;
                fjson.put("total_wrong", testSeriesJson.physics.total_wrong +testSeriesJson.chemistry.total_wrong +testSeriesJson.mathematics.total_wrong);
            }

            else if(currrentClsId==9) {
                testSeriesJson.biology.total_correct = hm_other.size();
                testSeriesJson.biology.total_wrong =testSeriesJson.biology.attempted_questions-testSeriesJson.biology.total_correct;
                fjson.put("total_wrong", testSeriesJson.physics.total_wrong +testSeriesJson.chemistry.total_wrong +testSeriesJson.biology.total_wrong);

            }

/*
            JsonElement element = gson.toJsonTree(listQuestions, new TypeToken<List<TestQuestions>>() {}.getType());

            JsonArray jsonArray = element.getAsJsonArray();
            JSONObject questionsObj = new JSONObject();
            questionsObj.put("questions", new JSONArray((gson.toJson(listQuestions))));


            quizObj.put("quiz", questionsObj);*/
               /* JSONObject hm=new JSONObject();
            hm.put("quiz",gson.toJsonTree(testSeriesJson,TestSeriesJson.class));*/
            //String quizJson=gson.toJson(testSeriesJson);

            fjson.put("quiz_json", gson.toJson(testSeriesJson));

            Log.v("Request","Request "+fjson.toString());
            Log.v("Request","Request ");
            String tag_string_req = Constants.reqRegister;

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SUBMIT_TEST_SERIES,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            CustomDialog.closeDialog();
                            Log.v("Response","Response "+response);
                            try {


                                JSONObject jObj = new JSONObject(response);

                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if(!error)
                                {
                                    String test_series_id=jObj.getString("test_series_id");
                                    if(showResult)
                                        showResult(total_marks,correctAnswers,attempted,test_series_id);
                                    else
                                    {

                                        Intent in=new Intent(TestQuizActivity.this,TestSeriesReviewActivity.class);
                                        in.putExtra("test_series_id",test_series_id);
                                        startActivity(in);
                                        finish();
                                    }

                                }else
                                {
                                    CommonUtils.showToast(getApplicationContext(), "Please try again");
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
                    CustomDialog.closeDialog();
                    CommonUtils.showToast(getApplicationContext(), msg);
                    //finish();
                }
            }) {
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
            CustomDialog.showDialog(TestQuizActivity.this,true);
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        exitTest(true);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCvCountdownView.stop();
        mCvCountdownView=null;
    }

    int which;
    String quesValue;
    String basePath="";
    Animation anim;

    private void loadFirstQuestion(final int quesIndex) {
        txt_show_q_type.setText("MCQ");
        try {
            which=0;
            Log.v("listQuestions ","listQuestions "+listQuestions.size());
            if(listQuestions.size()<=quesIndex)
            {
                return;
            }
            TestQuestions aQues = listQuestions.get(quesIndex);
            if(aQues.status==0)
            {
                listQuestions.get(quesIndex).status=1;
            }
            quesValue =("Q"+(quesIndex+1)+")")+mathLib+aQues.question+mathLib0;
            //quesValue =aQues.getString("question");
            final String question_id = aQues.question_id+"";



            question.loadDataWithBaseURL(basePath,  getStyle()/*+"<font color='#F79646'>Q " +(quesIndex + 1 )+")</font> "*/+ quesValue, "text/html", "utf-8", "");

            //question.setBackgroundColor(Color.parseColor("#eeeeee"));
            if (correctAns[quesIndex] == -1) {
                correctAns[quesIndex] = aQues.option_right;
            }
            edit_write.setVisibility(View.GONE);
            lnr_1.setVisibility(View.GONE);
            lnr_2.setVisibility(View.GONE);
            lnr_3.setVisibility(View.GONE);
            lnr_4.setVisibility(View.GONE);

            radio_1.setChecked(false);
            radio_2.setChecked(false);
            radio_3.setChecked(false);
            radio_4.setChecked(false);

            if(listQuestions.get(quesIndex).question_type.equalsIgnoreCase("O"))
                txt_show_q_type.setText("Numeric");
            if(listQuestions.get(quesIndex).question_type.equalsIgnoreCase("O")&&(currrentClsId==8))
            {
                edit_write.setVisibility(View.VISIBLE);
                edit_write.setText("");
                edit_write.requestFocus();
               /* edit_write.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });*/

                if(selected[quesIndex] ==5)
                {
                    if(quesIndex<PHYSICS_COUNT)
                    {
                        edit_write.setText(testSeriesJson.physics.questions.get(quesIndex).my_answer);
                    }
                    else  if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT)) {
                        edit_write.setText(testSeriesJson.chemistry.questions.get(quesIndex-(PHYSICS_COUNT)).my_answer);
                    }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT+OTHER_COUNT)) {
                        edit_write.setText(testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).my_answer);
                    }

                }

            }else
            {


            for (int i = 0; i <  aQues.options.size(); i++) {
                QuestionOption qOptions= aQues.options.get(i);

                String aAns;


                try {

                    if(qOptions.option_1!=null) {
                        lnr_1.setVisibility(View.VISIBLE);
                        aAns = mathLib + qOptions.option_1 + mathLib1;
                        a0.loadDataWithBaseURL(basePath, getIntialTable("A", aAns), "text/html", "utf-8", "");
                    }
                    if(qOptions.option_2!=null) {
                        lnr_2.setVisibility(View.VISIBLE);
                        aAns = mathLib +qOptions.option_2 + mathLib2;
                        a1.loadDataWithBaseURL(basePath, getIntialTable("B", aAns), "text/html", "utf-8", "");

                    }
                    if(qOptions.option_3!=null) {
                        lnr_3.setVisibility(View.VISIBLE);
                        aAns = mathLib + qOptions.option_3+ mathLib3;
                        a2.loadDataWithBaseURL(basePath, getIntialTable("C", aAns), "text/html", "utf-8", "");

                    }
                    if(qOptions.option_4!=null) {
                        lnr_4.setVisibility(View.VISIBLE);
                        aAns = mathLib + qOptions.option_4 + mathLib4;
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
                                    if(checkQtnLimit())
                                    {
                                        CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                                        break;
                                    }
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
                                    storeQuestionAnswer(question_id, 1,false);
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
                                    if(checkQtnLimit())
                                    {
                                        CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                                        break;
                                    }
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
                                    storeQuestionAnswer(question_id, 2,false);
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
                                    if(checkQtnLimit())
                                    {
                                        CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                                        break;
                                    }
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
                                    storeQuestionAnswer(question_id, 3,false);
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

                                    if(checkQtnLimit())
                                    {
                                        CommonUtils.showToast(getApplicationContext(),"You have answered max questions");
                                        break;
                                    }
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
                                    storeQuestionAnswer(question_id, 4,false);
                                    AppController.getInstance().playQuizAudio(R.raw.quiz_btn);
                                    break;
                                }
                            }
                            return true;

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            }




            if (quesIndex == listQuestions.size() - 1) {
                Next.setEnabled(false);
                Next.setTextColor(Color.parseColor("#D2D2D2"));
                //Next.setBackgroundResource(R.drawable.button_un_select);

            }else
            {
                Next.setEnabled(true);
            }



            Prev.setEnabled(false);
            if (quesIndex > 0) {
                Prev.setEnabled(true);
                //Prev.setBackgroundResource(background_drawable);
            }





        } catch (Exception e) {
            e.printStackTrace();
        }
        quiz.clearAnimation();
        if(click_next_previous==0)
            anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_left_to_right);
        else anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fall_down_slow);
        quiz.setAnimation(anim);
        anim.start();

        AppController.getInstance().playQuizAudio(R.raw.qz_next);





    }



    private void fetchJSON()
    {
        String tag_string_req = Constants.reqRegister;
        //loadingPanelID.show();
        if(activity==null)
            return;



        StringRequest strReq = new StringRequest(Request.Method.GET,
                BaseURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            // loadingPanelID.hide();
                            try {
                                CustomDialog.closeDialog();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            JSONObject obj = new JSONObject(response);
                            Gson gson=new Gson();
                            testSeriesJson=gson.fromJson(obj.getJSONObject("test_series").toString(),TestSeriesJson.class);


                            listQuestions.clear();
                            try{

                                max_phy_options=testSeriesJson.physics.max_optional_attempt;
                                max_che_options=testSeriesJson.chemistry.max_optional_attempt;


                                PHYSICS_COUNT=testSeriesJson.physics.total_questions;
                                CHEMISTRY_COUNT=testSeriesJson.chemistry.total_questions;
                                listQuestions.addAll(testSeriesJson.physics.questions);
                                if(testSeriesJson.chemistry.questions.size()>0)
                                    listQuestions.addAll(testSeriesJson.chemistry.questions);
                                else radioChe.setVisibility(View.GONE);
                                if(currrentClsId==8)
                                {
                                    max_other_options=testSeriesJson.mathematics.max_optional_attempt;

                                    OTHER_COUNT=testSeriesJson.mathematics.total_questions;
                                    listQuestions.addAll(testSeriesJson.mathematics.questions);
                                }else if(currrentClsId==9)
                                {
                                    max_other_options=testSeriesJson.biology.max_optional_attempt;
                                    OTHER_COUNT=testSeriesJson.biology.total_questions;
                                    listQuestions.addAll(testSeriesJson.biology.questions);
                                }


                                selected = new int[listQuestions.size()];
                                java.util.Arrays.fill(selected, -1);
                                correctAns = new int[listQuestions.size()];
                                java.util.Arrays.fill(correctAns, -1);



                                loadFirstQuestion(quesIndex);
                                isFromreview=false;

                                mCvCountdownView.start(1000*60*60*3);
                                mCvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                                    @Override
                                    public void onEnd(CountdownView cv) {

                                        SubmitButton(true);
                                    }
                                });
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                //loadingPanelID.hide();
                try {
                    CustomDialog.closeDialog();
                }catch (Exception e)
                {

                }
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = activity.getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = activity.getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = activity.getResources().getString(R.string.error_4);
                }

                // Toasty.warning(SubjectActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

                finish();
            }
        })


                ;
        CustomDialog.showDialog(activity, true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    HashMap<Integer,String>hm_phy=new HashMap<Integer, String>();
    HashMap<Integer,String>hm_che=new HashMap<Integer, String>();
    HashMap<Integer,String>hm_other=new HashMap<Integer, String>();

    Set<Integer> hm_wrong_other=new HashSet<>();
    Set<Integer> hm_wrong_phy=new HashSet<>();
    Set<Integer> hm_wrong_che=new HashSet<>();

    Set<Integer> hm_limit_other=new HashSet<>();
    Set<Integer> hm_limit_phy=new HashSet<>();
    Set<Integer> hm_limit_che=new HashSet<>();
    private boolean checkQtnLimit()
    {
        if(quesIndex<PHYSICS_COUNT) {
            if (testSeriesJson.physics.questions.get(quesIndex).question_type.equalsIgnoreCase("O")) {
                if (hm_limit_phy.size() >= max_phy_options&& !(hm_limit_phy.contains(testSeriesJson.physics.questions.get(quesIndex).question_id))) {
                    return true;
                }
            }
        }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT)){
            if (testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_type.equalsIgnoreCase("O")) {
                if (hm_limit_che.size() >= max_che_options&& !(hm_limit_che.contains(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id))) {
                    return true;
                }
            }
        }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT+OTHER_COUNT))
        {
            if(currrentClsId==8)
            {
                if (testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_type.equalsIgnoreCase("O")) {
                    if (hm_limit_other.size() >= max_other_options&& !(hm_limit_other.contains(testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id))) {
                        return true;
                    }
                }
            }else
            {
                if (testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_type.equalsIgnoreCase("O")) {
                    if (hm_limit_other.size() >= max_other_options&& !(hm_limit_other.contains(testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id))) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
    private void storeQuestionAnswer(String ans)
    {
        if(ans.length()>0)
        {

            if(quesIndex<PHYSICS_COUNT)
            {
                if (testSeriesJson.physics.questions.get(quesIndex).question_type.equalsIgnoreCase("O")) {
                    hm_limit_phy.add(testSeriesJson.physics.questions.get(quesIndex).question_id);
                }
                testSeriesJson.physics.questions.get(quesIndex ).my_answer = ans;


                if (testSeriesJson.physics.questions.get(quesIndex ).my_answer.equalsIgnoreCase(testSeriesJson.physics.questions.get(quesIndex ).answer))
                    hm_phy.put(testSeriesJson.physics.questions.get(quesIndex).question_id, testSeriesJson.physics.questions.get(quesIndex ).my_answer);
                /*else
                {
                    hm_wrong_phy.add(testSeriesJson.physics.questions.get(quesIndex).question_id);
                }*/
                if (testSeriesJson.physics.questions.get(quesIndex ).option_selected == -1) {
                    testSeriesJson.physics.attempted_questions = testSeriesJson.physics.attempted_questions + 1;
                }

                testSeriesJson.physics.questions.get(quesIndex).option_selected = 5;
                listQuestions.get(quesIndex).status = 2;
                listQuestions.get(quesIndex).option_selected = 5;

             }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT))
            {
                if (testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_type.equalsIgnoreCase("O")) {
                    hm_limit_che.add(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id);
                }
                testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).my_answer = ans;
                if (testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).my_answer.equalsIgnoreCase(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).answer))
                    hm_che.put(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id, testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).my_answer);
                /*else
                {
                    hm_wrong_che.add(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id);
                }*/
                if (testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT)).option_selected == -1) {
                    testSeriesJson.chemistry.attempted_questions = testSeriesJson.chemistry.attempted_questions + 1;
                }

                testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT)).option_selected = 5;
                listQuestions.get(quesIndex).status = 2;
                listQuestions.get(quesIndex).option_selected = 5;

            }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT+OTHER_COUNT)) {
                if (currrentClsId == 8) {
                    if (testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_type.equalsIgnoreCase("O")) {
                        hm_limit_other.add(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);
                    }
                    testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).my_answer = ans;
                    if (testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).my_answer.equalsIgnoreCase(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).answer))
                        hm_other.put(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id, testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).my_answer);
                    /*else
                    {
                        hm_wrong_other.add(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);
                    }*/
                    if (testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).option_selected == -1) {
                        testSeriesJson.mathematics.attempted_questions = testSeriesJson.mathematics.attempted_questions + 1;
                    }

                    testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).option_selected = 5;
                    listQuestions.get(quesIndex).status = 2;
                    listQuestions.get(quesIndex).option_selected = 5;
                }
            }
            return;
        }

        if(quesIndex<PHYSICS_COUNT)
        {
            if(hm_limit_phy.contains(testSeriesJson.physics.questions.get(quesIndex).question_id))
                hm_limit_phy.remove(testSeriesJson.physics.questions.get(quesIndex).question_id);

            if(testSeriesJson.physics.questions.get(quesIndex).option_selected!=-1)
            {
                testSeriesJson.physics.attempted_questions=testSeriesJson.physics.attempted_questions-1;
            }
            if(hm_phy.containsKey(testSeriesJson.physics.questions.get(quesIndex).question_id))
                hm_phy.remove(testSeriesJson.physics.questions.get(quesIndex).question_id);

           /* if(hm_wrong_phy.contains(testSeriesJson.physics.questions.get(quesIndex).question_id))
            hm_wrong_phy.remove(testSeriesJson.physics.questions.get(quesIndex).question_id);
*/
            testSeriesJson.physics.questions.get(quesIndex).option_selected=-1;


        }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT))
        {

            if(hm_limit_che.contains(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id))
                hm_limit_che.remove(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id);


            if(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected!=-1)
            {
                testSeriesJson.chemistry.attempted_questions=testSeriesJson.chemistry.attempted_questions-1;
            }
            testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected=-1;

            if(hm_che.containsKey(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id))
                hm_che.remove(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id);
           /* if(hm_wrong_che.contains(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id))
                hm_wrong_che.remove(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id);
        */
        }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT+OTHER_COUNT)) {
            if(currrentClsId==8) {
                if(hm_limit_other.contains(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                    hm_limit_other.remove(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);

                if (hm_other.containsKey(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                    hm_other.remove(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);

              /*  if(hm_wrong_other.contains(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                    hm_wrong_other.remove(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);
*/
                if (testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).option_selected != -1) {
                    testSeriesJson.mathematics.attempted_questions = testSeriesJson.mathematics.attempted_questions - 1;
                }
                testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).option_selected = -1;
            }
        }


    }



    private void storeQuestionAnswer(String quesValue, int option,boolean isAnsclr) {
        if(!isAnsclr)
        {
            if(quesIndex<PHYSICS_COUNT)
            {

                if (testSeriesJson.physics.questions.get(quesIndex).question_type.equalsIgnoreCase("O")) {
                    hm_limit_phy.add(testSeriesJson.physics.questions.get(quesIndex ).question_id);
                }

                if(testSeriesJson.physics.questions.get(quesIndex).option_selected==-1)
                {
                    testSeriesJson.physics.attempted_questions=testSeriesJson.physics.attempted_questions+1;
                }
                testSeriesJson.physics.questions.get(quesIndex).option_selected=option;
                if(testSeriesJson.physics.questions.get(quesIndex).option_selected==testSeriesJson.physics.questions.get(quesIndex).option_right) {
                    hm_phy.put(testSeriesJson.physics.questions.get(quesIndex).question_id, testSeriesJson.physics.questions.get(quesIndex).option_selected + "");
                    if(hm_wrong_phy.contains(testSeriesJson.physics.questions.get(quesIndex ).question_id))
                        hm_wrong_phy.remove(testSeriesJson.physics.questions.get(quesIndex).question_id);
                } else
                {
                    if ((currrentClsId==9)||!testSeriesJson.physics.questions.get(quesIndex).question_type.equalsIgnoreCase("O"))
                    hm_wrong_phy.add(testSeriesJson.physics.questions.get(quesIndex ).question_id);
                }
            }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT))
            {

                if (testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_type.equalsIgnoreCase("O")) {
                    hm_limit_che.add(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id);
                }

                if(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected==-1)
                {
                    testSeriesJson.chemistry.attempted_questions=testSeriesJson.chemistry.attempted_questions+1;
                }
                testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected=option;
                if(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected==testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_right)
                {
                    hm_che.put( testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id,testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected+"");
                    if(hm_wrong_che.contains(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT  )).question_id))
                        hm_wrong_che.remove(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id);

                }
                else
                {
                    if ((currrentClsId==9)||!testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_type.equalsIgnoreCase("O"))
                    hm_wrong_che.add(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id);
                }
            }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT+OTHER_COUNT))
            {
                if(currrentClsId==8)
                {
                    if (testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_type.equalsIgnoreCase("O")) {
                        hm_limit_other.add(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);
                    }

                    if(testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected==-1)
                    {
                        testSeriesJson.mathematics.attempted_questions=testSeriesJson.mathematics.attempted_questions+1;
                    }
                    testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected=option;

                    if(testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected==testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_right)
                    {
                        hm_other.put( testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id,testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected+"");
                        if(hm_wrong_other.contains(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                            hm_wrong_other.remove(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);

                    }
                    else
                    {
                        if ((currrentClsId==9)||!testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_type.equalsIgnoreCase("O"))
                        hm_wrong_other.add(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);
                    }


                }else if(currrentClsId==9){
                    if (testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_type.equalsIgnoreCase("O")) {
                        hm_limit_other.add(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);
                    }

                    if(testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected==-1)
                    {
                        testSeriesJson.biology.attempted_questions=testSeriesJson.biology.attempted_questions+1;
                    }
                    testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected=option;

                    if(testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected==testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_right)
                    {
                        hm_other.put( testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id,testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected+"");
                        if(hm_wrong_other.contains(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                            hm_wrong_other.remove(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);

                    }
                    else
                    {
                        if ((currrentClsId==9)||!testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_type.equalsIgnoreCase("O"))
                        hm_wrong_other.add(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);
                    }


                }
            }

            listQuestions.get(quesIndex).status=2;
            listQuestions.get(quesIndex).option_selected=option;

            return;
        }
        if(quesIndex<PHYSICS_COUNT)
        {

            if(hm_limit_phy.contains(testSeriesJson.physics.questions.get(quesIndex ).question_id))
                hm_limit_phy.remove(testSeriesJson.physics.questions.get(quesIndex ).question_id);

            if(testSeriesJson.physics.questions.get(quesIndex).option_selected!=-1)
            {
                testSeriesJson.physics.attempted_questions=testSeriesJson.physics.attempted_questions-1;
            }
            if(hm_phy.containsKey(testSeriesJson.physics.questions.get(quesIndex).question_id))
                hm_phy.remove(testSeriesJson.physics.questions.get(quesIndex).question_id);

            if(hm_wrong_phy.contains(testSeriesJson.physics.questions.get(quesIndex ).question_id))
                hm_wrong_phy.remove(testSeriesJson.physics.questions.get(quesIndex).question_id);


            testSeriesJson.physics.questions.get(quesIndex).option_selected=-1;
        }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT))
        {
            if(hm_limit_che.contains(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id))
                hm_limit_che.remove(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id);



            if(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected!=-1)
            {
                testSeriesJson.chemistry.attempted_questions=testSeriesJson.chemistry.attempted_questions-1;
            }
            testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).option_selected=-1;

            if(hm_che.containsKey(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id))
                hm_che.remove(testSeriesJson.chemistry.questions.get(quesIndex-PHYSICS_COUNT).question_id);

            if(hm_wrong_che.contains(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT  )).question_id))
                hm_wrong_che.remove(testSeriesJson.chemistry.questions.get(quesIndex - (PHYSICS_COUNT )).question_id);


        }else if(quesIndex<(PHYSICS_COUNT+CHEMISTRY_COUNT+OTHER_COUNT))
        {
            if(currrentClsId==8)
            {
                if(hm_limit_other.contains(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                    hm_limit_other.remove(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);


                if(hm_other.containsKey(testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id))
                    hm_other.remove(testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id);

                if(hm_wrong_other.contains(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                    hm_wrong_other.remove(testSeriesJson.mathematics.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);

                if(testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected!=-1)
                {
                    testSeriesJson.mathematics.attempted_questions=testSeriesJson.mathematics.attempted_questions-1;
                }
                testSeriesJson.mathematics.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected=-1;

            }else if(currrentClsId==9){

                if(hm_limit_other.contains(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                    hm_limit_other.remove(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);

                if(hm_other.containsKey(testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id))
                    hm_other.remove(testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).question_id);

                if(hm_wrong_other.contains(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id))
                    hm_wrong_other.remove(testSeriesJson.biology.questions.get(quesIndex - (PHYSICS_COUNT + CHEMISTRY_COUNT)).question_id);


                if(testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected!=-1)
                {
                    testSeriesJson.biology.attempted_questions=testSeriesJson.biology.attempted_questions-1;
                }
                testSeriesJson.biology.questions.get(quesIndex-(PHYSICS_COUNT+CHEMISTRY_COUNT)).option_selected=-1;

            }
        }

        listQuestions.get(quesIndex).status=1;
        listQuestions.get(quesIndex).option_selected=-1;

    }
    private void DeselectButtons() {
        /*for (int i = 0; i < quizJson.length(); i++) {
            if (ColorButton == i)
                this.findViewById(i).setBackground(ContextCompat.getDrawable(_this, R.drawable.colorbuttonshape));
        }*/
    }
    private String getStyle()
    {
        return "<style>"  +
                " table.quiz-table{margin:5px; padding:0px;border-collapse:collapse;}"+
                "table.quiz-table th, table.quiz-table td{padding:5px; border:1px solid #ccc;}" +
                " </style>";
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

    public void setSelection(int subject_select,int pos) {
        SUBJECT_SELECTED=subject_select;
        isFromreview=true;
        switch (SUBJECT_SELECTED)
        {
            case 1:
                radioPhy.setChecked(true);
                break;
            case 2:
                radioChe.setChecked(true);
                break;
            case 3:
                radioBio.setChecked(true);
                break;
            case 4:
                radioMath.setChecked(true);
                break;
        }
        quesIndex=pos;
        a0.setBackgroundColor(0);
        a1.setBackgroundColor(0);
        a2.setBackgroundColor(0);
        a3.setBackgroundColor(0);
        loadFirstQuestion(quesIndex);
    }
    int wrongAnswers;
    int correctAnswers;
    long TIME_TAKEN;
    private void SubmitButton(boolean timeUp) {

       /* if(!timeUp)
        {
            CommonUtils.showToast(getApplicationContext(),"You didn't answer any question, please answer first");
            return;
        }*/

        if(dialog!=null&&dialog.isShowing())
            return;


        testSeriesJson.physics.total_correct =hm_phy.size();
        testSeriesJson.physics.total_wrong =testSeriesJson.physics.attempted_questions-testSeriesJson.physics.total_correct;
        testSeriesJson.chemistry.total_correct =hm_che.size();
        testSeriesJson.chemistry.total_wrong =testSeriesJson.chemistry.attempted_questions-testSeriesJson.chemistry.total_correct;



        if(currrentClsId==8)
        {
            testSeriesJson.mathematics.total_correct =hm_other.size();
            testSeriesJson.mathematics.total_wrong =testSeriesJson.mathematics.attempted_questions-testSeriesJson.mathematics.total_correct;
        }

        else if(currrentClsId==9) {
            testSeriesJson.biology.total_correct = hm_other.size();
            testSeriesJson.biology.total_wrong =testSeriesJson.biology.attempted_questions-testSeriesJson.biology.total_correct;

        }

        if(timeUp)
        {



            HOUR =  mCvCountdownView.getHour();
            MINUTE = mCvCountdownView.getMinute();
            SECOUNDS=mCvCountdownView.getSecond();


            TIME_TAKEN=  3*60*60-((HOUR*60*60)+(MINUTE*60)+SECOUNDS);

        /* HOUR=(int)TIME_TAKEN/(60*60);
         MINUTE=(int)(TIME_TAKEN%(60*60)/60);*/

            SECOUNDS = (int) (TIME_TAKEN  % 60);
            MINUTE = (int) (TIME_TAKEN / (60 ) % 60);
            HOUR = (int) (TIME_TAKEN / (60 * 60 ) % 24);

            Log.v("TIme Taken","TIme Taken "+HOUR+":"+MINUTE+":"+SECOUNDS);
            mCvCountdownView.stop();


            //  wrongAnswers = quizJson.length() - score;
            // SubmitToServer(getTrackTime(), score, wrongAnswers,true);
            int attempted=testSeriesJson.physics.attempted_questions+testSeriesJson.chemistry.attempted_questions;
            correctAnswers=testSeriesJson.physics.total_correct +testSeriesJson.chemistry.total_correct;
            wrongAnswers=testSeriesJson.physics.total_wrong +testSeriesJson.chemistry.total_wrong;
            if(currrentClsId==8)
            {
                correctAnswers=correctAnswers+testSeriesJson.mathematics.total_correct;
                wrongAnswers=wrongAnswers+testSeriesJson.mathematics.total_wrong;
                attempted=attempted+testSeriesJson.mathematics.attempted_questions;
            }else
            {
                correctAnswers=correctAnswers+testSeriesJson.biology.total_correct;
                wrongAnswers=wrongAnswers+testSeriesJson.biology.total_wrong;
                attempted=attempted+testSeriesJson.biology.attempted_questions;
            }



            SubmitToServer(getTrackTime(), correctAnswers, wrongAnswers,attempted,true);
            return;
        }
        /*dialog=new Dialog(TestQuizActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert);
        TextView txt_msg=dialog.findViewById(R.id.txt_msg);
        txt_msg.setText("");
        TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok=dialog.findViewById(R.id.txt_ok);
        txt_ok.setBackgroundResource(background_drawable);

        txt_ok.setBackgroundResource(color_theme);
        if (timeUp) {
            txt_cancel.setVisibility(View.GONE);
            txt_ok.setText("Ok");
            dialog.setCancelable(false);
        }
        else {
            txt_cancel.setVisibility(View.VISIBLE);
            txt_ok.setText("Yes");
        }
        txt_cancel.setText("No");
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

                HOUR = mCvCountdownView.getHour();
                MINUTE = mCvCountdownView.getMinute();
                SECOUNDS=mCvCountdownView.getSecond();


                 TIME_TAKEN=  3*60*60-((HOUR*60*60)+(MINUTE*60)+SECOUNDS);

                SECOUNDS = (int) (TIME_TAKEN  % 60);
                MINUTE = (int) (TIME_TAKEN / (60 ) % 60);
                HOUR = (int) (TIME_TAKEN / (60 * 60 ) % 24);

                Log.v("TIme Taken","TIme Taken "+HOUR+":"+MINUTE+":"+SECOUNDS);

                mCvCountdownView.stop();




               //int wrongAnswers = testSeriesJson.total_questions - score;

                int attempted=testSeriesJson.physics.attempted_questions+testSeriesJson.chemistry.attempted_questions;
                int correctAnswers=testSeriesJson.physics.correct_answers+testSeriesJson.chemistry.correct_answers;
                 wrongAnswers=testSeriesJson.physics.wrong_answers+testSeriesJson.chemistry.wrong_answers;
                if(currrentClsId==8)
                {
                    correctAnswers=correctAnswers+testSeriesJson.mathematics.correct_answers;
                    wrongAnswers=wrongAnswers+testSeriesJson.mathematics.wrong_answers;
                    attempted=attempted+testSeriesJson.mathematics.attempted_questions;
                }else
                {
                    correctAnswers=correctAnswers+testSeriesJson.biology.correct_answers;
                    wrongAnswers=wrongAnswers+testSeriesJson.biology.wrong_answers;
                    attempted=attempted+testSeriesJson.biology.attempted_questions;
                }
                SubmitToServer(getTrackTime(), correctAnswers, wrongAnswers,attempted,true);
                    //showResult(correctAnswers,attempted);

            }
        });




        if (timeUp)
            txt_msg.setText("Timeup!");
        else
            txt_msg.setText("Do you really want to finish the quiz?");

        dialog.show();*/

    }
    AlertDialog    alertDialog;
    DecimalFormat df = new DecimalFormat("#.##");
    float   c;
    private void showResult(int total_marks,int score, int size, String test_series_id)
    {
        alertDialog = new AlertDialog.Builder(TestQuizActivity.this, R.style.DialogTheme_notrans_quiz).create();
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
        TextView txt_display_time = dialogView.findViewById(R.id.txt_display_time);
        txt_mocktest.setVisibility(View.GONE);
        TextView txt_sectionname = dialogView.findViewById(R.id.txt_sectionname);
        TextView txt_chapter_name = dialogView.findViewById(R.id.txt_chapter_name);
        TextView txt_score = dialogView.findViewById(R.id.txt_score);
        TextView txt_score_total = dialogView.findViewById(R.id.txt_score_total);
        TextView txt_score_slash = dialogView.findViewById(R.id.txt_score_slash);
        //TextView txt_speed = dialogView.findViewById(R.id.txt_speed);
        TextView txt_accuracy = dialogView.findViewById(R.id.txt_accuracy);
        CircularProgressBarThumb progress_score =dialogView. findViewById(R.id.progress_score);
        CircularProgressBarThumb progress_time =dialogView. findViewById(R.id.progress_time);
        CircularProgressBarThumb progress_accuracy =dialogView. findViewById(R.id.progress_accuracy);
        // ProgressBar progress_speed =dialogView. findViewById(R.id.progress_speed);
        RelativeLayout rel_top_main =dialogView. findViewById(R.id.rel_top_main);
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
        txt_score_total.setTextColor(getResources().getColor(color_theme));
        rel_top_main.setBackgroundResource(background_top_score_drawable);

        lnr_title_header.setCardBackgroundColor(getResources().getColor(color_theme_card));


        RelativeLayout rel_top_main_anim =dialogView. findViewById(R.id.rel_top_main_anim);
        View lnr_alert_one =dialogView. findViewById(R.id.lnr_alert_one);
        View scrollview_alert =dialogView. findViewById(R.id.scrollview_alert);
        View lnr_back_two =dialogView. findViewById(R.id.lnr_back_two);
        Button btn_view_status =dialogView. findViewById(R.id.btn_view_status);
        TextView txt_quiz_msg =dialogView. findViewById(R.id.txt_quiz_msg);
        ImageView img_emoji =dialogView. findViewById(R.id.img_emoji);

        EditText edit_suggestion = dialogView.findViewById(R.id.edit_suggestion);
        BarChart barchart = dialogView.findViewById(R.id.barchart);
        txt_score_slash.setBackgroundResource(color_theme);
        txt_quiz_msg.setTextColor(getResources().getColor(color_theme));
        rel_top_main_anim.setBackgroundResource(background_top_drawable);
        btn_view_status.setBackgroundResource(background_drawable);

        if(test_series_type.contains("J"))
           c=((float)total_marks/Integer.parseInt(testSeriesJson.total_marks));else
            c=((float)total_marks/Integer.parseInt(testSeriesJson.total_marks));
        c=c*100;
        int audi0;

        lnr_back_two.setVisibility(View.INVISIBLE);


        lnr_back_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(alertDialog!=null&&alertDialog.isShowing())
                {
                    alertDialog.dismiss();
                }
                finish();

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
                scrollview_alert .setAnimation(animation);

                String selectedAnswer =String.format("%02d",size) ;

                selected.setText((" Attempted : "+selectedAnswer));
                //txt_recomanded.setTextColor(getResources().getColor(color_theme));
                txt_sectionname.setText("Test Series");
                txt_chapter_name.setText("Test Series");
                String correctAnswer = String.format("%02d",score) ;
                correct.setText(fromHtml(getString(R.string.correct_answeres)+": "+correctAnswer));
                String wrongAnswer = String.format("%02d",wrongAnswers) ;
                wrong.setText(fromHtml(getString(R.string.wrong_answers)+": "+wrongAnswer));





                //progress_score.setMax(100);
                progress_score.setProgress(1);


                //progress_accuracy.setMax(100);
                if(test_series_type.contains("J"))
                    progress_accuracy.setProgress((float)total_marks/Integer.parseInt(testSeriesJson.total_marks));else
                    progress_accuracy.setProgress((float)total_marks/Integer.parseInt(testSeriesJson.total_marks));

                //Log.v("Score ","Score "+(float)score/quizJson.length());
                //setAccuracy(pie_accuracy,c,w);

                //float mnts=MINUTE+(float)SECOUNDS/60;

                ProgressBarAnimation anim_score = new ProgressBarAnimation(progress_score, 0, progress_score.getProgress());
                anim_score.setDuration(1000);
                progress_score.startAnimation(anim_score);

                ProgressBarAnimation anim_acc = new ProgressBarAnimation(progress_accuracy, 0, progress_accuracy.getProgress());
                anim_acc.setDuration(1000);


                progress_time.setProgress(((float) TIME_TAKEN/3*60*60));

                ProgressBarAnimation anim_time = new ProgressBarAnimation(progress_time, 0, progress_time.getProgress());
                anim_time.setDuration(1000);


                txt_score.setText(total_marks+"");
                if(test_series_type.contains("J"))
                txt_score_total.setText(""+(testSeriesJson.total_marks));else
                txt_score_total.setText(""+(testSeriesJson.total_marks));
                // txt_score_slash.setBackgroundResource(R.color.phy_background);
                time.setText((String.format("%02d",HOUR)+":"+String.format("%02d",MINUTE)+":"+String.format("%02d",SECOUNDS)));
                txt_display_time.setText("Hours");
                txt_accuracy.setText((int)c+" %");


                Review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* if(alertDialog!=null&&alertDialog.isShowing())
                        {
                            alertDialog.dismiss();
                        }*/
                        Intent in=new Intent(TestQuizActivity.this,TestSeriesReviewActivity.class);
                        in.putExtra("test_series_id",test_series_id);

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
                        }catch ( Exception e)
                        {

                        }
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        AppController.getInstance().playAudio(R.raw.button_click);
                    }
                });
                lnr_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quesIndex = 0;
                        if(alertDialog!=null&&alertDialog.isShowing())
                        {
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
                        if(alertDialog!=null&&alertDialog.isShowing())
                        {
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
                        if(alertDialog!=null&&alertDialog.isShowing())
                        {
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


        }


        if (c >= 90) {
            edit_suggestion.setText(getString(R.string.nighty_percent));
            txt_quiz_msg.setText(getString(R.string.nighty_percent));
            img_status.setImageResource(R.drawable.ic_smily_90);
            audi0=R.raw.qz_6;
            Glide.with(getApplicationContext()).load(R.drawable.q5).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);

        } else if (c >= 80) {
            txt_quiz_msg.setText(getString(R.string.eight_percent));
            edit_suggestion.setText(getString(R.string.eight_percent));
            img_status.setImageResource(R.drawable.ic_smily_80);
            audi0=R.raw.qz_5;
            Glide.with(getApplicationContext()).load(R.drawable.q4).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else if (c >= 60) {
            txt_quiz_msg.setText(getString(R.string.sixty_percent));
            edit_suggestion.setText(getString(R.string.sixty_percent));
            img_status.setImageResource(R.drawable.ic_smily_60);
            audi0=R.raw.qz_4;
            Glide.with(getApplicationContext()).load(R.drawable.q3).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else if (c >= 40) {
            txt_quiz_msg.setText(getString(R.string.fourty_percent));
            edit_suggestion.setText(getString(R.string.fourty_percent));
            img_status.setImageResource(R.drawable.ic_smily_40);
            audi0=R.raw.qz_3;
            Glide.with(getApplicationContext()).load(R.drawable.q2).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else if (c >= 20) {
            txt_quiz_msg.setText(getString(R.string.twenty_percent));
            edit_suggestion.setText(getString(R.string.twenty_percent));
            img_status.setImageResource(R.drawable.ic_smily_0);
            audi0=R.raw.qz_2;
            Glide.with(getApplicationContext()).load(R.drawable.q1).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        } else {
            txt_quiz_msg.setText(getString(R.string.zero_percent));
            edit_suggestion.setText(getString(R.string.zero_percent));
            img_status.setImageResource(R.drawable.ic_smily_0);
            audi0=R.raw.qz_1;
            Glide.with(getApplicationContext()).load(R.drawable.q1).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_emoji);
        }



        lnr_recommand.setVisibility(View.GONE);
        barchart.setVisibility(View.GONE);
        barchart.setClickable(false);




        alertDialog.setView(dialogView);
        alertDialog.show();



        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_bottom_to_top);
        lnr_alert_one .setAnimation(animation);
        AppController.getInstance().playQuizAudio(audi0);

    }
    private String start;

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
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
        if(difference<0)
            difference=-difference;
        long diffSeconds = difference / 1000 % 60;
        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        if(diffHours>=1 ||diffMinutes>TOTAL_TIME)
        {
            diffHours=0;
            diffMinutes=0;
            diffSeconds=0;

            return formatter.format(0) + ":" +formatter.format( TOTAL_TIME) + ":" +formatter.format( 0);
        }
        //Log.d("difference", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);

        return formatter.format(diffHours) + ":" +formatter.format( diffMinutes) + ":" +formatter.format( diffSeconds);
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

    public void readCountry()
    {
        try {
            InputStream is=getAssets().open("sample_neet_mocktest.json");
            StringBuffer s=new StringBuffer();
            byte[]b=new byte[is.available()];
            is.read(b);

            JSONObject obj=new JSONObject(new String(b));

            Gson gson=new Gson();
            testSeriesJson=gson.fromJson(obj.getJSONObject("neet_test_1").toString(),TestSeriesJson.class);



            listQuestions.clear();
            try{


                PHYSICS_COUNT=testSeriesJson.physics.total_questions;
                CHEMISTRY_COUNT=testSeriesJson.chemistry.total_questions;
                listQuestions.addAll(testSeriesJson.physics.questions);
                if(testSeriesJson.chemistry.questions.size()>0)
                    listQuestions.addAll(testSeriesJson.chemistry.questions);
                else radioChe.setVisibility(View.GONE);
                if(currrentClsId==8)
                {
                    OTHER_COUNT=testSeriesJson.mathematics.total_questions;
                    listQuestions.addAll(testSeriesJson.mathematics.questions);
                }else if(currrentClsId==9)
                {
                    OTHER_COUNT=testSeriesJson.biology.total_questions;
                    listQuestions.addAll(testSeriesJson.biology.questions);
                }


                selected = new int[listQuestions.size()];
                java.util.Arrays.fill(selected, -1);
                correctAns = new int[listQuestions.size()];
                java.util.Arrays.fill(correctAns, -1);



                loadFirstQuestion(quesIndex);
                isFromreview=false;

                mCvCountdownView.start(1000*60*60*3);
                mCvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {

                        SubmitButton(true);
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            Log.v("listQuestions ","listQuestions 2 "+listQuestions.size());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
