package com.tutorix.tutorialspoint.otp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.registration.SchoolInfoActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class OTPActivity extends AppCompatActivity {
    OTPActivity _this;
    //private SmsVerifyCatcher smsVerifyCatcher;
    AppCompatEditText otp;

    String classId, phoneNumber, countryCode, fullName, password;
    String android_id;
    String email;
    String referral_code;
    TextView txt_resend;
    TextView txt_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = OTPActivity.this;
        CommonUtils.setFullScreen(_this);
        setContentView(R.layout.activity_otp);
        otp = findViewById(R.id.otp);
        txt_resend = findViewById(R.id.txt_resend);
        txt_hint = findViewById(R.id.txt_hint);
        txt_resend.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_resend.setVisibility(View.GONE);
        smsVerify();

        Intent in = getIntent();
        if (in != null) {
            fullName = in.getStringExtra("full_name");
            phoneNumber = in.getStringExtra("phoneNumber");
            email = in.getStringExtra("email");
            password = in.getStringExtra("password");
            countryCode = in.getStringExtra("countryCode");
            referral_code = in.getStringExtra("referral_code");
            classId = in.getIntExtra("classId", 0) + "";

            //Log.v("Class ID","Class ID 1 "+classId);

        }

        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    txt_resend.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
            }
        }, 30000);

        if (countryCode != null && countryCode.contains("91")) {
            txt_hint.setText(getResources().getString(R.string.please_enter_your_4_digit_otp_from_your_respective_mobile_number));
        } else {
            txt_hint.setText(getResources().getString(R.string.please_enter_your_4_digit_otp_from_your_respective_email));
        }
    }

    private void smsVerify() {
       /* smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code1 = code(message);
                otp.setText(code1);//set code in edit text
                registerUser();
            }
        });
        smsVerifyCatcher.setPhoneNumberFilter("VM-TPOINT");*/
    }

    private String code(String message) {
        String mCode = message.replace("Hi, Your OTP is", "");
        String mCode1 = mCode.replace(". It is valid for next 24 hours. Regards Tutorials Point", "");
        return mCode1.trim();
    }

    public void validateOTP(View view) {
        AppCompatEditText otp = findViewById(R.id.otp);
        if (!(Objects.requireNonNull(otp.getText()).toString().isEmpty())) {
            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                registerUser();
            } else {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                //  Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
            }
        } else {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.fields_mandatory));
            // Toast.makeText(_this, getString(R.string.fields_mandatory), Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser() {
        Log.i(Constants.deviceId, android_id);
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.deviceId, android_id);
            fjson.put(Constants.fullName, fullName);
            fjson.put(Constants.mobileNumber, phoneNumber);
            fjson.put("email_id", email);
            fjson.put(Constants.password, password);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.mobile_country_code, countryCode);
            fjson.put(Constants.otpCode, Objects.requireNonNull(otp.getText()).toString());
            fjson.put(Constants.firebaseToken, SharedPref.getToken(_this));
            fjson.put(Constants.OS_TYPE, "A");
            fjson.put("referral_code", referral_code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(OTPActivity.this, false);

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.REGISTEATION_REF,
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

                                  /*  String activationType = jObj.getString(Constants.activationType);
                                    String activationEndDate = jObj.getString(Constants.activationEndDate);
                                    String activation_start_date = jObj.getString(Constants.activationStartData);
                                    String accessToken = jObj.getString(Constants.accessToken);
                                    String classId = jObj.getString(Constants.classId);
                                    String mobile_number = jObj.getString(Constants.mobileNumber);
                                    String country_code = jObj.getString(Constants.mobile_country_code);
                                    String user_type = jObj.getString(Constants.userType);
                                    String activation_key = jObj.getString(Constants.activationKey);
                                    String current_date = jObj.getString(Constants.currentDate);*/
                                    String userId = jObj.getString(Constants.userId);
                                    String activationType = "";
                                    String activationEndDate = "";
                                    String activation_start_date = "";
                                    String user_type = "";
                                    String activation_key = "";
                                    String current_date = "";
                                    String userName = "";

                                    if (jObj.has(jObj.getString(Constants.userName)))
                                        userName = jObj.getString(Constants.userName);

                                    String accessToken = jObj.getString(Constants.accessToken);
                                    String data_url = "";
                                    if (jObj.has(Constants.DATA_URL))
                                        data_url = jObj.getString(Constants.DATA_URL);

                                    String secure_data_url = "";
                                    if (jObj.has(Constants.SECURE_DATA_URL))
                                        secure_data_url = jObj.getString(Constants.SECURE_DATA_URL);
                                    //Log.d("secure_data_url", secure_data_url);
                                    SessionManager session = new SessionManager();
                                    String tutorix_school_flag = "";
                                    try {
                                        tutorix_school_flag = jObj.getString(Constants.TUTORIX_SCHOOL_FLAG);
                                    } catch (Exception e) {

                                    }
                                    session.setLogin(_this, userId, accessToken, android_id, activationType, classId, secure_data_url, data_url, tutorix_school_flag);

                                    long days = 0;
                                    if (activation_key.length() > 2) {
                                        days = CommonUtils.daysBetween(activation_start_date, activationEndDate);
                                    }

                                    MyDatabase db = MyDatabase.getDatabase(getApplicationContext());
                                    UserProfile profile = new UserProfile();
                                    profile.mobile_country_code = countryCode;
                                    profile.user_id = userId;
                                    profile.mobile_number = phoneNumber;
                                    profile.password = password;
                                    profile.full_name = fullName;
                                    profile.accessToken = accessToken;
                                    profile.deviceId = android_id;
                                    profile.user_type = user_type;
                                    db.userDAO().insertUser(profile);

                                    ActivationDetails aDeails = new ActivationDetails();
                                    aDeails.current_date = current_date;
                                    aDeails.user_id = userId;
                                    aDeails.activation_type = activationType;
                                    aDeails.activation_key = activation_key;
                                    aDeails.activation_start_date = activation_start_date;
                                    aDeails.activation_end_date = activationEndDate;
                                    aDeails.class_id = classId;
                                    aDeails.secure_url = secure_data_url;
                                    aDeails.data_url = data_url;
                                    aDeails.days_left = days + "";
                                    aDeails.device_id = android_id;
                                    db.activationDAO().inserActivation(aDeails);
                                    getProfileInformation(accessToken, classId, userId);

                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
                                    // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = getResources().getString(R.string.error_4);
                }
                CommonUtils.showToast(getApplicationContext(), msg);
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

    public void goBack(View view) {
        Intent i = new Intent(_this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(_this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //smsVerifyCatcher.onStop();
    }


    private void getProfileInformation(final String accessToken, String classId, final String userId) {
        if (AppStatus.getInstance(_this).isOnline()) {
            final JSONObject fjson = new JSONObject();

            try {
                fjson.put(Constants.accessToken, accessToken);
                fjson.put(Constants.classId, classId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tag_string_req = Constants.reqRegister;
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            CustomDialog.showDialog(OTPActivity.this, false);
            String urlEncode = "";
            try {
                urlEncode = URLEncoder.encode(encryption, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringRequest strReq = new StringRequest(
                    Request.Method.GET,
                    Constants.USERS + "/" + userId + "?json_data=" + urlEncode,
                    new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            CustomDialog.closeDialog();
                            try {
                                JSONObject jObj = new JSONObject(response);


                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {

                                    MyDatabase db = MyDatabase.getDatabase(getApplicationContext());
                                    UserProfile profile = db.userDAO().getUserProfile(userId);
                                    if (profile == null) {
                                        profile = new UserProfile();
                                        profile.password = password;
                                        profile.accessToken = accessToken;
                                        profile.user_id = userId;
                                    }

                                    profile.full_name = checkNotNull(jObj.getString(Constants.fullName));
                                    profile.user_name = checkNotNull(jObj.getString(Constants.userName));
                                    profile.father_name = checkNotNull(jObj.getString(Constants.fatherName));
                                    profile.mobile_number = checkNotNull(jObj.getString(Constants.mobileNumber));

                                    profile.email_id = checkNotNull(jObj.getString(Constants.emailId));
                                    profile.address = checkNotNull(jObj.getString(Constants.address));
                                    profile.city = checkNotNull(jObj.getString(Constants.city));
                                    profile.state = checkNotNull(jObj.getString(Constants.state));

                                    profile.country_code = checkNotNull(jObj.getString(Constants.country_code));
                                    profile.mobile_country_code = checkNotNull(jObj.getString(Constants.mobile_country_code));
                                    profile.date_of_birth = checkNotNull(jObj.getString(Constants.dob));
                                    profile.gender = checkNotNull(jObj.getString(Constants.gender));
                                    profile.school_name = checkNotNull(jObj.getString(Constants.schoolName));
                                    profile.roll_number = checkNotNull(jObj.getString(Constants.rollNumber));
                                    profile.section_name = checkNotNull(jObj.getString(Constants.sectionName));
                                    profile.school_city = checkNotNull(jObj.getString(Constants.school_city));
                                    profile.school_address = checkNotNull(jObj.getString(Constants.school_address));
                                    profile.school_state = checkNotNull(jObj.getString(Constants.school_state));
                                    profile.photo = checkNotNull(jObj.getString(Constants.photo));
                                    profile.school_country_code = checkNotNull(jObj.getString(Constants.school_country));
                                    profile.school_postal_code = checkNotNull(jObj.getString(Constants.school_zip));
                                    profile.postal_code = checkNotNull(jObj.getString(Constants.postal_code));

                                    db.userDAO().updateProfile(profile);
                                    callLandingPage();
                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    callLandingPage();
                                    //CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                                // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                callLandingPage();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //CommonUtils.showToast(getApplicationContext(),getString(R.string.there_is_error));
                    //Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();
                    callLandingPage();
                    CustomDialog.closeDialog();
                }
            }) {

            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {

            callLandingPage();

            //CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
            // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void callLandingPage() {
        try {
            if (!SharedPref.isTokenAdded(getApplicationContext())) {
                String token = SharedPref.getToken(getApplicationContext());
                if (token == null || token.length() < 6) {
                    token = FirebaseInstanceId.getInstance().getToken();
                }
                SharedPref.registerToken(token, getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPref sh = new SharedPref();
        sh.setExpired(getApplicationContext(), false);

        /*Intent i = new Intent(_this, HomeTabActivity.class);
        AppConfig.CALLED_EXPIERY=false;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();*/
        Intent intent = new Intent(_this, SchoolInfoActivity.class);
        startActivity(intent);
        /*Intent ii = new Intent(OTPActivity.this, SwitchClassActivity.class);
        ii.putExtra("islogin", true);
        ii.putExtra("isActivated", true);
        ii.putExtra("isFromOTP", true);
        AppConfig.CALLED_EXPIERY=false;
        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
      */
//        startActivity(intent);
        finish();
        try {
            AppController.getInstance().addSignupEvents(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppConfig.CALLED_EXPIERY = false;
    }

    private String checkNotNull(String s) {
        if (s == null || s.equals("null") || s.isEmpty())
            return "";
        else return s;
    }

    public void resendotp(View view) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.mobileNumber, phoneNumber);
            fjson.put(Constants.mobile_country_code, countryCode);
            fjson.put("email_id", email);
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
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
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
                String msg = "";

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = getResources().getString(R.string.error_4);
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
        CustomDialog.showDialog(OTPActivity.this, true);
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }
}
