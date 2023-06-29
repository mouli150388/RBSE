package com.tutorix.tutorialspoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tutorix.tutorialspoint.home.HomeTabActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivationResultActivity extends AppCompatActivity {

    @BindView(R.id.img_animate)
    ImageView imgAnimate;
    @BindView(R.id.btn_continue)
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_result);
        ButterKnife.bind(this);
        //imgAnimate.setBackgroundResource(R.drawable.animation_success);

        // Get the background, which has been compiled to an AnimationDrawable object.
        //AnimationDrawable frameAnimation = (AnimationDrawable) imgAnimate.getBackground();
        Glide.with(getApplicationContext()).load(R.drawable.key_activation_gif).into(imgAnimate);
        // Start the animation (looped playback by default).
        //frameAnimation.start();
    }

    @OnClick({R.id.img_animate, R.id.btn_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_animate:
                break;
            case R.id.btn_continue:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(200);
        Intent i = new Intent(ActivationResultActivity.this, HomeTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        startActivity(i);
        finish();
    }
}
