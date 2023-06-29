package com.tutorix.tutorialspoint.classes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.anaylysis.SectionPerformanceFragment;
import com.tutorix.tutorialspoint.anaylysis.TotalPerformFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentPerformanceActivity extends AppCompatActivity {
    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.radio_all)
    RadioButton radioAll;
    @BindView(R.id.radio_phy)
    RadioButton radioPhy;
    @BindView(R.id.radio_che)
    RadioButton radioChe;
    @BindView(R.id.radio_math)
    RadioButton radioMath;
    @BindView(R.id.radio_bio)
    RadioButton radioBio;
    @BindView(R.id.radio_sub_group)
    RadioGroup radioSubGroup;
    String selected_user_id;
    String selected_class_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_performance);
        ButterKnife.bind(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setSubtitle(getIntent().getStringExtra("student_name"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle(getResources().getString(R.string.over_all_activity));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        selected_user_id=getIntent().getStringExtra("selected_user_id");
        selected_class_id=getIntent().getStringExtra("selected_class_id");
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userid = userInfo[0];;
        loginType = userInfo[2];
        class_id = userInfo[4];
        try {
            int currrentClsId = Integer.parseInt(class_id);
            if (currrentClsId <=7) {

            } else if (currrentClsId ==8) {
                radioBio.setVisibility(View.GONE);

            } else if (currrentClsId ==9){
                radioMath.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadFragment(0);
        radioSubGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radio_all:
                        //subject_id=1;
                        loadFragment(0);
                        break;
                    case R.id.radio_phy:
                        //subject_id=1;
                        loadFragment(1);
                        break;
                    case R.id.radio_che:
                        //subject_id=2;
                        loadFragment(2);
                        break;
                    case R.id.radio_math:
                        //subject_id=4;
                        loadFragment(3);
                        break;
                    case R.id.radio_bio:
                        //subject_id=3;
                        loadFragment(4);
                        break;
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppController.getInstance().playAudio(R.raw.back_sound);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadFragment(int position)
    {
        Fragment fragment=null;
        switch (position)
        {
            case 0:
                fragment=new TotalPerformFragment(selected_user_id,selected_class_id);
                break;
            case 1:
                fragment= SectionPerformanceFragment.newInstance("1",getString(R.string.physics),selected_user_id,selected_class_id);
                break;
            case 2:
                fragment= SectionPerformanceFragment.newInstance("2",getString(R.string.chemistry),selected_user_id,selected_class_id);
                break;
            case 3:
                fragment= SectionPerformanceFragment.newInstance("4",getString(R.string.mathematics),selected_user_id,selected_class_id);
                break;
            case 4:
                fragment= SectionPerformanceFragment.newInstance("3",getString(R.string.biology),selected_user_id,selected_class_id);
                break;

        }
        if(fragment==null)
            return;

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.container,fragment).commit();
        AppController.getInstance().playAudio(R.raw.subject_sounds);
    }
}