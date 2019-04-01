package com.example.kaisa.androidproject.model.db;

import android.provider.BaseColumns;

public class DbContract {

    private DbContract() {}

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_HAT = "hat";
        public static final String COLUMN_SHIRT = "shirt";
        public static final String COLUMN_PANTS = "pants";
        public static final String COLUMN_SHOES = "shoes";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_TOTAL_STEPS = "totalSteps";
        public static final String COLUMN_DAILY_STEPS = "dailySteps";
        public static final String COLUMN_STEP_COUNTER_HELPER = "stepHelper";
        public static final String COLUMN_DAILY_STEP_COUNTER_HELPER = "dailyStepHelper";
        public static final String COLUMN_TOTAL_DISTANCE = "totalDistance";
        public static final String COLUMN_DAILY_DISTANCE = "dailyDistance";
        public static final String COLUMN_AVERAGE_SPEED = "averageSpeed";
        public static final String COLUMN_WALK_DATE = "walkDate";
        public static final String COLUMN_WALK_TIME = "walkTime";
        public static final String COLUMN_WALK_DISTANCE = "walkDistance";
    }
}
