package com.tutorix.tutorialspoint.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tutorix.tutorialspoint.activities.ActivationDetailsActivity;
import com.tutorix.tutorialspoint.activities.CompletedTaskActivity;
import com.tutorix.tutorialspoint.activities.ConnectusActivity;
import com.tutorix.tutorialspoint.activities.FeedbackActivity;
import com.tutorix.tutorialspoint.activities.ProfileActivity;
import com.tutorix.tutorialspoint.activities.SettingsActivity;
import com.tutorix.tutorialspoint.activities.SubscribeActivity;
import com.tutorix.tutorialspoint.activities.SubscribeOnlineActivity;
import com.tutorix.tutorialspoint.activities.SupportScreen;
import com.tutorix.tutorialspoint.activities.SwitchClassActivity;
import com.tutorix.tutorialspoint.activities.WebViewTestActivity;
import com.tutorix.tutorialspoint.classes.AddToBatchActivity;
import com.tutorix.tutorialspoint.classes.StudentsDashBoardActivity;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.doubts.DoubtsActivity;
import com.tutorix.tutorialspoint.dsw.DSWActivity;
import com.tutorix.tutorialspoint.dsw.RewardsActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.otp.DeleteOTPActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {


    Activity _this;
    String access_token;
    String userid;
    String classId;
    String loginType;
    String session_device_id;
    UserProfile profile;
    de.hdodenhof.circleimageview.CircleImageView logo;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_update, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _this = getActivity();
        initUI();
    }

    TextView username;

    TextView txt_subscribe;
    //    TextView txt_act_details;
    View lnr_profile;
    View lnr_switch;
    //    View lnr_buy;
//    View lnr_view;
    View lnr_doubt;
    View lnr_syllabus;
    View lnr_complete;
    //View lnr_orders;
    View lnr_support;
    View lnr_terms;
    View lnr_about;
    View lnr_faqs;
    //    View lnr_refund;
    View lnr_feedback;
    View lnr_logout;
    View lnr_privacy;
    //    View lnr_notification;
    View lnr_sounds;
    View lnr_dsw;
    //    View lnr_sd_card;
