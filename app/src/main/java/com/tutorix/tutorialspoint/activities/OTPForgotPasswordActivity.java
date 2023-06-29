package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;


public class OTPForgotPasswordActivity extends AppCompatActivity {
    OTPForgotPasswordActivity _this;
    private String PhoneNumber, countryCode;
    private String password, otp;
    //private String PASSWORD_PATTERN="(?=.*[0-9])(?=.*[a-z A-Z]).{6,20}";
    Pattern passwordPattern;
    TextView txt_resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = OTPForgotPasswordActivity.this;
        CommonUtils.setFullScreen(_this);
        setContentView(R.layout.activity_otp_forgot_password);
        passwordPattern=Pattern.compile(CommonUtils.PASSWORD_REG);
        txt_resend = findViewById(R.id.txt_resend);
        txt_resend.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_resend.setVisibility(View.GONE);

      /*  TextInputLayout inputlayout_a=findViewById(R.id.inputlayout_a);
        inputlayout_a.setTypeface(Typeface.createFromAsset(getAssets(),"open_sans_regular.ttf"));
*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PhoneNumber = extras.getString(Constants.phoneNumber);
            countryCode = extras.getString(Constants.mobile_country_code);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    txt_resend.setVisibility(View.VISIBLE);
                }catch (Exception e)
                {

                }
            }
        },30000);
    }

    public void goBack(View view) {
        Intent i = new Intent(_this, LoginActivity.class);

        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
        AppController.getInstance().playAudio(R.raw.qz_next);
    }

    public void resendotp(View view) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.mobileNumber, PhoneNumber);
            fjson.put(Constants.mobile_country_code, countryCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;


        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.RESEND_OTP,
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
        CustomDialog.showDialog(OTPForgotPasswordActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }
    public void changePassword(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        AppCompatEditText etPassword = findViewById(R.id.password);
        AppCompatEditText etOTP = findViewById(R.id.otp);
        password = etPassword.getText().toString().trim();
        otp = etOTP.getText().toString().trim();
        /*if(!Pattern.compile(PASSWORD_PATTERN).matcher(password).matches())
        {
            CommonUtils.showToast(getApplicationContext(),"Password should contains alpha numerics and minimum 6 characters");
            return;
        }*/
        if(password.isEmpty())
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter Password");
            return;
        }

        if(!passwordPattern.matcher(password).matches()||password.length()<4)
        {
            CommonUtils.showToast(getApplicationContext(),getResources().getString(R.string.passwordrestrict));
            return;
        }

        if(otp.isEmpty())
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter valid OTP");
            return;
        }
        if (!password.isEmpty() && !otp.isEmpty()) {
            changePassword();
        }
    }

    private void changePassword() {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.otpCode, otp);
            fjson.put(Constants.mobileNumber, PhoneNumber);
            fjson.put(Constants.newPassword, password);
            fjson.put(Constants.mobile_country_code, countryCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;


        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                 Constants.FORGOT_PASSWORD,
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
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                    Intent i = new Intent(_this, LoginActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();

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
        CustomDialog.showDialog(OTPForgotPasswordActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }


}