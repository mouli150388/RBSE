package com.tutorix.tutorialspoint.video;

import static com.tutorix.tutorialspoint.utility.Constants.LANG_VIDEO_SUPPORT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
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
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.BuildConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.players.DefaultHttpDataSourceFactorya;
import com.tutorix.tutorialspoint.players.SDCardDataSource;
import com.tutorix.tutorialspoint.players.SimpleExoPlayerView;
import com.tutorix.tutorialspoint.players.TrackSelectionDialog;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity implements VideoRendererEventListener, Player.EventListener {

    @BindView(R.id.subjectnameTVID)
    TextView subjectnameTVID;
    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.img_settings)
    ImageView imgSettings;
    @BindView(R.id.lnr_settings)
    LinearLayout lnrSettings;
    @BindView(R.id.lnr_close)
    LinearLayout lnr_close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tvMyNumber)
    TextView tvMyNumber;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.material_design_linear_scale_progress_loader)
    ProgressBar materialDesignLinearScaleProgressLoader;

    int rendererIndex = 1;
    VideoPlayerActivity _this;
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();
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

    int selectedposition = 0;


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

    String selected_user_id;
    String selected_class_id;

    Boolean hindiUrl = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig.setLanguages(this);
        CommonUtils.setFullScreen(this); // set screen full
        CommonUtils.secureScreen(this); //// secure from screen record/capture
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);

        _this = this;

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        selected_user_id = getIntent().getStringExtra("selected_user_id");
        selected_class_id = getIntent().getStringExtra("selected_class_id");
        hindiUrl = getIntent().getBooleanExtra("hindiUrl", true);
        lecture_video_url = getIntent().getStringExtra("lecture_video_url");

        setSupportActionBar(toolbar);
        tvMyNumber.setVisibility(View.GONE);
        if (LANG_VIDEO_SUPPORT != null && LANG_VIDEO_SUPPORT.length > 1)
            audio_language = SessionManager.getAudioLanguage(getApplicationContext());
        else
            audio_language = LANG_VIDEO_SUPPORT[0];
        initData();
        lnr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initData() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setTitle("Suggested Video");

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

            txt_title.setText(lectureName);
            showLayout(true);
            // initCollapsingToolbar();
            proceedData();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        showLayout(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 8000);
        }
    }

    private void showLayout(boolean show) {
        if (show) {
            txt_title.animate()
                    .translationX(0)
                    .setDuration(600)
                    .alpha(1)
                    .start();
        } else {
            txt_title.animate()
                    .translationX(txt_title.getWidth())
                    .alpha(0)
                    .setDuration(600)
                    .start();
        }
    }

    private void proceedData() {

        if (/*AppConfig.checkSDCardEnabled(_this, userid, classid) &&*/ AppConfig.checkSdcard(classid,getApplicationContext())) {
            lnrSettings.setVisibility(View.VISIBLE);
            if (checkPermissionforstorage()) {
                String sdCardpath = "";
                if ((sdCardpath = AppConfig.getSdCardPath(classid,getApplicationContext())) != null) {
                    lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                    lecture_video_srt = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;

                    loadData(false);
                } else {
                    finish();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));

                }
            } else {
                requestPermissionforstorage();
            }
        }else {
            if(lecture_video_url.toString().length() > 6) {
                loadData(true);
                lnrSettings.setVisibility(View.GONE);
            } else if (selected_user_id != null && selected_user_id != userid) {
                lecture_video_url = AppConfig.getOnlineURL(selected_class_id, false) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                lecture_video_srt = AppConfig.getOnlineURL(selected_class_id, false) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                loadData(true);
                lnrSettings.setVisibility(View.GONE);
            }
        }
        /*if (lecture_video_url.toString().length() > 6) {
            loadData(true);
            lnrSettings.setVisibility(View.GONE);
        } else if (selected_user_id != null && selected_user_id != userid) {
            lecture_video_url = AppConfig.getOnlineURL(selected_class_id, false) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
            lecture_video_srt = AppConfig.getOnlineURL(selected_class_id, false) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
            loadData(true);
            lnrSettings.setVisibility(View.GONE);
        } else if (loginType.isEmpty()) {
            if (AppStatus.getInstance(_this).isOnline()) {
                lecture_video_url = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                lecture_video_srt = AppConfig.getOnlineURL(classid, false) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                loadData(true);
                lnrSettings.setVisibility(View.GONE);
            } else {
                finish();
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

            }
        } else if (loginType.equalsIgnoreCase("O")) {
            if (AppConfig.checkSDCardEnabled(_this, userid, classid) && AppConfig.checkSdcard(classid,getApplicationContext())) {
                lnrSettings.setVisibility(View.VISIBLE);
                if (checkPermissionforstorage()) {
                    String sdCardpath = "";
                    if ((sdCardpath = AppConfig.getSdCardPath(classid,getApplicationContext())) != null) {
                        lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                        lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                        lecture_video_srt = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;

                        loadData(false);
                    } else {
                        finish();
                        CommonUtils.showToast(getApplicationContext(), getString(R.string.no_sdcard));

                    }
                } else {
                    requestPermissionforstorage();
                }
            }
            else {
                if (AppStatus.getInstance(_this).isOnline()) {
                    lecture_video_url = AppConfig.getOnlineURL(classid, true) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_video_srt = AppConfig.getOnlineURL(classid,true) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                    loadData(true);
                    lnrSettings.setVisibility(View.GONE);
                } else {
                    finish();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

                }
            }
        } else {
            lnrSettings.setVisibility(View.VISIBLE);
            if (checkPermissionforstorage()) {
                String sdCardpath = "";
                if ((sdCardpath = AppConfig.getSdCardPath(classid,getApplicationContext())) != null) {
                    lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                    lecture_video_srt = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;

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

    TextView txt_audio_track;
    View exo_settings;
    Button exo_speed;

    private void loadData(boolean isOnline) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        start = dateFormat.format(date);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        PlaybackControlView controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
        final ImageView exo_cc_icon = controlView.findViewById(R.id.exo_cc_icon);
        // final TextView txt_audio_track = controlView.findViewById(R.id.txt_audio_track);
        View exo_full = controlView.findViewById(R.id.exo_full);
        exo_full.setVisibility(View.GONE);
        //  TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new  DefaultBandwidthMeter.Builder().setInitialBitrateEstimate(10000).build());
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        DefaultTrackSelector.ParametersBuilder builder = new DefaultTrackSelector.ParametersBuilder();


        builder.setRendererDisabled(C.TRACK_TYPE_VIDEO, false);
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

            if (AppController.getInstance().childQaulityAudio == 0)
                builder.setPreferredAudioLanguage("EN");
            else if (AppController.getInstance().childQaulityAudio == 1)
                builder.setPreferredAudioLanguage("HI");

            if (AppController.getInstance().childQaulityText == 1)
                builder.setPreferredTextLanguage("EN");
            else builder.setDisabledTextTrackSelectionFlags(C.TRACK_TYPE_TEXT);
            ((DefaultTrackSelector) trackSelector).setParameters(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
            ((DefaultTrackSelector) trackSelector).setParameters(builder.build());
        }
        exo_settings = controlView.findViewById(R.id.exo_settings);
        exo_speed = controlView.findViewById(R.id.exo_speed);
        exo_full = controlView.findViewById(R.id.exo_full);
        exo_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpeed(v);
            }
        });
        exo_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTracksInfo(v);
            }
        });
        exo_cc_icon.setOnClickListener(new View.OnClickListener() {
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
        });
        txt_audio_track = controlView.findViewById(R.id.txt_audio_track);
        txt_audio_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
                    showPopup(v);
                    return;
                }
                setVDOPopupWindow(v);*/


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
                    isShowingTrackSelectionDialog = true;
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForTrackSelector(
                                    trackSelector,
                                    /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                    trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

                }

            }
        });
        player = ExoPlayerFactory.newSimpleInstance(_this, trackSelector, loadControl);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        // simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        player.setRepeatMode(Player.REPEAT_MODE_OFF);
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.setPlayer(player);
        Typeface subtitleTypeface = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
        CaptionStyleCompat style =
                new CaptionStyleCompat(getResources().getColor(R.color.white),
                        getResources().getColor(R.color.subtitle_trans), Color.TRANSPARENT,
                        CaptionStyleCompat.EDGE_TYPE_NONE,
                        getResources().getColor(R.color.white), subtitleTypeface);
        simpleExoPlayerView.getSubtitleView().setStyle(style);

        if (isOnline) {
            checkCookieThenPlay();
        } else {
            initForLocalFile(lecture_video_url);

        }
        player.addListener(this);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight / 3));
        } else {
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }

        setSpeed();
    }

    private void checkCookieThenPlay() {
        //playVideo();
        String encryption = "";
        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classid);
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption = URLEncoder.encode(encryption, "utf-8");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //CustomDialog.showDialog(NotesActivity.this,true);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.CHECK_COOKIES + "?json_data=" + encryption, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomDialog.closeDialog();
                playVideo();

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
        });
      /*  if(loginType.equalsIgnoreCase("O") )
            AppController.getInstance().addToRequestQueue(request);
        else*/
        playVideo();

    }

    private void playVideo() {

        findViewById(R.id.material_design_linear_scale_progress_loader).setVisibility(View.VISIBLE);
        Uri mp4VideoUri = Uri.parse(lecture_video_url);
        String userAgent = Util.getUserAgent(this, "");
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactorya(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                1800000,
                true);

        mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);

        //For Playing SRT Files
        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);


        MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(lecture_video_srt), textFormat, C.TIME_UNSET);
        concatenatedSource = new MergingMediaSource(mediaSource, textMediaSource);


        player.prepare(concatenatedSource);
        player.setPlayWhenReady(true);
    }

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

       /* MediaSource videoSource=  new HlsMediaSource(fileDataSource.getUri(), factory, 1800000,
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
      /*  MediaSource audioSource=  new HlsMediaSource(fileDataSourceAudi.getUri(), factoryAudi, 1800000,
                new Handler(), null);*/
        MediaSource audioSource = new HlsMediaSource.Factory(factoryAudi).createMediaSource(fileDataSourceAudi.getUri());

        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);

        DataSource.Factory dataSourceFactory = new FileDataSourceFactory();
        MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(lecture_video_srt), textFormat, C.TIME_UNSET);

        concatenatedSource = new MergingMediaSource(videoSource, audioSource, textMediaSource);
        //MergingMediaSource concatenatedSource = new MergingMediaSource(videoSource,audioSource);
        player.prepare(concatenatedSource);
        player.setPlayWhenReady(true);


        isConnected(getApplicationContext());
    }

    MediaSource concatenatedSource;
    boolean isCheckedUSb;

    public boolean isConnected(Context context) {
        isCheckedUSb = true;
        Intent intent = context.registerReceiver(mUsbAttachReceiver, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
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

                        if (AppConfig.getSdCardPath(classid,getApplicationContext()) != null) {
                            lecture_video_url = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                            lecture_audio_url = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language + "/" + audio_language + ".m3u8";
                            lecture_video_srt = AppConfig.getSdCardPath(classid,getApplicationContext()) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
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

    /* For USB CONNECTION CHECK*/
    BroadcastReceiver mUsbAttachReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.getExtras().getBoolean("connected")) {
                //showDevices();

                //player.stop();
                CommonUtils.showToast(getApplicationContext(), "Please remove your USB Connection");
                finish();
            } else {

            }
        }
    };

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        AppController.getInstance().playAudio(R.raw.button_click);
        return false;
    }

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
            if (trackSelections.get(rendererIndex) != null)
                if (trackSelections.get(rendererIndex).getSelectedFormat() != null && trackSelections.get(rendererIndex).getSelectedFormat().language != null)
                    txt_audio_track.setText(trackSelections.get(rendererIndex).getSelectedFormat().language.toUpperCase());

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
                tvMyNumber.setVisibility(View.VISIBLE);
                onBackPressed();
                break;
            case Player.STATE_IDLE:
                break;
            case Player.STATE_READY:

                tvMyNumber.setVisibility(View.GONE);
                if (!alreadyExecuted) {
                    //updateProgressBar();
                    //alreadyExecuted = true;
                }
                if (exo_settings != null)
                    exo_settings.setVisibility(View.VISIBLE);
                if (exo_speed != null)
                    exo_speed.setVisibility(View.VISIBLE);
                lineScaleLoaderExample_disable();
                break;
            default:
                break;
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

    private void pausePlayer() {
        if (player == null)
            return;
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    //Handler handlerGONE = new Handler();
    //Handler handlerVISIBLE = new Handler();
    boolean alreadyExecuted = false;

    @Override
    protected void onPause() {
        super.onPause();

        //handlerVISIBLE.removeCallbacks(updateVISIBLE);
        // handlerGONE.removeCallbacks(updateGONE);
        //tvMyNumber.setVisibility(View.GONE);
        // alreadyExecuted = false;

        pausePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // handlerVISIBLE.removeCallbacks(updateVISIBLE);
        // handlerGONE.removeCallbacks(updateGONE);
        //tvMyNumber.setVisibility(View.GONE);
        //alreadyExecuted = false;


        /*if(dm!=null&&lt!=null)
            dm.unregisterDisplayListener(lt);*/
    }

    DecimalFormat formatter = new DecimalFormat("00");

    @Override
    public void onBackPressed() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        long position = 0;
        try {
            if (player != null) {
                position = player.getCurrentPosition();
                player.release();
                concatenatedSource = null;
                trackSelector = null;
                player = null;
            }
        } catch (Exception e) {
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
        if (difference < 0)
            difference = -difference;
        long diffSeconds = difference / 1000 % 60;
        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        //Log.d("difference", String.valueOf(diffHours) + ":" + diffMinutes + ":" + diffSeconds);
        if (diffHours >= 1) {
            diffHours = 0;
            diffMinutes = 59;
            diffSeconds = 0;
        }
        String a = formatter.format(diffHours) + ":" + formatter.format(diffMinutes) + ":" + formatter.format(diffSeconds);
        if (diffHours > 0 || diffMinutes > 0 || diffSeconds >= 30)
            postTrackToServer(a, false, position);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            //appbar.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //appbar.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight / 3));
        }
    }

    private void postTrackToServer(final String a, final boolean typeRelease, long position) {


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
            if (selected_user_id != null && selected_user_id != userid) {
                fjson.put(Constants.classId, selected_class_id);
            } else
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
                        if (!error) {
                            String errorMsg = jObj.getString(Constants.message);
                            //CommonUtils.showToast(getApplicationContext(), errorMsg);

                            if (typeRelease) {

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

                                    chapters.is_sync = true;
                                    chapters.duration_insec = CommonUtils.getSeconds(chapters.activity_duration);
                                    database.trackDAO().insertTrack(chapters);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                finish();
                            }

                        } else {
                            String errorMsg = jObj.getString(Constants.message);
                            //CommonUtils.showToast(getApplicationContext(), errorMsg);

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


            //if (loginType.equalsIgnoreCase("O") || loginType.isEmpty())
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            /*else
            {

                MyDatabase database = MyDatabase.getDatabase(_this);
                try {


                    TrackModel chapters = new TrackModel();
                    chapters.activity_type = "V";
                    chapters.activity_duration =a;
                    chapters.activity_date = getDateTime();
                    chapters.quiz_id = "";
                    chapters.subject_id = subjectId;
                    chapters.section_id = section_id;
                    chapters.lecture_id = fjson.getString("lecture_id");
                    chapters.class_id = classid;
                    chapters.user_id =userid;
                    chapters.videoduratrion=videoDuration;

                    chapters.lecture_name = lectureName;

                    chapters.is_sync = false;
                    chapters.duration_insec=CommonUtils.getSeconds(chapters.activity_duration);
                    database.trackDAO().insertTrack(chapters);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (typeRelease) {
                    if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
                        lecture_video_url = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                        lecture_video_srt = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                        loadData(true);
                    } else {
                        String sdCardpath=sdCardpath=AppConfig.getSdCardPath(classid);
                        lecture_video_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                        lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language+"/"+audio_language+".m3u8";
                        lecture_video_srt = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                        loadData(false);
                    }

                } else {
                    finish();
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
               /* if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
                    lecture_video_url = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_video_srt = AppConfig.getOnlineURL(classid) + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                    loadData(true);
                } else {
                    String sdCardpath=AppConfig.getSdCardPath(classid);
                    lecture_video_url = sdCardpath+ subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_NAME;
                    lecture_audio_url = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + audio_language+"/"+audio_language+".m3u8";
                    lecture_video_srt = sdCardpath + subjectId + "/" + section_id + "/" + lecture_id + "/" + Constants.VIDEO_SRT;
                    loadData(false);
                }
*/
            } else {
                finish();
            }
        }
    }

    private String getDateTime() {
        Date date = new Date();
        return CommonUtils.format.format(date);
    }


    public void menuClick(View view) {
        //Log.v("IsOnline","IsOnline "+loginType);
        if (loginType.isEmpty()) {
            showPopup(view);

            return;
        }

        if (loginType.equalsIgnoreCase("O")) {

            if (AppConfig.checkSDCardEnabled(_this, userid, classid)) {

                setVDOPopupWindow(view);
            } else {
                showPopup(view);

            }
            return;
        }
        setVDOPopupWindow(view);


    }

    private void setSpeed() {
        switch (AppController.getInstance().SpeedControll) {

            case 0:
                speedparams = new PlaybackParameters(.75f, 1f, false);

                player.setPlaybackParameters(speedparams);
                exo_speed.setText(".75X");
                break;
            case 1:
                speedparams = new PlaybackParameters(1f, 1f);

                player.setPlaybackParameters(speedparams);
                exo_speed.setText("1X");
                break;
            case 2:
                speedparams = new PlaybackParameters(1.25f, 1f, true);
                player.setPlaybackParameters(speedparams);
                exo_speed.setText("1.25X");
                break;
            case 3:
                speedparams = new PlaybackParameters(1.5f, 1f, true);
                player.setPlaybackParameters(speedparams);
                exo_speed.setText("1.5X");
                break;
        }
    }

    PopupWindow popupWindow;

    private void showPopup(View v) {
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
            txt_phy.setText("Audio");
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
                        isShowingTrackSelectionDialog = true;
                        TrackSelectionDialog trackSelectionDialog =
                                TrackSelectionDialog.createForTrackSelector(
                                        trackSelector,
                                        /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                        trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

                    }

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
                        isShowingTrackSelectionDialog = true;
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
    boolean isShowingTrackSelectionDialog;
    PopupMenu langagePopup;

    private void setVDOPopupWindow(View v) {


        if (langagePopup == null) {
            langagePopup = new PopupMenu(VideoPlayerActivity.this, v);

            Menu menu1 = langagePopup.getMenu();

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

    @Override
    protected void onResume() {
        super.onResume();
        checkDisplayCount();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkDisplayCount();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDisplayCount();
    }

    private void checkDisplayCount() {
        try {
            DisplayManager manager = (DisplayManager) getSystemService(DISPLAY_SERVICE);
            if (manager.getDisplays() != null && manager.getDisplays().length > 1) {
                CommonUtils.showToast(getApplicationContext(), "Please stop your recording Apps before using Tutorix");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PopupMenu popupMenu;
    PlaybackParameters speedparams;

    private void showSpeed(View v) {
        if (popupMenu == null) {
            popupMenu = new PopupMenu(VideoPlayerActivity.this, v);

            Menu menu1 = popupMenu.getMenu();
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
                if (item.getTitle().equals("Slow")) {
                    speedparams = new PlaybackParameters(.75f, 1f, false);

                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText(".75X");
                    AppController.getInstance().SpeedControll = 0;
                } else if (item.getTitle().equals("Normal")) {
                    speedparams = new PlaybackParameters(1f, 1f);

                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText("1X");
                    AppController.getInstance().SpeedControll = 1;
                } else if (item.getTitle().equals("Fast")) {
                    speedparams = new PlaybackParameters(1.25f, 1f, true);
                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText("1.25X");
                    AppController.getInstance().SpeedControll = 2;
                } else if (item.getTitle().equals("Faster")) {
                    speedparams = new PlaybackParameters(1.5f, 1f, true);
                    player.setPlaybackParameters(speedparams);
                    exo_speed.setText("1.5X");
                    AppController.getInstance().SpeedControll = 3;
                }


                return false;
            }
        });
    }

    private void showTracksInfo(View view) {
        if (loginType.equalsIgnoreCase("O")) {

            if (AppConfig.checkSDCardEnabled(_this, userid, classid)) {

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
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

        }
    }
}
