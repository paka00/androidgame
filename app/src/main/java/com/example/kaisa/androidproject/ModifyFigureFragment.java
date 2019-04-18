package com.example.kaisa.androidproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.kaisa.androidproject.model.Achievement;
import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
    ImageView imageview_head, imageview_torso, imageview_legs, imageview_feet, imageView_rock;
    MainActivity context;
    EditText nameEditText = null;
    boolean isUserCreated = false;
    ConstraintLayout bg = null;
    SharedPreferences prefs = null;
    Typeface pixelFont = null;
    private boolean isVisible;
    private boolean isStarted;
    AchievementsFragment achievementsFragment;
    ImageButton button_head_to_left, button_head_to_right, button_torso_to_left, button_torso_to_right, button_legs_to_left, button_legs_to_right, button_feet_to_left, button_feet_to_right;


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
        pixelFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/smallest_pixel-7.ttf");
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        nameEditText.setTypeface(pixelFont);
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
        achievementsFragment = new AchievementsFragment();
        isUserCreated = false;
        DbModel model = new DbModel(getContext());
        if (model.checkIfTableEmpty("user")) {
            model.addHat(1, 0);
            model.addShirt(1, 0);
            model.addPants(1, 0);
            model.addShoes(1, 0);
            model.addHat(1, 1);
            model.addShirt(1, 1);
            model.addPants(1, 1);
            model.addShoes(1, 1);
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
            TextView bodyText = dialoglayout.findViewById(R.id.dialog_instruction_text);
            bodyText.setTypeface(pixelFont);
            titleText.setText("Welcome to creature chase!");
            bodyText.setText("Your epic journey to outrun a terrifying creature is beginning... \n" +
                    "But first, you have to create your character! " +
                    "Choose a name and gender and press the done button when you're finished.");
            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        } else {
            isUserCreated = true;
        }
        readUnlockedClothes();
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

        Button femaleButton = getView().findViewById(R.id.button_female);
        femaleButton.setOnClickListener(this);

        Button maleButton = getView().findViewById(R.id.button_male);
        maleButton.setOnClickListener(this);

        ImageButton doneButton = getView().findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);

        readClothesFromDatabase();
        nameEditText.setText(name);
        if (gender == 0) {
            setMaleHeadImage();
            setMaleTorsoImage();
            setMaleLegImage();
            setMaleFeetImage();
        } else {
            setFemaleHeadImage();
            setFemaleTorsoImage();
            setFemaleLegImage();
            setFemaleFeetImage();
        }

        Bundle bundle = this.getArguments();

        if (bundle != null) {
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

        if (model.checkIfTableEmpty("user")) {
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
        //super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isUserCreated) {
            try {
                readUnlockedClothes();
            } catch (NullPointerException e) {
                Log.d("modifyfigure", e.toString());
            }
            Log.d("modifyfigure", "setuservisiblehint");
        }
        isVisible = isVisibleToUser;
        if (isVisible && isStarted) {
            createDialog();
        }
    }

    //Creates a dialog box with information about the current page.
    public void createDialog() {
        prefs = getContext().getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        if (!prefs.getBoolean("appHasRunBeforeModify", false)) {
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
            TextView bodyText = dialoglayout.findViewById(R.id.dialog_instruction_text);
            bodyText.setTypeface(pixelFont);
            titleText.setText("Edit your character");
            bodyText.setText("On this page you can edit your character by pressing the yellow arrows. " +
                    "You might not have many clothes right now, but you'll earn some soon enough from the daily quests and achievements. " +
                    "You should check out the achievements page by pressing the gold cup if you haven't already!");
            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            prefs.edit().putBoolean("appHasRunBeforeModify", true).apply();
            Log.d("homefragment", "firstrun");
        }
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();

        if (buttonID == R.id.button_female) {
            setFemaleCharacter();
        }
        if (buttonID == R.id.button_male) {
            setMaleCharacter();
        }

        if (buttonID == R.id.done_button) {
            if (context.databaseEmpty) {
                //If database is empty
                createNewFigure();
                isUserCreated = true;
            } else {
                saveClothesToDatabase();
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                //Tietojentallennus tietokantaan
            }

        }

        if (gender == 0) {                                              //IF CHARACTER IS MALE
            switch (v.getId()) {
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
                        torsoPosition = maleTorsoList.size() - 1;
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
                        legPosition = maleLegList.size() - 1;
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
                        feetPosition = maleFeetList.size() - 1;
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
        if (gender == 1) {                                       //IF CHARACTER IS FEMALE
            switch (v.getId()) {
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
                        torsoPosition = femaleTorsoList.size() - 1;
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
                        legPosition = femaleLegList.size() - 1;
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
                        feetPosition = femaleFeetList.size() - 1;
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

    public void setMaleHeadImage() {
        imageview_head.setImageResource(maleHeadList.get(headPosition));
    }

    public void setMaleTorsoImage() {
        imageview_torso.setImageResource(maleTorsoList.get(torsoPosition));
    }

    public void setMaleLegImage() {
        imageview_legs.setImageResource(maleLegList.get(legPosition));
    }

    public void setMaleFeetImage() {
        imageview_feet.setImageResource(maleFeetList.get(feetPosition));
    }

    public void setFemaleHeadImage() {
        imageview_head.setImageResource(femaleHeadList.get(headPosition));
    }

    public void setFemaleTorsoImage() {
        imageview_torso.setImageResource(femaleTorsoList.get(torsoPosition));
    }

    public void setFemaleLegImage() {
        imageview_legs.setImageResource(femaleLegList.get(legPosition));
    }

    public void setFemaleFeetImage() {
        imageview_feet.setImageResource(femaleFeetList.get(feetPosition));
    }

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
        context.viewPager.setCurrentItem(0);
        saveClothesToDatabase();
        createDefaultAchievements();
        context.databaseEmpty = false;
        Toast.makeText(getActivity(), "New figure created!", Toast.LENGTH_SHORT).show();
    }

    public void createDefaultAchievements(){
        DbModel model = new DbModel(getContext());
        Achievement achievement1 = new Achievement("First step", "Take your first step!", 0);
        Achievement achievement2 = new Achievement("100 steps", "Reach 100 steps", 0);
        Achievement achievement3 = new Achievement("10 kilometers", "Walk 10 kilometers", 0);
        Achievement achievement4 = new Achievement("Estonia!", "You have walked from Helsinki to Tallinn! (85km)", 0);
        Achievement achievement5 = new Achievement("Sweden!", "You have walked from Oulu to Haparanda! (133km)", 0);
        Achievement achievement6 = new Achievement("Sweden!", "You have walked from Oulu to Haparanda! (133km)", 0);
        Achievement achievement7 = new Achievement("Sweden!", "You have walked from Oulu to Haparanda! (133km)", 0);
        Achievement achievement8 = new Achievement("Sweden!", "You have walked from Oulu to Haparanda! (133km)", 0);
        model.createAchievement(achievement1);
        model.createAchievement(achievement2);
        model.createAchievement(achievement3);
        model.createAchievement(achievement4);
        model.createAchievement(achievement5);
        model.createAchievement(achievement6);
        model.createAchievement(achievement7);
        model.createAchievement(achievement8);
    }

    //Takes a list of ints which contain the unlocked clothes
    //and adds them to the end of a predetermined string to get a drawable of the clothes
    //Then takes the drawables resource and adds that to a different array list.
    public void addToMaleHeadList(ArrayList<Integer> list) {
        maleHeadList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_paa_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleHeadList.add(imageResource);
        }
    }

    public void addToMaleTorsoList(ArrayList<Integer> list) {
        maleTorsoList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_torso_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleTorsoList.add(imageResource);
        }
    }

    public void addToMaleLegList(ArrayList<Integer> list) {
        maleLegList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_pants_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleLegList.add(imageResource);
        }
    }

    public void addToMaleFeetList(ArrayList<Integer> list) {
        maleFeetList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/ukko_shoes_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            maleFeetList.add(imageResource);
        }
    }
    public void addToFemaleHeadList(ArrayList<Integer> list) {
        femaleHeadList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_paa_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleHeadList.add(imageResource);
        }
    }

    public void addToFemaleTorsoList(ArrayList<Integer> list) {
        femaleTorsoList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_torso_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleTorsoList.add(imageResource);
        }
    }

    public void addToFemaleLegList(ArrayList<Integer> list) {
        femaleLegList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_pants_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleLegList.add(imageResource);
        }
    }

    public void addToFemaleFeetList(ArrayList<Integer> list) {
        femaleFeetList.clear();
        for (int i = 0; i < list.size(); i++) {
            String uri = "drawable/akka_shoes_" + list.get(i);
            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
            femaleFeetList.add(imageResource);
        }
    }

    //Sets the character to female.
    public void setFemaleCharacter() {
        gender = 1;
        headPosition = 0;
        torsoPosition = 0;
        legPosition = 0;
        feetPosition = 0;
        setFemaleFeetImage();
        setFemaleLegImage();
        setFemaleTorsoImage();
        setFemaleHeadImage();
    }

    //Sets the character to male.
    public void setMaleCharacter() {
        gender = 0;
        headPosition = 0;
        torsoPosition = 0;
        legPosition = 0;
        feetPosition = 0;
        setMaleFeetImage();
        setMaleLegImage();
        setMaleTorsoImage();
        setMaleHeadImage();
    }


    //Takes the clothes and other information the user has chosen and either creates a new user in the database,
    //Or updates an existing user if it exists.
    public void saveClothesToDatabase() {
        DbModel model = new DbModel(getContext());
        name = nameEditText.getText().toString();
        if (model.checkIfTableEmpty("user")) {
            User user = new User(name, gender, headPosition, torsoPosition, legPosition, feetPosition, 1, 0, 0, 0, 0, 0.0, 0.0, 0.0, "", "", 0, 0, 0, 0, 170);
            model.addUserToDb(user);
            model.addMonster();
        } else {
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

    //Reads the users information from the database
    public void readClothesFromDatabase() {
        DbModel model = new DbModel(getContext());
        if (!model.checkIfTableEmpty("user")) {
            User user = model.readUserFromDb();
            name = user.getName();
            gender = user.getGender();
            headPosition = (user.getHat());
            torsoPosition = (user.getShirt());
            legPosition = (user.getPants());
            feetPosition = (user.getShoes());
        } else {
            name = "";
            gender = 0;
            headPosition = 0;
            torsoPosition = 0;
            legPosition = 0;
            feetPosition = 0;
        }
    }

    //Reads all the clothes the user has unlocked from the database and adds them to the array lists.
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

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible && isUserCreated) {
            createDialog();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }
}