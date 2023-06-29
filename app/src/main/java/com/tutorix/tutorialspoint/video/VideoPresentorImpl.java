package com.tutorix.tutorialspoint.video;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

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
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class VideoPresentorImpl implements VideoPresentor {
    VideoView videoView;
    Activity activity;
    String lecture_video_url;
    String classid;
    String subjectId;
    String userid;

    public VideoPresentorImpl(VideoView videoView, Activity activity,String classid,String subjectId,String userid)
    {
        this.videoView=videoView;
        this.activity=activity;
        this.classid=classid;
        this.subjectId=subjectId;
        this.userid=userid;
    }

    @Override
    public void checkCookeAndPlay(String lecture_video_url) {
        this.lecture_video_url=lecture_video_url;
        checkCookieThenPlay();
    }



    @Override
    public void fillWithData() {

        loadData();
    }


    private void checkCookieThenPlay() {
        //playVideo();
        String encryption="";
        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classid);
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption= URLEncoder.encode(encryption,"utf-8");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //CustomDialog.showDialog(NotesActivity.this,true);
        StringRequest request=new StringRequest(Request.Method.GET, Constants.CHECK_COOKIES+"?json_data="+encryption, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomDialog.closeDialog();
                videoView.play();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                CustomDialog.closeDialog();
                String msg="";

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg=activity.getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg=activity.getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg=activity.getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg=activity.getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg=activity.getResources().getString(R.string.error_4);
                }
                videoView.showMessage(msg,true);
            }
        });
      /*  if(loginType.equalsIgnoreCase("O") )
            AppController.getInstance().addToRequestQueue(request);
        else*/  videoView.play();

    }

    private void loadData() {

        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.getOnlineURL(classid, false) + AppConfig.getSubjectName(subjectId) + ".json", new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);

                    videoView.parseResultData(jObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="";
                videoView.hideLoading();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg=activity.getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg=activity.getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg=activity.getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg=activity.getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg=activity.getResources().getString(R.string.error_4);
                }
                videoView.showMessage(msg,true);
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }





}
