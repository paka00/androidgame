package com.example.kaisa.androidproject;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static ImageButton imageButton = null;
    ViewPager viewPager = null;
    Fragment fragment = null;
    HomeFragment homeFragment = null;
    AchievementsFragment achievementsFragment = null;
    ModifyFigureFragment modifyFigureFragment = null;
    JoggingFragment joggingFragment = null;
    SettingsFragment settingsFragment = null;
    public static BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = new HomeFragment();
        achievementsFragment = new AchievementsFragment();
        modifyFigureFragment = new ModifyFigureFragment();
        joggingFragment = new JoggingFragment();
        settingsFragment = new SettingsFragment();
        imageButton = findViewById(R.id.btn_Menu);
        viewPager = findViewById(R.id.pager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().hide();
        Intent stepCounterIntent = new Intent(this, StepCounterService.class);
        startService(stepCounterIntent);
        //selectFragment(homeFragment);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getApplicationContext(), imageButton);
                menu.getMenuInflater().inflate(R.menu.dropdown, menu.getMenu());
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_settings) {
                            return selectFragment(settingsFragment);
                        }
                        return false;
                    }
                });
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                navigation.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });
        setupViewpager(viewPager);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // fragment = homeFragment;
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_dashboard:
                    //fragment = modifyFigureFragment;
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_notifications:
                    //fragment = achievementsFragment;
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_something:
                    //fragment = joggingFragment;
                    viewPager.setCurrentItem(3);
                    break;
            }
            // return selectFragment(fragment);
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dropdown, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent stepCounterIntent = new Intent(this, StepCounterService.class);
        stepCounterIntent.putExtra("reset", false);
        startService(stepCounterIntent);
        Log.v("stepsmain", "onresume");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public boolean selectFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        return true;
    }

    public void setupViewpager(ViewPager viewPager) {
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(adapter);
    }

}



