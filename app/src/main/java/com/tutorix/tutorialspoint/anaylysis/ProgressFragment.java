package com.tutorix.tutorialspoint.anaylysis;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {
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
    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;

    public ProgressFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
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
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] userInfo = SessionManager.getUserInfo(getActivity());

        access_token = userInfo[1];
        userid = userInfo[0];
        loginType = userInfo[2];
        class_id = userInfo[4];
        try {
            int currrentClsId = Integer.parseInt(class_id);
            if (currrentClsId <= 7) {

            } else if (currrentClsId == 8) {
                radioBio.setVisibility(View.GONE);

            } else if (currrentClsId == 9) {
                radioMath.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadFragment(0);
        radioSubGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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

    private void loadFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TimeVsSubFragment();
                break;
            case 1:
                fragment = LineGraphFragment.newInstance("1");
                break;
            case 2:
                fragment = LineGraphFragment.newInstance("2");
                break;
            case 3:
                fragment = LineGraphFragment.newInstance("4");
                break;
            case 4:
                fragment = LineGraphFragment.newInstance("3");
                break;

        }
        if (fragment == null)
            return;

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
        if (position != 0)
            AppController.getInstance().playAudio(R.raw.subject_sounds);
    }
}
