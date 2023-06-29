package com.tutorix.tutorialspoint.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.fragments.MenuFragment;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.models.RecomandedModel;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ActivationDetailsActivity extends AppCompatActivity {
    MyDatabase hadnler;
    String userInfo[];
    String userId;
    String classId;
    public static MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activation Details");

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        setContentView(R.layout.activity_activation_details);
        userInfo = SessionManager.getUserInfo(getApplicationContext());
        userId = userInfo[0];
        classId = userInfo[4];
        initUI();
    }

    private void initUI() {
        TextView txt_class = findViewById(R.id.txt_class);
        TextView txt_activation_date = findViewById(R.id.txt_activation_date);
        TextView txt_start_date = findViewById(R.id.txt_start_date);
        TextView txt_expiry_date = findViewById(R.id.txt_expiry_date);
        TextView txt_activation_type = findViewById(R.id.txt_activation_type);
        TextView txt_deactivate = findViewById(R.id.txt_deactivate);
        TextView txt_activation_key = findViewById(R.id.txt_activation_key);
        TextView txt_expierymsg = findViewById(R.id.txt_expierymsg);
        View lnr_expired = findViewById(R.id.lnr_expired);

        lnr_expired.setVisibility(View.GONE);
        String s=new SharedPref().getActivationStatus(getApplicationContext());
        if (new SharedPref().isExpired(getApplicationContext())||s.equalsIgnoreCase("T")) {
            if(s.equalsIgnoreCase("T"))
            {
                txt_expierymsg.setText("Your free trial expires in the next "+AppConfig.ACTIVATION_EXPIERY_DAYS+" days. You can upgrade to one of our full plans anytime during your trial");
            }
            lnr_expired.setVisibility(View.VISIBLE);

        }
        lnr_expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SubscribeActivity.class);
                i.putExtra("flag", "M");
                startActivity(i);
            }
        });
        txt_activation_key.setSelected(true);

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        String user_id = userInfo[0];
        String calssId = userInfo[4];
        txt_class.setText(AppConfig.getClassNameDisplay(classId));
       /* switch (calssId) {
            case "1":
                txt_class.setText("6th Class");
                break;
            case "2":
                txt_class.setText("7th Class");
                break;
            case "3":
                txt_class.setText("8th Class");
                break;
            case "4":
                txt_class.setText("9th Class");
                break;


        }*/

        hadnler = MyDatabase.getDatabase(getApplicationContext());
        ActivationDetails aDetails = hadnler.activationDAO().getActivationDetails(user_id, calssId);

        if (aDetails != null) {
            txt_activation_date.setText(aDetails.activation_start_date);
            txt_expiry_date.setText(aDetails.activation_end_date);
            txt_activation_key.setText(aDetails.activation_key);
            String type = aDetails.activation_type;

            SDActivationDetails sdActivationDetails= hadnler.sdActivationDAO().getActivationDetails(userId,classId);
            if(sdActivationDetails!=null&&!sdActivationDetails.activation_key.isEmpty())
            {
                txt_activation_type.setText("Offline Streaming");
                txt_deactivate.setVisibility(View.VISIBLE);
            }else
            {
                txt_activation_type.setText("Online Streaming");
                txt_deactivate.setVisibility(View.GONE);
            }
            /*if (type.equals("O")) {
                txt_activation_type.setText("Online Streaming");
                txt_deactivate.setVisibility(View.GONE);
            } else {
                txt_activation_type.setText("Offline Streaming");
                txt_deactivate.setVisibility(View.VISIBLE);
            }*/

        }

    }

    Dialog dialog;
    Dialog dialogSync;

    public void deActivate(View view) {

        if (!AppStatus.getInstance(ActivationDetailsActivity.this).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }
        dialog = new Dialog(ActivationDetailsActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert);
        TextView txt_msg = dialog.findViewById(R.id.txt_msg);
        txt_msg.setText(getString(R.string.areyou_sure_deactivate));
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
        txt_ok.setText(getString(R.string.yes));
        txt_cancel.setText(getString(R.string.no));
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                dialogSync = new Dialog(ActivationDetailsActivity.this);
                dialogSync.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogSync.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogSync.setContentView(R.layout.dialog_alert_password);
                final EditText edit_password = dialogSync.findViewById(R.id.edit_password);
                txt_msg.setText("Sync your data to Remote ?");
                TextView txt_cancel = dialogSync.findViewById(R.id.txt_cancel);
                TextView txt_ok = dialogSync.findViewById(R.id.txt_ok);
                txt_ok.setText(getString(R.string.contin));
                txt_cancel.setText(getString(R.string.cancel));
                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSync.dismiss();
                    }
                });
                txt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String password=edit_password.getText().toString().trim();
                        if(password.length()<=0)
                        {
                            CommonUtils.showToast(getApplicationContext(),"Please enter password");
                            return;
                        }
                        MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
                        UserProfile profile=db.userDAO().getUserProfile(userId);
                        if(!password.equals(profile.password))
                        {
                            CommonUtils.showToast(getApplicationContext(),"Please enter correct password");
                            return;
                        }

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                syncData();
                            }
                        });


                    }
                });
                dialogSync.show();

            }
        });
        dialog.show();
    }

    public void Renewal(View view) {
        dialog = new Dialog(ActivationDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert);
        TextView txt_msg = dialog.findViewById(R.id.txt_msg);
        txt_msg.setText(getString(R.string.aryou_sure_renuwal));
        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
        txt_ok.setText(getString(R.string.contin));
        txt_cancel.setText(getString(R.string.cancel));
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
        return false;
    }


    private void syncData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                hadnler = MyDatabase.getDatabase(getApplicationContext());
                TrackModel trackData = hadnler.trackDAO().getUnSyncTrackingDetails(userId, classId);

                if (trackData != null) {
                    if (!trackData.activity_type.equalsIgnoreCase("Q")||!trackData.activity_type.equalsIgnoreCase("M"))
                        callService(trackData);
                    else {
                        hadnler.trackDAO().updateTrackSyncStatus("Y", trackData.id + "");
                        syncData();
                    }

                } else {
                    syncBookmarks();
                }
            }
        });


    }

    private void syncBookmarks() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        SubChapters chapters = hadnler.subjectChapterDAO().getBookSync(userId, classId);
        if (chapters != null)
            action(chapters);
        else {
            syncQuiz();

        }
    }

    private void syncQuiz() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        QuizModel revieR = hadnler.quizModelDAO().getQuizDetailsSync(userId, classId);
        if (revieR != null) {
            SubmitToServer(revieR);
        } else {
            //clearUserData();
            syncRecomanded();

        }
    }

    private void syncRecomanded() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        RecomandedModel mocktable = hadnler.recomandedDAO().getRecomanded(userId, classId);
        if (mocktable != null) {
            SubmitToServerRecomanded(mocktable);
        } else {
            clearUserData();
            //callDeactivation();
        }
    }
    private void callService(final TrackModel trackData) {



        final JSONObject fjson = new JSONObject();
        try {
            String userid = userInfo[0];
            String access_token = userInfo[1];
            fjson.put(Constants.userId, userid);
            fjson.put("activity_type", trackData.activity_type);
            fjson.put("subject_id", trackData.subject_id);
            fjson.put("activity_duration", trackData.activity_duration);
            fjson.put("lecture_id", trackData.lecture_id);
            fjson.put("section_id", trackData.section_id);
            fjson.put("created_dtm", trackData.activity_date);
            fjson.put(Constants.classId, trackData.class_id);
            fjson.put(Constants.lectureName, trackData.lecture_name);
            fjson.put(Constants.accessToken, access_token);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.BASE_URL + "users/track", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);
                CustomDialog.closeDialog();
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
                        // Toasty.warning(VideoActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    // Toasty.warning(VideoActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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
                //Toasty.warning(VideoActivity.this, "There is something error", Toast.LENGTH_SHORT, true).show();
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
        CustomDialog.closeDialog();
        CustomDialog.showDialog(ActivationDetailsActivity.this, false);
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
            fjson.put(Constants.lectureVideoThumb, data.lecture_video_thumb);
            fjson.put(Constants.lectureSRTUrl, data.video_srt);

            fjson.put(Constants.completedFlag, (data.lecture_completed) ? "Y" : "N");
            fjson.put(Constants.bookmarkFlag, (data.lecture_bookmark) ? "Y" : "N");
            //Log.v("Notes","Notes "+data.lecture_notes);
            if(data.lecture_notes!=null&&data.lecture_notes.length()>0)
                fjson.put(Constants.lectureNotes, data.lecture_notes);

            fjson.put(Constants.accessToken, userInfo[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.LECTURE_ACTIONS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            //Log.d(Constants.response, response);


                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            hadnler =MyDatabase.getDatabase(getApplicationContext());
                            hadnler.subjectChapterDAO().updateBookmarkSyncStatus(1, data.id+"");
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
        CustomDialog.showDialog(ActivationDetailsActivity.this, true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void SubmitToServer(final QuizModel quizeR) {


        final JSONObject fjson = new JSONObject();
       // JSONObject quizObj = new JSONObject();
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
            fjson.put(Constants.accessToken, userInfo[1]);
            fjson.put(Constants.lectureName, quizeR.lecture_name);
            fjson.put("total_questions", quizeR.total + "");
            fjson.put("attempted_questions", quizeR.attempted_questions);
            fjson.put("created_dtm", quizeR.QuizCreatedDtm);
            fjson.put(Constants.lectureDuration, "00:00:00");

            fjson.put(Constants.sectionName, quizeR.section_name);
            fjson.put(Constants.mockTest, quizeR.mock_test);

            //JSONObject questionsObj = new JSONObject();
           // questionsObj.put("questions", quizeR.question);


            //quizObj.put("quiz", questionsObj);


            fjson.put("quiz_json", quizeR.question);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.USER_QUIZ_COMPLETED,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        try {


                            JSONObject jObj = new JSONObject(response);


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
                // Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();
                finish();
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
        CustomDialog.closeDialog();
        CustomDialog.showDialog(ActivationDetailsActivity.this, true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


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
            fjson.put(Constants.accessToken, userInfo[1]);
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
                        CustomDialog.closeDialog();
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
                CommonUtils.showToast(getApplicationContext(), msg);
                // Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();
                finish();
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
        CustomDialog.closeDialog();
        CustomDialog.showDialog(ActivationDetailsActivity.this, true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    private void clearUserData() {

        try{
            dialogSync.dismiss();
        }catch (Exception e)
        {

        }
        callDeactivation();


    }


    private void callDeactivation() {
        final JSONObject fjson = new JSONObject();

        try {
            String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            fjson.put(Constants.userId, userInfo[0]);
            fjson.put(Constants.classId, userInfo[4]);
            fjson.put(Constants.accessToken, userInfo[1]);
            fjson.put("device_id", android_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.DEATCTIVATE_USER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        try {

                            JSONObject jObj = new JSONObject(response);

                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (error) {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                return;
                            }
                            //Log.d(Constants.response, "Deactivate On resposne " + response);

                            //Log.d(Constants.response, "Deactivate On Clear Data 1 ");
                            hadnler = MyDatabase.getDatabase(getApplicationContext());
                            hadnler.subjectChapterDAO().deleteBookmarkForDeactUser(userInfo[0], userInfo[4]);
                            hadnler.trackDAO().deleteTracksForDeactUser(userInfo[0], userInfo[4]);
                            hadnler.quizModelDAO().deleteQuizeDetails(userInfo[0], userInfo[4]);
                            hadnler.activationDAO().deleteActivationDetails(userInfo[0], userInfo[4]);
                            hadnler.mockTestDAO().deleteMockTest(userInfo[0], userInfo[4]);
                            hadnler.recomandedDAO().deleteMockTestRecomanded(userInfo[0], userInfo[4]);
                            hadnler.sdActivationDAO().deleteActivationDetails(userInfo[0], userInfo[4]);
                            hadnler.userDAO().deleteUser(userInfo[0]);
                            //Log.d(Constants.response, "Deactivate On Clear Data 2 ");
                            SessionManager session = new SessionManager();
                            session.logoutUser(getApplicationContext());
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            if(menuFragment!=null)
                            menuFragment.finishActivity();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toasty.warning(context, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.warning(context, error.getMessage(), Toast.LENGTH_SHORT, true).show();
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
        CustomDialog.showDialog(ActivationDetailsActivity.this, true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
