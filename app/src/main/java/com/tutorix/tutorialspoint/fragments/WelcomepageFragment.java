package com.tutorix.tutorialspoint.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tutorix.tutorialspoint.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomepageFragment extends Fragment {

int position;
int layout;
    ImageView img_two;
    public WelcomepageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b=getArguments();
        position= b.getInt("position");
        layout=  b.getInt("layout");

        View view=inflater.inflate(layout, container, false);
        img_two=view.findViewById(R.id.img_two);
       // img_two.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
       /* if(isVisibleToUser&&position==0&&img_two!=null)
        {
            img_two.setVisibility(View.INVISIBLE);
            Animation anim= AnimationUtils.loadAnimation(getActivity(),R.anim.right_in_welcome);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    img_two.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            img_two.startAnimation(anim);
            //ObjectAnimator animator=ObjectAnimator.ofFloat(img_two,"translationX",100);

        }*/
    }

    public void showAnimation()
    {


    }

}
