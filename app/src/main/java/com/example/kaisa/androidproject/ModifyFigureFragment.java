package com.example.kaisa.androidproject;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

import java.net.URI;
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
    ImageView imageview_head, imageview_torso, imageview_legs,imageview_feet, imageView_rock;
    MainActivity context;
    EditText nameEditText = null;
    boolean isUserCreated = false;
    ConstraintLayout bg = null;
    ImageButton button_head_to_left, button_head_to_right, button_torso_to_left, button_torso_to_right, button_legs_to_left, button_legs_to_right, button_feet_to_left, button_feet_to_right;
    Button femaleButton,maleButton;

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
        nameEditText.setCursorVisible(false);
        bg = getView().findViewById(R.id.modify_figure_fragment);
        Glide.with(this).load(R.drawable.vesitausta).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bg.setBackground(resource);
                }
            }
        });
        imageView_rock = getView().findViewById(R.id.rock_image);
        Glide.with(this).load(R.drawable.kivi).into(imageView_rock);
        imageview_head = getView().findViewById(R.id.imageview_head);
        imageview_torso = getView().findViewById(R.id.imageview_torso);
        imageview_legs = getView().findViewById(R.id.imageview_legs);
        imageview_feet = getView().findViewById(R.id.imageview_feet);
        isUserCreated = false;
        DbModel model = new DbModel(getContext());
        if (model.checkIfUserTableEmpty()){
            model.addHat(1, 0);
            model.addShirt(1, 0);
            model.addPants(1, 0);
            model.addShoes(1, 0);
            model.addHat(1, 1);
            model.addShirt(1, 1);
            model.addPants(1, 1);
            model.addShoes(1, 1);

        } else {
            isUserCreated = true;
        }
        readUnlockedClothes();
        loadImages(maleHeadList);
        loadImages(maleTorsoList);
        loadImages(maleLegList);
        loadImages(maleFeetList);
        loadImages(femaleHeadList);
        loadImages(femaleTorsoList);
        loadImages(femaleLegList);
        loadImages(femaleFeetList);
        Log.d("modifyfigure", "male head array size: " + maleHeadList.size());
        Log.d("modifyfigure", "male head array: " + maleHeadList);
        setMaleCharacter();
        button_head_to_left = getView().findViewById(R.id.button_head_to_left);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_head_to_left);
        button_head_to_left.setOnClickListener(this);

        button_head_to_right = getView().findViewById(R.id.button_head_to_right);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_head_to_right);
        button_head_to_right.setOnClickListener(this);

        button_torso_to_left = getView().findViewById(R.id.button_torso_to_left);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_torso_to_left);
        button_torso_to_left.setOnClickListener(this);

        button_torso_to_right = getView().findViewById(R.id.button_torso_to_right);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_torso_to_right);
        button_torso_to_right.setOnClickListener(this);

        button_legs_to_left = getView().findViewById(R.id.button_legs_to_left);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_legs_to_left);
        button_legs_to_left.setOnClickListener(this);

        button_legs_to_right = getView().findViewById(R.id.button_legs_to_right);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_legs_to_right);
        button_legs_to_right.setOnClickListener(this);

        button_feet_to_left = getView().findViewById(R.id.button_feet_to_left);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_feet_to_left);
        button_feet_to_left.setOnClickListener(this);

        button_feet_to_right = getView().findViewById(R.id.button_feet_to_right);
        Glide.with(this).load(R.drawable.nuoli_uusi).into(button_feet_to_right);
        button_feet_to_right.setOnClickListener(this);

        femaleButton = getView().findViewById(R.id.button_female);
        femaleButton.setOnClickListener(this);

        maleButton = getView().findViewById(R.id.button_male);
        maleButton.setOnClickListener(this);

        ImageButton doneButton = getView().findViewById(R.id.done_button);
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
            int clothes = bundle.getInt("clothesType");
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

        if(model.checkIfUserTableEmpty()){
            button_head_to_left.setVisibility(View.INVISIBLE);
            button_head_to_right.setVisibility(View.INVISIBLE);
            button_torso_to_left.setVisibility(View.INVISIBLE);
            button_torso_to_right.setVisibility(View.INVISIBLE);
            button_legs_to_left.setVisibility(View.INVISIBLE);
            button_legs_to_right.setVisibility(View.INVISIBLE);
            button_feet_to_left.setVisibility(View.INVISIBLE);
            button_feet_to_right.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isUserCreated) {
            try {
                readUnlockedClothes();
            } catch (NullPointerException e) {
                Log.d("modifyfigure", e.toString());
            }
            Log.d("modifyfigure", "setuservisiblehint");

        }
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
                isUserCreated = true;
                maleButton.setVisibility(View.GONE);
                femaleButton.setVisibility(View.GONE);
            } else {
                saveClothesToDatabase();
                maleButton.setVisibility(View.GONE);
                femaleButton.setVisibility(View.GONE);
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

    /*public void setMaleHeadImage(){ Glide.with(this).load(maleHeadList.get(headPosition)).into(imageview_head);}
    public void setMaleTorsoImage(){ Glide.with(this).load(maleTorsoList.get(torsoPosition)).into(imageview_torso);}
    public void setMaleLegImage(){ Glide.with(this).load(maleLegList.get(legPosition)).into(imageview_legs);}
    public void setMaleFeetImage(){ Glide.with(this).load(maleFeetList.get(feetPosition)).into(imageview_feet);}
    public void setFemaleHeadImage(){ Glide.with(this).load(femaleHeadList.get(headPosition)).into(imageview_head);}
    public void setFemaleTorsoImage(){ Glide.with(this).load(femaleTorsoList.get(torsoPosition)).into(imageview_torso);}
    public void setFemaleLegImage(){ Glide.with(this).load(femaleLegList.get(legPosition)).into(imageview_legs); }
    public void setFemaleFeetImage(){ Glide.with(this).load(femaleFeetList.get(feetPosition)).into(imageview_feet);}*/

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
        button_head_to_left.setVisibility(View.VISIBLE);
        button_head_to_right.setVisibility(View.VISIBLE);
        button_torso_to_left.setVisibility(View.VISIBLE);
        button_torso_to_right.setVisibility(View.VISIBLE);
        button_legs_to_left.setVisibility(View.VISIBLE);
        button_legs_to_right.setVisibility(View.VISIBLE);
        button_feet_to_left.setVisibility(View.VISIBLE);
        button_feet_to_right.setVisibility(View.VISIBLE);
        maleButton.setVisibility(View.VISIBLE);
        femaleButton.setVisibility(View.VISIBLE);

        context.viewPager.setCurrentItem(0);
        saveClothesToDatabase();
        context.databaseEmpty = false;
        Toast.makeText(getActivity(), "New figure created!", Toast.LENGTH_SHORT).show();
    }

    public void addToMaleHeadList(ArrayList<Integer> list){
        maleHeadList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_paa_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleHeadList.add(imageResource);
        }
    }

    public void addToMaleTorsoList(ArrayList<Integer> list){
        maleTorsoList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_torso_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleTorsoList.add(imageResource);
        }
    }

    public void addToMaleLegList(ArrayList<Integer> list){
        maleLegList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_pants_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleLegList.add(imageResource);
        }
    }

    public void addToMaleFeetList(ArrayList<Integer> list){
        maleFeetList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_shoes_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleFeetList.add(imageResource);
        }
    }
    public void setFemaleCharacter(){
        gender = 1;
        headPosition = 0;
        torsoPosition = 0;
        legPosition = 0;
        feetPosition = 0;
        /*Glide.with(this).load(R.drawable.akka_paa_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_head);
        Glide.with(this).load(R.drawable.akka_torso_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_torso);
        Glide.with(this).load(R.drawable.akka_pants_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_legs);
        Glide.with(this).load(R.drawable.akka_shoes_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_feet);*/
        setFemaleFeetImage();
        setFemaleLegImage();
        setFemaleTorsoImage();
        setFemaleHeadImage();
    }
    public void setMaleCharacter(){
        gender = 0;
        headPosition = 0;
        torsoPosition = 0;
        legPosition = 0;
        feetPosition = 0;
        /*Glide.with(this).load(R.drawable.ukko_paa_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_head);
        Glide.with(this).load(R.drawable.ukko_torso_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_torso);
        Glide.with(this).load(R.drawable.ukko_pants_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_legs);
        Glide.with(this).load(R.drawable.ukko_shoes_1).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageview_feet);*/
        setMaleFeetImage();
        setMaleLegImage();
        setMaleTorsoImage();
        setMaleHeadImage();
    }
    public void addToFemaleHeadList(ArrayList<Integer> list){
        femaleHeadList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_paa_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleHeadList.add(imageResource);
        }
    }
    public void addToFemaleTorsoList(ArrayList<Integer> list){
        femaleTorsoList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_torso_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleTorsoList.add(imageResource);
        }
    }

    public void addToFemaleLegList(ArrayList<Integer> list){
        femaleLegList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_pants_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleLegList.add(imageResource);
        }
    }

    public void addToFemaleFeetList(ArrayList<Integer> list){
        femaleFeetList.clear();
        for(int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_shoes_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleFeetList.add(imageResource);
        }
    }

    public void saveClothesToDatabase() {
        DbModel model = new DbModel(getContext());
        name = nameEditText.getText().toString();
        if(model.checkIfUserTableEmpty()) {
            User user = new User(name, gender, headPosition, torsoPosition, legPosition, feetPosition, 1, 0, 0, 0, 0,0.0, 0.0, 0.0, "", "", 0, 0, 0);
            model.addUserToDb(user);
        }
        else {
            User user = model.readUserFromDb();
            user.setName(name);
            user.setGender(gender);
            user.setHat(headPosition);
            user.setShirt(torsoPosition);
            user.setPants(legPosition);
            user.setShoes(feetPosition);
            model.updateUser(user);
        }
    }

    public void readClothesFromDatabase() {
        DbModel model = new DbModel(getContext());
        if (!model.checkIfUserTableEmpty()){
            User user = model.readUserFromDb();
            name = user.getName();
            gender = user.getGender();
            headPosition = (user.getHat());
            torsoPosition = (user.getShirt());
            legPosition = (user.getPants());
            feetPosition = (user.getShoes());
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

    public void readUnlockedClothes() {
        DbModel model = new DbModel(getContext());
        addToMaleHeadList(model.readHats(0));
        addToMaleTorsoList(model.readShirts(0));
        addToMaleLegList(model.readPants(0));
        addToMaleFeetList(model.readShoes(0));
        addToFemaleHeadList(model.readHats(1));
        addToFemaleTorsoList(model.readShirts(1));
        addToFemaleLegList(model.readPants(1));
        addToFemaleFeetList(model.readShoes(1));
    }

    public void loadImages(ArrayList<Integer> imageList) {
        for (int i = 0; i < imageList.size(); i++) {
            /*RequestOptions requestOptions = RequestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(this)
                    .asBitmap()
                    .load(imageList.get(i))
                    .apply(requestOptions)
                    .submit();*/
        }
    }
}