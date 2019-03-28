package com.example.kaisa.androidproject.model;

import java.io.Serializable;

public class User implements Serializable {

    String name;
    int gender;
    int hat;
    int shirt;
    int pants;
    int shoes;
    int totalSteps;
    int dailySteps;
    int stepHelper;
    int dailyStepHelper;
    double totalDistance;
    double dailyDistance;
    double averageSpeed;
    long walkStartTime;
    long walkEndTime;

    public User(int totalSteps, int dailySteps, int stepHelper, int dailyStepHelper) {
        this.totalSteps = totalSteps;
        this.dailySteps = dailySteps;
        this.stepHelper = stepHelper;
        this.dailyStepHelper = dailyStepHelper;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHat() {
        return hat;
    }

    public void setHat(int hat) {
        this.hat = hat;
    }

    public int getShirt() {
        return shirt;
    }

    public void setShirt(int shirt) {
        this.shirt = shirt;
    }

    public int getPants() {
        return pants;
    }

    public void setPants(int pants) {
        this.pants = pants;
    }

    public int getShoes() {
        return shoes;
    }

    public void setShoes(int shoes) {
        this.shoes = shoes;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getDailyDistance() {
        return dailyDistance;
    }

    public void setDailyDistance(double dailyDistance) {
        this.dailyDistance = dailyDistance;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public long getWalkStartTime() {
        return walkStartTime;
    }

    public void setWalkStartTime(long walkStartTime) {
        this.walkStartTime = walkStartTime;
    }

    public long getWalkEndTime() {
        return walkEndTime;
    }

    public void setWalkEndTime(long walkEndTime) {
        this.walkEndTime = walkEndTime;
    }

    public int getStepHelper() {
        return stepHelper;
    }

    public void setStepHelper(int stepHelper) {
        this.stepHelper = stepHelper;
    }

    public int getDailyStepHelper() {
        return dailyStepHelper;
    }

    public void setDailyStepHelper(int dailyStepHelper) {
        this.dailyStepHelper = dailyStepHelper;
    }
}
