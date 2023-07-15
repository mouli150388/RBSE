package com.tutorix.tutorialspoint.home;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.BuildConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.SyncService;
import com.tutorix.tutorialspoint.activities.SubscribeOnlineActivity;
import com.tutorix.tutorialspoint.activities.SupportScreen;
import com.tutorix.tutorialspoint.anaylysis.NewAnalysisFragment;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.doubts.DoubtsFragment;
import com.tutorix.tutorialspoint.fragments.MenuFragment;
import com.tutorix.tutorialspoint.fragments.ToolsFragment;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.search.SearchFragment;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.WebfontText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeTabActivity extends AppCompatActivity implements HomeView, View.OnTouchListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

//    @BindView(R.id.icon_support)
//    FloatingActionButton iconSupport;

    View view;
    View view2;
    View view3;
    View view4;
    View view5;

    String access_token;
    String userid;
    String classId;
    String loginType;
    String session_device_id;
    HomePresenteroImpl homePresenter;
    SynBroadcastNetworkReceiver synBroadcastNetworkReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.setLanguages(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);

        ButterKnife.bind(this);
        Intent appLinkIntent = getIntent();
        Uri appLinkData;
        if (appLinkIntent != null && ((appLinkData = appLinkIntent.getData()) != null)) {
            if ((appLinkData + "").contains("app-payment")) {
                if (new SessionManager().isLoggedIn(this)) {
                    String user_id = new SessionManager().getUserInfo(getApplicationContext())[0];
                    UserProfile profile = MyDatabase.getDatabase(getApplicationContext()).userDAO().getUserProfile(user_id);
                    if (profile != null) {
                        Intent paymetnIntent = new Intent(this, SubscribeOnlineActivity.class);
                        paymetnIntent.putExtra("flag", "M");
                        startActivityForResult(paymetnIntent, 200);
                    } else {
                        Intent i = new Intent(this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        return;
                    }

                } else {
                    Intent i = new Intent(this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                    return;
                }
            }


        }


        homePresenter = new HomePresenteroImpl(this, HomeTabActivity.this);
        initiateTabs();

        AppController.childQaulity = SessionManager.getVideoQuality(AppController.getInstance());
        AppController.childQaulityAudio = SessionManager.getAudio(AppController.getInstance());
        AppController.childQaulityText = SessionManager.getAudioText(AppController.getInstance());
//        iconSupport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), SupportScreen.class));
//        /*if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
//            ZohoSalesIQ.Chat.show();
//
//        } else {
//            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
//
//        }*/
//            }
//        });
//iconSupport.setOnTouchListener(this);


    }

    float dX;
    float dY;
    int lastAction;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    startActivity(new Intent(getApplicationContext(), SupportScreen.class));
                break;

            default:
                return false;
        }
        return true;
    }

    private void initiateTabs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
        }
        synBroadcastNetworkReceiver = new SynBroadcastNetworkReceiver();
        IntentFilter filter = new IntentFilter(Constants.NETWORKSTATUS);
        registerReceiver(synBroadcastNetworkReceiver, filter);

        view = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view.findViewById(R.id.txt_iimage)).setText(R.string.home_font);
        ((TextView) view.findViewById(R.id.txt_title)).setText(R.string.home);


        view2 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view2.findViewById(R.id.txt_iimage)).setText(R.string.search_font);
        ((TextView) view2.findViewById(R.id.txt_title)).setText(R.string.search);


        view3 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view3.findViewById(R.id.txt_iimage)).setText(R.string.perform_font);
        ((TextView) view3.findViewById(R.id.txt_title)).setText(R.string.perform);


        view4 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view4.findViewById(R.id.txt_iimage)).setText(R.string.tools_font);
        ((TextView) view4.findViewById(R.id.txt_title)).setText(R.string.tools);


        view5 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view5.findViewById(R.id.txt_iimage)).setText(R.string.menu_font);
        ((TextView) view5.findViewById(R.id.txt_title)).setText(R.string.menu);


        tab_layout.addTab(tab_layout.newTab().setCustomView(view));
        tab_layout.addTab(tab_layout.newTab().setCustomView(view3));
        tab_layout.addTab(tab_layout.newTab().setCustomView(view4));

        tab_layout.addTab(tab_layout.newTab().setCustomView(view2));
        tab_layout.addTab(tab_layout.newTab().setCustomView(view5));

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                loadFragment(tab.getPosition());
                toolbar.setVisibility(View.GONE);
                setTabColor(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:

                        break;
                    case 1:

                        //toolbar.setTitle(getResources().getString(R.string.doubt));

                        break;
                    case 2:
                        //toolbar.setTitle("Time Analysis");
                        break;
                    case 3:

                        break;
                    case 4:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        session_device_id = userInfo[3];
        AppConfig.ACTIVATION_DATA_URL = userInfo[5];
        AppConfig.NORMAL_DATA_URL = userInfo[6];
        loadFragment(0);


        if (/*AppConfig.checkSDCardEnabled(this, userid, classId) && */AppConfig.checkSdcard(classId,getApplicationContext())) {
            homePresenter.checkValidation();
            readAudioFiles();
        } else {
            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                homePresenter.setCookie();
                homePresenter.checkAccesToken();


            }
        }

       /* if (loginType.isEmpty()) {
            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                homePresenter.setCookie();
                homePresenter.checkAccesToken();

            }
        } else if (loginType.equalsIgnoreCase("O")) {

            if (AppConfig.checkSDCardEnabled(this, userid, classId) && AppConfig.checkSdcard(classId,getApplicationContext())) {
                homePresenter.checkValidation();
                readAudioFiles();
            } else {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    homePresenter.setCookie();
                    homePresenter.checkAccesToken();


                }
            }
        } else {
            homePresenter.checkValidation();
            readAudioFiles();
        }*/

       /* if (AppStatus.getInstance(getApplicationContext()).isOnline())
        {
            homePresenter.setCookie();
            homePresenter.checkAccesToken();
            if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {

                homePresenter.checkValidation();
            }else
            {
                homePresenter.checkValidation();
                readAudioFiles();
            }

        }else
        {
            if (loginType.equalsIgnoreCase("S")) {

                homePresenter.checkValidation();
                readAudioFiles();


            }
        }*/


        setTabColor(0);


    }

    boolean isCalled;

    Fragment fragment = null;

    @Override
    protected void onStart() {
        super.onStart();
        if (!loginType.equalsIgnoreCase("S"))
            if (!isCalled) {
                checkCookieThenPlay();
            }
        //if(!AppConfig.CALLED_EXPIERY)
        // checkExpiery();
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            setRefreshToken();

            SharedPref sh = new SharedPref();
            if (sh.isExpired(getApplicationContext()) || !AppConfig.checkSDCardEnabled(getApplicationContext(), userid, classId)) {
                callSDEnableStatus(loginType);

            }

        }

    }

    private void loadFragment(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
        }

        toolbar.setVisibility(View.GONE);
        switch (position) {
            case 0:

                fragment = HomeFragment.newInstance();
                break;
          /*  case 1:
                fragment= DoubtsFragment.newInstance();
                break;*/
            case 1:
                fragment = NewAnalysisFragment.newInstance();
                break;
            case 2:

                fragment = ToolsFragment.newInstance();
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.statusbar_color_new));
                }*/
                break;
            case 3:
                fragment = SearchFragment.newInstance();
                break;
            case 4:
                fragment = MenuFragment.newInstance();
                break;
        }

        if (fragment == null)
            return;
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(
               /* R.animator.object_animator_fadein,
                R.animator.object_animator_fadeout*/
                R.animator.object_animator_enter_from_right_fadein,
                R.animator.object_animator_exit_to_right_fadeout

              /*  R.animator.card_flip_right_in,
                R.animator.card_flip_right_out,
                R.animator.card_flip_left_in,
                R.animator.card_flip_left_out*/

        );
        transaction.replace(R.id.container, fragment);
        transaction.commit();
        /*if(asound==null)
         asound = new AnimationSound(HomeTabActivity.this, R.raw.flip);
        asound.startsound();*/
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                asound.startsound();
            }
        },500);*/
        AppController.getInstance().playAudio(R.raw.menu_sound);
       /*if(position!=2||position!=1) {
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   AppController.getInstance().playAudio(R.raw.qz_next);
               }
           },500);

       }*/
    }

    TextView tab_icon;
    TextView tab_title;

    private void setTabColor(int which) {

        for (int k = 0; k < tab_layout.getTabCount(); k++) {
            view = tab_layout.getTabAt(k).view;
            tab_icon = view.findViewById(R.id.txt_iimage);
            tab_title = view.findViewById(R.id.txt_title);

            if (which == k) {
                tab_icon.setTextColor(getResources().getColor(R.color.colorPrimary));
                tab_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tab_icon.setTextColor(getResources().getColor(R.color.tab_default_text_color));
                tab_title.setTextColor(getResources().getColor(R.color.tab_default_text_color));
            }
        }
        view = getLayoutInflater().inflate(R.layout.custom_tab_item, null);

        view2 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view2.findViewById(R.id.txt_iimage)).setText(R.string.doubt_font);
        ((TextView) view2.findViewById(R.id.txt_title)).setText(R.string.doubt);
        view3 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view3.findViewById(R.id.txt_iimage)).setText(R.string.perform_font_2);
        ((TextView) view3.findViewById(R.id.txt_title)).setText(R.string.perform);
        view4 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view4.findViewById(R.id.txt_iimage)).setText(R.string.bookmark_font);
        ((TextView) view4.findViewById(R.id.txt_title)).setText(R.string.bookmarks);
        view5 = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        ((WebfontText) view5.findViewById(R.id.txt_iimage)).setText(R.string.menu_font);
        ((TextView) view5.findViewById(R.id.txt_title)).setText(R.string.menu);
    }

    public class SynBroadcastNetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = Objects.requireNonNull(connMgr).getActiveNetworkInfo();

            if (activeNetwork != null) {
                try {
                    if (networkAlert != null && networkAlert.isShowing())
                        networkAlert.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Intent i = new Intent(context, SyncService.class);  //is any of that needed?  idk.

                if (/*AppConfig.checkSDCardEnabled(getApplicationContext(), userid, classId) && */AppConfig.checkSdcard(classId,getApplicationContext()))
                    SyncService.enqueueWork(context, i);
                /*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {

                }else
                    SyncService.enqueueWork(context, i);*/

                if (!AppConfig.CALLED_EXPIERY)
                    checkExpiery();

            } else {
                // not connected to the internet
                /*if (loginType.equalsIgnoreCase("O") || loginType.isEmpty())
                        showNetworkAlert();*/
                if (/*!(AppConfig.checkSDCardEnabled(getApplicationContext(), userid, classId) && */AppConfig.checkSdcard(classId,getApplicationContext()))
                    showNetworkAlert();
            }


        }
    }

    @Override
    protected void onDestroy() {
        Constants.isOpened = false;
        if (synBroadcastNetworkReceiver != null)
            unregisterReceiver(synBroadcastNetworkReceiver);
        super.onDestroy();
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.v("Requestcode","Requestcode "+requestCode);
        if (requestCode == 200 && resultCode == 200) {
            String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

            access_token = userInfo[1];
            userid = userInfo[0];
            loginType = userInfo[2];
            classId = userInfo[4];
            session_device_id = userInfo[3];

            //AppConfig.ACTIVATION_DATA_URL = userInfo[5];
            AppConfig.NORMAL_DATA_URL = userInfo[6];

        } else if (requestCode == 100) {
            if (tab_layout.getSelectedTabPosition() == 1) {
                if (fragment != null && fragment instanceof DoubtsFragment)
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void showAlert(String msg) {
//        @SuppressLint("InflateParams")
//        View view = getLayoutInflater().inflate(R.layout.activation_end_layout, null);
//        final Dialog dialog = new Dialog(HomeTabActivity.this);
//        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        Button btn_cancel = view.findViewById(R.id.btn_cancel);
//        TextView dialog_message = view.findViewById(R.id.dialog_message);
//        Button btn_subscribe = view.findViewById(R.id.btn_subscribe);
//        if (msg != null) {
//            dialog_message.setText(msg);
//            btn_cancel.setText("Ok");
//            btn_subscribe.setVisibility(View.GONE);
//        }
//
//        btn_subscribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Intent i = new Intent(HomeTabActivity.this, SubscribePrePage.class);
//                i.putExtra("flag", "M");
//                startActivity(i);
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setContentView(view);
//        dialog.setCancelable(false);
//        dialog.show();
    }

    Dialog dialog;

    @Override
    public void callLogin() {
        SessionManager.logoutUser(getApplicationContext());
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    @Override
    public void showLoading(String msg) {
        showAlert(msg);
    }

    @Override
    public void cloaseLoading() {

    }

    boolean isback = false;

    @Override
    public void onBackPressed() {
        if (tab_layout.getSelectedTabPosition() != 0) {

            TabLayout.Tab tab = tab_layout.getTabAt(0);
            tab.select();
            //loadFragment(0);
            //setTabColor(0);
            return;
        }

        if (!isback) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isback = false;
                }
            }, 3000);
            CommonUtils.showToast(getApplicationContext(), "Press again to exit");
            isback = true;
            return;
        }
        Constants.isOpened = false;
        AppConfig.CALLED_EXPIERY = false;
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Constants.isOpened = false;
    }

    private String getAudiLanguageString() {


        String sdCardPath = "";
        if ((sdCardPath = AppConfig.getSdCardPath(classId,getApplicationContext())) != null) {
            String filepath = sdCardPath + "audio_languages.json";
            String s = AppConfig.readFromFile(filepath);
            try {
                JSONObject obj = new JSONObject(s);

                return obj.getString("languages");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return AppConfig.readFromFile(filepath);
        }
        return "";
       /* else
        {
           return  "[\"english\"]";
        }*/


    }


    private void checkCookieThenPlay() {
        //fillWithData();
        String encryption = "";
        String encryption2 = "";
        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classId);

            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption2 = URLEncoder.encode(encryption, "utf-8");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, Constants.CHECK_COOKIES + "?json_data=" + encryption2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (getApplicationContext() == null) {
                        return;
                    }

                    CustomDialog.closeDialog();
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("flag") && obj.getInt("flag") == 1)
                        Constants.isHadCookie = true;
                    else
                        Constants.isHadCookie = false;

                    isCalled = true;

                    //CookieManager manager = new CookieManager();
                    // manager.getCookieStore().removeAll();
                    // manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                    //CookieHandler.setDefault(manager);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                //loadingPanelID.hide();

            }
        });/*{
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };*/
        if (loginType.equalsIgnoreCase("O") || loginType.equalsIgnoreCase("")) {
            //loadingPanelID.show();

            AppController.getInstance().addToRequestQueue(request);
        }

    }


    public void checkExpiery() {
        //fillWithData();
        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];

        final JSONObject fjson = new JSONObject();
        try {

            fjson.put(Constants.userId, userid);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.accessToken, access_token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.POST, Constants.CHECK_EXPIERY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Log.v("response","response"+response);
                    //loadingPanelID.hide();
                    if (getApplicationContext() == null) {
                        return;
                    }
                    CustomDialog.closeDialog();
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error_flag")) {
                        int days = obj.getInt("activation_end_in_days");
                        String secure_url = obj.getString("secure_url");
                        String activation_status = obj.getString("activation_status");
                        //AppConfig.ACTIVATION_DATA_URL =secure_url;
                        SessionManager.setSecureURL(secure_url, getApplicationContext());
                        AppConfig.ACTIVATION_DATA_URL = secure_url;
                        //AppConfig.ALERT_TIME_TOTAL=Calendar.getInstance().getTimeInMillis();


                        if (activation_status.equalsIgnoreCase("T")) {
                            if (days <= 13)
                                showExpieryAlert(days, true);
                            SharedPref sh = new SharedPref();
                            sh.setExpired(getApplicationContext(), false);
                            sh.setActiveStatus(getApplicationContext(), "T");
                        } else if (activation_status.equalsIgnoreCase("D")) {
                            SharedPref sh = new SharedPref();
                            sh.setExpired(getApplicationContext(), true);
                            sh.setActiveStatus(getApplicationContext(), "D");
                            showExpieryAlert(0, true);
                        } else if (activation_status.equalsIgnoreCase("E")) {
                            SharedPref sh = new SharedPref();
                            sh.setExpired(getApplicationContext(), true);
                            sh.setActiveStatus(getApplicationContext(), "E");
                        } else {
                            if (days <= 13)
                                showExpieryAlert(days, false);

                            SharedPref sh = new SharedPref();
                            sh.setExpired(getApplicationContext(), false);
                            sh.setActiveStatus(getApplicationContext(), "A");
                        }
                        AppConfig.CALLED_EXPIERY = true;
                    } else {

                    }

                    //checkCookieThenPlay();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                //loadingPanelID.hide();

            }
        }) {

            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };

        ;
        /*{
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };*/
        //if (loginType.equalsIgnoreCase("O")||loginType.equalsIgnoreCase("")) {
        //loadingPanelID.show();

        AppController.getInstance().addToRequestQueue(request);
        //}

    }

    Dialog dialogExpeiry;

    public void showExpieryAlert(int days, boolean isTrial) {
//        long last_time = SharedPref.getLastTime(getApplicationContext());
//        if (last_time == 0 || (Calendar.getInstance().getTimeInMillis() - last_time >= 24 * 60 * 60 * 1000)) {
//            SharedPref.setLastTime(getApplicationContext(), Calendar.getInstance().getTimeInMillis());
//            try {
//
//                if (dialogExpeiry != null && dialogExpeiry.isShowing())
//                    dialogExpeiry.dismiss();
//                dialogExpeiry = null;
//                View view = getLayoutInflater().inflate(R.layout.activation_end_layout, null);
//                dialogExpeiry = new Dialog(HomeTabActivity.this);
//                Objects.requireNonNull(dialogExpeiry.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                Button btn_cancel = view.findViewById(R.id.btn_cancel);
//                TextView dialog_message = view.findViewById(R.id.dialog_message);
//                Button btn_subscribe = view.findViewById(R.id.btn_subscribe);
//
//                if (days <= 0) {
//                    btn_cancel.setText("Cancel");
//                    SharedPref sh = new SharedPref();
//                    sh.setExpired(getApplicationContext(), true);
//                    if (isTrial)
//                        dialog_message.setText("Your trial period has already expired, we recommend you to activate your subscription to unlock all the features of Tutorix.");
//                } else {
//                    SharedPref sh = new SharedPref();
//                    sh.setExpired(getApplicationContext(), false);
//
//                    if (!isTrial) {
//                        dialog_message.setText("Your activation expires in the next " + days + " days. You can upgrade to one of our full plans anytime");
//
//
//                    } else {
//                        if (days == 0)
//                            dialog_message.setText("Your trial period has already expired, we recommend you to activate your subscription to unlock all the features of Tutorix.");
//                        else
//                            dialog_message.setText("Your trial period will expire in " + days + " days, we recommend you to activate your subscription to continue enjoying all the features of Tutorix.");
//                    }
//                    //btn_cancel.setText("Ok");
//                    //btn_subscribe.setVisibility(View.GONE);
//
//                }
//                btn_subscribe.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogExpeiry.dismiss();
//                        Intent i = new Intent(getApplicationContext(), SubscribePrePage.class);
//                        i.putExtra("flag", "M");
//                        startActivity(i);
//
//                    }
//                });
//                btn_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogExpeiry.dismiss();
//                    }
//                });
//
//                dialogExpeiry.setContentView(view);
//                dialogExpeiry.setCancelable(false);
//                AppConfig.ACTIVATION_EXPIERY_DAYS = days;
//                SharedPref.setLastTime(getApplicationContext(), Calendar.getInstance().getTimeInMillis());
//                if (isTrial) {
//                    btn_cancel.setText(getString(R.string.contin));
//                    btn_subscribe.setText(getString(R.string.activate_now));
//                } else {
//                    btn_cancel.setText(getString(R.string.contin));
//                    btn_subscribe.setText(getString(R.string.upgrade_now));
//                }
//                try {
//                    if (getApplicationContext() != null)
//                        dialogExpeiry.show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                   /* if (isTrial || (days <= 7 && days > 0)) {
//                        //AppConfig.ALERT_SHOULD_SHOW = true;
//                        //AppConfig.ALERT_TIME = Calendar.getInstance().getTimeInMillis();
//                        AppConfig.ACTIVATION_EXPIERY_DAYS = days;
//                        btn_subscribe.setText("Upgrade Now");
//                        if (isTrial)
//                            btn_cancel.setText("Continue with Trial");
//                        else{
//                            btn_cancel.setText("Continue");
//                            btn_subscribe.setText("Renew Now");
//                        }
//                        try {
//                            if (getApplicationContext() != null)
//                                dialogExpeiry.show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }*/
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//
//        }

    }

    Dialog networkAlert;

    private void showNetworkAlert() {
//
//        try {
//            if (networkAlert != null && networkAlert.isShowing())
//                networkAlert.dismiss();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        View view = getLayoutInflater().inflate(R.layout.activation_end_layout, null);
//        networkAlert = new Dialog(HomeTabActivity.this);
//        Objects.requireNonNull(networkAlert.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        Button btn_cancel = view.findViewById(R.id.btn_cancel);
//        TextView dialog_message = view.findViewById(R.id.dialog_message);
//        Button btn_subscribe = view.findViewById(R.id.btn_subscribe);
//        btn_subscribe.setVisibility(View.GONE);
//        dialog_message.setText("No Network available\n Please check your internet");
//        btn_cancel.setText("OK");
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                networkAlert.dismiss();
//                /*final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo activeNetwork = Objects.requireNonNull(connMgr).getActiveNetworkInfo();
//                if (activeNetwork != null) {
//                    Intent in = new Intent(getApplicationContext(), HomeTabActivity.class);
//                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(in);
//                }
//                finish();*/
//            }
//        });
//
//        networkAlert.setContentView(view);
//        networkAlert.setCancelable(false);
//        networkAlert.show();
    }

    @Override
    protected void onRestart() {
        try {

          /*  long last_time=SharedPref.getLastTime(getApplicationContext());
            if(last_time==0||(Calendar.getInstance().getTimeInMillis() - last_time >= 24 * 60 * 60 * 1000) )*/
            if (!AppConfig.CALLED_EXPIERY)
                checkExpiery();

            /*if (AppConfig.ALERT_SHOULD_SHOW) {
                if (AppConfig.ALERT_TIME > 0) {
                    if ((Calendar.getInstance().getTimeInMillis() - AppConfig.ALERT_TIME_TOTAL >= 24 * 60 * 60 * 1000) || (Calendar.getInstance().getTimeInMillis() - AppConfig.ALERT_TIME_TOTAL <= 0)) {
                        AppConfig.CALLED_EXPIERY = false;
                        checkExpiery();
                    } else if (Calendar.getInstance().getTimeInMillis() - AppConfig.ALERT_TIME >= 4 * 60 * 60 * 1000) {
                        if (AppConfig.ACTIVATION_EXPIERY_DAYS > 0) {
                            showExpieryAlert(AppConfig.ACTIVATION_EXPIERY_DAYS,(new SharedPref().getActivationStatus(getApplicationContext()).equalsIgnoreCase("T")));
                        }
                    }
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onRestart();
    }


    private boolean checkPermissionForStorage() {
        if(Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
                return  false;
            } else return true;
        }else {
            int  result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void readAudioFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionForStorage()) {
                try {
                    JSONArray array = new JSONArray(getAudiLanguageString());
                    if (array.length() > 0) {
                        Constants.LANG_VIDEO_SUPPORT = new String[array.length()];
                    }
                    for (int k = 0; k < array.length(); k++) {
                        Constants.LANG_VIDEO_SUPPORT[k] = array.getString(k);
                        //Log.v("Language","Language "+array.getString(k));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if(Build.VERSION.SDK_INT >= 30) {
                    return;
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
            }
        } else {
            try {
                JSONArray array = new JSONArray(getAudiLanguageString());
                if (array.length() > 0) {
                    Constants.LANG_VIDEO_SUPPORT = new String[array.length()];
                }
                for (int k = 0; k < array.length(); k++) {
                    Constants.LANG_VIDEO_SUPPORT[k] = array.getString(k);
                    //Log.v("Language","Language "+array.getString(k));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 300:
                if (grantResults.length > 0) {
                    boolean galleryaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (galleryaccepted) {
                        //readAudioFiles();
                        if (checkPermissionForStorage()) {
                            try {
                                JSONArray array = new JSONArray(getAudiLanguageString());
                                if (array.length() > 0) {
                                    Constants.LANG_VIDEO_SUPPORT = new String[array.length()];
                                }
                                for (int k = 0; k < array.length(); k++) {
                                    Constants.LANG_VIDEO_SUPPORT[k] = array.getString(k);
                                    //Log.v("Language","Language "+array.getString(k));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            CommonUtils.showToast(getApplicationContext(), "Give Permissions in Permission llist from Settings");
                        }
                    } else {
                        CommonUtils.showToast(getApplicationContext(), "Give Permissions in Permission llist from Settings");
                        //Toast.makeText(SubjectActivity.this, "Access Required", Toast.LENGTH_SHORT).show();
                        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);

                    }
                }
                break;
            default:
                break;
        }
    }


    private void setRefreshToken() {

        //fillWithData();
        String encryption = "";

        final JSONObject fjson = new JSONObject();
        try {
            final String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            fjson.put(Constants.userId, userid);
            fjson.put(Constants.deviceId, android_id);


            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                fjson.put(Constants.app_version, pInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, Constants.SET_REFRESH_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    if (getApplicationContext() == null) {
                        return;
                    }
                    CustomDialog.closeDialog();
                    JSONObject obj = new JSONObject(response);

                    if (obj.has("doubt_filter_flag")) {
                        CommonUtils.doubt_filter_version = obj.getInt("doubt_filter_flag");
                    }
                    if (!obj.getBoolean("error_flag")) {

                        if (obj.getInt("update_flag") == 1) {
                            access_token = obj.getString("access_token");

                            SessionManager.setUserAccessToken(getApplicationContext(), access_token);
                            MyDatabase.getDatabase(getApplicationContext()).userDAO().updateAccessToken(userid, access_token);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                //loadingPanelID.hide();

            }
        }) {
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);

    }

    private void callSDEnableStatus(String activation_type) {


        final String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //fillWithData();
        String encryption = "";
        String encryption2 = "";
        final JSONObject fjson = new JSONObject();
        try {


            fjson.put(Constants.userId, userid);
            fjson.put(Constants.deviceId, android_id);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("activation_type", activation_type);

            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption2 = URLEncoder.encode(encryption, "utf-8");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, Constants.SDCARD_ACTIVATION + "/" + classId + "?json_data=" + encryption2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Log.v("response","response"+response);
                    //loadingPanelID.hide();
                    try {
                        if (getApplicationContext() == null) {
                            return;
                        }
                        CustomDialog.closeDialog();
                    } catch (Exception e) {

                    }
                    JSONObject obj = new JSONObject(response);
                    //Log.v("Response","Response "+obj.toString());
                    if (!obj.getBoolean("error_flag")) {
                        String activation_key_sd = obj.getString("activation_key_sd");
                        String end_date = obj.getString("end_date");
                        String activation_type = obj.getString("activation_type_online");
                        String activation_key_online = obj.getString("activation_key_online");
                        String activation_start_date_online = obj.getString("activation_start_date_online");

                        String current_date = obj.getString("current_date");
                        String data_url = obj.getString("data_url");
                        String secure_url = obj.getString("secure_url");
                        String device_id = obj.getString("device_id");
                        new SharedPref().setIsFirstime(getApplicationContext(), false);

                        if (!activation_key_sd.isEmpty()) {
                            SDActivationDetails sdActivationDetails = new SDActivationDetails();
                            sdActivationDetails.user_id = userid;
                            sdActivationDetails.class_id = classId;
                            sdActivationDetails.activation_key = activation_key_sd;
                            sdActivationDetails.activation_end_date = end_date;
                            sdActivationDetails.device_id = android_id;
                            sdActivationDetails.current_date = current_date;

                            SDActivationDetails sdactivationDetails = MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().getActivationDetails(userid, classId);
                            if (sdactivationDetails == null)
                                MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().inserActivation(sdActivationDetails);
                            else
                                MyDatabase.getDatabase(getApplicationContext()).sdActivationDAO().updateActivationDetails(sdActivationDetails.activation_end_date, sdActivationDetails.activation_key, sdActivationDetails.current_date, sdActivationDetails.class_id, sdActivationDetails.user_id, sdActivationDetails.device_id);


                            if (!activation_key_online.isEmpty()) {
                                ActivationDetails aDeails = new ActivationDetails();
                                aDeails.current_date = current_date;
                                aDeails.user_id = userid;
                                aDeails.activation_type = activation_type;
                                aDeails.activation_key = activation_key_online;
                                aDeails.activation_start_date = activation_start_date_online;
                                aDeails.activation_end_date = end_date;
                                aDeails.class_id = classId;
                                aDeails.secure_url = secure_url;
                                aDeails.data_url = data_url;

                                aDeails.device_id = device_id;
                                String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
                                SessionManager s = new SessionManager();
                                s.setLogin(getApplicationContext(), userid, userInfo[1], device_id, activation_type, classId, secure_url, data_url, userInfo[7]);
                                loginType = activation_type;
                                long days = 0;
                                if (activation_key_online.length() > 2) {
                                    days = CommonUtils.daysBetween(activation_start_date_online, end_date);
                                }
                                aDeails.days_left = days + "";
                                ActivationDetails activationDetails = MyDatabase.getDatabase(getApplicationContext()).activationDAO().getActivationDetails(userid, classId);

                                if (activationDetails == null)
                                    MyDatabase.getDatabase(getApplicationContext()).activationDAO().inserActivation(aDeails);
                                else
                                    MyDatabase.getDatabase(getApplicationContext()).activationDAO().updateActivationDetails(aDeails.activation_end_date, aDeails.activation_start_date, aDeails.activation_type, aDeails.activation_key, aDeails.current_date, aDeails.days_left, aDeails.class_id, aDeails.user_id, aDeails.secure_url, aDeails.data_url);

                            }

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                //loadingPanelID.hide();
                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {

                }
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = getResources().getString(R.string.error_4);
                }

            }
        });/*{
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };*/
        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Intent appLinkIntent = intent;
        Uri appLinkData;
        if (appLinkIntent != null && ((appLinkData = appLinkIntent.getData()) != null)) {


            if ((appLinkData + "").contains("app-payment")) {
                if (new SessionManager().isLoggedIn(this)) {
                    String user_id = new SessionManager().getUserInfo(getApplicationContext())[0];
                    UserProfile profile = MyDatabase.getDatabase(getApplicationContext()).userDAO().getUserProfile(user_id);
                    if (profile != null) {
                        Intent paymetnIntent = new Intent(this, SubscribeOnlineActivity.class);
                        paymetnIntent.putExtra("flag", "M");
                        startActivityForResult(paymetnIntent, 200);
                    } else {
                        Intent i = new Intent(this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        return;
                    }

                } else {
                    Intent i = new Intent(this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                    return;
                }
            }


        }
    }
}
