package com.tutorix.tutorialspoint.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FeedbackActivity extends AppCompatActivity {
    FeedbackActivity _this;
    private String userid;
    private String access_token;
    EditText input_message;
    EditText input_subject;
    Toolbar toolbar;
    UserProfile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = FeedbackActivity.this;
        setContentView(R.layout.activity_feedback);

        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userid = userInfo[0];

        profile= MyDatabase.getDatabase(getApplicationContext()).userDAO().getUserProfile(userid);
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Feedback");

        input_subject = findViewById(R.id.input_subject);
        toolbar = findViewById(R.id.toolbar);
        input_message = findViewById(R.id.input_message);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

    }

    private void feedback(String subject, String message) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userid);
            fjson.put("feedback_title", subject);
            fjson.put("feedback_msg", message);
            fjson.put("full_name", profile.full_name);
            fjson.put("email_id", profile.email_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.USERS_FEEDBACKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(getApplicationContext()==null)
                            return;
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            //Log.d(Constants.response, response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                //CommonUtils.showToast(getApplicationContext(),"Feedback data added");
                                // Toasty.success(FeedbackActivity.this, "FeedbackActivity data added", Toast.LENGTH_SHORT, true).show();
                               // finish();
                                String errorMsg = jObj.getString(Constants.message);


                                Dialog dialog=new Dialog(FeedbackActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.setContentView(R.layout.dialog_alert);
                                TextView txt_msg=dialog.findViewById(R.id.txt_msg);
                                txt_msg.setText("Thank you for contacting us. We will get back to you as soon as possible.");
                                TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
                                TextView txt_ok=dialog.findViewById(R.id.txt_ok);
                                txt_ok.setText(getString(R.string.ok));
                                txt_cancel.setVisibility(View.GONE);
                                txt_cancel.setText(getString(R.string.cancel));
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
                                        setResult(Activity.RESULT_OK);
                                        AppController.getInstance().playAudio(R.raw.button_click);
                                        finish();

                                    }
                                });
                                dialog.show();



                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
                                //Toasty.warning(FeedbackActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                            //Toasty.warning(FeedbackActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="";
                if(getApplicationContext()==null)
                    return;
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
                // Toasty.warning(FeedbackActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                finish();

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
        CustomDialog.showDialog(FeedbackActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
    }

    public void sendFeedback(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!(input_message.getText().toString().isEmpty())) {
            if (AppStatus.getInstance(FeedbackActivity.this).isOnline()) {
                feedback(input_subject.getText().toString(), input_message.getText().toString());

            } else {
                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
                //Toasty.info(FeedbackActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            CommonUtils.showToast(getApplicationContext(),getString(R.string.fields_mandatory));
            // Toasty.error(FeedbackActivity.this, "Oops! Please Enter all fields", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
