package com.tutorix.tutorialspoint.quiz;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class QuizRulesActivity extends AppCompatActivity {

    QuizRulesActivity _this;
    Intent menu = null;
    String loginType;
    //String UrlForInsideQuizImage = "";
    LinearLayout lnr_top;
    LinearLayout lnr_container;
    ImageView img_logo_subj;
    TextView txt_header;
    TextView love_music;
    TextView txt_view;
    TextView txt_time;
    TextView txt_ans;
    TextView txt_previous;
    Button btn_start;
    ImageView img_background;
    RelativeLayout rel_top_main;
    Drawable dr1;
    Drawable dr2;
    Drawable dr3;
    Drawable dr4;
    String tag_string_req = Constants.reqRegister;
    private JSONArray quesList = null;
    private String lectureId;
    private String lecture_name;
    private String section_name;
    private String subjectId;
    private String userid;
    private String section_id;
    private String classid;

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

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.setLanguages(this);
    }

    public JSONArray getQuesList() {
        return quesList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = QuizRulesActivity.this;
        setContentView(R.layout.activity_quiz_rules);


        lnr_container = findViewById(R.id.lnr_container);
        AppController.getInstance().startLayoutAnimation(lnr_container);

        txt_header = findViewById(R.id.txt_header);
        lnr_top = findViewById(R.id.lnr_top);
        rel_top_main = findViewById(R.id.rel_top_main);
        img_background = findViewById(R.id.img_background);
        btn_start = findViewById(R.id.btn_start);
        love_music = findViewById(R.id.love_music);
        img_logo_subj = findViewById(R.id.img_logo_subj);
        txt_view = findViewById(R.id.txt_view);
        txt_time = findViewById(R.id.txt_time);
        txt_ans = findViewById(R.id.txt_ans);
        txt_previous = findViewById(R.id.txt_previous);
        String[] userinfo = SessionManager.getUserInfo(_this);

        btn_start.setSoundEffectsEnabled(false);
        loginType = userinfo[2];


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lecture_name = extras.getString(Constants.lectureName);
            section_name = extras.getString(Constants.sectionName);
            lectureId = extras.getString(Constants.lectureId);
            subjectId = extras.getString(Constants.subjectId);
            classid = extras.getString(Constants.classId);
            userid = extras.getString(Constants.userId);
            section_id = extras.getString(Constants.sectionId);

            //Log.v("On Quize","onQuize 11  "+subjectId);
        }
        if (subjectId.equalsIgnoreCase("1")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_phy).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_logo_subj);
            rel_top_main.setBackgroundResource(R.drawable.ic_phy_bg_green);
            //img_background.setImageResource(R.drawable.ic_phy_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_phy_bg_white).into(img_background);
            dr1 = getResources().getDrawable(R.drawable.ic_r1_phy);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_phy);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_phy);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_phy);
            btn_start.setBackgroundResource(R.drawable.ic_phy_notes);
            btn_start.setTextColor(getResources().getColor(R.color.phy_background));
            txt_header.setText(getString(R.string.physics));
        } else if (subjectId.equalsIgnoreCase("2")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_che).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_logo_subj);
            rel_top_main.setBackgroundResource(R.drawable.ic_chemistry_bg_green);
            //img_background.setImageResource(R.drawable.ic_chemistry_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
            dr1 = getResources().getDrawable(R.drawable.ic_r1_che);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_che);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_che);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_che);
            btn_start.setBackgroundResource(R.drawable.ic_che_notes);
            btn_start.setTextColor(getResources().getColor(R.color.che_background));
            txt_header.setText(getString(R.string.chemistry));

        } else if (subjectId.equalsIgnoreCase("3")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_bio).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_logo_subj);
            rel_top_main.setBackgroundResource(R.drawable.ic_bio_bg_green);
            //img_background.setImageResource(R.drawable.ic_bio_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_bio_bg_white).into(img_background);
            dr1 = getResources().getDrawable(R.drawable.ic_r1_bio);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_bio);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_bio);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_bio);
            btn_start.setBackgroundResource(R.drawable.ic_bio_notes);
            btn_start.setTextColor(getResources().getColor(R.color.bio_background));
            txt_header.setText(getString(R.string.biology));
        } else {

            Glide.with(getApplicationContext()).load(R.drawable.gif_math).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_logo_subj);
            rel_top_main.setBackgroundResource(R.drawable.ic_math_bg_green);
            //img_background.setImageResource(R.drawable.ic_math_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_math_bg_white).into(img_background);
            dr1 = getResources().getDrawable(R.drawable.ic_r1_math);
            dr2 = getResources().getDrawable(R.drawable.ic_r2_math);
            dr3 = getResources().getDrawable(R.drawable.ic_r3_math);
            dr4 = getResources().getDrawable(R.drawable.ic_r4_math);
            btn_start.setBackgroundResource(R.drawable.ic_math_notes);
            btn_start.setTextColor(getResources().getColor(R.color.math_background));
            txt_header.setText(getString(R.string.mathematics));
        }

        txt_view.setCompoundDrawablesWithIntrinsicBounds(dr1, null, null, null);
        txt_ans.setCompoundDrawablesWithIntrinsicBounds(dr2, null, null, null);
        txt_time.setCompoundDrawablesWithIntrinsicBounds(dr3, null, null, null);
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
        }*/
        setbackground();
    }

    private void setbackground() {/*
        switch (subjectId)
        {
            case "1":
                lnr_top.setBackgroundColor(getResources().getColor(R.color.phy_background));
               // img_logo_subj.setImageResource(R.drawable.physics);
                Glide.with(getApplicationContext()).load(R.drawable.gif).into(img_logo_subj);
                love_music.setText(getString(R.string.physics));
                btn_start.setBackgroundResource(R.drawable.button_selector_phy);
                txt_ans.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_touch,0,0,0);
                txt_view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_faq,0,0,0);
                txt_time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_time,0,0,0);
                break;
            case "2":
                lnr_top.setBackgroundColor(getResources().getColor(R.color.che_background));
                //img_logo_subj.setImageResource(R.drawable.chemistry);
                Glide.with(getApplicationContext()).load(R.drawable.gif_che).into(img_logo_subj);
                love_music.setText(getString(R.string.chemistry));
                btn_start.setBackgroundResource(R.drawable.button_selector);
                txt_ans.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_touch_che,0,0,0);
                txt_view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_faq_che,0,0,0);
                txt_time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_time_che,0,0,0);
                break;
            case "3":
                lnr_top.setBackgroundColor(getResources().getColor(R.color.bio_background));
               // img_logo_subj.setImageResource(R.drawable.biology);
                Glide.with(getApplicationContext()).load(R.drawable.gif_bio).into(img_logo_subj);
                love_music.setText(getString(R.string.biology));
                btn_start.setBackgroundResource(R.drawable.button_selector_bio);
                txt_ans.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_touch_bio,0,0,0);
                txt_view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_faq_bio,0,0,0);
                txt_time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_time_bio,0,0,0);
                break;
            case "4":
                lnr_top.setBackgroundColor(getResources().getColor(R.color.math_background));
                //img_logo_subj.setImageResource(R.drawable.maths);
                Glide.with(getApplicationContext()).load(R.drawable.gif_math).into(img_logo_subj);
                love_music.setText(getString(R.string.mathematics));
                btn_start.setBackgroundResource(R.drawable.button_selector_math);
                txt_ans.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_touch_math,0,0,0);
                txt_view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_faq_math,0,0,0);
                txt_time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_time_math,0,0,0);
                break;
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subjectId)
            {
                case "1":
                    window.setStatusBarColor(getResources().getColor(R.color.phy_background));
                    break;
                case "2":
                    window.setStatusBarColor(getResources().getColor(R.color.che_background));
                    break;
                case "3":
                    window.setStatusBarColor(getResources().getColor(R.color.bio_background));
                    break;
                case "4":
                    window.setStatusBarColor(getResources().getColor(R.color.math_background));
                    break;
            }
        }*/
    }

    public void startQuiz(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        loadQuizQuestions();


    }

    private void loadQuizQuestions() {

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
                    if (lectureId.equals("0")) {
                        //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/";
                        filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + Constants.QUIZ_FILE;
                    } else {
                        //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
                        filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.QUIZ_FILE;
                    }

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
                if (lectureId.equals("0")) {
                    //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/";
                    filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + Constants.QUIZ_FILE;
                } else {
                    //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
                    filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.QUIZ_FILE;
                }

                new LoadFileAsyn().execute(filepath);

            }
        }
