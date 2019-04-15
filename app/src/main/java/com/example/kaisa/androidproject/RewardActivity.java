package com.example.kaisa.androidproject;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kaisa.androidproject.model.DbModel;
import com.example.kaisa.androidproject.model.User;

public class RewardActivity extends AppCompatActivity {

    private ImageButton imageButtonTreasure = null;
    private ImageView imageViewReward = null;
    private Button backButton = null;
    private String resourceString = "";
    private int randomType = 0;
    private Guideline guideline;
    private String[] types = {"paa", "torso", "pants", "shoes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        imageButtonTreasure = findViewById(R.id.imagebutton_aarrearkku);
        imageButtonTreasure.setImageResource(R.drawable.aarrearkku_kiinni);
        imageViewReward = findViewById(R.id.imageview_vaate);
        backButton = findViewById(R.id.button_back);
        backButton.setVisibility(View.INVISIBLE);
        guideline = findViewById(R.id.horGuideline1);
        String[] genders = {"ukko", "akka"};
        DbModel model = new DbModel(this);
        User user = model.readUserFromDb();
        int gender = user.getGender();

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 1);
        randomType = intent.getIntExtra("TYPE", 0);

        resourceString = genders[gender] + "_" + types[randomType] + "_" + id;

        imageButtonTreasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleImage(randomType);
                getReward(resourceString);
                backButton.setVisibility(View.VISIBLE);
                imageButtonTreasure.setEnabled(false);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RewardActivity.this, "Your reward has been saved! You can now add it to your figure!", Toast.LENGTH_SHORT).show();
                finish();
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

    public void scaleImage(int typeNumber) {
        switch (typeNumber) {
            case 0:
                guideline.setGuidelinePercent(1.0f);
                break;
            case 1:
                guideline.setGuidelinePercent(0.8f);
                break;
            case 2:
                guideline.setGuidelinePercent(0.55f);
                break;
            case 3:
                guideline.setGuidelinePercent(0.25f);
                break;
        }
    }
}