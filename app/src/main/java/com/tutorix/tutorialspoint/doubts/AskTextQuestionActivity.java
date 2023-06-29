package com.tutorix.tutorialspoint.doubts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AskTextQuestionActivity extends AppCompatActivity {

    @BindView(R.id.lnr_back)
    LinearLayout lnrBack;
    @BindView(R.id.tvPostQuestion)
    TextView tvPostQuestion;
    @BindView(R.id.lnr_submit)
    LinearLayout lnrSubmit;
    @BindView(R.id.etQuestionDescription)
    EditText etQuestionDescription;
    boolean isReply;
    String subjectId;
    String question_id;
    String questionDescription;

    Activity _this;
    @BindView(R.id.home)
    LinearLayout home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_text_question);
        ButterKnife.bind(this);
        _this = this;
        Bundle b = getIntent().getBundleExtra("intentType");
        if (b != null) {
            isReply = b.getBoolean("isReply");
            subjectId = b.getString(Constants.subjectId);
            question_id = b.getString("question_id");
        }

    }

    @OnClick({R.id.lnr_back, R.id.tvPostQuestion, R.id.lnr_submit, R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home:
                Intent in = new Intent(AskTextQuestionActivity.this, HomeTabActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;
                case R.id.lnr_back:
                finish();
                break;
            case R.id.tvPostQuestion:
            case R.id.lnr_submit:

                if (isReply) {
                    postReply();
                } else {
                    //startActivity(new Intent(getApplicationContext(),SimilarDoubtActivity.class));
                    //finish();
                    postQuestionForRelated();

                }
                break;
        }
    }

    private void postReply() {
        questionDescription = etQuestionDescription.getText().toString().trim();
        if (questionDescription.isEmpty()) {
            etQuestionDescription.setError("* required");
            etQuestionDescription.requestFocus();
        } else {
            postQuestionReplsy(questionDescription);
        }

    }

    private void postQuestionReplsy(String description) {
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.classId, classId);
            fjson.put("base64_reply_image", "");
            fjson.put("reply_text", description);
            fjson.put("question_id", question_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (_this == null) {
            return;
        }
        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(_this, true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.DOUBT_REPLY_NEW,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (_this == null) {
                            return;
                        }
                        //Log.v("Response","Response "+response);
                        CustomDialog.closeDialog();
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                if (_this == null) {
                                    return;
                                }
                                /*Dialog dialog=new Dialog(AskTextQuestionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.setContentView(R.layout.dialog_alert);
                                TextView txt_msg=dialog.findViewById(R.id.txt_msg);
                                txt_msg.setText("We have noted your query. One of our SMEs will check and respond soon!");
                                TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
                                TextView txt_ok=dialog.findViewById(R.id.txt_ok);
                                txt_ok.setText("Ok");
                                txt_cancel.setVisibility(View.GONE);
                                txt_cancel.setText("Cancel");
                                txt_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AppController.getInstance().playAudio(R.raw.button_click);
                                        dialog.dismiss();
                                    }
                                });
                                txt_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        AppController.getInstance().playAudio(R.raw.button_click);
                                        setResult(200);
                                        finish();

                                    }
                                });
                                dialog.show();*/

                                setResult(200);
                                finish();
                                CommonUtils.showToast(getApplicationContext(), "Noted your question!");

                                //CommonUtils.showToast(getApplicationContext(), "");

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                // setResult(Activity.RESULT_CANCELED);
                                // finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), "Json error : " + e.getMessage());
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
                try {
                    CommonUtils.showToast(getApplicationContext(), msg);
                    CustomDialog.closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }

    private void postQuestionForRelated() {
        questionDescription = etQuestionDescription.getText().toString().trim();
        if (questionDescription.isEmpty()) {
            etQuestionDescription.setError("* required");
            etQuestionDescription.requestFocus();
            return;
        }
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            //fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.base64_question_image, "");
            fjson.put(Constants.question_text, questionDescription);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        if (this == null)
            return;

        CustomDialog.showDialog(AskTextQuestionActivity.this, true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.DOUBT_RELATED_QTNS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.v("Response","Response "+response);
                        if (this == null)
                            return;
                        try {
                            CustomDialog.closeDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            JSONObject jObj = new JSONObject(response);
                            //Log.v("Reponse","Reponse "+jObj.toString());
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                SharedPreferences sh = getSharedPreferences("doubts", MODE_PRIVATE);
                                sh.edit().putString("doubts_data", jObj.toString()).apply();
                                startActivity(new Intent(getApplicationContext(), SimilarDoubtActivity.class));
                                finish();

                                //CommonUtils.showToast(getApplicationContext(), "Thank you for raising your doubt, Our faculty will check and respond it soon...");

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                setResult(Activity.RESULT_CANCELED);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), "Json error : " + e.getMessage());
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
                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sh = getSharedPreferences("doubts", MODE_PRIVATE);
        sh.edit().clear().apply();

    }
}
