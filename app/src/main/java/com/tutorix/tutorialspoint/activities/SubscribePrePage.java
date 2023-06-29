package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class SubscribePrePage extends AppCompatActivity {
    String access_token;
    String userid;
    String classId;
    String loginType;
    String session_device_id;
    TextView txt_header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_pre_page);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        Button lnr_start=findViewById(R.id.lnr_start);

        txt_header=findViewById(R.id.txt_header);
        String[] userInfo = SessionManager.getUserInfo(this);

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        if (loginType.equals("")) {
            txt_header.setText(getString(R.string.premium_feature));
        } else {

            txt_header.setText(getString(R.string.renew_feature));
        }
        lnr_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeOnline(v);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void subscribeOnline(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }
        startActivity(new Intent(getApplicationContext(), SubscribeActivity.class));
        finish();
    }
}
