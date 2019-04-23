package com.example.kaisa.androidproject.model;

import java.io.Serializable;

public class Achievement implements Serializable {

    int ID;
    String name;
    String description;
    double completionPercent;

    public Achievement() {}
    public Achievement(String name, String description, double completionPercent) {
        this.name = name;
        this.description = description;
        this.completionPercent = completionPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCompletionPercent() {
        return completionPercent;
    }

    public void setCompletionPercent(double completionPercent) {
        this.completionPercent = completionPercent;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
