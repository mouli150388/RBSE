package com.tutorix.tutorialspoint.classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.WholeTrackRecordNew;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
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

public class StudentTrackActivity extends AppCompatActivity {

    Activity _this;


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
    SwipeRefreshLayout swipe_refresh;
    LinearLayout lnr_reload;
    String selected_user_id;
    String selected_class_id;
    String student_name;
    ImageView img_back;
    TextView txt_header;
    TextView txt_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_track);
        _this=StudentTrackActivity.this;
        selected_user_id=getIntent().getStringExtra("selected_user_id");
        selected_class_id=getIntent().getStringExtra("selected_class_id");
        student_name=getIntent().getStringExtra("student_name");
        img_back=findViewById(R.id.img_back);
        txt_header=findViewById(R.id.txt_header);
        txt_name=findViewById(R.id.txt_name);
        txt_header.setText("Activity Tracker");
        txt_name.setText(student_name);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadData();
    }
    private void loadData()
    {
        String[] userinfo = SessionManager.getUserInfo(_this);
        access_token = userinfo[1];
        loginType = userinfo[2];
        class_id = userinfo[4];
        userid = userinfo[0];
        txt_date=findViewById(R.id.txt_date);
        swipe_refresh=findViewById(R.id.swipe_refresh);
        lnr_reload=findViewById(R.id.lnr_reload);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh.setRefreshing(false);
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                PAGE_START = 0;
                isLoading = false;
                isLastPage = false;
                TOTAL_PAGES = 0;
                currentPage = PAGE_START;
                loginType();
            }
        });
        lnr_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType();
            }
        });
        txt_date.setVisibility(View.GONE);


        setupRCV();
        loginType();

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
        Log.v("selected_user_id","selected_user_id11 "+selected_user_id+" selected_class_id "+selected_class_id);

        mAdapter = new WholeTrackRecordNew(_this,selected_user_id,selected_class_id);
        linearLayoutManager = new LinearLayoutManager(_this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        prepareTrackData(String.valueOf(currentPage * 20),false);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
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

        if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                try {
                    prepareTrackData("0",true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                checkEmptyData();
                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
                //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userid,class_id)&&AppConfig.checkSdcard(class_id,getApplicationContext()))
            {


                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        MyDatabase dbhelper = MyDatabase.getDatabase(_this);
                        List<TrackModel> trackList;

                        trackList = dbhelper.trackDAO().getTrackingDetailsAll(userid,  0, 20,class_id);


                        if (trackList != null && trackList.size() > 0) {
                            if (trackList.size() < 20) {
                                System.out.print("Empty");
                            } else {
                                TOTAL_PAGES = TOTAL_PAGES + 1;
                            }

                            mAdapter.addAll(trackList);
                            int resId = R.anim.layout_animation_slide;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
                            recyclerView.setLayoutAnimation(animation);
                            recyclerView.scheduleLayoutAnimation();

                            mAdapter.notifyDataSetChanged();


                            if (currentPage == TOTAL_PAGES) {
                                isLastPage = true;
                            } else if (currentPage < TOTAL_PAGES) {
                                mAdapter.addLoadingFooter();
                            }
                            checkEmptyData();
                        } else {
                            checkEmptyData();
                            CommonUtils.showToast(getApplicationContext(),"No Tracking Data");
                            //  Toasty.warning(_this, "No Tracking Data", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        prepareTrackData("0",true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    checkEmptyData();
                    CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
                    //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        }else
        {


            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    MyDatabase dbhelper = MyDatabase.getDatabase(_this);
                    List<TrackModel> trackList;

                    trackList = dbhelper.trackDAO().getTrackingDetailsAll(userid,  0, 20,class_id);


                    if (trackList != null && trackList.size() > 0) {
                        if (trackList.size() < 20) {
                            System.out.print("Empty");
                        } else {
                            TOTAL_PAGES = TOTAL_PAGES + 1;
                        }

                        mAdapter.addAll(trackList);
                        int resId = R.anim.layout_animation_slide;
                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
                        recyclerView.setLayoutAnimation(animation);
                        recyclerView.scheduleLayoutAnimation();

                        mAdapter.notifyDataSetChanged();


                        if (currentPage == TOTAL_PAGES) {
                            isLastPage = true;
                        } else if (currentPage < TOTAL_PAGES) {
                            mAdapter.addLoadingFooter();
                        }
                        checkEmptyData();
                    } else {
                        checkEmptyData();
                        CommonUtils.showToast(getApplicationContext(),"No Tracking Data");
                        //  Toasty.warning(_this, "No Tracking Data", Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
        }

    }
    StringRequest strReq;
    private void prepareTrackData(final String Offset,boolean isShow) throws UnsupportedEncodingException {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put("entity_type", "A");
            fjson.put("lecture_id", "0");
            fjson.put("subject_id", "0");
            fjson.put("section_id", "0");
            if(selected_user_id==userid)
                fjson.put(Constants.userId, selected_user_id);
            else {
                fjson.put(Constants.userId, userid);
                fjson.put(Constants.student_id, selected_user_id);
            }
            fjson.put(Constants.classId, selected_class_id);
            fjson.put("offset", Offset);
            fjson.put("limit", "20");
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);

        Log.d("Student Track","Student Track "+Constants.USER_TRACK + "/" + userid );
        Log.d("Student Track","Student Track "+Constants.USER_TRACK + "/" + userid + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"));
        strReq = new StringRequest(Request.Method.GET,
                Constants.USER_TRACK + "/" + userid + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();
                        if(getApplicationContext()==null)
                            return;
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



                                    chapters.subject_id = json_data.getString(Constants.subjectId);
                                    chapters.section_id = json_data.getString(Constants.sectionId);
                                    chapters.lecture_id = json_data.getString(Constants.lectureId);
                                    chapters.class_id = json_data.getString(Constants.classId);
                                    if(json_data.has("lecture_name"))
                                        chapters.lecture_name = json_data.getString(Constants.lectureName);


                                    chapters.user_id = userid;
                                    movieList.add(chapters);
                                    if(movieList.size()==0)
                                    {
                                        showDate(CommonUtils.getstringDate(movieList.get(0).activity_date));
                                    }
                                }

                                if (answers.length() < 20) {
                                    System.out.print("Empty");
                                } else {
                                    TOTAL_PAGES = TOTAL_PAGES + 1;
                                }

                                if (Offset.equalsIgnoreCase("0")) {
                                    mAdapter.addAll(movieList);
                                    int resId = R.anim.layout_animation_fall_down;
                                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
                                    recyclerView.setLayoutAnimation(animation);
                                    recyclerView.scheduleLayoutAnimation();
                                    // AppController.getInstance().playAudio(R.raw.slide_down);
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
                                String errorMsg = jObj.getString(Constants.message);
                                if(errorMsg.contains("Invalid access token"))
                                {
                                    MyDatabase dbHandler=MyDatabase.getDatabase(_this);
                                    dbHandler.userDAO().deleteUser(userid);
                                    SessionManager.logoutUser(_this);
                                    Intent i = new Intent(_this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    _this.finish();
                                    return;
                                }
                                if (Offset.equalsIgnoreCase("0")) {

                                    //CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                } else {
                                    mAdapter.removeLoadingFooter();
                                    isLoading = false;
                                    isLastPage = true;
                                }
                            }
                            checkEmptyData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(getApplicationContext()==null)
                    return;
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
                checkEmptyData();
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        if(isShow)
            CustomDialog.showDialog(_this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    private void checkEmptyData()
    {
        if(getApplicationContext()==null)
        {
            return;
        }
        if(mAdapter.getItemCount()>0)
            lnr_reload.setVisibility(View.GONE);
        else
            lnr_reload.setVisibility(View.VISIBLE);
    }

    private void checkCookieThenPlay()
    {
        //fillWithData();
        String encryption="";
        String  encryption2="";
        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);

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
                try {
                    prepareTrackData("0",true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="";
                if(_this==null)
                    return;

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
                CommonUtils.showToast(_this, msg);
                CustomDialog.closeDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(request);

    }
}