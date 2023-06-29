package com.tutorix.tutorialspoint.utility;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tutorix.tutorialspoint.R;


/**
 * Created by Employee on 4/11/2017.
 */

public class CustomDialog extends Dialog implements DialogInterface.OnClickListener{

    Dialog dialg;

    AlertDialog.Builder builder;

    static LayoutInflater lyt_Inflater;

    OnDialogClick onDialogClick;

    static Dialog mpd;
    public CustomDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context ctx)
    {
        builder=new AlertDialog.Builder(ctx);

    }
    public void setListener(OnDialogClick onDialogClick)
    {
        this.onDialogClick=onDialogClick;
    }
    public void showMessage(String title,boolean isSingleButton,String message)
    {
        builder.setTitle(title);
        builder.setMessage(message);
        if(isSingleButton) {
            builder.setNeutralButton(getContext().getString(R.string.ok), this);
        }else
        {
            builder.setPositiveButton(getContext().getString(R.string.yes), this);
            builder.setNegativeButton(getContext().getString(R.string.no), this);
        }
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i)
        {
            case BUTTON_POSITIVE:
                onDialogClick.onPassitive();
                break;
            case BUTTON_NEGATIVE:
                onDialogClick.onNegative();
                break;
            case BUTTON_NEUTRAL:
                onDialogClick.onNeutral();
                break;
        }
        dialogInterface.dismiss();
    }


    static ObjectAnimator animator;
    public static void showDialog(Context context, boolean cancelable)
    {
        if(context==null)
            return;
        lyt_Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view_lyt = lyt_Inflater.inflate(     R.layout.dialog_loading, null);
        ImageView imag=(ImageView) view_lyt.findViewById(R.id.imag);

        mpd = new Dialog(context,R.style.ThemeDialogCustom);
        mpd.setContentView(view_lyt);
        mpd.setCancelable(cancelable);
        mpd.getWindow().setLayout(200,200);
        mpd.setCanceledOnTouchOutside(cancelable);
        if(context==null)
            return;
        mpd.show();
    }
    public static void showCustomView(Context context,View view,boolean cancelable,int width)
    {
        mpd = new Dialog(context, R.style.ThemeDialogCustom);
        mpd.setContentView(view);
        mpd.setCancelable(cancelable);
        if(width>0)
            mpd.getWindow().setLayout(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mpd.show();
    }

    public static void closeDialog()
    {
       /* if(animator!=null)
            animator.end();*/

        if(mpd!=null&&mpd.isShowing()) {
            try {
                if (mpd.getContext() == null) {
                    return;
                }
                mpd.dismiss();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    public void unregisterListenrs()
    {
        onDialogClick=null;
    }

    public interface OnDialogClick{
        public void onPassitive();
        public void onNegative();
        public void onNeutral();
    }
}