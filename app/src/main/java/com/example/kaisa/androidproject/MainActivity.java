package com.example.kaisa.androidproject;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.Monster;
import com.example.kaisa.androidproject.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    public ImageButton imageButton = null;
    public NonSwipeableViewPager viewPager = null;
    public BottomNavigationView navigation;
    public boolean databaseEmpty = false;
    private View iconView = null;
    DbModel model = new DbModel(MainActivity.this);
    int i = 0;
    boolean appOn = false;
    PagerAdapter adapter;
    Date stopTime;
    Date startTime;
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    String stopTimeSeconds;
    String startTimeSeconds;
    static final String CHANNEL_ID = "CREATURE_CHASE_CHANNEL_ID";
    public static boolean isVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.btn_Menu);
        viewPager = findViewById(R.id.pager);
        Log.v("start", "main activity startattu");
        adapter = new PagerAdapter(getSupportFragmentManager(), 4);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        navigation.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        navigation.setItemIconSize(130);
        model = new DbModel(this);
        createNotificationChannel();
        if(!isServiceRunning(StepCounterService.class)) {
            Intent stepCounterIntent = new Intent(this, StepCounterService.class);
            startService(stepCounterIntent);
        }
        if (!model.checkIfTableEmpty("user")) {
            Monster monster = model.readMonster();
        }


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
        checkIfUserExist();

        if (appOn == false) {

            startTime = Calendar.getInstance().getTime();
            long seconds = startTime.getTime() / 1000;
            startTimeSeconds = Long.toString(seconds);
            Monster monster = model.readMonster();
            if(!model.checkIfTableEmpty("monsterStats")) {
                stopTimeSeconds = monster.getTurnOffDate();
            }
       if(model.checkIfTableEmpty("monsterStats")){

       }else {
           long stopTimeDb = Long.parseLong(stopTimeSeconds);
           long startTimeDb = Long.parseLong(startTimeSeconds);
           long monsterOfflineTime = startTimeDb - stopTimeDb;
           double monsterDistance = monster.getMonsterDistance();

           if (monsterOfflineTime - startTimeDb == 0) {
               monsterOfflineTime = Long.valueOf(0);
           }
           monsterDistance = monsterDistance + (monsterOfflineTime * 0.05);
           monster.setMonsterDistance(monsterDistance);
           model.updateMonster(monster);
           appOn = true;
       }
        }
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Creature Chase";
            String description = "Creature Chase channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
        isVisible = true;
        if (!isServiceRunning(StepCounterService.class)) {
            Intent stepCounterIntent = new Intent(this, StepCounterService.class);
            startService(stepCounterIntent);
            Log.v("stepsmain", "onresume");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
        stopTime = Calendar.getInstance().getTime();
        long seconds = stopTime.getTime()/1000;
        stopTimeSeconds =Long.toString(seconds);
        Monster monster = model.readMonster();
        monster.setTurnOffDate(stopTimeSeconds);
        model.updateMonster(monster);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean selectFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        return true;
    }

    public void setupViewpager(ViewPager viewPager) {
        viewPager.setAdapter(adapter);
    }

    public void setFragmentToHome() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    public void setItemsVisible() {
        viewPager.disableScroll(false);
        navigation.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        viewPager.setCurrentItem(0);
    }

    public void checkIfUserExist() {
        if (model.checkIfTableEmpty("user")) {
            this.databaseEmpty = true;
            viewPager.setCurrentItem(1);
            navigation.setVisibility(View.INVISIBLE);
            viewPager.disableScroll(true);
            imageButton.setVisibility(View.INVISIBLE);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}