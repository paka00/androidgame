package com.example.kaisa.androidproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView dailyTask = null;
    TextView dailyTaskTime = null;
    TextView dailyTaskProgress = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("StepCounter"));
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dailyStepsCheck();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dailyStepsCheck();
        }
    };

    protected void dailyStepsCheck() {
        DbModel model = new DbModel(getContext());
        User user = model.readUserFromDb();
        int dailySteps = user.getDailySteps();
        dailyTaskProgress = getView().findViewById(R.id.daily_task_progress);
        if (dailySteps < 1000) {
            double percentage = (double)dailySteps / 1000.0 * 100.0;
            DecimalFormat df = new DecimalFormat("####0.0");
            dailyTaskProgress.setText("Current progress: " + df.format(percentage) + " %");
        }
        else {
            dailyTaskProgress.setText("Task done!");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
