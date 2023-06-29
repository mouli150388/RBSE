package com.tutorix.tutorialspoint.login;

import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

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
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.MockTestModelTable;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.models.RecomandedModel;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.AppStatus;
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

public class LoginPresenterImpl implements LoginPresenter {
    LoginView loginView;
    LoginActivity activity;
    String android_id;
    String country_code;
    String mobile_no;
    String password;


    public LoginPresenterImpl(LoginView loginView, LoginActivity activity) {
        this.loginView = loginView;
        this.activity = activity;
        android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public void checkValidation(String mobile_no, String password, String country_codes) {
        country_code = country_codes;
        this.mobile_no = mobile_no;
        this.password = password;
        if (mobile_no.isEmpty() || password.isEmpty()) {
            loginView.showMessage(activity.getString(R.string.fields_mandatory), 0);
            return;
        }


        MyDatabase dbHandler = MyDatabase.getDatabase(activity);
        UserProfile profile = dbHandler.userDAO().getUserProfile(mobile_no, password);
        if (AppStatus.getInstance(activity).isOnline()) {
            loginMethod(mobile_no, password);
            return;
        }
        if (profile == null) {
            if (AppStatus.getInstance(activity).isOnline()) {
                loginMethod(mobile_no, password);
            } else {
                loginView.showMessage(activity.getResources().getString(R.string.no_internet), 0);
            }
        } else {
            String[] userInfo = SessionManager.getUserInfo(activity);
            ActivationDetails activationDetails = MyDatabase.getDatabase(activity).activationDAO().getActivationDetails(profile.user_id);
            String classId = "";
            String activation_type = "";

            if (activationDetails != null) {
                classId = activationDetails.class_id;
                activation_type = activationDetails.activation_type;
            }
            String user_session[] = SessionManager.getUserInfo(activity);
            SessionManager.setLogin(activity, profile.user_id, profile.accessToken, profile.deviceId, activation_type, classId, activationDetails.secure_url, activationDetails.data_url, user_session[7]);
            loginView.navigateScreen(0);

        }


    }

    private void loginMethod(final String inputPhoneNumber, final String inputPassword) {

        Log.i(Constants.deviceId, android_id);
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.mobile_country_code, country_code);
            fjson.put(Constants.mobileNumber, inputPhoneNumber);
            fjson.put(Constants.password, inputPassword);
            fjson.put(Constants.deviceId, android_id);
            fjson.put(Constants.firebaseToken, SharedPref.getToken(activity));
            fjson.put(Constants.OS_TYPE, "A");

            // Log.v("Login Data", "Login Data " + fjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        // DialogUtils.showProgressDialog(_this);
        CustomDialog.showDialog(activity, false);

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.USER_LOGIN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();
                        //DialogUtils.dismissProgressDialog();

                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                int error_code = jObj.getInt("error_code");
                                if (!error) {
                                    Log.d("jObj", "jObj " + jObj.toString());
                                    String userId = jObj.getString(Constants.userId);
                                    String userName = jObj.getString(Constants.userName);
                                    String fullName = jObj.getString(Constants.fullName);
                                    String activationType = jObj.getString(Constants.activationType);
                                    String activationEndDate = jObj.getString(Constants.activationEndDate);
                                    String activation_start_date = jObj.getString(Constants.activationStartData);
                                    String accessToken = jObj.getString(Constants.accessToken);
                                    String classId = jObj.getString(Constants.classId);
                                    String mobile_number = jObj.getString(Constants.mobileNumber);
                                    String mobile_country_code = jObj.getString(Constants.mobile_country_code);
                                    String user_type = jObj.getString(Constants.userType);
                                    String activation_key = jObj.getString(Constants.activationKey);
                                    String current_date = jObj.getString(Constants.currentDate);
                                    String device_id = jObj.getString(Constants.ACTIVATION_DEVICE_ID);
                                    String tutorix_school_flag = jObj.getString(Constants.TUTORIX_SCHOOL_FLAG);
                                    String data_url = "";
                                    if (jObj.has(Constants.DATA_URL))
                                        data_url = jObj.getString(Constants.DATA_URL);

                                    String secure_data_url = "";
                                    if (jObj.has(Constants.SECURE_DATA_URL))
                                        secure_data_url = jObj.getString(Constants.SECURE_DATA_URL);
                                    // Log.d("secure_data_url", secure_data_url);

                                    if (accessToken == null || accessToken.isEmpty()) {
                                        loginView.showMessage("Already Logged in other device, Please De-activate that device and try again!", 0);
                                        return;
                                    }

                                    SessionManager session = new SessionManager();
                                    SessionManager.setLogin(activity, userId, accessToken, device_id, activationType, classId, secure_data_url, data_url, tutorix_school_flag);

                                    UserProfile profile = new UserProfile();
                                    profile.full_name = fullName;
                                    profile.user_id = userId;
                                    profile.accessToken = accessToken;
                                    profile.mobile_number = mobile_number;
                                    profile.mobile_country_code = mobile_country_code;
                                    profile.user_type = user_type;
                                    profile.password = inputPassword;
                                    profile.deviceId = device_id;
                                    //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                    long isINsrted = MyDatabase.getDatabase(activity).userDAO().insertUser(profile);
                                    //long isINsrted = db.insertProfile(profile);

                                    ActivationDetails aDeails = new ActivationDetails();
                                    aDeails.current_date = current_date;
                                    aDeails.user_id = userId;
                                    aDeails.activation_type = activationType;
                                    aDeails.activation_key = activation_key;
                                    aDeails.activation_start_date = activation_start_date;
                                    aDeails.activation_end_date = activationEndDate;
                                    aDeails.class_id = classId;
                                    aDeails.secure_url = secure_data_url;
                                    aDeails.data_url = data_url;
                                    aDeails.device_id = device_id;

                                    long days = 0;
                                    if (activation_key.length() > 2) {
                                        days = CommonUtils.daysBetween(activation_start_date, activationEndDate);
                                    }
                                    aDeails.days_left = days + "";


                                    ActivationDetails activationDetails = MyDatabase.getDatabase(activity).activationDAO().getActivationDetails(userId, classId);
                                    if (activationDetails == null)
                                        MyDatabase.getDatabase(activity).activationDAO().inserActivation(aDeails);
                                    else
                                        MyDatabase.getDatabase(activity).activationDAO().updateActivationDetails(aDeails.activation_end_date, aDeails.activation_start_date, aDeails.activation_type, aDeails.activation_key, aDeails.current_date, aDeails.days_left, aDeails.class_id, aDeails.user_id, aDeails.secure_url, aDeails.data_url);

                                    SharedPref sh = new SharedPref();
                                    sh.setExpired(activity, false);
                                    AppConfig.CALLED_EXPIERY = false;
                                    //MyDatabase.getDatabase(activity).activationDAO().inserActivation(aDeails);
                                    //db.insertActivations(aDeails);
                                    sh.setIsFirstime(activity, false);
                                    getProfileInformation(accessToken, classId, userId, tutorix_school_flag.equalsIgnoreCase("N"));

                                    //Log.v("Activation Type","Activation Type "+activationType);

                                    if ((!activationType.equals("O") && !activationType.isEmpty())) {
                                        if (device_id.equalsIgnoreCase(android_id)) {
                                            getbookmarkscompleted(classId, accessToken, userId);
                                            prepareTrackData(classId, accessToken, userId);
                                            prepareQuizData(classId, accessToken, userId);
                                            prepareMockDataData(classId, accessToken, userId);
                                            prepareRecomanDataData(classId, accessToken, userId);
                                        }


                                    }

                                } else {
                                    loginView.showMessage(jObj.getString(Constants.message), error_code);
                                }

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
                    msg = activity.getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = activity.getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = activity.getResources().getString(R.string.error_4);
                }
                loginView.showMessage(msg, 0);
                CustomDialog.closeDialog();
            }
        }) {
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Log.v("encryption ","encryption "+encryption);
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }

    private void getProfileInformation(final String accessToken, String classId, final String userId,Boolean school_info) {
        if (AppStatus.getInstance(activity).isOnline()) {
            final JSONObject fjson = new JSONObject();

            try {
                fjson.put(Constants.accessToken, accessToken);
                fjson.put(Constants.classId, classId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tag_string_req = Constants.reqRegister;
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            CustomDialog.showDialog(activity, false);
            String urlEncode = "";
            try {
                urlEncode = URLEncoder.encode(encryption, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringRequest strReq = new StringRequest(
                    Request.Method.GET,
                    Constants.USERS + "/" + userId + "?json_data=" + urlEncode,
                    new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            CustomDialog.closeDialog();
                            try {
                                JSONObject jObj = new JSONObject(response);


                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    UserProfile profile = MyDatabase.getDatabase(activity).userDAO().getUserProfile(userId);

                                    if (profile == null) {
                                        profile = new UserProfile();
                                        profile.password = password;
                                        profile.accessToken = accessToken;
                                        profile.user_id = userId;
                                    }

                                    profile.full_name = checkNotNull(jObj.getString(Constants.fullName));
                                    profile.user_name = checkNotNull(jObj.getString(Constants.userName));
                                    profile.father_name = checkNotNull(jObj.getString(Constants.fatherName));
                                    profile.mobile_number = checkNotNull(jObj.getString(Constants.mobileNumber));

                                    profile.email_id = checkNotNull(jObj.getString(Constants.emailId));
                                    profile.address = checkNotNull(jObj.getString(Constants.address));
                                    profile.city = checkNotNull(jObj.getString(Constants.city));
                                    profile.state = checkNotNull(jObj.getString(Constants.state));

                                    profile.country_code = checkNotNull(jObj.getString(Constants.country_code));
                                    profile.mobile_country_code = checkNotNull(jObj.getString(Constants.mobile_country_code));
                                    profile.date_of_birth = checkNotNull(jObj.getString(Constants.dob));
                                    profile.gender = checkNotNull(jObj.getString(Constants.gender));
                                    profile.school_name = checkNotNull(jObj.getString(Constants.schoolName));
                                    profile.roll_number = checkNotNull(jObj.getString(Constants.rollNumber));
                                    profile.section_name = checkNotNull(jObj.getString(Constants.sectionName));
                                    profile.school_city = checkNotNull(jObj.getString(Constants.school_city));
                                    profile.school_address = checkNotNull(jObj.getString(Constants.school_address));
                                    profile.school_state = checkNotNull(jObj.getString(Constants.school_state));
                                    profile.photo = checkNotNull(jObj.getString(Constants.photo));
                                    profile.school_country_code = checkNotNull(jObj.getString(Constants.school_country));
                                    profile.school_postal_code = checkNotNull(jObj.getString(Constants.school_zip));
                                    profile.postal_code = checkNotNull(jObj.getString(Constants.postal_code));


                                    MyDatabase.getDatabase(activity).userDAO().updateProfile(profile);
                                    if (school_info) {
                                        loginView.navigateScreen(5);
                                    }else {
                                        loginView.navigateScreen(0);
                                    }
                                    //callLandingPage();
                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    //callLandingPage();
                                    //CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                                // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                loginView.navigateScreen(0);
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //CommonUtils.showToast(getApplicationContext(),getString(R.string.there_is_error));
                    //Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();
                    loginView.navigateScreen(0);
                    CustomDialog.closeDialog();
                }
            }) {

            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {

            loginView.showMessage(activity.getString(R.string.no_internet_message), 0);

            //CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet_message));
            // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void prepareTrackData(String class_id, String access_token, final String userid) {

        final List<Track> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {
            fjson.put("entity_type", "A");
            fjson.put("entity_id", "0");
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put("offset", "0");
            fjson.put("limit", "200");
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Log.v("Track req","Track Req "+ Constants.USER_TRACK + "/" + userid + "?json_data=" + encode);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.USER_TRACK + "/" + userid + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, "Track Resposnse " + response);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                JSONArray answers = jObj.getJSONArray(Constants.trackData);
                                MyDatabase db = MyDatabase.getDatabase(activity);


                                for (int i = 0; i < answers.length(); i++) {

                                    JSONObject json_data = answers.getJSONObject(i);
                                    TrackModel chapters = new TrackModel();
                                    chapters.activity_type = json_data.getString("activity_type");
                                    chapters.activity_duration = json_data.getString("activity_duration");
                                    chapters.activity_date = json_data.getString("activity_date");
                                    chapters.quiz_id = json_data.getString("quiz_id");
                                    chapters.subject_id = json_data.getString(Constants.subjectId);
                                    chapters.section_id = json_data.getString(Constants.sectionId);
                                    chapters.lecture_id = json_data.getString(Constants.lectureId);
                                    chapters.class_id = json_data.getString(Constants.classId);

                                    chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                                    if (json_data.has(Constants.lectureName))
                                        chapters.lecture_name = json_data.getString(Constants.lectureName);
                                    chapters.user_id = userid;
                                    chapters.is_sync = true;

                                    TrackModel tm = db.trackDAO().isTrackAdded(chapters.user_id, chapters.class_id, chapters.subject_id, chapters.section_id, chapters.lecture_id, chapters.activity_type, chapters.activity_date);
                                    //Log.v("Track Model","Track Model "+tm);
                                    if (tm == null) {
                                        long id = db.trackDAO().insertTrack(chapters);
                                        // Log.v("Track Model","Track Model "+id);
                                    }

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                //Log.v("Track req","Track Req encryption "+encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getbookmarkscompleted(String classid, String access_token, final String userid) {

        String tag_string_req = Constants.reqRegister;

        final JSONObject fjson3 = new JSONObject();
        try {
            fjson3.put(Constants.classId, classid);
            fjson3.put(Constants.accessToken, access_token);
            fjson3.put(Constants.classId, classid);
            fjson3.put(Constants.subjectId, "");
            fjson3.put("action_type", "A");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message3 = fjson3.toString();
        final String encryption3 = Security.encrypt(message3, Key);

        String encode = "";
        try {
            encode = URLEncoder.encode(encryption3, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = AppConfig.BASE_URL + "lectures/actions/" + userid + "?json_data=" + encode;

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);

                    if (!jObj.getBoolean("error_flag")) {
                        JSONArray array = jObj.getJSONArray("lectures");
                        JSONObject objLect;
                        SubChapters subChapters;
                        MyDatabase hadnler = MyDatabase.getDatabase(activity);

                        for (int k = 0; k < array.length(); k++) {
                            subChapters = new SubChapters();
                            objLect = array.getJSONObject(k);
                            subChapters.userid = userid;
                            subChapters.lecture_id = objLect.getString("lecture_id");
                            subChapters.classid = objLect.getString("class_id");
                            subChapters.subjectid = objLect.getString("subject_id");
                            subChapters.section_id = objLect.getString("section_id");
                            subChapters.txt = objLect.getString("lecture_name");
                            subChapters.lecture_duration = objLect.getString("lecture_duration");
                            if (objLect.has("lecture_video_url"))
                                subChapters.lecture_video_url = objLect.getString("lecture_video_url");
                            if (objLect.has("lecture_video_thumb"))
                                subChapters.lecture_video_thumb = objLect.getString("lecture_video_thumb");
                            subChapters.lecture_video_url = Constants.VIDEO_NAME;
                            subChapters.lecture_video_thumb = Constants.VIDEO_THUMB_NAME;
                            subChapters.video_srt = Constants.VIDEO_SRT;
                            subChapters.lecture_completed = (objLect.getString("completed_flag").equals("Y"));
                            subChapters.lecture_bookmark = (objLect.getString("bookmark_flag").equals("Y"));
                            subChapters.lecture_notes = objLect.getString("user_lecture_notes");

                            if (subChapters.lecture_notes != null && subChapters.lecture_notes.length() > 0)
                                subChapters.is_notes = true;
                            subChapters.is_sync = true;

                            hadnler.subjectChapterDAO().addBookMark(subChapters);
                        }
                    }
                    // Log.v("get Data", "get Data " + jObj);
                    //parsebookmarkdata(jObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = activity.getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = activity.getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = activity.getResources().getString(R.string.error_4);
                }
                CommonUtils.showToast(activity, msg);


            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void prepareQuizData(String class_id, String access_token, final String userid) {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.USER_QUIZ_ALL + "/" + userid + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                MyDatabase hadnler = MyDatabase.getDatabase(activity);
                                JSONArray quiz_data_array = jObj.getJSONArray("quiz_data");
                                JSONObject quiz_data;
                                for (int k = 0; k < quiz_data_array.length(); k++) {
                                    quiz_data = quiz_data_array.getJSONObject(k);
                                    String qJson = quiz_data.getString("quiz_json");
                                    qJson = qJson.replace("\\", "");
                                    String lecture_name = "";
                                    if (quiz_data.has(Constants.lectureName))
                                        lecture_name = quiz_data.getString(Constants.lectureName);
                                    QuizModel quizModel = new QuizModel();
                                    quizModel.userId = userid;
                                    quizModel.classId = quiz_data.getString("class_id");
                                    quizModel.subject_id = quiz_data.getString("subject_id");
                                    quizModel.section_id = quiz_data.getString("section_id");
                                    quizModel.lectur_id = quiz_data.getString("lecture_id");
                                    quizModel.QuizDuration = quiz_data.getString("quiz_duration");
                                    quizModel.total_wrong = quiz_data.getString("total_wrong");
                                    quizModel.total_correct = quiz_data.getString("total_marks");
                                    quizModel.total = quiz_data.getInt("total_questions");
                                    quizModel.lecture_name = quiz_data.getString("lecture_name");
                                    quizModel.section_name = quiz_data.getString("section_name");
                                    quizModel.mock_test = quiz_data.getString("mock_test");
                                    quizModel.attempted_questions = quiz_data.getString("attempted_questions");
                                    quizModel.QuizCreatedDtm = quiz_data.getString("created_dtm");
                                    quizModel.quiz_id = "";
                                    quizModel.question = qJson;
                                    // Log.v("qJson","qJson "+qJson);
                                    quizModel.sync = "Y";
                                    quizModel.lecture_name = lecture_name;

                                    QuizModel qm = hadnler.quizModelDAO().getQuizModel(userid, quizModel.classId, quizModel.subject_id, quizModel.QuizCreatedDtm);
                                    //Log.v("QuizModel","QuizModel "+qm);
                                    if (qm == null)
                                        hadnler.quizModelDAO().addQuiz(quizModel);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(activity, activity.getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(activity, error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                activity.finish();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void prepareMockDataData(String class_id, String access_token, final String userid) {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.QUIZ_SYNC_TO_SERVER_RECOMANDED + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            //Log.v("Response","Response Data "+response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                MyDatabase hadnler = MyDatabase.getDatabase(activity);
                                JSONArray quiz_data_array = jObj.getJSONArray("recommended_videos");
                                JSONObject quiz_data;
                                for (int k = 0; k < quiz_data_array.length(); k++) {
                                    RecomandedModel model = new RecomandedModel();
                                    quiz_data = quiz_data_array.getJSONObject(k);
                                    model.user_id = quiz_data.getString("user_id");
                                    model.class_id = quiz_data.getString("class_id");
                                    model.subject_id = quiz_data.getString("subject_id");
                                    model.section_id = quiz_data.getString("section_id");
                                    model.lecture_id = quiz_data.getString("lecture_id");
                                    model.lecture_title = quiz_data.getString("lecture_title");
                                    model.mocktest_type = quiz_data.getString("mocktest_type");
                                    model.created_dtm = quiz_data.getString("created_dtm");
                                    model.sync = "Y";
                                    hadnler.recomandedDAO().insertRecomanded(model);

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(activity, activity.getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(activity, error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                activity.finish();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void prepareRecomanDataData(String class_id, String access_token, final String userid) {

        final List<TrackModel> movieList = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, class_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String encode = "";
        try {
            encode = URLEncoder.encode(encryption, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.QUIZ_SYNC_FROM_SERVER_MOCKSTATS + "?json_data=" + encode,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            //Log.v("Resposnse Stats","Resposnse Stats "+response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                MyDatabase hadnler = MyDatabase.getDatabase(activity);
                                JSONArray quiz_data_array = jObj.getJSONArray("stats_data");
                                JSONObject quiz_data;
                                MockTestModelTable mockTestModelTable;
                                for (int k = 0; k < quiz_data_array.length(); k++) {

                                    quiz_data = quiz_data_array.getJSONObject(k);
                                    mockTestModelTable = new MockTestModelTable();
                                    mockTestModelTable.user_id = quiz_data.getString("user_id");
                                    mockTestModelTable.class_id = quiz_data.getString("class_id");
                                    mockTestModelTable.subject_id = quiz_data.getString("subject_id");
                                    mockTestModelTable.section_id = quiz_data.getString("section_id");
                                    mockTestModelTable.mocktest_type = quiz_data.getString("mocktest_type");
                                    mockTestModelTable.total_attempts = quiz_data.getInt("total_attempts");
                                    mockTestModelTable.total_marks = quiz_data.getInt("total_marks");
                                    mockTestModelTable.total_questions = quiz_data.getInt("total_questions");
                                    mockTestModelTable.low_marks = quiz_data.getInt("low_marks");
                                    mockTestModelTable.high_marks = quiz_data.getInt("high_marks");
                                    mockTestModelTable.created_dtm = quiz_data.getString("created_dtm");
                                    mockTestModelTable.total_questions = mockTestModelTable.total_questions * mockTestModelTable.total_attempts;
                                    MockTestModelTable mockTestModelTables = hadnler.mockTestDAO().getMockTest(userid, mockTestModelTable.class_id, mockTestModelTable.subject_id, mockTestModelTable.section_id, mockTestModelTable.mocktest_type);

                                    if (mockTestModelTables == null) {
                                        hadnler.mockTestDAO().insertRecomanded(mockTestModelTable);
                                    } else {
                                        hadnler.mockTestDAO().updateMockTest(userid, mockTestModelTable.class_id, mockTestModelTable.subject_id, mockTestModelTable.section_id, mockTestModelTable.mocktest_type, mockTestModelTable.total_marks, mockTestModelTable.total_attempts, mockTestModelTable.low_marks, mockTestModelTable.high_marks);
                                    }

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(activity, activity.getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.showToast(activity, error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                activity.finish();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String checkNotNull(String s) {
        if (s == null || s.equals("null") || s.isEmpty())
            return "";
        else return s;
    }
}
