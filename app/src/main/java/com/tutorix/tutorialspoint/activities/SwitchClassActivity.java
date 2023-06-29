package com.tutorix.tutorialspoint.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.database.ClassesDAO;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.ClassModel;
import com.tutorix.tutorialspoint.models.MockTestModelTable;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.models.RecomandedModel;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.registration.RegistrationActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwitchClassActivity extends AppCompatActivity {
    /*
        @BindView(R.id.btn_sixth)
        Button btnSixth;
        @BindView(R.id.btn_seventh)
        Button btnSeventh;
        @BindView(R.id.btn_eigth)
        Button btnEigth;
        @BindView(R.id.btn_more)
        Button btnMore;*/
   /* @BindView(R.id.lnr_classes)
    LinearLayout lnr_classes;*/
    Activity _this;
    boolean islogin;
    boolean isFromOTP;
    boolean isActivated;

    List<ClassModel> listClasses;


    @BindView(R.id.img_home)
    ImageView imgHome;

    @BindView(R.id.lnr_home)
    LinearLayout lnrHome;
    @BindView(R.id.lnr_skip)
    LinearLayout lnrSkip;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_classes)
    RecyclerView recycler_classes;

    private String access_token, userid, loginType, currentClassId;
    int selectedClassId;
    String mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = SwitchClassActivity.this;
        CommonUtils.setFullScreen(_this);
        setContentView(R.layout.activity_switch_class);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        listClasses = new ArrayList<>();
        islogin = getIntent().getBooleanExtra("islogin", false);
        isActivated = getIntent().getBooleanExtra("isActivated", false);
        isFromOTP = getIntent().getBooleanExtra("isFromOTP", false);
        mobile_number=getIntent().getStringExtra(Constants.phoneNumber);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        if (islogin) {
            if (isActivated) {


                if(!isFromOTP) {
                    Intent intent = new Intent(SwitchClassActivity.this, HomeTabActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
            }
        }
      //  lnr_classes.removeAllViews();
        if(islogin)
        getClassesOffline();
        else getClassesOnline();
        //initUI();
    }


    private void initUI() {

        recycler_classes.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recycler_classes.setAdapter(new ClassAdapter());
        /*lnr_classes.removeAllViews();
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        String class_id = userInfo[4];
        for (int k = 0; k < listClasses.size(); k++) {
           // if (listClasses.get(k).class_status) {
                View view = getLayoutInflater().inflate(R.layout.class_selection_item, null);
                Button b = view.findViewById(R.id.btn_class);
                b.setText(listClasses.get(k).class_name.toUpperCase());
                b.setTag(k);

                //b.setEnabled(listClasses.get(k).class_status);

                if (islogin) {

                    if (class_id.equals(listClasses.get((Integer) b.getTag()).class_id + "")) {
                        b.setEnabled(false);
                    }
                }
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppController.getInstance().playAudio(R.raw.button_click);
                        if(!(listClasses.get((Integer) b.getTag()).class_status))
                        {
                            CommonUtils.showToast(getApplicationContext(),"Coming Soon!");
                            return;
                        }
                        startSureActivity(Integer.parseInt(listClasses.get((Integer) b.getTag()).class_id  + ""));
                    }
                });
                lnr_classes.addView(view);
           // }


        }*/

        if (islogin) {
            if(isActivated){
                lnrHome.setVisibility(View.GONE);
                toolbar.setVisibility(View.INVISIBLE);
            }else
            {
                lnrSkip.setVisibility(View.GONE);
            }

            toolbar.setVisibility(View.VISIBLE);
        }else
        {
            lnrSkip.setVisibility(View.GONE);
        }

    }

    public void sixthClass(View view) {
        startSureActivity(1);
    }

    public void seventhClass(View view) {
        startSureActivity(2);
    }

    public void eightClass(View view) {
        CommonUtils.showToast(getApplicationContext(), getString(R.string.comming_soon));
    }

    private void startSureActivity(int _classId) {
       /* Intent i = new Intent(_this, AreYouSureActivity.class);
        i.putExtra(Constants.classId, classId);
        i.putExtra("islogin", islogin);
        startActivityForResult(i, 200);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();*/




        //String classname = _classId.trim().replaceAll(getString(R.string._th_class), "").trim();
        //int classId = Integer.parseInt(classname) - 5;
        selectedClassId = _classId;
        if (!getIntent().getBooleanExtra("islogin", false)) {
            if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
                CommonUtils.showToast(getApplicationContext(), getResources().getString(R.string.no_internet));
                return;
            }
            Intent i = new Intent(SwitchClassActivity.this, RegistrationActivity.class);
            i.putExtra(Constants.classId, selectedClassId);
            i.putExtra(Constants.phoneNumber, mobile_number);
            //Log.v("Class ID","Class ID 3 "+selectedClassId);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else {
            String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

            access_token = userInfo[1];
            userid = userInfo[0];
            loginType = userInfo[2];
            currentClassId = userInfo[4];




            if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
                ActivationDetails activationDetails = MyDatabase.getDatabase(_this).activationDAO().getActivationDetails(userid, selectedClassId+"");
                if(activationDetails!=null) {

                    if(AppConfig.checkSDCardEnabled(getApplicationContext(),userid,selectedClassId+""))
                    {

                        //AppConfig.ALERT_TIME_TOTAL= 0;
                        AppConfig.CALLED_EXPIERY= false;
                        SharedPref sh = new SharedPref();
                        sh.setExpired(getApplicationContext(), false);
                        sh.setActiveStatus(getApplicationContext(),activationDetails.activation_type);


                        String device_id = userInfo[3];

                        SessionManager s = new SessionManager();
                        s.setLogin(_this, userid, userInfo[1], activationDetails.device_id, activationDetails.activation_type, activationDetails.class_id,activationDetails.secure_url,activationDetails.data_url,userInfo[7]);



                        Intent in = new Intent(_this, HomeTabActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
                        return;
                    }

                }
                CommonUtils.showToast(getApplicationContext(), getResources().getString(R.string.no_internet));
                return;
            }
           //AppConfig.ALERT_TIME_TOTAL= 0;
            AppConfig.CALLED_EXPIERY= false;

            if (AppConfig.checkSDCardEnabled(_this,userid,currentClassId)&&AppConfig.checkSdcard(currentClassId,getApplicationContext())) {
                callSwitchClass(selectedClassId + "");
                //syncDataOfflineData();
            }
            else {

                callSwitchClass(selectedClassId + "");
            }
           /* SharedPreferences sh=SharedPref.getUserInfo(getApplicationContext());
            SharedPref.setUserInfo(_this, sh.getString(Constants.fullName,""), sh.getString(Constants.mobileNumber,""),
                    sh.getString(Constants.password,""), sh.getString(Constants.mobile_country_code,""), String.valueOf(classId));
    */
        }


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
    public void onBackPressed() {

        if (islogin) {
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
            return;
        }
        Intent i = new Intent(SwitchClassActivity.this, LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    StringRequest strReqSwitch;
    private void callSwitchClass(final String class_id) {
        final JSONObject fjson = new JSONObject();

        try {
            String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.deviceId, android_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;


        strReqSwitch = new StringRequest(Request.Method.PUT,
                AppConfig.BASE_URL + "users/class/" + userid,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            //AppConfig.ALERT_TIME_TOTAL= 0;
                            AppConfig.CALLED_EXPIERY= false;
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
                                aDeails.user_id = userid;
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
                                    if(jObj.getString(Constants.ACTIVATION_DEVICE_ID)!=null&&!jObj.getString(Constants.ACTIVATION_DEVICE_ID).isEmpty())
                                        device_id = jObj.getString(Constants.ACTIVATION_DEVICE_ID);


                                SessionManager s = new SessionManager();
                                s.setLogin(_this, userid, userInfo[1], device_id, activationType, class_id, secure_data_url, data_url,userInfo[7]);


                                long days = 0;
                                if (activation_key.length() > 2) {
                                    days = CommonUtils.daysBetween(activation_start_date, activationEndDate);
                                }
                                aDeails.days_left = days + "";
                                ActivationDetails activationDetails = MyDatabase.getDatabase(_this).activationDAO().getActivationDetails(userid, class_id);
                                if (activationDetails == null)
                                    MyDatabase.getDatabase(_this).activationDAO().inserActivation(aDeails);
                                else
                                    MyDatabase.getDatabase(_this).activationDAO().updateActivationDetails(aDeails.activation_end_date, aDeails.activation_start_date, aDeails.activation_type, aDeails.activation_key, aDeails.current_date, aDeails.days_left, aDeails.class_id, aDeails.user_id, aDeails.secure_url, aDeails.data_url);
                                new SharedPref().setIsFirstime(getApplicationContext(),false);
                                String  activation_sd_card_key="";
                                if(jObj.has("activation_sd_card_key"))
                                {
                                    activation_sd_card_key=jObj.getString("activation_sd_card_key");
                                    if(activation_sd_card_key!=null&&!activation_sd_card_key.isEmpty())
                                    {
                                        SDActivationDetails sdActivationDetails= new SDActivationDetails();
                                        sdActivationDetails.user_id=userid;
                                        sdActivationDetails.class_id=aDeails.class_id;
                                        sdActivationDetails.activation_key=activation_sd_card_key;
                                        sdActivationDetails.activation_end_date=aDeails.activation_end_date ;
                                        sdActivationDetails.device_id=device_id;
                                        sdActivationDetails.current_date=current_date;

                                        SDActivationDetails sdactivationDetails= MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().getActivationDetails(userid,aDeails.class_id);
                                        if(sdactivationDetails==null)
                                            MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().inserActivation(sdActivationDetails);
                                        else MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().updateActivationDetails(sdActivationDetails.activation_end_date,sdActivationDetails.activation_key,sdActivationDetails.current_date,sdActivationDetails.class_id,sdActivationDetails.user_id,sdActivationDetails.device_id);

                                    }
                                }


                                //Intent in=new Intent(AreYouSureActivity.this,LandingPageActivity.class);
                                //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                // startActivity(in);
                                // finish();
                                if (!activation_sd_card_key.isEmpty()) {
                                    syncServerData();
                                } else {
                                    SharedPref sh=new SharedPref();
                                    sh.setExpired(getApplicationContext(),false);
                                    Intent in = new Intent(_this, HomeTabActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);
                                    finish();
                                }

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
                                MyDatabase dbHandler = MyDatabase.getDatabase(getApplicationContext());
                                dbHandler.userDAO().deleteUser(userid);
                                SessionManager.logoutUser(getApplicationContext());
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CustomDialog.closeDialog();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //  Toasty.warning(AllNotificationActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    CustomDialog.closeDialog();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
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

                //Toasty.warning(AllNotificationActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                           /* headers.put("Content-Type", "application/json");
                            //or try with this:*/
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

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
        CustomDialog.showDialog(SwitchClassActivity.this, false);
        AppController.getInstance().addToRequestQueue(strReqSwitch, tag_string_req);
    }


    private void syncDataOfflineData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                syncData();
            }
        });

    }

    MyDatabase hadnler;

    private void syncData() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        TrackModel trackData = hadnler.trackDAO().getUnSyncTrackingDetails(userid, currentClassId);

        if (trackData != null) {
            //Log.v("Track ","Track "+trackData.activity_type);
            if (!trackData.activity_type.equalsIgnoreCase("Q") || !trackData.activity_type.equalsIgnoreCase("M"))
                callService(trackData);
            else {
                hadnler.trackDAO().updateTrackSyncStatus("Y", trackData.id + "");
                syncData();
            }
        } else {
            syncBookmarks();
        }

    }

    private void syncBookmarks() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        SubChapters chapters = hadnler.subjectChapterDAO().getBookSync(userid, currentClassId);
        if (chapters != null)
            action(chapters);
        else {
            syncQuiz();

        }
    }

    private void syncQuiz() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        QuizModel revieR = hadnler.quizModelDAO().getQuizDetailsSync(userid, currentClassId);
        if (revieR != null) {
            SubmitToServer(revieR);
        } else {
            syncRecomanded();
            //callDeactivation();
        }
    }

    private void syncRecomanded() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        RecomandedModel mocktable = hadnler.recomandedDAO().getRecomanded(userid, currentClassId);
        if (mocktable != null) {
            SubmitToServerRecomanded(mocktable);
        } else {
            clearUserData();
            //callDeactivation();
        }
    }

    private void SubmitToServerRecomanded(final RecomandedModel quizeR) {


        final JSONObject fjson = new JSONObject();
        JSONObject quizObj = new JSONObject();
        try {
            fjson.put(Constants.classId, quizeR.class_id);
            fjson.put(Constants.userId, quizeR.user_id);
            fjson.put(Constants.lectureId, quizeR.lecture_id);
            fjson.put(Constants.sectionId, quizeR.section_id);
            fjson.put(Constants.subjectId, quizeR.subject_id);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("lecture_title", quizeR.lecture_title);
            fjson.put("mocktest_type", quizeR.mocktest_type);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.QUIZ_SYNC_TO_SERVER_RECOMANDED,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {


                            //Log.v("response ","response "+response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                hadnler = MyDatabase.getDatabase(getApplicationContext());
                                hadnler.recomandedDAO().updateMockTestSyncStatus("Y", quizeR._id + "");

                                syncRecomanded();
                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
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

                // Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

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

    private void callService(final TrackModel trackData) {

        //Log.e("Sync Id ", "Sync Id track " + trackData.id + " " + trackData.activity_date);

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put("activity_type", trackData.activity_type);
            fjson.put("subject_id", trackData.subject_id);
            fjson.put("activity_duration", trackData.activity_duration);
            fjson.put("lecture_id", trackData.lecture_id);
            fjson.put("section_id", trackData.section_id);
            fjson.put("created_dtm", trackData.activity_date);
            fjson.put(Constants.lectureName, trackData.lecture_name);
            fjson.put(Constants.classId, trackData.class_id);
            fjson.put(Constants.accessToken, access_token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BASE_URL + "users/track", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);

                try {

                    JSONObject jObj = new JSONObject(response);
                    //Log.d(Constants.response, response);
                    boolean error = jObj.getBoolean(Constants.errorFlag);
                    if (!error) {
                        hadnler = MyDatabase.getDatabase(getApplicationContext());
                        hadnler.trackDAO().updateTrackSyncStatus("Y", trackData.id + "");
                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();

                        syncData();
                    } else {

                        String errorMsg = jObj.getString(Constants.message);
                        CommonUtils.showToast(getApplicationContext(), errorMsg);
                        //Log.v("Error", "Error errorMsg " + errorMsg);
                        // Toasty.warning(VideoActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (JSONException e) {

                    //Log.v("Error", "Error " + e.getMessage());
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    // Toasty.warning(VideoActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

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
        CustomDialog.closeDialog();
        CustomDialog.showDialog(SwitchClassActivity.this, false);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    private void action(final SubChapters data) {
        final JSONObject fjson = new JSONObject();

        try {
            fjson.put(Constants.userId, data.userid);
            fjson.put(Constants.lectureId, data.lecture_id);
            fjson.put(Constants.sectionId, data.section_id);
            fjson.put(Constants.subjectId, data.subjectid);
            fjson.put(Constants.classId, data.classid);
            fjson.put(Constants.lectureName, data.txt);
            fjson.put(Constants.lectureDuration, data.lecture_duration);
            fjson.put(Constants.lectureVideoUrl, data.lecture_video_url);
            fjson.put(Constants.lectureSRTUrl, data.video_srt);
            fjson.put(Constants.lectureVideoThumb, data.lecture_video_thumb);
            fjson.put(Constants.completedFlag, (data.lecture_completed) ? "Y" : "N");
            fjson.put(Constants.bookmarkFlag, (data.lecture_bookmark) ? "Y" : "N");
            if (data.lecture_notes != null && data.lecture_notes.length() > 0)
                fjson.put(Constants.lectureNotes, data.lecture_notes);
            fjson.put(Constants.accessToken, access_token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.LECTURE_ACTIONS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            //Log.d(Constants.response, response);

                            //Log.v("BookMark Sync", "Sync Id bookmark " + data.id);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            hadnler = MyDatabase.getDatabase(getApplicationContext());
                            hadnler.subjectChapterDAO().updateBookmarkSyncStatus(1, data.id + "");
                            syncBookmarks();
                            if (error) {

                                //Toasty.warning(context, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toasty.warning(context, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

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


    private void SubmitToServer(final QuizModel quizeR) {


        final JSONObject fjson = new JSONObject();
        //JSONObject quizObj = new JSONObject();
        try {
            fjson.put(Constants.classId, quizeR.classId);
            fjson.put(Constants.userId, quizeR.userId);
            fjson.put(Constants.lectureId, quizeR.lectur_id);
            fjson.put(Constants.sectionId, quizeR.section_id);
            fjson.put(Constants.subjectId, quizeR.subject_id);
            fjson.put("quiz_duration", quizeR.QuizDuration);
            fjson.put("total_marks", quizeR.total_correct);
            fjson.put("total_correct", quizeR.total_correct);
            fjson.put("total_wrong", quizeR.total_wrong);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.lectureName, quizeR.lecture_name);
            fjson.put(Constants.sectionName, quizeR.section_name);
            fjson.put(Constants.mockTest, quizeR.mock_test);
            fjson.put("total_questions", quizeR.total + "");
            fjson.put("attempted_questions", quizeR.attempted_questions);
            fjson.put("created_dtm", quizeR.QuizCreatedDtm);
            fjson.put(Constants.lectureDuration, "00:00:00");

            //JSONObject questionsObj = new JSONObject();
            //questionsObj.put("questions", quizeR.question);


            //quizObj.put("quiz", questionsObj);


            fjson.put("quiz_json", String.valueOf(quizeR.question));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.USER_QUIZ_COMPLETED,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            //Log.v("Quize Syn", "Quize Syn " + response);
                            JSONObject jObj = new JSONObject(response);
                            Log.e(Constants.response, response);

                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                hadnler = MyDatabase.getDatabase(getApplicationContext());
                                hadnler.quizModelDAO().updateQuizSyncStatus("Y", quizeR._id + "");

                                syncQuiz();
                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.there_is_error));
                // Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();
                finish();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

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


    private void clearUserData() {
        //Log.d(Constants.response, "Deactivate On Clear Data 1 ");
        try{
            CustomDialog.closeDialog();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        MyDatabase db = MyDatabase.getDatabase(getApplicationContext());
        db.subjectChapterDAO().deleteBookmarkForDeactUser(userid, currentClassId);
        db.trackDAO().deleteTracksForDeactUser(userid, currentClassId);
        db.quizModelDAO().deleteQuizeDetails(userid, currentClassId);
        db.mockTestDAO().deleteMockTest(userid, currentClassId);
        db.recomandedDAO().deleteMockTestRecomanded(userid, currentClassId);
        //String classname = selectedClassId.trim().replaceAll(getString(R.string._th_class), "").trim();
        //int classId = Integer.parseInt(classname) - 5;

        //Log.d(Constants.response, "Deactivate On Clear Data 1 ");

        ;

        callSwitchClass(selectedClassId + "");
    }


    private void syncServerData() {
        CustomDialog.showDialog(SwitchClassActivity.this, true);
        // String classname = selectedClassId.trim().replaceAll(getString(R.string._th_class), "").trim();
        //int classId = Integer.parseInt(classname) - 5;

        getbookmarkscompleted(selectedClassId + "", access_token, userid);

    }

    private void getbookmarkscompleted(final String classid, final String access_token, final String userid) {

        String tag_string_req = Constants.reqRegister;

        final JSONObject fjson3 = new JSONObject();
        try {
            fjson3.put(Constants.classId, classid);
            fjson3.put(Constants.accessToken, access_token);
            fjson3.put(Constants.classId, classid);
            fjson3.put(Constants.subjectId, "");
            fjson3.put("action_type", "A");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message3 = fjson3.toString();
        final String encryption3 = Security.encrypt(message3, Key);

        String encode = "";
        try {
            encode = URLEncoder.encode(encryption3, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = AppConfig.BASE_URL + "lectures/actions/" + userid + "?json_data=" + encode;

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);

                    if (!jObj.getBoolean("error_flag")) {
                        JSONArray array = jObj.getJSONArray("lectures");
                        JSONObject objLect;
                        SubChapters subChapters;
                        hadnler = MyDatabase.getDatabase(getApplicationContext());

                        for (int k = 0; k < array.length(); k++) {
                            subChapters = new SubChapters();
                            objLect = array.getJSONObject(k);
                            subChapters.userid = userid;
                            subChapters.lecture_id = objLect.getString("lecture_id");
                            subChapters.classid = objLect.getString("class_id");
                            subChapters.subjectid = objLect.getString("subject_id");
                            subChapters.section_id = objLect.getString("section_id");
                            subChapters.txt = objLect.getString("lecture_name");
                            subChapters.lecture_duration = objLect.getString("lecture_duration");
                            if (objLect.has("lecture_video_url"))
                                subChapters.lecture_video_url = objLect.getString("lecture_video_url");
                            if (objLect.has("lecture_video_thumb"))
                                subChapters.lecture_video_thumb = objLect.getString("lecture_video_thumb");


                            subChapters.lecture_video_url = Constants.VIDEO_NAME;
                            subChapters.lecture_video_thumb = Constants.VIDEO_THUMB_NAME;
                            subChapters.video_srt = Constants.VIDEO_SRT;
                            subChapters.lecture_completed = (objLect.getString("completed_flag").equals("Y"));
                            subChapters.lecture_bookmark = (objLect.getString("bookmark_flag").equals("Y"));
                            subChapters.lecture_notes = objLect.getString("user_lecture_notes");

                            if (subChapters.lecture_notes != null && subChapters.lecture_notes.length() > 0)
                                subChapters.is_notes = true;

                            subChapters.is_sync = true;

                            hadnler.subjectChapterDAO().addBookMark(subChapters);
                        }
                    }
                    prepareTrackData(classid, access_token, userid);

                    //Log.v("get Data", "get Data " + jObj);
                    //parsebookmarkdata(jObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.there_is_error));
                // Toasty.warning(VideoActivity.this, "There is something error", Toast.LENGTH_SHORT, true).show();

                finish();
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void prepareTrackData(final String class_id, final String access_token, final String userid) {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {
            fjson.put("entity_type", "A");
            fjson.put("entity_id", "0");
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put("offset", "0");
            fjson.put("limit", "200");
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.USER_TRACK + "/" + userid + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, "Track Resposnse " + response);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                JSONArray answers = jObj.getJSONArray(Constants.trackData);
                                hadnler = MyDatabase.getDatabase(getApplicationContext());

                                for (int i = 0; i < answers.length(); i++) {

                                    JSONObject json_data = answers.getJSONObject(i);
                                    TrackModel chapters = new TrackModel();
                                    chapters.activity_type = json_data.getString("activity_type");
                                    chapters.activity_duration = json_data.getString("activity_duration");
                                    chapters.activity_date = json_data.getString("activity_date");
                                    chapters.quiz_id = json_data.getString("quiz_id");
                                    chapters.subject_id = json_data.getString(Constants.subjectId);
                                    chapters.section_id = json_data.getString(Constants.sectionId);
                                    chapters.lecture_id = json_data.getString(Constants.lectureId);
                                    chapters.class_id = json_data.getString(Constants.classId);
                                    chapters.user_id = userid;
                                    chapters.is_sync = true;
                                    if (json_data.has(Constants.lectureName))
                                        chapters.lecture_name = json_data.getString(Constants.lectureName);

                                    chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                                    //hadnler.trackDAO().insertTrack(chapters);


                                    TrackModel tm=hadnler.trackDAO().isTrackAdded(chapters.user_id,chapters.class_id,chapters.subject_id,chapters.section_id,chapters.lecture_id,chapters.activity_type,chapters.activity_date);

                                    if(tm==null)
                                    {
                                        long id= hadnler.trackDAO().insertTrack(chapters);

                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                        prepareQuizData(class_id, access_token, userid);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(), error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void prepareQuizData(String class_id, String access_token, final String userid) {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Log.v("Requset", "Requset " + (Constants.USER_QUIZ_ALL + "/" + userid + "?json_data=" + encode));
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.USER_QUIZ_ALL + "/" + userid + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                hadnler = MyDatabase.getDatabase(getApplicationContext());
                                JSONArray quiz_data_array = jObj.getJSONArray("quiz_data");
                                JSONObject quiz_data;
                                for (int k = 0; k < quiz_data_array.length(); k++) {
                                    quiz_data = quiz_data_array.getJSONObject(k);
                                    String qJson = quiz_data.getString("quiz_json");
                                    qJson = qJson.replace("\\", "");
                                    String lecture_name = "";
                                    if (quiz_data.has(Constants.lectureName))
                                        lecture_name = quiz_data.getString(Constants.lectureName);
                                    QuizModel quizModel = new QuizModel();
                                    quizModel.userId = userid;
                                    quizModel.classId = quiz_data.getString("class_id");
                                    quizModel.subject_id = quiz_data.getString("subject_id");
                                    quizModel.section_id = quiz_data.getString("section_id");
                                    quizModel.lectur_id = quiz_data.getString("lecture_id");
                                    quizModel.QuizDuration = quiz_data.getString("quiz_duration");
                                    quizModel.total_wrong = quiz_data.getString("total_wrong");
                                    quizModel.total_correct = quiz_data.getString("total_marks");
                                    quizModel.total = quiz_data.getInt("total_questions");
                                    quizModel.lecture_name = quiz_data.getString("lecture_name");
                                    quizModel.section_name = quiz_data.getString("section_name");
                                    quizModel.mock_test = quiz_data.getString("mock_test");
                                    quizModel.attempted_questions = quiz_data.getString("attempted_questions");
                                    quizModel.QuizCreatedDtm = quiz_data.getString("created_dtm");
                                    quizModel.quiz_id = "";
                                    quizModel.question = qJson;
                                    quizModel.sync = "Y";
                                    quizModel.lecture_name = lecture_name;

                                    QuizModel qm = hadnler.quizModelDAO().getQuizModel(userid, quizModel.classId, quizModel.subject_id, quizModel.QuizCreatedDtm);
                                    //Log.v("QuizModel","QuizModel "+qm);
                                    if (qm == null)
                                        hadnler.quizModelDAO().addQuiz(quizModel);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                        prepareMockDataData(class_id, access_token, userid);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(), error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Log.v("encryption ", "encryption " + encryption);
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getClassesOnline() {
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_ALL_CLASSES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomDialog.closeDialog();
                Log.v("Class Response","Class Response "+response);
                try {
                    JSONObject obj = new JSONObject(response);

                    if (!obj.getBoolean("error_flag")) {

                        JSONArray array = obj.getJSONArray("classes");
                        listClasses.clear();
                        ClassModel classModel;
                        ClassesDAO classesDAO;
                        classesDAO=MyDatabase.getDatabase(getApplicationContext()).classesDAO();
                        for (int k = 0; k < array.length(); k++) {
                            classModel = new ClassModel();
                            classModel.class_folder = array.getJSONObject(k).getString("class_folder");
                            classModel.class_id = array.getJSONObject(k).getString("class_id");

                            classModel.class_name = array.getJSONObject(k).getString("class_name");
                            classModel.class_name=classModel.class_name.replaceAll("Class",getString(R.string._class));
                            //if(!classModel.class_id.equals("2"))
                            classModel.class_status = array.getJSONObject(k).getString("class_status").equalsIgnoreCase("Y");
                            // else classModel.class_status=true;
                            listClasses.add(classModel);
                            if(classesDAO.getCurrentClass(classModel.class_id)==null)
                                classesDAO.insertClassModel(classModel);
                            else classesDAO.updateClass(classModel.class_id,(classModel.class_status)?1:0);
                        }
                        initUI();
                        return;
                    }
                    CommonUtils.showToast(getApplicationContext(), "Try again ");
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), "Try again ");
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
                String msg="";
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg =getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg =getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = getResources().getString(R.string.error_4);
                }
                CommonUtils.showToast(getApplicationContext(), msg);
                finish();
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        if(AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CustomDialog.showDialog(SwitchClassActivity.this, true);
            AppController.getInstance().addToRequestQueue(request);
        }else{
            CommonUtils.showToast(getApplicationContext(), getResources().getString(R.string.no_internet));
        }
    }
    private void getClassesOffline()
    {
        if(AppStatus.getInstance(getApplicationContext()).isOnline())
        {
            getClassesOnline();
            return;
        }
        listClasses.clear();
        listClasses=MyDatabase.getDatabase(getApplicationContext()).classesDAO().getAllClasses();
        if(listClasses.isEmpty())
            getClassesOnline();
        else
            initUI();
    }

    private void prepareRecomanDataData(String class_id, String access_token, final String userid) {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.QUIZ_SYNC_FROM_SERVER_MOCKSTATS + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            //Log.v("Resposnse Stats","Resposnse Stats "+response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                MyDatabase hadnler = MyDatabase.getDatabase(getApplicationContext());
                                JSONArray quiz_data_array = jObj.getJSONArray("stats_data");
                                JSONObject quiz_data;
                                MockTestModelTable mockTestModelTable;
                                for (int k = 0; k < quiz_data_array.length(); k++) {

                                    quiz_data = quiz_data_array.getJSONObject(k);
                                    mockTestModelTable = new MockTestModelTable();
                                    mockTestModelTable.user_id = quiz_data.getString("user_id");
                                    mockTestModelTable.class_id = quiz_data.getString("class_id");
                                    mockTestModelTable.subject_id = quiz_data.getString("subject_id");
                                    mockTestModelTable.section_id = quiz_data.getString("section_id");
                                    mockTestModelTable.mocktest_type = quiz_data.getString("mocktest_type");
                                    mockTestModelTable.total_attempts = quiz_data.getInt("total_attempts");
                                    mockTestModelTable.total_marks = quiz_data.getInt("total_marks");
                                    mockTestModelTable.total_questions = quiz_data.getInt("total_questions");
                                    mockTestModelTable.low_marks = quiz_data.getInt("low_marks");
                                    mockTestModelTable.high_marks = quiz_data.getInt("high_marks");
                                    mockTestModelTable.created_dtm = quiz_data.getString("created_dtm");
                                    mockTestModelTable.total_questions = mockTestModelTable.total_questions * mockTestModelTable.total_attempts;
                                    MockTestModelTable mockTestModelTables = hadnler.mockTestDAO().getMockTest(userid, mockTestModelTable.class_id, mockTestModelTable.subject_id, mockTestModelTable.section_id, mockTestModelTable.mocktest_type);

                                    if (mockTestModelTables == null) {
                                        hadnler.mockTestDAO().insertRecomanded(mockTestModelTable);
                                    } else {
                                        hadnler.mockTestDAO().updateMockTest(userid, mockTestModelTable.class_id, mockTestModelTable.subject_id, mockTestModelTable.section_id, mockTestModelTable.mocktest_type, mockTestModelTable.total_marks, mockTestModelTable.total_attempts, mockTestModelTable.low_marks, mockTestModelTable.high_marks);
                                    }

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                        CustomDialog.closeDialog();
                        SharedPref sh=new SharedPref();
                        sh.setExpired(getApplicationContext(),false);
                        AppConfig.CALLED_EXPIERY=false;
                        Intent in = new Intent(SwitchClassActivity.this, HomeTabActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(), error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void prepareMockDataData(String class_id, String access_token, final String userid) {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.QUIZ_SYNC_TO_SERVER_RECOMANDED + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            //Log.v("Response","Response Data "+response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                MyDatabase hadnler = MyDatabase.getDatabase(getApplicationContext());
                                JSONArray quiz_data_array = jObj.getJSONArray("recommended_videos");
                                JSONObject quiz_data;
                                for (int k = 0; k < quiz_data_array.length(); k++) {
                                    RecomandedModel model = new RecomandedModel();
                                    quiz_data = quiz_data_array.getJSONObject(k);
                                    model.user_id = quiz_data.getString("user_id");
                                    model.class_id = quiz_data.getString("class_id");
                                    model.subject_id = quiz_data.getString("subject_id");
                                    model.section_id = quiz_data.getString("section_id");
                                    model.lecture_id = quiz_data.getString("lecture_id");
                                    model.lecture_title = quiz_data.getString("lecture_title");
                                    model.mocktest_type = quiz_data.getString("mocktest_type");
                                    model.created_dtm = quiz_data.getString("created_dtm");
                                    model.sync = "Y";
                                    hadnler.recomandedDAO().insertRecomanded(model);

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                        prepareRecomanDataData(class_id, access_token, userid);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(), error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }


                    cacheEntry.data = response.data;

                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                    "UTF-8");
                    return Response.success((jsonString), cacheEntry);
                } catch ( Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @OnClick({R.id.img_home, R.id.lnr_home,R.id.lnr_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_home:
            case R.id.lnr_home:
                Intent in = new Intent(SwitchClassActivity.this, HomeTabActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;
            case R.id.lnr_skip:
                Intent intent = new Intent(SwitchClassActivity.this, HomeTabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppController.getInstance().playAudio(R.raw.back_sound);
        return true;
    }
    /*@OnClick({R.id.btn_sixth, R.id.btn_seventh, R.id.btn_eigth, R.id.btn_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sixth:
                sixthClass(view);
                break;
            case R.id.btn_seventh:
                seventhClass(view);
                break;
            case R.id.btn_eigth:
                eightClass(view);
                break;
            case R.id.btn_more:
                eightClass(view);
                break;
        }
    }*/


    class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewholder>{
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        String class_id = userInfo[4];
        @NonNull
        @Override
        public ClassAdapter.ClassViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ClassViewholder(getLayoutInflater().inflate(R.layout.class_selection_item, null,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ClassAdapter.ClassViewholder holder, int position) {

            holder.btn_class.setText(listClasses.get(position).class_name.toUpperCase());
            holder.btn_class.setTag(position);
            if (islogin) {

                if (class_id.equals(listClasses.get((Integer) holder.btn_class.getTag()).class_id + "")) {
                    holder.btn_class.setEnabled(false);
                }
            }
            holder.btn_class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    if(!(listClasses.get((Integer) v.getTag()).class_status))
                    {
                        CommonUtils.showToast(getApplicationContext(),"Coming Soon!");
                        return;
                    }
                    startSureActivity(Integer.parseInt(listClasses.get((Integer) v.getTag()).class_id  + ""));

                }
            });
        }

        @Override
        public int getItemCount() {
            return listClasses.size();
        }

        class ClassViewholder extends RecyclerView.ViewHolder{

            Button btn_class;
            public ClassViewholder(@NonNull View itemView) {
                super(itemView);
                btn_class=itemView.findViewById(R.id.btn_class);
            }
        }
    }
}
