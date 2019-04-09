package com.example.kaisa.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SplashScreenReward extends AppCompatActivity {

    ImageButton imageButtonTreasure;
    ImageView imageViewVaate;
    HomeFragment fragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_reward);
        imageButtonTreasure = findViewById(R.id.imagebutton_aarrearkku);
        imageButtonTreasure.setImageResource(R.drawable.aarrearkku_kiinni);
        imageViewVaate = findViewById(R.id.imageview_vaate);

        imageButtonTreasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonTreasure.setImageResource(R.drawable.aarrearkku_auki);
                imageViewVaate.setImageResource(R.drawable.akka_paa_1);
            }
        });
    }
}