/*
        if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                // checkCookieThenPlay();
                fillWithData();
            } else {
                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
                // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            if (checkPermissionForStorage()) {
                String filepath;
                if (lectureId.equals("0")) {
                    //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/";
                    filepath = AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + Constants.QUIZ_FILE;
                } else {
                    //UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
                    filepath = AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.QUIZ_FILE;
                }

                new LoadFileAsyn().execute(filepath);

            }
        }*/
    }

    private void fillWithData() {

        String URL;
        if (lectureId.equalsIgnoreCase("0")) {
            URL = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/" + Constants.QUIZ_FILE;
            // UrlForInsideQuizImage = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id + "/";

        } else {
            URL = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.QUIZ_FILE;
            // UrlForInsideQuizImage = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
        }


        //Log.v("URL Quize","URL Quize "+section_id+" url "+URL);

        CustomDialog.showDialog(QuizRulesActivity.this, true);
        StringRequest strReq = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);
                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject json = new JSONObject(response);
                    loadData(json.getJSONObject("quiz"));
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String msg = "";

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = "Coming Soon";
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
                            StandardCharsets.UTF_8);
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        //CustomDialog.showDialog(QuizRulesActivity.this,true);
    }

    private void loadData(JSONObject jsonObj) {

        try {
            /* This is for Replacing Images inside quiz json*/
            //  int length = jsonObj.getJSONArray("questions").length();
            //Log.v("Resposne Json ","Resposne Json  "+jsonObj.toString());

//CustomDialog.showDialog(QuizRulesActivity.this,true);

           /* if (lectureId.equalsIgnoreCase("0")) {
                JSONArray randomJsonArray = new JSONArray();
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 0; i < jsonObj.getJSONArray("questions").length(); i++) {
                    list.add(i);
                }
                Collections.shuffle(list);

                List<Integer> finalShuffledList = list.subList(0, 20);

                for (int j = 0; j <= jsonObj.getJSONArray("questions").length(); j++) {
                    if (finalShuffledList.contains(j)) {
                        randomJsonArray.put(jsonObj.getJSONArray("questions").getJSONObject(j));
                    }
                }
                quesList = randomJsonArray;
            } else {*/
            quesList = jsonObj.getJSONArray("questions");
            // }

            // CustomDialog.closeDialog();
            Intent i = new Intent(_this, QuizQuestionActivity.class);
            i.putExtra(Constants.classId, classid);
            i.putExtra(Constants.userId, userid);
            i.putExtra("json", quesList.toString());
            i.putExtra(Constants.subjectId, subjectId);
            i.putExtra(Constants.sectionId, section_id);
            i.putExtra(Constants.lectureId, lectureId);
            i.putExtra(Constants.lectureName, lecture_name);
            i.putExtra(Constants.sectionName, section_name);
            i.putExtra(Constants.mockTest, "");
            i.putExtra("ismock", false);
            startActivityForResult(i, 200);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermissionForStorage() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 300) {
            if (grantResults.length > 0) {
                boolean permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (permissionGranted) {
                    String filepath;
                    if (getIntent().getStringExtra("lectureId").equalsIgnoreCase("0")) {
                        filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + Constants.QUIZ_FILE;
                        // UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/";
                    } else {
                        // UrlForInsideQuizImage = "file://" + AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lectureId + "/";
                        filepath = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.QUIZ_FILE;
                    }
                    byte[] encryptData = AppConfig.readFromFileBytes(filepath);

                    String jsonString = Security.getDecryptKeyNotes(encryptData, AppConfig.ENC_KEY);
                    //String jsonString = AppConfig.readFromFile(filepath);
                    try {
                        JSONObject json = new JSONObject(jsonString);
                        loadData(json.getJSONObject("quiz"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.showToast(getApplicationContext(), "Access Required");
                    //Toast.makeText(_this, "Access Required", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void goBack(View view) {
        onBackPressed();
        AppController.getInstance().playAudio(R.raw.back_sound);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void checkCookieThenPlay() {
        String encryption = "";
        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classid);
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption = URLEncoder.encode(encryption, StandardCharsets.UTF_8);
            //Log.v("fjson ", "Json request " + fjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //CustomDialog.showDialog(NotesActivity.this,true);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.CHECK_COOKIES + "?json_data=" + encryption, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fillWithData();
                //Log.v("Resposne","Resposne "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.v("Resposne","Resposne error "+error.getMessage());
                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                            StandardCharsets.UTF_8);
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

    }

    public void home(View view) {
        Intent in = new Intent(QuizRulesActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Log.v("OnActivity Result","OnActivity Result "+requestCode+" "+resultCode);
        if (requestCode == 200 && resultCode == 200) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class LoadFileAsyn extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomDialog.showDialog(QuizRulesActivity.this, true);
        }


        @Override
        protected String doInBackground(String... strings) {
            String filepath = strings[0];
            byte[] encryptData = AppConfig.readFromFileBytes(filepath);


            return Security.getDecryptKeyNotes(encryptData, AppConfig.ENC_KEY);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject json = new JSONObject(s);
                loadData(json.getJSONObject("quiz"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
