package com.tutorix.tutorialspoint.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

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
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.ExpertsAdapter;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.tutorix.tutorialspoint.utility.Constants.FACULTY_CURRENT;

public class SMEActivity extends AppCompatActivity {
    SMEActivity _this;
    private LinearLayoutManager layoutManager;
    private RecyclerView rvExperts;
    private ExpertsAdapter mAdapter;
    String class_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = SMEActivity.this;
        setContentView(R.layout.activity_sme);
        String[] userInfo = SessionManager.getUserInfo(_this);
        final String access_token = userInfo[1];
        final String user_id = userInfo[0];
class_id=userInfo[4];
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setTitle("Subject Matter Experts");

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        if (AppStatus.getInstance(_this).isOnline()) {
            initList(access_token, user_id);
        } else {
            CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
            // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void initList(String access_token, String user_id) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, user_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;
        String url = String.format(FACULTY_CURRENT, AppConfig.getClassName(class_id));
        //Log.v("url ","url "+url);
        CustomDialog.showDialog(SMEActivity.this,true);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        //Log.d(Constants.response, response);
                        List<Data> data = new ArrayList<>();

                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                JSONArray faculties = jObj.getJSONArray(Constants.faculties);
                                for (int i = 0; i < faculties.length(); i++) {
                                    JSONObject json_data = faculties.getJSONObject(i);
                                    Data fishData = new Data();
                                    fishData.uid = json_data.getString(Constants.facultyId);
                                    fishData.full_name = json_data.getString(Constants.facultyName);
                                    fishData.introduction = json_data.getString(Constants.introduction);
                                    fishData.photo_url = json_data.getString(Constants.photoUrl);
                                    fishData.video_url = json_data.getString(Constants.videoUrl);
                                    fishData.video_thumb_url = json_data.getString(Constants.videoThumbUrl);
                                    fishData.expertise = json_data.getString(Constants.expertise);
                                    data.add(fishData);
                                }
                                rvExperts = findViewById(R.id.rvExperts);
                                rvExperts.setLayoutManager(layoutManager);
                                rvExperts.setHasFixedSize(true);
                                mAdapter = new ExpertsAdapter(_this, data);
                                rvExperts.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                rvExperts.setItemViewCacheSize(20);
                                rvExperts.setDrawingCacheEnabled(true);
                                rvExperts.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                               /* RecyclerView.ItemAnimator animator = rvExperts.getItemAnimator();

                                if (animator instanceof SimpleItemAnimator) {
                                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
                                }*/
                                int resId = R.anim.layout_animation_fall_down;
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(SMEActivity.this, resId);
                                rvExperts.setLayoutAnimation(animation);
                                rvExperts.scheduleLayoutAnimation();

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
                                //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
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
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
