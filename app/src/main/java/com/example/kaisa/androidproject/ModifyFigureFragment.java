package com.example.kaisa.androidproject;

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
import android.widget.Toast;

import java.util.ArrayList;

public class ModifyFigureFragment extends Fragment implements View.OnClickListener {

    ArrayList<Integer> maleHeadList = new ArrayList<Integer>();
    ArrayList<Integer> maleTorsoList = new ArrayList<Integer>();
    ArrayList<Integer> maleLegList = new ArrayList<Integer>();
    ArrayList<Integer> maleFeetList = new ArrayList<Integer>();
    ArrayList<Integer> femaleHeadList = new ArrayList<Integer>();
    ArrayList<Integer> femaleTorsoList = new ArrayList<Integer>();
    ArrayList<Integer> femaleLegList = new ArrayList<Integer>();
    ArrayList<Integer> femaleFeetList = new ArrayList<Integer>();
    int position = 0;
    String gender;
    int listMinValue = 0;
    ImageView imageview_head, imageview_torso, imageview_legs,imageview_feet;
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
        imageview_head = getView().findViewById(R.id.imageview_head);
        addToMaleHeadList();
        imageview_torso = getView().findViewById(R.id.imageview_torso);
        addToMaleTorsoList();
        imageview_legs = getView().findViewById(R.id.imageview_legs);
        addToMaleLegList();
        imageview_feet = getView().findViewById(R.id.imageview_feet);
        addToMaleFeetList();
        addToFemaleHeadList();
        addToFemaleTorsoList();
        addToFemaleLegList();
        addToFemaleFeetList();
        setMaleCharacter();
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

        ImageButton button_feets_to_left = getView().findViewById(R.id.button_feet_to_left);
        button_feets_to_left.setOnClickListener(this);

        ImageButton button_feets_to_right = getView().findViewById(R.id.button_feet_to_right);
        button_feets_to_right.setOnClickListener(this);

        Button femaleButton = getView().findViewById(R.id.button_female);
        femaleButton.setOnClickListener(this);

        Button maleButton = getView().findViewById(R.id.button_male);
        maleButton.setOnClickListener(this);

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

