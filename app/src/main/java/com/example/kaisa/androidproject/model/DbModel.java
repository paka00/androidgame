package com.example.kaisa.androidproject.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.kaisa.androidproject.model.db.DbContract;
import com.example.kaisa.androidproject.model.db.DbHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DbModel {
    private DbHelper mDbHelper;

    public DbModel(Context context) {
        this.mDbHelper = new DbHelper(context);
    }

    public void addUserToDb(User addable){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues userValues = new ContentValues();
        userValues.put(DbContract.User.COLUMN_USERNAME, addable.name);
        userValues.put(DbContract.User.COLUMN_GENDER, addable.gender);
        userValues.put(DbContract.User.COLUMN_HAT, addable.hat);
        userValues.put(DbContract.User.COLUMN_SHIRT, addable.shirt);
        userValues.put(DbContract.User.COLUMN_PANTS, addable.pants);
        userValues.put(DbContract.User.COLUMN_SHOES, addable.shoes);
        userValues.put(DbContract.User.COLUMN_LEVEL, addable.level);
        userValues.put(DbContract.User.COLUMN_TOTAL_STEPS, addable.totalSteps);
        userValues.put(DbContract.User.COLUMN_DAILY_STEPS, addable.dailySteps);
        userValues.put(DbContract.User.COLUMN_STEP_COUNTER_HELPER, addable.stepHelper);
        userValues.put(DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER, addable.dailyStepHelper);
        userValues.put(DbContract.User.COLUMN_TOTAL_DISTANCE, addable.totalDistance);
        userValues.put(DbContract.User.COLUMN_DAILY_DISTANCE, addable.dailyDistance);
        userValues.put(DbContract.User.COLUMN_AVERAGE_SPEED, addable.averageSpeed);
        userValues.put(DbContract.User.COLUMN_WALK_DATE, addable.walkDate);
        userValues.put(DbContract.User.COLUMN_WALK_TIME, addable.walkDate);
        userValues.put(DbContract.User.COLUMN_WALK_DISTANCE, addable.walkDistance);

        ContentValues clothesValues = new ContentValues();
        clothesValues.put(DbContract.ClothesUnlocks.COLUMN_HATS, 0);
        clothesValues.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS, 0);
        clothesValues.put(DbContract.ClothesUnlocks.COLUMN_PANTS, 0);
        clothesValues.put(DbContract.ClothesUnlocks.COLUMN_SHOES, 0);

        try {
            long newRowId = db.insert(DbContract.User.TABLE_NAME_USER, null, userValues);
            long newRowId2 = db.insert(DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES, null, clothesValues);
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
                DbContract.User.COLUMN_LEVEL,
                DbContract.User.COLUMN_TOTAL_STEPS,
                DbContract.User.COLUMN_DAILY_STEPS,
                DbContract.User.COLUMN_STEP_COUNTER_HELPER,
                DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER,
                DbContract.User.COLUMN_TOTAL_DISTANCE,
                DbContract.User.COLUMN_DAILY_DISTANCE,
                DbContract.User.COLUMN_AVERAGE_SPEED,
                DbContract.User.COLUMN_WALK_DATE,
                DbContract.User.COLUMN_WALK_TIME,
                DbContract.User.COLUMN_WALK_DISTANCE
        };

        String sortOrder = DbContract.User._ID + " DESC";

        Cursor cursor = db.query(
                DbContract.User.TABLE_NAME_USER,
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
            int level = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_LEVEL));
            int totalSteps = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_TOTAL_STEPS));
            int dailySteps = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_STEPS));
            int stepHelper = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_STEP_COUNTER_HELPER));
            int dailyStepHelper = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER));
            double totalDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_TOTAL_DISTANCE));
            double dailyDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_DISTANCE));
            double averageSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_AVERAGE_SPEED));
            String walkDate = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_WALK_DATE));
            String walkTime = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_WALK_TIME));
            float walkDistance = cursor.getFloat(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_WALK_DISTANCE));
            user = new User(name, gender, hat, shirt, pants, shoes, level, totalSteps, dailySteps, stepHelper, dailyStepHelper, totalDistance, dailyDistance, averageSpeed, walkDate, walkTime, walkDistance);
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
        int shoes = user.getShoes();
        int level = user.getLevel();
        int totalSteps = user.getSteps();
        int dailySteps = user.getDailySteps();
        int stepHelper = user.getStepHelper();
        int dailyStepHelper = user.getDailyStepHelper();
        double totalDistance = user.getTotalDistance();
        double dailyDistance = user.getDailyDistance();
        double averageSpeed = user.getAverageSpeed();
        String walkDate = user.getWalkDate();
        String walkTime = user.getWalkTime();
        float walkDistance = user.getWalkDistance();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DbContract.User.COLUMN_USERNAME, name);
        values.put(DbContract.User.COLUMN_GENDER, gender);
        values.put(DbContract.User.COLUMN_HAT, hat);
        values.put(DbContract.User.COLUMN_SHIRT, shirt);
        values.put(DbContract.User.COLUMN_PANTS, pants);
        values.put(DbContract.User.COLUMN_SHOES, shoes);
        values.put(DbContract.User.COLUMN_LEVEL, level);
        values.put(DbContract.User.COLUMN_TOTAL_STEPS, totalSteps);
        values.put(DbContract.User.COLUMN_DAILY_STEPS, dailySteps);
        values.put(DbContract.User.COLUMN_STEP_COUNTER_HELPER, stepHelper);
        values.put(DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER, dailyStepHelper);
        values.put(DbContract.User.COLUMN_TOTAL_DISTANCE, totalDistance);
        values.put(DbContract.User.COLUMN_DAILY_DISTANCE, dailyDistance);
        values.put(DbContract.User.COLUMN_AVERAGE_SPEED, averageSpeed);
        values.put(DbContract.User.COLUMN_WALK_DATE, walkDate);
        values.put(DbContract.User.COLUMN_WALK_TIME, walkTime);
        values.put(DbContract.User.COLUMN_WALK_DISTANCE, walkDistance);

        // Which row to update, based on the title
        String selection = DbContract.User._ID + " LIKE ?";
        String[] selectionArgs = { "1" };

        int count = db.update(
                DbContract.User.TABLE_NAME_USER,
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
                DbContract.User.TABLE_NAME_USER,
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

    public void addHat (int hatNumber) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ArrayList<Integer> hats = readHats();
        String size =  String.valueOf(hats.size() + 1);
        String selection = DbContract.ClothesUnlocks._ID + " LIKE ?";
        String[] selectionArgs = { size };
        ContentValues values = new ContentValues();
        values.put(DbContract.ClothesUnlocks.COLUMN_HATS, hatNumber);
        int count = db.update(
                DbContract.User.TABLE_NAME_USER,
                values,
                selection,
                selectionArgs);
    }

    public void addShirt (int shirtNumber) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ArrayList<Integer> shirts = readShirts();
        String size =  String.valueOf(shirts.size() + 1);
        String selection = DbContract.ClothesUnlocks._ID + " LIKE ?";
        String[] selectionArgs = { size };
        ContentValues values = new ContentValues();
        values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS, shirtNumber);
        int count = db.update(
                DbContract.User.TABLE_NAME_USER,
                values,
                selection,
                selectionArgs);
    }

    public void addPants (int pantsNumber) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ArrayList<Integer> pants = readPants();
        String size =  String.valueOf(pants.size() + 1);
        String selection = DbContract.ClothesUnlocks._ID + " LIKE ?";
        String[] selectionArgs = { size };
        ContentValues values = new ContentValues();
        values.put(DbContract.ClothesUnlocks.COLUMN_PANTS, pantsNumber);
        int count = db.update(
                DbContract.User.TABLE_NAME_USER,
                values,
                selection,
                selectionArgs);
    }

    public void addShoes (int shoesNumber) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ArrayList<Integer> shoes = readShoes();
        String size =  String.valueOf(shoes.size() + 1);
        String selection = DbContract.ClothesUnlocks._ID + " LIKE ?";
        String[] selectionArgs = { size };
        ContentValues values = new ContentValues();
        values.put(DbContract.ClothesUnlocks.COLUMN_SHOES, shoesNumber);
        int count = db.update(
                DbContract.User.TABLE_NAME_USER,
                values,
                selection,
                selectionArgs);
    }

    public ArrayList<Integer> readHats() {
        ArrayList<Integer> hats = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " DESC";

        String[] projection = {
                DbContract.ClothesUnlocks.COLUMN_HATS
        };

        Cursor cursor = db.query(
                DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()){
            hats.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_HATS)));
        }

        return hats;
    }

    public ArrayList<Integer> readShirts() {
        ArrayList<Integer> shirts = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " DESC";

        String[] projection = {
                DbContract.ClothesUnlocks.COLUMN_SHIRTS
        };

        Cursor cursor = db.query(
                DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()){
            shirts.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_SHIRTS)));
        }

        return shirts;
    }

    public ArrayList<Integer> readPants() {
        ArrayList<Integer> pants = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " DESC";

        String[] projection = {
                DbContract.ClothesUnlocks.COLUMN_PANTS
        };

        Cursor cursor = db.query(
                DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()){
            pants.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_PANTS)));
        }

        return pants;
    }

    public ArrayList<Integer> readShoes() {
        ArrayList<Integer> shoes = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " DESC";

        String[] projection = {
                DbContract.ClothesUnlocks.COLUMN_SHOES
        };

        Cursor cursor = db.query(
                DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()){
            shoes.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_SHOES)));
        }

        return shoes;
    }

}
