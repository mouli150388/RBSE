package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.AllNotificationAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.NotificationAll;
import com.tutorix.tutorialspoint.models.Notifications;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNotificationActivity extends AppCompatActivity {


    @BindView(R.id.recycler_notifications)
    RecyclerView recyclerNotifications;
    String access_token;
    String userid;
    private ArrayList<NotificationAll> data;
    private AllNotificationAdapter chaptersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notification);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userid = userInfo[0];

        getNotifications();


        /*if (AppStatus.getInstance(AllNotificationActivity.this).isOnline()) {
            try {
                fillNotificationWithData();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
            //Toasty.info(AllNotificationActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }*/

    }

    private void clearNotification(String userid) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.PUT,
                Constants.USER_NOTIFICATIONS + "/" + userid,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);

                                finish();

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);

                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                            //  Toasty.warning(AllNotificationActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(),error.getMessage());
                //Toasty.warning(AllNotificationActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }) {
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

   /* private void fillNotificationWithData() throws UnsupportedEncodingException {
        data = new ArrayList<>();
        final JSONObject fjson1 = new JSONObject();
        try {
            fjson1.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson1.toString();
        final String encryption = Security.encrypt(message, Key);
        String tag_string_req = Constants.reqRegister;

        CustomDialog.showDialog(AllNotificationActivity.this,false);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.USER_NOTIFICATIONS + "/" + userid + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        //Log.d(Constants.response, response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                JSONArray answers = jObj.getJSONArray("notifications");
                                for (int i = 0; i < answers.length(); i++) {
                                    JSONObject json_data = answers.getJSONObject(i);
                                    NotificationAll chapters = new NotificationAll();
                                    chapters.txt = json_data.getString("notification_title");
                                    chapters.lecture_count = json_data.getString("notification_msg");
                                    data.add(chapters);
                                }

                                recyclerNotifications.setLayoutManager(new LinearLayoutManager(AllNotificationActivity.this, RecyclerView.VERTICAL, false));
                                chaptersAdapter = new AllNotificationAdapter(data, getApplication());
                                recyclerNotifications.setAdapter(chaptersAdapter);
                                recyclerNotifications.setHasFixedSize(true);
                                chaptersAdapter.notifyDataSetChanged();
                                recyclerNotifications.setItemViewCacheSize(20);
                                recyclerNotifications.setDrawingCacheEnabled(true);
                                recyclerNotifications.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                                RecyclerView.ItemAnimator animator = recyclerNotifications.getItemAnimator();

                                if (animator instanceof SimpleItemAnimator) {
                                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
                                }

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
                                // Toasty.warning(AllNotificationActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                            //Toasty.warning(AllNotificationActivity.this, "Json error: " + e.getMessage(),+ Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
                CommonUtils.showToast(getApplicationContext(),error.getMessage());
                //Toasty.warning(AllNotificationActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }*/



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getNotifications();
    }

    private void getNotifications()
    {
        List<Notifications> listNotification= MyDatabase.getDatabase(getApplicationContext()).notificationsDAO().getAll();

        Log.v("listNotification ","listNotification "+listNotification.size());

        recyclerNotifications.setLayoutManager(new LinearLayoutManager(AllNotificationActivity.this, RecyclerView.VERTICAL, false));
        chaptersAdapter = new AllNotificationAdapter(listNotification, getApplication());
        recyclerNotifications.setAdapter(chaptersAdapter);
        recyclerNotifications.setHasFixedSize(true);
        chaptersAdapter.notifyDataSetChanged();
        recyclerNotifications.setItemViewCacheSize(20);
        recyclerNotifications.setDrawingCacheEnabled(true);
        recyclerNotifications.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.ItemAnimator animator = recyclerNotifications.getItemAnimator();
    }
}
