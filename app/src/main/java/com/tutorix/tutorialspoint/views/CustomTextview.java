package com.tutorix.tutorialspoint.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextview extends TextView {

    public CustomTextview(Context context) {
        super(context);
        init(context);
    }

    public CustomTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context ctx)
    {
        //this.setTypeface(Typeface.createFromAsset(ctx.getAssets(),"open_sans_regular.ttf"));
    }
}
