package com.tutorix.tutorialspoint.doubts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.activities.ImagePreviewActivity;
import com.tutorix.tutorialspoint.adapters.DoubtDetailsAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.AnswerModel;
import com.tutorix.tutorialspoint.models.DoubtModel;
import com.tutorix.tutorialspoint.models.UserProfile;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoubtDetailsActivity extends AppCompatActivity {


    @BindView(R.id.recycler_doubtslist)
    RecyclerView recyclerDoubtslist;
    DoubtDetailsAdapter adapter;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.home)
    ImageView home;
    @BindView(R.id.lnr_home)
    LinearLayout lnrHome;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    RelativeLayout appbar;
    String question;
    String question_id;
    String question_status;
    int rating;
    int subject_id;
    String question_image;
    String question_asked_time;
    List<AnswerModel> listData;
    String classId;
    String userId;
    String access_token ;
    @BindView(R.id.txt_q_usr_name)
    TextView txtQUsrName;
    @BindView(R.id.txt_q_time)
    TextView txtQTime;
    @BindView(R.id.txt_q)
    WebView txtQ;
    @BindView(R.id.txt_q_image)
    TextView txtQImage;
    @BindView(R.id.img_qtn_img)
    ImageView img_qtn_img;

    @BindView(R.id.img_rating_smily)
    ImageView img_rating_smily;
    @BindView(R.id.txt_submit)
    TextView txt_submit;
    @BindView(R.id.lnr_notanswer)
    LinearLayout lnr_notanswer;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottom_sheet;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    BottomSheetBehavior sheetBehavior;
    boolean showFoter=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_details);
        ButterKnife.bind(this);


        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userId = userInfo[0];
        classId = userInfo[4];


        Intent in = getIntent();
        question = in.getStringExtra("question");
        subject_id = in.getIntExtra("subject_id", -1);
        question_image = in.getStringExtra("question_image");
        question_id = in.getStringExtra("question_id");
        question_asked_time = in.getStringExtra("question_asked_time");
        question_status = in.getStringExtra("question_status");
        listData = new ArrayList<>();
        DoubtModel model = new DoubtModel();
        model.question_image = question_image;
        model.question = question;
        model.subject_id = subject_id;
        model.question_id = question_id;

        //question=ss+ Html.fromHtml((String) question).toString().replaceAll("<img src=\"","<img src=\"https://www.tutorix.com");
        question=Constants.JS_FILES+ question.replaceAll("<img src=\"","<img src=\"https://www.tutorix.com");

        txtQ.loadData(question,"text/html",null);

        //txtQ.setText(question);
        txt_submit.setEnabled(false);

        if (question_status.equals("U")) {

            showFoter=false;
        } else  if (question_status.equals("O")) {
            showFoter=true;

        }else  if (question_status.equals("C")) {
            showFoter=false;
        }


        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //Log.v("XX","XXXXXX "+newState);

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ratingbar.setStepSize(1);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float ratings, boolean fromUser) {
                txt_submit.setEnabled(true);
                AppController.getInstance().playAudio(R.raw.button_click);
                switch ((int)ratings) {
                    case 2:
                        rating=2;
                        img_rating_smily.setImageResource(R.drawable.rate_2);
                        break;
                    case 4:
                        Log.i("Smile", "Good");
                        rating=4;
                        img_rating_smily.setImageResource(R.drawable.rate_4);
                        break;
                    case 5:
                        Log.i("Smile", "Great");
                        rating=5;
                        img_rating_smily.setImageResource(R.drawable.rate_5);
                        break;
                    case 3:
                        Log.i("Smile", "Okay");
                        rating=3;
                        img_rating_smily.setImageResource(R.drawable.rate_3);
                        break;
                    case 1:
                        Log.i("Smile", "Terrible");
                        rating=1;
                        img_rating_smily.setImageResource(R.drawable.rate_1);
                        break;
                    case 0:
                        Log.i("Smile", "None");
                        img_rating_smily.setImageResource(R.drawable.rate_1);
                        break;
                }
            }
        });

        if(question_image!=null&&question_image.length()>3)
            txtQImage.setVisibility(View.VISIBLE);
        else txtQImage.setVisibility(View.GONE);

        switch (subject_id)
        {
            case 1:
                img_qtn_img.setImageResource(R.drawable.physics_quiz);
                break;
            case 2:
                img_qtn_img.setImageResource(R.drawable.chemistry_quiz);
                break;
            case 3:
                img_qtn_img.setImageResource(R.drawable.biolog_quiz);
                break;
            case 4:
                img_qtn_img.setImageResource(R.drawable.math_quiz);
                break;
        }


        initData();

        try {
           // if (!question_status.equals("U")) {

                getNswers();
           /* }else
            {
                lnr_notanswer.setVisibility(View.VISIBLE);
            }*/

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
       UserProfile profile= MyDatabase.getDatabase(getApplicationContext()).userDAO().getUserProfile(userId);
        String name = profile.full_name;
        txtQUsrName.setText(name);
        txtQTime.setText(CommonUtils.getstringDate(question_asked_time));

    }

    private void initData() {
        adapter = new DoubtDetailsAdapter(userId,DoubtDetailsActivity.this,showFoter,true);
        recyclerDoubtslist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerDoubtslist.setAdapter(adapter);
        addData();
    }

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        finish();
        //overridePendingTransition(R.anim.left_out, R.anim.right_in);

    }

    private void addData() {
        adapter.addData(listData);
    }

    @OnClick({R.id.txt_q_image,R.id.txt_submit,R.id.lnr_home})
    public void onViewClicked(View v) {
        switch (v.getId())
        {
            case R.id.txt_q_image:
                Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                intent.putExtra("uri", Constants.IMAGE_REQUAET_URL + userId + "/" + question_image);
                startActivity(intent);
                AppController.getInstance().playAudio(R.raw.button_click);
                break;
            case R.id.txt_submit:
                AppController.getInstance().playAudio(R.raw.button_click);
                if(rating<=0)
                {
                  CommonUtils.showToast(getApplicationContext(),"Please share your rating...");
                }else
                {
                    sendRating();
                }
                break;
            case R.id.lnr_home:
                Intent in = new Intent(DoubtDetailsActivity.this, HomeTabActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;
        }


    }

    public void getNswers() throws UnsupportedEncodingException {
        final JSONObject fjson = new JSONObject();



        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.subjectId, subject_id);
            fjson.put(Constants.userId, userId);
            fjson.put("offset", 0);
            fjson.put("limit", 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        CustomDialog.showDialog(DoubtDetailsActivity.this, true);
        StringRequest strReq = new StringRequest(Request.Method.GET, Constants.GET_DOUBT_DETAILS  + question_id + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                new Response.Listener<String>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            //Log.v("Resposne","Resposne "+jObj.toString());
                            boolean error = jObj.getBoolean(Constants.errorFlag);

                            if (!error) {

                                JSONArray answers=jObj.getJSONArray("answers");
                                AnswerModel model;
                                JSONObject jsonObject;
                                for(int k=0;k<answers.length();k++)
                                {

                                    jsonObject=answers.getJSONObject(k);
                                    model=new AnswerModel();
                                    model.answer_image=jsonObject.getString("answer_image");
                                    model.answer=jsonObject.getString("answer");
                                    model.answer_user_name=jsonObject.getString("answer_user_name");
                                    model.answer_user_id=jsonObject.getString("answer_user_id");
                                    model.answer_image=jsonObject.getString("answer_image");
                                    model.answer_given_time=jsonObject.getString("answer_given_time");
                                    listData.add(model);
                                }
                                if((listData.size()>0&&question_status.equals("O"))||(!question_status.equals("C")&&listData.size()>0))
                                    listData.add(new AnswerModel());
                                adapter.addData(listData);

                                if(!question_status.equals("C")&&listData.size()>0)
                                {
                                    showFoter=true;
                                    adapter.setfooterShow(showFoter);
                                }

                                if(adapter.getItemCount()>0)
                                    lnr_notanswer.setVisibility(View.GONE);
                                else  lnr_notanswer.setVisibility(View.VISIBLE);
                            } else {
                                lnr_notanswer.setVisibility(View.VISIBLE);
                                //CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));


                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

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

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void closeDoubt() {
        //Log.v("Expand","XXXXXX y "+sheetBehavior.getState());
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
           // btnBottomSheet.setText("Close sheet");

            //Log.v("Expand","XXXXXX Expand");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
           // btnBottomSheet.setText("Expand sheet");
            //Log.v("Expand","XXXXXX Collapse"+sheetBehavior.getState());
        }
    }

    @Override
    public void onBackPressed() {

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //Log.v("Expand","XXXXXX Collapse");
        } else {

            super.onBackPressed();
        }
    }

    public void replayDoubt() {
        Intent in=new Intent(getApplicationContext(),AskQuestionActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("question_id",question_id);
        bundle.putString(Constants.subjectId, subject_id+"");
        in.putExtra(Constants.intentType, bundle);

        startActivityForResult(in,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200&&resultCode==200)
        {
            try {
                if(listData!=null)
                listData.clear();
                adapter.clear();
                adapter=new DoubtDetailsAdapter(userId,DoubtDetailsActivity.this,showFoter,true);
                recyclerDoubtslist.setAdapter(adapter);
                getNswers();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendRating()
    {

        final JSONObject fjson = new JSONObject();


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put("question_id", question_id);
            fjson.put("rating", rating );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(DoubtDetailsActivity.this,true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.CLOSE_DOUBT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.v("Response","Response "+response);
                        CustomDialog.closeDialog();
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                //CommonUtils.showToast(getApplicationContext(), "Thank you for raising your doubt, Our faculty will check and respond it soon...");
                                setResult(Activity.RESULT_OK);
                                   finish();

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);

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
                CustomDialog.closeDialog();
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
}
