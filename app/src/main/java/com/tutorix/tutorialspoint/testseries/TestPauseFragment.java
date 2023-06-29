package com.tutorix.tutorialspoint.testseries;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tutorix.tutorialspoint.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestPauseFragment extends DialogFragment {


    @BindView(R.id.btn_submit_test)
    Button btn_submit_test;
    @BindView(R.id.btn_continue_test)    Button btn_continue_test;
    @BindView(R.id.img_pause)
    ImageView img_pause;


    Activity activity;
    public TestPauseFragment() {
        // Required empty public constructor
    }

    public static TestPauseFragment newInstance() {
      return  new TestPauseFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_pause, container, false);
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

                if(activity instanceof TestQuizActivity)
                    ((TestQuizActivity)activity).exitTest(false);
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

         activity=getActivity();
        Glide.with(activity).load(R.drawable.time_paused).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_pause);

    }




    @OnClick({R.id.btn_submit_test, R.id.btn_continue_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_test:
                if(activity instanceof TestQuizActivity)
                ((TestQuizActivity)activity).exitTest(false);
                dismiss();


                break;
            case R.id.btn_continue_test:
                if(activity instanceof TestQuizActivity)
                    ((TestQuizActivity)activity).onPlayQuiz();
                dismiss();


                break;

        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

}
