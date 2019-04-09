package com.example.kaisa.androidproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SplashScreenReward extends AppCompatActivity {

    ImageButton imageButtonTreasure;
    ImageView imageViewReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_reward);
        imageButtonTreasure = findViewById(R.id.imagebutton_aarrearkku);
        imageButtonTreasure.setImageResource(R.drawable.aarrearkku_kiinni);
        imageViewReward = findViewById(R.id.imageview_vaate);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("ID", 0);
        String type = intent.getStringExtra("TYPE");
        final String resourceString = "akka_"+type+"_"+id;
        imageButtonTreasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReward(resourceString);
            }
        });
    }


    public void getReward(String resource) {
        imageButtonTreasure.setImageResource(R.drawable.aarrearkku_auki);
        imageViewReward.setImageResource(getImageId(this, resource));
    }


    public int getImageId(Context context, String resourceName) {
        return context.getResources().getIdentifier("drawable/" + resourceName, null, context.getPackageName());
    }
}
