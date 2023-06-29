package com.tutorix.tutorialspoint.doubts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.tutorix.tutorialspoint.cropper.CropImage;
import com.tutorix.tutorialspoint.cropper.CropImageView;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.activities.ImagePreviewActivity;
import com.tutorix.tutorialspoint.adapters.DoubtDetailsAdapter;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.AnswerModel;
import com.tutorix.tutorialspoint.models.QuestionModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.PermissionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoubtsViewActivity extends AppCompatActivity {

    @BindView(R.id.recycler_doubtslist)
    RecyclerView recyclerDoubtslist;
    DoubtDetailsAdapter adapter;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;

    @BindView(R.id.home)
    LinearLayout lnrHome;
    @BindView(R.id.delete)
    LinearLayout lnrDelete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    RelativeLayout appbar;

    List<AnswerModel> listData;
    String classId;
    String userId;
    String access_token;




    @BindView(R.id.bottom_sheet)
    LinearLayout bottom_sheet;

    BottomSheetBehavior sheetBehavior;
    boolean showFoter = false;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.edit_report)
    EditText edit_report;
    @BindView(R.id.lnr_gallery)
    LinearLayout lnrGallery;
    @BindView(R.id.lnr_camera)
    LinearLayout lnrCamera;
    @BindView(R.id.lnr_keyboard)
    LinearLayout lnrKeyboard;
    int MY_PERMISSIONS_REQUEST_DOCUMENT = 201;
    Uri mCropImageUri;
    String encodedString;
    @BindView(R.id.question_options)
    MaterialCardView questionOptions;
    String q_user_id;
    String q_user_name;
    String q_user_image;
    String q_class_id;
    String question_view_count;
    String question;
    String question_id;
    String question_status;
    int rating;
    int subject_id;
    String question_image;
    String question_asked_time;
    QuestionModel questionModel;

    @BindView(R.id.lnr_loading)
    LinearLayout lnrLoading;
    boolean own_doubt;
    @BindView(R.id.txt_submit)
    TextView txtSubmit;
    @BindView(R.id.txt_cancel)
    TextView txtCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubts_view);
        ButterKnife.bind(this);


        lnrDelete.setVisibility(View.GONE);
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userId = userInfo[0];
        classId = userInfo[4];


        Intent in = getIntent();
        q_user_id = in.getStringExtra("q_user_id");
        own_doubt = in.getBooleanExtra("own_doubt", true);
        q_user_name = in.getStringExtra("q_user_name");
        q_user_image = in.getStringExtra("q_user_image");
        //Log.v("q_user_id", "q_user_id " + q_user_id + " q_user_name " + q_user_name + " q_user_image " + q_user_image);

        question = in.getStringExtra("question");

        subject_id = in.getIntExtra("subject_id", -1);
        question_image = in.getStringExtra("question_image");
        question_id = in.getStringExtra("question_id");
        question_asked_time = in.getStringExtra("question_asked_time");
        question_status = in.getStringExtra("question_status");
        listData = new ArrayList<>();

        question = Constants.JS_FILES + question + question_image;





        questionOptions.setVisibility(View.GONE);
        if (question_status.equals("U")||question_status.equals("D")) {
            if(q_user_id.equals(userId))
            lnrDelete.setVisibility(View.VISIBLE);
            showFoter = false;
            //questionOptions.setVisibility(View.GONE);
        } else if (question_status.equals("O")) {
            if(own_doubt||q_user_id.equalsIgnoreCase(userId))
                showFoter = false;
            else {
                //Uncomment when required to enable answer
                //showFoter = true;
            }
            //questionOptions.setVisibility(View.VISIBLE);

        } else if (question_status.equals("C")) {
            showFoter = false;
            // questionOptions.setVisibility(View.GONE);
        }

        /*if (showFoter) {
            if (userId.equalsIgnoreCase(q_user_id))
                showFoter = true;
            else showFoter = false;
        }*/


        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        // sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        initData();


        getNswers();


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void initData() {
        adapter = new DoubtDetailsAdapter(userId, DoubtsViewActivity.this, showFoter,own_doubt);
        recyclerDoubtslist.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerDoubtslist.setAdapter(adapter);
        addData();
    }

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        finish();


    }

    private void addData() {
        adapter.addData(listData);
    }

    Bundle bundle;
    Intent i;

    @OnClick({ R.id.txt_submit,  R.id.txt_cancel,R.id.home,R.id.delete, R.id.img_back, R.id.lnr_gallery, R.id.lnr_camera, R.id.lnr_keyboard})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.txt_q_image:
                Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                intent.putExtra("uri", Constants.IMAGE_REQUAET_URL + userId + "/" + question_image);
                startActivity(intent);
                AppController.getInstance().playAudio(R.raw.button_click);
                break;
            case R.id.txt_submit:
                report_message=edit_report.getText().toString().trim();
                if(report_message.isEmpty())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please write your message");
                    return;
                }
                sendReport();
                break;
            case R.id.txt_cancel:
                edit_report.setText("");
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //Log.v("Expand","XXXXXX Collapse");
                }
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.home:
                Intent in = new Intent(DoubtsViewActivity.this, HomeTabActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;
                case R.id.delete:
                deleteMyDoubt();

                break;
            case R.id.lnr_gallery:
                CropImage.startPickImageActivity(DoubtsViewActivity.this);
                break;
            case R.id.lnr_camera:
                i = new Intent(DoubtsViewActivity.this, AskDoubtMainActivity.class);
                bundle = new Bundle();
                bundle.putString("subject_id", subject_id + "");
                bundle.putString("question_id", question_id + "");
                bundle.putBoolean("isReply", true);
                i.putExtra(Constants.intentType, bundle);
                startActivityForResult(i, 200);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.qz_next);
                AppController.getInstance().startItemAnimation(v);
                break;
            case R.id.lnr_keyboard:
                i = new Intent(getApplicationContext(), AskTextQuestionActivity.class);
                bundle = new Bundle();
                bundle.putString("question_id", question_id);
                bundle.putBoolean("isReply", true);
                bundle.putString(Constants.subjectId, subject_id + "");
                i.putExtra(Constants.intentType, bundle);
                startActivityForResult(i, 200);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.qz_next);
                AppController.getInstance().startItemAnimation(v);
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
        CustomDialog.showDialog(DoubtsViewActivity.this, true);
        String url = "";
        if (own_doubt)
            url = Constants.DOUBT_GET_ANSWERS;
        else url = Constants.DOUBT_GET_ANSWERS_PUBLISHED;
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
                                q_class_id = questionObj.getString("class_id");
                                question_view_count = questionObj.getString("question_view_count");
                               /* txtView.setText(question_view_count + " Views");
                                txtClass.setText(AppConfig.getClassNameDisplay(questionObj.getString("class_id")));
                                txtSubject.setText(AppConfig.getSubjectName(questionObj.getString("subject_id")));*/
                                AnswerModel questionModel=new AnswerModel();
                                //JSONObject questionObj = jObj.getJSONObject("question_array");

                                questionModel.q_user_id=questionObj.getString("user_id");
                                questionModel.q_user_name=questionObj.getString("full_name");
                                questionModel.q_user_image=questionObj.getString("user_photo");
                                questionModel.question_id=questionObj.getString("question_id");
                                questionModel.question=Constants.JS_FILES+questionObj.getString("question").replaceAll("<img src=\"","<img src=\"https://www.tutorix.com")+question_image;
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
                                    model.answer = Constants.JS_FILES + jsonObject.getString("answer").replaceAll("<img src=\"", "<img src=\"https://www.tutorix.com");
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

                                if (!question_status.equals("C") && listData.size() > 1) {
                                    //if(!q_user_id.equalsIgnoreCase(userId))
                                    //Uncomment when required to enable answer
                                        //showFoter = true;
                                    //adapter.setfooterShow(showFoter);
                                }


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
        }) {


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
    public void deleteMyDoubt() {
        final JSONObject fjson = new JSONObject();


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.question_id, question_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        CustomDialog.showDialog(DoubtsViewActivity.this, true);

        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.DOUBT_DELETE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        try {

                            //Log.v("Resposne", "Resposne " + response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));
                            if (!error) {
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));

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
        }) {


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
        questionOptions.setVisibility(View.VISIBLE);

    }


    private void sendRating() {

        final JSONObject fjson = new JSONObject();


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put("question_id", question_id);
            fjson.put("rating", rating);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(DoubtsViewActivity.this, true);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser

        if (requestCode == 2001) {
            if (resultCode > 0) {

            }
            return;
        }
        if (requestCode == 200 && resultCode == 200) {
            try {
                if (listData != null)
                    listData.clear();
                adapter.clear();
                //adapter = new DoubtDetailsAdapter(userId, DoubtsViewActivity.this, showFoter);
                //recyclerDoubtslist.setAdapter(adapter);
                getNswers();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(DoubtsViewActivity.this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(DoubtsViewActivity.this, imageUri)) {
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //  Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();

                try {
                    //questionPicLayout.setVisibility(View.VISIBLE);
                    //ivQuestionPic.setImageURI(result.getUri());
                    iStream = getContentResolver().openInputStream(result.getUri());
                    long fileSize = iStream.available();

                    if (fileSize / (1000 * 1000) > 1) {
                        CommonUtils.showToast(getApplicationContext(), "Image size too big");
                        return;
                    }
                    //Log.v("InputSream","InputSream "+iStream.available());

                    DoubtImagePick lecturDescFragment =
                            DoubtImagePick.newInstance();
                    lecturDescFragment.show(getSupportFragmentManager(),
                            "add_photo_dialog_fragment");
                    lnrLoading.setVisibility(View.VISIBLE);
                    if (true) {
                        return;
                    }
                    //byte[] fileData = getBytes(iStream);
                    //encodedString = Base64.encodeToString(fileData, Base64.DEFAULT);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(DoubtsViewActivity.this);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void hideKeyboard() {
        try {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null && getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow((getCurrentFocus()).getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void permisionCheckHere(final int requestCode) {
        if (ContextCompat.checkSelfPermission(DoubtsViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(DoubtsViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(DoubtsViewActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(DoubtsViewActivity.this)
                        .setTitle("Permission required")
                        .setMessage("Permissions are required for this application to work ! ")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, requestCode);
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

            } else {
                CommonUtils.showToast(getApplicationContext(), "Go to settings>Enable permission to open the application.");
                //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, requestCode);
            }
        }
    }

    public static Uri getCaptureImageOutputUri(@NonNull Context context) {
        Uri outputFileUri = null;

        File getImage = Environment.getExternalStorageDirectory();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath()));
        }
        return outputFileUri;
    }

    private void loadImage() {
        CropImage.startPickImageActivity(DoubtsViewActivity.this);
    }

    public InputStream iStream;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2001) {
            if (PermissionRequest.selfPermissionGranted(DoubtsViewActivity.this, Manifest.permission.CAMERA) && PermissionRequest.selfPermissionGranted(DoubtsViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && PermissionRequest.selfPermissionGranted(DoubtsViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Uri outputFileUri = getCaptureImageOutputUri(getApplicationContext());

                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (outputFileUri != null) {
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }

                startActivityForResult(captureIntent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
            }
        } else if (PermissionRequest.selfPermissionGranted(DoubtsViewActivity.this, Manifest.permission.CAMERA) && PermissionRequest.selfPermissionGranted(DoubtsViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && PermissionRequest.selfPermissionGranted(DoubtsViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadImage();
        } else {
            permisionCheckHere(MY_PERMISSIONS_REQUEST_DOCUMENT);
            // ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_DOCUMENT);
        }
    }

    public String convert(Bitmap bitmap) {

        try {
            //CustomDialog.showDialog(DoubtsViewActivity.this,true);

            postReplyQuestionImg(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //CustomDialog.closeDialog();
        return "";
    }

    public void setAnswerLike(String answer_id, int position) {
        final JSONObject fjson = new JSONObject();


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.question_class_id, q_class_id);
            fjson.put(Constants.subjectId, subject_id);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.answer_id, answer_id);
            fjson.put(Constants.question_id, question_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);

        strReq = new StringRequest(Request.Method.POST, Constants.DOUBT_SET_LIKE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        try {

                            //Log.v("Resposne", "Resposne " + response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);

                            if (!error) {
                                adapter.updateLikeStatus(position, jObj.getInt("like_count"));

                            } else {

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


            }
        }) {


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

    String report_message;

    String answer_id;
    public void setReport(String answer_id) {
      this.answer_id=answer_id;

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //Log.v("Expand","XXXXXX Collapse");

    }
    public void sendReport() {
        final JSONObject fjson = new JSONObject();


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.report_message, report_message);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.answer_id, answer_id);
            fjson.put(Constants.question_id, question_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);

        strReq = new StringRequest(Request.Method.POST, Constants.DOUBT_REPORT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        try {
                            edit_report.setText("");
                            txtCancel.performClick();
                            //Log.v("Resposne", "Resposne 12 " + response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);

                            if (!error) {


                            } else {

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


            }
        }) {


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
    StringRequest strReq;
    private void postReplyQuestionImg(Bitmap bitmap) {
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        encodedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, subject_id);
            fjson.put(Constants.question_id, question_id);
            fjson.put(Constants.classId, classId);
            if (!encodedString.isEmpty())
                encodedString = "data:image/jpeg;base64," + encodedString;
            fjson.put("base64_answer_image", encodedString);
            fjson.put("reply_text", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        if (this == null)
            return;


         strReq = new StringRequest(Request.Method.POST,
                Constants.DOUBT_REPLY_NEW,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.v("Response","Response "+response);
                        if (this == null)
                            return;
                        lnrLoading.setVisibility(View.GONE);
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                CommonUtils.showToast(getApplicationContext(), "Noted your question!");
                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                setResult(Activity.RESULT_CANCELED);

                            }
                            finish();
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

    public void disMissDialog() {
        if(lnrLoading.getVisibility()==View.VISIBLE)
        {
            lnrLoading.setVisibility(View.GONE);
            return;
        }
    }

    public void callPublishDoubts(String s) {
        Intent i = new Intent(this, DoubtsViewActivity.class);
        i.putExtra("q_user_id", q_user_id);
        i.putExtra("q_user_name", q_user_name);
        i.putExtra("q_user_image", q_user_image);
        i.putExtra("question", question);
        i.putExtra("question_id", s);
        i.putExtra("question_image", question_image);
        i.putExtra("subject_id", subject_id);
        i.putExtra("question_status", "O");
       // i.putExtra("question_asked_time", listDoubts.get(position).questionAskedTime);
          startActivityForResult(i, 100);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}

