package com.tutorix.tutorialspoint.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.WholeTrackRecordNew;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class TrackMainActivity extends AppCompatActivity {
    TrackMainActivity _this;
    String lectureId;
    String subjectId;
    String classid;
    String section_id;
    private String class_id;
    private WholeTrackRecordNew mAdapter;
    private String userid;
    private String access_token;
    private String lecture_name;

    String loginType;

    RecyclerView recyclerView;
    private static int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    LinearLayoutManager linearLayoutManager;
    TextView txt_date;
    LinearLayout lnr_reload;
    RelativeLayout rel_top_main;
    View lnr_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = TrackMainActivity.this;
        setContentView(R.layout.activity_main_track);
        rel_top_main=findViewById(R.id.rel_top_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        lnr_container=findViewById(R.id.lnr_container);
        setSupportActionBar(toolbar);
        AppController.getInstance().startLayoutAnimation(lnr_container);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setTitle(getResources().getString(R.string.over_all_activity));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        String[] userinfo = SessionManager.getUserInfo(_this);
        access_token = userinfo[1];
        loginType = userinfo[2];
        class_id = userinfo[4];
        txt_date=findViewById(R.id.txt_date);
        lnr_reload=findViewById(R.id.lnr_reload);
        txt_date.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lectureId = extras.getString(Constants.lectureId);
            lecture_name = extras.getString(Constants.lectureName);
            subjectId = extras.getString(Constants.subjectId);
            classid = extras.getString(Constants.classId);
            userid = extras.getString(Constants.userId);
            section_id = extras.getString(Constants.sectionId);

            setupRCV();
            loginType();


            if (subjectId.equalsIgnoreCase("1")) {


                rel_top_main.setBackgroundResource(R.drawable.ic_phy_bg_green);
                //img_background.setImageResource(R.drawable.ic_phy_bg_white);

            } else if (subjectId.equalsIgnoreCase("2")) {


                rel_top_main.setBackgroundResource(R.drawable.ic_chemistry_bg_green);


            } else if (subjectId.equalsIgnoreCase("3")) {


                rel_top_main.setBackgroundResource(R.drawable.ic_bio_bg_green);

            } else {


                rel_top_main.setBackgroundResource(R.drawable.ic_math_bg_green);

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
                   default:
                        window.setStatusBarColor(getResources().getColor(R.color.math_background_status));
                        break;
                }
            }
        }


    }


    public void showDate(String date)
    {

        int lastposition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int firstposition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        int position;
          if(lastposition-firstposition>1)
            position=lastposition;
        else position=firstposition;
        position=position-1;
        if(position>-1&&mAdapter.getItemCount()>0)
        txt_date.setText(CommonUtils.getstringDate(mAdapter.getItem(position).activity_date));
        //txt_date.setText(date);
    }
    private void setupRCV() {
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new WholeTrackRecordNew(_this,userid,class_id);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;

                if(loginType.isEmpty())
                {
                    if (AppStatus.getInstance(_this).isOnline()) {
                        try {
                            prepareTrackData(String.valueOf(currentPage * 20),false);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }else if (loginType.equalsIgnoreCase("O")){

                    if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
                    {

                        MyDatabase dbhelper = MyDatabase.getDatabase(_this);
                        List<TrackModel> trackList;
                        if (getIntent().getStringExtra(Constants.intentType).equalsIgnoreCase(Constants.global)) {
                            trackList = dbhelper.trackDAO().getTrackingDetailsAll(userid, currentPage * 20, 20,classid);
                        } else {
                            trackList = dbhelper.trackDAO().getTrackingDetailsLecture(userid,subjectId,section_id, lectureId,  currentPage * 20, 20,classid);
                        }
                        if (trackList != null && trackList.size() > 0) {
                            if (trackList.size() < 20) {

                            } else {
                                TOTAL_PAGES = TOTAL_PAGES + 1;
                            }
                            mAdapter.removeLoadingFooter();
                            isLoading = false;

                            mAdapter.addAll(trackList);

                            if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
                            else isLastPage = true;
                        } else {
                            mAdapter.removeLoadingFooter();
                            isLoading = false;
                            isLastPage = true;
                        }
                    }else
                    {
                        if (AppStatus.getInstance(_this).isOnline()) {
                            try {
                                prepareTrackData(String.valueOf(currentPage * 20),false);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else
                {

                    MyDatabase dbhelper = MyDatabase.getDatabase(_this);
                    List<TrackModel> trackList;
                    if (getIntent().getStringExtra(Constants.intentType).equalsIgnoreCase(Constants.global)) {
                        trackList = dbhelper.trackDAO().getTrackingDetailsAll(userid, currentPage * 20, 20,classid);
                    } else {
                        trackList = dbhelper.trackDAO().getTrackingDetailsLecture(userid,subjectId,section_id, lectureId,  currentPage * 20, 20,classid);
                    }
                    if (trackList != null && trackList.size() > 0) {
                        if (trackList.size() < 20) {

                        } else {
                            TOTAL_PAGES = TOTAL_PAGES + 1;
                        }
                        mAdapter.removeLoadingFooter();
                        isLoading = false;

                        mAdapter.addAll(trackList);

                        if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
                        else isLastPage = true;
                    } else {
                        mAdapter.removeLoadingFooter();
                        isLoading = false;
                        isLastPage = true;
                    }
                }


            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down

                } else if (dy < 0) {
                    // Scroll Up

                }
            }
        });
    }

    private void loginType() {

        if(/*AppConfig.checkSDCardEnabled(_this,userid,classid)&&*/AppConfig.checkSdcard(classid,getApplicationContext()))
        {


            MyDatabase dbhelper =MyDatabase.getDatabase(_this);
            List<TrackModel> trackList;
            if (getIntent().getStringExtra(Constants.intentType).equalsIgnoreCase(Constants.global)) {
                trackList = dbhelper.trackDAO().getTrackingDetailsAll(userid,  0, 20,classid);
            } else {
                trackList = dbhelper.trackDAO().getTrackingDetailsLecture(userid,subjectId,section_id, lectureId,  0, 20,classid);
            }

            if (trackList != null && trackList.size() > 0) {
                if (trackList.size() < 20) {

                } else {
                    TOTAL_PAGES = TOTAL_PAGES + 1;
                }

                mAdapter.addAll(trackList);
                mAdapter.notifyDataSetChanged();

                if (currentPage == TOTAL_PAGES) {
                    isLastPage = true;
                } else if (currentPage < TOTAL_PAGES) {
                    mAdapter.addLoadingFooter();
                }
            } else {
                // CommonUtils.showToast(getApplicationContext(),"No Tracking Data");
                //  Toasty.warning(_this, "No Tracking Data", Toast.LENGTH_SHORT, true).show();
            }
            nodata();
        }else
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                try {
                    prepareTrackData("0",true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                nodata();
                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
                //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }
      /*  if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                try {
                    prepareTrackData("0",true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                nodata();
                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
                //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
            {


                MyDatabase dbhelper =MyDatabase.getDatabase(_this);
                List<TrackModel> trackList;
                if (getIntent().getStringExtra(Constants.intentType).equalsIgnoreCase(Constants.global)) {
                    trackList = dbhelper.trackDAO().getTrackingDetailsAll(userid,  0, 20,classid);
                } else {
                    trackList = dbhelper.trackDAO().getTrackingDetailsLecture(userid,subjectId,section_id, lectureId,  0, 20,classid);
                }

                if (trackList != null && trackList.size() > 0) {
                    if (trackList.size() < 20) {

                    } else {
                        TOTAL_PAGES = TOTAL_PAGES + 1;
                    }

                    mAdapter.addAll(trackList);
                    mAdapter.notifyDataSetChanged();

                    if (currentPage == TOTAL_PAGES) {
                        isLastPage = true;
                    } else if (currentPage < TOTAL_PAGES) {
                        mAdapter.addLoadingFooter();
                    }
                } else {
                    // CommonUtils.showToast(getApplicationContext(),"No Tracking Data");
                    //  Toasty.warning(_this, "No Tracking Data", Toast.LENGTH_SHORT, true).show();
                }
                nodata();
            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        prepareTrackData("0",true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    nodata();
                    CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
                    //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        }else
        {


            MyDatabase dbhelper =MyDatabase.getDatabase(_this);
            List<TrackModel> trackList;
            if (getIntent().getStringExtra(Constants.intentType).equalsIgnoreCase(Constants.global)) {
                trackList = dbhelper.trackDAO().getTrackingDetailsAll(userid,  0, 20,classid);
            } else {
                trackList = dbhelper.trackDAO().getTrackingDetailsLecture(userid,subjectId,section_id, lectureId,  0, 20,classid);
            }

            if (trackList != null && trackList.size() > 0) {
                if (trackList.size() < 20) {

                } else {
                    TOTAL_PAGES = TOTAL_PAGES + 1;
                }

                mAdapter.addAll(trackList);
                mAdapter.notifyDataSetChanged();

                if (currentPage == TOTAL_PAGES) {
                    isLastPage = true;
                } else if (currentPage < TOTAL_PAGES) {
                    mAdapter.addLoadingFooter();
                }
            } else {
                // CommonUtils.showToast(getApplicationContext(),"No Tracking Data");
                //  Toasty.warning(_this, "No Tracking Data", Toast.LENGTH_SHORT, true).show();
            }
            nodata();
        }*/


    }

    private void prepareTrackData(final String Offset,boolean isShow) throws UnsupportedEncodingException {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {
            if (getIntent().getStringExtra(Constants.intentType).equalsIgnoreCase(Constants.global)) {
                fjson.put("entity_type", "A");
                fjson.put("entity_id", "0");
            } else {
                fjson.put("entity_type", "L");
                fjson.put("lecture_id", lectureId);
                fjson.put("subject_id", subjectId);
                fjson.put("section_id", section_id);
            }
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put("offset", Offset);
            fjson.put("limit", "20");
            fjson.put(Constants.accessToken, access_token);
            //Log.v("Request","Request "+fjson.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        Log.e("Request","Request "+Constants.USER_TRACK + "/" + userid + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"));
        StringRequest strReq = new StringRequest(Request.Method.GET,
                 Constants.USER_TRACK + "/" + userid + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(Constants.response, response);
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                JSONArray answers = jObj.getJSONArray(Constants.trackData);
                                for (int i = 0; i < answers.length(); i++) {

                                    JSONObject json_data = answers.getJSONObject(i);
                                    TrackModel chapters = new TrackModel();
                                    chapters.activity_type = json_data.getString("activity_type");
                                    chapters.activity_duration = json_data.getString("activity_duration");
                                    chapters.activity_date = json_data.getString("activity_date");
                                    chapters.quiz_id = json_data.getString("quiz_id");


                                    if (getIntent().getStringExtra(Constants.intentType).equalsIgnoreCase(Constants.global)) {
                                        chapters.subject_id = json_data.getString(Constants.subjectId);
                                        chapters.section_id = json_data.getString(Constants.sectionId);
                                        chapters.lecture_id = json_data.getString(Constants.lectureId);
                                        chapters.class_id = json_data.getString(Constants.classId);
                                        if(json_data.has("lecture_name"))
                                        chapters.lecture_name = json_data.getString(Constants.lectureName);
                                    } else {
                                        chapters.lecture_id = lectureId;
                                        chapters.subject_id = subjectId;
                                        chapters.class_id = classid;
                                        chapters.section_id = section_id;
                                        chapters.user_id = userid;
                                        chapters.lecture_name =lecture_name;
                                    }

                                    chapters.user_id = userid;
                                    movieList.add(chapters);
                                    if(movieList.size()==0)
                                    {
                                        showDate(CommonUtils.getstringDate(movieList.get(0).activity_date));
                                    }
                                }

                                if (answers.length() < 20) {

                                } else {
                                    TOTAL_PAGES = TOTAL_PAGES + 1;
                                }

                                if (Offset.equalsIgnoreCase("0")) {
                                    mAdapter.addAll(movieList);
                                    mAdapter.notifyDataSetChanged();

                                    if (currentPage == TOTAL_PAGES) {
                                        isLastPage = true;
                                    } else if (currentPage < TOTAL_PAGES) {
                                        mAdapter.addLoadingFooter();
                                    }
                                } else {
                                    mAdapter.removeLoadingFooter();
                                    isLoading = false;

                                    mAdapter.addAll(movieList);

                                    if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
                                    else isLastPage = true;
                                }
                            } else {
                                if (Offset.equalsIgnoreCase("0")) {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                } else {
                                    mAdapter.removeLoadingFooter();
                                    isLoading = false;
                                    isLastPage = true;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                        nodata();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                String msg="";
                nodata();
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



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        if(isShow)
        CustomDialog.showDialog(TrackMainActivity.this,false);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppController.getInstance().playAudio(R.raw.back_sound);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void nodata()
    {
        if(mAdapter!=null&&mAdapter.getItemCount()>0)
            lnr_reload.setVisibility(View.GONE);
            else lnr_reload.setVisibility(View.VISIBLE);
    }
}
