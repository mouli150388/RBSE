package com.tutorix.tutorialspoint.home;

import android.app.Activity;
import android.provider.Settings;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomePresenteroImpl implements HomePresentor{
    HomeView homeView;
    String access_token;
    String userid;
    String classId;
    String loginType;
    String session_device_id;
    Activity activity;
    public HomePresenteroImpl(HomeView homeView, Activity activity)
    {
        this.homeView=homeView;
        this.activity=activity;
        String[] userInfo = SessionManager.getUserInfo(activity);

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        session_device_id = userInfo[3];
    }

    @Override
    public void checkValidation() {
        checkActivation();
    }

    @Override
    public void setCookie() {
        callCookies();
    }

    @Override
    public void checkAccesToken() {
        checkAccessToken();

    }
    private void callCookies() {
        CookieManager manager = new CookieManager();
       // manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
    }
    private void checkAccessToken() {

        final JSONObject fjson = new JSONObject();
        try {


            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        // CustomDialog.showDialog(HomeTabActivity.this, true);
        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.CHECK_ACCESSTOKEN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        //CustomDialog.closeDialog();
                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);

                                if (!error) {

                                } else {
                                    MyDatabase dbHandler=MyDatabase.getDatabase(activity);
                                    dbHandler.userDAO().deleteUser(userid);
                                    //checkUnCookieThenPlay();
                                   homeView.callLogin();

                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //CommonUtils.showToast(getApplicationContext(), error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                //CustomDialog.closeDialog();
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
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }
    private boolean checkActivation() {
        SharedPref sh=new SharedPref();
        if(sh.isExpired(activity))
        {
            return false;
        }

        if(loginType.isEmpty())
        {
            sh=new SharedPref();
            sh.setExpired(activity,false);
            return true;
        }
        MyDatabase db=MyDatabase.getDatabase(activity);
        SDActivationDetails sdActivationDetails= db.sdActivationDAO().getActivationDetails(userid,classId);
        if(loginType.equalsIgnoreCase("O")){
            if(sdActivationDetails==null||sdActivationDetails.activation_key.isEmpty())
            {
                return true;
            }
        }

        if(sdActivationDetails==null)
        {
            return true;
        }
        //ActivationDetails aDetails = db.activationDAO().getActivationDetails(userid, classId);
        String endDate = sdActivationDetails.activation_end_date;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        if(sh.getIsDateCheckFirsttime(activity))
        {

            Date d = new Date();
            String cDate = sdf.format(d);
            sdActivationDetails.current_date=cDate;
            db.sdActivationDAO().updateActivationDate(sdActivationDetails.activation_end_date,cDate,classId,userid);
            sh.setIsFirstime(activity,false);
        }else
        {

            Date d1, d2;
            try {
                d1 = sdf.parse(sdActivationDetails.current_date);
                Date d = new Date();
                if (d1.getTime() > d.getTime()) {
                    //sh = new SharedPref();
                    //sh.setExpired(getActivity(), true);
                    CommonUtils.showToast(activity,"It seems issue with your device date, please select current date");
                    //showAlert(null);
                    return false;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        String android_id = Settings.Secure.getString( activity.getContentResolver(), Settings.Secure.ANDROID_ID);


       /* if (!session_device_id.isEmpty()&& !session_device_id.equalsIgnoreCase(android_id)) {
            //showAlert("Activated in other Device, Please De-Activate that device and again Login to this device");
            return false;
        }*/

        if (!endDate.isEmpty()) {

            Date d = new Date();
            String cDate = sdf.format(d);
            Date d1, d2;
            try {
                d1 = sdf.parse(cDate);
                d2 = sdf.parse(endDate);



                if (d1.getTime()>=d2.getTime() ) {
                    homeView.showLoading(null);
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return true;
            }


            return true;
        }

        String s = AppConfig.getSdCardPath(classId,activity.getApplicationContext());
        if (!new File(s).exists()) {
            homeView.showLoading("Don't have SD card for the current class, Please insert and use application");
            return false;
        }
        return true;
    }

}
