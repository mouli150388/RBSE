package com.tutorix.tutorialspoint.dsw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.views.CustomTextview;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DWSKeyAccessActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_dsw_desc)
    CustomTextview txtDswDesc;
    @BindView(R.id.edit_key)
    TextInputEditText editKey;
    @BindString(R.string.dsw_str2)
    String dsw2;
    @BindString(R.string.gotadoubtclick)
    String gotadoubtclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwskey_access);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 24) {
            txtDswDesc.setText(Html.fromHtml(dsw2,Html.FROM_HTML_OPTION_USE_CSS_COLORS));
              } else {
            txtDswDesc.setText(Html.fromHtml(dsw2));

        }
    }

    @OnClick({R.id.img_back, R.id.txt_dsw_desc, R.id.btn_viewrsult})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_dsw_desc:
            case R.id.btn_viewrsult:
                startActivity(new Intent(getApplicationContext(),DWSPerformanceActivity.class));
                break;

        }
    }
}
