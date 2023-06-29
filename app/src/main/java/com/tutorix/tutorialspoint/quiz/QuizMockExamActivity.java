package com.tutorix.tutorialspoint.quiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.adapters.MockTestListAdapter;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.MockTestModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizMockExamActivity extends AppCompatActivity {

    @BindView(R.id.img_logo_subj)
    ImageView imgLogoSubj;
    @BindView(R.id.lnr_top)
    LinearLayout lnrTop;
    @BindView(R.id.recycler_mocktest)
    RecyclerView recyclerMocktest;


    @BindView(R.id.txt_view)
    TextView txtView;
    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.txt_ans)
    TextView txtAns;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_previous)
    TextView txt_previous;

    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.lnr_bottom)
    LinearLayout lnrBottom;
    @BindView(R.id.scroll_ruls)
    ScrollView scrollRuls;
    @BindView(R.id.img_bottom)
    ImageView img_bottom;
    @BindView(R.id.rel_top_main)
    RelativeLayout rel_top_main;
    @BindView(R.id.img_background)
    ImageView img_background;
    private String lectureId;
    private String lecture_name;
    private String section_name;
    private String subjectId;
    private String userid;
    private String section_id;
    private String classid;
    String loginType;
    QuizMockExamActivity _this;

    MockTestListAdapter adapter;
    // private JSONArray quesList = null;
    private String access_token;

    int MOCK_TEST;
    int background_drawable;
    int text_color;
    Drawable dr1;
    Drawable dr2;
    Drawable dr3;
    Drawable dr4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mock_exam);
        ButterKnife.bind(this);
        _this = this;
        btnStart.setSoundEffectsEnabled(false);
        initUI();
        txtView.setText(Html.fromHtml(getString(R.string.before_attempting_this_mock)));

    }

    public void home(View view) {
        Intent in = new Intent(QuizMockExamActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }


    private void initUI() {
        txt_previous.setVisibility(View.VISIBLE);
        //txt_previous.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        String[] userinfo = SessionManager.getUserInfo(_this);
        loginType = userinfo[2];
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lecture_name = extras.getString(Constants.lectureName);
            section_name = extras.getString(Constants.sectionName);
            lectureId = extras.getString(Constants.lectureId);
            subjectId = extras.getString(Constants.subjectId);
            classid = extras.getString(Constants.classId);
            userid = extras.getString(Constants.userId);
            section_id = extras.getString(Constants.sectionId);
            txtView.setText((getString(R.string.before_attempting_this_mock_test_please_make_sure_you_have_watched) + section_name + getString(R.string.chapter_videos)));

            // Log.v("On Quize", "onQuize 11  " + subjectId);
        }
        if (subjectId.equalsIgnoreCase("1")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_phy).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoSubj);
            rel_top_main.setBackgroundResource(R.drawable.ic_phy_bg_green);
            background_drawable = R.drawable.ic_phy_notes;
            //img_background.setImageResource(R.drawable.ic_phy_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_phy_bg_white).into(img_background);
            text_color = getResources().getColor(R.color.phy_background);
            btnStart.setBackgroundResource(R.drawable.ic_phy_notes);
            btnStart.setTextColor(getResources().getColor(R.color.phy_background));

            dr1 = getResources().getDrawable(R.drawable.ic_r1_phy);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_phy);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_phy);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_phy);
            txt_header.setText(getString(R.string.physics));

        } else if (subjectId.equalsIgnoreCase("2")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_che).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoSubj);
            rel_top_main.setBackgroundResource(R.drawable.ic_chemistry_bg_green);
            background_drawable = R.drawable.ic_che_notes;
            //img_background.setImageResource(R.drawable.ic_chemistry_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
            text_color = getResources().getColor(R.color.che_background);

            btnStart.setBackgroundResource(R.drawable.ic_che_notes);
            btnStart.setTextColor(getResources().getColor(R.color.che_background));

            dr1 = getResources().getDrawable(R.drawable.ic_r1_che);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_che);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_che);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_che);
            txt_header.setText(getString(R.string.chemistry));
        } else if (subjectId.equalsIgnoreCase("3")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_bio).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoSubj);
            rel_top_main.setBackgroundResource(R.drawable.ic_bio_bg_green);
            background_drawable = R.drawable.ic_bio_notes;
            //img_background.setImageResource(R.drawable.ic_bio_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_bio_bg_white).into(img_background);
            text_color = getResources().getColor(R.color.bio_background);
            btnStart.setBackgroundResource(R.drawable.ic_bio_notes);
            btnStart.setTextColor(getResources().getColor(R.color.bio_background));

            dr1 = getResources().getDrawable(R.drawable.ic_r1_bio);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_bio);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_bio);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_bio);
            txt_header.setText(getString(R.string.biology));
        } else {

            Glide.with(getApplicationContext()).load(R.drawable.gif_math).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoSubj);
            rel_top_main.setBackgroundResource(R.drawable.ic_math_bg_green);
            background_drawable = R.drawable.ic_math_notes;
            //img_background.setImageResource(R.drawable.ic_math_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_math_bg_white).into(img_background);
            text_color = getResources().getColor(R.color.math_background);
            btnStart.setBackgroundResource(R.drawable.ic_math_notes);
            btnStart.setTextColor(getResources().getColor(R.color.math_background));

            dr1 = getResources().getDrawable(R.drawable.ic_r1_math);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_math);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_math);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_math);
            txt_header.setText(getString(R.string.mathematics));

        }

        txtView.setCompoundDrawablesWithIntrinsicBounds(dr1, null, null, null);
        txtAns.setCompoundDrawablesWithIntrinsicBounds(dr2, null, null, null);
        txtTime.setCompoundDrawablesWithIntrinsicBounds(dr3, null, null, null);
        txt_previous.setCompoundDrawablesWithIntrinsicBounds(dr4, null, null, null);

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
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
        }

        */
        adapter = new MockTestListAdapter(_this, background_drawable, text_color);
        recyclerMocktest.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerMocktest.setAdapter(adapter);


        if (loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                // checkCookieThenPlay();

                getMockTest();
            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        } else if (loginType.equalsIgnoreCase("O")) {

            if (AppConfig.checkSDCardEnabled(_this, userid, classid) && AppConfig.checkSdcard(classid,getApplicationContext())) {
                if (checkPermissionForStorage()) {
                    String filepath;

                    //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
                    filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/mock_test_main.json";

                    byte[] encryptData = AppConfig.readFromFileBytes(filepath);

                    String jsonString = Security.getDecryptKeyNotes(encryptData, AppConfig.ENC_KEY);

                    //String jsonString = AppConfig.readFromFile(filepath);
                    try {
                        JSONObject json = new JSONObject(jsonString);


                        JSONObject tests = json.getJSONObject("mock_tests");
                        Iterator<String> it = tests.keys();
                        MockTestModel model;
                        while (((Iterator) it).hasNext()) {
                            String key = it.next();
                            model = new MockTestModel();

                            model.questioNo = Integer.parseInt(tests.getString(key));
                            model.title = key;
                            adapter.addMock(model);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                        //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }

                }

            } else {
                if (AppStatus.getInstance(_this).isOnline()) {
                    // checkCookieThenPlay();

                    getMockTest();
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                    // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        } else {
            if (checkPermissionForStorage()) {
                String filepath;

                //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
                filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/mock_test_main.json";

                byte[] encryptData = AppConfig.readFromFileBytes(filepath);

                String jsonString = Security.getDecryptKeyNotes(encryptData, AppConfig.ENC_KEY);

                //String jsonString = AppConfig.readFromFile(filepath);
                try {
                    JSONObject json = new JSONObject(jsonString);


                    JSONObject tests = json.getJSONObject("mock_tests");
                    Iterator<String> it = tests.keys();
                    MockTestModel model;
                    while (((Iterator) it).hasNext()) {
                        String key = it.next();
                        model = new MockTestModel();

                        model.questioNo = Integer.parseInt(tests.getString(key));
                        model.title = key;
                        adapter.addMock(model);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }

        }


        /*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                // checkCookieThenPlay();

                getMockTest();
            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            if (checkPermissionForStorage()) {
                String filepath;

                //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
                filepath = AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/mock_test_main.json";

                byte[] encryptData=AppConfig.readFromFileBytes(filepath);

                String jsonString= Security.getDecryptKeyNotes(encryptData,AppConfig.ENC_KEY);

                //String jsonString = AppConfig.readFromFile(filepath);
                try {
                    JSONObject json = new JSONObject(jsonString);


                    JSONObject tests = json.getJSONObject("mock_tests");
                    Iterator<String> it = tests.keys();
                    MockTestModel model;
                    while (((Iterator) it).hasNext()) {
                        String key = it.next();
                        model = new MockTestModel();

                        model.questioNo = Integer.parseInt(tests.getString(key));
                        model.title = key;
                        adapter.addMock(model);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }
        }*/
    }

    String json_filename;
    boolean isBack = true;

    public void selectedTest(int position, String fileName) {
        AppController.getInstance().playAudio(R.raw.button_click);
        json_filename = fileName;
        recyclerMocktest.setVisibility(View.GONE);
        scrollRuls.setVisibility(View.VISIBLE);
        scrollRuls.clearAnimation();
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_fall_down_slow);
        scrollRuls.setAnimation(anim);
        anim.start();
        json_filename = fileName;
        MOCK_TEST = position;
        isBack = false;
        img_bottom.setVisibility(View.GONE);
    }


    public void startQuiz(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
       /* if (new SharedPref().isExpired(getApplicationContext())) {
            Intent i = new Intent(getApplicationContext(), SubscribePrePage.class);
            i.putExtra("flag", "M");
            startActivity(i);
            return;
        }*/

       /* if(loginType.isEmpty())
        {
            Intent i = new Intent(getApplicationContext(), SubscribeActivity.class);
            i.putExtra("flag", "M");
            startActivity(i);
            return;
        }*/
        loadQuizQuestions(json_filename);
    }

    private void loadQuizQuestions(String filename) {


        if (loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                // checkCookieThenPlay();
                fillWithData();
            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        } else if (loginType.equalsIgnoreCase("O")) {

            if (AppConfig.checkSDCardEnabled(_this, userid, classid) && AppConfig.checkSdcard(classid,getApplicationContext())) {

                if (checkPermissionForStorage()) {
                    String filepath;

                    filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + filename + ".json";


                    new LoadFileAsyn().execute(filepath);
                }
            } else {
                if (AppStatus.getInstance(_this).isOnline()) {
                    // checkCookieThenPlay();
                    fillWithData();
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                    // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        } else {

            if (checkPermissionForStorage()) {
                String filepath;

                filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + filename + ".json";


                new LoadFileAsyn().execute(filepath);
            }
        }

       /* if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                // checkCookieThenPlay();
                fillWithData();
            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
                // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            if (checkPermissionForStorage()) {
                String filepath;

                filepath = AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + filename + ".json";



                new LoadFileAsyn().execute(filepath);
            }
        }*/
    }

    private void fillWithData() {
        String tag_string_req = Constants.reqRegister;
        String URL;

        URL = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/" + json_filename + ".json";


        //Log.v("URL Quize", "URL Quize " + section_id + " url " + URL);

        CustomDialog.showDialog(QuizMockExamActivity.this, true);
        StringRequest strReq = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);
                CustomDialog.closeDialog();
                try {
                    JSONObject json = new JSONObject(response);
                    loadData(json.getJSONObject("quiz").getJSONArray("questions").toString());
                    // jsonObj.getJSONArray("questions");
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        //CustomDialog.showDialog(QuizRulesActivity.this,true);
    }

    private void loadData(String jsonObj) {

        try {

            Intent i = new Intent(_this, QuizQuestionActivity.class);
            i.putExtra(Constants.classId, classid);
            i.putExtra(Constants.userId, userid);
            i.putExtra("json", jsonObj);
            i.putExtra(Constants.subjectId, subjectId);
            i.putExtra(Constants.sectionId, section_id);
            i.putExtra(Constants.lectureId, lectureId);
            i.putExtra(Constants.lectureName, lecture_name);
            i.putExtra(Constants.sectionName, section_name);
            i.putExtra(Constants.mockTest, "M" + (MOCK_TEST + 1));
            i.putExtra("ismock", true);
            startActivityForResult(i, 200);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            isBack = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray shuffleJsonArray(JSONArray array) throws JSONException {
        // Implementing Fisher–Yates shuffle
        Random rnd = new Random();
        for (int i = array.length() - 1; i >= 0; i--) {
            int j = rnd.nextInt(i + 1);
            // Simple swap
            Object object = array.get(j);
            array.put(j, array.get(i));
            array.put(i, object);
        }
        return array;
    }

    private boolean checkPermissionForStorage() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 300:
                if (grantResults.length > 0) {
                    boolean permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (permissionGranted) {
                        String filepath;

                        filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + json_filename;


                        byte[] encryptData = AppConfig.readFromFileBytes(filepath);

                        String jsonString = Security.getDecryptKeyNotes(encryptData, AppConfig.ENC_KEY);
                        //String jsonString = AppConfig.readFromFile(filepath);
                        try {
                            JSONObject json = new JSONObject(jsonString);
                            //loadData(json.getJSONObject("quiz"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showToast(getApplicationContext(), "Access Required");
                        //Toast.makeText(_this, "Access Required", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        if (!isBack) {
            recyclerMocktest.setVisibility(View.VISIBLE);
            scrollRuls.setVisibility(View.GONE);
            scrollRuls.clearAnimation();
            recyclerMocktest.clearAnimation();
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_fall_down_slow);
            recyclerMocktest.setAnimation(anim);
            anim.start();
            img_bottom.setVisibility(View.VISIBLE);
            isBack = true;
            return;
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick({R.id.txt_previous, R.id.btn_previous, R.id.btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_previous:
            case R.id.btn_previous:
                if (loginType.isEmpty()) {
                    Intent i = new Intent(getApplicationContext(), SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    startActivity(i);
                    return;
                }
                previousTest();
                break;
            case R.id.btn_start:
                startQuiz(view);
                break;
        }
    }


    private void getMockTest() {
        String URL = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/mock_test_main.json";
        //Log.v("url","url "+URL);
        CustomDialog.showDialog(QuizMockExamActivity.this, true);
        StringRequest strReq = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);
                CustomDialog.closeDialog();
                try {
                    JSONObject json = new JSONObject(response);
                    // Log.v("MockTest", "MockTest " + json.toString());


                    JSONObject tests = json.getJSONObject("mock_tests");
                    Iterator<String> it = tests.keys();
                    MockTestModel model;
                    while (((Iterator) it).hasNext()) {
                        String key = it.next();
                        model = new MockTestModel();

                        model.questioNo = Integer.parseInt(tests.getString(key));
                        model.title = key;
                        adapter.addMock(model);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
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
        };
        ;
        AppController.getInstance().addToRequestQueue(strReq, "");
    }


    private void previousTest() {
        Intent i = new Intent(_this, MockTestPreviousActivity.class);
        i.putExtra(Constants.classId, classid);
        i.putExtra(Constants.userId, userid);
        i.putExtra(Constants.subjectId, subjectId);
        i.putExtra(Constants.sectionId, section_id);
        i.putExtra(Constants.lectureId, lectureId);
        i.putExtra(Constants.lectureName, lecture_name);
        i.putExtra(Constants.sectionName, section_name);
        i.putExtra(Constants.mockTest, "M" + (MOCK_TEST + 1));
        startActivity(i);
        AppController.getInstance().playAudio(R.raw.qz_next);
        /*overridePendingTransition(R.anim.right_in, R.anim.left_out);

        recyclerMocktest.setVisibility(View.VISIBLE);
        scrollRuls.setVisibility(View.GONE);
        scrollRuls.clearAnimation();
        recyclerMocktest.clearAnimation();*/
        //Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_fall_down_slow);
        //recyclerMocktest.setAnimation(anim);
        //anim.start();
        // img_bottom.setVisibility(View.VISIBLE);
        isBack = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 200 && resultCode == 200) {
            onBackPressed();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    class LoadFileAsyn extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomDialog.showDialog(QuizMockExamActivity.this, true);
        }


        @Override
        protected String doInBackground(String... strings) {
            String filepath = strings[0];
            byte[] encryptData = AppConfig.readFromFileBytes(filepath);

            JSONObject json = null;
            try {
                json = new JSONObject(Security.getDecryptKeyNotes(encryptData, AppConfig.ENC_KEY));
                return json.getJSONObject("quiz").getJSONArray("questions").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                CustomDialog.closeDialog();
                loadData(s);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
