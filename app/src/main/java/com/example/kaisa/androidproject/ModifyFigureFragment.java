package com.example.kaisa.androidproject;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ModifyFigureFragment extends Fragment implements View.OnClickListener{
    ImageView imageView,imageView1;
    Button buttonHat,buttonMale,buttonFemale,buttonCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modify_figure, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonHat = getView().findViewById(R.id.testi_button);
        buttonHat.setOnClickListener(this);
        buttonCancel = getView().findViewById(R.id.peru_button);
        buttonCancel.setOnClickListener(this);
        buttonMale = getView().findViewById(R.id.button_gender_male);
        buttonMale.setOnClickListener(this);
        buttonFemale = getView().findViewById(R.id.button_gender_female);
        buttonFemale.setOnClickListener(this);
        imageView = getView().findViewById(R.id.imageview_hattu);
        imageView1 = getView().findViewById(R.id.imageview_character);


    }
    @Override
    public void onClick(View v) {
        int ID = v.getId();
        if(ID == R.id.testi_button){
            imageView.setImageResource(R.drawable.hattu);
        }
        if(ID == R.id.peru_button){
            imageView.setImageResource(0);
        }
        if(ID == R.id.button_gender_male){
            imageView1.setImageResource(R.drawable.character_male);
        }
        if(ID == R.id.button_gender_female){
            imageView1.setImageResource(R.drawable.character_female);
        }
    }


}
