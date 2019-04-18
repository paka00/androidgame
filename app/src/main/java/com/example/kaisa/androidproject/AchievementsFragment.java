package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.kaisa.androidproject.model.Achievement;
import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.Monster;
import com.example.kaisa.androidproject.model.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AchievementsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView textView = null;
    Button boxButton = null;
    View gift = null;
    float monsterDistance;
    float monsterTravelledDistance;

    float distancem = 0;
    static final float distancerange = 10000;
    float percentagedistance = 0;
    TextView characterdistancetxt = null;
    float travelleddistance = 0;
    ImageView giftimg = null;
    int dailySteps = 0;
    int totalSteps = 0;
    int memorysteps = 0;
    double dbdistance = 0;
    String dbwalktime;
    String dbjogdate;
    TextView jogdata = null;
    double dbdailydistance = 0;
    AnimationDrawable wifianimation;
    AnimationDrawable monsterAnimation;
    AnimationDrawable characterWalkAnimation;
    ImageView characterimg = null;
    AchievementArrayAdapter adapter = null;
    ArrayList<Achievement> arrayListAchievements;
    String totalDistanceFormatted;

    ImageView monsterimg = null;
    ListView listView;
    DbModel dbModel;
    View monster = null;
    SharedPreferences prefs = null;
    Typeface pixelFont = null;
    private boolean isVisible;
    private boolean isStarted;
    ConstraintLayout bg = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("StepCounter"));
        return inflater.inflate(R.layout.fragment_achievements, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //gift = getView().findViewById(R.id.box);
        pixelFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/smallest_pixel-7.ttf");
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        bg = getView().findViewById(R.id.clayout);
        Glide.with(this).load(R.drawable.juoksutausta3).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bg.setBackground(resource);
                }
            }
        });
        //boxButton = getView().findViewById(R.id.moveButton);
        //gift = getView().findViewById(R.id.box);
        giftimg = getView().findViewById(R.id.gift);
        giftimg.setBackgroundResource(R.drawable.animationtest);
        wifianimation = (AnimationDrawable) giftimg.getBackground();
        //monster = getView().findViewById(R.id.monsterline);
        listView = getView().findViewById(R.id.listview_achievements);
        monsterimg = getView().findViewById(R.id.monsterImage);
        monsterimg.setBackgroundResource(R.drawable.animationtest2);
        monsterAnimation = (AnimationDrawable) monsterimg.getBackground();
        dbModel = new DbModel(getContext());
        characterimg = getView().findViewById(R.id.characterImage);
        characterimg.setBackgroundResource(R.drawable.animationwalkcyclecharacter);
        characterWalkAnimation = (AnimationDrawable) characterimg.getBackground();

        characterdistancetxt = getView().findViewById(R.id.distancecharacter);

        jogdata = getView().findViewById(R.id.userdata);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        ((MainActivity) getActivity()).setFragmentToHome();

                        return true;
                    }
                }
                return false;
            }
        });


        getdbdata();
        updatedistance();

        arrayListAchievements = dbModel.readAchievements();
        adapter = new AchievementArrayAdapter(getContext(), arrayListAchievements);
        listView.setAdapter(adapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isStarted) {
            createDialog();
        }
    }

    public void createDialog() {
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        if (!prefs.getBoolean("appHasRunBeforeAchievement", false)) {
            LayoutInflater inflater = (LayoutInflater) this
                    .getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialoglayout = inflater.inflate(R.layout.instruction_dialog_layout, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dialoglayout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tekstilaatikko));
            alertDialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
            ImageButton doneBtn = dialoglayout.findViewById(R.id.dialog_done_btn);
            TextView titleText = dialoglayout.findViewById(R.id.dialog_welcome_text);
            titleText.setTypeface(pixelFont);
            titleText.setText("The Creature and achievements");
            TextView bodyText = dialoglayout.findViewById(R.id.dialog_instruction_text);
            bodyText.setTypeface(pixelFont);
            bodyText.setText("On the top you can see the creature chasing you. You have to run away from it by walking in real life! " +
                    "If you get far enough you will get rewards.\n" +
                    "On the bottom are your stats and achievements. You will also get rewards from the achievements. Try to complete them all! " +
                    "\nIf you are planning to go outside for a walk now, check out the walk page by pressing the shoe at the bottom.");
            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            prefs.edit().putBoolean("appHasRunBeforeAchievement", true).apply();
            Log.d("homefragment", "firstrun");

        }
    }

    private void getdbdata() {
        final DbModel model = new DbModel(getContext());
        if (!model.checkIfTableEmpty("user")) {
            User user = model.readUserFromDb();
            Monster monster = model.readMonster();
            totalSteps = user.getTotalSteps();
            dailySteps = user.getDailySteps();
            user.setTotalDistance(5050);
            model.updateUser(user);
            dbdistance = user.getTotalDistance();
            dbjogdate = user.getWalkDate();
            dbwalktime = user.getWalkTime();
            dbdailydistance = user.getDailyDistance();
            textView = getView().findViewById(R.id.steps);
            String steps = String.valueOf(totalSteps);
            String dSteps = String.valueOf(dailySteps);
            textView.setText("Total steps: " + steps + "\nDaily steps: " + dSteps);
            //  textView.setText("monsterdistance: " + monster.getMonsterDistance());
            dbdistance = dbdistance + totalSteps * 1;
            totalDistanceFormatted = String.format("%.2f", dbdistance);

            //jogdata.setText("Distance: " + formattedValue +"\nDaily distance: "+ dbdailydistance +"\nTotal jog time: " + dbwalktime + "\nlast jog was on: "+ dbjogdate);
            jogdata.setText("Monster: " + monster.getMonsterDistance() + "\nDistance: " + totalDistanceFormatted + "\nDaily distance: " + dbdailydistance + "\nTotal jog time: " + dbwalktime + "\nlast jog was on: " + dbjogdate);
        }

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView = getView().findViewById(R.id.steps);
            String steps = intent.getStringExtra("steps_string");
            String dSteps = intent.getStringExtra("dsteps_string");
            textView.setText("Total steps: " + steps + "\nDaily steps: " + dSteps);
            updatedistance();

        }
    };


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("StepCounter"));
        getdbdata();
        updatedistance();
        //updateAchievements();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    public void updatedistance() {
      /*  final DbModel model = new DbModel(getContext());
        Monster monsterDb = model.readMonster();

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        percentagedistance = dpWidth / distancerange;
        travelleddistance = (float) dbdistance;
        distancem = travelleddistance % 5000;
        distancem = dpWidth-(distancem * percentagedistance);
        gift.setX(distancem);
        characterdistancetxt.setText(Float.toString(travelleddistance)+ "m");
        giftimg.setX(distancem-dpWidth/16);
        wifianimation.start();
        monsterAnimation.start();
        characterWalkAnimation.start();
        //float monsterTravelledDistance = Float.valueOf(Double.toString(monsterDb.getMonsterDistance()));
        double monsterD = monsterDb.getMonsterDistance();
        monsterTravelledDistance = (float) monsterD;
        //monster distance ja monster traveleddista pitää olla monster.getMonsterDistance

        // monsterDistance =   monsterTravelledDistance * percentagedistance;
        if(dbdistance-monsterTravelledDistance<6000&&dbdistance>5500){
            monsterDistance = monsterTravelledDistance;
            monsterDistance = dpWidth/2-(travelleddistance - monsterDistance ) *percentagedistance;
            monster.setX(monsterDistance);
            monsterimg.setX(monsterDistance-dpWidth/10);
        }
        if(travelleddistance-monsterTravelledDistance<0){
            monsterDb.setMonsterDistance(dbdistance-6000);
        }



        if (distancem <= (dpWidth / 2)) {
            distancem = dpWidth;
            gift.setX(distancem);

        }*/
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




