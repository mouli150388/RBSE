package com.tutorix.tutorialspoint.activities;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExpertsProfileActivity extends AppCompatActivity implements VideoRendererEventListener {
    ExpertsProfileActivity _this;
    private static final String TAG = ExpertsProfileActivity.class.getSimpleName();
    private String video_url;
    private SimpleExoPlayer player;
    SimpleExoPlayerView simpleExoPlayerView;
    int screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = ExpertsProfileActivity.this;
        setContentView(R.layout.activity_expert_pofile);
       /* Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subject Matter Expert");*/
        TextView tvName = findViewById(R.id.name);
        TextView tvDescription = findViewById(R.id.description);
        TextView txt_expertin = findViewById(R.id.txt_expertin);
        CircleImageView ivProfilePic = findViewById(R.id.pic);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(wm).getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (AppStatus.getInstance(_this).isOnline()) {
                tvName.setText(bundle.getString(Constants.facultyName));
                tvDescription.setText(bundle.getString(Constants.introduction));
                txt_expertin.setText(bundle.getString(Constants.expertise));
                video_url = bundle.getString(Constants.videoUrl);
                Picasso.with(_this).load(bundle.getString(Constants.photoUrl)).placeholder(R.mipmap.ic_launcher).fit().into(ivProfilePic, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
            if(tvDescription.getText().length()>5)
                tvDescription.setVisibility(View.VISIBLE);
            else
                tvDescription.setVisibility(View.GONE);

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            player.setRepeatMode(Player.REPEAT_MODE_OFF);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
            simpleExoPlayerView = findViewById(R.id.player_view);
            simpleExoPlayerView.setUseController(true);
            simpleExoPlayerView.requestFocus();
            simpleExoPlayerView.setPlayer(player);
            Uri mp4VideoUri = Uri.parse(video_url); //Radnom 540p indian channel
            DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
         /*   final MediaSource mediaSource = new HlsMediaSource(mp4VideoUri,
                    dataSourceFactory, null, null);*/
            MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);
            int orientation = this.getResources().getConfiguration().orientation;

            Objects.requireNonNull(wm).getDefaultDisplay().getMetrics(displayMetrics);
            int  screenHeight = displayMetrics.heightPixels;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight / 3));
            } else {
                simpleExoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            player.prepare(mediaSource);
       /* final MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 0, null, null);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
        player.prepare(videoSource);*/

            player.addListener(new ExoPlayer.EventListener() {

                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case ExoPlayer.STATE_BUFFERING:
                            break;
                        case ExoPlayer.STATE_ENDED:
                            finish();
                            break;
                        case ExoPlayer.STATE_IDLE:
                            break;
                        case ExoPlayer.STATE_READY:
                            lineScaleLoaderExample_disable();
                            break;
                        default:
                            break;
                    }
                }

                private void lineScaleLoaderExample() {
                    findViewById(R.id.material_design_linear_scale_progress_loader).setVisibility(View.VISIBLE);
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
                    player.prepare(mediaSource);
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
            });
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            player.setVideoDebugListener(this);
        } else {
            CommonUtils.showToast(getApplicationContext(),getString(R.string.no_internet));
            // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }



    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlayer();
    }

    private void startPlayer() {
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
    }

    private void pausePlayer() {
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        player.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}
