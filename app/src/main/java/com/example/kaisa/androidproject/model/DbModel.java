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
        values.put(DbContract.User.COLUMN_TOTAL_STEPS, addable.totalSteps);
        values.put(DbContract.User.COLUMN_DAILY_STEPS, addable.dailySteps);

        try {
            long newRowId = db.insert(DbContract.User.TABLE_NAME, null, values);
        }
        catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                Log.e("stepdbmodel", "table doesn't exist");
            }
        }
    }

    public ArrayList<User> readUserFromDb() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList<User> userStats = new ArrayList<>();

        String[] projection = {
                DbContract.User.COLUMN_TOTAL_STEPS,
                DbContract.User.COLUMN_DAILY_STEPS
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
            int totalSteps = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_TOTAL_STEPS));
            int dailySteps = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_STEPS));
            User user = new User(totalSteps, dailySteps);
            userStats.add(user);
        }
        cursor.close();

        return userStats;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int totalSteps = user.getSteps();
        int dailySteps = user.getDailySteps();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DbContract.User.COLUMN_TOTAL_STEPS, totalSteps);
        values.put(DbContract.User.COLUMN_DAILY_STEPS, dailySteps);

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
