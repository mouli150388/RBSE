package com.tutorix.tutorialspoint.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Employee on 11/3/2017.
 */

public class WebfontText extends TextView {

    public WebfontText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context);

    }

    public WebfontText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public WebfontText(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context ctx) {
        // TODO Auto-generated method stub
        Typeface tf= Typeface.createFromAsset(ctx.getAssets(),"fontawesome_webfont.ttf");
        this.setTypeface(tf);
    }
}
