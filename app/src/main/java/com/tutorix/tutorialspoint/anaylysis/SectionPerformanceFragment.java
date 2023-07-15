package com.tutorix.tutorialspoint.anaylysis;


import static java.lang.Math.round;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.snackbar.Snackbar;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.BuildConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.SectionPerFormanceAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.Chapters;
import com.tutorix.tutorialspoint.models.MockTestModelTable;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionPerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionPerformanceFragment extends Fragment {


    @BindView(R.id.recycler_sections)
    RecyclerView recyclerSections;
    private String access_token;
    private String userId;
    private String loginType;
    private String classId;
    private String BaseURL;
    private String subjectName;
    private String subjectId;
    Activity activity;
    SectionPerFormanceAdapter adapter;
    String BASE_URL;
    String selected_user_id;
    String selected_class_id;
    JSONObject avgJsonObj;
    public SectionPerformanceFragment() {
        // Required empty public constructor

    }

    // TODO: Rename and change types and number of parameters
    public static SectionPerformanceFragment newInstance(String subjectId,String subjectName,String selected_user_id,String selected_class_id) {
        SectionPerformanceFragment fragment = new SectionPerformanceFragment();
        Bundle args = new Bundle();
        args.putString("subjectId",subjectId);
        args.putString("subjectName",subjectName);
        args.putString("selected_user_id",selected_user_id);
        args.putString("selected_class_id",selected_class_id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.subjectName=getArguments().getString("subjectName");
            this.subjectId=getArguments().getString("subjectId");
            this.selected_user_id=getArguments().getString("selected_user_id");
            this.selected_class_id=getArguments().getString("selected_class_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section_performance, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] userInfo = SessionManager.getUserInfo(getActivity());
        access_token = userInfo[1];
        userId = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        activity=getActivity();

        recyclerSections.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        fetchData();
    }

    public void fetchData() {
       /* if(selected_user_id==userId)
        {
            callOnline();
            return;
        }*/
        if(/*AppConfig.checkSDCardEnabled(activity,userId,classId)&&*/AppConfig.checkSdcard(classId,getContext()))
        {
            BASE_URL = AppConfig.getSdCardPath(classId,getContext());
            fetchMockPerformnce();
        }else
        {
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                BASE_URL = AppConfig.getOnlineURLImage(classId);
                // checkCookieThenPlay();
                chartData();

            } else {

                CommonUtils.showToast(getActivity(),getActivity().getString(R.string.no_internet_message));
                //Toasty.info(SubjectActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }
/*
        if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                BASE_URL = AppConfig.getOnlineURLImage(classId);
                // checkCookieThenPlay();
                chartData();

            } else {

                CommonUtils.showToast(getActivity(),getActivity().getString(R.string.no_internet_message));
                //Toasty.info(SubjectActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(activity,userId,classId)&&AppConfig.checkSdcard(classId,getContext()))
            {
                BASE_URL = AppConfig.getSdCardPath(classId,getContext());
                fetchMockPerformnce();
            }else
            {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    BASE_URL = AppConfig.getOnlineURLImage(classId);
                    // checkCookieThenPlay();
                    chartData();

                } else {

                    CommonUtils.showToast(getActivity(),getActivity().getString(R.string.no_internet_message));
                    //Toasty.info(SubjectActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        }else
        {
            BASE_URL = AppConfig.getSdCardPath(classId,getContext());
            fetchMockPerformnce();
        }*/



    }

    private void checkCookieThenPlay() {
        //fillWithData();
        String encryption = "";
        String encryption2 = "";
        final JSONObject fjson = new JSONObject();
        try {

            if(selected_user_id==userId)
                fjson.put(Constants.userId, selected_user_id);
            else {
                fjson.put(Constants.userId, userId);
                fjson.put(Constants.student_id, selected_user_id);
            }
            fjson.put(Constants.classId, selected_class_id);

            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption2 = URLEncoder.encode(encryption, "utf-8");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, Constants.CHECK_COOKIES + "?json_data=" + encryption2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Log.v("response","response"+response);
                    //loadingPanelID.hide();
                    CustomDialog.closeDialog();
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("flag") && obj.getInt("flag") == 1) {
                        Constants.isHadCookie=true;
                        fillWithData();
                    }
                    else {
                        CommonUtils.showToast(getActivity(),"Cookie Expired, Please logout");
                        Constants.isHadCookie=false;
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
                CustomDialog.closeDialog();
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
                CommonUtils.showToast(getActivity(), msg);
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
        };/*{
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };*/


        if (loginType.equalsIgnoreCase("O")) {
            if(Constants.isHadCookie)
                fillWithData();
            else
            {
                CustomDialog.showDialog(activity, true);
                AppController.getInstance().addToRequestQueue(request);
            }

        } else fillWithData();

    }

    private void fillWithData() {

        String tag_string_req = Constants.reqRegister;
        //loadingPanelID.show();
        if(activity==null)
            return;
        CustomDialog.showDialog(activity, true);
        if(selected_user_id==userId)
            BaseURL = AppConfig.getOnlineURLImage(classId);
        else {
            BaseURL = AppConfig.getOnlineURLImage(selected_class_id);

        }



//Log.v("Image Path","URL Request "+AppConfig.getOnlineURLImage(classId) + subjectName + ".json");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.getOnlineURLImage((selected_user_id==userId)?classId:selected_class_id) + subjectName + ".json",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            // loadingPanelID.hide();
                            CustomDialog.closeDialog();
                            JSONObject jObj = new JSONObject(response);
                            parseData(jObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                //loadingPanelID.hide();
                CustomDialog.closeDialog();
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
                CommonUtils.showToast(getActivity(), msg);
                // Toasty.warning(SubjectActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

                activity.finish();
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
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
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
    }


    private void parseData(JSONObject jObj) {
        //loadingPanelID.hide();
        CustomDialog.closeDialog();

        List data = new ArrayList<>();

        try {
            JSONArray answers = jObj.getJSONObject(subjectName).getJSONArray("sections");
            for (int i = 0; i < answers.length(); i++) {
                JSONObject json_data = answers.getJSONObject(i);
                Chapters chapters = new Chapters();
                chapters.txt = json_data.getString("section_name");
                chapters.section_id = json_data.getString("section_id");
                chapters.section_image = json_data.getString("section_image");
                chapters.section_image = json_data.getString("section_image");
                chapters.lecture_count = json_data.getString("lecture_count");

                chapters.question_count = json_data.getString("question_count");
                chapters.total_duration = json_data.getString("total_duration");
                chapters.section_status = json_data.getString("section_status");
                chapters.subject_name = subjectName;
                chapters.subjectid = subjectId;
                chapters.classid = classId;
                chapters.userid = userId;

                //Log.v("avgJsonObj ","avgJsonObj "+avgJsonObj);
                if(avgJsonObj!=null&&avgJsonObj.has(chapters.section_id))
                {
                    chapters.avg=avgJsonObj.getJSONObject(chapters.section_id).getString("percentage");
                    chapters.attrmpted_count=avgJsonObj.getJSONObject(chapters.section_id).getString("attempts");
                }else
                {
                    chapters.avg="";
                }




                data.add(chapters);
            }
            adapter=new SectionPerFormanceAdapter(BASE_URL,getActivity(),subjectId,selected_user_id,selected_class_id);
            adapter.addData(data);
            recyclerSections.setAdapter(null);
            recyclerSections.setAdapter(adapter);
            recyclerSections.setHasFixedSize(true);
            adapter.notifyDataSetChanged();
            recyclerSections.setItemViewCacheSize(20);
            recyclerSections.setDrawingCacheEnabled(true);
            recyclerSections.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
           /* RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();

            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }*/
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
            recyclerSections.setLayoutAnimation(animation);
            recyclerSections.scheduleLayoutAnimation();



        } catch (JSONException e) {
            e.printStackTrace();
            CommonUtils.showToast(getActivity(),activity.getString(R.string.json_error) + e.getMessage());
            //Toasty.warning(SubjectActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
        }
    }
    private boolean checkPermissionForStorage() {
        if(Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(activity.findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    activity.startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    activity.startActivity(intent);
                                }
                            }
                        })
                        .show();
                return  false;
            } else return true;
        }else {
            int  result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void chartData()   {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        if(/*AppConfig.checkSDCardEnabled(getActivity(),userId,classId)&&*/AppConfig.checkSdcard(classId,getContext()))
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyDatabase dbhelper = MyDatabase.getDatabase(getActivity());


                    try {



                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });





                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else
        {
            callOnline();
        }
       /* if(loginType.isEmpty())
        {
            callOnline();
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(getActivity(),userId,classId)&&AppConfig.checkSdcard(classId,getContext()))
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyDatabase dbhelper = MyDatabase.getDatabase(getActivity());


                        try {



                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });





                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else
            {
                callOnline();
            }
        }else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyDatabase dbhelper = MyDatabase.getDatabase(getActivity());


                    try {



                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });





                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }*/



    }


    private void callOnline()
    {

        final JSONObject fjson3 = new JSONObject();
        try {
            if(selected_user_id==userId) {
                fjson3.put(Constants.userId, selected_user_id);
                fjson3.put(Constants.classId, classId);
            }
            else {
                fjson3.put(Constants.userId, userId);
                fjson3.put(Constants.student_id, selected_user_id);
                fjson3.put(Constants.classId, selected_class_id);
            }

            fjson3.put(Constants.accessToken, access_token);
            //fjson3.put(Constants.subjectId, subject_id );

            fjson3.put(Constants.accessToken, access_token);

            // Log.v("Request Data","Request Data "+fjson3.toString());
            //Log.v("Request Data","Request Data "+Constants.USER_GET_MOCKTEST_PERFORMANCE +"/"+subjectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message3 = fjson3.toString();

        final String encryption3 = Security.encrypt(message3, Key);

        try {
            Log.d("Performance","Performance "+Constants.USER_GET_MOCKTEST_PERFORMANCE +"/"+subjectId+ "?json_data=" + URLEncoder.encode(encryption3, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        if (AppStatus.getInstance(Objects.requireNonNull(getActivity()).getBaseContext()).isOnline()) {
            StringRequest strReq = null;
            try {

                strReq = new StringRequest(Request.Method.GET,
                        Constants.USER_GET_MOCKTEST_PERFORMANCE +"/"+subjectId+ "?json_data=" + URLEncoder.encode(encryption3, "UTF-8"),
                        new Response.Listener<String>() {

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                //Log.d(Constants.response, response);

                                try {
                                    CustomDialog.closeDialog();

                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean(Constants.errorFlag);



                                    if (!error) {

                                        avgJsonObj=jObj.getJSONObject("section_averages");



                                    } else {

                                        // CommonUtils.showToast(getActivity(),errorMsg);
                                        String errorMsg = jObj.getString(Constants.message);
                                        if(errorMsg.contains("Invalid access token"))
                                        {
                                            MyDatabase dbHandler=MyDatabase.getDatabase(getActivity());
                                            dbHandler.userDAO().deleteUser(userId);
                                            SessionManager.logoutUser(getActivity());
                                            Intent i = new Intent(getActivity(), LoginActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                            getActivity().finish();
                                        }
                                        //nodata(new ArrayList<QuizModel>());
                                    }
                                    checkCookieThenPlay();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //nodata(new ArrayList<QuizModel>());
                                    //CommonUtils.showToast(getActivity(),getString(R.string.json_error)+e.getMessage());

                                    //Toasty.warning(Objects.requireNonNull(getActivity()).getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkCookieThenPlay();

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
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CustomDialog.showDialog(getActivity(),true);
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            CommonUtils.showToast(getActivity(),getString(R.string.no_internet));
            //Toasty.info(getActivity().getBaseContext(), "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }
    private void fetchMockPerformnce()
    {

        MyDatabase databse=MyDatabase.getDatabase(getActivity());
       List<MockTestModelTable> listMock= databse.mockTestDAO().getMockTest(userId,classId,subjectId);
       avgJsonObj=new JSONObject();
     JSONObject  child=new JSONObject();
     //Log.v("Json request","Json request "+listMock.size());
       try {
           for (int k = 0; k < listMock.size(); k++) {
               //Log.v("MoCkTest Data", "MoCkTest Data " + listMock.get(k).section_id + " " + listMock.get(k).total_marks + " " + listMock.get(k).total_questions);
               child.put("attempts",(listMock.get(k).total_attempts));
               child.put("percentage",round(((float)listMock.get(k).total_marks * 100) / listMock.get(k).total_questions));
               avgJsonObj.put(listMock.get(k).section_id, child);

           }
       }catch (Exception e)
       {
           e.printStackTrace();
       }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionForStorage()) {
                String sdCardpath="";
                if ((sdCardpath=AppConfig.getSdCardPath(classId,getContext())) != null) {
                    String filepath = sdCardpath + "/" + subjectName + ".json";
                    BaseURL = sdCardpath;
                    String jsonString = AppConfig.readFromFile(filepath);
                    if (jsonString.isEmpty()) {

                        //subjectView.showNoSdcard();
                        CustomDialog.closeDialog();
                        //loadingPanelID.hide();
                        return;
                    }
                    try {
                        parseData(new JSONObject(jsonString));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.showToast(getActivity(),getActivity().getString(R.string.no_sdcard));
                    //Toasty.info(SubjectActivity.this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
            }
        } else {
            String sdCardpath="";
            if ((sdCardpath=AppConfig.getSdCardPath(classId,getContext()) )!= null) {
                String filepath = sdCardpath+ subjectName + ".json";
                BaseURL = sdCardpath;
                String jsongString = AppConfig.readFromFile(filepath);
                try {
                    parseData(new JSONObject(jsongString));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CommonUtils.showToast(getActivity(),getActivity().getString(R.string.no_sdcard));
                //Toasty.info(SubjectActivity.this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
            }
        }

    }
}
