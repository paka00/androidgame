package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView dailyTask = null;
    TextView dailyTaskTime = null;
    TextView dailyTaskProgress = null;
    int dailySteps;
    int dailyStepGoal;
    CountDownTimer countDownTimer = null;
    DbModel model = null;
    Button btnClaimReward, btnTest = null;
    MainActivity context;
    int maxClothes = 3;
    ArrayList<Integer> clothesArrayList;
    List<Integer> checkedValues;
    int clothesType;
    int clothesID;
    ConstraintLayout bg = null;
    boolean randomizeDailyStepGoal = true;
    SharedPreferences prefs = null;
    Typeface pixelFont = null;


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
        pixelFont = Typeface.createFromAsset(getContext().getAssets(),  "fonts/smallest_pixel-7.ttf");
        btnClaimReward = getView().findViewById(R.id.button_claim_reward);
        btnClaimReward.setVisibility(View.INVISIBLE);
        btnTest = getView().findViewById(R.id.test_button);
        dailyTask = getView().findViewById(R.id.daily_task);
        bg = getView().findViewById(R.id.fragment_home);
        Glide.with(this).load(R.drawable.paanakyma).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bg.setBackground(resource);
                }
            }
        });
        model = new DbModel(getContext());
        dailyTaskProgress = getView().findViewById(R.id.daily_task_progress);
        if(!model.checkIfTableEmpty("user")) {
            User user = model.readUserFromDb();
            dailySteps = user.getDailySteps();
            dailyStepGoal = user.getDailyStepGoal();
            if(dailyStepGoal == 0){
                dailyStepGoal = getRandomSteps();
            }
            dailyStepsCheck();
            randomizeDailyStepGoal = false;
        }
        if(randomizeDailyStepGoal) {
            dailyStepGoal = getRandomSteps();
        }
        Log.d("homefragment", "onviewcreated");
        dailyTask.setText("Daily task: Walk " + dailyStepGoal + " steps");
        startCountdownTimer();

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRandomClothes();
                Intent intent = new Intent(getActivity(), RewardActivity.class);
                intent.putExtra("TYPE", clothesType);
                intent.putExtra("ID", clothesID);
                startActivity(intent);
            }
        });
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            try {
                User user = model.readUserFromDb();
                user.setDailyStepGoal(dailyStepGoal);
                model.updateUser(user);
            } catch (NullPointerException e) {
                Log.d("homefragment", e.toString());
            }
            if (!prefs.getBoolean("appHasRunBeforeHome", false)) {
                LayoutInflater inflater = (LayoutInflater) this
                        .getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialoglayout = inflater.inflate(R.layout.instruction_dialog_layout, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialoglayout);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.tekstilaatikko));
                alertDialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                ImageButton doneBtn = dialoglayout.findViewById(R.id.dialog_done_btn);
                TextView titleText = dialoglayout.findViewById(R.id.dialog_welcome_text);
                titleText.setTypeface(pixelFont);
                titleText.setText("Welcome to Creature Chase!");
                TextView bodyText = dialoglayout.findViewById(R.id.dialog_instruction_text);
                bodyText.setTypeface(pixelFont);
                bodyText.setText("This is the home page where you will find some essential information about your character. " +
                        "You can check your level, your characters appearance and your progress on the current daily quest. " +
                        "Go check out the other pages from the buttons at the bottom of the screen!");
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                prefs.edit().putBoolean("appHasRunBeforeHome", true).apply();
                Log.d("homefragment", "firstrun");
            }
            Log.d("homefragment", "setuservisiblehint");
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dailySteps = intent.getIntExtra("daily_steps_int", 0);
            model = new DbModel(getContext());
            if(!model.checkIfTableEmpty("user")) {
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
                    selectRandomClothes();
                    Intent intent = new Intent(getActivity(), RewardActivity.class);
                    intent.putExtra("TYPE", clothesType);
                    intent.putExtra("ID", clothesID);
                    startActivity(intent);
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
        Log.d("homefragment", "onResume");
    }

    protected void startCountdownTimer() {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeZone(TimeZone.getDefault());
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
                DbModel model = new DbModel(getContext());
                User user = model.readUserFromDb();
                int totalSteps = user.getTotalSteps();
                dailyStepGoal = getRandomSteps();
                dailyTask.setText("Daily task: Walk " + dailyStepGoal + " steps");
                user.setDailyStepHelper(totalSteps);
                user.setDailyReward(0);
                user.setDailyStepGoal(dailyStepGoal);
                model.updateUser(user);
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
        int gender = user.getGender();
        Random clothesRandom = new Random();
        clothesType = clothesRandom.nextInt(4);
        Log.v("clothesType", "clothesType type = " + clothesType);
        if(!checkedValues.contains(clothesType) && checkIfAllUnlocked(clothesType, gender)) {
            Log.v("clothesType", "clothesType type full");
            checkedValues.add(clothesType);
            selectRandomClothes();
        }
        else if (checkedValues.contains(clothesType)){
            if(checkedValues.size() == 4){
                Toast.makeText(getContext(), "No more rewards left!", Toast.LENGTH_LONG).show();
            }
            else {
                selectRandomClothes();
            }
        }
        else {
            clothesID = randomInt(maxClothes);
            switch (clothesType) {
                case 0:
                    Log.v("clothesType", "random clothesID = " + clothesID);
                    clothesArrayList = model.readHats(gender);
                    while (clothesArrayList.contains(clothesID)) {
                        clothesID = randomInt(maxClothes);
                        Log.v("clothesType", "random clothesID was in db, new random clothesID = " + clothesID);
                    }
                    model.addHat(clothesID, gender);
                    Log.v("clothesType", "added clothesID to db");
                    break;
                case 1:
                    Log.v("clothesType", "random clothesID = " + clothesID);
                    clothesArrayList = model.readShirts(gender);
                    while (clothesArrayList.contains(clothesID)) {
                        clothesID = randomInt(maxClothes);
                        Log.v("clothesType", "random clothesID was in db, new random clothesID = " + clothesID);
                    }
                    model.addShirt(clothesID, gender);
                    Log.v("clothesType", "added clothesID to db");
                    break;
                case 2:
                    Log.v("clothesType", "random clothesID = " + clothesID);
                    clothesArrayList = model.readPants(gender);
                    while (clothesArrayList.contains(clothesID)) {
                        clothesID = randomInt(maxClothes);
                        Log.v("clothesType", "random clothesID was in db, new random clothesID = " + clothesID);
                    }
                    model.addPants(clothesID, gender);
                    Log.v("clothesType", "added clothesID to db");
                    break;
                case 3:
                    Log.v("clothesType", "random clothesID = " + clothesID);
                    clothesArrayList = model.readShoes(gender);
                    while (clothesArrayList.contains(clothesID)) {
                        clothesID = randomInt(maxClothes);
                        Log.v("clothesType", "random clothesID was in db, new random clothesID = " + clothesID);
                    }
                    model.addShoes(clothesID, gender);
                    Log.v("clothesType", "added clothesID to db");
                    break;
            }
            //sendRandomClothes(clothesID);
            model.updateUser(user);

        }
    }

    public int randomInt(int max) {
        Random random = new Random();
        int rand = random.nextInt(max) + 1;
        return rand;
    }

    public boolean checkIfAllUnlocked(int i, int g) {
        boolean bool = false;
        switch (i) {
            case 0:
                clothesArrayList = model.readHats(g);
                break;
            case 1:
                clothesArrayList = model.readShirts(g);
                break;
            case 2:
                clothesArrayList = model.readPants(g);
                break;
            case 3:
                clothesArrayList = model.readShoes(g);
                break;
        }
        if (clothesArrayList.size() >= maxClothes) {
            bool = true;
        }
        return bool;
    }

    public int getRandomSteps() {
        Random r = new Random();
        int i = r.nextInt((10 - 5) + 1) + 5;
        return i * 1000;
    }
}