package com.example.kaisa.androidproject.model;

import java.io.Serializable;

public class User implements Serializable {

    String name;
    int gender;
    int hat;
    int shirt;
    int pants;
    int shoes;
    int hatNumber;
    int shirtNumber;
    int pantsNumber;
    int shoesNumber;
    int level;
    int totalSteps;
    int dailySteps;
    int stepHelper;
    int dailyStepHelper;
    double totalDistance;
    double dailyDistance;
    double averageSpeed;
    String walkDate;
    String walkTime;
    float walkDistance;
    int dailyReward;
    int dailyStepGoal;
    int difficultyLevel;
    int height;

    public User(String name, int gender, int hat, int shirt, int pants, int shoes, int hatNumber, int shirtNumber, int pantsNumber, int shoesNumber, int level, int totalSteps, int dailySteps, int stepHelper, int dailyStepHelper, double totalDistance, double dailyDistance, double averageSpeed, String walkDate, String walkTime, float walkDistance, int dailyReward, int dailyStepGoal, int difficultyLevel, int height) {
        this.name = name;
        this.gender = gender;
        this.hat = hat;
        this.shirt = shirt;
        this.pants = pants;
        this.shoes = shoes;
        this.hatNumber = hatNumber;
        this.shirtNumber = shirtNumber;
        this.pantsNumber = pantsNumber;
        this.shoesNumber = shoesNumber;
        this.level = level;
        this.totalSteps = totalSteps;
        this.dailySteps = dailySteps;
        this.stepHelper = stepHelper;
        this.dailyStepHelper = dailyStepHelper;
        this.totalDistance = totalDistance;
        this.dailyDistance = dailyDistance;
        this.averageSpeed = averageSpeed;
        this.walkDate = walkDate;
        this.walkTime = walkTime;
        this.walkDistance = walkDistance;
        this.dailyReward = dailyReward;
        this.dailyStepGoal = dailyStepGoal;
        this.difficultyLevel = difficultyLevel;
        this.height = height;
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

    public String getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getWalkDate() {
        return walkDate;
    }

    public void setWalkDate(String walkDate) {
        this.walkDate = walkDate;
    }

    public float getWalkDistance() {
        return walkDistance;
    }

    public void setWalkDistance(float walkDistance) {
        this.walkDistance = walkDistance;
    }

    public int getDailyReward() {
        return dailyReward;
    }

    public void setDailyReward(int dailyReward) {
        this.dailyReward = dailyReward;
    }

    public int getDailyStepGoal() {
        return dailyStepGoal;
    }

    public void setDailyStepGoal(int dailyStepGoal) {
        this.dailyStepGoal = dailyStepGoal;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHatNumber() {
        return hatNumber;
    }

    public void setHatNumber(int hatNumber) {
        this.hatNumber = hatNumber;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public int getPantsNumber() {
        return pantsNumber;
    }

    public void setPantsNumber(int pantsNumber) {
        this.pantsNumber = pantsNumber;
    }

    public int getShoesNumber() {
        return shoesNumber;
    }

    public void setShoesNumber(int shoesNumber) {
        this.shoesNumber = shoesNumber;
    }
}
