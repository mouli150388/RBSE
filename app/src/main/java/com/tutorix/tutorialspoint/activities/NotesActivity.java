package com.tutorix.tutorialspoint.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.google.android.material.snackbar.Snackbar;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.BuildConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class NotesActivity extends AppCompatActivity {
    NotesActivity _this;
    private String start;
    private String lectureId;
    private String subjectId;
    private String userid;
    private String section_id;
    private String lecture_name="";
    private String classId;
    private WebView webview;
    private String access_token;
    String loginType;
    View lnr_home;
    String mathLib="";
    View lnr_container;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = NotesActivity.this;
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        lnr_home = findViewById(R.id.lnr_home);
        lnr_container=findViewById(R.id.lnr_container);
        AppController.getInstance().startLayoutAnimation(lnr_container);
        setSupportActionBar(toolbar);
       // toolbar.setBackgroundResource(R.drawable.rectangle_gradient_home_actionabr);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notes");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                AppController.getInstance().playAudio(R.raw.back_sound);
            }
        });

        String assets= "file:///android_asset";
        mathLib=Constants.MathJax_Offline;
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userid = userInfo[0];;
        loginType = userInfo[2];

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        start = dateFormat.format(date);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lectureId = extras.getString(Constants.lectureId);
            lecture_name = extras.getString(Constants.lectureName);
            subjectId = extras.getString(Constants.subjectId);
            classId = extras.getString(Constants.classId);
            userid = extras.getString(Constants.userId);
            section_id = extras.getString(Constants.sectionId);
            getSupportActionBar().setTitle(lecture_name);
            if(subjectId.equals("1"))
                toolbar.setBackgroundResource((R.drawable.ic_phy_bg_green));
            else  if(subjectId.equals("2"))
                toolbar.setBackgroundResource(( R.drawable.ic_chemistry_bg_green));
            else  if(subjectId.equals("3"))
                toolbar.setBackgroundResource((R.drawable.ic_bio_bg_green));
            else  if(subjectId.equals("4"))
                toolbar.setBackgroundResource((R.drawable.ic_math_bg_green));

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
            webViewContent();
            //webview.setWebViewClient(new CustWebviewClient());
            loginType();

        }
        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home(v);
            }
        });
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
        }*/
    }
    public void home(View v)
    {

        Intent in=new Intent(NotesActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        onBackPressed();

    }
    @SuppressLint("SetJavaScriptEnabled")
    private void webViewContent() {
        webview = findViewById(R.id.wv_content);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollContainer(false);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        final WebSettings settings = webview.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);

        webview.setVerticalScrollBarEnabled(true);
        webview.clearCache(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    if(this!=null)
                    CustomDialog.showDialog(NotesActivity.this, true);
                }catch (Exception e)
                {

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                super.shouldOverrideUrlLoading(view, url);
                return false;
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                super.onPageFinished(view, url);
                try {
                    CustomDialog.closeDialog();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                webview.requestLayout();
            }

        });
    }

    private void loginType() {


        if(AppConfig.checkSdcard(classId,getApplicationContext()))
        {

            if (checkPermissionForStorage()) {
                String sdCardpath=AppConfig.getSdCardPath(classId,getApplicationContext());
                if ( sdCardpath!= null) {
                    String filepath = sdCardpath + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.NOTE_FILE;



                    byte[] encryptData=AppConfig.readFromFileBytes(filepath);
                    String data= Security.getDecryptKeyNotes(encryptData,AppConfig.ENC_KEY);
                    /* String data= AppConfig.readFromFile(filepath);*/
                    loadDataBaseURL(data);

                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));
                    //Toasty.info(_this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
                }
            } else {
                requestPermissionForStorage();
            }
        }else
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                checkCookieThenPlay();

            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }

       /* if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                checkCookieThenPlay();

            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userid,classId)&&AppConfig.checkSdcard(classId,getApplicationContext()))
            {

                if (checkPermissionForStorage()) {
                    String sdCardpath=AppConfig.getSdCardPath(classId,getApplicationContext());
                    if ( sdCardpath!= null) {
                        String filepath = sdCardpath + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.NOTE_FILE;



                        byte[] encryptData=AppConfig.readFromFileBytes(filepath);
                        String data= Security.getDecryptKeyNotes(encryptData,AppConfig.ENC_KEY);
                        *//* String data= AppConfig.readFromFile(filepath);*//*
                        loadDataBaseURL(data);

                    } else {
                        CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));
                        //Toasty.info(_this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    requestPermissionForStorage();
                }
            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    checkCookieThenPlay();

                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        }else
        {


            if (checkPermissionForStorage()) {
                String sdCardpath=AppConfig.getSdCardPath(classId,getApplicationContext());
                if ( sdCardpath!= null) {
                    String filepath = sdCardpath + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.NOTE_FILE;



                    byte[] encryptData=AppConfig.readFromFileBytes(filepath);
                    String data= Security.getDecryptKeyNotes(encryptData,AppConfig.ENC_KEY);
                    *//* String data= AppConfig.readFromFile(filepath);*//*
                    loadDataBaseURL(data);

                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));
                    //Toasty.info(_this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
                }
            } else {
                requestPermissionForStorage();
            }
        }*/
        /*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                checkCookieThenPlay();

            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            if (checkPermissionForStorage()) {
                String sdCardpath=AppConfig.getSdCardPath(classId);
                if ( sdCardpath!= null) {
                    String filepath = sdCardpath + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.NOTE_FILE;



                    byte[] encryptData=AppConfig.readFromFileBytes(filepath);
                    String data= Security.getDecryptKeyNotes(encryptData,AppConfig.ENC_KEY);
                   *//* String data= AppConfig.readFromFile(filepath);*//*
                  loadDataBaseURL(data);

                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));
                    //Toasty.info(_this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
                }
            } else {
                requestPermissionForStorage();
            }
        }*/
    }

    private void fillWithData() {
        String tag_string_req = Constants.reqRegister;
        String URL = AppConfig.getOnlineURL(classId, false) + subjectId + "/" + section_id + "/" + lectureId + "/" + Constants.NOTE_FILE;
      // final  String filePath = AppConfig.getOnlineURLImage(classId) + subjectId + "/" + section_id + "/" + lectureId;
        Log.d("Notes URL",""+URL);
       final  String filePath = AppConfig.getOnlineURLImage(classId) ;

        StringRequest strReq = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(Constants.response, response);

                try {
                    CustomDialog.closeDialog();
                    loadData(response,filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="";
                CustomDialog.closeDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg=getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg="Coming Soon";
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
        })
        {
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
        CustomDialog.showDialog(NotesActivity.this,true);
    }


    private void loadData(String notesData,String filePath) {
        // webview.loadUrl("https://firebasestorage.googleapis.com/v0/b/kapsule-7234.appspot.com/o/notes1.html?alt=media&token=dabc927d-b00a-416e-99d6-2b2695ad3c4b");


        webview.loadDataWithBaseURL(filePath,mathLib+notesData, "text/html", "utf-8","");
        //webview.loadData(notesData, "text/html", "UTF-8");
        // webview.loadData(getHtmlData("<div>" + notesData + "</div>"), "text/html; charset=utf-8", "utf-8");

    }



    @Override
    public void onBackPressed() {
        try{
            webview.clearFormData();
            webview.clearHistory();

        }catch (Exception e)
        {

        }
        super.onBackPressed();
        @SuppressLint("SimpleDateFormat")
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

        long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
        difference=difference+video_millese;
        if(difference<0)
            difference=-difference;
        long diffSeconds = difference / 1000 % 60;
        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        //Log.d("difference", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);


        if(diffHours>=1)
        {
            diffHours=0;
            diffMinutes=59;
            diffSeconds=0;
        }
        String a = formatter.format(diffHours) + ":" + formatter.format(diffMinutes) + ":" + formatter.format(diffSeconds);
        if(diffHours>0||diffMinutes>0||diffSeconds>=30)
        time(a);
    }

    private void time(final String a) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userid);
            fjson.put("activity_type", "N");
            fjson.put("subject_id", subjectId);
            fjson.put("activity_duration", a);
            fjson.put("lecture_id", lectureId);
            fjson.put("section_id", section_id);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.lectureName, lecture_name);

            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;


        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BASE_URL + "users/track", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(Constants.response, response);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean(Constants.errorFlag);
                        if (!error) {
                            String errorMsg = jObj.getString(Constants.message);

                            if (AppConfig.checkSDCardEnabled(_this,userid,classId)&&AppConfig.checkSdcard(classId,getApplicationContext())) {
                                MyDatabase database = MyDatabase.getDatabase(_this);
                                TrackModel chapters = new TrackModel();
                                chapters.activity_type = "N";
                                chapters.activity_duration =a;
                                chapters.activity_date = getDateTime();
                                chapters.quiz_id = "";
                                chapters.subject_id = subjectId;
                                chapters.section_id = section_id;
                                chapters.lecture_id =lectureId;
                                chapters.class_id = classId;
                                chapters.user_id =userid;
                                chapters.lecture_name = lecture_name;
                                chapters.is_sync = true;
                                chapters.duration_insec=CommonUtils.getSeconds(chapters.activity_duration);
                                database.trackDAO().insertTrack(chapters);
                            }
                            finish();
                        } else {
                            String errorMsg = jObj.getString(Constants.message);
                            //CommonUtils.showToast(getApplicationContext(), errorMsg);
                            if (AppConfig.checkSDCardEnabled(_this,userid,classId)&&AppConfig.checkSdcard(classId,getApplicationContext())) {
                                MyDatabase database = MyDatabase.getDatabase(_this);
                                TrackModel chapters = new TrackModel();
                                chapters.activity_type = "N";
                                chapters.activity_duration =a;
                                chapters.activity_date = getDateTime();
                                chapters.quiz_id = "";
                                chapters.subject_id = subjectId;
                                chapters.section_id = section_id;
                                chapters.lecture_id =lectureId;
                                chapters.class_id = classId;
                                chapters.user_id =userid;
                                chapters.lecture_name = lecture_name;
                                chapters.is_sync = false;
                                chapters.duration_insec=CommonUtils.getSeconds(chapters.activity_duration);
                                database.trackDAO().insertTrack(chapters);
                            }
                            finish();
                            // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                        // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CommonUtils.showToast(getApplicationContext(), error.getMessage());
                    if (AppConfig.checkSDCardEnabled(_this,userid,classId)&&AppConfig.checkSdcard(classId,getApplicationContext())) {
                        MyDatabase database = MyDatabase.getDatabase(_this);
                        TrackModel chapters = new TrackModel();
                        chapters.activity_type = "N";
                        chapters.activity_duration =a;
                        chapters.activity_date = getDateTime();
                        chapters.quiz_id = "";
                        chapters.subject_id = subjectId;
                        chapters.section_id = section_id;
                        chapters.lecture_id =lectureId;
                        chapters.class_id = classId;
                        chapters.user_id =userid;
                        chapters.lecture_name = lecture_name;
                        chapters.is_sync = false;
                        chapters.duration_insec=CommonUtils.getSeconds(chapters.activity_duration);
                        database.trackDAO().insertTrack(chapters);
                    }
                    finish();
                    // Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }) {
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
            /*if (AppConfig.checkSDCardEnabled(_this,userid,classId)&&AppConfig.checkSdcard(classId))
            {

                MyDatabase database = MyDatabase.getDatabase(_this);
                TrackModel chapters = new TrackModel();
                chapters.activity_type = "N";
                chapters.activity_duration =a;
                chapters.activity_date = getDateTime();
                chapters.quiz_id = "";
                chapters.subject_id = subjectId;
                chapters.section_id = section_id;
                chapters.lecture_id =lectureId;
                chapters.class_id = classId;
                chapters.user_id =userid;
                chapters.lecture_name = lecture_name;
                chapters.is_sync = false;
                chapters.duration_insec=CommonUtils.getSeconds(chapters.activity_duration);
                database.trackDAO().insertTrack(chapters);
            }*/
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            MyDatabase database = MyDatabase.getDatabase(_this);


            TrackModel chapters = new TrackModel();
            chapters.activity_type = "N";
            chapters.activity_duration =a;
            chapters.activity_date = getDateTime();
            chapters.quiz_id = "";
            chapters.subject_id = subjectId;
            chapters.section_id = section_id;
            chapters.lecture_id =lectureId;
            chapters.class_id = classId;
            chapters.user_id =userid;
            chapters.lecture_name = lecture_name;
            chapters.is_sync = false;
            chapters.duration_insec=CommonUtils.getSeconds(chapters.activity_duration);
            database.trackDAO().insertTrack(chapters);
        }
    }

    private String getDateTime() {

        Date date = new Date();
        return CommonUtils.format.format(date);
    }
    DecimalFormat formatter = new DecimalFormat("00");
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                @SuppressLint("SimpleDateFormat")
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

                long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
                if(difference<0)
                    difference=-difference;
                long diffSeconds = difference / 1000 % 60;
                long diffMinutes = difference / (60 * 1000) % 60;
                long diffHours = difference / (60 * 60 * 1000) % 24;
                //Log.d("difference", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);
                if(diffHours>=1)
                {
                    diffHours=0;
                    diffMinutes=59;
                    diffSeconds=0;
                }
                String a = formatter.format(diffHours) + ":" + formatter.format(diffMinutes) + ":" + formatter.format(diffSeconds);
                time(a);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean checkPermissionForStorage() {
        if(Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                   startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                   startActivity(intent);
                                }
                            }
                        })
                        .show();
                return  false;
            } else return true;
        }else {
            int  result = ContextCompat.checkSelfPermission(NotesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissionForStorage() {
        if (Build.VERSION.SDK_INT >= 30)
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE,}, 300);
      else  ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 300:
                if (grantResults.length > 0) {
                    boolean galleryaccepted=false;
                    if (Build.VERSION.SDK_INT >= 30)
                         galleryaccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED)&&(grantResults[1] == PackageManager.PERMISSION_GRANTED);
                    else
                         galleryaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (galleryaccepted) {
                        String sdCardpath="";
                        if ((sdCardpath=AppConfig.getSdCardPath(classId,getApplicationContext())) != null) {
                            String filepath = sdCardpath + subjectId + "/" + section_id + "/" + lectureId + "/" +Constants.NOTE_FILE;
                             byte[] encryptData=AppConfig.readFromFileBytes(filepath);
                           String data= Security.getDecryptKeyNotes(encryptData,AppConfig.ENC_KEY);

                             //String data=AppConfig.readFromFile(filepath);
                            loadDataBaseURL(data);
                        } else {
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));
                            // Toasty.info(_this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
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

    private void loadDataBaseURL(String data)
    {
        String filePath="file://"+ AppConfig.getSdCardPath(classId,getApplicationContext()) /*+ subjectId + "/" + section_id + "/" + lectureId*/;

        if(!filePath.trim().endsWith("/"))
            filePath=filePath+"/";
        webview.loadDataWithBaseURL(filePath,mathLib+data, "text/html", "UTF-8","");

    }
    private void checkCookieThenPlay()
    {
        fillWithData();/*
        String encryption="";
        String  encryption2="";
        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classId);

            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption2= URLEncoder.encode(encryption,"utf-8");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, Constants.CHECK_COOKIES+"?json_data="+encryption2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomDialog.closeDialog();
                fillWithData();

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
                CustomDialog.closeDialog();
                finish();
            }
        });*/
        //CustomDialog.showDialog(NotesActivity.this,true);
        //AppController.getInstance().addToRequestQueue(request);

    }
    @Override
    protected void onResume() {
        super.onResume();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        start = dateFormat.format(date);




    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        start = dateFormat.format(date);

    }

    @Override
    protected void onPause() {
        super.onPause();
        timeCal();
    }
    long video_millese;
    private void timeCal()
    {
        try{
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
            long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
            video_millese=video_millese+difference;
            //Log.v("video_millese","video_millese "+video_millese);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
