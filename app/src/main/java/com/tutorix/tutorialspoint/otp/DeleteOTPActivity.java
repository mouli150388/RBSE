package com.tutorix.tutorialspoint.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tutorix.tutorialspoint.activities.OTPForgotPasswordActivity;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DeleteOTPActivity extends AppCompatActivity {

    private String reason, otp;
    private String access_token, userid,loginType,classId,session_device_id;


    TextView txt_resend;
    EditText etotp;
    EditText etReason;
    EditText etMobileNumber;
    EditText etMailId;
    UserProfile profile;
    DeleteOTPActivity _this;
    LinearLayout lnr_reson,lnr_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_otpactivity);
        _this=this;
        etotp = findViewById(R.id.otp);
        etMailId = findViewById(R.id.etMailId);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etReason = findViewById(R.id.etReason);
        txt_resend = findViewById(R.id.txt_resend);
        txt_resend.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_resend.setVisibility(View.GONE);
        String[] userInfo = SessionManager.getUserInfo(this);

        etMobileNumber.setEnabled(false);

        etMailId.setEnabled(false);
        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        session_device_id = userInfo[3];
         profile = MyDatabase.getDatabase(this).userDAO().getUserProfile(userid);

        etMailId.setText(profile.email_id);
        etMobileNumber.setText(profile.mobile_number);
        lnr_reson=findViewById(R.id.lnr_reson);
        lnr_otp=findViewById(R.id.lnr_otp);
        if(profile.email_id.isEmpty())
            etMailId.setEnabled(true);
        else
            etMailId.setEnabled(false);

    }
    public void deleteAccount(View view) {

        otp=etotp.getText().toString().trim();
         if(otp.length()<4)
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter valid OTP");
            return;
        }
        deleteAccount();
    }
    public void getOtp(View view) {

        reason=etReason.getText().toString().trim();

        if(reason.isEmpty())
        {
            CommonUtils.showToast(getApplicationContext(),"Please write your reason");
            return;
        }
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        deleteOTP(userid,android_id);
       // deleteAccount();

    }

    private void deleteOTP(String userId, String android_id) {

        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.deviceId, android_id);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.mobile_country_code, profile.mobile_country_code);
            fjson.put(Constants.mobileNumber, profile.mobile_number);
            fjson.put(Constants.emailId, profile.email_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(_this, false);
        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.GET_DELETE_ACCOUNT_OTP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        if(_this==null)
                        {
                            return;
                        }
                        CustomDialog.closeDialog();
                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                //checkUnCookieThenPlay();
                                CommonUtils.showToast(_this, jObj.getString(Constants.message));
                                if (!error) {
                                    lnr_reson.setVisibility(View.GONE);
                                    lnr_otp.setVisibility(View.VISIBLE);
                                   // startActivity(new Intent(_this, DeleteOTPActivity.class));
                                } else {
                                    //String errorMsg = jObj.getString(Constants.message);
                                    //CommonUtils.showToast(  _this, errorMsg);
                                    /*if (errorMsg.contains("Invalid")) {
                                        SessionManager.logoutUser(_this);
                                        MyDatabase dbHandler=MyDatabase.getDatabase(_this);
                                        dbHandler.userDAO().deleteUser(userId);
                                        Intent i = new Intent(_this, LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        _this. finish();
                                    }*/
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
                CommonUtils.showToast(_this, error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                CustomDialog.closeDialog();
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
    private void deleteAccount() {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.otpCode, otp);
            fjson.put(Constants.mobileNumber, profile.mobile_number);
            fjson.put(Constants.mobile_country_code, profile.mobile_country_code);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.emailId, profile.email_id);
            fjson.put("reason", reason);
            fjson.put(Constants.OS_TYPE, "A");
            fjson.put("ip_address", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;


        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.GET_DELETE_ACCOUNT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);

                        CustomDialog.closeDialog();
                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {

                                    MyDatabase    hadnler = MyDatabase.getDatabase(getApplicationContext());
                                    hadnler.subjectChapterDAO().deleteBookmarkForDeactUser(userid,classId);
                                    hadnler.trackDAO().deleteTracksForDeactUser(userid,classId);
                                    hadnler.quizModelDAO().deleteQuizeDetails(userid,classId);
                                    hadnler.activationDAO().deleteActivationDetails(userid,classId);
                                    hadnler.mockTestDAO().deleteMockTest(userid,classId);
                                    hadnler.recomandedDAO().deleteMockTestRecomanded(userid,classId);
                                    hadnler.sdActivationDAO().deleteActivationDetails(userid,classId);
                                    hadnler.userDAO().deleteUser(userid);
                                    SessionManager.logoutUser(DeleteOTPActivity.this);
                                    Intent intent = new Intent(DeleteOTPActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
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
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

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
        CustomDialog.showDialog(DeleteOTPActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }
}