package com.example.kaisa.androidproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kaisa.androidproject.model.Achievement;
import com.example.kaisa.androidproject.model.db.DbContract;

import java.util.ArrayList;

public class AchievementArrayAdapter extends ArrayAdapter<Achievement> {

    static final int VIEW_TYPE = 2;

    public AchievementArrayAdapter(Context context, ArrayList<Achievement> achievements){
        super(context, 0, achievements);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        Achievement achievement = getItem(position);
        return VIEW_TYPE;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Achievement achievement = getItem(position);

        if(convertView == null) {
            int layoutId = 0;
            layoutId = R.layout.achievement;

            convertView = LayoutInflater.from(getContext()).inflate(layoutId,parent,false);
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        TextView textViewDescription = convertView.findViewById(R.id.text_view_description);
        TextView textViewPercent = convertView.findViewById(R.id.text_view_percent);
        textViewName.setText(achievement.getName());
        textViewDescription.setText(achievement.getDescription());
        textViewPercent.setText(""+achievement.getCompletionPercent());
        return convertView;
    }
}
