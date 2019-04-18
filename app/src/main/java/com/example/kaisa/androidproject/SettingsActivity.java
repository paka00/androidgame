package com.example.kaisa.androidproject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBarHeight, seekBarDifficulty;
    private TextView textViewHeight, textViewDifficulty, hintHeight, hintDifficulty;
    int height = 0;
    int level = 0;
    DbModel model;
    User user;
    ImageButton doneButton;
    ModifyFigureFragment modifyFigureFragment;
    Typeface pixelFont = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        model = new DbModel(this);
        user = model.readUserFromDb();
        modifyFigureFragment = new ModifyFigureFragment();
        adjustHeight();
        adjustLevel();

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
        level = user.getDifficultyLevel();
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