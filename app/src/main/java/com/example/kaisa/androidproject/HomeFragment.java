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
    FrameLayout layout = null;
    boolean buttonVisibility = false;
    boolean rewardClaimed = false;
    MainActivity context;
    int max = 1;




    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("StepCounter"));
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = getView().findViewById(R.id.fragment_home);
        btnClaimReward = getView().findViewById(R.id.button_claim_reward);
        btnClaimReward.setVisibility(View.INVISIBLE);
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
        if (dailySteps == 0) {
            dailyTaskProgress.setText("Current progress: 0 %");
        }
        if (dailySteps < dailyStepGoal) {
            double percentage = (double)dailySteps / (double)dailyStepGoal * 100.0;
            DecimalFormat df = new DecimalFormat("####0.0");
            dailyTaskProgress.setText("Current progress: " + df.format(percentage) + " %");
        }

        else {
            dailyTaskProgress.setText("");
            dailyTask.setText("Task done!");
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


    public void claimReward() {
        btnClaimReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Reward claimed!", Toast.LENGTH_SHORT).show();
                btnClaimReward.setVisibility(View.GONE);
            }
        });
    }


    public boolean rewardClaimed() {
        if (buttonVisibility) {
            btnClaimReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Reward claimed!", Toast.LENGTH_SHORT).show();
                    btnClaimReward.setVisibility(View.GONE);
                }
            });
            return true;
        }
        return false;
    }

    public void selectRandomClothes() {
        DbModel model = new DbModel(getContext());
        ArrayList<Integer> clothesArrayList;
        User user = model.readUserFromDb();
        Random clothesRandom = new Random();
        int clothes = clothesRandom.nextInt(4);
        switch (clothes) {
            case 0:
                int hat = randomInt(max);
                clothesArrayList = model.readHats();
                while(!clothesArrayList.contains(hat)){
                    hat = randomInt(max);
                }
                user.setHat(hat);
                return;
            case 1:
                int shirt = randomInt(max);
                clothesArrayList = model.readShirts();
                while(!clothesArrayList.contains(shirt)){
                    shirt = randomInt(max);
                }
                user.setShirt(shirt);
                return;
            case 2:
                int pants = randomInt(max);
                clothesArrayList = model.readPants();
                while(!clothesArrayList.contains(pants)){
                    pants = randomInt(max);
                }
                user.setPants(pants);
                return;
            case 3:
                int shoes = randomInt(max);
                clothesArrayList = model.readShoes();
                while(!clothesArrayList.contains(shoes)){
                    shoes = randomInt(max);
                }
                user.setShoes(shoes);
                return;
        }
        model.updateUser(user);
    }

    public int randomInt(int max) {
        Random random = new Random();
        int rand = random.nextInt(2) + 1;
        return rand;
    }
}