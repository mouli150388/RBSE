package com.tutorix.tutorialspoint.activities;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import com.google.android.material.textfield.TextInputLayout;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class ChangePasswordActivity extends AppCompatActivity {

    ChangePasswordActivity _this;
    EditText etOldPassword, etNewPassword, etRenterPassword;

    Pattern passwordPattern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = ChangePasswordActivity.this;
        setContentView(R.layout.activity_change_password);
        passwordPattern=Pattern.compile(CommonUtils.PASSWORD_REG);
        initViews();
    }

    private void initViews() {
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etRenterPassword = findViewById(R.id.etRenterPassword);

        TextInputLayout txtinput_a = findViewById(R.id.txtinput_a);
        TextInputLayout txtinput_b = findViewById(R.id.txtinput_b);
        TextInputLayout txtinput_c = findViewById(R.id.txtinput_c);
        Typeface tf=Typeface.createFromAsset(getAssets(),"opensans_regular.ttf");
        txtinput_a.setTypeface(tf);
        txtinput_b.setTypeface(tf);
        txtinput_c.setTypeface(tf);
    }

    public void updatePassword(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        final JSONObject fjson = new JSONObject();

        String oldPassword = etOldPassword.getText().toString().trim();
        final String newPassword = etNewPassword.getText().toString().trim();
        String renterPassword = etRenterPassword.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            CommonUtils.showToast(getApplicationContext(),"Old Password required");
            //Toast.makeText(_this, "Old Password required", Toast.LENGTH_SHORT).show();
            etOldPassword.requestFocus();
        } else if (newPassword.isEmpty()) {
            CommonUtils.showToast(getApplicationContext(),"New password reqired");
           // Toast.makeText(_this, "New Password required", Toast.LENGTH_SHORT).show();
            etNewPassword.requestFocus();
        } else if (!newPassword.equals(renterPassword)) {
            CommonUtils.showToast(getApplicationContext(),"Password Doesn't match");
           // Toast.makeText(_this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
            etRenterPassword.requestFocus();
        }
        else if(!passwordPattern.matcher(newPassword).matches()||newPassword.length()<4)
        {
            CommonUtils.showToast(getApplicationContext(),getResources().getString(R.string.passwordrestrict));
            return;
        }
        else {
          String[]  userInfo= SessionManager.getUserInfo(getApplicationContext());

            String access_token = userInfo[1];
            final String userId = userInfo[0];


            try {
                fjson.put(Constants.accessToken, access_token);
                fjson.put(Constants.oldPassword, oldPassword);
                fjson.put(Constants.newPassword, newPassword);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String reqRegister = Constants.reqRegister;
            //DialogUtils.showProgressDialog(_this);
            CustomDialog.showDialog(ChangePasswordActivity.this,false);

            StringRequest strReq = new StringRequest(Request.Method.PUT,
                     Constants.USER_PASSWORD + "/" + userId,
                    new Response.Listener<String>() {

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(String response) {
                            CustomDialog.closeDialog();
                            try {

                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
                                    db.userDAO().updatePassword(userId,newPassword);
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
                                   // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                                //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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

    }

    public void cancelPassword(View view) {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppController.getInstance().playAudio(R.raw.button_click);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
