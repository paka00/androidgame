package com.example.kaisa.androidproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int NoT) {
        super(fm);
        this.numberOfTabs = NoT;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                ModifyFigureFragment modifyFigureFragment = new ModifyFigureFragment();
                return modifyFigureFragment;
            case 2:
                AchievementsFragment achievementsFragment = new AchievementsFragment();
                return achievementsFragment;
            case 3:
                JoggingFragment joggingFragment = new JoggingFragment();
                return joggingFragment;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return numberOfTabs;
    }
}

