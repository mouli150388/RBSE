package com.tutorix.tutorialspoint.registration;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

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
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.classes.ClassesAdapter;
import com.tutorix.tutorialspoint.classes.model.StudentClass;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SchoolInfoActivity extends AppCompatActivity {
    AppCompatEditText inputName;
    TextView txt_school_name;
    TextView txt_school_village;
    TextView txt_school_district;

    LinearLayout lnr_school_info;
    EditText input_school_district;
    EditText input_school_village;
    EditText input_school_name;

    Spinner spinner_class;
    SchoolInfoActivity _this;
    String school_code;
    String user_id;
    String access_token;
    String class_id;
    String school_id;

    ArrayList<Object> classList;
    ClassesAdapter classesAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = SchoolInfoActivity.this;
        CommonUtils.setFullScreen(_this);
        checkOrientation();
        setContentView(R.layout.activity_school_info);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.information_of_schoole));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        lnr_school_info = findViewById(R.id.lnr_school_info);
        spinner_class = findViewById(R.id.spinner_class);

        input_school_name = findViewById(R.id.input_school_name);
        input_school_village = findViewById(R.id.input_school_village);
        input_school_district = findViewById(R.id.input_school_district);
        inputName = findViewById(R.id.input_name);

        classList = new ArrayList<>();
        classesAdapter = new ClassesAdapter();

        StudentClass stdCls = new StudentClass();
        stdCls.title = getString(R.string.select_class);
        classList.add(stdCls);

        String clstring = getString(R.string._class);

        stdCls = new StudentClass();
        stdCls.title = clstring + " 6";
        stdCls.id = "1";
        classList.add(stdCls);


        stdCls = new StudentClass();
        stdCls.title = clstring + " 7";
        stdCls.id = "2";
        classList.add(stdCls);

        stdCls = new StudentClass();
        stdCls.title = clstring + " 8";
        stdCls.id = "3";
        classList.add(stdCls);

        stdCls = new StudentClass();
        stdCls.title = clstring + " 9";
        stdCls.id = "4";
        classList.add(stdCls);


        stdCls = new StudentClass();
        stdCls.title = clstring + " 10";
        stdCls.id = "5";
        classList.add(stdCls);

        stdCls = new StudentClass();
        stdCls.title = clstring + " 11";
        stdCls.id = "6";
        classList.add(stdCls);

        stdCls = new StudentClass();
        stdCls.title = clstring + " 12";
        stdCls.id = "8";
        classList.add(stdCls);

        stdCls = new StudentClass();
        stdCls.title = "IIT-JEE";
        stdCls.id = "9";
        classList.add(stdCls);


        stdCls = new StudentClass();
        stdCls.title = "NEET";
        stdCls.id = "10";
        classList.add(stdCls);


        classesAdapter.setList(classList);
        spinner_class.setAdapter(classesAdapter);

        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    class_id = ((StudentClass) classList.get(i)).id;
                } else {
                    class_id = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent in = getIntent();
        String userdata[] = new SessionManager().getUserInfo(getApplicationContext());
        user_id = userdata[0];
        access_token = userdata[1];

    }

    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        finish();
        return super.onSupportNavigateUp();
    }

    public void saveInfo(View view) {
        if (!(Objects.requireNonNull(school_code.isEmpty()))) {
            if (class_id.isEmpty()) {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.fields_mandatory));
                return;
            }
            if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                return;
            }
            final JSONObject fjson = new JSONObject();


            try {

                fjson.put(Constants.userId, user_id);
                fjson.put(Constants.accessToken, access_token);
                fjson.put("rbse_school_id", school_id);
                fjson.put("class_id", class_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String reqRegister = Constants.reqRegister;
//DialogUtils.showProgressDialog(_this);

            StringRequest strReq = new StringRequest(
                    Request.Method.POST,
                    Constants.SET_SCHOOL_INFO,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            CustomDialog.closeDialog();
                            Log.v("Error", "Error " + response);

                            try {
                                if (response != null) {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean(Constants.errorFlag);
                                    Log.d("jObj", "jObj " + jObj.toString());
                                    if (!error) {

                                        //new SessionManager() .setSchoolSetUp(getApplicationContext(),"Y");

                                        try {


                                            //AppConfig.ALERT_TIME_TOTAL= 0;
                                            AppConfig.CALLED_EXPIERY = false;
                                            if (!error) {
                                                String activationType = jObj.getString(Constants.activationType);
                                                String activationEndDate = jObj.getString(Constants.activationEndDate);
                                                String activation_start_date = jObj.getString(Constants.activationStartData);
                                                String activation_key = jObj.getString(Constants.activationKey);
                                                String current_date = jObj.getString(Constants.currentDate);
                                                String data_url = jObj.getString(Constants.DATA_URL);
                                                String secure_data_url = jObj.getString(Constants.SECURE_DATA_URL);
                                                ActivationDetails aDeails = new ActivationDetails();
                                                aDeails.current_date = current_date;
                                                aDeails.user_id = user_id;
                                                aDeails.activation_type = activationType;
                                                aDeails.activation_key = activation_key;
                                                aDeails.activation_start_date = activation_start_date;
                                                aDeails.activation_end_date = activationEndDate;
                                                aDeails.class_id = class_id;
                                                aDeails.secure_url = secure_data_url;
                                                aDeails.data_url = data_url;

                                                aDeails.device_id = jObj.getString("activation_device_id");

                                                String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

                                                String device_id = userInfo[3];
                                                if (jObj.has(Constants.ACTIVATION_DEVICE_ID))
                                                    if (jObj.getString(Constants.ACTIVATION_DEVICE_ID) != null && !jObj.getString(Constants.ACTIVATION_DEVICE_ID).isEmpty())
                                                        device_id = jObj.getString(Constants.ACTIVATION_DEVICE_ID);


                                                SessionManager s = new SessionManager();
                                                s.setLogin(_this, user_id, userInfo[1], device_id, activationType, class_id, secure_data_url, data_url, "Y");


                                                long days = 0;
                                                if (activation_key.length() > 2) {
                                                    days = CommonUtils.daysBetween(activation_start_date, activationEndDate);
                                                }
                                                aDeails.days_left = days + "";
                                                ActivationDetails activationDetails = MyDatabase.getDatabase(_this).activationDAO().getActivationDetails(user_id, class_id);
                                                if (activationDetails == null)
                                                    MyDatabase.getDatabase(_this).activationDAO().inserActivation(aDeails);
                                                else
                                                    MyDatabase.getDatabase(_this).activationDAO().updateActivationDetails(aDeails.activation_end_date, aDeails.activation_start_date, aDeails.activation_type, aDeails.activation_key, aDeails.current_date, aDeails.days_left, aDeails.class_id, aDeails.user_id, aDeails.secure_url, aDeails.data_url);
                                                new SharedPref().setIsFirstime(getApplicationContext(), false);
                                                String activation_sd_card_key = "";
                                                if (jObj.has("activation_sd_card_key")) {
                                                    activation_sd_card_key = jObj.getString("activation_sd_card_key");
                                                    if (activation_sd_card_key != null && !activation_sd_card_key.isEmpty()) {
                                                        SDActivationDetails sdActivationDetails = new SDActivationDetails();
                                                        sdActivationDetails.user_id = user_id;
                                                        sdActivationDetails.class_id = aDeails.class_id;
                                                        sdActivationDetails.activation_key = activation_sd_card_key;
                                                        sdActivationDetails.activation_end_date = aDeails.activation_end_date;
                                                        sdActivationDetails.device_id = device_id;
                                                        sdActivationDetails.current_date = current_date;

                                                        SDActivationDetails sdactivationDetails = MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().getActivationDetails(user_id, aDeails.class_id);
                                                        if (sdactivationDetails == null)
                                                            MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().inserActivation(sdActivationDetails);
                                                        else
                                                            MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().updateActivationDetails(sdActivationDetails.activation_end_date, sdActivationDetails.activation_key, sdActivationDetails.current_date, sdActivationDetails.class_id, sdActivationDetails.user_id, sdActivationDetails.device_id);

                                                    }
                                                }


                                                //Intent in=new Intent(AreYouSureActivity.this,LandingPageActivity.class);
                                                //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                // startActivity(in);
                                                // finish();


                                                SharedPref sh = new SharedPref();
                                                sh.setExpired(getApplicationContext(), false);
                                                Intent in = new Intent(_this, HomeTabActivity.class);
                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(in);
                                                finish();


                                            } else {
                                                String errorMsg = jObj.getString(Constants.message);
                                                CommonUtils.showToast(getApplicationContext(), errorMsg);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            CustomDialog.closeDialog();
                                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                                            //  Toasty.warning(AllNotificationActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                        }
                                        // startActivity(new Intent(getApplicationContext(), HomeTabActivity.class));
                                        // finish();
                                    } else {
                                        String errorMsg = jObj.getString(Constants.message);
                                        CommonUtils.showToast(getApplicationContext(), errorMsg);
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
            CustomDialog.showDialog(_this, true);
            AppController.getInstance().addToRequestQueue(strReq, reqRegister);
        } else {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.fields_mandatory));
        }
    }

    public void getSchoolData(View view) {
        school_code = inputName.getText().toString().trim();
        if (!(Objects.requireNonNull(school_code.isEmpty()))) {
            getSchoolData();
        } else {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.fields_mandatory));
        }
    }

    private void checkOrientation() {
        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void getSchoolData() {
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
            return;
        }
        final JSONObject fjson = new JSONObject();


        try {

            fjson.put(Constants.userId, user_id);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("rbse_school_code", school_code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
//DialogUtils.showProgressDialog(_this);

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.GET_SCHOOL_INFO,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        Log.v("Error", "Error " + response);

                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    String rbse_school_code = jObj.getString("rbse_school_code");
                                    String rbse_school_name = jObj.getString("rbse_school_name");
                                    String rbse_school_village = jObj.getString("rbse_school_village");
                                    String rbse_school_district = jObj.getString("rbse_school_district");
                                    school_id = jObj.getString("rbse_school_id");
                                    lnr_school_info.setVisibility(View.VISIBLE);
                                    input_school_name.setText(rbse_school_name);
                                    input_school_village.setText(rbse_school_village);
                                    input_school_district.setText(rbse_school_district);

                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
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
        CustomDialog.showDialog(_this, true);
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }
}