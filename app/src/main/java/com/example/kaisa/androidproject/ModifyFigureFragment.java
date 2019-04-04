package com.example.kaisa.androidproject;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class ModifyFigureFragment extends Fragment implements View.OnClickListener {

    ArrayList<Integer> maleHeadList = new ArrayList<Integer>();
    int position = 0;
    int ListMinValue = 0;
    ImageView imageview_maleHead, imageview_maleTorso, imageview_maleLegs, imageview_maleFeet;
    MainActivity context;
    public Button buttonHat, buttonMale, buttonFemale, buttonCancel, doneButton;
    public RadioButton maleButton, femaleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MainActivity) container.getContext();
        return inflater.inflate(R.layout.fragment_modify_figure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageview_maleHead = getView().findViewById(R.id.imageview_male_head);
        imageview_maleTorso = getView().findViewById(R.id.imageview_male_torso);
        imageview_maleLegs = getView().findViewById(R.id.imageview_male_legs);
        imageview_maleFeet = getView().findViewById(R.id.imageview_male_feet);
        maleHeadList.add(R.drawable.mies_paa_0);
        maleHeadList.add(R.drawable.mies_paa_1);

        imageview_maleHead.setImageResource(maleHeadList.get(position));

        ImageButton button_head_to_left = getView().findViewById(R.id.button_head_to_left);
        button_head_to_left.setOnClickListener(this);

        ImageButton button_head_to_right = getView().findViewById(R.id.button_head_to_right);
        button_head_to_right.setOnClickListener(this);

        Button doneButton = getView().findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);


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
        doneButton = getView().findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
        maleButton = getView().findViewById(R.id.radio_button_man);
        femaleButton = getView().findViewById(R.id.radio_button_woman);
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        if (buttonID == R.id.button_head_to_left) {
            if (position <= ListMinValue) {
                position = maleHeadList.size() - 1;
            } else {
                position--;
            }
            setImage();
        }
        if (buttonID == R.id.button_head_to_right) {
            if (position >= maleHeadList.size() - 1) {
                position = ListMinValue;
            } else {
                position++;
            }
            setImage();
        }

        if (buttonID == R.id.done_button) {
            if (context.databaseEmpty) {
                //If database is empty
                createNewFigure();
            } else {
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                //Tietojentallennus tietokantaan
            }
        }
    }


        public void setImage () {
            imageview_maleHead.setImageResource(maleHeadList.get(position));
        }


        public void createNewFigure () {
            context.viewPager.disableScroll(false);
            context.navigation.setVisibility(View.VISIBLE);
            context.imageButton.setVisibility(View.VISIBLE);
            context.viewPager.setCurrentItem(0);
            context.databaseEmpty = false;
            Toast.makeText(getActivity(), "New figure created!", Toast.LENGTH_SHORT).show();
        }


/*
        public void changeGender() {
            maleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageview_maleHead.setImageResource(R.drawable.mies_paa_0);
                    imageview_maleTorso.setImageResource(R.drawable.mies_torso_0);
                    imageview_maleLegs.setImageResource(R.drawable.mies_alaosa_0);
                    imageview_maleFeet.setImageResource(R.drawable.mies_jalat_0);
                }
            });

            femaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageview_maleHead.setImageResource(R.drawable.nainen_paa_0);
                    imageview_maleTorso.setImageResource(R.drawable.nainen_torso_0);
                    imageview_maleLegs.setImageResource(R.drawable.nainen_alaosa_0);
                    imageview_maleFeet.setImageResource(R.drawable.nainen_jalat_0);
                }
            });
        }*/
}
