package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

public class AchievementsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView textView = null;
    Button boxButton = null;
    View character = null;
    float distancem = 0;
    static final float distancerange = 10000;
    float percentagedistance = 0;
    TextView characterdistancetxt = null;
    float travelleddistance = 0;
    ImageView giftimg = null;
    int dailySteps = 0;
    int totalSteps = 0;
    int memorysteps = 0;
    float dbdistance = 0;
    float dbwalktime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("StepCounter"));
        return inflater.inflate(R.layout.fragment_achievements, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boxButton = getView().findViewById(R.id.moveButton);
        character = getView().findViewById(R.id.box);
        giftimg = getView().findViewById(R.id.gift);
        characterdistancetxt = getView().findViewById(R.id.distancecharacter);
        boxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedistance();
            }
        });

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

        final DbModel model = new DbModel(getContext());
        if (!model.checkIfTableEmpty()) {
            User user = model.readUserFromDb();
            totalSteps = user.getTotalSteps();
            dailySteps = user.getDailySteps();

            textView = getView().findViewById(R.id.steps);
            String steps = String.valueOf(totalSteps);
            String dSteps = String.valueOf(dailySteps);
            textView.setText("Total steps: " + steps + "\nDaily steps: " + dSteps);
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
        updatedistance();

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
        boxButton.setText(Float.toString(distancem));
        distancem = distancem * percentagedistance;
        // steps = steps +(percentagedistance*50);

        character.setX(distancem);
        characterdistancetxt.setText(Float.toString(travelleddistance)+ "m");
        giftimg.setX(distancem-dpWidth/18);

        if (distancem >= (dpWidth / 2)) {
            distancem = -dpWidth/1000;
            character.setX(distancem);
        }



    }


}
