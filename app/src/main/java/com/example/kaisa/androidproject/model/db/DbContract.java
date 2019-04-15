package com.example.kaisa.androidproject.model.db;

import android.provider.BaseColumns;

public class DbContract {

    private DbContract() {}

    public static class User implements BaseColumns {
        public static final String TABLE_NAME_USER = "user";
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
        public static final String COLUMN_DAILY_REWARD = "dailyReward";
        public static final String COLUMN_DAILY_STEP_GOAL = "dailyStepGoal";
    }

    public static class ClothesUnlocks implements BaseColumns {
        public static final String TABLE_NAME_CLOTHES = "clothesUnlocks";
        public static final String COLUMN_HATS_M = "hats";
        public static final String COLUMN_SHIRTS_M = "shirts";
        public static final String COLUMN_PANTS_M = "pants";
        public static final String COLUMN_SHOES_M = "shoes";
        public static final String COLUMN_HATS_F = "hatsf";
        public static final String COLUMN_SHIRTS_F = "shirtsf";
        public static final String COLUMN_PANTS_F = "pantsf";
        public static final String COLUMN_SHOES_F = "shoesf";
    }

    public static class MonsterStats implements BaseColumns {
        public static final String TABLE_NAME_MONSTER = "monsterStats";
        public static final String COLUMN_TURN_OFF_DATE = "turnOffDate";
        public static final String COLUMN_TURN_ON_DATE = "turnonDate";
        public static final String COLUMN_MONSTER_DISTANCE = "monsterDistance";
        public static final String COLUMN_HIGH_SCORE_DISTANCE = "highScoreDistance";
    }

    public static class Achievement implements BaseColumns {
        public static final String TABLE_NAME_ACHIEVEMENT = "achievement";
        public static final String COLUMN_ACHIEVEMENT_NAME = "achievementName";
        public static final String COLUMN_ACHIEVEMENT_DESCRIPTION = "achievementDescription";
        public static final String COLUMN_ACHIEVEMENT_PERCENT = "achievementPercent";
    }
}