package com.tutorix.tutorialspoint.sdcardactivation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.material.snackbar.Snackbar;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.BuildConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SDCardActivation extends AppCompatActivity {

    String userInfo[];
    String userId;
    String access_token;
    String classId;
    @BindView(R.id.txt_class)
    TextView txtClass;
    @BindView(R.id.online_key)
    EditText onlineKey;
    @BindView(R.id.regKey1)
    EditText regKey1;
    @BindView(R.id.regKey2)
    EditText regKey2;
    @BindView(R.id.regKey3)
    EditText regKey3;
    @BindView(R.id.regKey4)
    EditText regKey4;
    @BindView(R.id.subscribeLayout1)
    LinearLayout subscribeLayout1;
    ActivationDetails aDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcard_activation);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SD Card Activation");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        userInfo = SessionManager.getUserInfo(getApplicationContext());
        userId = userInfo[0];
        access_token = userInfo[1];
        classId = userInfo[4];

        MyDatabase hadnler = MyDatabase.getDatabase(getApplicationContext());
        aDetails = hadnler.activationDAO().getActivationDetails(userId, classId);
        txtClass.setText(AppConfig.getClassNameDisplayClass(classId));
       /* List<ActivationDetails>aList=hadnler.activationDAO().getActivationDetailsList(user_id, calssId);
        for(int k=0;k<aList.size();k++)
        {
            Log.v("ActionvationID","ActionvationIDD "+k+"="+aList.get(k)._id);
        }*/
        if (aDetails != null&&!aDetails.activation_key.isEmpty()) {

            onlineKey.setText(aDetails.activation_key);
            String type = aDetails.activation_type;


        }else
        {
            CommonUtils.showToast(getApplicationContext(),"Please activate online activation first");
            finish();
        }

        setKeyEnabled(null, regKey1, regKey2);
        setKeyEnabled(regKey1, regKey2, regKey3);
        setKeyEnabled(regKey2, regKey3, regKey4);
        setKeyEnabled(regKey3, regKey4, null);
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

    @OnClick({R.id.txt_class, R.id.subscribeLayout1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_class:
                break;
            case R.id.subscribeLayout1:
                break;
        }
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

                    if (nextKey != null)
                        nextKey.requestFocus();
                } else if (editable.length() == 0) {
                    //if (prevKey != null)
                    //prevKey.requestFocus();
                }
            }
        });
    }
    public void sdcardACtivation(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissionForStorage()) {
                ActivityCompat.requestPermissions(SDCardActivation.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                return;
            }
        }
        callSDEnabled();


       /* } else {
            CommonUtils.showToast(getApplicationContext(), "Key should be 14 digit");
        }*/
    }

    private void callSDEnabled()
    {
        AppController.getInstance().playAudio(R.raw.button_click);
        final String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userId);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("activation_key_sd", aDetails.activation_key);
            fjson.put("end_date", aDetails.activation_end_date);

            fjson.put(Constants.deviceId, android_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomDialog.showDialog(SDCardActivation.this, false);
        String tag_string_req = Constants.reqRegister;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.SDCARD_ACTIVATION,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        Log.d(Constants.response, response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {


                                //String activation_type = jObj.getString("activation_type");
                                String activation_key_sd = jObj.getString("activation_key_sd");

                                String activation_end_date = jObj.getString("end_date");
                                String current_date = jObj.getString("current_date");

                                SDActivationDetails sdActivationDetails= new SDActivationDetails();

                                sdActivationDetails.user_id=userId;
                                sdActivationDetails.class_id=classId;
                                sdActivationDetails.activation_key=activation_key_sd;
                                sdActivationDetails.activation_end_date=activation_end_date;
                                sdActivationDetails.device_id=android_id;
                                sdActivationDetails.current_date=current_date;

                                SDActivationDetails activationDetails= MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().getActivationDetails(userId,classId);
                                if(activationDetails==null)
                                    MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().inserActivation(sdActivationDetails);
                                else MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().updateActivationDetails(sdActivationDetails.activation_end_date,sdActivationDetails.activation_key,sdActivationDetails.current_date,sdActivationDetails.class_id,sdActivationDetails.user_id,sdActivationDetails.device_id);

                               /* JSONObject objData=new JSONObject();
                                objData.put("device_id",android_id);
                                objData.put("class_id",classId);
                                objData.put("key",activation_key_sd);

                                JSONObject ob=new JSONObject();
                                ob.put("info",objData.toString());
                                try {
                                    File f=AppConfig.getSdcard(classId);
                                    if(f.exists())
                                    {
                                        File infofile = new File(f, "file_info");
                                        if(infofile.exists())
                                            infofile.delete();
                                        infofile.createNewFile();
                                        FileWriter writer = new FileWriter(infofile);
                                        writer.append(ob.toString());
                                        writer.flush();
                                        writer.close();
                                    }


                                } catch (Exception e) { e.printStackTrace();}
*/
                                Intent in = new Intent(getApplicationContext(), HomeTabActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);
                                finish();
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                  /*  ActivationDetails aDetails=new ActivationDetails();
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
*/


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
                //Log.v("error","error "+error.getMessage());
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
    }

    private boolean checkPermissionForStorage() {
        if(Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
                return  false;
            } else return true;
        }else {
            int  result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(checkPermissionForStorage())
        {
            callSDEnabled();
        }else
        {
            ActivityCompat.requestPermissions(SDCardActivation.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);

        }

    }
}
