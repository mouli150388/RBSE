package com.tutorix.tutorialspoint.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.AllNotesActivity;
import com.tutorix.tutorialspoint.activities.BookMarkActivity;
import com.tutorix.tutorialspoint.activities.CompletedTaskActivity;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.activities.TrackMainActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToolsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolsFragment extends Fragment {


    @BindView(R.id.img_complete)
    ImageView imgComplete;
    @BindView(R.id.img_bookmark)
    ImageView imgBookmark;
    @BindView(R.id.img_doubt)
    ImageView imgDoubt;
    @BindView(R.id.img_feedback)
    ImageView imgFeedback;
    @BindView(R.id.lnr_doubts)
    LinearLayout lnr_doubts;
    Activity _this;
    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;
    public ToolsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ToolsFragment newInstance() {
        ToolsFragment fragment = new ToolsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        ButterKnife.bind(this, view);
        _this=getActivity();
        return view;

    }
    Dialog dialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[]userInfo= SessionManager.getUserInfo(getActivity());

        access_token = userInfo[1];
        userid = userInfo[0];;
        loginType = userInfo[2];
        class_id = userInfo[4];

    }
    public void onResume() {
        super.onResume();
        AppConfig.setLanguages(getContext());
    }
    @OnClick({R.id.img_complete,R.id.lnr_complete, R.id.img_bookmark,R.id.lnr_bookmark, R.id.img_doubt,R.id.lnr_doubts, R.id.img_feedback,R.id.lnr_notes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_complete:
            case R.id.lnr_complete:
                AppController.getInstance().playAudio(R.raw.button_click);
                AppController.getInstance().startButtonAnimation(view);
                if (new SharedPref().isExpired(getActivity())) {
                    Intent i = new Intent(getActivity(), SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    startActivity(i);
                    return;
                }

                if (!AppConfig.checkSDCardEnabled(_this,userid,class_id)&& !AppStatus.getInstance(_this).isOnline()) {
                    CommonUtils.showToast(_this, getString(R.string.no_internet));
                    return;
                }

                startActivity(new Intent(_this, CompletedTaskActivity.class));
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case R.id.img_bookmark:
            case R.id.lnr_bookmark:
                AppController.getInstance().playAudio(R.raw.button_click);
                AppController.getInstance().startButtonAnimation(view);
                if (new SharedPref().isExpired(getActivity())) {
                    Intent i = new Intent(getActivity(), SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    startActivity(i);
                    return;
                }
                if (!(AppConfig.checkSDCardEnabled(_this,userid,class_id)&&AppConfig.checkSdcard(class_id,getContext())) && !AppStatus.getInstance(_this).isOnline()) {
                    CommonUtils.showToast(_this, getString(R.string.no_internet));
                    return;
                }

                startActivity(new Intent(_this, BookMarkActivity.class));
                _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            case R.id.img_doubt:
            case R.id.lnr_doubts:
                AppController.getInstance().playAudio(R.raw.button_click);
                Intent t = new Intent(getActivity(), TrackMainActivity.class);
                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                t.putExtra(Constants.lectureId, "0");
                t.putExtra(Constants.subjectId, "0");
                t.putExtra(Constants.classId, class_id);
                t.putExtra(Constants.userId, userid);
                t.putExtra(Constants.sectionId, "0");
                t.putExtra(Constants.intentType, Constants.global);
                t.putExtra(Constants.lectureName, "");
                startActivity(t);
                //mContext = (Activity) vh.itemView.getContext();
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.img_feedback:
            case R.id.lnr_notes:
                AppController.getInstance().playAudio(R.raw.button_click);
                AppController.getInstance().startButtonAnimation(view);

                // if (AppStatus.getInstance(_this).isOnline()) {
                    startActivity(new Intent(getActivity(), AllNotesActivity.class));

               // } else {
                 //   CommonUtils.showToast(getActivity(), getString(R.string.no_internet_message));
                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
               // }
                break;
        }
    }
}
