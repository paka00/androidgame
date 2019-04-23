package com.example.kaisa.androidproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

import org.w3c.dom.Text;

import java.util.Set;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBarHeight, seekBarDifficulty;
    private TextView textViewHeight, textViewDifficulty, hintHeight, hintDifficulty;
    int height = 0;
    int level = 1;
    DbModel model;
    User user;
    Button doneButton;
    ModifyFigureFragment modifyFigureFragment;
    Typeface pixelFont = null;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        pixelFont = Typeface.createFromAsset(this.getAssets(), "fonts/smallest_pixel-7.ttf");
        seekBarHeight = findViewById(R.id.seekbar_height);
        seekBarDifficulty = findViewById(R.id.seekbar_difficulty);
        hintHeight = findViewById(R.id.hint_height);
        hintDifficulty = findViewById(R.id.hint_difficulty);
        textViewHeight = findViewById(R.id.text_view_height);
        textViewDifficulty = findViewById(R.id.text_view_difficultylevel);
        textViewHeight.setTypeface(pixelFont);
        textViewDifficulty.setTypeface(pixelFont);
        hintHeight.setTypeface(pixelFont);
        hintDifficulty.setTypeface(pixelFont);
        doneButton = findViewById(R.id.done_button);
        prefs = this.getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        model = new DbModel(this);
        user = model.readUserFromDb();
        modifyFigureFragment = new ModifyFigureFragment();
        adjustHeight();
        adjustLevel();
        createDialog();


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getHeight() == 170) {
                    //If database is empty
                    Toast.makeText(SettingsActivity.this, "New figure created!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SettingsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                user.setHeight(height);
                user.setDifficultyLevel(level);
                model.updateUser(user);
            }
        });
    }

    //Creates a dialog box with information about the current page.
    public void createDialog() {
        prefs = this.getSharedPreferences("com.KOTKAME.CreatureChase", MODE_PRIVATE);
        if (!prefs.getBoolean("appHasRunBeforeSettings", false)) {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialoglayout = inflater.inflate(R.layout.instruction_dialog_layout, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialoglayout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button doneBtn = dialoglayout.findViewById(R.id.dialog_done_btn);
            TextView titleText = dialoglayout.findViewById(R.id.dialog_welcome_text);
            titleText.setTypeface(pixelFont);
            titleText.setText("Settings");
            TextView bodyText = dialoglayout.findViewById(R.id.dialog_instruction_text);
            bodyText.setTypeface(pixelFont);
            bodyText.setText("Please set your height and the difficulty level you want. " +
                    "The higher the difficulty level, the faster the creature chases you. " +
                    "You can change these later.");
            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            prefs.edit().putBoolean("appHasRunBeforeSettings", true).apply();
            Log.d("homefragment", "firstrun");
        }
    }

    private void adjustHeight() {
        height = user.getHeight();
        textViewHeight.setText("Current height: " + height);
        seekBarHeight.setProgress(height);
        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                height = progress;
                textViewHeight.setText("Current height: " + height);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void adjustLevel() {
        if(!model.checkIfTableEmpty("user")) {
            level = user.getDifficultyLevel();
        }
        textViewDifficulty.setText("Current level: " + level);
        seekBarDifficulty.setProgress(level);
        seekBarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                level = progress;
                textViewDifficulty.setText("Current level: " + level);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}