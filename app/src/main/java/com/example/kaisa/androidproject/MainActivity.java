package com.example.kaisa.androidproject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent stepCounterIntent = new Intent(this, StepCounterService.class);
        startService(stepCounterIntent);
        registerReceiver(broadcastReceiver, new IntentFilter(StepCounterService.BROADCAST_ACTION));
        //setDailyResetAlarm();
        Log.v("stepsmain", "oncreate");
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
}
