package com.example.kaisa.androidproject.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.kaisa.androidproject.model.db.DbContract;
import com.example.kaisa.androidproject.model.db.DbHelper;

import java.util.ArrayList;

public class DbModel {
    private DbHelper mDbHelper;

    public DbModel(Context context) {
        this.mDbHelper = new DbHelper(context);
    }

    public void addUserToDb(User addable){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.User.COLUMN_USERNAME, addable.name);
        values.put(DbContract.User.COLUMN_GENDER, addable.gender);
        values.put(DbContract.User.COLUMN_HAT, addable.hat);
        values.put(DbContract.User.COLUMN_SHIRT, addable.shirt);
        values.put(DbContract.User.COLUMN_PANTS, addable.pants);
        values.put(DbContract.User.COLUMN_SHOES, addable.shoes);
        values.put(DbContract.User.COLUMN_TOTAL_STEPS, addable.totalSteps);
        values.put(DbContract.User.COLUMN_DAILY_STEPS, addable.dailySteps);
        values.put(DbContract.User.COLUMN_STEP_COUNTER_HELPER, addable.stepHelper);
        values.put(DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER, addable.dailyStepHelper);
        values.put(DbContract.User.COLUMN_TOTAL_DISTANCE, addable.totalDistance);
        values.put(DbContract.User.COLUMN_DAILY_DISTANCE, addable.dailyDistance);
        values.put(DbContract.User.COLUMN_AVERAGE_SPEED, addable.averageSpeed);
        values.put(DbContract.User.COLUMN_WALK_START_TIME, addable.walkStartTime);
        values.put(DbContract.User.COLUMN_WALK_END_TIME, addable.walkEndTime);

        try {
            long newRowId = db.insert(DbContract.User.TABLE_NAME, null, values);
        }
        catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                Log.e("stepdbmodel", "table doesn't exist");
            }
        }
    }

    public User readUserFromDb() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        User user = null;

        String[] projection = {
                DbContract.User.COLUMN_USERNAME,
                DbContract.User.COLUMN_GENDER,
                DbContract.User.COLUMN_HAT,
                DbContract.User.COLUMN_SHIRT,
                DbContract.User.COLUMN_PANTS,
                DbContract.User.COLUMN_SHOES,
                DbContract.User.COLUMN_TOTAL_STEPS,
                DbContract.User.COLUMN_DAILY_STEPS,
                DbContract.User.COLUMN_STEP_COUNTER_HELPER,
                DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER,
                DbContract.User.COLUMN_TOTAL_DISTANCE,
                DbContract.User.COLUMN_DAILY_DISTANCE,
                DbContract.User.COLUMN_AVERAGE_SPEED,
                DbContract.User.COLUMN_WALK_START_TIME,
                DbContract.User.COLUMN_WALK_END_TIME
        };

        String sortOrder = DbContract.User._ID + " DESC";

        Cursor cursor = db.query(
                DbContract.User.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_USERNAME));
            int gender = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_GENDER));
            int hat = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_HAT));
            int shirt = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_SHIRT));
            int pants = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_PANTS));
            int shoes = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_SHOES));
            int totalSteps = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_TOTAL_STEPS));
            int dailySteps = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_STEPS));
            int stepHelper = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_STEP_COUNTER_HELPER));
            int dailyStepHelper = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER));
            double totalDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_TOTAL_DISTANCE));
            double dailyDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_DISTANCE));
            double averageSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_AVERAGE_SPEED));
            long walkStartTime = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_WALK_START_TIME));
            long walkEndTime = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_WALK_END_TIME));
            user = new User(name, gender, hat, shirt, pants, shoes, totalSteps, dailySteps, stepHelper, dailyStepHelper, totalDistance, dailyDistance, averageSpeed, walkStartTime, walkEndTime);
        }
        cursor.close();

        return user;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String name = user.getName();
        int gender = user.getGender();
        int hat = user.getHat();
        int shirt = user.getShirt();
        int pants = user.getPants();
        int totalSteps = user.getSteps();
        int dailySteps = user.getDailySteps();
        int stepHelper = user.getStepHelper();
        int dailyStepHelper = user.getDailyStepHelper();
        double totalDistance = user.getTotalDistance();
        double dailyDistance = user.getDailyDistance();
        double averageSpeed = user.getAverageSpeed();
        long walkStartTime = user.getWalkStartTime();
        long walkEndTime = user.getWalkEndTime();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DbContract.User.COLUMN_USERNAME, name);
        values.put(DbContract.User.COLUMN_GENDER, gender);
        values.put(DbContract.User.COLUMN_HAT, hat);
        values.put(DbContract.User.COLUMN_SHIRT, shirt);
        values.put(DbContract.User.COLUMN_PANTS, pants);
        values.put(DbContract.User.COLUMN_TOTAL_STEPS, totalSteps);
        values.put(DbContract.User.COLUMN_DAILY_STEPS, dailySteps);
        values.put(DbContract.User.COLUMN_STEP_COUNTER_HELPER, stepHelper);
        values.put(DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER, dailyStepHelper);
        values.put(DbContract.User.COLUMN_TOTAL_DISTANCE, totalDistance);
        values.put(DbContract.User.COLUMN_DAILY_DISTANCE, dailyDistance);
        values.put(DbContract.User.COLUMN_AVERAGE_SPEED, averageSpeed);
        values.put(DbContract.User.COLUMN_WALK_START_TIME, walkStartTime);
        values.put(DbContract.User.COLUMN_WALK_END_TIME, walkEndTime);

        // Which row to update, based on the title
        String selection = DbContract.User._ID + " LIKE ?";
        String[] selectionArgs = { "1" };

        int count = db.update(
                DbContract.User.TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    public void resetDailyStats() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.User.COLUMN_DAILY_STEPS, 0);
        values.put(DbContract.User.COLUMN_DAILY_DISTANCE, 0);
        String selection = DbContract.User._ID + " LIKE ?";
        String[] selectionArgs = { "1" };

        int count = db.update(
                DbContract.User.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public boolean checkIfTableEmpty() {
        boolean empty = true;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM user";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0){
            empty = false;
        }
        return empty;
    }

}
