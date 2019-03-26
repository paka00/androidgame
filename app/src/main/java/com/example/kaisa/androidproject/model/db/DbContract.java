package com.example.kaisa.androidproject.model.db;

import android.provider.BaseColumns;

public class DbContract {

    private DbContract() {}

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_TOTAL_STEPS = "totalSteps";
        public static final String COLUMN_DAILY_STEPS = "dailySteps";
    }
}
