package com.example.kaisa.androidproject;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ModifyFigureFragment extends Fragment implements View.OnClickListener {

    ArrayList<Integer> maleHeadList = new ArrayList<Integer>();
    ArrayList<Integer> maleTorsoList = new ArrayList<Integer>();
    int position = 0;
    int listMinValue = 0;
    ImageView imageview_maleHead,imageview_maleTorso;
    MainActivity context;
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
        addToMaleHeadList();
        imageview_maleTorso = getView().findViewById(R.id.imageview_male_torso);
        addToMaleTorsoList();
        ImageButton button_head_to_left = getView().findViewById(R.id.button_head_to_left);
        button_head_to_left.setOnClickListener(this);

        ImageButton button_head_to_right = getView().findViewById(R.id.button_head_to_right);
        button_head_to_right.setOnClickListener(this);

        ImageButton button_torso_to_left = getView().findViewById(R.id.button_torso_to_left);
        button_torso_to_left.setOnClickListener(this);

        ImageButton button_torso_to_right = getView().findViewById(R.id.button_torso_to_right);
        button_torso_to_right.setOnClickListener(this);

        ImageButton button_legs_to_left = getView().findViewById(R.id.button_legs_to_left);
        button_legs_to_left.setOnClickListener(this);

        ImageButton button_legs_to_right = getView().findViewById(R.id.button_legs_to_right);
        button_legs_to_right.setOnClickListener(this);

        ImageButton button_shoes_to_left = getView().findViewById(R.id.button_shoes_to_left);
        button_shoes_to_left.setOnClickListener(this);

        ImageButton button_shoes_to_right = getView().findViewById(R.id.button_shoes_to_right);
        button_shoes_to_right.setOnClickListener(this);



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
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        if (buttonID == R.id.button_head_to_left) {
            if (position <= listMinValue){
                position = maleHeadList.size()-1;
            }else {
                position--;
                Toast.makeText(context, "asd", Toast.LENGTH_SHORT).show();
            }
            setMaleHeadImage();
        }
        if (buttonID == R.id.button_head_to_right) {
            if(position >= maleHeadList.size()-1){
                position = listMinValue;
            }else{
                position++;
                Toast.makeText(context, "asdasd", Toast.LENGTH_SHORT).show();
            }
            setMaleHeadImage();
        }

        if(buttonID == R.id.done_button){

            if (context.databaseEmpty) {
                //If database is empty
                createNewFigure();
            } else {
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                //Tietojentallennus tietokantaan
            }

        }
        if(buttonID == R.id.button_torso_to_left){
            if(position <= maleTorsoList.size()-1){
                position = listMinValue;
            }else{
                position--;
            }
            setMaleTorsoImage();
        }
        if(buttonID == R.id.button_torso_to_right){
            if(position >= maleTorsoList.size()-1){
                position = listMinValue;
            }else{
                position++;
            }
            setMaleTorsoImage();
        }
    }


    public void setMaleHeadImage(){imageview_maleHead.setImageResource(maleHeadList.get(position));}
    public void setMaleTorsoImage(){imageview_maleTorso.setImageResource(maleTorsoList.get(position));}


    public void createNewFigure() {
        context.viewPager.disableScroll(false);
        context.navigation.setVisibility(View.VISIBLE);
        context.imageButton.setVisibility(View.VISIBLE);
        context.viewPager.setCurrentItem(0);
        context.databaseEmpty = false;
        Toast.makeText(getActivity(), "New figure created!", Toast.LENGTH_SHORT).show();
    }

    public void addToMaleHeadList(){
        imageview_maleHead.setImageResource(maleHeadList.get(position));
        maleHeadList.add(R.drawable.ukko_paa_0);
        maleHeadList.add(R.drawable.ukko_paa_1);
    }

    public void addToMaleTorsoList(){
        imageview_maleTorso.setImageResource(maleHeadList.get(position));
        maleTorsoList.add(R.drawable.ukko_torso_0);
        maleTorsoList.add(R.drawable.ukko_torso_1);
    }

}
