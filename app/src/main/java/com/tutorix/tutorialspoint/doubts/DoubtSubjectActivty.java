package com.tutorix.tutorialspoint.doubts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoubtSubjectActivty extends AppCompatActivity {

    @BindView(R.id.radio_math)
    ImageView radioMath;
    @BindView(R.id.radio_phy)
    ImageView radioPhy;
    @BindView(R.id.radio_che)
    ImageView radioChe;
    @BindView(R.id.radio_bio)
    ImageView radioBio;


    int subjectId;
    @BindView(R.id.rel_top)
    RelativeLayout relTop;
    @BindView(R.id.radio_sub_group)
    LinearLayout radioSubGroup;
    @BindView(R.id.txt_dinot_find)
    TextView txtDinotFind;
    @BindView(R.id.card_bottom)
    MaterialCardView cardBottom;
    @BindView(R.id.lnr_math)
    LinearLayout lnr_math;
    @BindView(R.id.lnr_bio)
    LinearLayout lnr_bio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_subject_activty);
        ButterKnife.bind(this);
        String[] userInfo = SessionManager.getUserInfo(this);
       String classId = userInfo[4];
        try {
            int currrentClsId = Integer.parseInt(classId);
            if (currrentClsId <=7) {

            } else if (currrentClsId ==8) {
                lnr_bio.setVisibility(View.GONE);

            } else if (currrentClsId ==9){
                lnr_math.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtDinotFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppController.getInstance().playAudio(R.raw.button_click);
                if (subjectId <= 0) {
                    CommonUtils.showToast(getApplicationContext(), "Please select Subject");
                    return;
                }
                setResult(subjectId);
                finish();
            }
        });
    }

    public void home(View view) {
        Intent in = new Intent(DoubtSubjectActivty.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        subjectId = -1;
        finish();

    }

    @OnClick({R.id.radio_math, R.id.radio_phy, R.id.radio_che, R.id.radio_bio,R.id.lnr_math, R.id.lnr_phy, R.id.lnr_che, R.id.lnr_bio})
    public void onViewClicked(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        switch (view.getId()) {
            case R.id.radio_math:
            case R.id.lnr_math:
                subjectId = 4;
                radioMath.setImageResource(R.drawable.quiz_math_selector);
                radioPhy.setImageResource(R.drawable.phics_graph);
                radioChe.setImageResource(R.drawable.chemistry_graph);
                radioBio.setImageResource(R.drawable.bio_graph);
                break;
            case R.id.radio_phy:
            case R.id.lnr_phy:
                subjectId = 1;
                radioMath.setImageResource(R.drawable.math_graph);
                radioPhy.setImageResource(R.drawable.quiz_phy_selector);
                radioChe.setImageResource(R.drawable.chemistry_graph);
                radioBio.setImageResource(R.drawable.bio_graph);
                break;
            case R.id.radio_che:
            case R.id.lnr_che:
                subjectId = 2;
                radioMath.setImageResource(R.drawable.math_graph);
                radioPhy.setImageResource(R.drawable.phics_graph);
                radioChe.setImageResource(R.drawable.quiz_che_selector);
                radioBio.setImageResource(R.drawable.bio_graph);
                break;
            case R.id.radio_bio:
            case R.id.lnr_bio:
                subjectId = 3;
                radioMath.setImageResource(R.drawable.math_graph);
                radioPhy.setImageResource(R.drawable.phics_graph);
                radioChe.setImageResource(R.drawable.chemistry_graph);
                radioBio.setImageResource(R.drawable.quiz_bio_selector);
                break;
        }
    }
}
