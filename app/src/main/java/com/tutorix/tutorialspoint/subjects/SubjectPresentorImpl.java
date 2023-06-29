package com.tutorix.tutorialspoint.subjects;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.models.Chapters;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SubjectPresentorImpl implements SubjectPresentor{

    String userId;
    String classId;
    String access_token;
    String loginType;
    SubjectView subjectView;
    Activity activity;
    String BaseURL;
    String subjectId;
    String subjectName;

    public SubjectPresentorImpl(SubjectView subjectView, Activity activity,String subjectId,String subjectName)
    {
        this.subjectView=subjectView;
        this.activity=activity;
        this.subjectId=subjectId;
        this.subjectName=subjectName;
        String[] userInfo = SessionManager.getUserInfo(activity);
        access_token = userInfo[1];
        userId = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
    }

    @Override
    public void fetchData() {


/*Start Updated code for Read data from SDCard if the User is in Offline*/

        if(AppConfig.checkSDCardEnabled(activity,userId,classId)&&AppConfig.checkSdcard(classId,activity.getApplicationContext()))
            readSDCARD();
        else
        {

            if (AppStatus.getInstance(activity).isOnline()) {
                checkCookieThenPlay();
            } else {
                subjectView.showMessage(activity.getString(R.string.no_internet_message));
            }
        }
        if(true)
        {
            return;
        }
        /* End Updated code for Read data from SDCard if the User is in Offline*/
        if ( loginType.isEmpty()) {
            if (AppStatus.getInstance(activity).isOnline()) {
                checkCookieThenPlay();

            } else {
                subjectView.showMessage(activity.getString(R.string.no_internet_message));
                //Toasty.info(SubjectActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }

        }else if(loginType.equalsIgnoreCase("O"))
        {

             if(AppConfig.checkSDCardEnabled(activity,userId,classId)&&AppConfig.checkSdcard(classId,activity.getApplicationContext()))
                 readSDCARD();
             else
             {

                 if (AppStatus.getInstance(activity).isOnline()) {
                     checkCookieThenPlay();
                 } else {
                     subjectView.showMessage(activity.getString(R.string.no_internet_message));
                 }
             }


        }else {

            readSDCARD();
        }
    }

    private void readSDCARD() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionForStorage()) {
                String sdCardPath="";
                if ((sdCardPath=AppConfig.getSdCardPath(classId,activity.getApplicationContext()) )!= null) {
                    String filepath = sdCardPath + subjectName + ".json";
                    BaseURL = sdCardPath;
                    String jsonString = AppConfig.readFromFile(filepath);
                    if (jsonString.isEmpty()) {

                        subjectView.showNoSdcard();
                        try {
                            CustomDialog.closeDialog();
                        }catch (Exception e)
                        {

                        }
                        //loadingPanelID.hide();
                        return;
                    }
                    try {
                        parseData(new JSONObject(jsonString));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    subjectView.showMessage(activity.getString(R.string.no_sdcard));
                    //Toasty.info(SubjectActivity.this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
                }
            } else {
                subjectView.requestPermissionForStorage();
            }
        } else {
            String sdCardPath="";
            if ((sdCardPath=AppConfig.getSdCardPath(classId,activity.getApplicationContext()) )!= null) {
                String filepath = sdCardPath + subjectName + ".json";
                BaseURL = sdCardPath;
                String jsongString = AppConfig.readFromFile(filepath);
                try {
                    parseData(new JSONObject(jsongString));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                subjectView.showMessage(activity.getString(R.string.no_sdcard));
                //Toasty.info(SubjectActivity.this, "No SDCard Found", Toast.LENGTH_SHORT, true).show();
            }
        }

    }

    private void checkCookieThenPlay() {
        //fillWithData();
        String encryption = "";
        String encryption2 = "";
        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userId);
            fjson.put(Constants.classId, classId);

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
                    try {
                        CustomDialog.closeDialog();
                    }catch (Exception e)
                    {

                    }
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("flag") && obj.getInt("flag") == 1) {
                        Constants.isHadCookie=true;
                        fillWithData();
                    }
                    else {
                        subjectView.showMessage("Cookie Expired, Please logout");
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
                subjectView.showMessage( msg);
            }
        });/*{
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
        if ((loginType.equalsIgnoreCase("O")||loginType.equalsIgnoreCase(""))&&!new SharedPref().isExpired(activity)) {
            if(Constants.isHadCookie)
                fillWithData();
            else
            {
                try {
                    CustomDialog.showDialog(activity, true);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                AppController.getInstance().addToRequestQueue(request);
            }

        } else fillWithData();

    }

    private void fillWithData() {

        String tag_string_req = Constants.reqRegister;
        //loadingPanelID.show();
        if(activity==null)
            return;
        try {
            CustomDialog.showDialog(activity, true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        BaseURL = AppConfig.getOnlineURLImage(classId);
        String s=subjectName;


        Log.d("URL","URL For Subject: "+AppConfig.getOnlineURLImage(classId) + subjectName + ".json");
        if(subjectName.contains("physics"))
            s=s+"1";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.getOnlineURLImage(classId) + subjectName + ".json",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            // loadingPanelID.hide();
                            try {
                                CustomDialog.closeDialog();
                            }catch (Exception e)
                            {

                            }

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
               subjectView.showMessage( msg);
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
        try {
            CustomDialog.closeDialog();
        }catch (Exception e)
        {

        }

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
                int length = answers.getJSONObject(i).getJSONArray("lectures").length();

                ArrayList<SubChapters> subChaptersList = new ArrayList<>();

int totlatime=-1;
                for (int j = 0; j < length; j++) {
                    JSONObject lecturesJsonObject = answers.getJSONObject(i).getJSONArray("lectures").getJSONObject(j);

                    SubChapters subchapters = new SubChapters();

                    subchapters.lecture_id = lecturesJsonObject.getString("lecture_id");
                    subchapters.txt = lecturesJsonObject.getString("lecture_name");
                    //subchapters.lecture_video_url = lecturesJsonObject.getString("lecture_video_url");
                    //subchapters.lecture_video_thumb = lecturesJsonObject.getString("lecture_video_thumb");
                    subchapters.lecture_video_url = Constants.VIDEO_NAME;
                    subchapters.video_srt = Constants.VIDEO_SRT;
                    subchapters.lecture_video_thumb = Constants.VIDEO_THUMB_NAME;
                    subchapters.lecture_duration = lecturesJsonObject.getString("lecture_duration");
                    subchapters.lecture_notes = lecturesJsonObject.getString("lecture_notes");
                    subchapters.lecture_quiz = lecturesJsonObject.getString("lecture_quiz");
                    subchapters.status = lecturesJsonObject.getString("lecture_status");

                    subchapters.is_demo = !((lecturesJsonObject.getString("lecture_demo_flag")).equalsIgnoreCase("N"));
                    subchapters.subjectid = subjectId;
                    subchapters.classid = classId;
                    subchapters.userid = userId;
                    subchapters.section_id = json_data.getString("section_id");
                    subchapters.lecture_completed = false;
                    subchapters.lecture_bookmark = false;


                    if(subchapters.status.equals("P")) {
                        totlatime = totlatime + getSecondsTime(subchapters.lecture_duration);
                        subChaptersList.add(subchapters);
                    }
                    else {
                        totlatime = totlatime + 0;
                        chapters.availableAllVideos=false;
                    }

                    //Log.v("Totla Time","Totla Time "+totlatime+" "+subchapters.status.equals("P"));
                }

                if(totlatime>=0)
                {
                    chapters.calculated_time=secondstoMinsts(totlatime);
                }else
                {
                    chapters.calculated_time="";
                }

                chapters.subchapters = subChaptersList;

                data.add(chapters);
            }

                subjectView.loadData(data,BaseURL);


        } catch (JSONException e) {
            e.printStackTrace();
            subjectView.showMessage(activity.getString(R.string.json_error) + e.getMessage());
            //Toasty.warning(SubjectActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
        }
    }
    private boolean checkPermissionForStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }



   /* private void cachedVolley()
    {
        StringRequest jsonArrayRequest = new StringRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setTitle(jsonObject.getString("title"));
                        movie.setRating(jsonObject.getInt("rating"));
                        movie.setYear(jsonObject.getInt("releaseYear"));

                        movieList.add(movie);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
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
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }*/


   private int getSecondsTime(String time)
   {
      try {
          //Log.v("Time","Time String "+time);
          String[] units = time.split(":"); //will break the string up into an array
          int minutes = Integer.parseInt(units[1]); //first element
          int seconds = Integer.parseInt(units[2]); //second element
          return 60 * minutes + seconds;
      }catch (Exception e)
      {
          e.printStackTrace();
          return 0;
      }
   }

   private String secondstoMinsts(int totlatime)
   {
       try {
           int mints = totlatime / 60;
           int seconds = totlatime % 60;
           return String.format("%02d",mints) + ":" +String.format("%02d", seconds);
       }catch (Exception e)
       {
           e.printStackTrace();
       }
       return "";
   }
}
