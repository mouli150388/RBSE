package com.tutorix.tutorialspoint.dsw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

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
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.CustomTextview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardsActivity extends AppCompatActivity {
    @BindView(R.id.txt_goildcoins)
    CustomTextview txtGoildcoins;
    @BindView(R.id.txt_silvercoins)
    CustomTextview txtSilvercoins;
    @BindView(R.id.lnr_bottom)
    LinearLayout lnrBottom;
    @BindView(R.id.txt_1)
    CustomTextview txt1;
    @BindView(R.id.txt_2)
    CustomTextview txt2;
    @BindView(R.id.txt_3)
    CustomTextview txt3;
    @BindView(R.id.lnr_middle)
    LinearLayout lnrMiddle;
    @BindView(R.id.btn_win)
    AppCompatButton btn_win;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String userid;
    private String section_id;
    private String lecture_name = "";
    private String classId;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
        }
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userid = userInfo[0];
        ;
        classId = userInfo[4];
        try {
            getMyRewards();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        btn_win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),RewardDescriptionActivity.class));
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getMyRewards() throws UnsupportedEncodingException {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.accessToken, access_token);
            //Log.v("Request","Request "+fjson.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        //Log.v("Request","Request "+Constants.USER_TRACK + "/" + userid + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"));
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.GET_MY_REWARDS + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                int silver_count = jObj.getInt("silver_count");
                                int gold_count = jObj.getInt("gold_count");
                                txtSilvercoins.setText(silver_count + " ");
                                txtGoildcoins.setText(gold_count + " ");

                                if(silver_count==0&&gold_count==0)
                                {
                                    txt1.setText(getString(R.string.reward_text4));
                                    txt2.setText(getString(R.string.reward_text5));
                                    //txt3.setText(getString(R.string.reward_text6));
                                    //txt3.setCompoundDrawables(null,null,getDrawable(R.drawable.vote_48_dowb),null);
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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
                finish();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };

        CustomDialog.showDialog(RewardsActivity.this, false);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
