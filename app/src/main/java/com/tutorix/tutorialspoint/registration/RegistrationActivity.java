package com.tutorix.tutorialspoint.registration;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.activities.WebViewTestActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.otp.OTPActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.countrycode.Country;
import com.tutorix.tutorialspoint.views.countrycode.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    RegistrationActivity _this;
    private Integer classId;
    private String countryCode;
    private String email;
    LinearLayout lnr_alreay_account;
    CheckBox checkbox;
    Pattern passwordPattern;
    InputFilter inputFilter[];
    int MAX_LENGTH=10;
    AppCompatEditText inputName;
    AppCompatEditText inputPhoneNumber;
    AppCompatEditText input_email;
    AppCompatEditText input_ref_code;
    LinearLayout lnr_ref_code;
    Typeface tf;
    TextView txt_terms;
    TextView have_ref_code;
    Pattern EmailPattern;
    Switch btn_switch;
    String ref_code="";

    TextView txt_signup_aler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = RegistrationActivity.this;
        CommonUtils.setFullScreen(_this);
        checkOrientation();
        passwordPattern= Pattern.compile(CommonUtils.PASSWORD_REG);
        EmailPattern= Patterns.EMAIL_ADDRESS;
        inputFilter=new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)};

        setContentView(R.layout.activity_registration_new);
        inputName = findViewById(R.id.input_name);
        inputPhoneNumber = findViewById(R.id.input_phone);
        input_email = findViewById(R.id.input_email);
        input_ref_code = findViewById(R.id.input_ref_code);
        lnr_ref_code = findViewById(R.id.lnr_ref_code);
        have_ref_code = findViewById(R.id.have_ref_code);
        inputPhoneNumber.setFilters(inputFilter);

        lnr_alreay_account=findViewById(R.id.lnr_alreay_account);
        txt_terms=findViewById(R.id.txt_terms);
        checkbox=findViewById(R.id.checkbox);
        btn_switch=findViewById(R.id.btn_switch);
        txt_signup_aler=findViewById(R.id.txt_signup_aler);

        try{
            Calendar c1=Calendar.getInstance();
            Calendar c=Calendar.getInstance();
            c.set(Calendar.YEAR,2022);
            c.set(Calendar.MONTH,Calendar.APRIL);
            c.set(Calendar.DAY_OF_MONTH,30);
            txt_signup_aler.setVisibility(View.GONE);

            if(c1.getTimeInMillis()<=c.getTimeInMillis())
            {
                txt_signup_aler.setVisibility(View.VISIBLE);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    lnr_ref_code.setVisibility(View.VISIBLE);
                    have_ref_code.setVisibility(View.GONE);
                    ref_code="";
                    input_ref_code.setText("");
                }else
                {
                    lnr_ref_code.setVisibility(View.GONE);
                    have_ref_code.setVisibility(View.VISIBLE);
                    ref_code="";
                    input_ref_code.setText("");
                }
            }
        });
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        String userid = userInfo[0];;

        if(getIntent()!=null)
        {
            String number=getIntent().getStringExtra(Constants.phoneNumber);
            if(number!=null)
                inputPhoneNumber.setText(number);
        }


        if(userid==null||userid.isEmpty())
            lnr_alreay_account.setVisibility(View.VISIBLE);
        else
            lnr_alreay_account.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            classId = extras.getInt(Constants.classId);
        }
        setCountryCode();
        tf=Typeface.createFromAsset(getAssets(),"opensans_regular.ttf");
        checkbox.setTypeface(tf);
        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web_intent= new Intent(_this, WebViewTestActivity.class);
                web_intent.putExtra("data_path", Constants.TERMS_POLICY);
                web_intent.putExtra("name", getResources().getString(R.string.terms_conditions));
                startActivity(web_intent);
            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
            }
        });
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
        inputFilter=new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)};
        inputPhoneNumber.setFilters(inputFilter);
    }
    private boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    public void signUp(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        AppCompatEditText inputPassword = findViewById(R.id.input_password);
        String phone=inputPhoneNumber.getText().toString().trim();
        email=input_email.getText().toString().trim();
        if (!(Objects.requireNonNull(inputName.getText()).toString().isEmpty() ||
                phone.isEmpty()
                || Objects.requireNonNull(inputPassword.getText()).toString().isEmpty())) {
            if(MAX_LENGTH==10&&phone.length()<MAX_LENGTH)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter valid Mobile Number");
                return;
            }
            if(!passwordPattern.matcher(inputPassword.getText().toString().trim()).matches()||inputPassword.getText().toString().trim().length()<4)
            {
                CommonUtils.showToast(getApplicationContext(),getResources().getString(R.string.passwordrestrict));
                return;
            } /*if(!EmailPattern.matcher(email).find())
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter valid Email id");
                return;
            }*/
            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                if (validCellPhone(inputPhoneNumber.getText().toString().trim())) {
                    if(!checkbox.isChecked())
                    {
                        CommonUtils.showToast(getApplicationContext(),"Please Read and Accept our Terms and Conditions.");
                        return;
                    }
                    sendOTP(countryCode, inputName.getText().toString(), inputPhoneNumber.getText().toString(), inputPassword.getText().toString());
                } else {
                    CommonUtils.showToast(getApplicationContext(),getString(R.string.invalid_phone_number));
                    // Toasty.error(_this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT, true).show();
                }
            } else
                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
        } else {
            CommonUtils.showToast(getApplicationContext(),getString(R.string.fields_mandatory));
            //Toast.makeText(_this, getString(R.string.fields_mandatory), Toast.LENGTH_LONG).show();
        }
    }

    private void sendOTP(final String countryCode, final String fullName, final String phoneNumber, final String password) {
        final JSONObject fjson = new JSONObject();

        ref_code=input_ref_code.getText().toString();
        try {
            fjson.put(Constants.mobileNumber, phoneNumber);
            fjson.put(Constants.mobile_country_code, countryCode);
            fjson.put(Constants.password, password);
            fjson.put("email_id", email);
            fjson.put("referral_code", ref_code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        //DialogUtils.showProgressDialog(_this);

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.GET_OTP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                       // Log.v("Error","Error "+response);

                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {

                                   /* SharedPref.setUserInfo(_this, fullName, phoneNumber,
                                            password, countryCode, String.valueOf(classId));*/

                                    Intent i = new Intent(_this, OTPActivity.class);

                                    i.putExtra("full_name",fullName);
                                    i.putExtra("email",email);
                                    i.putExtra("phoneNumber",phoneNumber);
                                    i.putExtra("password",password);
                                    i.putExtra("countryCode",countryCode);
                                    i.putExtra("classId",classId);
                                    i.putExtra("referral_code",ref_code);
                                    //Log.v("Class ID","Class ID 2 "+classId);

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
                // DialogUtils.dismissProgressDialog();
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
        CustomDialog.showDialog(RegistrationActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }

    public void login(View view) {
        Intent i = new Intent(_this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
        AppController.getInstance().playAudio(R.raw.qz_next);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(_this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
        AppController.getInstance().playAudio(R.raw.qz_next);
    }
}