//    View lnr_rateapp;
    View lnr_rewards;
    View lnr_delete_account;
    View lnr_knowledge;
    View lnr_student_dashboard;
    View lnr_student_add;
    TextView txt_appversion;

    private void initUI() {
        View view = getView();
        logo = view.findViewById(R.id.logo);
        username = view.findViewById(R.id.username);

//        txt_act_details=view.findViewById(R.id.txt_act_details);
        txt_subscribe = view.findViewById(R.id.txt_subscribe);
        lnr_profile = view.findViewById(R.id.lnr_profile);
        lnr_switch = view.findViewById(R.id.lnr_switch);
//        lnr_buy=view.findViewById(R.id.lnr_buy);
//        lnr_view=view.findViewById(R.id.lnr_view);
        lnr_doubt = view.findViewById(R.id.lnr_doubt);
        lnr_dsw = view.findViewById(R.id.lnr_dsw);

        lnr_syllabus = view.findViewById(R.id.lnr_syllabus);
        lnr_complete = view.findViewById(R.id.lnr_complete);
        //lnr_orders=view.findViewById(R.id.lnr_orders);
        lnr_support = view.findViewById(R.id.lnr_support);
//        lnr_notification=view.findViewById(R.id.lnr_notification);
        lnr_terms = view.findViewById(R.id.lnr_terms);
        lnr_about = view.findViewById(R.id.lnr_about);
        lnr_faqs = view.findViewById(R.id.lnr_faqs);
//        lnr_refund=view.findViewById(R.id.lnr_refund);
        lnr_privacy = view.findViewById(R.id.lnr_privacy);
        lnr_feedback = view.findViewById(R.id.lnr_feedback);
        lnr_logout = view.findViewById(R.id.lnr_logout);
        lnr_sounds = view.findViewById(R.id.lnr_sounds);
//        lnr_sd_card=view.findViewById(R.id.lnr_sd_card);
//        lnr_rateapp = view.findViewById(R.id.lnr_rateapp);
        lnr_rewards = view.findViewById(R.id.lnr_rewards);
        txt_appversion = view.findViewById(R.id.txt_appversion);
        lnr_knowledge = view.findViewById(R.id.lnr_knowledge);
        lnr_delete_account = view.findViewById(R.id.lnr_delete_account);
        lnr_student_dashboard = view.findViewById(R.id.lnr_student_dashboard);
        lnr_student_add = view.findViewById(R.id.lnr_student_add);

        //logo.setOnClickListener(this);
        lnr_profile.setOnClickListener(this);

        lnr_switch.setOnClickListener(this);
//        lnr_buy.setOnClickListener(this);
//        lnr_view.setOnClickListener(this);
        lnr_doubt.setOnClickListener(this);
        lnr_syllabus.setOnClickListener(this);
//        lnr_notification.setOnClickListener(this);
        lnr_complete.setOnClickListener(this);
        // lnr_orders.setOnClickListener(this);
        lnr_support.setOnClickListener(this);
        lnr_terms.setOnClickListener(this);
        lnr_about.setOnClickListener(this);
        lnr_faqs.setOnClickListener(this);
//        lnr_refund.setOnClickListener(this);
        lnr_privacy.setOnClickListener(this);
        lnr_feedback.setOnClickListener(this);
        lnr_logout.setOnClickListener(this);
        lnr_sounds.setOnClickListener(this);
        lnr_dsw.setOnClickListener(this);
//        lnr_sd_card.setOnClickListener(this);
//        lnr_rateapp.setOnClickListener(this);
        lnr_rewards.setOnClickListener(this);
        lnr_knowledge.setOnClickListener(this);
        lnr_delete_account.setOnClickListener(this);
        lnr_student_dashboard.setOnClickListener(this);
        lnr_student_add.setOnClickListener(this);


        String[] userInfo = SessionManager.getUserInfo(_this);

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        session_device_id = userInfo[3];
        MyDatabase db = MyDatabase.getDatabase(_this);
        profile = db.userDAO().getUserProfile(userid);
        CommonUtils.userPhone = profile.mobile_number;


        //username.setTypeface(font);
        String name = profile.full_name;
        if (name == null || name.trim().length() <= 0)
            name = "" + "Guest";
        else name = "" + name;
        username.setText(name);


        checkActivationCode();

        setProfileLogo();
        getVersion();
        UserProfile userProfile = db.userDAO().getUserProfile(userid);
        try {
            if (userProfile != null) {
                //Log.d("userProfile.user_type",userProfile.user_type);
                if (userProfile.user_type != null && userProfile.user_type.equals("T")) {
                    lnr_student_add.setVisibility(View.VISIBLE);
                    lnr_student_dashboard.setVisibility(View.VISIBLE);
                } else if (userProfile.user_type != null && userProfile.user_type.equals("E")) {
                    lnr_student_add.setVisibility(View.VISIBLE);

                }


            }
        } catch (Exception e) {

        }


    }

    private void setProfileLogo() {

        if (profile == null) {
            return;
        }
        String path = profile.photo;

        if (path != null && !path.isEmpty()) {
            path = Constants.IMAGE_PATH + userid + "/" + path;

            Picasso.with(_this).load(path).into(logo);
        } else {
            Picasso.with(_this).load(R.drawable.profile).into(logo);
        }

    }

    private void checkActivationCode() {
        MyDatabase db = MyDatabase.getDatabase(_this);
        ActivationDetails aDetails = db.activationDAO().getActivationDetails(userid, classId);

//        if (aDetails != null && aDetails.activation_key != null && !aDetails.activation_key.isEmpty()) {
//
////            txt_act_details.setText(getString(R.string.subscribed));
//
//            if(AppConfig.checkSDCardEnabled(_this,userid,classId))
//            {
//                lnr_sd_card.setVisibility(View.GONE);
//            }else
//            {
//
//                if (new SharedPref().isExpired(getActivity())) {
//                    lnr_sd_card.setVisibility(View.GONE);
//                }else
//                {
//                    lnr_sd_card.setVisibility(View.VISIBLE);
//                }
//
//            }
//        } else {
//
//            lnr_sd_card.setVisibility(View.GONE);
////            txt_act_details.setText(getString(R.string.subscribe_now));
//
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    Dialog dialog;
    Intent web_intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnr_switch:
                AppController.getInstance().playAudio(R.raw.button_click);
                // if (AppStatus.getInstance(_this).isOnline()) {
                Intent ii = new Intent(_this, SwitchClassActivity.class);
                ii.putExtra("islogin", true);
                ii.putExtra("isFromOTP", false);
                startActivity(ii);
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
               /* } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));

                }*/
                break;
//            case R.id.lnr_notification:
//                AppController.getInstance().playAudio(R.raw.button_click);
//                startActivity(new Intent(getContext(), AllNotificationActivity.class));
//                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                break;
//            case R.id.lnr_buy:
//                AppController.getInstance().playAudio(R.raw.button_click);
//                subscribeClass(true);
//                break;
            case R.id.lnr_knowledge:
                startActivity(new Intent(getActivity(), ConnectusActivity.class));
                /*  if (AppStatus.getInstance(_this).isOnline()) {
                 *//*web_intent= new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", Constants.OFFICIAL_WEBSITE);
                    web_intent.putExtra("name", getResources().getString(R.string.welcome_to_tutorix));
                    startActivity(web_intent);*//*

                    Intent in=new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse(Constants.OFFICIAL_WEBSITE));
                    startActivity(in);
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }*/

                break;
//            case R.id.lnr_view:
//                AppController.getInstance().playAudio(R.raw.button_click);
//                subscribeClass(false);
//                break;

            case R.id.lnr_rewards:
                if (!AppStatus.getInstance(_this).isOnline()) {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet));
                    return;
                }
                startActivity(new Intent(getActivity(), RewardsActivity.class));
                break;
