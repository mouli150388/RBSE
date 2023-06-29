package com.tutorix.tutorialspoint.doubts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.activities.ImagePreviewActivity;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionDetailActivity extends AppCompatActivity {

    String[] userInfo;
    JSONObject questionDetails;

    LinearLayout lnr_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        lnr_home=findViewById(R.id.lnr_home);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                userInfo = SessionManager.getUserInfo(getApplicationContext());
                String user_id = userInfo[0];
                questionDetails = new JSONObject(bundle.getString("question"));


                TextView tvQuestionTitle = findViewById(R.id.tvQuestionTitle);
                TextView tvQuestionDescription = findViewById(R.id.tvQuestionDescription);
                TextView tvQuestionTime = findViewById(R.id.tvQuestionTime);
                ImageView ivQuestionImage = findViewById(R.id.ivQuestionImage);

                LinearLayout answerLayout = findViewById(R.id.answerLayout);
                SimpleExoPlayerView epAnswer = findViewById(R.id.epAnswer);
                ImageView ivAnswer = findViewById(R.id.ivAnswer);
                TextView tvAnswer = findViewById(R.id.tvAnswer);
                TextView tvAnsweredBy = findViewById(R.id.tvAnsweredBy);

                tvQuestionTitle.setText(capitalizeFirstChar(questionDetails.getString("question_title")));
                tvQuestionDescription.setText(capitalizeFirstChar(questionDetails.getString("question_description")));
                tvQuestionTime.setText(questionDetails.getString("question_asked_time"));

                String questionImage = questionDetails.getString("question_image");
                String answer_user_name = questionDetails.getString("answer_user_name");
                tvAnsweredBy.setText("");
                if(answer_user_name!=null&&!answer_user_name.equalsIgnoreCase("null"))
                {
                    tvAnsweredBy.setText(capitalizeFirstChar(answer_user_name));
                }
                if (!questionImage.isEmpty()) {
                    ivQuestionImage.setVisibility(View.VISIBLE);
                    String questionImageUri = Constants.IMAGE_REQUAET_URL + user_id + "/" + questionImage;

                    Picasso.with(this).load(questionImageUri).resize(120, 120).placeholder(R.drawable.circle_default_load).into(ivQuestionImage);
                } else {
                    ivQuestionImage.setVisibility(View.GONE);
                }

                String answerUrl = questionDetails.getString("answer_url");
                String ans_desc = questionDetails.getString("answer_description");
                String _by = questionDetails.getString("answer_given_time");
                tvAnsweredBy.append("  On  "+_by);
                if (!answerUrl.isEmpty()) {

                    String uri = Constants.IMAGE_REQUAET_URL + questionDetails.getString("answer_user_id") + "/" + answerUrl;

                    if (questionDetails.getString("answer_type").equalsIgnoreCase("i")) {
                        ivAnswer.setVisibility(View.VISIBLE);
                        Picasso.with(this).load(uri).resize(120, 120).placeholder(R.drawable.loading_drawable).into(ivAnswer);

                    } else {
                        epAnswer.setVisibility(View.VISIBLE);
                        epAnswer.requestFocus();

                        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
                        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                        LoadControl loadControl = new DefaultLoadControl();
                        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
                        player.setRepeatMode(Player.REPEAT_MODE_OFF);

                        epAnswer.setUseController(true);
                        epAnswer.setPlayer(player);

                        Uri mp4VideoUri = Uri.parse(uri);
                        String userAgent = Util.getUserAgent(this, "tutorix");
                        DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(userAgent);

                        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource)
                                .createMediaSource(mp4VideoUri, null, null);

                        player.prepare(mediaSource);
                        player.setPlayWhenReady(true);
                    }
                } else {
                    answerLayout.setVisibility(View.GONE);
                }

                if (!questionDetails.getString("answer_description").isEmpty()) {
                    tvAnswer.setText(capitalizeFirstChar(questionDetails.getString("answer_description")));
                    answerLayout.setVisibility(View.VISIBLE);
                } else {
                    tvAnswer.setVisibility(View.GONE);
                    answerLayout.setVisibility(View.GONE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home(v);
            }
        });
    }

    public void home(View v) {
        Intent in = new Intent(this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void questionImagePreview(View view) throws JSONException {
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("uri", Constants.IMAGE_REQUAET_URL + userInfo[0] + "/" + questionDetails.getString("question_image"));
        startActivity(intent);
    }

    public void answerImagePreview(View view) throws JSONException {
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("uri", Constants.IMAGE_REQUAET_URL + questionDetails.getString("answer_user_id") + "/" + questionDetails.getString("answer_url"));
        startActivity(intent);
    }


    private String capitalizeFirstChar(String input) {
        if (input.isEmpty())
            return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
