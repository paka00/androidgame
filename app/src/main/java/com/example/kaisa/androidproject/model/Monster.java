package com.example.kaisa.androidproject.model;

import java.io.Serializable;

public class Monster implements Serializable {

    String turnOffDate;
    String turnOnDate;
    Double monsterDistance;
    Double highScoreDistance;

    public Monster(String turnOffDate, String turnOnDate, Double monsterDistance, Double highScoreDistance) {
        this.turnOffDate = turnOffDate;
        this.turnOnDate = turnOnDate;
        this.monsterDistance = monsterDistance;
        this.highScoreDistance = highScoreDistance;
    }

    public String getTurnOffDate() {
        return turnOffDate;
    }

    public void setTurnOffDate(String turnOffDate) {
        this.turnOffDate = turnOffDate;
    }

    public String getTurnOnDate() {
        return turnOnDate;
    }

    public void setTurnOnDate(String turnOnDate) {
        this.turnOnDate = turnOnDate;
    }

    public Double getMonsterDistance() {
        return monsterDistance;
    }

    public void setMonsterDistance(Double monsterDistance) {
        this.monsterDistance = monsterDistance;
    }

    public Double getHighScoreDistance() {
        return highScoreDistance;
    }

    public void setHighScoreDistance(Double highScoreDistance) {
        this.highScoreDistance = highScoreDistance;
    }
}
