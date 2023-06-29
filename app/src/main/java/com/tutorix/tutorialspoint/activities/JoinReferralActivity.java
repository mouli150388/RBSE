package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

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
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.CustomTextview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JoinReferralActivity extends AppCompatActivity {

    String access_token;
    String userid;
    String classId;
    String loginType;

    @BindString(R.string.you_ref_code)
    String you_ref_code;
    @BindString(R.string.so_far_ref_code)
    String so_far_ref_code;
    @BindString(R.string.ref_desc)
    String ref_desc;
    @BindView(R.id.txt_ref_code)
    CustomTextview txt_ref_code;
    @BindView(R.id.txt_ref_numbers)
    CustomTextview txt_ref_numbers;
    @BindView(R.id.txt_ref_desc)
    CustomTextview txt_ref_desc;
    @BindView(R.id.img_back)
    LinearLayout img_back;
    @BindView(R.id.img_home)
    LinearLayout img_home;
    @BindView(R.id.ref_code)
    RadioButton radio_ref_code;
    @BindView(R.id.share_buttons)
    FrameLayout share_buttons;
    String referral_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_referral);
        ButterKnife.bind(this);
        String[] userInfo = SessionManager.getUserInfo(this);
        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        txt_ref_code.setText(String.format(you_ref_code,""));
        txt_ref_numbers.setText(String.format(so_far_ref_code,"",""));
        txt_ref_desc.setText(String.format(ref_desc,"",""));

        getReferralDetails();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void shareRefCode()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Tutorix - Best Learning App");
        intent.putExtra(Intent.EXTRA_TEXT, "Join me on Tutorix, personalized " +
                "learning app, which teaches  Maths and Science through quality visuals. Enter my code '"+ referral_code+"' to get 30-Days Free subscription. \nLink : https://bit.ly/2WbAv15");
        startActivity(Intent.createChooser(intent, "choose one"));
    }
    private void getReferralDetails() {
        final JSONObject fjson = new JSONObject();


        try {
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("referral_code", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        //DialogUtils.showProgressDialog(_this);

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.REFERRAL_DETAILS,
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
                                     referral_code=jObj.getString("referral_code");
                                    int referral_count=jObj.getInt("referral_count");
                                    int referral_max=jObj.getInt("referral_max");

                                    txt_ref_code.setText(String.format(you_ref_code,referral_code));
                                    txt_ref_numbers.setText(String.format(so_far_ref_code,referral_count+"",referral_max+""));

                                if(referral_count>0&&(referral_count>=referral_max))
                                {
                                    share_buttons.setVisibility(View.GONE);
                                    txt_ref_numbers.setText("Referral Details");
                                    txt_ref_desc.setText("Congratulations on winning your one year Tutorix subscription");

                                    return;
                                }else
                                {

                                }


                                    String ref_desc="Distribute the referral code <b>"+referral_code+"</b> to your friends and ask them to register with tutorix using this referral code. <br><br>Once we get " +referral_max+" subscriptions with your referral code, we will activate your one year subscription absolutely free. <br><br>Your subscribed friend will also get  one month subscription absolutely free";

                                    if (Build.VERSION.SDK_INT >= 24) {

                                        txt_ref_desc.setText(Html.fromHtml(ref_desc, Html.FROM_HTML_OPTION_USE_CSS_COLORS|Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE));
                                    } else {
                                        txt_ref_desc.setText(Html.fromHtml(ref_desc));

                                    }
                                    radio_ref_code.setText(referral_code);


                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            finish();
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
                finish();
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
        CustomDialog.showDialog(JoinReferralActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }

    public void shareCode(View view) {
        shareRefCode();
    }
}
