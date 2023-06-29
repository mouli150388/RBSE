package com.tutorix.tutorialspoint.cropper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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

import com.tutorix.tutorialspoint.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropImageFragemtn extends DialogFragment {


    @BindView(R.id.ImageView_image)
    ImageView ImageView_image;
    @BindView(R.id.img_continue)
    LinearLayout img_continue;
    @BindView(R.id.img_reset)
    LinearLayout img_reset;
    Activity activity;

    public CropImageFragemtn() {
        // Required empty public constructor
    }

    public static CropImageFragemtn newInstance() {
        return new CropImageFragemtn();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crop_image_layout, container, false);
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

                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    Bitmap bitmapOrigin = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        try {
            CropImage.ActivityResult result =
                    new CropImage.ActivityResult(
                            ((CropImageActivity) getActivity()).mCropImageView.getImageUri(),
                            ((CropImageActivity) getActivity()).finalUri,
                            ((CropImageActivity) getActivity()).error,
                            ((CropImageActivity) getActivity()).mCropImageView.getCropPoints(),
                            ((CropImageActivity) getActivity()).mCropImageView.getCropRect(),
                            ((CropImageActivity) getActivity()).mCropImageView.getRotatedDegrees(),
                            ((CropImageActivity) getActivity()).mCropImageView.getWholeImageRect(),
                            ((CropImageActivity) getActivity()).sampleSize);

            ImageView_image.setImageURI(result.getUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.v("OnDialog","OnDialog Dismissed");
        //bitmapOriginRota=null;
        //bitmapResize=null;
        //bitmapOrigin=null;
    }

    @OnClick({R.id.img_continue, R.id.img_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_continue:

               // dismiss();
                ((CropImageActivity)getActivity()).setResultFromCrop();
                break;
            case R.id.img_reset:
                dismiss();
                break;
        }
    }
}
