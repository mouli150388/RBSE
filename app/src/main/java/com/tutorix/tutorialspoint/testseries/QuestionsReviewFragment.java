package com.tutorix.tutorialspoint.testseries;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.testseries.data.TestQuestions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionsReviewFragment extends DialogFragment {


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
    @BindView(R.id.btn_submit_test)
    Button btn_submit_test;
    @BindView(R.id.btn_continue_test)    Button btn_continue_test;
    @BindView(R.id.recycler_question_no)
    RecyclerView recycler_question_no;

    TestQuestionsAdapter testQuestionsAdapter;
    TestQuizActivity activity;
    QuestionsReviewFragment questionsReviewFragment;
    static int SUBJECT_SELECTED;
    List<TestQuestions>listQuestion=null;
    public QuestionsReviewFragment() {
        // Required empty public constructor
    }

    public static QuestionsReviewFragment newInstance(int SUJECT_SELECT) {
        SUBJECT_SELECTED=SUJECT_SELECT;
      return  new QuestionsReviewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_review_questions, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(),getTheme()){
            @Override
            public void onBackPressed() {

                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         activity= (TestQuizActivity) getActivity();

        recycler_question_no.setLayoutManager(new GridLayoutManager(getContext(),6));
        TestQuestions testQuestions;
        Random ramdom=new Random();
        String[]userInfo= SessionManager.getUserInfo(activity);
        String access_token = userInfo[1];
        String userid = userInfo[0];;
        String loginType = userInfo[2];
        String class_id = userInfo[4];
        questionsReviewFragment=this;

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
        int spanCount = 6; // 3 columns
        int spacing = 30; // 50px
        boolean includeEdge = false;
        recycler_question_no.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        listQuestion=new ArrayList<>();
        testQuestionsAdapter=new TestQuestionsAdapter(questionsReviewFragment);
        recycler_question_no.setAdapter(testQuestionsAdapter);

        updateList();




        radioSubGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {

                    case R.id.radio_phy:

                        SUBJECT_SELECTED=1;

                        break;
                    case R.id.radio_che:

                        SUBJECT_SELECTED=2;

                        break;
                    case R.id.radio_math:

                        SUBJECT_SELECTED=4;

                        break;
                    case R.id.radio_bio:

                        SUBJECT_SELECTED=3;

                        break;
                }
                updateList();
            }
        });
    }

    private void updateList() {
        if(SUBJECT_SELECTED==1){
            radioPhy.setChecked(true);
            listQuestion=activity.listQuestions.subList(0,activity.PHYSICS_COUNT);
        }
        else if(SUBJECT_SELECTED==2){
            radioChe.setChecked(true);
            listQuestion= activity.listQuestions.subList(activity.PHYSICS_COUNT,activity.CHEMISTRY_COUNT+activity.PHYSICS_COUNT);
        }
        else if(SUBJECT_SELECTED==3){
            radioBio.setChecked(true);
            listQuestion= activity.listQuestions.subList(activity.CHEMISTRY_COUNT+activity.PHYSICS_COUNT,activity.listQuestions.size());
        }
        else if(SUBJECT_SELECTED==4)
        {
            radioMath.setChecked(true);
            listQuestion= activity.listQuestions.subList(activity.CHEMISTRY_COUNT+activity.PHYSICS_COUNT,activity.listQuestions.size());

        }
        testQuestionsAdapter.addData(listQuestion);
    }


    @OnClick({R.id.btn_submit_test, R.id.btn_continue_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_test:
                dismiss();
                if(activity instanceof TestQuizActivity)
                    ((TestQuizActivity)activity).exitTest(false);

                break;
            case R.id.btn_continue_test:
                dismiss();


                break;

        }
    }

    public void selectedQuestion(int pos)
    {
        if(SUBJECT_SELECTED==1)
        {

        }else if(SUBJECT_SELECTED==2)
        {
            pos=activity.PHYSICS_COUNT+pos;
        }else if(SUBJECT_SELECTED==3||SUBJECT_SELECTED==4)
        {
            pos=activity.PHYSICS_COUNT+activity.CHEMISTRY_COUNT+pos;
        }
       activity.setSelection(SUBJECT_SELECTED,pos);
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

}
