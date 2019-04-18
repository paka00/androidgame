package com.example.kaisa.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBarHeight, seekBarDifficulty;
    private TextView textViewHeight, textViewDifficulty;
    int height = 0;
    int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekBarHeight = findViewById(R.id.seekbar_height);
        seekBarDifficulty = findViewById(R.id.seekbar_difficulty);
        textViewHeight = findViewById(R.id.text_view_height);
        textViewDifficulty = findViewById(R.id.text_view_difficultylevel);
        adjustHeight();
        adjustLevel();
    }

    private void adjustHeight(){
        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                height = progress;
                textViewHeight.setText("Current height: "+height);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //tietokantaantallennus tänne
            }
        });
    }

    private void adjustLevel() {
        seekBarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                level = progress;
                textViewDifficulty.setText("Current level: "+level);
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