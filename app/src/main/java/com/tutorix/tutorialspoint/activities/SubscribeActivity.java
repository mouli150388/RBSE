package com.tutorix.tutorialspoint.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.tutorix.tutorialspoint.ActivationResultActivity;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubscribeActivity extends AppCompatActivity {
    SubscribeActivity _this;
    int backStack = 1;

    LinearLayout subscribeLayout1;
    EditText regKey1, regKey2, regKey3, regKey4;


    static SubscribeActivity subscribeActivity;


   /* private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AVtQ34ctu3rWfhUDFVd9h4p7es67mrzuUAm6OBeBFINdZVF3QE61av-urQiYQBx_HAZgBQshPxET9nw6");
*/

    String []userInfo;
    String userId,access_token,classId,device_id;
    ImageView img_offer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = SubscribeActivity.this;
        setContentView(R.layout.activity_subscribe);
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userId = userInfo[0];
        device_id = userInfo[3];
        classId = userInfo[4];

        subscribeActivity = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // String flag = extras.getString("flag");
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activation");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_gradient_home));
        initMainLayoutViews();
        initLayout1Views();
        setKeyEnabled(null, regKey1, regKey2);
        setKeyEnabled(regKey1, regKey2, regKey3);
        setKeyEnabled(regKey2, regKey3, regKey4);
        setKeyEnabled(regKey3, regKey4, null);



        initUI();
       /* receiver=new ActivationBroadCastReceiver();
        registerReceiver(receiver,new IntentFilter("paymentprocess"));*/
    }

    private void initUI()
    {
        userInfo= SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userId = userInfo[0];
        classId = userInfo[4];

       MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
        UserProfile userProfile=db.userDAO().getUserProfile(userId);
        if(userProfile==null)
        {
            return;
        }

    }
    String currency_type = Constants.INR;

    private void initMainLayoutViews() {
        subscribeLayout1 = findViewById(R.id.subscribeLayout1);
        img_offer = findViewById(R.id.img_offer);
        subscribeLayout1.setVisibility(View.VISIBLE);
        img_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeOnline(v);
            }
        });
    }

    private void initLayout1Views() {
        regKey1 = findViewById(R.id.regKey1);
        regKey2 = findViewById(R.id.regKey2);
        regKey3 = findViewById(R.id.regKey3);
        regKey4 = findViewById(R.id.regKey4);
        regKey1.requestFocus();
    }

    private void setKeyEnabled(final EditText prevKey, final EditText currentKey, final EditText nextKey) {
        currentKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 4) {
                    String s=editable.toString();
                   /* if(s.startsWith("SD"))
                    {
                        if(!AppConfig.checkSdcard(classId))
                        {
                            currentKey.setText("");
                            CommonUtils.showToast(getApplicationContext(),"Please insert Current Class SDcard");
                            return;
                        }
                    }*/
                    if (nextKey != null)
                        nextKey.requestFocus();
                } else if (editable.length() == 0) {
                    //if (prevKey != null)
                        //prevKey.requestFocus();
                }
            }
        });
    }
    public void subscribeNow(View view) {
       /* if(true)
        {
            showActivationStatus();
            return;
        }*/
        AppController.getInstance().playAudio(R.raw.button_click);
        final String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }
        String key_1=regKey1.getText().toString().trim();
        String key_2=regKey2.getText().toString().trim();
        String key_3=regKey3.getText().toString().trim();
        String key_4=regKey4.getText().toString().trim();

        if (key_1.isEmpty()||key_2.isEmpty()||key_3.isEmpty()||key_4.isEmpty()) {
            CommonUtils.showToast(getApplicationContext(), "Please enter valid activation key");
            return;
        }
        String regKey = key_1 + "-"
                + key_2 + "-"
                + key_3 + "-"
                +key_4;
        if (regKey.length() > 0) {

            if(regKey.startsWith("SD"))
            {
                if(!AppConfig.checkSdcard(classId,getApplicationContext()))
                {

                    CommonUtils.showToast(getApplicationContext(),"Please insert Current Class SDcard");
                    return;
                }
            }

            final JSONObject fjson = new JSONObject();
            try {

                fjson.put(Constants.userId, userId);
                fjson.put(Constants.classId, classId);
                fjson.put(Constants.accessToken, access_token);
                fjson.put(Constants.activationKey, regKey);

                fjson.put(Constants.deviceId, android_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomDialog.showDialog(SubscribeActivity.this, false);
            String tag_string_req = Constants.reqRegister;
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.USER_ACTIVATION_KEY,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            CustomDialog.closeDialog();
                            //Log.d(Constants.response, response);
                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    String successMsg = jObj.getString(Constants.message);

                                    SessionManager session = new SessionManager();


                                    String activation_type = jObj.getString("activation_type");
                                    String activation_key = jObj.getString("activation_key");
                                    String activation_start_date = jObj.getString("activation_start_date");
                                    String activation_end_date = jObj.getString("activation_end_date");
                                    String current_date = jObj.getString("current_date");



                                    long date = CommonUtils.daysBetween(activation_start_date, activation_end_date);
                                    String data_url="";
                                    if(jObj.has(Constants.DATA_URL))
                                        data_url = jObj.getString(Constants.DATA_URL);
                                    String secure_data_url = "";
                                    if (jObj.has(Constants.SECURE_DATA_URL))
                                        secure_data_url = jObj.getString(Constants.SECURE_DATA_URL);

                                    session.setLogin(_this,userId,access_token,android_id,activation_type,classId,secure_data_url,data_url,userInfo[7]);

                                    MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
                                    ActivationDetails aDetails=new ActivationDetails();
                                    aDetails.user_id=userId;
                                    aDetails.class_id=classId;
                                    aDetails.activation_type=activation_type;
                                    aDetails.activation_key=activation_key;
                                    aDetails.activation_start_date=activation_start_date;
                                    aDetails.activation_end_date=activation_end_date;
                                    aDetails.days_left=date+"";
                                    aDetails.current_date=current_date;
                                    aDetails.secure_url = secure_data_url;
                                    aDetails.data_url = data_url;
                                    aDetails.device_id = device_id;
                                    ActivationDetails activationDetails= MyDatabase.getDatabase(getApplicationContext()).activationDAO().getActivationDetails(userId,classId);
                                    if(activationDetails==null)
                                        MyDatabase.getDatabase(getApplicationContext()).activationDAO().inserActivation(aDetails);
                                    else MyDatabase.getDatabase(getApplicationContext()).activationDAO().updateActivationDetails(aDetails.activation_end_date,aDetails.activation_start_date,aDetails.activation_type,aDetails.activation_key,aDetails.current_date,aDetails.days_left,aDetails.class_id,aDetails.user_id,aDetails.secure_url,aDetails.data_url);

                                    // db.activationDAO().inserActivation(aDetails);

                                    //SharedPref.setUserInfoActivationKey(_this, activation_type, activation_key, activation_start_date, activation_end_date, date);
                                    showActivationStatus();
                                    //CommonUtils.showToast(getApplicationContext(), successMsg);


                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialog.closeDialog();
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
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            CommonUtils.showToast(getApplicationContext(), "Key should be 14 digit");
        }
    }
    public void subscribeOnline(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }
        startActivity(new Intent(getApplicationContext(), SubscribeOnlineActivity.class));
        finish();
    }
    boolean isCalled;

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 200) {
            setResult(200);
            finish();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(receiver);
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (isCalled) {

            MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
            ActivationDetails activationDetails= db.activationDAO().getActivationDetails(userId,classId);
            String activationKey = activationDetails.activation_key;
            if (activationKey != null && !activationKey.isEmpty()) {
                setResult(200);
                finish();
            }
        }
    }

    AlertDialog alertDialog;
    private void showActivationStatus()
    {
        SharedPref sh=new SharedPref();
        sh.setExpired(getApplicationContext(),false);
        sh.setActiveStatus(getApplicationContext(),"A");
        AppConfig.CALLED_EXPIERY=false;
        Intent i = new Intent(SubscribeActivity.this, ActivationResultActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        startActivity(i);
        finish();

        /*alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        LayoutInflater inflater = SubscribeActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_activation_status, null);
        alertDialog.setView(dialogView);
        Button btDialogYes = dialogView.findViewById(R.id.btDialogYes);
        btDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    alertDialog.cancel();
                }
                setResult(200);
                Intent i = new Intent(SubscribeActivity.this, HomeTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                startActivity(i);
                finish();

            }
        });
        //alertDialog.getWindow().setLayout(300, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.show();*/

    }

}
