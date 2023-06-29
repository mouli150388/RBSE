package com.tutorix.tutorialspoint.quiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

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
import com.tutorix.tutorialspoint.adapters.MockTestReviewAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.MockTestReviewModel;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MockTestPreviousActivity extends AppCompatActivity {
    @BindView(R.id.img_logo_subj)
    ImageView imgLogoSubj;
    @BindView(R.id.lnr_top)
    LinearLayout lnrTop;
    @BindView(R.id.lnr_reload)
    LinearLayout lnr_reload;
    @BindView(R.id.viewpager_reports)
    ViewPager viewpagerReports;
    @BindView(R.id.lnr_left)
    LinearLayout lnrLeft;
    @BindView(R.id.lnr_right)
    LinearLayout lnrRight;
    @BindView(R.id.rel_top_main)
    RelativeLayout rel_top_main;
    @BindView(R.id.txt_total)
    TextView txt_total;
    /*@BindView(R.id.Retake)
    Button Retake;
    @BindView(R.id.Review)
    Button Review;
    @BindView(R.id.Quit)
    Button Quit;*/

   /* @BindView(R.id.img_logo_subj)
    ImageView imgLogoSubj;
    @BindView(R.id.love_music)
    TextView loveMusic;
    @BindView(R.id.lnr_top)
    LinearLayout lnrTop;
    @BindView(R.id.viewpager_reports)
    ViewPager viewpagerReports;*/


    private String lectureId;
    private String lecture_name;
    public String section_name;
    private String subjectId;
    private String userid;
    private String section_id;
    private String classid;
    private String mock_test;
    String loginType;
    String access_token;
    MockTestPreviousActivity _this;
    MockTestReviewAdapter adapter;
    int background_drawable;
    int color_theme;
    int color_theme_card;
    int progressDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_test_previous);
        ButterKnife.bind(this);
        _this = MockTestPreviousActivity.this;
        init();

    }

    private void init() {
        String[] userinfo = SessionManager.getUserInfo(_this);
        loginType = userinfo[2];
        access_token = userinfo[1];
        lnrRight.setVisibility(View.GONE);
        lnrLeft.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lecture_name = extras.getString(Constants.lectureName);
            section_name = extras.getString(Constants.sectionName);
            lectureId = extras.getString(Constants.lectureId);
            subjectId = extras.getString(Constants.subjectId);
            classid = extras.getString(Constants.classId);
            userid = extras.getString(Constants.userId);
            section_id = extras.getString(Constants.sectionId);
            mock_test = extras.getString(Constants.mockTest);

            //Log.v("On Quize", "onQuize 11  " + subjectId);
        }

        if (subjectId.equalsIgnoreCase("1")) {

            rel_top_main.setBackgroundResource(R.drawable.quiz_score_phy);
            background_drawable=R.drawable.quiz_quit_btn_phy;
            color_theme=R.color.phy_background;
            color_theme_card=R.color.phy_background_card;
            progressDrawable=R.drawable.circular_progress_phy;

        } else if (subjectId.equalsIgnoreCase("2")) {

            rel_top_main.setBackgroundResource(R.drawable.quiz_score_che);
            background_drawable=R.drawable.quiz_quit_btn_che;
            color_theme=R.color.che_background;
            color_theme_card=R.color.che_background_card;
            progressDrawable=R.drawable.circular_progress_che;

        } else if (subjectId.equalsIgnoreCase("3")) {

            rel_top_main.setBackgroundResource(R.drawable.quiz_score_bio);
            background_drawable=R.drawable.quiz_quit_btn_bio;
            color_theme=R.color.bio_background;
            color_theme_card=R.color.bio_background_card;
            progressDrawable=R.drawable.circular_progress_bio;

        } else {

            rel_top_main.setBackgroundResource(R.drawable.quiz_score_math);
            background_drawable=R.drawable.quiz_quit_btn_math;
            color_theme=R.color.math_background;
            color_theme_card=R.color.math_background_card;
            progressDrawable=R.drawable.circular_progress_math;
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

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
        }*/
        adapter = new MockTestReviewAdapter(_this, userid,color_theme,background_drawable,color_theme_card,progressDrawable);
        viewpagerReports.setAdapter(adapter);
        viewpagerReports.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                txt_total.setText((position+1)+"/"+mocktest_total);
                handleLeftRight();
                //Log.v("OnPage Selected","OnPage Selected "+position+" "+(OFSET+LIMIT-2));
                if(position==OFSET-2)
                {
                    loadServerData();
                }
                AppController.getInstance().playAudio(R.raw.qz_next);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //nodata();
        cheklLogin();

    }

    private void cheklLogin() {

        if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                // checkCookieThenPlay();
                loadServerData();
            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
            {


                MyDatabase dbHelper = MyDatabase.getDatabase(_this);


                List<QuizModel> QuizResult = dbHelper.quizModelDAO().getMoCkPrevious(userid, classid, subjectId, section_id, mock_test);
                MockTestReviewModel model;
                QuizModel quizModel;
                for (int k = 0; k < QuizResult.size(); k++) {


                    quizModel = QuizResult.get(k);
                    model = new MockTestReviewModel();
                    model.attempted_questions = Integer.parseInt(quizModel.attempted_questions);
                    try {
                        model.total_marks = Integer.parseInt(quizModel.total_correct);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        model.total_correct = Integer.parseInt(quizModel.total_correct);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    model.total_questions = quizModel.total;
                    try {
                        model.total_wrong = Integer.parseInt(quizModel.total_wrong);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    model.quiz_id = quizModel.quiz_id;
                    model.lecture_id = quizModel.lectur_id;
                    model.section_id = quizModel.section_id;
                    model.subject_id = quizModel.subject_id;
                    model.class_id = quizModel.classId;
                    model.quiz_duration = quizModel.QuizDuration;
                    model.lecture_name = quizModel.lecture_name;
                    model.section_name = quizModel.section_name;
                    model.mock_test = quizModel.mock_test;
                    model.created_dtm = quizModel.QuizCreatedDtm;


                    adapter.addMockReview(model);


                }
                nodata();


            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    // checkCookieThenPlay();
                    loadServerData();
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                    // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        }else
        {


            MyDatabase dbHelper = MyDatabase.getDatabase(_this);


            List<QuizModel> QuizResult = dbHelper.quizModelDAO().getMoCkPrevious(userid, classid, subjectId, section_id, mock_test);
            MockTestReviewModel model;
            QuizModel quizModel;
            for (int k = 0; k < QuizResult.size(); k++) {


                quizModel = QuizResult.get(k);
                model = new MockTestReviewModel();
                model.attempted_questions = Integer.parseInt(quizModel.attempted_questions);
                try {
                    model.total_marks = Integer.parseInt(quizModel.total_correct);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    model.total_correct = Integer.parseInt(quizModel.total_correct);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                model.total_questions = quizModel.total;
                try {
                    model.total_wrong = Integer.parseInt(quizModel.total_wrong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                model.quiz_id = quizModel.quiz_id;
                model.lecture_id = quizModel.lectur_id;
                model.section_id = quizModel.section_id;
                model.subject_id = quizModel.subject_id;
                model.class_id = quizModel.classId;
                model.quiz_duration = quizModel.QuizDuration;
                model.lecture_name = quizModel.lecture_name;
                model.section_name = quizModel.section_name;
                model.mock_test = quizModel.mock_test;
                model.created_dtm = quizModel.QuizCreatedDtm;


                adapter.addMockReview(model);


            }
            nodata();


        }


    }

    private boolean checkPermissionForStorage() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

int OFSET=0;
int LIMIT=20;
    int mocktest_total;
    private void loadServerData() {

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.mockTest, mock_test);
            fjson.put(Constants.sectionId, section_id);
            fjson.put(Constants.subjectId, subjectId);
            fjson.put("offset", OFSET);
            fjson.put("limit", LIMIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        //Log.v("message ", "message " + message);
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.USER_GET_MOCKTEST + "/" + userid + "?json_data=" + encode,

                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                if(OFSET==0)
                               mocktest_total=jObj.getInt("mocktest_total");
                                OFSET=OFSET+LIMIT;
                                JSONArray array = jObj.getJSONArray("quiz_data");
                                MockTestReviewModel model;
                                JSONObject object;
                                for (int k = 0; k < array.length(); k++) {
                                    object = array.getJSONObject(k);
                                    model = new MockTestReviewModel();
                                    model.attempted_questions = object.getInt("attempted_questions");
                                    model.total_marks = object.getInt("total_marks");
                                    model.total_correct = object.getInt("total_correct");
                                    model.total_questions = object.getInt("total_questions");
                                    model.total_wrong = object.getInt("total_wrong");
                                    model.total_wrong = object.getInt("total_wrong");
                                    model.quiz_id = object.getString("quiz_id");
                                    model.lecture_id = object.getString("lecture_id");
                                    model.section_id = object.getString("section_id");
                                    model.subject_id = object.getString("subject_id");
                                    model.class_id = object.getString("class_id");
                                    model.quiz_duration = object.getString("quiz_duration");
                                    model.lecture_name = object.getString("lecture_name");
                                    model.section_name = object.getString("section_name");
                                    model.mock_test = object.getString("mock_test");
                                    model.created_dtm = object.getString("created_dtm");


                                    adapter.addMockReview(model);
                                }
                                txt_total.setText((viewpagerReports.getCurrentItem()+1)+"/"+mocktest_total);

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                               // CommonUtils.showToast(_this, errorMsg);
                            }
                            nodata();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //CommonUtils.showToast(_this, e.getMessage());
                            nodata();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                CustomDialog.closeDialog();
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
        };;

        CustomDialog.showDialog(MockTestPreviousActivity.this, true);
        AppController.getInstance().addToRequestQueue(strReq, "");
    }

    private void loadLocalData() {


    }

    public void home(View view) {
        Intent in = new Intent(MockTestPreviousActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    private void nodata() {
        if (adapter.getCount() > 0) {
            lnr_reload.setVisibility(View.GONE);
            lnrRight.setVisibility(View.VISIBLE);
            lnrLeft.setVisibility(View.VISIBLE);
            handleLeftRight();
        }
        else {
            lnr_reload.setVisibility(View.VISIBLE);
        }
    }
    private void handleLeftRight()
    {
        if (adapter.getCount() > 0) {
            lnrRight.setVisibility(View.VISIBLE);
            if (viewpagerReports.getCurrentItem() == 0)
                lnrLeft.setVisibility(View.GONE);
            else  lnrLeft.setVisibility(View.VISIBLE);
            if (viewpagerReports.getCurrentItem() == adapter.getCount() - 1)
                lnrRight.setVisibility(View.GONE);
        }
        else {
            lnrRight.setVisibility(View.GONE);
            if (adapter.getCount() == 0)
                lnrLeft.setVisibility(View.GONE);
        }


    }
    @OnClick({R.id.lnr_left, R.id.lnr_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lnr_left:
                if(viewpagerReports.getCurrentItem()>0)
                    viewpagerReports.setCurrentItem(viewpagerReports.getCurrentItem()-1);
                if(viewpagerReports.getCurrentItem()==0)
                    lnrLeft.setVisibility(View.GONE);

                lnrRight.setVisibility(View.VISIBLE);
                break;
            case R.id.lnr_right:
                lnrLeft.setVisibility(View.VISIBLE);
                if(viewpagerReports.getCurrentItem()<adapter.getCount()-1)
                    viewpagerReports.setCurrentItem(viewpagerReports.getCurrentItem()+1);
                if(viewpagerReports.getCurrentItem()==adapter.getCount()-1)
                    lnrRight.setVisibility(View.GONE);
                break;
        }
    }
}
