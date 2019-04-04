package com.example.kaisa.androidproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;
    HomeFragment homeFragment = new HomeFragment();
    ModifyFigureFragment modifyFigureFragment = new ModifyFigureFragment();
    AchievementsFragment achievementsFragment = new AchievementsFragment();
    JoggingFragment joggingFragment = new JoggingFragment();


    public PagerAdapter(FragmentManager fm, int NoT) {
        super(fm);
        this.numberOfTabs = NoT;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return homeFragment;
            case 1:
                return modifyFigureFragment;
            case 2:
                return achievementsFragment;
            case 3:
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

