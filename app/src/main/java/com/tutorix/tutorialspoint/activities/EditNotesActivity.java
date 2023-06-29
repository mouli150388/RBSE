package com.tutorix.tutorialspoint.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditNotesActivity extends AppCompatActivity {

    EditNotesActivity _this;
    private final String TAG = EditNotesActivity.class.getSimpleName();

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText yourEditText;
    private String user_id, access_token;
    private String lecture_id,lectureName, classid,subjectId,section_id;
    AppBarLayout appbar;
    String loginType;
    String filePath;
    View lnr_home;
    TextView txt_save;
    TextView subjectnameTVID;
    View lnr_container;
    //SubChapters subChapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = EditNotesActivity.this;
        setContentView(R.layout.activity_edit_notes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        lnr_home= findViewById(R.id.lnr_home);
        appbar= findViewById(R.id.appbar);
        subjectnameTVID= findViewById(R.id.subjectnameTVID);
        lnr_container= findViewById(R.id.lnr_container);
        AppController.getInstance().startLayoutAnimation(lnr_container);

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        loginType = userInfo[2];
        txt_save = findViewById(R.id.txt_save);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            user_id = extras.getString(Constants.userId);
            classid = extras.getString(Constants.classId);
            subjectId = extras.getString(Constants.subjectId);
            section_id = extras.getString(Constants.sectionId);
            lecture_id = extras.getString(Constants.lectureId);
            lectureName = extras.getString(Constants.lectureName);

            yourEditText = findViewById(R.id.quoteTextArea);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                yourEditText.setImeHintLocales(new LocaleList(new Locale("hi", "India")));
            }


            if(subjectId.equals("1"))
                appbar.setBackgroundResource((R.drawable.ic_phy_bg_green));
            else  if(subjectId.equals("2"))
                appbar.setBackgroundResource(( R.drawable.ic_chemistry_bg_green));
            else  if(subjectId.equals("3"))
                appbar.setBackgroundResource((R.drawable.ic_bio_bg_green));
            else  if(subjectId.equals("4"))
                appbar.setBackgroundResource((R.drawable.ic_math_bg_green));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                switch (subjectId) {
                    case "1":
                        window.setStatusBarColor(getResources().getColor(R.color.phy_background_status));
                        subjectnameTVID.setTextColor(getResources().getColor(R.color.phy_background));
                        break;
                    case "2":
                        window.setStatusBarColor(getResources().getColor(R.color.che_background_status));
                        subjectnameTVID.setTextColor(getResources().getColor(R.color.che_background));
                        break;
                    case "3":
                        window.setStatusBarColor(getResources().getColor(R.color.bio_background_status));
                        subjectnameTVID.setTextColor(getResources().getColor(R.color.bio_background));
                        break;
                    case "4":
                        window.setStatusBarColor(getResources().getColor(R.color.math_background_status));
                        subjectnameTVID.setTextColor(getResources().getColor(R.color.math_background));
                        break;
                }
            }

            txt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txt_save.getText().toString().equals("Edit"))
                    {
                        yourEditText.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (Objects.requireNonNull(imm).isAcceptingText()) {
                            imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
                        } else {
                            imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                        }
                        yourEditText.setCursorVisible(true);
                        yourEditText.setSelection(yourEditText.getText().toString().length());
                        txt_save.setText("Save");
                        return;
                    }

                    if(yourEditText.getText().toString().trim().length()<=0)
                    {
                        CommonUtils.showToast(getApplicationContext(),"Please write some notes");
                        return;
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);



                    if(loginType.isEmpty())
                    {
                        if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                            saveNotes(yourEditText.getText().toString());
                        } else {
                            // finish();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
                            // Toasty.info(VideoActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                        }
                    }else if (loginType.equalsIgnoreCase("O")){

                        if(AppConfig.checkSDCardEnabled(_this,user_id,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
                        {
                            if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                                saveNotes(yourEditText.getText().toString());
                                saveNotesIntoLocal(false);
                            }else
                            {
                                saveNotesIntoLocal(true);
                            }
                        }else
                        {
                            if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                                saveNotes(yourEditText.getText().toString());
                            } else {
                                // finish();
                                CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
                                // Toasty.info(VideoActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }else
                    {
                        if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                            saveNotes(yourEditText.getText().toString());
                            saveNotesIntoLocal(false);
                        }else
                        {
                            saveNotesIntoLocal(true);
                        }
                    }

                    /*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
                        if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                            saveNotes(yourEditText.getText().toString());
                        } else {
                           // finish();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
                            // Toasty.info(VideoActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                        }

                    }else
                    {
                        if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                            saveNotes(yourEditText.getText().toString());
                            saveNotesIntoLocal(false);
                        }else
                        {
                            saveNotesIntoLocal(true);
                        }

                    }*/



                }
            });

            if(loginType.isEmpty())
            {
                if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                    try {
                        getNotes();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    // finish();
                    CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
                    // Toasty.info(VideoActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }

            }else if (loginType.equalsIgnoreCase("O")){

                if(AppConfig.checkSDCardEnabled(_this,user_id,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
                {
                    filePath= AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lecture_id + "/" + "mynotes.txt";

                    getNotesFromLocal();
                }else
                {
                    if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                        try {
                            getNotes();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // finish();
                        CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
                        // Toasty.info(VideoActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                    }

                }
            }else
            {
                filePath= AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lecture_id + "/" + "mynotes.txt";

                getNotesFromLocal();
            }

          /*  if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
                if (AppStatus.getInstance(EditNotesActivity.this).isOnline()) {
                    try {
                        getNotes();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                   // finish();
                    CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
                    // Toasty.info(VideoActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }

            }else
            {

                filePath= AppConfig.getSdCardPath(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + "mynotes.txt";

                getNotesFromLocal();
            }*/


        }
        subjectnameTVID.setText(lectureName);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setTitle("Write Your Notes");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
      /*  yourEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (Objects.requireNonNull(imm).isAcceptingText()) {
                        imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                    }
                }
            }
        });*/
        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home(v);
            }
        });
    }
    public void home(View v)
    {
        Intent in=new Intent(EditNotesActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();

    }
    private void getNotes() throws UnsupportedEncodingException {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, user_id);
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.sectionId, section_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption1 = Security.encrypt(message, Key);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Constants.LECTURE_ACTIONS_NOTES + "/" + lecture_id + "?json_data=" + URLEncoder.encode(encryption1, "UTF-8"),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(TAG, response);
                        try {
                            CustomDialog.closeDialog();
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);

                            if (!error) {
                                String notes=jObj.getString(Constants.notes);


                                if(notes==null||notes.equals("null"))
                                    notes="";
                                yourEditText.setText(notes);
                                yourEditText.setSelection(notes.length());
                                if(yourEditText.getText().toString().isEmpty())
                                    toogleButton(false);
                                else toogleButton(true);



                            } else {
                                if(yourEditText.getText().toString().isEmpty())
                                    toogleButton(false);
                                else
                                    toogleButton(true);

                               // CommonUtils.showToast(getApplicationContext(),jObj.getString(Constants.message));
                                // Toasty.warning(_this, jObj.getString(Constants.message), Toast.LENGTH_SHORT, true).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),e.getMessage());
                            if(yourEditText.getText().toString().isEmpty())
                                toogleButton(false);
                            else toogleButton(true);
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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
                CommonUtils.showToast(getApplicationContext(), msg);
                if(yourEditText.getText().toString().isEmpty())
                    toogleButton(false);
                else toogleButton(true);
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
               // finish();

            }
        });
        CustomDialog.showDialog(EditNotesActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void saveNotes(String s) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, user_id);
            fjson.put(Constants.lectureId, lecture_id);
            fjson.put(Constants.notes, s);
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.sectionId, section_id);
            fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.lectureName, lectureName);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.LECTURE_ACTIONS_NOTES,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(TAG, response);
                        try {
                            CustomDialog.closeDialog();
                            JSONObject jObj = new JSONObject(response);
                            if (!jObj.getBoolean(Constants.errorFlag)) {
                                CommonUtils.showToast(getApplicationContext(),jObj.getString(Constants.message));
                                //Toast.makeText(_this, jObj.getString(Constants.message), Toast.LENGTH_SHORT).show();
                                //Log.v("OnactivityResult","OnactivityResult"+200);
                               set_result=200;
                                toogleButton(true);
                               // finish();
                            } else {
                                CommonUtils.showToast(getApplicationContext(),jObj.getString(Constants.message));
                                //  Toasty.warning(_this, jObj.getString(Constants.message), Toast.LENGTH_SHORT, true).show();
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
                CustomDialog.closeDialog();
                CommonUtils.showToast(getApplicationContext(), msg);
                // Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                //finish();
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
        CustomDialog.showDialog(EditNotesActivity.this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onSupportNavigateUp() {
       onBackPressed();
        AppController.getInstance().playAudio(R.raw.back_sound);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent();
        in.putExtra("text",yourEditText.getText().toString());
        setResult(set_result,in);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          /*  case R.id.menu_item_new_quote:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (Objects.requireNonNull(imm).isAcceptingText()) {
                    imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                }
                return (true);
              case R.id.menu_item_save:
                  txt_save.performClick();
                return (true);*/
            case R.id.record:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.todo));
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                   /* Toast.makeText(getApplicationContext(),
                            "Sorry your device is not supported",
                            Toast.LENGTH_SHORT).show();*/
                    CommonUtils.showToast(getApplicationContext(),"Sorry your device is not supported");
                }
                return true;

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String recorded = result.get(0);
                    int start = Math.max(yourEditText.getSelectionStart(), 0);
                    int end = Math.max(yourEditText.getSelectionEnd(), 0);
                    yourEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                            recorded, 0, recorded.length());
                }
                break;
            }
        }
    }


    private void getNotesFromLocal()
    {

       /* File f=new File(filePath);
        if(f.exists())
        {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line = "";
                StringBuffer b=new StringBuffer();
                while ((line = br.readLine()) != null) {
                    //Do something here
                    b.append(line);
                    b.append("\n");
                }
                yourEditText.setText(b);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
        MyDatabase dbHelper = MyDatabase.getDatabase(getApplicationContext());
        SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                user_id, classid,
                section_id, lecture_id
                , subjectId
        );
        if(subChapter!=null&&subChapter.lecture_notes!=null&&!subChapter.lecture_notes.equals("null"))
           yourEditText.setText(subChapter.lecture_notes);
        else
       yourEditText.setText("");

        if(yourEditText.getText().toString().trim().length()>0) {
            txt_save.setText("Edit");


            if(getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
          else {
                txt_save.setText("Save");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            yourEditText.requestFocus();
            yourEditText.setCursorVisible(true);
                imm.showSoftInput(yourEditText, InputMethodManager.SHOW_FORCED);

        }

    }

    private void saveNotesIntoLocal(boolean hasToFinish)
    {

      /*  File f=new File(filePath);
        if(f.exists())
            f.delete();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
           /* FileOutputStream fos = new FileOutputStream(f);
            fos.write(yourEditText.getText().toString().getBytes());
            fos.close();*/
            CommonUtils.showToast(getApplicationContext(),getString(R.string.notes_saved));


            MyDatabase dbHelper = MyDatabase.getDatabase(getApplicationContext());
            SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                    user_id, classid,
                    section_id, lecture_id
                    , subjectId
            );

            if(subChapter==null) {
                SubChapters subchapters=new SubChapters();
                subchapters.lecture_id = lecture_id;
                subchapters.txt = lectureName;
                subchapters.userid = user_id;
                subchapters.classid = classid;
                subchapters.section_id = section_id;
                subchapters.subjectid = subjectId;
                subchapters.is_notes =true;
                subchapters.is_sync =false;

                subchapters.lecture_notes =yourEditText.getText().toString().trim();
                dbHelper.subjectChapterDAO().addBookMark(subchapters);
            }
            else {
                subChapter.is_sync=false;
                dbHelper.subjectChapterDAO().updateBookmarkNotes(subChapter.userid, subChapter.classid, subChapter.section_id, subChapter.lecture_id, subChapter.subjectid, true, yourEditText.getText().toString().trim(),false);
            }
            if(hasToFinish)
            {
                set_result=200;
                //setResult(200);
                //finish();
            }
            toogleButton(true);
            //finish();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    int set_result;

    private void toogleButton(boolean value)
    {
        if(value)
            txt_save.setText("Edit");
        else txt_save.setText("Save");
        if(value)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            yourEditText.setCursorVisible(false);
        }else
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            yourEditText.requestFocus();
            yourEditText.setCursorVisible(true);
            if (Objects.requireNonNull(imm).isAcceptingText()) {
                imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
            }

        }
    }
}
