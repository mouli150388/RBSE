package com.tutorix.tutorialspoint.classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.tutorix.tutorialspoint.classes.model.StudentDetails;
import com.tutorix.tutorialspoint.classes.model.StudentQuiz;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StudentListActivity extends AppCompatActivity {

    Activity _this;


    private String selected_class_id;
    private String city;
    private String batch_id;
    private String branch_id;
    private StudentListAdapter mAdapter;
    private String userId;
    private String loginType;
    private String access_token;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipe_refresh;
    RecyclerView recyclerView;
    LinearLayout lnr_reload;
    View img_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        _this=this;
        String[] userinfo = SessionManager.getUserInfo(_this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle(getResources().getString(R.string.over_all_activity));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        access_token = userinfo[1];
        loginType = userinfo[2];
        //class_id = userinfo[4];
        userId = userinfo[0];
        Intent in=getIntent();
        if(in==null)
        {
            finish();
            return;
        }
        selected_class_id=in.getStringExtra("selected_class_id");
        city=in.getStringExtra("city");
        batch_id=in.getStringExtra("batch_id");
        branch_id=in.getStringExtra("branch_id");

        swipe_refresh=findViewById(R.id.swipe_refresh);
        lnr_reload=findViewById(R.id.lnr_reload);
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new StudentListAdapter(_this);
        linearLayoutManager = new LinearLayoutManager(_this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        img_filter=findViewById(R.id.img_filter);
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fillWithData();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppController.getInstance().playAudio(R.raw.back_sound);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillWithData()  {




        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, selected_class_id);
            fjson.put("city", city);
            fjson.put("branch_id", branch_id);
            fjson.put("batch_id", batch_id);
            fjson.put("offset", "0");
            fjson.put("limit", "200");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String tag_string_req = Constants.reqRegister;

        Log.v("Constants.",Constants.GET_TUTION_STUDENTS);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.GET_TUTION_STUDENTS, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);

                CustomDialog.closeDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean(Constants.errorFlag);
                    StudentDetails studentDetails;
                    if (!error) {
                        JSONArray stdntArray=jObj.getJSONArray("users");
                       JSONObject obj;
                       JSONObject qobj;

                        StudentQuiz studentQuiz;
                        for(int k=0;k<stdntArray.length();k++)
                        {
                            obj=stdntArray.getJSONObject(k);
                            studentDetails=new StudentDetails();
                            studentDetails.user_id=obj.getString("user_id");
                            studentDetails.user_name=obj.getString("user_name");
                            studentDetails.class_id=obj.getString("class_id");
                            studentDetails.branch_name=obj.getString("branch_name");
                            studentDetails.branch_city=obj.getString("branch_city");
                            studentDetails.batch_name=obj.getString("batch_name");

                            if(obj.has("quiz"))
                            {
                                qobj=obj.getJSONObject("quiz");
                                if(!qobj.getBoolean("error_flag"))
                                {


                                studentQuiz=new StudentQuiz();
                                studentQuiz.quiz_id=qobj.getString("quiz_id");
                                studentQuiz.lecture_id=qobj.getString("lecture_id");
                                studentQuiz.section_id=qobj.getString("section_id");
                                studentQuiz.subject_id=qobj.getString("subject_id");
                                studentQuiz.class_id=qobj.getString("class_id");
                                studentDetails.studentQuiz=studentQuiz;
                                }
                            }

                            mAdapter.add(studentDetails);
                           // mAdapter.add(studentDetails);
                        }

                    } else {
                        String errorMsg = jObj.getString(Constants.message);
                        //CommonUtils.showToast(getActivity(),errorMsg);
                        nodata();
                        // Toasty.warning(BookmarkActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //CommonUtils.showToast(getActivity(),e.getMessage());
                    nodata();
                    //Toasty.warning(BookmarkActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="";
                CustomDialog.closeDialog();

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
                CommonUtils.showToast(_this, msg);
                finish();



            }
        }){
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
        CustomDialog.showDialog(_this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void nodata()
    {
        if(mAdapter!=null&&mAdapter.getItemCount()>0)
            lnr_reload.setVisibility(View.GONE);
        else lnr_reload.setVisibility(View.VISIBLE);
    }
}