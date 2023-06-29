package com.tutorix.tutorialspoint.toc;

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
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.players.DefaultHttpDataSourceFactorya;
import com.tutorix.tutorialspoint.players.SDCardDataSource;
import com.tutorix.tutorialspoint.players.SimpleExoPlayerView;
import com.tutorix.tutorialspoint.players.TrackSelectionDialog;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.video.VideoPresentorImpl;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TOCPlayerActivity extends AppCompatActivity implements VideoRendererEventListener,Player.EventListener{

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

    int rendererIndex=1;
    TOCPlayerActivity _this;
    private static final String TAG = TOCPlayerActivity.class.getSimpleName();
    private String start;
    private AlertDialog alertDialog;
    private SimpleExoPlayer player;
    private String lecture_id, selected_lectureid,lectureName,section_name;
    private String subjectId;
    private String lecture_audio_url="", lecture_video_url;
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
    String lecture_video_srt="";
    boolean pos=false;
    boolean isCC_On=false;
    VideoPresentorImpl videoPresentorImpl;
    public String audio_language="english";

    String selected_user_id;
    String selected_class_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        selected_user_id=getIntent().getStringExtra("selected_user_id");
        selected_class_id=getIntent().getStringExtra("selected_class_id");

        setSupportActionBar(toolbar);
        tvMyNumber.setVisibility(View.GONE);
       /* if(LANG_VIDEO_SUPPORT!=null&&LANG_VIDEO_SUPPORT.length>1)
            audio_language = SessionManager.getAudioLanguage(getApplicationContext());
        else
            audio_language = LANG_VIDEO_SUPPORT[0];*/
        initData();
        lnr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void initData()
    {
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
            lecture_video_url = extras.getString("lecture_video_url");

            txt_title.setText(lectureName);
            showLayout(true);
           // initCollapsingToolbar();
            proceedData();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try{
                        showLayout(false);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            },8000);
        }
    }

    private void showLayout(boolean show){
        if (show){
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



            if (AppStatus.getInstance(_this).isOnline()) {
                 loadData(true);
                lnrSettings.setVisibility(View.GONE);
            } else {
                finish();
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

            }



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
        DefaultTrackSelector.ParametersBuilder builder=new DefaultTrackSelector.ParametersBuilder();


        builder .setRendererDisabled(C.TRACK_TYPE_VIDEO, false);
       /* if(AppController.getInstance().builder==null) {
            ((DefaultTrackSelector) trackSelector).setParameters(new DefaultTrackSelector.ParametersBuilder()
                    .setRendererDisabled(C.TRACK_TYPE_VIDEO, false)
                    .build()
            );
        }else
        {*/


        exo_settings = controlView.findViewById(R.id.exo_settings);
        exo_speed = controlView.findViewById(R.id.exo_speed);
        exo_full = controlView.findViewById(R.id.exo_full);
        exo_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpeed(v);
            }
        });exo_settings.setOnClickListener(new View.OnClickListener() {
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
                    isCC_On=true;
                }
                else {
                    isCC_On=false;
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
                    isShowingTrackSelectionDialog=true;
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
            playVideo();
        } else {


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


    private void playVideo() {

        findViewById(R.id.material_design_linear_scale_progress_loader).setVisibility(View.VISIBLE);
        Uri mp4VideoUri = Uri.parse(lecture_video_url);
        String userAgent = Util.getUserAgent(this, "");
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactorya(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                1800000,
                true);

        mediaSource= new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);

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


      /*  DataSpec dataSpecAudi = new DataSpec(Uri.parse(lecture_audio_url));
        final FileDataSource fileDataSourceAudi = new FileDataSource();
        try {
            fileDataSourceAudi.open(dataSpecAudi);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }*/

      /*  DataSource.Factory factoryAudi = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSourceAudi;
            }
        };*/
      /*  MediaSource audioSource=  new HlsMediaSource(fileDataSourceAudi.getUri(), factoryAudi, 1800000,
                new Handler(), null);*/
      //  MediaSource audioSource = new HlsMediaSource.Factory(factoryAudi).createMediaSource(fileDataSourceAudi.getUri());

        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);

        DataSource.Factory dataSourceFactory = new FileDataSourceFactory();
        MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(lecture_video_srt), textFormat, C.TIME_UNSET);

         concatenatedSource = new MergingMediaSource(videoSource,textMediaSource);
        //MergingMediaSource concatenatedSource = new MergingMediaSource(videoSource,audioSource);
        player.prepare(concatenatedSource);
        player.setPlayWhenReady(true);


        isConnected(getApplicationContext());
    }
    MediaSource concatenatedSource;
    boolean isCheckedUSb;
    public boolean isConnected(Context context) {
        isCheckedUSb=true;
        Intent intent = context.registerReceiver(mUsbAttachReceiver, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
    }

    private boolean checkPermissionforstorage() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
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

        if(player!=null)
            player.release();
        try {
            if (mUsbAttachReceiver != null&&isCheckedUSb)
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
            if(trackSelections.get(rendererIndex)!=null)
            if(trackSelections.get(rendererIndex).getSelectedFormat()!=null&&trackSelections.get(rendererIndex).getSelectedFormat().language!=null)
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
                if(exo_settings!=null)
                    exo_settings.setVisibility(View.VISIBLE);
                if(exo_speed!=null)
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
    boolean alreadyExecuted=false;

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

    private String getDateTime() {
        Date date = new Date();
        return CommonUtils.format.format(date);
    }


    public void menuClick(View view) {
        //Log.v("IsOnline","IsOnline "+loginType);
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

                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = ((DefaultTrackSelector)trackSelector).getCurrentMappedTrackInfo();
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
            });
            txt_che.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow.dismiss();

                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = ((DefaultTrackSelector)trackSelector).getCurrentMappedTrackInfo();
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
    boolean isShowingTrackSelectionDialog;
    PopupMenu langagePopup;
    private void setVDOPopupWindow(View v) {




        if(langagePopup==null)
        {
            langagePopup=new PopupMenu(TOCPlayerActivity.this,v);

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

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.setLanguages(this);
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
    PopupMenu popupMenu;
    PlaybackParameters speedparams;
    private void showSpeed(View v)
    {
        if(popupMenu==null)
        {
            popupMenu=new PopupMenu(TOCPlayerActivity.this,v);

            Menu menu1=popupMenu.getMenu();
            menu1.add("Slow");
            menu1.add("Normal");
            menu1.add("Fast");
            menu1.add("Faster");

        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) { item.setChecked(true);
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
    private void showTracksInfo(View view)
    {
        if(loginType.equalsIgnoreCase("O") )
        {

            if(AppConfig.checkSDCardEnabled(_this,userid,classid))
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
}
