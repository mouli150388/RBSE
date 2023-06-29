package com.tutorix.tutorialspoint.testseries;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.Gson;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.testseries.data.TestQuestions;
import com.tutorix.tutorialspoint.testseries.data.TestSeriesJson;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.CircularProgressBarThumb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestSeriesReviewActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)    RecyclerView recyclerView;
    @BindView(R.id.toolbar)    Toolbar toolbar;
    @BindView(R.id.lnr_home)    LinearLayout lnr_home;
    @BindView(R.id.correct)    TextView correct;
    @BindView(R.id.attempt)    TextView attempt;
    @BindView(R.id.notattempt)    TextView notattempt;
    @BindView(R.id.holoCircularProgressBar)
    CircularProgressBarThumb holoCircularProgressBar;
    @BindView(R.id.marks)
    TextView marks;
    @BindView(R.id.radio_phy)
    RadioButton radioPhy;
    @BindView(R.id.radio_che)
    RadioButton radioChe;
    @BindView(R.id.radio_math)
    RadioButton radioMath;
    @BindView(R.id.radio_bio)
    RadioButton radioBio;
    @BindView(R.id.radio_sub_group)
    RadioGroup radioSubGroup;

    List<TestQuestions> listQuiz;
    TestSeriesReviewAdapter testSeriesReviewAdapter;
    String basePath ;
    String classid;
    private String access_token;
    private String userId;
    private String loginType;
    TestSeriesJson testSeriesJson;
    int currrentClsId;
    String test_series_id;
    TestSeriesReviewActivity _this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series_review);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        _this=this;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        test_series_id=getIntent().getStringExtra("test_series_id");


        recyclerView.setHasFixedSize(true);
        // chaptersAdapter.notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userId = userInfo[0];;
        loginType = userInfo[2];
        classid = userInfo[4];
        currrentClsId = Integer.parseInt(classid);
        basePath = AppConfig.getOnlineURLImage(classid)+"test-series/";

            basePath = AppConfig.getOnlineURLImage(currrentClsId+"")+"test-series/";


        testSeriesReviewAdapter=new TestSeriesReviewAdapter(this,basePath,currrentClsId);
        recyclerView.setAdapter(testSeriesReviewAdapter);

        fillWithData();
        holoCircularProgressBar.setProgressColor((R.color.che_background));


        radioSubGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {

                    case R.id.radio_phy:
                        testSeriesReviewAdapter.clearAll();
                        testSeriesReviewAdapter.addData(testSeriesJson.physics.questions);
                        break;
                    case R.id.radio_che:
                        testSeriesReviewAdapter.clearAll();
                        testSeriesReviewAdapter.addData(testSeriesJson.chemistry.questions);
                        break;
                    case R.id.radio_math:
                        testSeriesReviewAdapter.clearAll();
                        testSeriesReviewAdapter.addData(testSeriesJson.mathematics.questions);
                        break;
                    case R.id.radio_bio:
                        testSeriesReviewAdapter.clearAll();
                        testSeriesReviewAdapter.addData(testSeriesJson.biology.questions);
                        break;
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                AppController.getInstance().playAudio(R.raw.back_sound);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void readDummyJSON(JSONObject obj)
    {
        try {
           /* InputStream is=getAssets().open("testseries_review.json");
            StringBuffer s=new StringBuffer();
            byte[]b=new byte[is.available()];
            is.read(b);

            JSONObject obj=new JSONObject(new String(b));*/

            Gson gson=new Gson();

            int gain_total_marks=obj.getInt("total_marks");
           String quiz_json= obj.getString("quiz_json");
            testSeriesJson=gson.fromJson(new JSONObject(quiz_json).toString(), TestSeriesJson.class);

            try{
                Log.v("testSeriesJson","testSeriesJson "+testSeriesJson.name);
                Log.v("testSeriesJson","testSeriesJson "+testSeriesJson.total_marks);
                testSeriesReviewAdapter.clearAll();
                testSeriesReviewAdapter.addData(testSeriesJson.physics.questions);
                radioPhy.setChecked(true);
                //testSeriesReviewAdapter.addData(testSeriesJson.chemistry.questions);
               // testSeriesReviewAdapter.addData(testSeriesJson.mathematics.questions);

                try {

                    if (currrentClsId <=7) {

                    } else if (currrentClsId ==8) {
                        radioBio.setVisibility(View.GONE);
                        radioMath.setVisibility(View.VISIBLE);
                        correct.setText(getString(R.string.correct_answeres)+": "+(testSeriesJson.physics.total_correct +testSeriesJson.chemistry.total_correct +testSeriesJson.mathematics.total_correct)+"");
                        attempt.setText(getString(R.string.attemped)+": "+(testSeriesJson.physics.attempted_questions+testSeriesJson.chemistry.attempted_questions+testSeriesJson.mathematics.attempted_questions)+"");
                        notattempt.setText(getString(R.string.wrong_answers)+": "+((testSeriesJson.physics.total_wrong +testSeriesJson.chemistry.total_wrong +testSeriesJson.mathematics.total_wrong))+"");
                        //marks.setText((testSeriesJson.physics.correct_answers+testSeriesJson.chemistry.correct_answers+testSeriesJson.mathematics.correct_answers)+"/"+(testSeriesJson.physics.total_marks+testSeriesJson.chemistry.total_marks+testSeriesJson.mathematics.total_marks));
                        //holoCircularProgressBar.setProgress(((float)(testSeriesJson.physics.correct_answers+testSeriesJson.chemistry.correct_answers+testSeriesJson.mathematics.correct_answers)/(testSeriesJson.physics.total_marks+testSeriesJson.chemistry.total_marks+testSeriesJson.mathematics.total_marks)));
                        if(testSeriesJson.name.contains("J")) {
                            marks.setText((gain_total_marks)+"/"+testSeriesJson.total_marks+"");

                            holoCircularProgressBar.setProgress((float) gain_total_marks / Integer.parseInt(testSeriesJson.total_marks));
                        }else {
                            marks.setText(gain_total_marks+"/"+testSeriesJson.total_marks+"");

                            holoCircularProgressBar.setProgress((float) gain_total_marks / Integer.parseInt(testSeriesJson.total_marks));
                        }
                    } else if (currrentClsId ==9){
                        radioMath.setVisibility(View.GONE);
                        radioBio.setVisibility(View.VISIBLE);
                        correct.setText(getString(R.string.correct_answeres)+": "+(testSeriesJson.physics.total_correct +testSeriesJson.chemistry.total_correct +testSeriesJson.biology.total_correct)+"");
                        attempt.setText(getString(R.string.attemped)+": "+(testSeriesJson.physics.attempted_questions+testSeriesJson.chemistry.attempted_questions+testSeriesJson.biology.attempted_questions)+"");
                        notattempt.setText(getString(R.string.wrong_answers)+": "+((testSeriesJson.physics.total_wrong +testSeriesJson.chemistry.total_wrong +testSeriesJson.biology.total_wrong))+"");

                        //holoCircularProgressBar.setProgress(((float)(testSeriesJson.physics.correct_answers+testSeriesJson.chemistry.correct_answers+testSeriesJson.biology.correct_answers)/(testSeriesJson.physics.total_marks+testSeriesJson.chemistry.total_marks+testSeriesJson.biology.total_marks)));
                       // marks.setText((testSeriesJson.physics.correct_answers+testSeriesJson.chemistry.correct_answers+testSeriesJson.biology.correct_answers)+"/"+(testSeriesJson.physics.total_marks+testSeriesJson.chemistry.total_marks+testSeriesJson.biology.total_marks)+"");



                        if(testSeriesJson.name.contains("J")) {
                            marks.setText(gain_total_marks+"/"+testSeriesJson.total_marks+"");

                            holoCircularProgressBar.setProgress((float) gain_total_marks / Integer.parseInt(testSeriesJson.total_marks));
                        }else {
                            marks.setText(gain_total_marks+"/"+testSeriesJson.total_marks+"");

                            holoCircularProgressBar.setProgress((float)gain_total_marks / Integer.parseInt(testSeriesJson.total_marks));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @OnClick({R.id.lnr_home})
    public void onViewClicked(View view) {
        switch (view.getId())
        {
            case R.id.lnr_home:
                Intent in = new Intent(TestSeriesReviewActivity.this, HomeTabActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;
        }


    }

    private void fillWithData()  {
        String quiz_id;

        quiz_id = getIntent().getStringExtra("quiz_id");



        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("test_series_id", test_series_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;




        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.GET_TEST_SERIES_TRACK_REVIEW,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        // loadingPanelID.hide();
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                readDummyJSON(jObj);
                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(_this,errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(_this,e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
                //loadingPanelID.hide();
                CustomDialog.closeDialog();
                finish();

            }
        }){
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);


            @Override
            protected Map<String, String> getParams() {
                //Log.v("JSON","JSON "+encryption);
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };;
        //loadingPanelID.setVisibility(View.VISIBLE);
        CustomDialog.showDialog(TestSeriesReviewActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
