package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

public class ResetDailyStatsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DbModel model = new DbModel(context);
        User user = model.readUserFromDb();
        int totalSteps = user.getTotalSteps();
        model.resetDailyStats(totalSteps);
        Log.v("stepsreset", "reset");
        /*Intent intent1 = new Intent(context, StepCounterService.class);
        intent1.putExtra("reset", true);
        context.startService(intent1);*/
    }
}
