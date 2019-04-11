package com.example.kaisa.androidproject;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

public class RewardActivity extends AppCompatActivity {

    ImageButton imageButtonTreasure = null;
    ImageView imageViewReward = null;
    private String resourceString = "";
    private int randomType = 0;
    Guideline guideline;
    String[] types = {"paa", "torso", "pants", "shoes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        imageButtonTreasure = findViewById(R.id.imagebutton_aarrearkku);
        imageButtonTreasure.setImageResource(R.drawable.aarrearkku_kiinni);
        imageViewReward = findViewById(R.id.imageview_vaate);
        guideline = findViewById(R.id.horGuideline1);
        String[] genders = {"ukko", "akka"};
        DbModel model = new DbModel(this);
        User user = model.readUserFromDb();
        int gender = user.getGender();

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 1);
        randomType = intent.getIntExtra("TYPE", 0);

        resourceString = genders[gender]+"_"+types[randomType]+"_"+id;

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

    public void scaleImage() {
        //kengille 25
        //jaloille 55
        //torsolle 80
        //päälle 100
        guideline.setGuidelinePercent(25);
    }
}
