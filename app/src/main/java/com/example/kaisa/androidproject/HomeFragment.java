package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbRequest;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    TextView levelTextView = null;
    int dailySteps, totalSteps;
    int dailyStepGoal;
    CountDownTimer countDownTimer = null;
    DbModel model = null;
    Button btnClaimReward = null;
    MainActivity context;
    int maxClothes = 3;
    ArrayList<Integer> clothesArrayList;
    List<Integer> checkedValues;
    int clothesType;
    int clothesID;
    ConstraintLayout bg, exitDialogLayout = null;
    boolean randomizeDailyStepGoal = true;
    SharedPreferences prefs = null;
    Typeface pixelFont = null;
    private boolean isVisible;
    private boolean isStarted;
    private ImageView levelProgress, levelBarBackground;
    View view2;
    int level = 1;
    int stepsToNextLevel, previousStepsToNextLevel;


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
        pixelFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/smallest_pixel-7.ttf");
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        levelTextView = getView().findViewById(R.id.level_text);
        Glide.with(this).load(R.drawable.leveli_palkki_tausta).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    levelTextView.setBackground(resource);
                }
            }
        });
        levelProgress = getView().findViewById(R.id.level_bar);
        levelBarBackground = getView().findViewById(R.id.level_bar_background);
        view2 = getView().findViewById(R.id.level_bar_view);
        btnClaimReward = getView().findViewById(R.id.button_claim_reward);
        btnClaimReward.setVisibility(View.INVISIBLE);
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
        if (!model.checkIfTableEmpty("user")) {
            User user = model.readUserFromDb();
            dailySteps = user.getDailySteps();
            dailyStepGoal = user.getDailyStepGoal();
            if(dailyStepGoal == 0){
                dailyStepGoal = getRandomSteps();
            }
            dailyStepsCheck();
            levelCheck();
            randomizeDailyStepGoal = false;
        }
        if (randomizeDailyStepGoal) {
            dailyStepGoal = getRandomSteps();
        }
        Log.d("homefragment", "onviewcreated");
        dailyTask.setText("Daily task: Walk " + dailyStepGoal + " steps");
        levelTextView.setText("" + level);
        startCountdownTimer();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        LayoutInflater inflater = (LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialoglayout = inflater.inflate(R.layout.exit_dialog_layout, null);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setView(dialoglayout);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Button cancelBtn = dialoglayout.findViewById(R.id.dialog_cancel_btn);
                        Button okBtn = dialoglayout.findViewById(R.id.dialog_ok_btn);
                        TextView titleText = dialoglayout.findViewById(R.id.dialog_welcome_text);
                        titleText.setTypeface(pixelFont);
                        titleText.setText("Are you sure you want to exit?");
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((MainActivity)getActivity()).closeActivity();
                            }
                        });
                        return true;
                    }
                }
                return false;
            }
        });
        setLevelBar();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if(isVisibleToUser) {
            Log.d("homefragment", "setuservisiblehint");
        }
        if (isVisible && isStarted) {
            createDialog();
            try {
                dailyStepsCheck();
                levelCheck();
                User user = model.readUserFromDb();
                user.setDailyStepGoal(dailyStepGoal);
                model.updateUser(user);
            } catch (NullPointerException e) {
                Log.d("homefragment", e.toString());
            }
        }
    }

    //Creates a dialog box with information about the current page.
    public void createDialog() {
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        if (!prefs.getBoolean("appHasRunBeforeHome", false)) {
            LayoutInflater inflater = (LayoutInflater) this
                    .getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialoglayout = inflater.inflate(R.layout.instruction_dialog_layout, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialoglayout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button doneBtn = dialoglayout.findViewById(R.id.dialog_done_btn);
            TextView titleText = dialoglayout.findViewById(R.id.dialog_welcome_text);
            titleText.setTypeface(pixelFont);
            titleText.setText("Home page");
            TextView bodyText = dialoglayout.findViewById(R.id.dialog_instruction_text);
            bodyText.setTypeface(pixelFont);
            bodyText.setText("Now that you've created your character, you can start your journey!\n " +
                    "This is the home page where you will find some essential information about your character. " +
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
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dailySteps = intent.getIntExtra("daily_steps_int", 0);
            model = new DbModel(getContext());
            if (!model.checkIfTableEmpty("user")) {
                dailyStepsCheck();
                levelCheck();
            }
        }
    };

    protected void levelCheck() {
        User user = model.readUserFromDb();
        level = user.getLevel();
        totalSteps = user.getTotalSteps();
        stepsToNextLevel = previousStepsToNextLevel + level * 100;
        Log.d("stepstonextlevel", "" + stepsToNextLevel);
        if(totalSteps >= stepsToNextLevel) {
            previousStepsToNextLevel = stepsToNextLevel;
            level++;
            user.setLevel(level);
            model.updateUser(user);
        }
        levelTextView.setText("" + level);
    }

    protected void dailyStepsCheck() {
        final User user = model.readUserFromDb();

        if (dailySteps == 0) {
            dailyTaskProgress.setText("Current progress: 0 %");
        }
        if (dailySteps < dailyStepGoal) {
            double percentage = (double) dailySteps / (double) dailyStepGoal * 100.0;
            DecimalFormat df = new DecimalFormat("####0.0");
            dailyTaskProgress.setText("Current progress: " + df.format(percentage) + " %");
        }
        if (dailySteps >= dailyStepGoal && user.getDailyReward() == 0) {
            btnClaimReward.setVisibility(View.VISIBLE);
            dailyTaskProgress.setText("");
            dailyTask.setText("Task done!");
            btnClaimReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectRandomClothes();
                    Intent intent = new Intent(getActivity(), RewardActivity.class);
                    intent.putExtra("TYPE", clothesType);
                    intent.putExtra("ID", clothesID);
                    startActivity(intent);
                    dailyTask.setText("Task done!");
                    btnClaimReward.setVisibility(View.GONE);
                    user.setDailyReward(1);
                    model.updateUser(user);
                }
            });
        }

        if (dailySteps >= dailyStepGoal && user.getDailyReward() == 1) {
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
        Log.d("homefragment", "onResume");
    }

    //Starts a timer which ends at midnight each day
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
                } else {
                    dailyTaskTime.setText("Time until next \ntask: " + timeRemaining);
                }
                try {
                    User user = model.readUserFromDb();
                    user.setDailyStepGoal(dailyStepGoal);
                    model.updateUser(user);
                    dailyStepsCheck();
                } catch (Exception e) {
                    Log.e("homefragment", e.toString());
                }
            }

            @Override
            public void onFinish() {
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

    //Formats the time to HH:MM:SS.
    //For some reason the already existing tools for that didn't work.
    protected String formatTime(long hours, long minutes, long seconds) {
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

    //Randomizes a piece of clothing.
    //First a random int from 0 to 3 is taken, which determines whether the random piece of clothing is a hat, a shirt, pants or shoes.
    //Then a check if all the clothes from a certain clothing type have been unlocked.
    //Finally, another random int is taken, which is the ID for that particular piece of clothing.
    public void selectRandomClothes() {
        model = new DbModel(getContext());
        User user = model.readUserFromDb();
        int gender = user.getGender();
        Random clothesRandom = new Random();
        clothesType = clothesRandom.nextInt(4);
        Log.v("clothesType", "clothesType type = " + clothesType);
        if (!checkedValues.contains(clothesType) && checkIfAllUnlocked(clothesType, gender)) {
            Log.v("clothesType", "clothesType type full");
            checkedValues.add(clothesType);
            selectRandomClothes();
        } else if (checkedValues.contains(clothesType)) {
            if (checkedValues.size() == 4) {
                Toast.makeText(getContext(), "No more rewards left!", Toast.LENGTH_LONG).show();
            } else {
                selectRandomClothes();
            }
        } else {
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
            model.updateUser(user);
        }
    }

    //Returns a random int from range 1 to max.
    public int randomInt(int max) {
        Random random = new Random();
        int rand = random.nextInt(max) + 1;
        return rand;
    }

    //Used to check if the database already has all of the certain type of clothing.
    //i is the type of clothing (e.g. a hat, which is 0), and g is the gender of the users character.
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

    //Takes a random int from 5 to 10 and multiplies it by 1000, and returns it.
    public int getRandomSteps() {
        Random r = new Random();
        int i = r.nextInt((10 - 5) + 1) + 5;
        return i * 1000;
    }

    public void setLevelBar(){
        int steps = totalSteps-stepsToNextLevel;
        User user = model.readUserFromDb();
        int level = user.getLevel();
        int steplevel = level*100;
        int kerroin = steps / steplevel;
        view2.setMinimumHeight(500);
        levelBarBackground.setBackgroundColor(Color.RED);
        //10 steppia
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible) {
            createDialog();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }
}