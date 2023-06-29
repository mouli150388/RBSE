package com.tutorix.tutorialspoint.ncert;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.AnswerModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NCERTQuestionDetailsActivity extends AppCompatActivity {

    @BindView(R.id.recycler_doubtslist)
    RecyclerView recyclerDoubtslist;
    NCERTQuestionAdapter adapter;
    @BindView(R.id.lnr_top)
    LinearLayout lnr_top;
    @BindView(R.id.img_gif)
    ImageView imgGif;

    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.lnr_home)
    LinearLayout lnrHome;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_background)
    ImageView img_background;
    @BindView(R.id.rel_top_main)
    RelativeLayout rel_top_main;

    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.img_back)
    ImageView imgBack;
    /*
    @BindView(R.id.activity_title)
    TextView activityTitle;

    @BindView(R.id.home)
    LinearLayout lnrHome;


    @BindView(R.id.appbar)
    RelativeLayout appbar;*/
    String classId;
    String userId;
    String access_token;
    String subject_id;
    String sectionName;
    String question_id;
    String desc_excercise;
    List<AnswerModel> listData;
    String subjectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncertquestion_details);
        ButterKnife.bind(this);
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userId = userInfo[0];
        classId = userInfo[4];
        Intent in = getIntent();
        subject_id = in.getStringExtra(Constants.subjectId);
        sectionName = in.getStringExtra(Constants.sectionName);
        desc_excercise = in.getStringExtra("desc_excercise");
        listData=new ArrayList<>();

        question_id = in.getStringExtra(Constants.question_id);
        adapter = new NCERTQuestionAdapter(desc_excercise,userId, NCERTQuestionDetailsActivity.this, false,false);
        adapter.setEnableFavourite(false);
        recyclerDoubtslist.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerDoubtslist.setAdapter(adapter);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        imgGif.setVisibility(View.VISIBLE);
        if(subject_id.equals("1"))
        {
            subjectName="physics";
        }else if(subject_id.equals("2"))
        {
            subjectName="chemistry";
        }else if(subject_id.equals("3"))
        {
            subjectName="biology";
        }else if(subject_id.equals("4"))
        {
            subjectName="mathematics";
        }
        txt_header.setText(sectionName);
        if (subjectName.equalsIgnoreCase("physics")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_phy).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_phy_bg_green);
            //img_background.setImageResource(R.drawable.ic_phy_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_phy_bg_white).into(img_background);


        } else if (subjectName.equalsIgnoreCase("chemistry")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_che).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            //Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
            rel_top_main.setBackgroundResource(R.drawable.ic_chemistry_bg_green);
            //img_background.setImageResource(R.drawable.ic_chemistry_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
           // txt_header.setText(sectionName);

        } else if (subjectName.equalsIgnoreCase("biology")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_bio).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_bio_bg_green);
            //img_background.setImageResource(R.drawable.ic_bio_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_bio_bg_white).into(img_background);
           // txt_header.setText(sectionName);

        } else {

            Glide.with(getApplicationContext()).load(R.drawable.gif_math).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_math_bg_green);
            //img_background.setImageResource(R.drawable.ic_math_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_math_bg_white).into(img_background);
           // txt_header.setText(sectionName);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subject_id) {
                case "1":
                    window.setStatusBarColor(getResources().getColor(R.color.phy_background_status));
                    break;
                case "2":
                    window.setStatusBarColor(getResources().getColor(R.color.che_background_status));
                    break;
                case "3":
                    window.setStatusBarColor(getResources().getColor(R.color.bio_background_status));
                    break;
                case "4":
                    window.setStatusBarColor(getResources().getColor(R.color.math_background_status));
                    break;
            }
        }
        getNswers();
    }
    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @OnClick({ R.id.img_back, R.id.lnr_home})
    public void onViewClicked(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;
            case R.id.lnr_home:
                Intent in = new Intent(NCERTQuestionDetailsActivity.this, HomeTabActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;

        }


    }
    public void getNswers() {
        final JSONObject fjson = new JSONObject();


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.subjectId, subject_id);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.question_id, question_id);
            fjson.put("start_from", 0);
            fjson.put("limit", 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        CustomDialog.showDialog(NCERTQuestionDetailsActivity.this, true);
        String url = "";
        url = Constants.DOUBT_GET_ANSWERS_PUBLISHED;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        try {

                            //Log.v("Resposne", "Resposne " + response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);

                            if (!error) {

                                JSONArray answers = jObj.getJSONArray("answer_array");
                                JSONObject questionObj = jObj.getJSONObject("question_array");
                              String  q_class_id = questionObj.getString("class_id");
                                String question_view_count = questionObj.getString("question_view_count");
                               /* txtView.setText(question_view_count + " Views");
                                txtClass.setText(AppConfig.getClassNameDisplay(questionObj.getString("class_id")));
                                txtSubject.setText(AppConfig.getSubjectName(questionObj.getString("subject_id")));*/
                                AnswerModel questionModel=new AnswerModel();
                                //JSONObject questionObj = jObj.getJSONObject("question_array");

                                questionModel.q_user_id=questionObj.getString("user_id");
                                questionModel.q_user_name=questionObj.getString("full_name");
                                questionModel.q_user_image=questionObj.getString("user_photo");
                                questionModel.question_id=questionObj.getString("question_id");
                                questionModel.question=Constants.JS_FILES+questionObj.getString("question");
                                questionModel.q_class_id=q_class_id;
                                questionModel.subject_id=questionObj.getInt("subject_id");
                                // questionModel.question=question;
                                questionModel.question_image=questionObj.getString("question_image");
                                questionModel.question_status=questionObj.getString("question_status");
                                questionModel.question_rating=questionObj.getInt("question_rating");
                                questionModel.question_asked_time=questionObj.getString("question_asked_time");
                                questionModel.question_view_count=questionObj.getInt("question_view_count");
                                try{

                                    questionModel.question_marks = questionObj.getInt("question_marks");
                                    questionModel.question_board = questionObj.getString("question_board");
                                    questionModel.question_year=questionObj.getString("question_year");

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                                listData.add(questionModel);
                                AnswerModel model;
                                JSONObject jsonObject;
                                for (int k = 0; k < answers.length(); k++) {

                                    jsonObject = answers.getJSONObject(k);

                                    model = new AnswerModel();
                                    model.question_id = jsonObject.getString("question_id");
                                    model.answer_image = jsonObject.getString("answer_image");
                                    model.answer_id = jsonObject.getString("answer_id");
                               //     model.answer = Constants.JS_FILES + jsonObject.getString("answer").replaceAll("<img src=\"", "<img src=\"https://www.tutorix.com");
                                    model.answer = Constants.JS_FILES + jsonObject.getString("answer");
                                    if(!model.answer_image.isEmpty())
                                        model.answer=model.answer+model.answer_image;
                                    model.answer_user_name = jsonObject.getString("full_name");
                                    model.user_photo = jsonObject.getString("user_photo");
                                    model.answer_user_id = jsonObject.getString("user_id");
                                    // model.answer_image = jsonObject.getString("answer_image");
                                    model.answer_status = jsonObject.getString("answer_status");
                                    model.answer_given_time = jsonObject.getString("answer_time");
                                    model.answer_like_count = jsonObject.getInt("answer_like_count");
                                    model.user_like_answer = jsonObject.getInt("user_like_answer");
                                    listData.add(model);
                                }
                                //if ((listData.size() > 1 && question_status.equals("O")) || (!question_status.equals("C") && listData.size() > 1))
                                //   if(!own_doubt&&!q_user_id.equalsIgnoreCase(userId))
                                //  listData.add(new AnswerModel());
                                adapter.addData(listData);




                            } else {

                                CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));


                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

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
                CommonUtils.showToast(getApplicationContext(), msg);

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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}