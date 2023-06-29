package com.tutorix.tutorialspoint;

import android.content.Context;
import android.content.Intent;

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
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.models.RecomandedModel;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class SyncService extends JobIntentService {
    static int JOB_ID=1000;
     String[] userInfo;
     String userId,classId;
     MyDatabase hadnler;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SyncService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        userInfo= SessionManager.getUserInfo(getApplicationContext());
        userId=userInfo[0];
        classId=userInfo[4];
            if(userId.isEmpty())
            return;
        syncData();
        //syncBookmarks();

    }

    private void syncData()
    {
        hadnler=MyDatabase.getDatabase(getApplicationContext());
        TrackModel trackData=  hadnler.trackDAO().getUnSyncTrackingDetails(userId,classId);

        if(trackData!=null) {
            if (!trackData.activity_type.equalsIgnoreCase("Q")||!trackData.activity_type.equalsIgnoreCase("M"))
                callService(trackData);
            else {
                hadnler.trackDAO().updateTrackSyncStatus("Y", trackData.id + "");
                syncData();
            }

        }
        else syncBookmarks();

    }
    private void syncBookmarks()
    {
        hadnler=MyDatabase.getDatabase(getApplicationContext());
        SubChapters chapters= hadnler.subjectChapterDAO().getBookSync(userId,classId);
        if(chapters!=null)
            action(chapters);
        else syncQuiz();
    }
    private void callService(final TrackModel trackData)
    {



        final JSONObject fjson = new JSONObject();
        try {

            String userid=userInfo[0];
            String access_token=userInfo[1];
            fjson.put(Constants.userId, userid);
            fjson.put("activity_type", trackData.activity_type);
            fjson.put("subject_id", trackData.subject_id);
            fjson.put("activity_duration", trackData.activity_duration);
            fjson.put("lecture_id", trackData.lecture_id);
            fjson.put("section_id", trackData.section_id);
            fjson.put("created_dtm", trackData.activity_date);
            fjson.put(Constants.classId, trackData.class_id);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.lectureName, trackData.lecture_name);

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
                         hadnler=MyDatabase.getDatabase(getApplicationContext());
                        hadnler.trackDAO(). updateTrackSyncStatus("Y",trackData.id+"");
                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();

                        syncData();
                    } else {
                        String errorMsg = jObj.getString(Constants.message);
                        CommonUtils.showToast(getApplicationContext(),errorMsg);
                        // Toasty.warning(VideoActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                    // Toasty.warning(VideoActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(getApplicationContext(),getString(R.string.there_is_error));
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
            if(data.lecture_notes!=null&&data.lecture_notes.length()>0)
            fjson.put(Constants.lectureNotes, data.lecture_notes);
            fjson.put(Constants.lectureVideoThumb, data.lecture_video_thumb);
            fjson.put(Constants.lectureSRTUrl, data.video_srt);
            fjson.put(Constants.completedFlag, (data.lecture_completed)?"Y":"N");
            fjson.put(Constants.bookmarkFlag, (data.lecture_bookmark)?"Y":"N");
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
                        try {
                            JSONObject jObj = new JSONObject(response);
                            //Log.d(Constants.response, response);


                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            hadnler=MyDatabase.getDatabase(getApplicationContext());
                            hadnler.subjectChapterDAO().updateBookmarkSyncStatus(1,data.id+"");
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
                //Toasty.warning(context, error.getMessage(), Toast.LENGTH_SHORT, true).show();

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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void syncQuiz()
    {
        hadnler=MyDatabase.getDatabase(getApplicationContext());
        QuizModel revieR= hadnler.quizModelDAO().getQuizDetailsSync(userId,classId);


        if(revieR!=null)
        {
            SubmitToServer(revieR);
        }else
        {
            syncRecomanded();
        }

    }

    private void syncRecomanded() {
        hadnler = MyDatabase.getDatabase(getApplicationContext());
        RecomandedModel mocktable = hadnler.recomandedDAO().getRecomanded(userId, classId);
        if (mocktable != null) {
            SubmitToServerRecomanded(mocktable);
        }
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
            fjson.put(Constants.accessToken, userInfo[1]);
            fjson.put(Constants.lectureName, quizeR.lecture_name);
            fjson.put(Constants.sectionName, quizeR.section_name);
            fjson.put(Constants.mockTest, quizeR.mock_test);

            fjson.put("total_questions", quizeR.total+"");
            fjson.put("attempted_questions", quizeR.attempted_questions);
            fjson.put("created_dtm", quizeR.QuizCreatedDtm);
            fjson.put(Constants.lectureDuration, "00:00:00");



            //JSONObject questionsObj = new JSONObject();
            //questionsObj.put("questions", quizeR.question);


           // quizObj.put("quiz", questionsObj);


            fjson.put("quiz_json", (quizeR.question));



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


                            JSONObject jObj = new JSONObject(response);


                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                 hadnler=MyDatabase.getDatabase(getApplicationContext());
                                hadnler.quizModelDAO(). updateQuizSyncStatus("Y",quizeR._id+"");

                                syncQuiz();
                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
                                // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                            // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                CommonUtils.showToast(getApplicationContext(),getString(R.string.there_is_error));
                // Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();

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

                        try {

                        //Log.v("inserted","inserted "+response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                hadnler = MyDatabase.getDatabase(getApplicationContext());
                                hadnler.recomandedDAO().updateMockTestSyncStatus("Y", quizeR._id + "");
                                //Log.v("inserted","inserted 23 "+response);
                                syncRecomanded();
                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                               // CommonUtils.showToast(getApplicationContext(), errorMsg);
                                // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
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

}
