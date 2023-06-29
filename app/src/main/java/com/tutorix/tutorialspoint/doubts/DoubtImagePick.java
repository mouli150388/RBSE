package com.tutorix.tutorialspoint.doubts;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tutorix.tutorialspoint.R;

import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoubtImagePick extends DialogFragment {


    @BindView(R.id.imgone)
    ImageView imgone;
    @BindView(R.id.imgTwo)
    ImageView imgTwo;
    @BindView(R.id.imgThree)
    ImageView imgThree;
    Activity activity;
    public DoubtImagePick() {
        // Required empty public constructor
    }

    public static DoubtImagePick newInstance() {
      return  new DoubtImagePick();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_image, container, false);
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
                if(activity instanceof AskDoubtMainActivity)
                    ((AskDoubtMainActivity) activity).disMissDialog();
                else   if(activity instanceof DoubtsViewActivity)
                    ((DoubtsViewActivity)activity).disMissDialog();
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
    Bitmap bitmapOriginRota;
    Bitmap bitmapResize;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         activity=getActivity();

        try {
            if(activity instanceof AskDoubtMainActivity)
            bitmapOrigin = getFitSampleBitmap(((AskDoubtMainActivity)activity).iStream);
            else if(activity instanceof DoubtsViewActivity)
            bitmapOrigin = getFitSampleBitmap(((DoubtsViewActivity)activity).iStream);


        imgThree.setImageBitmap(bitmapOrigin);
        //Log.v("Image Sizes","Image fileSize3 "+bitmapOrigin.getWidth()+" "+bitmapOrigin.getHeight()+" "+(bitmapOrigin.getWidth()<bitmapOrigin.getHeight()));
        if(bitmapOrigin.getWidth()<bitmapOrigin.getHeight()) {
            imgone.setVisibility(View.VISIBLE);
            imgTwo.setVisibility(View.VISIBLE);
            bitmapOriginRota = RotateBitmap(bitmapOrigin, 90);
            bitmapResize = RotateBitmap(bitmapOrigin, 270);
            imgone.setImageBitmap(bitmapOriginRota );
            imgTwo.setImageBitmap(bitmapResize);
        }
        else
        {
            imgone.setImageBitmap(null);
            imgTwo.setImageBitmap(null);
            imgone.setVisibility(View.GONE);
            imgTwo.setVisibility(View.GONE);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    /*public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }*/
    public    Bitmap  getFitSampleBitmap(InputStream inputStream) throws Exception{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] bytes=null;
        if(activity instanceof AskDoubtMainActivity)
        bytes    = ((AskDoubtMainActivity)activity).getBytes(inputStream);
        else if(activity instanceof DoubtsViewActivity)
       bytes = ((DoubtsViewActivity)activity).getBytes(inputStream);
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {

        }
    }


    @OnClick({R.id.imgone, R.id.imgTwo, R.id.imgThree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgone:
                dismiss();
                bitmapOrigin=null;
                bitmapResize=null;
                //((AskDoubtMainActivity)getActivity()).   ivQuestionPic.setImageBitmap(bitmapOriginRota);
                //dismiss();
                if(activity instanceof AskDoubtMainActivity)
                ((AskDoubtMainActivity)activity).convert(bitmapOriginRota);
                else if(activity instanceof DoubtsViewActivity)
                ((DoubtsViewActivity)activity).convert(bitmapOriginRota);

                break;
            case R.id.imgTwo:
                dismiss();
                bitmapOrigin=null;
                bitmapOriginRota=null;
               // ((AskDoubtMainActivity)getActivity()).   ivQuestionPic.setImageBitmap(bitmapResize);
                //dismiss();
                if(activity instanceof AskDoubtMainActivity)
                ((AskDoubtMainActivity)activity).convert(bitmapResize);
                else if(activity instanceof DoubtsViewActivity)
                ((DoubtsViewActivity)activity).convert(bitmapResize);

                break;
            case R.id.imgThree:
              //  ((AskDoubtMainActivity)getActivity()).   ivQuestionPic.setImageBitmap(bitmapOrigin);
                dismiss();
                bitmapResize=null;
                bitmapOriginRota=null;
                if(activity instanceof AskDoubtMainActivity)
                ((AskDoubtMainActivity)activity).convert(bitmapOrigin);
                else if(activity instanceof DoubtsViewActivity)
                ((DoubtsViewActivity)activity).convert(bitmapOrigin);

                break;
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

}