        if(buttonID == R.id.button_female){
            setFemaleCharacter();
        }
        if(buttonID == R.id.button_male){
            setMaleCharacter();
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

        if (gender == "male") {                                              //IF CHARACTER IS MALE
            switch (v.getId()){
                case R.id.button_head_to_left:                              //head left
                    if (position <= listMinValue) {
                        position = maleHeadList.size() - 1;
                    } else {
                        position--;
                    }
                    setMaleHeadImage();
                    break;

                case R.id.button_head_to_right:                            //head right
                    if (position >= maleHeadList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setMaleHeadImage();
                    break;

                case R.id.button_torso_to_left:                         //torso left
                    if (position <= listMinValue) {
                        position = maleHeadList.size() -1;
                    } else {
                        position--;
                    }
                    setMaleTorsoImage();
                    break;

                case R.id.button_torso_to_right:                        //torso right
                    if (position >= maleTorsoList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setMaleTorsoImage();
                    break;

                case R.id.button_legs_to_left:                          //legs left
                    if (position <= listMinValue) {
                        position = maleLegList.size()-1;
                    } else {
                        position--;
                    }
                    setMaleLegImage();
                    break;

                case R.id.button_legs_to_right:                         //legs right
                    if (position >= maleLegList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setMaleLegImage();
                    break;

                case R.id.button_feet_to_left:                          //feet left
                    if (position <= listMinValue) {
                        position = maleFeetList.size()-1;
                    } else {
                        position--;
                    }
                    setMaleFeetImage();
                    break;

                case R.id.button_feet_to_right:                         //feet right
                    if (position >= maleFeetList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setMaleFeetImage();
                    break;
            }
        }
        if(gender == "female"){                                       //IF CHARACTER IS FEMALE
            switch (v.getId()){
                case R.id.button_head_to_left:                              //head left
                    if (position <= listMinValue) {
                        position = femaleHeadList.size() - 1;
                    } else {
                        position--;
                    }
                    setFemaleHeadImage();
                    break;

                case R.id.button_head_to_right:                            //head right
                    if (position >= femaleHeadList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setFemaleHeadImage();
                    break;

                case R.id.button_torso_to_left:                         //torso left
                    if (position <= listMinValue) {
                        position = femaleHeadList.size() -1;
                    } else {
                        position--;
                    }
                    setFemaleTorsoImage();
                    break;

                case R.id.button_torso_to_right:                        //torso right
                    if (position >= femaleTorsoList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setFemaleTorsoImage();
                    break;

                case R.id.button_legs_to_left:                          //legs left
                    if (position <= listMinValue) {
                        position = femaleLegList.size()-1;
                    } else {
                        position--;
                    }
                    setFemaleLegImage();
                    break;

                case R.id.button_legs_to_right:                         //legs right
                    if (position >= femaleLegList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setFemaleLegImage();
                    break;

                case R.id.button_feet_to_left:                          //feet left
                    if (position <= listMinValue) {
                        position = femaleFeetList.size()-1;
                    } else {
                        position--;
                    }
                    setFemaleFeetImage();
                    break;

                case R.id.button_feet_to_right:                         //feet right
                    if (position >= femaleFeetList.size() - 1) {
                        position = listMinValue;
                    } else {
                        position++;
                    }
                    setFemaleFeetImage();
                    break;
            }
        }
    }



    public void setMaleHeadImage(){imageview_head.setImageResource(maleHeadList.get(position));}
    public void setMaleTorsoImage(){ imageview_torso.setImageResource(maleTorsoList.get(position));}
    public void setMaleLegImage(){ imageview_legs.setImageResource(maleLegList.get(position));}
    public void setMaleFeetImage(){imageview_feet.setImageResource(maleFeetList.get(position));}
    public void setFemaleHeadImage(){imageview_head.setImageResource(femaleHeadList.get(position));}
    public void setFemaleTorsoImage(){ imageview_torso.setImageResource(femaleTorsoList.get(position));}
    public void setFemaleLegImage(){ imageview_legs.setImageResource(femaleLegList.get(position));}
    public void setFemaleFeetImage(){imageview_feet.setImageResource(femaleFeetList.get(position));}

    public void createNewFigure() {
        context.viewPager.disableScroll(false);
        context.navigation.setVisibility(View.VISIBLE);
        context.imageButton.setVisibility(View.VISIBLE);
        context.viewPager.setCurrentItem(0);
        context.databaseEmpty = false;
        Toast.makeText(getActivity(), "New figure created!", Toast.LENGTH_SHORT).show();
    }

    public void addToMaleHeadList(){
        maleHeadList.add(R.drawable.ukko_paa_0);
        maleHeadList.add(R.drawable.ukko_paa_1);
    }

    public void addToMaleTorsoList(){
        maleTorsoList.add(R.drawable.ukko_torso_0);
        maleTorsoList.add(R.drawable.ukko_torso_1);
    }

    public void addToMaleLegList(){
        maleLegList.add(R.drawable.ukko_pants_0);
        maleLegList.add(R.drawable.ukko_pants_1);
    }

    public void addToMaleFeetList(){
        maleFeetList.add(R.drawable.ukko_shoes_0);
        maleFeetList.add(R.drawable.ukko_shoes_1);
    }
    public void setFemaleCharacter(){
        gender = "female";
        imageview_head.setImageResource(R.drawable.akka_paa_0);
        imageview_torso.setImageResource(R.drawable.akka_torso_0);
        imageview_legs.setImageResource(R.drawable.akka_pants_0);
        imageview_feet.setImageResource(R.drawable.akka_shoes_0);
    }
    public void setMaleCharacter(){
        gender = "male";
        imageview_head.setImageResource(R.drawable.ukko_paa_0);
        imageview_torso.setImageResource(R.drawable.ukko_torso_0);
        imageview_legs.setImageResource(R.drawable.ukko_pants_0);
        imageview_feet.setImageResource(R.drawable.ukko_shoes_0);
    }
    public void addToFemaleHeadList(){
        femaleHeadList.add(R.drawable.akka_paa_0);
        femaleHeadList.add(R.drawable.akka_paa_1);
    }
    public void addToFemaleTorsoList(){
        femaleTorsoList.add(R.drawable.akka_torso_0);
        femaleTorsoList.add(R.drawable.akka_torso_1);
    }

    public void addToFemaleLegList(){
        femaleLegList.add(R.drawable.akka_pants_0);
        femaleLegList.add(R.drawable.akka_pants_1);
    }

    public void addToFemaleFeetList(){
        femaleFeetList.add(R.drawable.akka_shoes_0);
        femaleFeetList.add(R.drawable.akka_shoes_1);
    }

}
