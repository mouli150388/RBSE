package com.tutorix.tutorialspoint.activities;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.SubAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.Chapters;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.quiz.QuizMockExamActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LecturesActivity extends AppCompatActivity {
    LecturesActivity _this;

    Chapters chapters;
    MyDatabase dbhelper;


    String loginType;


    String[] userInfo;
    @BindView(R.id.lnr_container)
    LinearLayout lnr_container;
    @BindView(R.id.img_gif)
    ImageView imgGif;

    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.list_grid)
    CheckBox listGrid;
    @BindView(R.id.lnr_gridlist)
    LinearLayout lnrGridlist;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.lnr_home)
    LinearLayout lnrHome;
    @BindView(R.id.lnr_mock)
    LinearLayout lnr_mock;
    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;
   /* @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;*/
    @BindView(R.id.lnr_reload)
    LinearLayout lnrReload;
    @BindView(R.id.img_nodata)
    ImageView imgNodata;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.lnr_nosdcard)
    LinearLayout lnrNosdcard;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rel_top_main)
    RelativeLayout rel_top_main;
    @BindView(R.id.img_background)
    ImageView img_background;
    @BindView(R.id.txt_header)
    TextView txt_header;
    private String access_token;
    String subjectId = "";
    String section_id = "";


    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.setLanguages(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = LecturesActivity.this;
        setContentView(R.layout.activity_lecture);
        ButterKnife.bind(this);
        AppController.getInstance().startLayoutAnimation(lnr_container);
        userInfo = SessionManager.getUserInfo(_this);

        access_token = userInfo[1];
        loginType = userInfo[2];

        recyclerView.setLayoutManager(new LinearLayoutManager(_this, RecyclerView.VERTICAL, false));
        //recyclerView.setItemViewCacheSize(20);
        //recyclerView.setDrawingCacheEnabled(true);
        //recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        dbhelper = MyDatabase.getDatabase(_this);
        getSupportActionBar().setTitle("");
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            chapters = (Chapters) extras.get(Constants.chapterIntent);
            String _subjectName = chapters.txt.substring(0, 1).toUpperCase() + chapters.txt.substring(1);

            subjectId = chapters.subjectid;
            section_id = chapters.section_id;

            imgGif.setVisibility(View.VISIBLE);
            if (chapters.subject_name.equalsIgnoreCase("physics")) {
                Glide.with(getApplicationContext()).load(R.drawable.gif_phy).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
                rel_top_main.setBackgroundResource(R.drawable.ic_phy_bg_green);
               // img_background.setImageResource(R.drawable.ic_phy_bg_white);
                Glide.with(getApplicationContext()).load(R.drawable.ic_phy_bg_white).into(img_background);
                txt_header.setText(getString(R.string.physics));
            } else if (chapters.subject_name.equalsIgnoreCase("chemistry")) {
                Glide.with(getApplicationContext()).load(R.drawable.gif_che).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
                //Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
                rel_top_main.setBackgroundResource(R.drawable.ic_chemistry_bg_green);
                //img_background.setImageResource(R.drawable.ic_chemistry_bg_white);
                Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
                txt_header.setText(getString(R.string.chemistry));
            } else if (chapters.subject_name.equalsIgnoreCase("biology")) {
                Glide.with(getApplicationContext()).load(R.drawable.gif_bio).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
                rel_top_main.setBackgroundResource(R.drawable.ic_bio_bg_green);
                //img_background.setImageResource(R.drawable.ic_bio_bg_white);
                Glide.with(getApplicationContext()).load(R.drawable.ic_bio_bg_white).into(img_background);
                txt_header.setText(getString(R.string.biology));
            } else {
                Glide.with(getApplicationContext()).load(R.drawable.gif_math).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
                rel_top_main.setBackgroundResource(R.drawable.ic_math_bg_green);
                //img_background.setImageResource(R.drawable.ic_math_bg_white);
                Glide.with(getApplicationContext()).load(R.drawable.ic_math_bg_white).into(img_background);
                txt_header.setText(getString(R.string.mathematics));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                switch (subjectId) {
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

            //loadingPanelID.hide();

            if (loginType.isEmpty()){
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        fillWithData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                    // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }else if(loginType.equalsIgnoreCase("O")) {

                if(AppConfig.checkSDCardEnabled(_this,userInfo[0],userInfo[4])&&AppConfig.checkSdcard(userInfo[4],getApplicationContext()))
                {
                    if (chapters.subchapters != null) {
                        for (int i = 0; i < chapters.subchapters.size(); i++) {

                            SubChapters subChapter = dbhelper.subjectChapterDAO().getBookmarkorData(
                                    chapters.userid, chapters.classid,
                                    chapters.section_id, chapters.subchapters.get(i).lecture_id
                                    , chapters.subjectid
                            );
                            if (subChapter != null) {
                                chapters.subchapters.get(i).lecture_completed = subChapter.lecture_completed;
                                chapters.subchapters.get(i).lecture_bookmark = subChapter.lecture_bookmark;
                                chapters.subchapters.get(i).is_notes = subChapter.is_notes;

                                //chapters.subchapters.get(i).is_notes = subChapter.is_notes;
                            }

                        }
                    }

                    SubAdapter subchaptersAdapter = new SubAdapter(chapters.subchapters,chapters.txt, _this, true, false,chapters.availableAllVideos);


                    //recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(subchaptersAdapter);
                    subchaptersAdapter.notifyDataSetChanged();


                    int resId = R.anim.layout_animation_slide;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
                    recyclerView.setLayoutAnimation(animation);
                    subchaptersAdapter.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();

                }else
                {
                    if (AppStatus.getInstance(_this).isOnline()) {
                        try {
                            fillWithData();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                        // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                    }
                }

            }else
            {
                if (chapters.subchapters != null) {
                    for (int i = 0; i < chapters.subchapters.size(); i++) {

                        SubChapters subChapter = dbhelper.subjectChapterDAO().getBookmarkorData(
                                chapters.userid, chapters.classid,
                                chapters.section_id, chapters.subchapters.get(i).lecture_id
                                , chapters.subjectid
                        );
                        if (subChapter != null) {
                            chapters.subchapters.get(i).lecture_completed = subChapter.lecture_completed;
                            chapters.subchapters.get(i).lecture_bookmark = subChapter.lecture_bookmark;
                            chapters.subchapters.get(i).is_notes = subChapter.is_notes;

                            //chapters.subchapters.get(i).is_notes = subChapter.is_notes;
                        }

                    }
                }

                SubAdapter subchaptersAdapter = new SubAdapter(chapters.subchapters,chapters.txt, _this, true, false,chapters.availableAllVideos);


                //recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(subchaptersAdapter);
                subchaptersAdapter.notifyDataSetChanged();


                int resId = R.anim.layout_animation_slide;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
                recyclerView.setLayoutAnimation(animation);
                subchaptersAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
            }




            //initCollapsingToolbar(chapters.txt);
        }
        if(chapters==null)
            lnr_mock.setVisibility(View.GONE);
        else
            lnr_mock.setVisibility(View.VISIBLE);


    }



    private void fillWithData() throws UnsupportedEncodingException {

        String tag_string_req = Constants.reqRegister;
        //loadingPanelID.show();
        CustomDialog.showDialog(LecturesActivity.this, true);
        final JSONObject fjson3 = new JSONObject();
        try {

            fjson3.put(Constants.classId, chapters.classid);
            fjson3.put(Constants.accessToken, access_token);
            fjson3.put(Constants.subjectId, subjectId);
            fjson3.put(Constants.sectionId, section_id);
            fjson3.put(Constants.action_type, "S");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message3 = fjson3.toString();
        final String encryption3 = Security.encrypt(message3, Key);



        String url = AppConfig.BASE_URL + "lectures/actions/" + chapters.userid + "?json_data=" + URLEncoder.encode(encryption3, "UTF-8");

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    CustomDialog.closeDialog();
                    JSONObject jObj = new JSONObject(response);

                    if(jObj.getBoolean("error_flag")) {
                        String errorMsg = jObj.getString(Constants.message);
                        if (errorMsg.contains("Invalid access token")) {
                            MyDatabase dbHandler = MyDatabase.getDatabase(getApplicationContext());
                            dbHandler.userDAO().deleteUser(chapters.userid);
                            SessionManager.logoutUser(getApplicationContext());
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            startActivity(i);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                           finish();
                        }
                    }
                    /*{"error_flag":true,"message":"Invalid access token"}*/
                    parseData(jObj);
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
                CommonUtils.showToast(getApplicationContext(), msg);
                //Toasty.warning(_this, "There is something error", Toast.LENGTH_SHORT, true).show();
                //loadingPanelID.hide();
                CustomDialog.closeDialog();
                finish();

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void parseData(JSONObject jObj) {
        try {
            //Log.v("Resposne","Resposne "+jObj.toString());
            if (jObj.has("lectures")) {

                JSONArray bookmarksJsonArray = jObj.getJSONArray("lectures");

                if (chapters.subchapters != null) {
                    for (int i = 0; i < chapters.subchapters.size(); i++) {
                        String lectureString = chapters.userid + "/" + chapters.classid + "/" +
                                chapters.subjectid + "/" + chapters.section_id + "/" + chapters.subchapters.get(i).lecture_id;
                        for (int j = 0; j < bookmarksJsonArray.length(); j++) {

                            String lectureStringUrl = chapters.userid + "/" +
                                    bookmarksJsonArray.getJSONObject(j).getString(Constants.classId) + "/" +
                                    bookmarksJsonArray.getJSONObject(j).getString(Constants.subjectId) + "/" +
                                    bookmarksJsonArray.getJSONObject(j).getString(Constants.sectionId) + "/" +
                                    bookmarksJsonArray.getJSONObject(j).getString(Constants.lectureId);


                            if (lectureString.equalsIgnoreCase(lectureStringUrl)) {
                                chapters.subchapters.get(i).lecture_completed = bookmarksJsonArray.getJSONObject(j).getString("completed_flag").equals("Y");
                                chapters.subchapters.get(i).lecture_bookmark = bookmarksJsonArray.getJSONObject(j).getString("bookmark_flag").equals("Y");
                               String s=bookmarksJsonArray.getJSONObject(j).getString("user_lecture_notes");
                               if(s!=null&&s.length()>0&&!s.equals("null")) {
                                   chapters.subchapters.get(i).is_notes = true;
                                   chapters.subchapters.get(i).lecture_notes = s;
                               } else {
                                   chapters.subchapters.get(i).lecture_notes = "";
                                   chapters.subchapters.get(i).is_notes=false;
                               }

                                break;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SubAdapter subchaptersAdapter = new SubAdapter(chapters.subchapters,chapters.txt, _this, true, false,chapters.availableAllVideos);



        recyclerView.setAdapter(subchaptersAdapter);
        subchaptersAdapter.notifyDataSetChanged();
        //loadingPanelID.hide();

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            AppController.getInstance().playAudio(R.raw.back_sound);
        }
        return super.onOptionsItemSelected(item);
    }

    public void home(View v) {
        Intent in = new Intent(LecturesActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();

    }

    public void showSnackbar() {
        Snackbar snackbar = Snackbar.make(lnrHome, "No Quiz for this Lecture", 2000);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
// Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        switch (subjectId) {
            case "1":
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.phy_background));

                break;
            case "2":
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.che_background));
                break;
            case "3":
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bio_background));
                break;
            case "4":
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.math_background));
                break;

        }


        snackbar.show();

    }

    @OnClick({R.id.img_filter, R.id.lnr_mock,R.id.img_mock,R.id.list_grid, R.id.lnr_gridlist, R.id.img_home, R.id.lnr_home})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.img_filter:
                break;
            case R.id.list_grid:
                break;
            case R.id.lnr_gridlist:
                break;
            case R.id.img_home:
            case R.id.lnr_home:
                home(v);
                break;
            case R.id.lnr_mock:
            case R.id.img_mock:
                AppController.getInstance().playAudio(R.raw.button_click);
                        if(chapters!=null)
                        {
                            Intent i = new Intent(getApplicationContext(), QuizMockExamActivity.class);
                            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra(Constants.lectureId, "0");
                            i.putExtra(Constants.subjectId, chapters.subjectid);
                            i.putExtra(Constants.classId, chapters.classid);
                            i.putExtra(Constants.userId, chapters.userid);
                            i.putExtra(Constants.sectionId, section_id);
                            i.putExtra(Constants.sectionName, chapters.txt);
                            i.putExtra(Constants.lectureName, "");

                            startActivity(i);
                        }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Log.v("onactivityResult","onactivityResult "+requestCode+" "+resultCode);


       /* if(requestCode==200&&resultCode==200)
        {
            if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        fillWithData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                    // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }

            } else {

                if (chapters.subchapters != null) {
                    for (int i = 0; i < chapters.subchapters.size(); i++) {

                        SubChapters subChapter = dbhelper.subjectChapterDAO().getBookmarkorData(
                                chapters.userid, chapters.classid,
                                chapters.section_id, chapters.subchapters.get(i).lecture_id
                                , chapters.subjectid
                        );
                        if (subChapter != null) {
                            chapters.subchapters.get(i).lecture_completed = subChapter.lecture_completed;
                            chapters.subchapters.get(i).lecture_bookmark = subChapter.lecture_bookmark;
                            chapters.subchapters.get(i).is_notes = subChapter.is_notes;

                            //chapters.subchapters.get(i).is_notes = subChapter.is_notes;
                        }

                    }
                }

                SubAdapter subchaptersAdapter = new SubAdapter(chapters.subchapters,chapters.txt, _this, true, false);


                //recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(subchaptersAdapter);
                subchaptersAdapter.notifyDataSetChanged();


                int resId = R.anim.layout_animation_slide;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
                recyclerView.setLayoutAnimation(animation);
                subchaptersAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
            }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void NotesAactivityCall(SubChapters subChapters)
    {
        Intent i = new Intent(getApplicationContext(), EditNotesActivity.class);
        i.putExtra(Constants.userId, subChapters.userid);
        i.putExtra(Constants.lectureId, subChapters.lecture_id);
        i.putExtra(Constants.classId, subChapters.classid);
        i.putExtra(Constants.subjectId, subChapters.subjectid);
        i.putExtra(Constants.sectionId, subChapters.section_id);
        i.putExtra(Constants.lectureName, subChapters.txt);
        i.putExtra("isNotes", false);
        //i.putExtra("subchapter",data.get(holder.getAdapterPosition()));
       // i.addFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(i,200);
        //mContext = (Activity) vh.itemView.getContext();
        //overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
