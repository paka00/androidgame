package com.example.kaisa.androidproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageButton;

public class ModifyFigureFragment extends Fragment implements View.OnClickListener {

    ImageView imageView, imageView1;
    MainActivity context;
    public Button buttonHat, buttonMale, buttonFemale, buttonCancel, doneButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MainActivity) container.getContext();
        return inflater.inflate(R.layout.fragment_modify_figure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        ImageButton btntest = getView().findViewById(R.id.btn_nuoli);
        doneButton = getView().findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
        btntest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int ID = v.getId();
        if (ID == R.id.done_button) {
            if (context.databaseEmpty) {
                //Jos tietokanta on tyhjä
                Toast.makeText(getActivity(), "New figure created!", Toast.LENGTH_SHORT).show();
                context.viewPager.disableScroll(false);
                context.navigation.setVisibility(View.VISIBLE);
                context.imageButton.setVisibility(View.VISIBLE);
                context.viewPager.setCurrentItem(0);
            } else {
                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                //Tietojentallennus tietokantaan
            }
        }
    }
}