package com.example.kaisa.androidproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kaisa.androidproject.model.DbModel;
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
    boolean isUserCreated = true;
    boolean serviceStopped;
    private final Handler handler = new Handler();
    User user = null;
    DbModel model = null;

    @Override
    public void onCreate() {
        final DbModel model = new DbModel(this);
        if(!model.checkIfTableEmpty()) {
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
        }
        else {
            setDailyResetAlarm();
            stepHelper = 0;
            totalStepCounter = 0;
            dailyStepHelper = 0;
            dailyStepCounter = 0;
            isUserCreated = false;
            resetDailySteps();
            Log.v("stepscounter", "total stepcounter reset");
        }
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
        try {
            if (intent.hasExtra("reset")) {
                resetDailySteps();
                Log.v("stepsdailyreset", "reset intent");
            }
        } catch (Exception e) {
            Log.v("stepservice", "intent null");
        }
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
                dailyStepHelper = (int) event.values[0];
            }
            totalStepCounter = countSteps - stepHelper;
            dailyStepCounter = countSteps - dailyStepHelper;
            Log.v("totalsteps", "" + totalStepCounter);
            Log.v("dailysteps", "" + dailyStepCounter);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void resetDailySteps() {
        Log.v("stepscounter", "resetDailySteps");
        dailyStepHelper = countSteps;
        dailyStepCounter = 0;
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
        Intent intent = new Intent("StepCounter");
        String sSteps = String.valueOf(totalStepCounter);
        String dSteps = String.valueOf(dailyStepCounter);
        totalDistance = totalStepCounter * 0.000762;
        dailyDistance = dailyStepCounter * 0.000762;
        if(!isUserCreated) {
            User newUser = new User("Pentti", 0, 0, 0, 0, 0, 0, 0, 0, stepHelper, dailyStepHelper, 0.0, 0.0, 0.0, "", "", 0, 0);
            model.addUserToDb(newUser);
            isUserCreated = true;
        }
        else {
            User user = model.readUserFromDb();
            user.setTotalSteps(totalStepCounter);
            user.setDailySteps(dailyStepCounter);
            user.setDailyStepHelper(dailyStepHelper);
            user.setStepHelper(stepHelper);
            user.setTotalDistance(totalDistance);
            user.setDailyDistance(dailyDistance);
            model.updateUser(user);
        }
        intent.putExtra("steps_int", totalStepCounter);
        intent.putExtra("daily_steps_int", dailyStepCounter);
        intent.putExtra("steps_string", sSteps);
        intent.putExtra("dsteps_string", dSteps);
        sendBroadcast(intent);
    }

    protected void setDailyResetAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Intent alarmIntent = new Intent(this, ResetDailyStatsBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmMgr = (AlarmManager)StepCounterService.this.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmPendingIntent);
        Log.v("stepsalarm", "alarm set");
    }
}
