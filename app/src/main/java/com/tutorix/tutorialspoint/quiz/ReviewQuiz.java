package com.tutorix.tutorialspoint.quiz;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.QuizReviewAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ReviewQuiz extends AppCompatActivity {
    ReviewQuiz _this;
    String classid;
    private String access_token;
    private String userId;
    private String subject_id;
    private String selected_user_id;
    private String selected_class_id;

    String loginType;
    //AVLoadingIndicatorView loadingPanelID;

    RecyclerView recyclerView;
    List<QuizModel>listQuiz;
    QuizReviewAdapter chaptersAdapter;
    Toolbar toolbar;
    LinearLayout lnr_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = ReviewQuiz.this;
        Configuration config= AppConfig.setLanguages(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        setContentView(R.layout.activity_review_quiz);
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userId = userInfo[0];;
        loginType = userInfo[2];
        classid = userInfo[4];
        subject_id=getIntent().getStringExtra("subject_id");
        String mock=getIntent().getStringExtra("mock_test");
        selected_user_id=getIntent().getStringExtra("selected_user_id");
        selected_class_id=getIntent().getStringExtra("selected_class_id");
       toolbar = findViewById(R.id.toolbar);
        lnr_home = findViewById(R.id.lnr_home);
        Log.v("selected_user_id","selected_user_id "+selected_user_id+" selected_class_id "+selected_class_id);
       if(mock.isEmpty())
        toolbar.setTitle(getString(R.string.review_quiz));
       else
        toolbar.setTitle(getString(R.string.mock_test_review));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        if(subject_id.equals("1"))
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(ReviewQuiz.this, R.drawable.ic_phy_bg_green));
        else  if(subject_id.equals("2"))
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(ReviewQuiz.this, R.drawable.ic_chemistry_bg_green));
        else  if(subject_id.equals("3"))
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(ReviewQuiz.this, R.drawable.ic_bio_bg_green));
        else  if(subject_id.equals("4"))
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(ReviewQuiz.this, R.drawable.ic_math_bg_green));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subject_id) {
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

        recyclerView = findViewById(R.id.recycler_view);
        listQuiz=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(_this, RecyclerView.VERTICAL, false));

        recyclerView.setHasFixedSize(true);
       // chaptersAdapter.notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        if(selected_user_id!=null&&selected_user_id==userId)
        {
            try {
                basePath = AppConfig.getOnlineURLImage(selected_class_id);
                chaptersAdapter = new QuizReviewAdapter(listQuiz, ReviewQuiz.this,basePath,subject_id);
                recyclerView.setAdapter(chaptersAdapter);
                fillWithData();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
      else  /*if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                try {
                    basePath = AppConfig.getOnlineURLImage(classid);
                    chaptersAdapter = new QuizReviewAdapter(listQuiz, ReviewQuiz.this,basePath,subject_id);
                    recyclerView.setAdapter(chaptersAdapter);
                    fillWithData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                CommonUtils.showToast(_this,getResources().getString(R.string.no_internet));

            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userId,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
            {
                SDData();
            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        basePath = AppConfig.getOnlineURLImage(classid);
                        chaptersAdapter = new QuizReviewAdapter(listQuiz, ReviewQuiz.this,basePath,subject_id);
                        recyclerView.setAdapter(chaptersAdapter);
                        fillWithData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.showToast(_this,getResources().getString(R.string.no_internet));

                }
            }
        }else
        {
            SDData();
        }*/
            if(/*AppConfig.checkSDCardEnabled(_this,userId,classid)&&*/AppConfig.checkSdcard(classid,getApplicationContext()))
            {
                SDData();
            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        basePath = AppConfig.getOnlineURLImage(classid);
                        chaptersAdapter = new QuizReviewAdapter(listQuiz, ReviewQuiz.this,basePath,subject_id);
                        recyclerView.setAdapter(chaptersAdapter);
                        fillWithData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.showToast(_this,getResources().getString(R.string.no_internet));

                }
            }
        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home(v);
            }
        });
    }

    private void fillWithData() throws UnsupportedEncodingException {
        String quiz_id;
        //if (getIntent().getStringExtra("comingfromtrack") != null) {
            quiz_id = getIntent().getStringExtra("quiz_id");
       // } else {
          //  SharedPreferences pref = getApplicationContext().getSharedPreferences("Quiz", MODE_PRIVATE);
          //  quiz_id = pref.getString("quiz_id", "");
      //  }

        final List<QuizModel> data = new ArrayList<>();
        final JSONObject fjson = new JSONObject();
        try {
            //fjson.put(Constants.userId, getIntent().getStringExtra(Constants.userId));

            if(selected_user_id==null||selected_user_id==userId)
                fjson.put(Constants.userId, userId);
            else {
                fjson.put(Constants.userId, userId);
                fjson.put(Constants.student_id, selected_user_id);
            }

            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);

        Log.v("QuizId","QuizId "+Constants.USER_QUIZ + "/" + quiz_id + "?json_data=" + URLEncoder.encode(encryption, "UTF-8") );
        StringRequest strReq = new StringRequest(Request.Method.GET,
                 Constants.USER_QUIZ + "/" + quiz_id + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                       // loadingPanelID.hide();
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                String total_marks = jObj.getString("total_marks");
                                String total_correct = jObj.getString("total_correct");
                                String total_wrong = jObj.getString("total_wrong");
                                String section_name = jObj.getString("section_name");
                                String lecture_name = jObj.getString("lecture_name");
                                String subject_id = jObj.getString("subject_id");
                                String section_id = jObj.getString("section_id");
                                String lecture_id = jObj.getString("lecture_id");
                                String mock_test = jObj.getString("mock_test");
                                String attempted_questions = jObj.getString("attempted_questions");

                                JSONObject jsonobj = new JSONObject(jObj.getString("quiz_json"));

                                JSONArray answers = jsonobj.getJSONObject("quiz").getJSONArray("questions");
                               // Log.v("answers","answers "+answers.toString());
                                for (int i = 0; i < answers.length(); i++) {
                                    JSONObject json_data = answers.getJSONObject(i);
                                    //Log.d(AppConfig.JSON_DATA, json_data.toString());
                                    QuizModel chapters = new QuizModel();
                                    chapters.question = json_data.getString("question");
                                    chapters.total_marks = total_marks;
                                    chapters.total_correct = total_correct;
                                    chapters.total_wrong = total_wrong;
                                    chapters.total = answers.length();
                                    chapters.attempted_questions = attempted_questions;

                                    JSONArray ansList = json_data.getJSONArray("options");
                                    if(ansList.getJSONObject(0).has("option_1"))
                                    chapters.option_1 = ansList.getJSONObject(0).getString("option_1");
                                    if(ansList.getJSONObject(0).has("option_2"))
                                    chapters.option_2 = ansList.getJSONObject(0).getString("option_2");
                                    if(ansList.getJSONObject(0).has("option_3"))
                                    chapters.option_3 = ansList.getJSONObject(0).getString("option_3");
                                    if(ansList.getJSONObject(0).has("option_4"))
                                    chapters.option_4 = ansList.getJSONObject(0).getString("option_4");
                                    chapters.option_selected = json_data.getString("option_selected");
                                    chapters.option_right = json_data.getString("option_right");
                                    chapters.explanation = json_data.getString("explanation");
                                    if(json_data.has("question_type"))
                                        chapters.question_type = json_data.getString("question_type");
                                    if(json_data.has("section_title"))
                                        chapters.section_name=json_data.getString("section_title");;
                                    if(json_data.has("lecture_title"))
                                        chapters.lecture_name=json_data.getString("lecture_title");;
                                    if(json_data.has("subject_id"))
                                        chapters.subject_id=json_data.getString("subject_id");;
                                    if(json_data.has("section_id"))
                                        chapters.section_id=json_data.getString("section_id");;
                                    if(json_data.has("lecture_id"))
                                        chapters.lectur_id=json_data.getString("lecture_id");;

                                        if(json_data.has("mock_test"))
                                        chapters.mock_test=json_data.getString("mock_test");;
                                    chapters.classId=classid;
                                    chapters.userId=userId;


                                    chaptersAdapter.addData(chapters);
                                    //data.add(chapters);
                                }


                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(_this,errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(_this,e.getMessage());
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
                CommonUtils.showToast(getApplicationContext(), msg);
                //loadingPanelID.hide();
                CustomDialog.closeDialog();
                finish();

            }
        }){
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
                } catch ( Exception e) {
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
        };
        //loadingPanelID.setVisibility(View.VISIBLE);
        CustomDialog.showDialog(ReviewQuiz.this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                AppController.getInstance().playAudio(R.raw.back_sound);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    String basePath;
    private void loadDataBaseURL(String subjectId,String section_id,String lectureId)
    {
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
     String   userid = userInfo[0];;
        loginType = userInfo[2];


        if(loginType.isEmpty())
        {
            if (lectureId==null||lectureId.equalsIgnoreCase("0"))
                basePath = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id;
            else
                basePath = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/" + lectureId;
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
            {
                if (lectureId==null||lectureId.equalsIgnoreCase("0"))
                    basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id;
                else
                    basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lectureId;
            }else
            {
                if (lectureId==null||lectureId.equalsIgnoreCase("0"))
                    basePath = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id;
                else
                    basePath = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/" + lectureId;
            }
        }else
        {
            if (lectureId==null||lectureId.equalsIgnoreCase("0"))
                basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id;
            else
                basePath = "file://" + AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lectureId;
        }

       /* if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            if (lectureId==null||lectureId.equalsIgnoreCase("0"))
                basePath = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id;
            else
                basePath = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id + "/" + lectureId;
        }
        else {
            if (lectureId==null||lectureId.equalsIgnoreCase("0"))
                basePath = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id;
            else
                basePath = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId;
        }*/

        if(!basePath.trim().endsWith("/"))
            basePath=basePath+"/";



    }
    public void home(View v) {
        Intent in = new Intent(ReviewQuiz.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();

    }


    private void SDData()
    {

        basePath="file://"+ AppConfig.getSdCardPath(classid,getApplicationContext());

        chaptersAdapter = new QuizReviewAdapter(listQuiz, ReviewQuiz.this,basePath,subject_id);
        recyclerView.setAdapter(chaptersAdapter);
        final List<QuizModel> data = new ArrayList<>();
        MyDatabase dbHelper = MyDatabase.getDatabase(_this);


        QuizModel QuizResult = dbHelper.quizModelDAO().getQuizDetails(getIntent().getStringExtra("lecture_time"));

        String total_marks = QuizResult.total_marks;
        String total_correct = QuizResult.total_correct;
        String total_wrong = QuizResult.total_wrong;
        String subjectId = QuizResult.subject_id;
        String section_id = QuizResult.section_id;
        String lectureId = QuizResult.lectur_id;
        String section_name = QuizResult.section_name;
        String lecture_name =QuizResult.lecture_name;
        String mock_test = QuizResult.mock_test;
        String subject_id = QuizResult.subject_id;
        String attempted_questions = QuizResult.attempted_questions;

        String lecture_id = QuizResult.lectur_id;
        loadDataBaseURL(subjectId,section_id,lectureId);
        JSONObject jsonobj;
        try {
            //Log.d(AppConfig.JSON_DATA, QuizResult.question);
            jsonobj = new JSONObject(QuizResult.question);


            JSONArray answers = jsonobj.getJSONObject("quiz").getJSONArray("questions");

            for (int i = 0; i < answers.length(); i++) {
                JSONObject json_data = answers.getJSONObject(i);

                QuizModel chapters = new QuizModel();
                chapters.question = json_data.getString("question");
                chapters.total_marks = total_marks;
                chapters.total_correct = total_correct;
                chapters.total_wrong = total_wrong;
                chapters.total = answers.length();
                chapters.attempted_questions = attempted_questions;
                JSONArray ansList = json_data.getJSONArray("options");
                if(ansList.getJSONObject(0).has("option_1"))
                    chapters.option_1 = ansList.getJSONObject(0).getString("option_1");
                if(ansList.getJSONObject(0).has("option_2"))
                    chapters.option_2 = ansList.getJSONObject(0).getString("option_2");
                if(ansList.getJSONObject(0).has("option_3"))
                    chapters.option_3 = ansList.getJSONObject(0).getString("option_3");
                if(ansList.getJSONObject(0).has("option_4"))
                    chapters.option_4 = ansList.getJSONObject(0).getString("option_4");
                chapters.option_selected = json_data.getString("option_selected");
                chapters.option_right = json_data.getString("option_right");
                chapters.explanation = json_data.getString("explanation");
                if(json_data.has("question_type"))
                    chapters.question_type = json_data.getString("question_type");

                if(json_data.has("section_title"))
                    chapters.section_name=json_data.getString("section_title");;
                if(json_data.has("lecture_title"))
                    chapters.lecture_name=json_data.getString("lecture_title");;
                if(json_data.has("subject_id"))
                    chapters.subject_id=json_data.getString("subject_id");;
                if(json_data.has("section_id"))
                    chapters.section_id=json_data.getString("section_id");;
                if(json_data.has("lecture_id"))
                    chapters.lectur_id=json_data.getString("lecture_id");;
                if(json_data.has("mock_test"))
                    chapters.mock_test=json_data.getString("mock_test");;
                chapters.classId=classid;
                chapters.userId=userId;
                chaptersAdapter.addData(chapters);
                //data.add(chapters);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
