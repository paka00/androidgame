package com.example.kaisa.androidproject.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
        userValues.put(DbContract.User.COLUMN_WALK_TIME, addable.walkTime);
        userValues.put(DbContract.User.COLUMN_WALK_DISTANCE, addable.walkDistance);
        userValues.put(DbContract.User.COLUMN_DAILY_REWARD, addable.dailyReward);
        userValues.put(DbContract.User.COLUMN_DAILY_STEP_GOAL, addable.dailyStepGoal);

        try {
            long newRowId = db.insert(DbContract.User.TABLE_NAME_USER, null, userValues);
            //long newRowId2 = db.insert(DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES, null, clothesValues);
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
                DbContract.User.COLUMN_WALK_DISTANCE,
                DbContract.User.COLUMN_DAILY_REWARD,
                DbContract.User.COLUMN_DAILY_STEP_GOAL
        };

        Cursor cursor = db.query(
                DbContract.User.TABLE_NAME_USER,
                projection,
                null,
                null,
                null,
                null,
                null
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
            int dailyReward = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_REWARD));
            int dailyStepGoal = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.User.COLUMN_DAILY_STEP_GOAL));
            user = new User(name, gender, hat, shirt, pants, shoes, level, totalSteps, dailySteps, stepHelper, dailyStepHelper, totalDistance, dailyDistance, averageSpeed, walkDate, walkTime, walkDistance, dailyReward, dailyStepGoal);
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
        int dailyReward = user.getDailyReward();
        int dailyStepGoal = user.getDailyStepGoal();
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
        values.put(DbContract.User.COLUMN_DAILY_REWARD, dailyReward);
        values.put(DbContract.User.COLUMN_DAILY_STEP_GOAL, dailyStepGoal);

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
        values.put(DbContract.User.COLUMN_DAILY_REWARD, 0);
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

    public void addHat (int hatNumber, int gender) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (gender == 0) {
            values.put(DbContract.ClothesUnlocks.COLUMN_HATS_M, hatNumber);
        }
        else {
            values.put(DbContract.ClothesUnlocks.COLUMN_HATS_F, hatNumber);
        }
        ArrayList<Integer> hats = readHats(gender);
        int size = hats.size() + 1;
        Log.v("clothesdb", "hat array size" + size);
        long rows = getRowsCount();
        if (size > rows) {
            if (gender == 0) {
                values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_M, 0);
            } else {
                values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_F, 0);
            }
            values.put(DbContract.ClothesUnlocks._ID, size);
            db.insert(DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES, null, values);
        }
        else {
            String selection = DbContract.ClothesUnlocks._ID + " = " + size;
            int count = db.update(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    values,
                    selection,
                    null);
        }
    }

    public void addShirt (int shirtNumber, int gender) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ArrayList<Integer> shirts = readShirts(gender);
        int size = shirts.size() + 1;
        Log.v("clothesdb", "shirt array size" + size);
        long rows = getRowsCount();
        ContentValues values = new ContentValues();
        if (gender == 0) {
            values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_M, shirtNumber);
        }
        else {
            values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_F, shirtNumber);
        }
        if (size > rows) {
            if (gender == 0) {
                values.put(DbContract.ClothesUnlocks.COLUMN_HATS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_M, 0);
            } else {
                values.put(DbContract.ClothesUnlocks.COLUMN_HATS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_F, 0);
            }
            values.put(DbContract.ClothesUnlocks._ID, size);
            db.insert(DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES, null, values);
        }
        else {
            String selection = DbContract.ClothesUnlocks._ID + " = " + size;
            int count = db.update(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    values,
                    selection,
                    null);
        }
    }

    public void addPants (int pantsNumber, int gender) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ArrayList<Integer> pants = readPants(gender);
        int size = pants.size() + 1;
        long rows = getRowsCount();
        ContentValues values = new ContentValues();
        if (gender == 0) {
            values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_M, pantsNumber);
        } else {
            values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_F, pantsNumber);
        }
        if (size > rows) {
            values.put(DbContract.ClothesUnlocks._ID, size);
            if (gender == 0) {
                values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_HATS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_M, 0);
            } else {
                values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_HATS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_F, 0);
            }
            db.insert(DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES, null, values);
        }
        else {
            String selection = DbContract.ClothesUnlocks._ID + " = " + size;
            int count = db.update(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    values,
                    selection,
                    null);
        }
    }

    public void addShoes (int shoesNumber, int gender) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ArrayList<Integer> shoes = readShoes(gender);
        int size = shoes.size() + 1;
        long rows = getRowsCount();
        ContentValues values = new ContentValues();
        if (gender == 0) {
            values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_M, shoesNumber);
        } else {
            values.put(DbContract.ClothesUnlocks.COLUMN_SHOES_F, shoesNumber);
        }
        if (size > rows) {
            values.put(DbContract.ClothesUnlocks._ID, size);
            if (gender == 0) {
                values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_M, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_HATS_M, 0);
            } else {
                values.put(DbContract.ClothesUnlocks.COLUMN_SHIRTS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_PANTS_F, 0);
                values.put(DbContract.ClothesUnlocks.COLUMN_HATS_F, 0);
            }
            db.insert(DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES, null, values);
        }
        else {
            String selection = DbContract.ClothesUnlocks._ID + " = " + size;
            int count = db.update(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    values,
                    selection,
                    null);
        }
    }

    public ArrayList<Integer> readHats(int gender) {
        ArrayList<Integer> hats = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " ASC";
        Cursor cursor = null;

        if (gender == 0) {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_HATS_M
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_HATS_M + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            while(cursor.moveToNext()){
                hats.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_HATS_M)));
            }
        }
        else {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_HATS_F
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_HATS_F + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            while(cursor.moveToNext()){
                hats.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_HATS_F)));
            }
        }
        cursor.close();

        return hats;
    }

    public ArrayList<Integer> readShirts(int gender) {
        ArrayList<Integer> shirts = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " ASC";
        Cursor cursor = null;

        if (gender == 0) {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_SHIRTS_M
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_SHIRTS_M + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            while (cursor.moveToNext()) {
                shirts.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_SHIRTS_M)));
            }
        } else {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_SHIRTS_F
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_SHIRTS_F + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            while (cursor.moveToNext()) {
                shirts.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_SHIRTS_F)));
            }
        }
        cursor.close();

        return shirts;
    }

    public ArrayList<Integer> readPants(int gender) {
        ArrayList<Integer> pants = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " ASC";
        Cursor cursor = null;

        if (gender == 0) {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_PANTS_M
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_PANTS_M + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            while(cursor.moveToNext()){
                pants.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_PANTS_M)));
            }
        } else {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_PANTS_F
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_PANTS_F + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            while(cursor.moveToNext()){
                pants.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_PANTS_F)));
            }
        }
        cursor.close();

        return pants;
    }

    public ArrayList<Integer> readShoes(int gender) {
        ArrayList<Integer> shoes = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder = DbContract.User._ID + " ASC";
        Cursor cursor = null;

        if (gender == 0) {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_SHOES_M
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_SHOES_M + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            while(cursor.moveToNext()){
                shoes.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_SHOES_M)));
            }
        } else {
            String[] projection = {
                    DbContract.ClothesUnlocks.COLUMN_SHOES_F
            };

            String selection = DbContract.ClothesUnlocks.COLUMN_SHOES_F + " != ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            while(cursor.moveToNext()){
                shoes.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ClothesUnlocks.COLUMN_SHOES_F)));
            }
        }
        cursor.close();
        return shoes;
    }

    public long getRowsCount() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES);
        return count;
    }

    public void clearDatabase() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String clearDBQuery = "DROP TABLE IF EXISTS " + DbContract.User.TABLE_NAME_USER;
        String clearDBQuery2 = "DROP TABLE IF EXISTS " + DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES;
        db.execSQL(clearDBQuery);
        db.execSQL(clearDBQuery2);
    }

}
