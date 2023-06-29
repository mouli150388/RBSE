package com.tutorix.tutorialspoint.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;

public class SeekBarWithValues extends RelativeLayout {

    private int mMax = 100;
    private TextView mMinText;
    private TextView mMaxText;
    private TextView mCurrentText;
    private SeekBar mSeek;

    public SeekBarWithValues(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(
                R.layout.customseek_layout, this);
        // the minimum value is always 0
        mMinText = (TextView) findViewById(R.id.minValue);
        mMinText.setText("0");
        mMaxText = (TextView) findViewById(R.id.maxValue);
        mCurrentText = (TextView) findViewById(R.id.curentValue);
        mSeek = (SeekBar) findViewById(R.id.seekBar);
        mSeek.setMax(100);
        mMaxText.setText(String.valueOf(mSeek.getMax()));
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCurrentText(progress,seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * This needs additional work to make the current progress text stay
     * right under the thumb drawable.
     *
     * @param newProgress
     *            the new progress for which to place the text
     */
    public void updateCurrentText(int newProgress,int max) {
        mMax=max;
        mSeek.setMax(max);
        mSeek.setProgress(newProgress);
        mCurrentText.setText(String.valueOf(newProgress));
        final int padding = mMinText.getWidth() + mSeek.getPaddingLeft();
        final int totalSeekWidth = mSeek.getWidth();
        final RelativeLayout.LayoutParams lp = (LayoutParams) mCurrentText
                .getLayoutParams();
        final int seekLocation = (mSeek.getProgress() * totalSeekWidth)
                / mMax - mCurrentText.getWidth() / 2;
        lp.leftMargin = seekLocation + padding;
        mCurrentText.setLayoutParams(lp);
    }

    public SeekBar getSeekBar() {
        return mSeek;
    }

    public void updateSeekMaxValue(int newValue) {
        mMax = newValue;
        mMaxText.setText(mMax);
        mSeek.setMax(mMax);
    }


}
