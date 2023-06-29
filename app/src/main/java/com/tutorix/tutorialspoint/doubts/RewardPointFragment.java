package com.tutorix.tutorialspoint.doubts;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.views.CustomTextview;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RewardPointFragment extends DialogFragment {


    SimilarDoubtActivity activity;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.appbar)
    RelativeLayout appbar;
    @BindView(R.id.txt_silvercoins)
    CustomTextview txtSilvercoins;
    @BindView(R.id.txt_current_points)
    CustomTextview txtCurrentPoints;
    @BindView(R.id.txt_previous)
    TextView txtPrevious;
    @BindView(R.id.txt_next)
    TextView txtNext;
    @BindView(R.id.lnr_bottom)
    LinearLayout lnrBottom;

    int doubts_balance=0;

    @BindString(R.string.currently_you_have)
    String currently_you_have;

    public RewardPointFragment() {
        // Required empty public constructor
    }

    public static RewardPointFragment newInstance() {
        return new RewardPointFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dount_rewardpoints, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {

                //dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    Bitmap bitmapOrigin = null;
    Bitmap bitmapOriginRota;
    Bitmap bitmapResize;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (SimilarDoubtActivity) getActivity();

        Bundle b=getArguments();
        if(b!=null)
        {
            try{
                JSONObject coinObj=new JSONObject(b.getString("rewardpoint"));
                int silver_coins=coinObj.getInt("silver_coins");
                int gold_coins=coinObj.getInt("gold_coins");
                int doubts_total=coinObj.getInt("doubts_total");
                int doubts_used=coinObj.getInt("doubts_used");
                doubts_balance=coinObj.getInt("doubts_balance");


                txtCurrentPoints.setText(String.format(currently_you_have,doubts_balance));
            }catch (JSONException jex)
            {
                jex.printStackTrace();
            }

        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {

        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @OnClick({R.id.img_back, R.id.txt_previous, R.id.txt_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                dismiss();
                break;
            case R.id.txt_previous:
                dismiss();
                break;
            case R.id.txt_next:
                dismiss();
                if(doubts_balance>0)
                {
                    activity.fromRewardPoints(true);
                }else
                {
                    activity.fromRewardPoints(false);
                }
                break;
        }
    }
}
