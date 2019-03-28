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

public class MainActivity extends AppCompatActivity  {

    ImageButton imageButton = null;
    ViewPager viewPager = null;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent stepCounterIntent = new Intent(this, StepCounterService.class);
        startService(stepCounterIntent);
        registerReceiver(broadcastReceiver, new IntentFilter(StepCounterService.BROADCAST_ACTION));
        //setDailyResetAlarm();
        Log.v("stepsmain", "oncreate");
        imageButton = findViewById(R.id.btn_Menu);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().hide();
        /*
        viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(adapter);
        */
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getApplicationContext(), imageButton);
                menu.getMenuInflater().inflate(R.menu.dropdown, menu.getMenu());
                menu.show();
            }
        });
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
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView textViewTotal = findViewById(R.id.total_steps_text_view);
            TextView textViewDaily = findViewById(R.id.daily_steps_text_view);
            String steps = intent.getStringExtra("steps_string");
            String dSteps = intent.getStringExtra("dsteps_string");
            textViewTotal.setText("Total steps: " + steps);
            textViewDaily.setText("Daily steps: " + dSteps);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new ModifyFigureFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new AchievementsFragment();
                    break;
                case R.id.navigation_something:
                    fragment = new JoggingFragment();
                    break;
            }
            return selectFragment(fragment);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.menu_settings){
            fragment = new SettingsFragment();
            TextView textView = findViewById(R.id.fragment_settings_text);
            textView.setText("Test");
        }
        return selectFragment(fragment);
    }

    public boolean selectFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        return true;
    }
}

