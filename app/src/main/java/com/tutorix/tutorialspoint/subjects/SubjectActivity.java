package com.tutorix.tutorialspoint.subjects;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.ChaptersAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.Chapters;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectActivity extends AppCompatActivity implements SubjectView{

    @BindView(R.id.lnr_top)
    LinearLayout lnr_top;
    @BindView(R.id.img_gif)
    ImageView imgGif;
    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.list_grid)
    CheckBox listGrid;
    @BindView(R.id.lnr_gridlist)
    LinearLayout lnrGridlist;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.lnr_home)
    LinearLayout lnrHome;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /*@BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;*/
    @BindView(R.id.lnr_reload)
    LinearLayout lnrReload;
    @BindView(R.id.img_nodata)
    ImageView imgNodata;
    @BindView(R.id.img_background)
    ImageView img_background;
    @BindView(R.id.rel_top_main)
    RelativeLayout rel_top_main;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.lnr_nosdcard)
    LinearLayout lnrNosdcard;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    SubjectActivity _this;
    SharedPreferences userInfo;
    private String classId;
    private String userId;
    private String subjectId, subjectName;
    String access_token;
    String loginType;
    String BaseURL;
    List<Chapters> data;
    ChaptersAdapter chaptersAdapter;
    int gride_count;
    MyDatabase dbhelper;

    SubjectPresentorImpl subjectPresentor;
    public void onResume() {
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subjectId = extras.getString(Constants.subjectId);
            subjectName = extras.getString(Constants.subjectName);
        }
        String _subjectName = subjectName.substring(0, 1).toUpperCase() + subjectName.substring(1);
        subjectPresentor=new SubjectPresentorImpl(this,SubjectActivity.this,subjectId,subjectName);
        gride_count = getResources().getInteger(R.integer.gride_count);
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userId = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        dbhelper = MyDatabase.getDatabase(SubjectActivity.this);

        data = new ArrayList<>();
       // initCollapsingToolbar();
        listGrid.setVisibility(View.VISIBLE);
        lnrGridlist.setVisibility(View.VISIBLE);

        listGrid.setChecked(SessionManager.isViewList(getApplicationContext()));


        imgGif.setVisibility(View.VISIBLE);

        if (subjectName.equalsIgnoreCase("physics")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_phy).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_phy_bg_green);
            //img_background.setImageResource(R.drawable.ic_phy_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_phy_bg_white).into(img_background);
            txt_header.setText(getString(R.string.physics));

        } else if (subjectName.equalsIgnoreCase("chemistry")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_che).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            //Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
            rel_top_main.setBackgroundResource(R.drawable.ic_chemistry_bg_green);
            //img_background.setImageResource(R.drawable.ic_chemistry_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
            txt_header.setText(getString(R.string.chemistry));

        } else if (subjectName.equalsIgnoreCase("biology")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_bio).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_bio_bg_green);
            //img_background.setImageResource(R.drawable.ic_bio_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_bio_bg_white).into(img_background);
            txt_header.setText(getString(R.string.biology));

        } else {

            Glide.with(getApplicationContext()).load(R.drawable.gif_math).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_math_bg_green);
            //img_background.setImageResource(R.drawable.ic_math_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_math_bg_white).into(img_background);
            txt_header.setText(getString(R.string.mathematics));

        }


        //loadingPanelID.hide();

        subjectPresentor.fetchData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subjectId) {
                case "1":
                    window.setStatusBarColor(getResources().getColor(R.color.phy_background_status));
                    break;
                case "2":
                    window.setStatusBarColor(getResources().getColor(R.color.che_background_status));
                    break;
                case "3":
                    window.setStatusBarColor(getResources().getColor(R.color.bio_background_status));
                    break;
                case "4":
                    window.setStatusBarColor(getResources().getColor(R.color.math_background_status));
                    break;
            }
        }
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return false;
    }

    public void home(View v) {
        Intent in = new Intent(SubjectActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void requestPermissionForStorage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 300);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 300:
                if (grantResults.length > 0) {
                    boolean galleryaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (galleryaccepted) {
                        subjectPresentor.fetchData();
                    } else {
                        CommonUtils.showToast(getApplicationContext(), "Give Permissions in Permission llist from Settings");

                    }
                }
                break;
            default:
                break;
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            AppController.getInstance().playAudio(R.raw.back_sound);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == 200) {
            if ( loginType.isEmpty()||!(AppConfig.checkSDCardEnabled(_this,userId,classId)&&AppConfig.checkSdcard(classId,getApplicationContext()))) {
                if (AppStatus.getInstance(SubjectActivity.this).isOnline()) {
                   setData();
                } else {
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
                    //Toasty.info(SubjectActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }

            }
        }
    }



    private void setData() {

        if (listGrid.isChecked()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(_this, RecyclerView.VERTICAL, false));
            chaptersAdapter = new ChaptersAdapter(data, SubjectActivity.this, BaseURL, false);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(_this, gride_count));
            chaptersAdapter = new ChaptersAdapter(data, SubjectActivity.this, BaseURL, true);
        }

        //recyclerView.setLayoutManager(new GridLayoutManager(_this, 2));
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(chaptersAdapter);
        //recyclerView.setHasFixedSize(true);
        chaptersAdapter.notifyDataSetChanged();
        //recyclerView.setItemViewCacheSize(20);
        //recyclerView.setDrawingCacheEnabled(true);
       // recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
           /* RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();

            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }*/
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.scheduleLayoutAnimation();
    }

    @OnClick({R.id.img_filter,  R.id.lnr_gridlist,  R.id.lnr_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_filter:
                break;
            case R.id.lnr_gridlist:
                listGrid.setChecked(!listGrid.isChecked());
                SessionManager.setListView(getApplicationContext(),listGrid.isChecked());
                setData();
                AppController.getInstance().playAudio(R.raw.button_click);
                break;
            case R.id.lnr_home:
                home(view);

                break;
        }
    }


    @Override
    public void loadData(List<Chapters> listData,String BaseURL) {
        data=listData;
        this.BaseURL=BaseURL;
        setData();
    }

    @Override
    public void showMessage(String msg) {
        CommonUtils.showToast(getApplicationContext(),msg);

        if(msg.contains("Invalid access token"))
        {
            MyDatabase dbHandler=MyDatabase.getDatabase(getApplicationContext());
            dbHandler.userDAO().deleteUser(userId);
            SessionManager.logoutUser(getApplicationContext());
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
           finish();
        }
    }

    @Override
    public void showNoSdcard() {

        lnrNosdcard.setVisibility(View.VISIBLE);
    }

    @Override
    public void shoaLoding() {

    }

    @Override
    public void cloaseLoding() {

    }
}
