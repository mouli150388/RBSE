package com.tutorix.tutorialspoint.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.fragments.WelcomepageFragment;

public class WelcomepageFragmentAdapter extends FragmentPagerAdapter {
    int welcomePages[];
    public WelcomepageFragmentAdapter(FragmentManager fm) {
        super(fm);
        welcomePages = new int[]{R.layout.welcome_slider_page, R.layout.welcome_slide_page_two, R.layout.welcome_slide_page_three};
    }

    @Override
    public Fragment getItem(int position) {
        WelcomepageFragment fragment=new WelcomepageFragment();
        Bundle b=new Bundle();

     /*   switch (position)
        {
            case 0:
                b.putInt("position",0);

                break;
            case 1:
                b.putInt("position",1);
                b.putInt("layout", R.layout.welcome_slide_page_two);
                break;
            case 2:
                b.putInt("position",2);
                b.putInt("layout", R.layout.welcome_slide_page_three);
                break;
            case 4:

                b.putInt("layout", R.layout.welcome_slide_page_four);
                break;

        }*/
        b.putInt("position",position);
        b.putInt("layout", welcomePages[position]);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
