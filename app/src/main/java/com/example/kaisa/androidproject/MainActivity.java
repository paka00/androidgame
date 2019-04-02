package com.example.kaisa.androidproject;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import com.example.kaisa.androidproject.model.DbModel;

public class MainActivity extends AppCompatActivity {

    public static ImageButton imageButton = null;
    ViewPager viewPager = null;
    public static BottomNavigationView navigation;
    DbModel model = new DbModel(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.btn_Menu);
        viewPager = findViewById(R.id.pager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        navigation.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        getSupportActionBar().hide();
        Intent stepCounterIntent = new Intent(this, StepCounterService.class);
        startService(stepCounterIntent);



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
                            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(intent);
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

        boolean database = model.checkIfTableEmpty();
        if(database){
            Log.d("Moro", "on");

        } else {
            Log.d("Moro", "EI");
            viewPager.setCurrentItem(1);
            navigation.setVisibility(View.INVISIBLE);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_something:
                    viewPager.setCurrentItem(3);
                    break;
            }
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
    public void setFragmentToHome()
    {
        if(viewPager.getCurrentItem()==0)
        {
            super.onBackPressed();
        }
        else
        {
            viewPager.setCurrentItem(0);

        }

    }

}



