package com.tutorix.tutorialspoint.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.btn_switch)
    Switch btnSwitch;
    @BindView(R.id.quiz_switch)
    Switch quizSwitch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_clear_video_setngs)
    Button btn_clear_video_setngs;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
         sharedPref=new SharedPref();
        btnSwitch.setChecked(sharedPref.isButtonEffects(getApplicationContext()));
        quizSwitch.setChecked(sharedPref.isQuizEffects(getApplicationContext()));
        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.setButtenEffect(getApplicationContext(),isChecked);
            }
        });
        quizSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.setQuizEffect(getApplicationContext(),isChecked);
            }
        });

        btn_clear_video_setngs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.setAudioText(getApplicationContext(),-1);
                SessionManager.setVideoQuality(getApplicationContext(),0);
                SessionManager.setAudio(getApplicationContext(),-1);

                AppController.childQaulityText=-1;
                //AppController.childQaulityAudio=-1;
                AppController.childQaulity=0;
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void goBack(View view) {
        onBackPressed();
    }
}
