package com.tutorix.tutorialspoint.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.smarteist.autoimageslider.SliderView;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.ForgotPasswordActivity;
import com.tutorix.tutorialspoint.activities.SwitchClassActivity;
import com.tutorix.tutorialspoint.adapters.SliderAdapter;
import com.tutorix.tutorialspoint.registration.RegistrationActivity;
import com.tutorix.tutorialspoint.registration.SchoolInfoActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.views.CustomTextview;
import com.tutorix.tutorialspoint.views.countrycode.Country;
import com.tutorix.tutorialspoint.views.countrycode.CountryCodePicker;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    LoginPresenterImpl loginPresenterImpl;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.input_password)
    EditText inputPassword;
    /* @BindView(R.id.txtinput)
     TextInputLayout txtinput;*/
    @BindView(R.id.txt_forgot)
    TextView txtForgot;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.txt_signup)
    TextView txtSignup;
   /* @BindView(R.id.txt_donthaveacnt)
    TextView txtDonthaveacnt;*/


    @BindView(R.id.lnr_btns_hide)
    LinearLayout lnrBtnsHide;
    @BindView(R.id.txt_mobile_alert_msg_2)
    CustomTextview txtMobileAlert_msg2;
    @BindView(R.id.txt_mobile_alert_msg)
    CustomTextview txtMobileAlertMsg;
    @BindView(R.id.txt_yes)
    TextView txtYes;
    @BindView(R.id.txt_no)
    TextView txtNo;
    @BindView(R.id.txt_signup_aler)
    TextView txt_signup_aler;
    @BindView(R.id.lnr_btns_view)
    LinearLayout lnrBtnsView;
    @BindView(R.id.lnr_bottom_img)
    View lnrBottomImg;

    @BindString(R.string.don_t_have_an_account_register)
    String don_t_have_an_account;
    @BindString(R.string.wrong_password_msg)
    String wrong_password_msg;

    @BindString(R.string.mobile_not_registered_msg)
    String mobile_not_registered_msg;
    @BindString(R.string.would_you_like_to_register)
    String would_you_like_to_register;
    @BindString(R.string.would_you_like_to_generatepassword)
    String would_you_like_to_generatepassword;
    private String countryCode;
    String password;
    String android_id = "";
    int MAX_LENGTH = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(LoginActivity.this);
        View view = getLayoutInflater().inflate(R.layout.login_new, null);
        setContentView(view);
        ButterKnife.bind(this);
        //String s="New to Tutorix? <font color='#65ADFF'>Sign Up</font>";
        String s=getString(R.string.don_t_have_an_account_register);
        /*if (Build.VERSION.SDK_INT >= 24) {

            txtSignup.setText(Html.fromHtml(s, Html.FROM_HTML_OPTION_USE_CSS_COLORS));
        } else {
            txtSignup.setText(Html.fromHtml(s));

        }*/

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
        countryCode = ccp.getDefaultCountryCode();
        initFilter();
        loginPresenterImpl = new LoginPresenterImpl(this, this);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                countryCode = country.getPhoneCode();
                inputPhone.setText("");
                initFilter();
                lnrBtnsHide.setVisibility(View.VISIBLE);
                lnrBottomImg.setVisibility(View.VISIBLE);
                lnrBtnsView.setVisibility(View.GONE);
            }
        });

        inputPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus)
               {
                   lnrBtnsHide.setVisibility(View.VISIBLE);
                   lnrBottomImg.setVisibility(View.VISIBLE);
                   lnrBtnsView.setVisibility(View.GONE);
               }
            }
        });
        inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    lnrBtnsHide.setVisibility(View.VISIBLE);
                    lnrBottomImg.setVisibility(View.VISIBLE);
                    lnrBtnsView.setVisibility(View.GONE);
                }
            }
        });

      SliderView sliderView = findViewById(R.id.slider);
        // on below line creating variable for array list.
         ArrayList<Integer> sliderDataArrayList= new ArrayList();
        // on below line adding urls in slider list.

        sliderDataArrayList.clear();
        if(AppController.childQaulityAudio==1)
        {
            sliderDataArrayList.add(R.drawable.ag_hindi);
            sliderDataArrayList.add(R.drawable.dr_bd_hindi);
            sliderDataArrayList.add(R.drawable.zahida_hindi);
        }else {
            sliderDataArrayList.add(R.drawable.ag_eng);
            sliderDataArrayList.add(R.drawable.dr_bd_eng);
            sliderDataArrayList.add(R.drawable.zahida_eng);
        }
        // on below line initializing our adapter class by passing our list to it.
        SliderAdapter adapter = new SliderAdapter(sliderDataArrayList);
        // on below line setting auto cycle direction for slider view from left to right.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        // on below line setting adapter for slider view.
        sliderView.setSliderAdapter(adapter);
        // on below line setting scroll time for slider view
        sliderView.setScrollTimeInSec(3);
        // on below line setting auto cycle for slider view.
        sliderView.setAutoCycle(true);
        // on below line setting start cycle for slider view.
        sliderView.startAutoCycle();

      /*  TextInputLayout txtinput = findViewById(R.id.txtinput);
        txtinput.setTypeface(Typeface.createFromAsset(getAssets(),"open_sans_regular.ttf"));*/
    }

    private void initFilter() {
        if (countryCode.contains("91"))
            MAX_LENGTH = 10;
        else
            MAX_LENGTH = 15;
        inputPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
    }

    @OnClick({R.id.ccp, R.id.txt_forgot, R.id.txt_login, R.id.txt_signup,R.id.txt_yes, R.id.txt_no/*, R.id.txt_donthaveacnt*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ccp:
                break;
            case R.id.txt_forgot:
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                AppController.getInstance().playAudio(R.raw.qz_next);
                break;
            case R.id.txt_login:
                inputPassword.clearFocus();
                loginPresenterImpl.checkValidation(inputPhone.getText().toString().trim(), inputPassword.getText().toString().trim(), countryCode);
                AppController.getInstance().playAudio(R.raw.button_click);
                break;
            case R.id.txt_signup:
                /* case R.id.txt_donthaveacnt:*/
                if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

                    CommonUtils.showToast(getApplicationContext(), getResources().getString(R.string.no_internet));
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.putExtra(Constants.classId, 1);
                intent.putExtra(Constants.phoneNumber, inputPhone.getText().toString().trim());
                //Log.v("Class ID","Class ID 3 "+selectedClassId);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                AppController.getInstance().playAudio(R.raw.qz_next);
                break;
            case R.id.txt_yes:
                lnrBtnsHide.setVisibility(View.VISIBLE);
                lnrBottomImg.setVisibility(View.VISIBLE);
                lnrBtnsView.setVisibility(View.GONE);
                if(ERROR_CODE==1)
                {
                    if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

                        CommonUtils.showToast(getApplicationContext(), getResources().getString(R.string.no_internet));
                        return;
                    }
                   /* Intent intent = new Intent(LoginActivity.this, SwitchClassActivity.class);
                    intent.putExtra("islogin", false);
                    intent.putExtra(Constants.phoneNumber, inputPhone.getText().toString().trim());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();*/




                    Intent intent2 = new Intent(LoginActivity.this, RegistrationActivity.class);
                    intent2.putExtra(Constants.classId, 1);
                    intent2.putExtra(Constants.phoneNumber, inputPhone.getText().toString().trim());
                    //Log.v("Class ID","Class ID 3 "+selectedClassId);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                    AppController.getInstance().playAudio(R.raw.qz_next);
                    return;
                }
                if(ERROR_CODE==2)
                {
                    Intent intent2 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    intent2.putExtra(Constants.phoneNumber, inputPhone.getText().toString().trim());
                    startActivity(intent2);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                    AppController.getInstance().playAudio(R.raw.qz_next);
                    return;
                }
                break;
            case R.id.txt_no:
                lnrBtnsHide.setVisibility(View.VISIBLE);
                lnrBottomImg.setVisibility(View.VISIBLE);
                lnrBtnsView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    int ERROR_CODE=0;
    @Override
    public void showMessage(String msg,int code) {

        ERROR_CODE=code;
       //

        if(ERROR_CODE==2)
        {
            lnrBtnsHide.setVisibility(View.GONE);
            lnrBottomImg.setVisibility(View.GONE);
            lnrBtnsView.setVisibility(View.VISIBLE);

            txtMobileAlert_msg2.setText(would_you_like_to_generatepassword);
            // mobile_not_registered_msg=  String.format(mobile_not_registered_msg,countryCode+" - "+inputPhone.getText().toString());
            if (Build.VERSION.SDK_INT >= 24) {
                txtMobileAlertMsg.setText(Html.fromHtml(wrong_password_msg, Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            } else {
                txtMobileAlertMsg.setText(Html.fromHtml(wrong_password_msg));

            }
        }else if(ERROR_CODE==1)
        {
            lnrBtnsHide.setVisibility(View.GONE);
            lnrBottomImg.setVisibility(View.GONE);
            lnrBtnsView.setVisibility(View.VISIBLE);
            ERROR_CODE=1;
            String s="Mobile Number <font color='#ca8827'>"+"+"+countryCode+"-"+inputPhone.getText().toString()+" </font> is not registered with us.";
            txtMobileAlert_msg2.setText(would_you_like_to_register);
            // mobile_not_registered_msg=  String.format(mobile_not_registered_msg,countryCode+" - "+inputPhone.getText().toString());
            if (Build.VERSION.SDK_INT >= 24) {
                txtMobileAlertMsg.setText(Html.fromHtml(s, Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            } else {
                txtMobileAlertMsg.setText(Html.fromHtml(s));

            }
        }else
        {
            CommonUtils.showToast(getApplicationContext(), msg);
        }

    }

    @Override
    public void navigateScreen(int screen) {

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
        if(screen == 5){
            Intent intent = new Intent(this, SchoolInfoActivity.class);
            startActivity(intent);
            finish();
        }
        else if (screen == 0) {

            Intent ii = new Intent(LoginActivity.this, SwitchClassActivity.class);
            ii.putExtra("islogin", true);
            ii.putExtra("isActivated", true);
            ii.putExtra("isFromOTP", false);
            startActivity(ii);
            finish();
            AppConfig.CALLED_EXPIERY = false;
            try {
                AppController.getInstance().addLoginEvents(inputPhone.getText().toString());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            /*  AppConfig.ALERT_TIME_TOTAL= 0;

            Intent i = new Intent(LoginActivity.this, HomeTabActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();*/
        }

    }


}
