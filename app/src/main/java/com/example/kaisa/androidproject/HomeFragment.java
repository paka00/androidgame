package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView dailyTask = null;
    TextView dailyTaskTime = null;
    TextView dailyTaskProgress = null;
    int dailySteps;
    int dailyStepGoal = 5000;
    CountDownTimer countDownTimer= null;
    DbModel model = null;
    Button btnClaimReward = null;
    MainActivity context;
    int max = 10;
    Button addHat = null;
    ArrayList<Integer> clothesArrayList;
    List<Integer> checkedValues;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("StepCounter"));
        context = (MainActivity) container.getContext();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        checkedValues = new ArrayList<>();
        super.onViewCreated(view, savedInstanceState);
        btnClaimReward = getView().findViewById(R.id.button_claim_reward);
        btnClaimReward.setVisibility(View.INVISIBLE);
        addHat = getView().findViewById(R.id.add_hat_button);
        addHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRandomClothes();
            }
        });
        dailyTask = getView().findViewById(R.id.daily_task);
        dailyTask.setText("Daily task: Walk " + dailyStepGoal + " steps");
        model = new DbModel(getContext());
        dailyTaskProgress = getView().findViewById(R.id.daily_task_progress);
        if(!model.checkIfTableEmpty()) {
            User user = model.readUserFromDb();
            dailySteps = user.getDailySteps();
            dailyStepsCheck();
        }
        startCountdownTimer();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dailySteps = intent.getIntExtra("daily_steps_int", 0);
            model = new DbModel(getContext());
            if(!model.checkIfTableEmpty()) {
                dailyStepsCheck();
            }
        }
    };

    protected void dailyStepsCheck() {
        final User user = model.readUserFromDb();

        if (dailySteps == 0) {
            dailyTaskProgress.setText("Current progress: 0 %");
        }
        if (dailySteps < dailyStepGoal) {
            double percentage = (double)dailySteps / (double)dailyStepGoal * 100.0;
            DecimalFormat df = new DecimalFormat("####0.0");
            dailyTaskProgress.setText("Current progress: " + df.format(percentage) + " %");
        }
        if(dailySteps >= dailyStepGoal && user.getDailyReward() == 0) {
            btnClaimReward.setVisibility(View.VISIBLE);
            dailyTaskProgress.setText("");
            dailyTask.setText("Task done! You can now claim the reward");
            btnClaimReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Reward claimed!", Toast.LENGTH_SHORT).show();
                    dailyTask.setText("Task done! Wait for tomorrow");
                    btnClaimReward.setVisibility(View.GONE);
                    user.setDailyReward(1);
                    model.updateUser(user);
                }
            });
        }

        if(dailySteps >= dailyStepGoal && user.getDailyReward() == 1) {
            dailyTaskProgress.setText("");
            dailyTask.setText("Task done! Wait for tomorrow!");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("StepCounter"));
    }

    protected void startCountdownTimer() {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeZone(TimeZone.getDefault());
        /*int curYear = currentCalendar.get(Calendar.YEAR);
        int curMonth = currentCalendar.get(Calendar.MONTH);
        int curDay = currentCalendar.get(Calendar.DAY_OF_MONTH);*/
        Calendar timerEndCalendar = Calendar.getInstance();
        timerEndCalendar.setTimeZone(TimeZone.getDefault());
        timerEndCalendar.add(Calendar.DATE, 1);
        timerEndCalendar.set(Calendar.HOUR_OF_DAY, 0);
        timerEndCalendar.set(Calendar.MINUTE, 0);
        timerEndCalendar.set(Calendar.SECOND, 0);
        long timerMillis = timerEndCalendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
        countDownTimer = new CountDownTimer(timerMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = millisUntilFinished / 3600000;
                long minutes = (millisUntilFinished - (hours * 3600000)) / 60000;
                long seconds = (millisUntilFinished - (hours * 3600000) - (minutes * 60000)) / 1000;
                String timeRemaining = formatTime(hours, minutes, seconds);
                dailyTaskTime = getView().findViewById(R.id.daily_task_time);
                if (dailySteps < dailyStepGoal) {
                    dailyTaskTime.setText("Time remaining: " + timeRemaining);
                }
                else {
                    dailyTaskTime.setText("Time until next task: " + timeRemaining);
                }
            }

            @Override
            public void onFinish() {
                startCountdownTimer();
            }
        };
        countDownTimer.start();
    }

    protected String formatTime (long hours, long minutes, long seconds) {
        String hour = "" + hours;
        String minute = "" + minutes;
        String second = "" + seconds;
        if (hours < 10) {
            hour = "0" + hours;
        }
        if (minutes < 10) {
            minute = "0" + minutes;
        }
        if (seconds < 10) {
            second = "0" + seconds;
        }
        String timeRemaining = hour + ":" + minute + ":" + second;
        return timeRemaining;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public void selectRandomClothes() {
        model = new DbModel(getContext());
        User user = model.readUserFromDb();
        Random clothesRandom = new Random();
        int clothes = clothesRandom.nextInt(4);
        Log.v("clothes", "clothes type = " + clothes);
        if(!checkedValues.contains(clothes) && checkIfAllUnlocked(clothes)) {
            Log.v("clothes", "clothes type full");
            checkedValues.add(clothes);
            selectRandomClothes();
        }
        else if (checkedValues.contains(clothes)){
            if(checkedValues.size() == 4){
                Toast.makeText(getContext(), "No more rewards left!", Toast.LENGTH_LONG).show();
            }
            else {
                selectRandomClothes();
            }
        }
        else {
            switch (clothes) {
                case 0:
                    int hat = randomInt(max);
                    Log.v("clothes", "random hat = " + hat);
                    clothesArrayList = model.readHats();
                    while (clothesArrayList.contains(hat)) {
                        hat = randomInt(max);
                        Log.v("clothes", "random hat was in db, new random hat = " + hat);
                    }
                    model.addHat(hat);
                    Log.v("clothes", "added hat to db");
                    break;
                case 1:
                    int shirt = randomInt(max);
                    Log.v("clothes", "random shirt = " + shirt);
                    clothesArrayList = model.readShirts();
                    while (clothesArrayList.contains(shirt)) {
                        shirt = randomInt(max);
                        Log.v("clothes", "random shirt was in db, new random hat = " + shirt);
                    }
                    model.addShirt(shirt);
                    Log.v("clothes", "added shirt to db");
                    break;
                case 2:
                    int pants = randomInt(max);
                    Log.v("clothes", "random pants = " + pants);
                    clothesArrayList = model.readPants();
                    while (clothesArrayList.contains(pants)) {
                        pants = randomInt(max);
                        Log.v("clothes", "random pants was in db, new random hat = " + pants);
                    }
                    model.addPants(pants);
                    Log.v("clothes", "added pants to db");
                    break;
                case 3:
                    int shoes = randomInt(max);
                    Log.v("clothes", "random shoes = " + shoes);
                    clothesArrayList = model.readShoes();
                    while (clothesArrayList.contains(shoes)) {
                        shoes = randomInt(max);
                        Log.v("clothes", "random shoes was in db, new random hat = " + shoes);
                    }
                    model.addShoes(shoes);
                    Log.v("clothes", "added shoes to db");
                    break;
            }
            model.updateUser(user);

        }
    }

    public int randomInt(int max) {
        Random random = new Random();
        int rand = random.nextInt(max) + 1;
        return rand;
    }

    public boolean checkIfAllUnlocked(int i) {
        boolean bool = false;
        switch (i){
            case 0:
                clothesArrayList = model.readHats();
                break;
            case 1:
                clothesArrayList = model.readShirts();
                break;
            case 2:
                clothesArrayList = model.readPants();
                break;
            case 3:
                clothesArrayList = model.readShoes();
                break;
        }
        if (clothesArrayList.size() >= max){
            bool = true;
        }
        return bool;
    }
}