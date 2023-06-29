package com.tutorix.tutorialspoint.dsw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.WebViewTestActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.views.CustomTextview;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DSWActivity extends AppCompatActivity {


    @BindString(R.string.dsw_str2)
    String dsw2;
    @BindString(R.string.gotadoubtclick)
    String gotadoubtclick;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_dsw_desc)
    CustomTextview txtDswDesc;
    @BindView(R.id.img_click)
    ImageView imgClick;
    @BindView(R.id.txt_clickhere)
    CustomTextview txtClickhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsw);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 24) {
            txtDswDesc.setText(Html.fromHtml(dsw2,Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            txtClickhere.setText(Html.fromHtml(gotadoubtclick,Html.FROM_HTML_OPTION_USE_CSS_COLORS)); // for 24 api and more
        } else {
            txtDswDesc.setText(Html.fromHtml(dsw2));
            txtClickhere.setText(Html.fromHtml(gotadoubtclick));
        }
    }


    @OnClick({R.id.img_back, R.id.txt_dsw_desc, R.id.img_click, R.id.txt_clickhere,R.id.lnr_result,R.id.lnr_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_dsw_desc:
                break;
            case R.id.img_click:
                break;
            case R.id.lnr_result:
                Intent  intentp = new Intent(getApplicationContext(), DWSKeyAccessActivity.class);

                startActivity(intentp);

                break;
            case R.id.lnr_test:
                Intent  intent = new Intent(getApplicationContext(), DSWQuiz.class);

                startActivity(intent);
                break;
            case R.id.txt_clickhere:
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                  Intent  web_intent = new Intent(getApplicationContext(), WebViewTestActivity.class);
                    web_intent.putExtra("data_path", Constants.FAQ);
                    web_intent.putExtra("name", getResources().getString(R.string.faq));
                    startActivity(web_intent);
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }
}
