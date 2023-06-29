package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectusActivity extends AppCompatActivity {

    @BindView(R.id.lnr_website)
    LinearLayout lnrWebsite;
    @BindView(R.id.img_facebook)
    ImageView imgFacebook;
    @BindView(R.id.lnr_facebook)
    LinearLayout lnrFacebook;
    @BindView(R.id.lnr_linkedin)
    LinearLayout lnrLinkedin;
    @BindView(R.id.img_twitter)
    ImageView imgTwitter;
    @BindView(R.id.lnr_twitter)
    LinearLayout lnrTwitter;
    @BindView(R.id.lnr_instagram)
    LinearLayout lnrInstagram;
    String url;
    int position;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lnr_youtube)
    LinearLayout lnrYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectus);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
    }

    @OnClick({R.id.lnr_website, R.id.lnr_facebook, R.id.lnr_linkedin, R.id.lnr_twitter, R.id.lnr_instagram, R.id.lnr_youtube})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lnr_website:
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse(Constants.OFFICIAL_WEBSITE));
                    startActivity(in);
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.lnr_youtube:

                url = "https://www.youtube.com/c/Tutorixindia";
                openLinks(url, "com.google.android.youtube");
            break;
            case R.id.lnr_facebook:
                url = "https://m.facebook.com/tutorixind/";
                openLinks(url, "com.facebook.katana");
                break;
            case R.id.lnr_linkedin:
                url = "https://www.linkedin.com/mwlite/company/tutorix";
                openLinks(url, "com.linkedin.android");
                break;
            case R.id.lnr_twitter:
                url = "https://twitter.com/TutorixIndia";
                openLinks(url, "com.twitter.android");
                break;
            case R.id.lnr_instagram:
                url = "https://www.instagram.com/tutorixindia/";
                openLinks(url, "com.instagram.android");
                break;
        }
    }

    private void openLinks(String url, String packageName) {
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
            return;
        }
        Intent intent;
        try {

            getPackageManager().getPackageInfo(packageName, 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        startActivity(intent);
    }


}
