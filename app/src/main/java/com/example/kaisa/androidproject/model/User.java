package com.example.kaisa.androidproject.model;

import java.io.Serializable;

public class User implements Serializable {

    int totalSteps;
    int dailySteps;

    public User(int totalSteps, int dailySteps) {
        this.totalSteps = totalSteps;
        this.dailySteps = dailySteps;
    }

    public int getSteps() {
        return totalSteps;
    }

    public void setSteps(int steps) {
        this.totalSteps = steps;
    }

    public int getDailySteps() {
        return dailySteps;
    }

    public void setDailySteps(int dailySteps) {
        this.dailySteps = dailySteps;
    }
}
