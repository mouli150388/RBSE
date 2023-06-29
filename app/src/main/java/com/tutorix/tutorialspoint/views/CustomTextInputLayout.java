package com.tutorix.tutorialspoint.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;

public class CustomTextInputLayout extends TextInputLayout {

    public CustomTextInputLayout(Context context) {
        super(context);
        initFont(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont(context);
    }

    private void initFont(Context context) {
        Typeface tf=Typeface.createFromAsset(context.getAssets(),"opensans_regular.ttf");

        EditText editText = getEditText();
        if (editText != null) {
            editText.setTypeface(tf);
        }
        try {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            ((TextPaint) tpf.get(cth)).setTypeface(tf);
        } catch (Exception ignored) {
            // Nothing to do
        }
    }
}