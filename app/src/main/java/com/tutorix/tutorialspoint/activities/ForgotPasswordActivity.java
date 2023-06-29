package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;

import androidx.annotation.RequiresApi;
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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.countrycode.Country;
import com.tutorix.tutorialspoint.views.countrycode.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ForgotPasswordActivity extends AppCompatActivity {

    ForgotPasswordActivity _this;
    private String countryCode;
    AppCompatEditText inputPhoneNumber;
    int MAX_LENGTH=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = ForgotPasswordActivity.this;
        CommonUtils.setFullScreen(_this);
        checkOrientation();
        setContentView(R.layout.activity_forgot_password);
         inputPhoneNumber = findViewById(R.id.input_phone);
        setCountryCode();
        if(getIntent()!=null)
        {
            String number=getIntent().getStringExtra(Constants.phoneNumber);
            if(number!=null) {
                inputPhoneNumber.setText(number);
                try {
                    inputPhoneNumber.setSelection(inputPhoneNumber.getText().toString().trim().length());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
    private void checkOrientation()
    {
        boolean isTablet=getResources().getBoolean(R.bool.is_tablet);
        if(!isTablet)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void setCountryCode() {
        CountryCodePicker ccp = findViewById(R.id.ccp);
        countryCode = ccp.getDefaultCountryCode();
        initFilter();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                countryCode = country.getPhoneCode();
                inputPhoneNumber.setText("");
                initFilter();
            }
        });
    }

    private void initFilter()
    {

        if(countryCode.contains("91"))
        {
            MAX_LENGTH=10;

        }else
        {
            MAX_LENGTH=15;
        }

        inputPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
    }
    public void forgotPassword(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!(Objects.requireNonNull(inputPhoneNumber.getText()).toString().isEmpty())) {
            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                forgetPasswordMethod(inputPhoneNumber.getText().toString().trim());
            } else {
                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
                //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
            }
        } else {
            CommonUtils.showToast(getApplicationContext(),getString(R.string.fields_mandatory));
            //Toast.makeText(_this, getString(R.string.fields_mandatory), Toast.LENGTH_LONG).show();
        }

    }

    private void forgetPasswordMethod(final String inputPhoneNumber) {
        final JSONObject fjson = new JSONObject();

        try {
            fjson.put(Constants.mobileNumber, inputPhoneNumber);
            fjson.put(Constants.mobile_country_code, countryCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;
        //DialogUtils.showProgressDialog(_this);

        CustomDialog.showDialog(ForgotPasswordActivity.this,true);
        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.USER_FORGOT,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);

                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                String successMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),successMsg);
                               // Toasty.warning(_this, successMsg, Toast.LENGTH_SHORT, true).show();
                                Intent i = new Intent(_this, OTPForgotPasswordActivity.class);
                                i.putExtra(Constants.phoneNumber, inputPhoneNumber);
                                i.putExtra(Constants.mobile_country_code, countryCode);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
                                //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
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
        }) {
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, AppConfig.ENC_KEY);


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void login(View view) {
        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
        AppController.getInstance().playAudio(R.raw.qz_next);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
        AppController.getInstance().playAudio(R.raw.qz_next);
    }
}
