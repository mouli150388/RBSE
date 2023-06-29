package com.tutorix.tutorialspoint.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;

import androidx.viewpager.widget.PagerAdapter;


/**
 * Created by Employee on 7/3/2017.
 */

public class WelcomePageAdapter extends PagerAdapter {

    int[] layouts;

    Activity mContext;

    LayoutInflater layoutInflater;
    public WelcomePageAdapter(int[]layouts,Activity mContext)
    {
     this.layouts=layouts;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position], container, false);
        if(position==0)
        {
            final ImageView img_two=view.findViewById(R.id.img_two);
            Animation anim= AnimationUtils.loadAnimation(container.getContext(),R.anim.right_in_welcome);
            img_two.setVisibility(View.VISIBLE);
            img_two.startAnimation(anim);
            //ObjectAnimator animator=ObjectAnimator.ofFloat(img_two,"translationX",100);



        }
        TextView txt_msg=(TextView)view.findViewById(R.id.txt_msg);

        container.addView(view);
        return view;
    }



    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
