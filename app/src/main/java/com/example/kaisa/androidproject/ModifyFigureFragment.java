package com.example.kaisa.androidproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

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
    int headPosition = 0;
    int torsoPosition = 0;
    int legPosition = 0;
    int feetPosition = 0;
    int gender = 0;
    int listMinValue = 0;
    String name;
    ImageView imageview_head, imageview_torso, imageview_legs,imageview_feet;
    MainActivity context;
    EditText nameEditText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MainActivity) container.getContext();
        return inflater.inflate(R.layout.fragment_modify_figure, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEditText = getView().findViewById(R.id.name_edit_text);
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/smallest_pixel-7.ttf");
        nameEditText.setTypeface(custom_font);
        nameEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        imageview_head = getView().findViewById(R.id.imageview_head);
        imageview_torso = getView().findViewById(R.id.imageview_torso);
        imageview_legs = getView().findViewById(R.id.imageview_legs);
        imageview_feet = getView().findViewById(R.id.imageview_feet);
        addToMaleHeadList(0);
        addToMaleTorsoList(0);
        addToMaleLegList(0);
        addToMaleFeetList(0);
        addToFemaleHeadList(0);
        addToFemaleTorsoList(0);
        addToFemaleLegList(0);
        addToFemaleFeetList(0);
        addToMaleHeadList(1);
        addToMaleTorsoList(1);
        addToMaleLegList(1);
        addToMaleFeetList(1);
        addToFemaleHeadList(1);
        addToFemaleTorsoList(1);
        addToFemaleLegList(1);
        addToFemaleFeetList(1);
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

        readClothesFromDatabase();
        nameEditText.setText(name);
        if(gender == 0){
            setMaleHeadImage();
            setMaleTorsoImage();
            setMaleLegImage();
            setMaleFeetImage();
        }
        else {
            setFemaleHeadImage();
            setFemaleTorsoImage();
            setFemaleLegImage();
            setFemaleFeetImage();
        }

        Bundle bundle = this.getArguments();

        if(bundle != null){
            int clothes = bundle.getInt("clothes");
            int rand = bundle.getInt("randClothes");
            switch (clothes) {
                case 0:

            }
        }

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
                saveClothesToDatabase();
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                //Tietojentallennus tietokantaan
            }

        }

        if (gender == 0) {                                              //IF CHARACTER IS MALE
            switch (v.getId()){
                case R.id.button_head_to_left:                              //head left
                    if (headPosition <= listMinValue) {
                        headPosition = maleHeadList.size() - 1;
                    } else {
                        headPosition--;
                    }
                    setMaleHeadImage();
                    break;

                case R.id.button_head_to_right:                            //head right
                    if (headPosition >= maleHeadList.size() - 1) {
                        headPosition = listMinValue;
                    } else {
                        headPosition++;
                    }
                    setMaleHeadImage();
                    break;

                case R.id.button_torso_to_left:                         //torso left
                    if (torsoPosition <= listMinValue) {
                        torsoPosition = maleTorsoList.size() -1;
                    } else {
                        torsoPosition--;
                    }
                    setMaleTorsoImage();
                    break;

                case R.id.button_torso_to_right:                        //torso right
                    if (torsoPosition >= maleTorsoList.size() - 1) {
                        torsoPosition = listMinValue;
                    } else {
                        torsoPosition++;
                    }
                    setMaleTorsoImage();
                    break;

                case R.id.button_legs_to_left:                          //legs left
                    if (legPosition <= listMinValue) {
                        legPosition = maleLegList.size()-1;
                    } else {
                        legPosition--;
                    }
                    setMaleLegImage();
                    break;

                case R.id.button_legs_to_right:                         //legs right
                    if (legPosition >= maleLegList.size() - 1) {
                        legPosition = listMinValue;
                    } else {
                        legPosition++;
                    }
                    setMaleLegImage();
                    break;

                case R.id.button_feet_to_left:                          //feet left
                    if (feetPosition <= listMinValue) {
                        feetPosition = maleFeetList.size()-1;
                    } else {
                        feetPosition--;
                    }
                    setMaleFeetImage();
                    break;

                case R.id.button_feet_to_right:                         //feet right
                    if (feetPosition >= maleFeetList.size() - 1) {
                        feetPosition = listMinValue;
                    } else {
                        feetPosition++;
                    }
                    setMaleFeetImage();
                    break;
            }
        }
        if(gender == 1){                                       //IF CHARACTER IS FEMALE
            switch (v.getId()){
                case R.id.button_head_to_left:                              //head left
                    if (headPosition <= listMinValue) {
                        headPosition = femaleHeadList.size() - 1;
                    } else {
                        headPosition--;
                    }
                    setFemaleHeadImage();
                    break;

                case R.id.button_head_to_right:                            //head right
                    if (headPosition >= femaleHeadList.size() - 1) {
                        headPosition = listMinValue;
                    } else {
                        headPosition++;
                    }
                    setFemaleHeadImage();
                    break;

                case R.id.button_torso_to_left:                         //torso left
                    if (torsoPosition <= listMinValue) {
                        torsoPosition = femaleTorsoList.size() -1;
                    } else {
                        torsoPosition--;
                    }
                    setFemaleTorsoImage();
                    break;

                case R.id.button_torso_to_right:                        //torso right
                    if (torsoPosition >= femaleTorsoList.size() - 1) {
                        torsoPosition = listMinValue;
                    } else {
                        torsoPosition++;
                    }
                    setFemaleTorsoImage();
                    break;

                case R.id.button_legs_to_left:                          //legs left
                    if (legPosition <= listMinValue) {
                        legPosition = femaleLegList.size()-1;
                    } else {
                        legPosition--;
                    }
                    setFemaleLegImage();
                    break;

                case R.id.button_legs_to_right:                         //legs right
                    if (legPosition >= femaleLegList.size() - 1) {
                        legPosition = listMinValue;
                    } else {
                        legPosition++;
                    }
                    setFemaleLegImage();
                    break;

                case R.id.button_feet_to_left:                          //feet left
                    if (feetPosition <= listMinValue) {
                        feetPosition = femaleFeetList.size()-1;
                    } else {
                        feetPosition--;
                    }
                    setFemaleFeetImage();
                    break;

                case R.id.button_feet_to_right:                         //feet right
                    if (feetPosition >= femaleFeetList.size() - 1) {
                        feetPosition = listMinValue;
                    } else {
                        feetPosition++;
                    }
                    setFemaleFeetImage();
                    break;
            }
        }
    }

    public void setMaleHeadImage(){imageview_head.setImageResource(maleHeadList.get(headPosition));}
    public void setMaleTorsoImage(){ imageview_torso.setImageResource(maleTorsoList.get(torsoPosition));}
    public void setMaleLegImage(){ imageview_legs.setImageResource(maleLegList.get(legPosition));}
    public void setMaleFeetImage(){imageview_feet.setImageResource(maleFeetList.get(feetPosition));}
    public void setFemaleHeadImage(){imageview_head.setImageResource(femaleHeadList.get(headPosition));}
    public void setFemaleTorsoImage(){ imageview_torso.setImageResource(femaleTorsoList.get(torsoPosition));}
    public void setFemaleLegImage(){ imageview_legs.setImageResource(femaleLegList.get(legPosition));}
    public void setFemaleFeetImage(){imageview_feet.setImageResource(femaleFeetList.get(feetPosition));}

    public void createNewFigure() {
        context.viewPager.disableScroll(false);
        context.navigation.setVisibility(View.VISIBLE);
        context.imageButton.setVisibility(View.VISIBLE);
        context.viewPager.setCurrentItem(0);
        saveClothesToDatabase();
        context.databaseEmpty = false;
        Toast.makeText(getActivity(), "New figure created!", Toast.LENGTH_SHORT).show();
    }

    public void addToMaleHeadList(int index){
        String uri = "drawable/ukko_paa_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        maleHeadList.add(imageResource);
    }

    public void addToMaleTorsoList(int index){
        String uri = "drawable/ukko_torso_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        maleTorsoList.add(imageResource);
    }

    public void addToMaleLegList(int index){
        String uri = "drawable/ukko_pants_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        maleLegList.add(imageResource);
    }

    public void addToMaleFeetList(int index){
        String uri = "drawable/ukko_shoes_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        maleFeetList.add(imageResource);
    }
    public void setFemaleCharacter(){
        gender = 1;
        headPosition = 0;
        torsoPosition = 0;
        legPosition = 0;
        feetPosition = 0;
        imageview_head.setImageResource(R.drawable.akka_paa_0);
        imageview_torso.setImageResource(R.drawable.akka_torso_0);
        imageview_legs.setImageResource(R.drawable.akka_pants_0);
        imageview_feet.setImageResource(R.drawable.akka_shoes_0);
    }
    public void setMaleCharacter(){
        gender = 0;
        headPosition = 0;
        torsoPosition = 0;
        legPosition = 0;
        feetPosition = 0;
        imageview_head.setImageResource(R.drawable.ukko_paa_0);
        imageview_torso.setImageResource(R.drawable.ukko_torso_0);
        imageview_legs.setImageResource(R.drawable.ukko_pants_0);
        imageview_feet.setImageResource(R.drawable.ukko_shoes_0);
    }
    public void addToFemaleHeadList(int index){
        String uri = "drawable/akka_paa_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        femaleHeadList.add(imageResource);
    }
    public void addToFemaleTorsoList(int index){
        String uri = "drawable/akka_torso_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        femaleTorsoList.add(imageResource);
    }

    public void addToFemaleLegList(int index){
        String uri = "drawable/akka_pants_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        femaleLegList.add(imageResource);
    }

    public void addToFemaleFeetList(int index){
        String uri = "drawable/akka_shoes_" + index;
        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        femaleFeetList.add(imageResource);
    }

    public void saveClothesToDatabase() {
        DbModel model = new DbModel(getContext());
        name = nameEditText.getText().toString();
        if(model.checkIfTableEmpty()) {
            User user = new User(name, gender, headPosition + 1, torsoPosition + 1, legPosition + 1, feetPosition + 1, 1, 0, 0, 0, 0,0.0, 0.0, 0.0, "", "", 0, 0, 0);
            model.addUserToDb(user);
        }
        else {
            User user = model.readUserFromDb();
            user.setName(name);
            user.setGender(gender);
            user.setHat(headPosition + 1);
            user.setShirt(torsoPosition + 1);
            user.setPants(legPosition + 1);
            user.setShoes(feetPosition + 1);
            model.updateUser(user);
        }
    }

    public void readClothesFromDatabase() {
        DbModel model = new DbModel(getContext());
        if (!model.checkIfTableEmpty()){
            User user = model.readUserFromDb();
            name = user.getName();
            gender = user.getGender();
            headPosition = (user.getHat() - 1);
            torsoPosition = (user.getShirt() - 1);
            legPosition = (user.getPants() - 1);
            feetPosition = (user.getShoes() - 1);
        }
        else {
            name = "";
            gender = 0;
            headPosition = 0;
            torsoPosition = 0;
            legPosition = 0;
            feetPosition = 0;
        }
    }
}