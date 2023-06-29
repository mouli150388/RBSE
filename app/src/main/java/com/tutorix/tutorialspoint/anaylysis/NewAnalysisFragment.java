package com.tutorix.tutorialspoint.anaylysis;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAnalysisFragment extends Fragment {


    @BindView(R.id.main_container)
    FrameLayout main_container;

    @BindView(R.id.btn_progress)
    RadioButton btnProgress;
    @BindView(R.id.btn_performance)
    RadioButton btnPerformance;
    @BindView(R.id.radi_grp_select)
    RadioGroup radiGrpSelect;
    @BindView(R.id.rel_toolbar)
    RelativeLayout relToolbar;


    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;

    public NewAnalysisFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NewAnalysisFragment newInstance() {
        NewAnalysisFragment fragment = new NewAnalysisFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_analysis, container, false);
        ButterKnife.bind(this, view);
        return view;

    }
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[]userInfo= SessionManager.getUserInfo(getActivity());
        access_token = userInfo[1];
        userid = userInfo[0];;
        loginType = userInfo[2];
        class_id = userInfo[4];
        loadFragment(0);
        radiGrpSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.btn_performance:
                        loadFragment(1);
                        break;
                    case R.id.btn_progress:
                        loadFragment(0);
                        break;
                }
            }
        });


    }

    private void loadFragment(int position)
    {

        Fragment fragment=null;
        switch (position)
        {
            case 0:
                fragment=new ProgressFragment();
                break;
            case 1:
                fragment= new PerformanceFragment();
                break;


        }
        if(fragment==null)
            return;

        FragmentManager manager=getChildFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.main_container,fragment).commit();
        //AppController.getInstance().playAudio(R.raw.qz_next);
    }



}
