package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kaisa.androidproject.model.Achievement;
import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView textView = null;
    Button boxButton = null;
    View gift = null;
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
    ImageView characterimg= null;
    AchievementArrayAdapter adapter = null;
    ArrayList<Achievement> arrayListAchievements;
    ImageView monsterimg = null;
    ListView listView;
    DbModel dbModel;
   View monster = null;

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

    private void getdbdata(){
        final DbModel model = new DbModel(getContext());
        if (!model.checkIfTableEmpty()) {
            User user = model.readUserFromDb();
            totalSteps = user.getTotalSteps();
            dailySteps = user.getDailySteps();
            dbdistance = user.getTotalDistance();
            dbjogdate = user.getWalkDate();
            dbwalktime = user.getWalkTime();
            dbdailydistance = user.getDailyDistance();
            textView = getView().findViewById(R.id.steps);
            String steps = String.valueOf(totalSteps);
            String dSteps = String.valueOf(dailySteps);
            textView.setText("Total steps: " + steps + "\nDaily steps: " + dSteps);
            dbdistance = dbdistance + totalSteps*1;
            String formattedValue = String.format("%.2f", dbdistance);

            //jogdata.setText("Distance: " + formattedValue +"\nDaily distance: "+ dbdailydistance +"\nTotal jog time: " + dbwalktime + "\nlast jog was on: "+ dbjogdate);
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

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        float dpWidth = displayMetrics.widthPixels;
        percentagedistance = dpWidth / distancerange;
        travelleddistance = totalSteps;


        distancem = travelleddistance % 5000;
        distancem = dpWidth-(distancem * percentagedistance);
        //gift.setX(distancem);
        characterdistancetxt.setText(Float.toString(travelleddistance)+ "m");
        //giftimg.setX(distancem-dpWidth/16);
        wifianimation.start();
        monsterAnimation.start();
        characterWalkAnimation.start();

        float monsterTravelledDistance = 2500;
        float monsterDistance = monsterTravelledDistance;
        monsterDistance =   monsterTravelledDistance * percentagedistance;
       characterdistancetxt.setText(Float.toString(monsterDistance));
       if(travelleddistance-monsterTravelledDistance<5000){
          // monster.setX(monsterDistance);
           //monsterimg.setX(monsterDistance-dpWidth/10);
       }

        if (distancem <= (dpWidth / 2)) {
            distancem = dpWidth;
           // gift.setX(distancem);
        }
    }



}