//            case R.id.lnr_sd_card:
//                AppController.getInstance().playAudio(R.raw.button_click);
//                  if (loginType.equals("")) {
//                    Intent i = new Intent(_this, SubscribeActivity.class);
//                    i.putExtra("flag", "M");
//                    _this.startActivityForResult(i, 200);
//                } else {
//
//
//                      if(!AppConfig.checkSdcard(classId))
//                      {
//
//                          CommonUtils.showToast(getActivity(),"Please insert Current Class SDcard");
//                          return;
//                      }
//                    Intent i = new Intent(_this, SDCardActivation.class);
//                    _this.startActivityForResult(i, 200);
//                }
//                break;
            case R.id.lnr_doubt:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (!AppStatus.getInstance(_this).isOnline()) {
                    CommonUtils.showToast(_this, getString(R.string.no_internet));
                    return;
                }
               /* MyDatabase db = MyDatabase.getDatabase(_this);
                ActivationDetails aDetails = db.activationDAO().getActivationDetails(userid, classId);
                if(aDetails==null||aDetails.activation_key.isEmpty())
                {

                    dialog=new Dialog(_this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.dialog_alert);
                    TextView txt_msg=dialog.findViewById(R.id.txt_msg);
                    txt_msg.setText(getResources().getString(R.string.service_availabile));
                    TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
                    TextView txt_ok=dialog.findViewById(R.id.txt_ok);
                    txt_ok.setText("Ok");
                    txt_cancel.setVisibility(View.GONE);
                    txt_cancel.setText("Cancel");
                    txt_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppController.getInstance().playAudio(R.raw.button_click);
                            dialog.dismiss();
                        }
                    });
                    txt_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppController.getInstance().playAudio(R.raw.button_click);
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                    return;
                }*/
                Intent i = new Intent(_this, DoubtsActivity.class);
                i.putExtra(Constants.subjectId, "0");
                startActivity(i);
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.lnr_syllabus:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (AppConfig.checkSDCardEnabled(_this, userid, classId) && AppConfig.checkSdcard(classId,getContext())) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", "file:///" + AppConfig.getFAQSSDCardPath(getContext()));
                    web_intent.putExtra("name", getResources().getString(R.string.syllabus));
                    startActivity(web_intent);
                    return;
                }
                if (AppStatus.getInstance(_this).isOnline()) {

                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", Constants.SYLLABUS);
                    web_intent.putExtra("name", getResources().getString(R.string.syllabus));
                    startActivity(web_intent);
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.lnr_complete:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (!AppConfig.checkSDCardEnabled(_this, userid, classId) && !AppStatus.getInstance(_this).isOnline()) {
                    CommonUtils.showToast(_this, getString(R.string.no_internet));
                    return;
                }

                startActivity(new Intent(_this, CompletedTaskActivity.class));
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            /*case R.id.lnr_orders:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (AppStatus.getInstance(_this).isOnline()) {
                    startActivity(new Intent(_this, MyOrdersActivity.class));
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }

                break;*/
            case R.id.lnr_support:
                AppController.getInstance().playAudio(R.raw.button_click);
                startActivity(new Intent(_this, SupportScreen.class));

                break;
            case R.id.lnr_terms:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (AppConfig.checkSDCardEnabled(_this, userid, classId) && AppConfig.checkSdcard(classId,getContext())) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", "file:///" + AppConfig.getFAQSSDCardPath(getContext()) + "terms_of_use_m.htm");
                    web_intent.putExtra("name", getResources().getString(R.string.terms_conditions));
                    startActivity(web_intent);
                    return;
                }

                if (AppStatus.getInstance(_this).isOnline()) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", Constants.TERMS_POLICY);
                    web_intent.putExtra("name", getResources().getString(R.string.terms_conditions));
                    startActivity(web_intent);
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }

                break;
            case R.id.lnr_about:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (AppConfig.checkSDCardEnabled(_this, userid, classId) && AppConfig.checkSdcard(classId,getContext())) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", "file:///" + AppConfig.getFAQSSDCardPath(getContext()) + "about_us_m.htm");
                    web_intent.putExtra("name", getResources().getString(R.string.aboutcompany));
                    startActivity(web_intent);
                    return;
                }
                if (AppStatus.getInstance(_this).isOnline()) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", Constants.ABOUT);
                    web_intent.putExtra("name", getResources().getString(R.string.aboutcompany));
                    startActivity(web_intent);
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }

                break;
            case R.id.lnr_faqs:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (AppConfig.checkSDCardEnabled(_this, userid, classId) && AppConfig.checkSdcard(classId,getContext())) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", "file:///" + AppConfig.getFAQSSDCardPath(getContext()) + "faqs_m.htm");
                    web_intent.putExtra("name", getResources().getString(R.string.faq));
                    startActivity(web_intent);
                    return;
                }
                if (AppStatus.getInstance(_this).isOnline()) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", Constants.FAQ);
                    web_intent.putExtra("name", getResources().getString(R.string.faq));
                    startActivity(web_intent);
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }

                break;
