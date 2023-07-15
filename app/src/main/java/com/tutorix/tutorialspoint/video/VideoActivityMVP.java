package com.tutorix.tutorialspoint.video;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.tutorix.tutorialspoint.utility.Constants.LANG_VIDEO_SUPPORT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.BuildConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.activities.NotesActivity;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.players.SDCardDataSource;
import com.tutorix.tutorialspoint.players.SimpleExoPlayerView;
import com.tutorix.tutorialspoint.players.TrackSelectionDialog;
import com.tutorix.tutorialspoint.quiz.QuizRulesActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivityMVP extends AppCompatActivity implements VideoRendererEventListener, Player.EventListener, VideoView {


    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.img_settings)
    ImageView imgSettings;
    @BindView(R.id.lnr_settings)
    LinearLayout lnrSettings;
    @BindView(R.id.txt_subject)
    TextView txt_subject;
    @BindView(R.id.txt_lecturename)
    TextView txt_lecturename;

    @BindView(R.id.lnr_top)
    LinearLayout lnr_top;
    @BindView(R.id.lnr_bottom)
    LinearLayout lnr_bottom;


    @BindView(R.id.tvMyNumber)
    TextView tvMyNumber;
    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.material_design_linear_scale_progress_loader)
    ProgressBar materialDesignLinearScaleProgressLoader;


    VideoActivityMVP _this;
    private static final String TAG = VideoActivityMVP.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_quiz)
    ImageView imgQuiz;
    @BindView(R.id.img_notes)
    ImageView imgNotes;
    @BindView(R.id.completed)
    ImageView completed;
    @BindView(R.id.favourite)
    ImageView favourite;
    @BindView(R.id.notes)
    ImageView notes;
    @BindView(R.id.doubts)
    ImageView doubts;
    @BindView(R.id.showLayout)
    LinearLayout showLayout;
    @BindView(R.id.img_video)
    ImageView img_video;
    @BindView(R.id.img_background)
    ImageView img_background;
    /* @BindView(R.id.rel_video)
     RelativeLayout relVideo;*/
    @BindView(R.id.img_rating_smily)
    ImageView img_rating_smily;
    @BindView(R.id.txt_ratthisvide)
    TextView txt_ratthisvide;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.edit_feedback)
    EditText editFeedback;
    @BindView(R.id.txt_submit)
    TextView txtSubmit;
    @BindView(R.id.lnr_rate_us)
    FrameLayout lnrRateUs;
    @BindView(R.id.img_close)
    ImageView img_close;
    /*@BindView(R.id.lnr_main)
    RelativeLayout lnrMain;*/
    private String start;
    private AlertDialog alertDialog;
    private SimpleExoPlayer player;
    private String lecture_id, selected_lectureid, lectureName, section_name;
    private String subjectId;
    private String lecture_audio_url = "", lecture_video_url;
    private String classid;
    private String userid;
    private String section_id;
    private String access_token;
    String loginType;
    int screenHeight;
    int img_height;
    // ArrayList<SubChapters> data;
    int selectedposition = 0;
    LinearLayoutManager linear;

    int rendererIndex = 1;
    DefaultTrackSelector trackSelector;
    HlsMediaSource mediaSource = null;
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private int mResumeWindow;
    private long mResumePosition;
    private boolean mExoPlayerFullscreen = false;
    String lecture_video_srt = "";
    boolean pos = false;
    boolean isCC_On = false;
    VideoPresentorImpl videoPresentorImpl;
    public String audio_language = "english";

    int quiz_drawable;
    int notes_drawable;
    int rating;
    boolean isTablet;
    View lnr_container;
    String selected_user_id;
    String selected_class_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this); // set screen full
        CommonUtils.secureScreen(this); // secure from screen record/capture
        setContentView(R.layout.activity_video_update);
        ButterKnife.bind(this);
        _this = VideoActivityMVP.this;
        Resources res = getResources();
        isTablet = res.getBoolean(R.bool.isTablet);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        selected_user_id=getIntent().getStringExtra("selected_user_id");
        selected_class_id=getIntent().getStringExtra("selected_class_id");

        //AppController.getInstance().playQuizAudio(R.raw.qz_next);
        lnr_container=findViewById(R.id.lnr_container);
        AppController.getInstance().startLayoutRoatateAnimation(lnr_container);
        txtSubmit.setEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.back_sound);
                onBackPressed();
            }
        });
        tvMyNumber.setVisibility(View.GONE);
        if(LANG_VIDEO_SUPPORT!=null&&LANG_VIDEO_SUPPORT.length>1)
            audio_language = SessionManager.getAudioLanguage(getApplicationContext());
        else
            audio_language = LANG_VIDEO_SUPPORT[0];
        initData();
        tvMyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    player.seekTo(0);
                    player.setPlayWhenReady(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ratingbar.setStepSize(1);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float ratings, boolean fromUser) {
                txtSubmit.setEnabled(true);
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
                        rating=0;
                        img_rating_smily.setImageResource(R.drawable.rate_1);
                        break;
                }
            }
        });

    }

    private void initData() {
        // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        linear = new LinearLayoutManager(_this, RecyclerView.VERTICAL, false);


        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        loginType = userInfo[2];

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(wm).getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            lecture_id = extras.getString(Constants.lectureId);
            lectureName = extras.getString(Constants.lectureName);
            section_name = extras.getString(Constants.sectionName);

            subjectId = extras.getString(Constants.subjectId);
            classid = extras.getString(Constants.classId);
            userid = extras.getString(Constants.userId);
            section_id = extras.getString(Constants.sectionId);
           /* data = (ArrayList<SubChapters>) extras.getSerializable("data");

            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).selectedlecture_id = lecture_id;

                    if (data.get(i).lecture_id.equalsIgnoreCase(lecture_id)) {
                        selectedposition = i;
                    }
                }
            } else {

            }*/
            int img_height = (int) convertDpToPixel(90, getApplicationContext());
            String BaseURL = "";


            if(AppConfig.checkSdcard(classid,getApplicationContext()))
            {
                BaseURL = AppConfig.getSdCardPath(classid,getApplicationContext());
            }else
            {
                BaseURL = AppConfig.getOnlineURLImage(classid);
            }



           /* if(selected_user_id!=null&&selected_user_id!=userid)
            {
                BaseURL = AppConfig.getOnlineURLImage(selected_class_id);
            }

           else if(loginType.isEmpty())
            {
                BaseURL = AppConfig.getOnlineURLImage(classid);
            }else if (loginType.equalsIgnoreCase("O")){

                if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
                {
                    BaseURL = AppConfig.getSdCardPath(classid,getApplicationContext());
                }else
                {
                    BaseURL = AppConfig.getOnlineURLImage(classid);
                }
            }else
            {
                BaseURL = AppConfig.getSdCardPath(classid,getApplicationContext());
            }*/


            String imageUrl = BaseURL;
            if (imageUrl.contains("http")) {
                if (subjectId.equals("1"))
                    imageUrl = BaseURL + "images/" + "physics/" + section_id + ".png";
                else if (subjectId.equals("2"))
                    imageUrl = BaseURL + "images/" + "chemistry/" + section_id + ".png";
                else if (subjectId.equals("3"))
                    imageUrl = BaseURL + "images/" + "biology/" + section_id + ".png";
                else if (subjectId.equals("4"))
                    imageUrl = BaseURL + "images/" + "mathematics/" + section_id + ".png";


                Picasso.with(getApplicationContext()).load(imageUrl).resize(img_height, img_height).placeholder(R.drawable.circle_default_load).into(img_video);

            } else {
                if (subjectId.equals("1"))
                    imageUrl = BaseURL + "images/" + "physics/" + section_id + ".png";
                else if (subjectId.equals("2"))
                    imageUrl = BaseURL + "images/" + "chemistry/" + section_id + ".png";
                else if (subjectId.equals("3"))
                    imageUrl = BaseURL + "images/" + "biology/" + section_id + ".png";
                else if (subjectId.equals("4"))
                    imageUrl = BaseURL + "images/" + "mathematics/" + section_id + ".png";


                Uri uri = Uri.fromFile(new File(imageUrl));
                Picasso.with(getApplicationContext()).load(uri).resize(img_height, img_height).placeholder(R.drawable.circle_default_load).into(img_video);


            }

            //Picasso.with(getApplicationContext()).load("https://d2vgb5tug4mj1f.cloudfront.net/class6/images/physics/1.png").into(img_video);
            txt_lecturename.setText(lectureName);

            if (subjectId.equalsIgnoreCase("1")) {


                Glide.with(getApplicationContext()).load(R.drawable.ic_video_phy_background).into(img_background);
                //lnr_main.setBackgroundResource(R.drawable.ic_video_phy_background);
                txt_subject.setText(getString(R.string.physics));
                txt_lecturename.setTextColor(getResources().getColor(R.color.phy_background));
                imgQuiz.setImageResource(R.drawable.ic_video_phy_quiz);
                imgNotes.setImageResource(R.drawable.ic_video_phy_notes);
                txt_ratthisvide.setTextColor(getResources().getColor(R.color.phy_background));
                txtSubmit.setBackgroundResource(R.drawable.quiz_quit_btn_phy);
            } else if (subjectId.equalsIgnoreCase("2")) {

                txt_subject.setText(getString(R.string.chemistry));
                Glide.with(getApplicationContext()).load(R.drawable.ic_video_che_background).into(img_background);
                // lnr_main.setBackgroundResource(R.drawable.ic_video_che_background);
                txt_lecturename.setTextColor(getResources().getColor(R.color.che_background));
                imgQuiz.setImageResource(R.drawable.ic_video_che_quiz);
                imgNotes.setImageResource(R.drawable.ic_video_che_notes);
                txt_ratthisvide.setTextColor(getResources().getColor(R.color.che_background));
                txtSubmit.setBackgroundResource(R.drawable.quiz_quit_btn_che);
            } else if (subjectId.equalsIgnoreCase("3")) {

                txt_subject.setText(getString(R.string.biology));
                // lnr_main.setBackgroundResource(R.drawable.ic_video_bio_background);
                Glide.with(getApplicationContext()).load(R.drawable.ic_video_bio_background).into(img_background);
                txt_lecturename.setTextColor(getResources().getColor(R.color.bio_background));
                imgQuiz.setImageResource(R.drawable.ic_video_bio_quiz);
                imgNotes.setImageResource(R.drawable.ic_video_bio_notes);
                txt_ratthisvide.setTextColor(getResources().getColor(R.color.bio_background));
                txtSubmit.setBackgroundResource(R.drawable.quiz_quit_btn_bio);

            } else {

                txt_subject.setText(getString(R.string.mathematics));
                //lnr_main.setBackgroundResource(R.drawable.ic_video_math_background);
                Glide.with(getApplicationContext()).load(R.drawable.ic_video_math_background).into(img_background);
                txt_lecturename.setTextColor(getResources().getColor(R.color.math_background));
                imgQuiz.setImageResource(R.drawable.ic_video_math_quiz);
                imgNotes.setImageResource(R.drawable.ic_video_math_notes);
                txt_ratthisvide.setTextColor(getResources().getColor(R.color.math_background));
                txtSubmit.setBackgroundResource(R.drawable.quiz_quit_btn_math);
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
            videoPresentorImpl = new VideoPresentorImpl(this, VideoActivityMVP.this, classid, subjectId, userid);

            proceedData();
        }
    }

    private void proceedData() {

        if(AppConfig.checkSdcard(classid,getApplicationContext()))
        {
            //lnrSettings.setVisibility(View.VISIBLE);
            if (checkPermissionforstorage()) {
                String sdCardpath="";
                if ((sdCardpath=AppConfig.getSdCardPath(classid,getApplicationContext()) )!= null) {
                    lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                    lecture_video_srt = sdCardpath+ subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;

                    loadData(false);
                } else {
                    finish();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));

                }
            } else {
                requestPermissionforstorage();
            }
        }else
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                lecture_video_url = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                lecture_video_srt = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                loadData(true);
                lnrSettings.setVisibility(View.GONE);
            } else {
                finish();
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

            }
        }


     /*
        if(selected_user_id!=null&&selected_user_id!=userid)
        {
            lecture_video_url = AppConfig.getOnlineURLVideo(selected_class_id) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
            lecture_video_srt = AppConfig.getOnlineURLVideo(selected_class_id) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
            loadData(true);
            lnrSettings.setVisibility(View.GONE);
        }else
        if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                lecture_video_url = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                lecture_video_srt = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                loadData(true);
                lnrSettings.setVisibility(View.GONE);
            } else {
                finish();
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
            {
                //lnrSettings.setVisibility(View.VISIBLE);
                if (checkPermissionforstorage()) {
                    String sdCardpath="";
                    if ((sdCardpath=AppConfig.getSdCardPath(classid,getApplicationContext()) )!= null) {
                        lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                        lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                        lecture_video_srt = sdCardpath+ subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;

                        loadData(false);
                    } else {
                        finish();
                        CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));

                    }
                } else {
                    requestPermissionforstorage();
                }
            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    lecture_video_url = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_video_srt = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                    loadData(true);
                    lnrSettings.setVisibility(View.GONE);
                } else {
                    finish();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

                }
            }
        }else
        {
            //lnrSettings.setVisibility(View.VISIBLE);
            if (checkPermissionforstorage()) {
                String sdCardpath="";
                if ((sdCardpath=AppConfig.getSdCardPath(classid,getApplicationContext()) )!= null) {
                    lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                    lecture_video_srt = sdCardpath+ subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;

                    loadData(false);
                } else {
                    finish();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));

                }
            } else {
                requestPermissionforstorage();
            }
        }*/


       /* if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                lecture_video_url = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                lecture_video_srt = AppConfig.getOnlineURLVideo(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                loadData(true);
                lnrSettings.setVisibility(View.GONE);
            } else {
                finish();
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

            }

        } else {
            lnrSettings.setVisibility(View.VISIBLE);
            if (checkPermissionforstorage()) {
                String sdCardpath="";
                if ((sdCardpath=AppConfig.getSdCardPath(classid) )!= null) {
                    lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                    lecture_video_srt = sdCardpath+ subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;

                    loadData(false);
                } else {
                    finish();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));

                }
            } else {
                requestPermissionforstorage();
            }
        }*/
    }

    //TextView txt_audio_track;
    View exo_settings;
    View exo_full;
    Button exo_speed;
    private void loadData(boolean isOnline) {

        simpleExoPlayerView.requestFocus();
        PlaybackControlView controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
        exo_settings = controlView.findViewById(R.id.exo_settings);
        exo_full = controlView.findViewById(R.id.exo_full);
        exo_speed = controlView.findViewById(R.id.exo_speed);
        exo_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpeed(v);
                //showTracksInfo();
            }
        }); exo_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTracksInfo(v);
            }
        });
       // final ImageView exo_cc_icon = controlView.findViewById(R.id.exo_cc_icon);
        //txt_audio_track = controlView.findViewById(R.id.txt_audio_track);
       // ImageView exo_full = controlView.findViewById(R.id.exo_full);
       /* txt_audio_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
                    showPopup(v);
                    return;
                }
                setVDOPopupWindow(v);*//*




                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = ((DefaultTrackSelector) trackSelector).getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {


                    for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                        TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
                        if (trackGroups.length != 0) {
                            Button button = new Button(_this);
                            int label;
                            switch (player.getRendererType(i)) {
                                case C.TRACK_TYPE_AUDIO:
                                    label = R.string.exo_track_selection_title_audio;
                                    break;
                                case C.TRACK_TYPE_VIDEO:
                                    label = R.string.exo_track_selection_title_video;
                                    break;
                                case C.TRACK_TYPE_TEXT:
                                    label = R.string.exo_track_selection_title_text;
                                    break;
                                default:
                                    continue;
                            }


                        }
                    }


                    int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
                    boolean allowAdaptiveSelections =
                            rendererType == C.TRACK_TYPE_VIDEO
                                    || (rendererType == C.TRACK_TYPE_AUDIO
                                    && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);
                    isShowingTrackSelectionDialog=true;
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForTrackSelector(
                                    trackSelector,
                                    *//* onDismissListener= *//* dismissedDialog -> isShowingTrackSelectionDialog = false);
                    trackSelectionDialog.show(getSupportFragmentManager(), *//* tag= *//* null);

                }


            }
        });*/
        //TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter.Builder().setInitialBitrateEstimate(320000).build());
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();

        //videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER, minDurationForQualityIncreaseMs, minDurationForQualityDecreaseMs, minDurationToRetainAfterDiscardMs, bandwithFraction);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        DefaultTrackSelector.ParametersBuilder builder=new DefaultTrackSelector.ParametersBuilder();


                builder .setRendererDisabled(C.TRACK_TYPE_VIDEO, false);
       /* if(AppController.getInstance().builder==null) {
            ((DefaultTrackSelector) trackSelector).setParameters(new DefaultTrackSelector.ParametersBuilder()
                    .setRendererDisabled(C.TRACK_TYPE_VIDEO, false)
                    .build()
            );
        }else
        {*/

       try {
           if (AppController.getInstance().childQaulity == 0)
               builder.setMaxVideoSize(640, 360);
           else if (AppController.getInstance().childQaulity == 1)
               builder.setMaxVideoSize(854, 480);
           else if (AppController.getInstance().childQaulity == 2)
               builder.setMaxVideoSize(1280, 720);

           if (AppController.getInstance().childQaulityAudioVideo == 0)
               builder.setPreferredAudioLanguage("EN");
           else if (AppController.getInstance().childQaulityAudioVideo == 1)
               builder.setPreferredAudioLanguage("HI");

           if (AppController.getInstance().childQaulityText == 1)
               builder.setPreferredTextLanguage("EN");
           else builder.setDisabledTextTrackSelectionFlags(C.TRACK_TYPE_TEXT);
           ((DefaultTrackSelector) trackSelector).setParameters(builder.build());
       }catch (Exception e){
           e.printStackTrace();
           ((DefaultTrackSelector) trackSelector).setParameters(builder.build());
       }


     //   }

        exo_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orientation = getResources().getConfiguration().orientation;

                switch (orientation) {

                    case Configuration.ORIENTATION_LANDSCAPE:

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        break;

                    case Configuration.ORIENTATION_PORTRAIT:

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                        break;
                }
            }
        });


       /* exo_cc_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!isCC_On) {
                    exo_cc_icon.setImageResource(R.drawable.ic_cc_close);
                    ((DefaultTrackSelector) trackSelector).setRendererDisabled(C.TRACK_TYPE_VIDEO, true);
                    isCC_On = true;
                } else {
                    isCC_On = false;
                    exo_cc_icon.setImageResource(R.drawable.ic_cc_open);
                    ((DefaultTrackSelector) trackSelector).setRendererDisabled(C.TRACK_TYPE_VIDEO, false);
                }
            }
        });*/

        player = ExoPlayerFactory.newSimpleInstance(_this, trackSelector, loadControl);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        player.setRepeatMode(Player.REPEAT_MODE_OFF);

        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.setPlayer(player);
        Typeface subtitleTypeface = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
        CaptionStyleCompat style =
                new CaptionStyleCompat(getResources().getColor(R.color.white),getResources().getColor(R.color.subtitle_trans),  Color.TRANSPARENT,
                        CaptionStyleCompat.EDGE_TYPE_NONE,
                        getResources().getColor(R.color.white), subtitleTypeface);

        //simpleExoPlayerView.setPadding(20,20,20,20);
        simpleExoPlayerView.getSubtitleView().setStyle(style);


        if (isOnline) {
            videoPresentorImpl.checkCookeAndPlay(lecture_video_url);
        } else {
            initForLocalFile(lecture_video_url);

        }
        player.addListener(this);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            lnr_top.setVisibility(View.VISIBLE);
            lnr_bottom.setVisibility(View.VISIBLE);
            if(isTablet)
                simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight / 3));
            else simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,/*(int) convertDpToPixel(200f,getApplicationContext())*/screenHeight / 3));

            //  simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) convertDpToPixel(200f,getApplicationContext())));

        } else {
            lnr_top.setVisibility(View.GONE);
            lnr_bottom.setVisibility(View.GONE);
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }

        setSpeed();
    }
    boolean isShowingTrackSelectionDialog;
    private void moveToPosition() {
        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(_this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        smoothScroller.setTargetPosition(selectedposition);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linear.startSmoothScroll(smoothScroller);
            }
        }, 200);
    }

    private boolean checkPermissionforstorage() {
        if(Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
                return  false;
            } else return true;
        }else {
            int  result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissionforstorage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 300:
                if (grantResults.length > 0) {
                    boolean galleryaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (galleryaccepted) {

                        String sdCardpath="";
                        if ((sdCardpath=AppConfig.getSdCardPath(classid,getApplicationContext())) != null) {
                            lecture_video_url = sdCardpath+ subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                            lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                            lecture_video_srt =sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                            loadData(false);
                        } else {
                            finish();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));

                        }
                    } else {
                        finish();
                        CommonUtils.showToast(getApplicationContext(), "Access Required");

                    }
                }

                break;
            default:
                break;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppController.getInstance().playAudio(R.raw.back_sound);
        return false;
    }

    int current_color;


    private void playVideo() {

        findViewById(R.id.material_design_linear_scale_progress_loader).setVisibility(View.VISIBLE);
        Uri mp4VideoUri = Uri.parse(lecture_video_url);
        String userAgent = Util.getUserAgent(this, "");
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                1800000,
                true);
       /* DefaultHlsExtractorFactory defaultHlsExtractorFactory =
                new DefaultHlsExtractorFactory(FLAG_ALLOW_NON_IDR_KEYFRAMES,true);*/
        /*mediaSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, DEFAULT_MIN_LOADABLE_RETRY_COUNT,
                null, null);*/
        mediaSource= new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);
        //For Playing SRT Files
        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);


        MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(lecture_video_srt), textFormat, C.TIME_UNSET);
        //Log.v("lecture_video_srt ","lecture_video_srt "+lecture_video_srt);
        concatenatedSource = new MergingMediaSource(mediaSource, textMediaSource);


        player.prepare(concatenatedSource);
        player.setPlayWhenReady(true);
        simpleExoPlayerView.setPlaybackPreparer(new PlaybackPreparer() {
            @Override
            public void preparePlayback() {
                if(player!=null)
                player.retry();
            }
        });
    }
    MergingMediaSource concatenatedSource;
    public void initForLocalFile(String uri) {


        File file = new File(uri);
        if (!file.exists()) {
            CommonUtils.showToast(getApplicationContext(), "File not available");
            finish();
            return;
        }
        //-----------------------------------Video Source--->>>>>------------------------------------------

        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        final SDCardDataSource fileDataSource = new SDCardDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSource;
            }
        };

        /*MediaSource videoSource = new HlsMediaSource(fileDataSource.getUri(), factory, 1800000,
                new Handler(), null);*/
        MediaSource videoSource = new HlsMediaSource.Factory(factory).createMediaSource(fileDataSource.getUri());
        //-----------------------------------Audi Source--->>>>>------------------------------------------


        DataSpec dataSpecAudi = new DataSpec(Uri.parse(lecture_audio_url));
        final FileDataSource fileDataSourceAudi = new FileDataSource();
        try {
            fileDataSourceAudi.open(dataSpecAudi);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factoryAudi = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSourceAudi;
            }
        };
       /* MediaSource audioSource = new HlsMediaSource(fileDataSourceAudi.getUri(), factoryAudi, 1800000,
                new Handler(), null);*/
        MediaSource audioSource = new HlsMediaSource.Factory(factoryAudi).createMediaSource(fileDataSourceAudi.getUri());

        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);


        String userAgent = Util.getUserAgent(this, "");
        DataSource.Factory dataSourceFactory = new FileDataSourceFactory();

        MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(lecture_video_srt), textFormat, C.TIME_UNSET);


        concatenatedSource = new MergingMediaSource(videoSource, audioSource, textMediaSource);
        // concatenatedSource = new MergingMediaSource(videoSource, audioSource);
        player.prepare(concatenatedSource);
        player.setPlayWhenReady(true);


      //  isConnected(getApplicationContext());
    }

    boolean isCheckedUSb;

    public boolean isConnected(Context context) {
        isCheckedUSb = true;
        Intent intent = context.registerReceiver(mUsbAttachReceiver, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
    }


    @Override
    public void showLoading() {
        CustomDialog.showDialog(_this, true);
    }

    @Override
    public void hideLoading() {
        CustomDialog.closeDialog();
    }

    @Override
    public void showMessage(String msh, boolean hasToFinish) {
        CommonUtils.showToast(_this, msh);
        if (hasToFinish)
            finish();
    }

    @Override
    public void parseResultData(JSONObject obj) {

    }

    @Override
    public void play() {
        playVideo();
    }


    /* For USB CONNECTION CHECK*/
    BroadcastReceiver mUsbAttachReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.getExtras().getBoolean("connected")) {
                //showDevices();

                try {
                    //  player.stop();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                CommonUtils.showToast(getApplicationContext(), "Please remove your USB Connection");
                finish();
            } else {

            }
        }
    };


    //Video Event Listners

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        //Log.v("OnVideo Size", "OnVideo Size " + width + " " + height);
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }


    //Player Event Listners


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        try {
           // if(trackSelections.get(rendererIndex).getSelectedFormat()!=null&&trackSelections.get(rendererIndex).getSelectedFormat().language!=null)
               // txt_audio_track.setText(trackSelections.get(rendererIndex).getSelectedFormat().language.toUpperCase());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                findViewById(R.id.material_design_linear_scale_progress_loader).setVisibility(View.VISIBLE);
                checkDisplayCount();
                break;
            case Player.STATE_ENDED:
                //dialoag();
                //tvMyNumber.setVisibility(View.VISIBLE);

                onEndVideoOrPause();
                break;
            case Player.STATE_IDLE:
                break;
            case Player.STATE_READY:
                tvMyNumber.setVisibility(View.GONE);
                if (!alreadyExecuted) {
                    //updateProgressBar();
                    //alreadyExecuted = true;
                }

                lineScaleLoaderExample_disable();
                if(exo_settings!=null)
                exo_settings.setVisibility(View.VISIBLE);
                if(exo_speed!=null)
                    exo_speed.setVisibility(View.VISIBLE);
                break;


            default:
                break;
        }
    }
    DecimalFormat formatter = new DecimalFormat("00");
    private void onEndVideoOrPause() {
        try{


        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String end = dateFormat.format(date);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateFormat.parse(start);
            date2 = dateFormat.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
        if(difference<0)
            difference=-difference;
        difference=difference+video_millese;
        long diffSeconds = difference / 1000 % 60;
        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        //Log.d("difference", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);
            if(diffHours>=1)
            {
                diffHours=0;
                diffMinutes=59;
                diffSeconds=0;
            }
            String a = formatter.format(diffHours) + ":" + formatter.format(diffMinutes) + ":" + formatter.format(diffSeconds);
        if(diffHours>0||diffMinutes>0||diffSeconds>=30)
            postTrackToServer(a, false,false, player.getCurrentPosition());
        video_millese=0;
        date = new Date();
        start = dateFormat.format(date);
        tvMyNumber.setVisibility(View.VISIBLE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void lineScaleLoaderExample_disable() {
        findViewById(R.id.material_design_linear_scale_progress_loader).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }


    @Override
    public void onPlayerError(ExoPlaybackException error) {


        player.stop();
        // player.release();
        player.prepare(concatenatedSource);
        player.setPlayWhenReady(true);
    }


    @Override
    public void onPositionDiscontinuity(int reason) {
    }


    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        start = dateFormat.format(date);
        checkDisplayCount();



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        start = dateFormat.format(date);
        checkDisplayCount();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDisplayCount();
    }

    private void pausePlayer() {
        if (player == null)
            return;


        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    //Handler handlerGONE = new Handler();
    //Handler handlerVISIBLE = new Handler();
    boolean alreadyExecuted = false;

    long video_millese;
    @Override
    protected void onPause() {

        pausePlayer();
        super.onPause();
        timeCal();

        //handlerVISIBLE.removeCallbacks(updateVISIBLE);
        // handlerGONE.removeCallbacks(updateGONE);
        //tvMyNumber.setVisibility(View.GONE);
        // alreadyExecuted = false;


    }

    private void timeCal()
    {
        try{
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String end = dateFormat.format(date);
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = dateFormat.parse(start);
                date2 = dateFormat.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();

            video_millese=video_millese+difference;
            //Log.v("video_millese","video_millese "+video_millese);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        pausePlayer();
        super.onStop();
        //timeCal();
        // handlerVISIBLE.removeCallbacks(updateVISIBLE);
        // handlerGONE.removeCallbacks(updateGONE);
        //tvMyNumber.setVisibility(View.GONE);
        //alreadyExecuted = false;


        /*if(dm!=null&&lt!=null)
            dm.unregisterDisplayListener(lt);*/
    }

    @Override
    public void onBackPressed() {
        //
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        long position=0;
        try {
            if (player != null) {
                position = player.getCurrentPosition();
                player.release();
                player.clearVideoSurface();
                player.clearAuxEffectInfo();
                concatenatedSource = null;
                trackSelector = null;
                player = null;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onBackPressed();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String end = dateFormat.format(date);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateFormat.parse(start);
            date2 = dateFormat.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
        // Log.v("video_millese","video_millese 2 "+video_millese);
        if(difference<0)
            difference=-difference;
        difference=difference+video_millese;
        //Log.v("video_millese","video_millese 3 "+video_millese);
        long diffSeconds = difference / 1000 % 60;
        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        //Log.d("difference", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);
        if(diffHours>=1)
        {
            diffHours=0;
            diffMinutes=59;
            diffSeconds=0;
        }
        String a = formatter.format(diffHours) + ":" +formatter.format( diffMinutes) + ":" + formatter.format(diffSeconds);
        if(diffHours>0||diffMinutes>0||diffSeconds>=30)
            postTrackToServer(a, true,false,position);
        video_millese=0;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null)
            player.release();
        try {
            if (mUsbAttachReceiver != null && isCheckedUSb)
                getApplicationContext().unregisterReceiver(mUsbAttachReceiver);

            /*if(dm!=null&&lt!=null)
                dm.unregisterDisplayListener(lt);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProgressBar() {
        //handlerVISIBLE.removeCallbacks(updateVISIBLE);
        //handlerGONE.removeCallbacks(updateGONE);
        int width = simpleExoPlayerView.getWidth() - tvMyNumber.getWidth();
        int height = simpleExoPlayerView.getHeight() - tvMyNumber.getHeight();
        tvMyNumber.setX(new Random().nextInt(width));
        tvMyNumber.setY(new Random().nextInt(height));

        tvMyNumber.setVisibility(View.VISIBLE);

        //handlerVISIBLE.postDelayed(updateVISIBLE, 3000);
    }

    private final Runnable updateVISIBLE = new Runnable() {
        @Override
        public void run() {
            int playbackState = player == null ? Player.STATE_IDLE : player.getPlaybackState();
            if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
                tvMyNumber.setVisibility(View.GONE);

                // handlerGONE.postDelayed(updateGONE, 15000);
            }
        }
    };

    private final Runnable updateGONE = new Runnable() {
        @Override
        public void run() {
            updateProgressBar();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            lnr_top.setVisibility(View.GONE);
            lnr_bottom.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lnr_top.setVisibility(View.VISIBLE);
            lnr_bottom.setVisibility(View.VISIBLE);
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            Objects.requireNonNull(wm).getDefaultDisplay().getMetrics(displayMetrics);
            screenHeight = displayMetrics.heightPixels;
            if(isTablet)
                simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight / 3));
            else simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,/*(int) convertDpToPixel(200f,getApplicationContext()*/screenHeight / 3));
        }
    }



    private void postTrackToServer(final String a, final boolean typeRelease,final boolean replay,long position) {


        long diffSeconds = position / 1000 % 60;
        long diffMinutes = position / (60 * 1000) % 60;
        long diffHours = position / (60 * 60 * 1000) % 24;
        final String videoDuration = diffHours + ":" + diffMinutes + ":" + diffSeconds;
        //Log.d("minutes", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userid);
            fjson.put("activity_type", "V");
            fjson.put("subject_id", subjectId);
            fjson.put("activity_duration", a);
            fjson.put("lecture_id", lecture_id);
            fjson.put(Constants.lectureName, lectureName);
            fjson.put("section_id", section_id);
            if(selected_user_id!=null&&selected_user_id!=userid)
            {
                fjson.put(Constants.classId, selected_class_id);
            }else
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("video_duration", videoDuration);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;


        //Updated by chandu Need to verify
        // if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            StringRequest strReq = new StringRequest(Request.Method.POST, Constants.USER_TRACK, new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(String response) {
                    //Log.d(Constants.response, response);
                    try {

                        JSONObject jObj = new JSONObject(response);
                        //Log.d(Constants.response, response);
                        boolean error = jObj.getBoolean(Constants.errorFlag);


                        if(loginType.isEmpty())
                        {

                        }else if (loginType.equalsIgnoreCase("O")){

                            if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
                            {
                                MyDatabase database = MyDatabase.getDatabase(_this);
                                try {
                                    TrackModel chapters = new TrackModel();
                                    chapters.activity_type = "V";
                                    chapters.activity_duration = a;
                                    chapters.activity_date = getDateTime();
                                    chapters.quiz_id = "";
                                    chapters.subject_id = subjectId;
                                    chapters.section_id = section_id;
                                    chapters.lecture_id = fjson.getString("lecture_id");
                                    chapters.class_id = classid;
                                    chapters.user_id = userid;
                                    chapters.videoduratrion = videoDuration;

                                    chapters.lecture_name = lectureName;

                                    chapters.is_sync = true;
                                    chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                                    database.trackDAO().insertTrack(chapters);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else
                            {

                            }
                        }else
                        {
                            MyDatabase database = MyDatabase.getDatabase(_this);
                            try {
                                TrackModel chapters = new TrackModel();
                                chapters.activity_type = "V";
                                chapters.activity_duration = a;
                                chapters.activity_date = getDateTime();
                                chapters.quiz_id = "";
                                chapters.subject_id = subjectId;
                                chapters.section_id = section_id;
                                chapters.lecture_id = fjson.getString("lecture_id");
                                chapters.class_id = classid;
                                chapters.user_id = userid;
                                chapters.videoduratrion = videoDuration;

                                chapters.lecture_name = lectureName;

                                chapters.is_sync = true;
                                chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                                database.trackDAO().insertTrack(chapters);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                       /* if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()){

                        }else
                        {
                            MyDatabase database = MyDatabase.getDatabase(_this);
                            try {
                                TrackModel chapters = new TrackModel();
                                chapters.activity_type = "V";
                                chapters.activity_duration = a;
                                chapters.activity_date = getDateTime();
                                chapters.quiz_id = "";
                                chapters.subject_id = subjectId;
                                chapters.section_id = section_id;
                                chapters.lecture_id = fjson.getString("lecture_id");
                                chapters.class_id = classid;
                                chapters.user_id = userid;
                                chapters.videoduratrion = videoDuration;

                                chapters.lecture_name = lectureName;

                                chapters.is_sync = true;
                                chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                                database.trackDAO().insertTrack(chapters);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (typeRelease) {
                            //finish();
                        }*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //CommonUtils.showToast(getApplicationContext(), getString(R.string.there_is_error));
                    if (typeRelease) {
                        //finish();
                    }
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
            //if (loginType.equalsIgnoreCase("O") || loginType.isEmpty())
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            /*else
            {
                MyDatabase database = MyDatabase.getDatabase(_this);
                try {

                    TrackModel chapters = new TrackModel();
                    chapters.activity_type = "V";
                    chapters.activity_duration = a;
                    chapters.activity_date = getDateTime();
                    chapters.quiz_id = "";
                    chapters.subject_id = subjectId;
                    chapters.section_id = section_id;
                    chapters.lecture_id = fjson.getString("lecture_id");
                    chapters.class_id = classid;
                    chapters.user_id = userid;
                    chapters.videoduratrion = videoDuration;

                    chapters.lecture_name = lectureName;

                    chapters.is_sync = false;
                    chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                    database.trackDAO().insertTrack(chapters);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (typeRelease) {
                    // finish();
                }
            }*/
        } else {
            MyDatabase database = MyDatabase.getDatabase(_this);
            try {

                TrackModel chapters = new TrackModel();
                chapters.activity_type = "V";
                chapters.activity_duration = a;
                chapters.activity_date = getDateTime();
                chapters.quiz_id = "";
                chapters.subject_id = subjectId;
                chapters.section_id = section_id;
                chapters.lecture_id = fjson.getString("lecture_id");
                chapters.class_id = classid;
                chapters.user_id = userid;
                chapters.videoduratrion = videoDuration;

                chapters.lecture_name = lectureName;

                chapters.is_sync = false;
                chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                database.trackDAO().insertTrack(chapters);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (typeRelease) {
                // finish();
            }

        }
    }

    private String getDateTime() {
        Date date = new Date();
        return CommonUtils.format.format(date);
    }


    public void menuClick(View view) {
        //Log.v("IsOnline", "IsOnline " + loginType);
        if ( loginType.isEmpty())
        {
            showPopup(view);

            return;
        }

        if(loginType.equalsIgnoreCase("O") )
        {

            if(AppConfig.checkSDCardEnabled(_this,userid,classid))
            {

                setVDOPopupWindow(view);
            }else
            {
                showPopup(view);

            }
            return;
        }
        setVDOPopupWindow(view);



    }

    private void setSpeed()
    {
        switch (AppController.getInstance().SpeedControll)
        {

            case 0:
                speedparams=new PlaybackParameters(.75f,1f,false);

                player.setPlaybackParameters(speedparams);
                exo_speed.setText(".75X");
                break;
            case 1:
                speedparams=new PlaybackParameters(1f,1f);

                player.setPlaybackParameters(speedparams);
                exo_speed.setText("1X");
                break;
            case 2:
                speedparams=new PlaybackParameters(1.25f,1f,true);
                player.setPlaybackParameters(speedparams);
                exo_speed.setText("1.25X");
                break;
            case 3:
                speedparams=new PlaybackParameters(1.5f,1f,true);
                player.setPlaybackParameters(speedparams);
                exo_speed.setText("1.5X");
                break;
        }
    }
    PopupMenu popupMenu;
    PlaybackParameters speedparams;
    private void showSpeed(View v)
    {
        if(popupMenu==null)
        {
            popupMenu=new PopupMenu(VideoActivityMVP.this,v);

            Menu menu1=popupMenu.getMenu();
            menu1.add("Slow");
            menu1.add("Normal");
            menu1.add("Fast");
            menu1.add("Faster");


        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                if(item.getTitle().equals("Slow"))
                {
                    speedparams=new PlaybackParameters(.75f,1f,false);

                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText(".75X");
                    AppController.getInstance().SpeedControll=0;
                }else  if(item.getTitle().equals("Normal"))
                {
                    speedparams=new PlaybackParameters(1f,1f);

                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText("1X");
                    AppController.getInstance().SpeedControll=1;
                }else  if(item.getTitle().equals("Fast"))
                {
                    speedparams=new PlaybackParameters(1.25f,1f,true);
                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText("1.25X");
                    AppController.getInstance().SpeedControll=2;
                }else  if(item.getTitle().equals("Faster"))
                {
                    speedparams=new PlaybackParameters(1.5f,1f,true);
                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText("1.5X");
                    AppController.getInstance().SpeedControll=3;
                }


                return false;
            }
        });
    }
    PopupWindow popupWindow;

    private void showPopup(View v) {
        if(true)
        {
            showTracksInfo(v);
            return;
        }
        if (popupWindow == null) {
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.popup_menu, null);

            TextView txt_phy = view.findViewById(R.id.txt_phy);
            TextView txt_che = view.findViewById(R.id.txt_che);
            TextView txt_bio = view.findViewById(R.id.txt_bio);
            TextView txt_math = view.findViewById(R.id.txt_math);
            TextView txt_all = view.findViewById(R.id.txt_all);
            txt_math.setVisibility(View.GONE);
            txt_che.setVisibility(View.GONE);
            txt_bio.setVisibility(View.GONE);
            txt_all.setVisibility(View.GONE);
            txt_phy.setText("Quality");
            txt_che.setText("Video");

            popupWindow = new PopupWindow(
                    view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);

            txt_phy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();

                    showSpeed(view);
                    //showTracksInfo();

                }
            });
            txt_che.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow.dismiss();

                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = ((DefaultTrackSelector) trackSelector).getCurrentMappedTrackInfo();
                    if (mappedTrackInfo != null) {


                        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
                            if (trackGroups.length != 0) {

                                int label;
                                switch (player.getRendererType(i)) {
                                    case C.TRACK_TYPE_AUDIO:
                                        label = R.string.exo_track_selection_title_audio;
                                        break;
                                    case C.TRACK_TYPE_VIDEO:
                                        label = R.string.exo_track_selection_title_video;
                                        break;
                                    case C.TRACK_TYPE_TEXT:
                                        label = R.string.exo_track_selection_title_text;
                                        break;
                                    default:
                                        continue;
                                }


                            }
                        }


                        int rendererType = mappedTrackInfo.getRendererType(0);
                        boolean allowAdaptiveSelections =
                                rendererType == C.TRACK_TYPE_VIDEO
                                        || (rendererType == C.TRACK_TYPE_AUDIO
                                        && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                        == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);
                        isShowingTrackSelectionDialog=true;
                        TrackSelectionDialog trackSelectionDialog =
                                TrackSelectionDialog.createForTrackSelector(
                                        trackSelector,
                                        /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                        trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

                    }


                }
            });

        }
        popupWindow.showAsDropDown(v);
    }

    PopupWindow window;

    private void showTracksInfo(View view)
    {

        if(loginType.equalsIgnoreCase("O") )
        {

            if(AppConfig.checkSDCardEnabled(_this,userid,classid)&&AppConfig.checkSdcard(classid,getApplicationContext()))
            {

                setVDOPopupWindow(view);
                return;
            }
        }


        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = ((DefaultTrackSelector) trackSelector).getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {


            for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
                if (trackGroups.length != 0) {
                    Button button = new Button(_this);
                    int label;
                    switch (player.getRendererType(i)) {
                        case C.TRACK_TYPE_AUDIO:
                            label = R.string.exo_track_selection_title_audio;
                            break;
                        case C.TRACK_TYPE_VIDEO:
                            label = R.string.exo_track_selection_title_video;
                            break;
                        case C.TRACK_TYPE_TEXT:
                            label = R.string.exo_track_selection_title_text;
                            break;
                        default:
                            continue;
                    }


                }
            }


            int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
            boolean allowAdaptiveSelections =
                    rendererType == C.TRACK_TYPE_VIDEO
                            || (rendererType == C.TRACK_TYPE_AUDIO
                            && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);
             isShowingTrackSelectionDialog=true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

        }
    }

    PopupMenu langagePopup;
    private void setVDOPopupWindow(View v) {




        if(langagePopup==null)
        {
            langagePopup=new PopupMenu(VideoActivityMVP.this,v);

            Menu menu1=langagePopup.getMenu();

            for (int k = 0; k < LANG_VIDEO_SUPPORT.length; k++) {

                menu1.add(LANG_VIDEO_SUPPORT[k]);
            }




        }

        langagePopup.show();

        langagePopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                SessionManager.setAudioLanguage(getApplicationContext(), item.getTitle().toString());
                audio_language = SessionManager.getAudioLanguage(getApplicationContext());
                lecture_audio_url = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";


                player.release();
                loadData(false);

                return false;
            }
        });



    }

    Intent i;

    @OnClick({R.id.img_quiz,R.id.img_close,R.id.lnr_rate_us, R.id.img_notes, R.id.completed, R.id.favourite, R.id.notes, R.id.doubts, R.id.img_filter, R.id.img_settings, R.id.lnr_settings,R.id.txt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_quiz:
                onEndVideoOrPause();
                AppController.getInstance().startItemAnimation(view);
                i = new Intent(getApplicationContext(), QuizRulesActivity.class);
                i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Constants.lectureId, lecture_id);
                i.putExtra(Constants.sectionName, section_name);
                i.putExtra(Constants.subjectId, subjectId);
                i.putExtra(Constants.classId, classid);
                i.putExtra(Constants.userId, userid);
                i.putExtra(Constants.sectionId, section_id);
                i.putExtra(Constants.lectureName, lectureName);
                startActivity(i);
                // mContext = (Activity) vh.itemView.getContext();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.button_click);
                break;
            case R.id.img_notes:
                onEndVideoOrPause();
                AppController.getInstance().startItemAnimation(view);
                i = new Intent(getApplicationContext(), NotesActivity.class);
                i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Constants.lectureId, lecture_id);
                i.putExtra(Constants.sectionName, section_name);
                i.putExtra(Constants.subjectId, subjectId);
                i.putExtra(Constants.classId, classid);
                i.putExtra(Constants.userId, userid);
                i.putExtra(Constants.sectionId, section_id);
                i.putExtra(Constants.lectureName, lectureName);
                startActivity(i);
                //mContext = (Activity) vh.itemView.getContext();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.button_click);

                break;
            case R.id.completed:
                break;
            case R.id.favourite:
                break;
            case R.id.notes:
                break;
            case R.id.doubts:
                break;
            case R.id.img_filter:
                //lnrRateUs.setVisibility(View.VISIBLE);
                if (!AppStatus.getInstance(getApplicationContext()).isOnline())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please connect internet!");
                    return;
                }
                AppController.getInstance().playAudio(R.raw.alert_audio);
                slideUp(lnrRateUs);
                break;
            case R.id.img_settings:
            case R.id.lnr_settings:
                menuClick(view);
                break;
            case R.id.txt_submit:

                //lnrRateUs.setVisibility(View.GONE);
                AppController.getInstance().playAudio(R.raw.button_click);
                if(rating==0)
                {
                    CommonUtils.showToast(getApplicationContext(),"Please add your rating");
                    return;
                }
                slideDown(lnrRateUs);
                sendReport( editFeedback.getText().toString().trim());
                break;
            case R.id.img_close:
            case R.id.lnr_rate_us:
                slideDown(lnrRateUs);
                //  lnrRateUs.setVisibility(View.GONE);
                break;
        }
    }


    long scrrenOffTime = 0;

    public static float convertDpToPixel(float dp, Context context) {

        int dencity = context.getResources().getDisplayMetrics().densityDpi;
        if (dencity == DisplayMetrics.DENSITY_DEFAULT)
            return 120;
        float height = dp * (dencity / DisplayMetrics.DENSITY_DEFAULT);
        //Log.v("height","height density "+height);

      /*  if(height>200)
           height=270;*/

        //Log.v("height","height density "+height);
        if(height<=0)
            height=120;
        return height;
    }


    public void sendReport(String feedback)
    {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userid);
            fjson.put("lecture_id", lecture_id);
            fjson.put("subject_id", subjectId);

            fjson.put("lecture_id", lecture_id);
            fjson.put("lecture_title", lectureName);
            fjson.put("section_id", section_id);
            fjson.put("feedback", feedback);
            fjson.put("rating", rating);
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.accessToken, access_token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;


        //Updated by chandu Need to verify
        // if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            StringRequest strReq = new StringRequest(Request.Method.POST, Constants.SEND_VIDEO_RATING, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    //Log.d(Constants.response, response);
                    try {

                        JSONObject jObj = new JSONObject(response);
                        //Log.d(Constants.response, response);
                        boolean error = jObj.getBoolean(Constants.errorFlag);
                        if (!error) {
                            String errorMsg = jObj.getString(Constants.message);
                            CommonUtils.showToast(getApplicationContext(), errorMsg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //CommonUtils.showToast(getApplicationContext(), getString(R.string.there_is_error));

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
    // slide the view from below itself to the current position
    public void slideUp(View view){
        lnrRateUs.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lnrRateUs.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    private void checkDisplayCount()
    {
        try{
            DisplayManager manager= (DisplayManager) getSystemService(DISPLAY_SERVICE);
            if(manager.getDisplays()!=null&&manager.getDisplays().length>1)
            {
                CommonUtils.showToast(getApplicationContext(),"Please stop your recording Apps before using Tutorix");
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
