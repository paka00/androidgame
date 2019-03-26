package com.example.kaisa.androidproject;

import android.app.Service;
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

import java.util.ArrayList;

public class StepCounterService extends Service implements SensorEventListener {

    SensorManager sensorManager = null;
    Sensor stepCounterSensor = null;
    Sensor stepDetectorSensor = null;
    int stepCounter;
    int totalStepCounter;
    int dailyStepCounter;
    int dailyStepHelper;
    public boolean checkDailySteps = true;
    boolean isUserCreated = true;
    public static final String BROADCAST_ACTION = "StepCounter";
    boolean serviceStopped;
    private final Handler handler = new Handler();
    ArrayList<User> userStats = new ArrayList<>();
    DbModel model = null;

    @Override
    public void onCreate() {
        final DbModel model = new DbModel(this);
        if(!model.checkIfTableEmpty()) {
            try {
                userStats = model.readUserFromDb();
                totalStepCounter = userStats.get(0).getSteps();
                dailyStepCounter = userStats.get(0).getDailySteps();
            } catch (SQLiteException e) {
                if (e.getMessage().contains("no such table")) {
                    Log.e("stepdb", "table doesn't exist");
                }
            }
        }
        else {
            stepCounter = 0;
            isUserCreated = false;
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
        if (intent.hasExtra("reset")) {
            if (intent.getBooleanExtra("reset", true)) {
                checkDailySteps = intent.getBooleanExtra("reset", true);
                dailyStepCounter = 0;
            }
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
            int countSteps = (int) event.values[0];
            if (checkDailySteps) {
                dailyStepHelper = (int) event.values[0];
                checkDailySteps = false;
            }
            if (stepCounter == 0) {
                stepCounter = (int) event.values[0];
            }
            totalStepCounter = countSteps - stepCounter;
            dailyStepCounter = countSteps - dailyStepHelper;
            Log.v("newsteps", "" + totalStepCounter);
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
        Intent intent = new Intent(BROADCAST_ACTION);
        String sSteps = String.valueOf(totalStepCounter);
        String dSteps = String.valueOf(dailyStepCounter);
        User user = new User(totalStepCounter, dailyStepCounter);
        if(!isUserCreated) {
            model.addUserToDb(user);
            isUserCreated = true;
        }
        else {
            model.updateUser(user);
        }
        intent.putExtra("steps_int", totalStepCounter);
        intent.putExtra("steps_string", sSteps);
        intent.putExtra("dsteps_string", dSteps);
        sendBroadcast(intent);
    }
}
