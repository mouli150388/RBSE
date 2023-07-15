package com.tutorix.tutorialspoint.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.SplashScreen;
import com.tutorix.tutorialspoint.activities.ImagePreviewActivity;
import com.tutorix.tutorialspoint.activities.JoinReferralActivity;
import com.tutorix.tutorialspoint.activities.VocationCoursesActivity;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.doubts.DoubtsActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.subjects.SubjectActivity;
import com.tutorix.tutorialspoint.testseries.TestSeriesActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.MyBounceInterpolator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    @BindView(R.id.logo)
    de.hdodenhof.circleimageview.CircleImageView logo;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.classname)
    TextView classname;
    @BindView(R.id.txt_phy)
    ImageView txtPhy;
    @BindView(R.id.txt_che)
    ImageView txtChe;
    @BindView(R.id.txt_math)
    ImageView txtMath;
    @BindView(R.id.txt_bio)
    ImageView txtBio;

    @BindView(R.id.lnr_bio)
    LinearLayout lnr_bio;
    @BindView(R.id.lnr_math)
    LinearLayout lnr_math;
    @BindView(R.id.lnr_phy)
    LinearLayout lnr_phy;
    @BindView(R.id.lnr_che)
    LinearLayout lnr_che;
    @BindView(R.id.expertsLayout)
    LinearLayout expertsLayout;
    @BindView(R.id.lnr_share)
    FrameLayout lnr_share;
    @BindView(R.id.referral_layout)
    FrameLayout referral_layout;

    @BindView(R.id.btn_language)
    Button btn_language;
    @BindView(R.id.radi_grp_language)
    RadioGroup radi_grp_language;

    @BindView(R.id.btn_english)
    RadioButton btn_english;

    @BindView(R.id.btn_hindi)
    RadioButton btn_hindi;

    @BindView(R.id.lnr_language_selection)
    LinearLayout lnr_language_selection;
    @BindView(R.id.lnr_sujects_hide)
    LinearLayout lnr_sujects_hide;

    UserProfile profile;

    String access_token;
    String userid;
    String classId;
    String loginType;
    String session_device_id;
    Activity _this;
    @BindView(R.id.lnr_classes_all)
    LinearLayout lnrClassesAll;
    @BindView(R.id.lnr_j_math)
    LinearLayout lnrJMath;
    @BindView(R.id.lnr_j_phy)
    LinearLayout lnrJPhy;
    @BindView(R.id.lnr_j_che)
    LinearLayout lnrJChe;
    @BindView(R.id.lnr_jee)
    LinearLayout lnrJee;
    @BindView(R.id.lnr_n_phy)
    LinearLayout lnrNPhy;
    @BindView(R.id.lnr_n_che)
    LinearLayout lnrNChe;
    @BindView(R.id.lnr_n_bio)
    LinearLayout lnrNBio;
    @BindView(R.id.lnr_neet)
    LinearLayout lnrNeet;
    @BindView(R.id.lnr_j_test)
    LinearLayout lnr_j_test;
    @BindView(R.id.lnr_n_test)
    LinearLayout lnr_n_test;
    MyDatabase db;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @param
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this=getActivity();
        if (getArguments() != null) {

        }
    }
    public void onResume() {
        super.onResume();
        AppConfig.setLanguages(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lnr_bio.setSoundEffectsEnabled(false);
        txtBio.setSoundEffectsEnabled(false);
        lnr_che.setSoundEffectsEnabled(false);
        txtChe.setSoundEffectsEnabled(false);
        lnr_math.setSoundEffectsEnabled(false);
        txtMath.setSoundEffectsEnabled(false);
        lnr_phy.setSoundEffectsEnabled(false);
        txtPhy.setSoundEffectsEnabled(false);
        referral_layout.setSoundEffectsEnabled(false);
        String[] userInfo = SessionManager.getUserInfo(getActivity());
        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        session_device_id = userInfo[3];
        AppConfig.ACTIVATION_DATA_URL = userInfo[5];
        AppConfig.NORMAL_DATA_URL = userInfo[6];

        try {
            int currrentClsId = Integer.parseInt(classId);
            if (currrentClsId <=7) {
                lnrClassesAll.setVisibility(View.VISIBLE);
                lnrNeet.setVisibility(View.GONE);
                lnrJee.setVisibility(View.GONE);
            } else if (currrentClsId ==8) {
                lnrClassesAll.setVisibility(View.GONE);
                lnrNeet.setVisibility(View.GONE);
                lnrJee.setVisibility(View.VISIBLE);
            } else if (currrentClsId ==9){
                lnrClassesAll.setVisibility(View.GONE);
                lnrNeet.setVisibility(View.VISIBLE);
                lnrJee.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Shader textShader = new LinearGradient(0, 0, username.getWidth(), username.getTextSize(),
                new int[]{
                        Color.parseColor("#AE9AFC"),
                        Color.parseColor("#25A591"),
                        Color.parseColor("#4D9BE2"),
                        Color.parseColor("#E15F3E"),

                }, null, Shader.TileMode.CLAMP);
        username.getPaint().setShader(textShader);*/


        profile=MyDatabase.getDatabase(getActivity()).userDAO().getUserProfile(userid);
        if(profile==null)
        {
            SessionManager.logoutUser(_this);
            Intent intent = new Intent(_this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
            _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return;
        }
        String name = profile.full_name;
        if (name == null || name.trim().length() <= 0)
            name = getString(R.string.welcome) + " Guest";
        else name = getString(R.string.welcome)+" "+ name;
        username.setText(name);


        //classname.setText();

        String classId = userInfo[4];
        classname.setText(AppConfig.getClassNameDisplayClass(classId));
        /*if (classId.equalsIgnoreCase("1")) {
            classname.setText(R.string._6th);
        } else if (classId.equalsIgnoreCase("2")) {
            classname.setText(R.string._7th);
        } else if (classId.equalsIgnoreCase("3")) {
            classname.setText(R.string._8th);
        }*/

        setProfileLogo();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(profile==null)
                {
                    return;
                }
                String path = profile.photo;

                if (path!=null&&!path.isEmpty()) {
                    path = Constants.IMAGE_PATH + userid + "/" + path;
                    Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                    intent.putExtra("uri", path);
                    startActivity(intent);
                }
            }
        });
       // checkAccessToken();


        //long last_time=SharedPref.getLastTime(getActivity());
       // if(last_time==0||(Calendar.getInstance().getTimeInMillis() - last_time >= 24 * 60 * 60 * 1000) )
            ((HomeTabActivity)getActivity()).checkExpiery();


       /* if(AppConfig.ALERT_SHOULD_SHOW)
        {
            if(AppConfig.ALERT_TIME>0)
            {
                if((Calendar.getInstance().getTimeInMillis()-AppConfig.ALERT_TIME_TOTAL>=24*60*60*1000)||(Calendar.getInstance().getTimeInMillis()-AppConfig.ALERT_TIME_TOTAL<=0))
                {
                    AppConfig.CALLED_EXPIERY=false;
                    ((HomeTabActivity)getActivity()).checkExpiery();
                } else if(Calendar.getInstance().getTimeInMillis()-AppConfig.ALERT_TIME>=4*60*60*1000)
                {
                    if(AppConfig.ACTIVATION_EXPIERY_DAYS>0)
                    {
                        ((HomeTabActivity)getActivity()).showExpieryAlert(AppConfig.ACTIVATION_EXPIERY_DAYS,(new SharedPref().getActivationStatus(getActivity()).equalsIgnoreCase("T")));
                    }
                }
            }
        }*/

        db=MyDatabase.getDatabase(getActivity());
        ActivationDetails aDetails = db.activationDAO().getActivationDetails(userid, classId);

        if (aDetails != null && aDetails.activation_key != null && !aDetails.activation_key.isEmpty()) {

            referral_layout.setVisibility(View.GONE);
        }else
        {
            referral_layout.setVisibility(View.VISIBLE);
        }

        try{
            lnr_language_selection.setVisibility(View.GONE);
            int crnt_cls=Integer.parseInt(classId)+5;
            Log.v("Current class","Current class "+crnt_cls+" "+AppConfig.HINDI_AVAILABLE_CLASSES);




           /* if(AppConfig.HINDI_AVAILABLE_CLASSES.contains(crnt_cls+""))
            {*/
                lnr_language_selection.setVisibility(View.VISIBLE);
                if(AppController.childQaulityAudio==0)
                    btn_english.setChecked(true);
                else if(AppController.childQaulityAudio==1)
                    btn_hindi.setChecked(true);


            if(AppConfig.HINDI_AVAILABLE_CLASSES.contains(crnt_cls+""))
            {
                AppController.childQaulityAudioVideo=AppController.childQaulityAudio;
            }
                radi_grp_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if(i==R.id.btn_english)
                        {
                            SessionManager.setAudio(AppController.getInstance(),0);

                        }else if(i==R.id.btn_hindi)
                        {
                            SessionManager.setAudio(AppController.getInstance(),1);
                        }
                        isShow=false;
                        AppController.childQaulityAudio=SessionManager.getAudio(AppController.getInstance());
                        if(AppConfig.HINDI_AVAILABLE_CLASSES.contains(crnt_cls+""))
                        {
                            AppController.childQaulityAudioVideo=AppController.childQaulityAudio;
                        }
                       startActivity(new Intent(getActivity(), SplashScreen.class));
                       getActivity().finish();
                        lnr_sujects_hide.setVisibility(View.VISIBLE);
                        radi_grp_language.setVisibility(View.GONE);
                    }
                });

            /*}else {
                lnr_language_selection.setVisibility(View.GONE);
            }*/
        }catch (Exception e)
        {

        }

    }
    private void setProfileLogo() {

        String path = profile.photo;

        if (path!=null&&!path.isEmpty()) {
            path = Constants.IMAGE_PATH + userid + "/" + path;

            Picasso.with(_this).load(path).placeholder(R.drawable.profile).into(logo);
        }else
        {
            Picasso.with(_this).load(R.drawable.profile).into(logo);
        }
        //logo.setVisibility(View.GONE);
    }


    int PROFILE_INTENT_REQUEST = 10001;



    public void subjectPhysics(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (checkActivation())
            startActivity(view, Constants.physics, Constants.physicsId);
    }

    public void subjectChemistry(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (checkActivation())
            startActivity(view, Constants.chemistry, Constants.chemistryId);
    }

    public void subjectBiology(View view) {

        AppController.getInstance().playAudio(R.raw.button_click);
        if (checkActivation())
            startActivity(view, Constants.biology, Constants.biologyId);
    }

    public void subjectMaths(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (checkActivation())
            startActivity(view, Constants.mathematics, Constants.mathsId);
    }

    private boolean checkActivation() {

        //FOr RBSE we removed this feature
        if(true)
        {
            return true;
        }
        if(loginType==null)
        {
            return false;
        }
        if (loginType.isEmpty()) {

            return true;
        }
         db=MyDatabase.getDatabase(getActivity());
        SDActivationDetails sdActivationDetails= db.sdActivationDAO().getActivationDetails(userid,classId);
        if(loginType.equalsIgnoreCase("O")){
        if(sdActivationDetails==null||sdActivationDetails.activation_key.isEmpty())
            {
               return true;
            }
        }

        if(!new SharedPref().getActivationStatus(getActivity()).equalsIgnoreCase("E"))
        {
            return true;
        }

        //ActivationDetails aDetails = db.activationDAO().getActivationDetails(userid, classId);
        String endDate = sdActivationDetails.activation_end_date;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SharedPref sh=new SharedPref();
        if(sh.getIsDateCheckFirsttime(getActivity()))
        {

            Date d = new Date();
            String cDate = sdf.format(d);
            sdActivationDetails.current_date=cDate;
            db.sdActivationDAO().updateActivationDate(sdActivationDetails.activation_end_date,cDate,classId,userid);
            sh.setIsFirstime(getActivity(),false);
        }else
        {

            Date d1, d2;
            try {
                d1 = sdf.parse(sdActivationDetails.current_date);
                Date d = new Date();
                if (d1.getTime() > d.getTime()) {
                    //sh = new SharedPref();
                    //sh.setExpired(getActivity(), true);
                    CommonUtils.showToast(getActivity(),"It seems issue with your device date, please select current date");
                    //showAlert(null);
                    return false;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        String android_id = Settings.Secure.getString( _this.getContentResolver(), Settings.Secure.ANDROID_ID);


       /* if (!session_device_id.isEmpty()&& !session_device_id.equalsIgnoreCase(android_id)) {
            showAlert("Activated in other Device, Please De-Activate that device and again Login to this device");
            return false;
        }*/

        if (!endDate.isEmpty()) {

            Date d = new Date();
            String cDate = sdf.format(d);
            Date d1, d2;
            try {
                d1 = sdf.parse(cDate);
                d2 = sdf.parse(endDate);



                if (d1.getTime()>=d2.getTime() ) {
//                    showAlert(null);
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return true;
            }


            return true;
        }

        String s = AppConfig.getSdCardPath(classId,getContext());
        if (!new File(s).exists()) {
            showAlert("Don't have SD card for the current class, Please insert and use application");
            return false;
        }
        return true;
    }

    private void showAlert(String msg) {
//        @SuppressLint("InflateParams")
//        View view = getLayoutInflater().inflate(R.layout.activation_end_layout, null);
//        final Dialog dialog = new Dialog(_this);
//        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        Button btn_cancel = view.findViewById(R.id.btn_cancel);
//        TextView dialog_message = view.findViewById(R.id.dialog_message);
//        Button btn_subscribe = view.findViewById(R.id.btn_subscribe);
//        if (msg != null) {
//            dialog_message.setText(msg);
//            btn_cancel.setText(getString(R.string.ok));
//            btn_subscribe.setVisibility(View.GONE);
//
//        }else
//        {
//            //btn_cancel.setVisibility(View.GONE);
//        }
//
//        btn_subscribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(dialog!=null&&dialog.isShowing())
//                dialog.dismiss();
//                Intent i = new Intent(_this, SubscribePrePage.class);
//                i.putExtra("flag", "M");
//                startActivity(i);
//                getActivity().finish();
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(dialog!=null&&dialog.isShowing())
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setContentView(view);
//        dialog.setCancelable(false);
//        dialog.show();
    }

    private void checkAccessToken() {
        if (!AppStatus.getInstance(_this).isOnline())
            return;
        final JSONObject fjson = new JSONObject();
        try {


            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(_this, true);
        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.CHECK_ACCESSTOKEN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();
                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);

                                if (!error) {

                                } else {
                                    //checkUnCookieThenPlay();
                                    MyDatabase dbHandler=MyDatabase.getDatabase(_this);
                                    dbHandler.userDAO().deleteUser(userid);
                                    SessionManager.logoutUser(_this);
                                    Intent i = new Intent(_this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    _this.finish();

                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                CommonUtils.showToast(getActivity(), error.getMessage());
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                CustomDialog.closeDialog();
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
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }
    public void experts(View view) {
        AppController.getInstance().playAudio(R.raw.qz_next);
       /* if(AppConfig.checkSDCardEnabled(_this,userid,classId)&&AppConfig.checkSdcard(classId))
        {
            startActivity(new Intent(_this, VocationCoursesActivity.class));
            _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return;
        }
        if (!AppStatus.getInstance(getActivity()).isOnline()) {
            CommonUtils.showToast(getActivity(), getString(R.string.no_internet));
            return;
        }*/
        startActivity(new Intent(_this, VocationCoursesActivity.class));
        _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    public void referral(View view) {
        AppController.getInstance().playAudio(R.raw.qz_next);

        if (!AppStatus.getInstance(getActivity()).isOnline()) {
            CommonUtils.showToast(getActivity(), getString(R.string.no_internet));
            return;
        }
        startActivity(new Intent(_this, JoinReferralActivity.class));
        _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    private void startActivity(View view, String subjectName, String subjectId) {

        if ((/*AppConfig.checkSDCardEnabled(_this,userid,classId)&&*/AppConfig.checkSdcard(classId,getContext())) || AppStatus.getInstance(getActivity()).isOnline()) {
            Intent i = new Intent(getActivity(), SubjectActivity.class);
            i.putExtra(Constants.subjectName, subjectName);
            i.putExtra(Constants.subjectId, subjectId);
           /* if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions transitionActivityOptions = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), view, "trans");
                startActivity(i, transitionActivityOptions.toBundle());
            }else
            {*/
                startActivity(i) ;
           // }
        } else {
            CommonUtils.showToast(getActivity(), getString(R.string.no_internet));
            //Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    boolean isShow=false;
    @OnClick({ R.id.btn_language,R.id.lnr_phy,R.id.lnr_che,R.id.lnr_bio,R.id.lnr_math, R.id.expertsLayout, R.id.lnr_share, R.id.referral_layout,R.id.lnr_j_math, R.id.lnr_j_phy, R.id.lnr_j_che, R.id.lnr_n_phy,
            R.id.lnr_n_che, R.id.lnr_n_bio,R.id.lnr_n_test,R.id.lnr_j_test})
    public void onViewClicked(View v) {
        if(getActivity()==null||_this==null)
        {
            return;
        }
        final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.button_scale);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        v.startAnimation(myAnim);
        switch (v.getId()) {
            case R.id.btn_language:
                if(!isShow) {
                    lnr_sujects_hide.setVisibility(View.GONE);
                    radi_grp_language.setVisibility(View.VISIBLE);
                    if(AppController.childQaulityAudio==0)
                        btn_english.setChecked(true);
                    else if(AppController.childQaulityAudio==1)
                        btn_hindi.setChecked(true);
                    isShow=true;
                    btn_hindi.setText(getString(R.string.hindi));
                    int crnt_cls=Integer.parseInt(classId)+5;
                    if(AppConfig.HINDI_AVAILABLE_CLASSES.contains(crnt_cls+""))
                    {
                        AppController.childQaulityAudioVideo=AppController.childQaulityAudio;
                    }
                }else {
                    lnr_sujects_hide.setVisibility(View.VISIBLE);
                    radi_grp_language.setVisibility(View.GONE);
                    isShow=false;
                }
                break;

            case R.id.lnr_share:
                if (!AppStatus.getInstance(_this).isOnline())
                {
                    CommonUtils.showToast(getActivity(),getString(R.string.no_internet));
                    return;
                }
                Intent i = new Intent(_this, DoubtsActivity.class);
                i.putExtra(Constants.subjectId, "0");
                startActivity(i);
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                //TabLayout.Tab tab =  ((HomeTabActivity)getActivity()).tab_layout.getTabAt(1);
                //tab.select();
                break;

                case R.id.txt_phy:
            case R.id.lnr_phy:
            case R.id.lnr_j_phy:
            case R.id.lnr_n_phy:
                subjectPhysics(v);
                break;

            case R.id.txt_che:
            case R.id.lnr_che:
            case R.id.lnr_j_che:
            case R.id.lnr_n_che:
                subjectChemistry(v);
                break;

            case R.id.txt_math:
            case R.id.lnr_math:
            case R.id.lnr_j_math:
                subjectMaths(v);
                break;

            case R.id.txt_bio:
            case R.id.lnr_bio:
            case R.id.lnr_n_bio:

                subjectBiology(v);
                break;
            case R.id.expertsLayout:
                experts(v);
                break;

            case R.id.referral_layout:
                referral(v);
                break;
            case R.id.lnr_n_test:
            case R.id.lnr_j_test:

                startActivity(new Intent(getActivity(), TestSeriesActivity.class));
                break;
        }
    }
    private void shareApp()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Tutorix ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://bit.ly/2ROKdEG");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