//                case R.id.lnr_refund:
//                    AppController.getInstance().playAudio(R.raw.button_click);
//                    if(AppConfig.checkSDCardEnabled(_this,userid,classId)&&AppConfig.checkSdcard(classId))
//                    {
//                        web_intent = new Intent(_this, WebViewTestActivity.class);
//                        web_intent.putExtra("data_path", "file:///"+AppConfig.getFAQSSDCardPath()+"refund_policy_m.htm");
//                        web_intent.putExtra("name",  getResources().getString(R.string.refund));
//                        startActivity(web_intent);
//                        return;
//                    }
//                    if (AppStatus.getInstance(_this).isOnline()) {
//                        web_intent = new Intent(_this, WebViewTestActivity.class);
//                        web_intent.putExtra("data_path", Constants.REFUND_POLICY);
//                        web_intent.putExtra("name", getResources().getString(R.string.refund));
//                        startActivity(web_intent);
//                    } else {
//                        CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
//                        //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
//                    }
//
//                break;
            case R.id.lnr_privacy:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (AppConfig.checkSDCardEnabled(_this, userid, classId) && AppConfig.checkSdcard(classId,getContext())) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", "file:///" + AppConfig.getFAQSSDCardPath(getContext()) + "privacy_policy_m.htm");
                    web_intent.putExtra("name", getResources().getString(R.string.privacy));
                    startActivity(web_intent);
                    return;
                }
                if (AppStatus.getInstance(_this).isOnline()) {
                    web_intent = new Intent(_this, WebViewTestActivity.class);
                    web_intent.putExtra("data_path", Constants.PRIVACY_POLICY);
                    web_intent.putExtra("name", getResources().getString(R.string.privacy));
                    startActivity(web_intent);
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }

                break;
            case R.id.lnr_logout:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (!AppStatus.getInstance(_this).isOnline()) {

                    if (loginType.equals("")) {
                        CommonUtils.showToast(_this, getString(R.string.no_internet));
                        return;
                    }
                    if (AppConfig.checkSDCardEnabled(_this, userid, classId) && AppConfig.checkSdcard(classId,getContext())) {
                        SessionManager.logoutUser(_this);
                        Intent intent = new Intent(_this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        return;
                    } else {
                        CommonUtils.showToast(_this, getString(R.string.no_internet));
                        return;
                    }
                }
                if (AppConfig.checkSDCardEnabled(_this, userid, classId)) {
                    SessionManager.logoutUser(_this);
                    Intent intent = new Intent(_this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                    _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    return;
                }

                if (SessionManager.isLoggedIn(_this)) {
                    String android_id = Settings.Secure.getString(_this.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    logout(userid, android_id);

                } else {
                    String android_id = Settings.Secure.getString(_this.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    logout(userid, android_id);
                }
                break;
            case R.id.lnr_feedback:
                if (AppStatus.getInstance(_this).isOnline()) {
                    startActivity(new Intent(getActivity(), FeedbackActivity.class));
                } else {
                    CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
                }
                AppController.getInstance().playAudio(R.raw.button_click);
                break;

            case R.id.logo:
            case R.id.lnr_profile:
            case R.id.classname:
                startActivityForResult(new Intent(_this, ProfileActivity.class), PROFILE_INTENT_REQUEST);
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.qz_next);
                break;
            case R.id.lnr_sounds:
                Intent intent = new Intent(_this, SettingsActivity.class);
                startActivity(intent);
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.button_click);
                break;
            case R.id.lnr_dsw:
                Intent intentdsw = new Intent(_this, DSWActivity.class);
                startActivity(intentdsw);
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.button_click);
                break;
//            case R.id.lnr_rateapp:
//                giveFeedback();
//                break;
            case R.id.lnr_student_add:
                startActivity(new Intent(_this, AddToBatchActivity.class));
                break;
            case R.id.lnr_student_dashboard:
                startActivity(new Intent(_this, StudentsDashBoardActivity.class));
                break;
            case R.id.lnr_delete_account:
                startActivity(new Intent(_this, DeleteOTPActivity.class));


                // deleteOTP(userid,session_device_id);
                break;

        }
    }

    private void subscribeClass(boolean newBuy) {


        if (newBuy) {
            if (!AppStatus.getInstance(getActivity()).isOnline()) {
                CommonUtils.showToast(getActivity(), getString(R.string.no_internet));
                return;
            }
            Intent i = new Intent(_this, SubscribeOnlineActivity.class);
            i.putExtra("flag", "M");
            _this.startActivityForResult(i, 200);
        } else if (loginType.equals("")) {
            Intent i = new Intent(_this, SubscribeActivity.class);
            i.putExtra("flag", "M");
            _this.startActivityForResult(i, 200);
        } else {
            ActivationDetailsActivity.menuFragment = this;
            Intent i = new Intent(_this, ActivationDetailsActivity.class);
            _this.startActivityForResult(i, 200);
        }
    }

    private void logout(String userId, String android_id) {
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.deviceId, android_id);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(_this, false);
        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                Constants.USER_LOGOUT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        if (_this == null || getActivity() == null) {
                            return;
                        }
                        CustomDialog.closeDialog();
                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                //checkUnCookieThenPlay();
                                if (!error) {
                                    SessionManager.logoutUser(_this);
                                    String errorMsg = jObj.getString(Constants.message);
                                    // Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                    MyDatabase dbHandler = MyDatabase.getDatabase(getActivity());
                                    dbHandler.userDAO().deleteUser(userId);
                                    CommonUtils.showToast(_this, errorMsg);
                                    SessionManager session = new SessionManager();
                                    session.logoutUser(getActivity());
                                    Intent i = new Intent(_this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    _this.finish();
                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(_this, errorMsg);
                                    if (errorMsg.contains("Invalid")) {
                                        SessionManager.logoutUser(_this);
                                        MyDatabase dbHandler = MyDatabase.getDatabase(getActivity());
                                        dbHandler.userDAO().deleteUser(userId);
                                        Intent i = new Intent(_this, LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        _this.finish();
                                    }
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
                CommonUtils.showToast(_this, error.getMessage());
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

    int PROFILE_INTENT_REQUEST = 10001;

    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Log.v("Requestcode","Requestcode fragment "+requestCode);
        if (getActivity() == null) {
            return;
        }
        if (requestCode == PROFILE_INTENT_REQUEST) {
            MyDatabase db = MyDatabase.getDatabase(getActivity());

            profile = db.userDAO().getUserProfile(userid);
            setProfileLogo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            txt_appversion.setText("Version : " + version);
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void giveFeedback() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }
}
