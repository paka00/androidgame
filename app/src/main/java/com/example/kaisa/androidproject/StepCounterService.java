package com.example.kaisa.androidproject;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.Monster;
import com.example.kaisa.androidproject.model.User;

import java.util.Calendar;

public class StepCounterService extends Service implements SensorEventListener {

    SensorManager sensorManager = null;
    Sensor stepCounterSensor = null;
    Sensor stepDetectorSensor = null;
    int stepHelper;
    int totalStepCounter;
    int dailyStepCounter;
    int dailyStepHelper;
    int countSteps;
    double totalDistance;
    double dailyDistance;
    boolean serviceStopped;
    private final Handler handler = new Handler();
    User user = null;
    DbModel model = null;
    double monsterSpeed = 0.5;
    int notificationId = 1;

    @Override
    public void onCreate() {
        model = new DbModel(this);
        if (!model.checkIfTableEmpty("user")) {
            try {
                user = model.readUserFromDb();
                totalStepCounter = user.getSteps();
                dailyStepCounter = user.getDailySteps();
                stepHelper = user.getStepHelper();
                dailyStepHelper = user.getDailyStepHelper();
                Log.v("stepservice", "totalsteps from db " + totalStepCounter);
                Log.v("stepservice", "stepcounter: " + stepHelper);
            } catch (SQLiteException e) {
                if (e.getMessage().contains("no such table")) {
                    Log.v("stepsdb", "table doesn't exist");
                }
            }
        } else {
            stepHelper = 0;
            totalStepCounter = 0;
            dailyStepHelper = 0;
            dailyStepCounter = 0;
            Log.v("stepscounter", "total stepcounter reset");
        }
        setDailyResetAlarm();
        super.onCreate();
        serviceStopped = false;
        Log.v("stepservice", "oncreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepCounterSensor, 0);
        sensorManager.registerListener(this, stepDetectorSensor, 0);
        Log.v("stepservice", "onstart");
        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStopped = true;
        Log.v("stepservice", "ondestroy");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            countSteps = (int) event.values[0];
            if (stepHelper == 0) {
                Log.v("stepscounter", "stepcounter = 0");
                stepHelper = (int) event.values[0];
            }
            totalStepCounter = countSteps - stepHelper;
            Log.v("totalsteps", "" + totalStepCounter);
            Log.v("dailysteps", "" + dailyStepCounter);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!serviceStopped) {
                broadcastSteps();
                handler.postDelayed(this, 10000);
            }
        }
    };

    private void broadcastSteps() {
        Log.v("stepservice", "broadcaststeps");
        model = new DbModel(StepCounterService.this);
        User user = model.readUserFromDb();
        Monster monster = model.readMonster();
        Intent intent = new Intent("StepCounter");
        if (!model.checkIfTableEmpty("user")) {
            dailyStepHelper = user.getDailyStepHelper();
            Log.v("stepservice", "dailyStepHelper: " + dailyStepHelper);
            dailyStepCounter = totalStepCounter - dailyStepHelper;
            dailyDistance = dailyStepCounter * 0.000762;
            totalDistance = user.getTotalDistance();

            double monsterDistance = monster.getMonsterDistance();
            monsterDistance = monsterDistance + monsterSpeed * user.getDifficultyLevel();
            monster.setMonsterDistance(monsterDistance);

            user.setTotalSteps(totalStepCounter);
            user.setTotalDistance(totalDistance);
            user.setDailySteps(dailyStepCounter);
            user.setDailyStepHelper(dailyStepHelper);
            user.setStepHelper(stepHelper);
           // user.setDailyDistance(dailyDistance);

            if(user.getDailySteps() >= user.getDailyStepGoal() && user.getDailyStepGoal() != 0 && user.getDailyReward() == 0 && !MainActivity.isVisible) {
                showNotification("Creature Chase", "You have finished your daily quest!");
            }

            model.updateMonster(monster);
            model.updateUser(user);
        }
        String sSteps = String.valueOf(totalStepCounter);
        String dSteps = String.valueOf(dailyStepCounter);
        intent.putExtra("steps_int", totalStepCounter);
        intent.putExtra("daily_steps_int", dailyStepCounter);
        intent.putExtra("steps_string", sSteps);
        intent.putExtra("dsteps_string", dSteps);
        sendBroadcast(intent);
    }

    protected void setDailyResetAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent alarmIntent = new Intent(this, ResetDailyStatsBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmMgr = (AlarmManager) StepCounterService.this.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmPendingIntent);
        Log.v("stepsalarm", "alarm set");
    }

    public void showNotification(String title, String description) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.monster_idle_1)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }
}
