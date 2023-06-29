package com.tutorix.tutorialspoint.dsw;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.views.CustomTextview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardDescriptionActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_redeem)
    CustomTextview txt_redeem;
    @BindString(R.string.howtoreemtext)
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_description);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
        }
        if (Build.VERSION.SDK_INT >= 24) {
            txt_redeem.setText(Html.fromHtml(text,Html.FROM_HTML_OPTION_USE_CSS_COLORS));

        } else {
            txt_redeem.setText(Html.fromHtml(text));

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
