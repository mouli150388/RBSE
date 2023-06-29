package com.tutorix.tutorialspoint.doubts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.models.RelatedQuestionModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimilarDoubtActivity extends AppCompatActivity {


    @BindView(R.id.img_question)
    ImageView imgQuestion;
    @BindView(R.id.txt_question)
    TextView txtQuestion;
    @BindView(R.id.lnr_top_main)
    LinearLayout lnrTopMain;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.lnr_reload)
    LinearLayout lnrReload;
    @BindView(R.id.rvQuestions)
    RecyclerView rvQuestions;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.appbar)
    RelativeLayout appbar;
    @BindView(R.id.txt_dinot_find)
    TextView txtDinotFind;

    String subjectId = "0";
    Activity _this;
    List<RelatedQuestionModel> listDoubts;
    private static int PAGE_START = 0;
    @BindView(R.id.lnr_loading)
    LinearLayout lnrLoading;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int OFFSET = 0;
    private int LIMIT = 10;

    private int currentPage = PAGE_START;
    DoubtsRelatedAdapter mAdapter;
    String math_text;
    String base64_question_image;
    String question_text;
    String access_token;
    String userid;
    String loginType;
    String classId;
    int coin_flag=0;
    String rewardpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_doubt);
        ButterKnife.bind(this);
        _this = this;
        listDoubts = new ArrayList<>();
        String[] userInfo = SessionManager.getUserInfo(_this);

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        mAdapter = new DoubtsRelatedAdapter(_this);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvQuestions.setAdapter(mAdapter);
        SharedPreferences sh = getSharedPreferences("doubts", MODE_PRIVATE);
        String doubts_data = sh.getString("doubts_data", "");
        txtQuestion.setText("");
        if (doubts_data != null && !doubts_data.isEmpty()) {
            try {
                JSONObject obj = new JSONObject(doubts_data);
                math_text = obj.getString("math_text");
                base64_question_image = obj.getString("base64_question_image");
                question_text = obj.getString("question_text");
                txtQuestion.setText("Q) " + question_text);
                if (base64_question_image.isEmpty()) {
                    imgQuestion.setVisibility(View.GONE);
                    txtQuestion.setVisibility(View.VISIBLE);
                } else {
                    imgQuestion.setVisibility(View.VISIBLE);
                    txtQuestion.setVisibility(View.GONE);
                    try{

                        byte[] decodedString = Base64.decode(base64_question_image.replace("data:image/jpeg;base64,",""), Base64.DEFAULT);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imgQuestion.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                                    }
                                });

                            }
                        }).start();
                    }catch (OutOfMemoryError oom)
                    {

                    }catch (Exception e)
                    {

                    }



                }

                if (obj.has("question_array")) {
                    JSONArray question_array = obj.getJSONArray("question_array");
                    RelatedQuestionModel relatedQuestionModel;
                    JSONObject jObj;
                    for (int k = 0; k < question_array.length(); k++) {
                        jObj = question_array.getJSONObject(k);
                        relatedQuestionModel = new RelatedQuestionModel();
                        relatedQuestionModel.classId = jObj.getInt("class_id");
                        relatedQuestionModel.fullName = jObj.getString("full_name");
                        relatedQuestionModel.question = Constants.JS_FILES+jObj.getString("question");
                        relatedQuestionModel.questionAskedTime = jObj.getString("question_asked_time");
                        relatedQuestionModel.questionImage = jObj.getString("question_image");
                        relatedQuestionModel.questionId = jObj.getString("question_id");
                        relatedQuestionModel.questionRating = jObj.getString("question_rating");
                        relatedQuestionModel.questionStatus = jObj.getString("question_status");
                        relatedQuestionModel.subjectId = jObj.getInt("subject_id");
                        relatedQuestionModel.userPhoto = jObj.getString("user_photo");
                        relatedQuestionModel.userId = jObj.getString("user_id");
                        relatedQuestionModel.question_chapter_name=jObj.getString("question_chapter_name");
                        try{
                        relatedQuestionModel.question_board=jObj.getString("question_board");
                        relatedQuestionModel.question_year=jObj.getString("question_year");
                        relatedQuestionModel.question_marks = jObj.getInt("question_marks");

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        relatedQuestionModel.question_view_count = 0+"";
                        relatedQuestionModel.class_name = AppConfig.getClassNameDisplayClass(relatedQuestionModel.classId + "")+"th".replaceAll("-","");;
                        listDoubts.add(relatedQuestionModel);
                    }
                    mAdapter.addDoubts(listDoubts);
                    mAdapter.notifyDataSetChanged();
                }

                if(obj.has("coins_array")){
                    rewardpoint=obj.getJSONObject("coins_array").toString();
                    /*int silver_coins=coinObj.getInt("silver_coins");
                    int gold_coins=coinObj.getInt("gold_coins");
                    int doubts_total=coinObj.getInt("doubts_total");
                    int doubts_used=coinObj.getInt("doubts_used");
                    int doubts_balance=coinObj.getInt("doubts_balance");*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (mAdapter.getItemCount() > 0) {
            lnrReload.setVisibility(View.GONE);
        }


        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed

                    appbar.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    //Expanded
                    appbar.setBackgroundColor(getResources().getColor(R.color.transparent));

                }

            }
        });
        if(mAdapter.getItemCount() ==0)
        {
            if (!loginType.equals("")) {
                txtDinotFind.performClick();
            }else
            {
                //txtDinotFind.setText("Connect Our SME");
            }
            //finish();
        }


    }

    @OnClick({R.id.img_question, R.id.txt_question, R.id.img_back, R.id.txt_dinot_find})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_question:
                break;
            case R.id.txt_question:
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_dinot_find:
                if (loginType.equals("")) {
                   /* Intent i = new Intent(_this, SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    _this.startActivityForResult(i, 200);
                    finish();*/
                    RewardPointFragment rewardFragment =
                            RewardPointFragment.newInstance();
                    Bundle b=new Bundle();
                    b.putString("rewardpoint",rewardpoint);
                    rewardFragment.setArguments(b);
                    rewardFragment.show(getSupportFragmentManager(),
                            "RewardPoints");
                    return;
                }
                if (new SharedPref().isExpired(getApplicationContext())) {
                   /* Intent i = new Intent(getApplicationContext(), SubscribePrePage.class);
                    i.putExtra("flag", "D");
                    startActivity(i);
                    finish();*/
                    RewardPointFragment rewardFragment =
                            RewardPointFragment.newInstance();
                    Bundle b=new Bundle();
                    b.putString("rewardpoint",rewardpoint);
                    rewardFragment.setArguments(b);
                    rewardFragment.show(getSupportFragmentManager(),
                            "RewardPoints");
                    return;
                }
                startActivityForResult(new Intent(getApplicationContext(), DoubtSubjectActivty.class), 2001);
                break;
        }
    }


    private void populateData() {

        nodata();
    }

    private void nodata() {
        if (mAdapter != null && mAdapter.getItemCount() > 0)
            lnrReload.setVisibility(View.GONE);
        else
            lnrReload.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2001) {
            if (resultCode > 0) {
                subjectId = resultCode + "";
                AppController.QUESTION_ASKED=1;
                postQuestion();
            }
            return;
        }
    }
    StringRequest strReq;

    public void fromRewardPoints(boolean canPost)
    {
        if(canPost)
        {
            coin_flag=1;
            startActivityForResult(new Intent(getApplicationContext(), DoubtSubjectActivty.class), 2001);
        }else
        {
            Intent i = new Intent(getApplicationContext(), SubscribePrePage.class);
            i.putExtra("flag", "D");
            startActivity(i);
            finish();
        }
    }
    private void postQuestion() {
        lnrLoading.setVisibility(View.VISIBLE);
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.classId, classId);
            //if (!base64_question_image.isEmpty())
               // base64_question_image = "data:image/jpeg;base64," + base64_question_image;
            fjson.put("base64_question_image", base64_question_image);
            fjson.put("math_text", math_text);
            fjson.put("question_text", question_text);
            fjson.put("coin_flag", coin_flag+"");
            fjson.put("active_user", (loginType==null||loginType.isEmpty())?"0":"1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        if (_this == null)
            return;



         strReq = new StringRequest(Request.Method.POST,
                Constants.DOUBT_ASK_QTNS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.v("Response","Response "+response);
                        if (_this == null)
                            return;
                        lnrLoading.setVisibility(View.GONE);
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {


                                Dialog dialog = new Dialog(SimilarDoubtActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.setContentView(R.layout.dialog_alert);
                                TextView txt_msg = dialog.findViewById(R.id.txt_msg);
                                txt_msg.setText("We have noted your question. One of our SMEs will check and respond soon!");
                                TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
                                TextView txt_ok = dialog.findViewById(R.id.txt_ok);
                                txt_ok.setText("Ok");
                                txt_cancel.setVisibility(View.GONE);
                                txt_cancel.setText("Cancel");
                                txt_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AppController.getInstance().playAudio(R.raw.button_click);
                                        dialog.dismiss();
                                    }
                                });
                                txt_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        AppController.getInstance().playAudio(R.raw.button_click);
                                        setResult(Activity.RESULT_OK);
                                        finish();

                                    }
                                });
                                if (_this == null)
                                    return;

                                try {
                                    dialog.setCancelable(false);
                                    dialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //CommonUtils.showToast(getApplicationContext(), "Thank you for raising your doubt, Our faculty will check and respond it soon...");

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                setResult(Activity.RESULT_CANCELED);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), "Json error : " + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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
                lnrLoading.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(strReq!=null)
            AppController.getInstance().cancelPendingRequests(Constants.reqRegister);
    }
}
