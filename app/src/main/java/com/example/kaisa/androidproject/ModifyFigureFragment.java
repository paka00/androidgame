package com.example.kaisa.androidproject;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModifyFigureFragment extends Fragment implements View.OnClickListener {
    ArrayList<Integer> maleHeadList = new ArrayList<Integer>();
    int position = 0;
    int ListMinValue = 0;
    ImageView imageview_maleHead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modify_figure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageview_maleHead = getView().findViewById(R.id.imageview_male_head);
        maleHeadList.add(R.drawable.mies_paa_0);
        maleHeadList.add(R.drawable.mies_paa_1);

        imageview_maleHead.setImageResource(maleHeadList.get(position));

        ImageButton button_head_to_left = getView().findViewById(R.id.button_head_to_left);
        button_head_to_left.setOnClickListener(this);

        ImageButton button_head_to_right = getView().findViewById(R.id.button_head_to_right);
        button_head_to_right.setOnClickListener(this);

        Button doneButton = getView().findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);


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
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        int headListMaxValue = Collections.max(maleHeadList);
        if (buttonID == R.id.button_head_to_left) {
            if (position >= ListMinValue || position <= headListMaxValue) {
                position--;
                imageview_maleHead.setImageResource(maleHeadList.get(position));
            }
        }
        if (buttonID == R.id.button_head_to_right) {
            if (position >= ListMinValue || position <= headListMaxValue) {
                position++;
                imageview_maleHead.setImageResource(maleHeadList.get(position));
            }
        }

        if(buttonID == R.id.done_button){
            if(MainActivity.databaseEmpty){
                //jos tietokanta on tyhjä
                MainActivity.viewPager.setCurrentItem(0);
                MainActivity.navigation.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                //Tietojen tallennus tietokantaan
            }
        }
    }
}
