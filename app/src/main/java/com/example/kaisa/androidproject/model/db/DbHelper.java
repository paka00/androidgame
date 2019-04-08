package com.example.kaisa.androidproject.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DbContract.User.TABLE_NAME_USER + " (" +
                    DbContract.User._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    DbContract.User.COLUMN_USERNAME + " STRING," +
                    DbContract.User.COLUMN_GENDER + " INTEGER," +
                    DbContract.User.COLUMN_HAT + " INTEGER," +
                    DbContract.User.COLUMN_SHIRT + " INTEGER," +
                    DbContract.User.COLUMN_PANTS + " INTEGER," +
                    DbContract.User.COLUMN_SHOES + " INTEGER," +
                    DbContract.User.COLUMN_LEVEL + " INTEGER," +
                    DbContract.User.COLUMN_DAILY_REWARD + " INTEGER," +
                    DbContract.User.COLUMN_TOTAL_STEPS + " INTEGER," +
                    DbContract.User.COLUMN_DAILY_STEPS + " INTEGER," +
                    DbContract.User.COLUMN_STEP_COUNTER_HELPER + " INTEGER," +
                    DbContract.User.COLUMN_DAILY_STEP_COUNTER_HELPER + " INTEGER," +
                    DbContract.User.COLUMN_TOTAL_DISTANCE + " DOUBLE," +
                    DbContract.User.COLUMN_DAILY_DISTANCE + " DOUBLE," +
                    DbContract.User.COLUMN_AVERAGE_SPEED + " DOUBLE," +
                    DbContract.User.COLUMN_WALK_DATE + " STRING," +
                    DbContract.User.COLUMN_WALK_TIME + " STRING," +
                    DbContract.User.COLUMN_WALK_DISTANCE + " DOUBLE)";

    private static final String SQL_CREATE_CLOTHES_TABLE =
            "CREATE TABLE " + DbContract.ClothesUnlocks.TABLE_NAME_CLOTHES + " (" +
                    DbContract.ClothesUnlocks._ID + " INTEGER PRIMARY KEY NOT NULL," +
                    DbContract.ClothesUnlocks.COLUMN_HATS + " INTEGER," +
                    DbContract.ClothesUnlocks.COLUMN_SHIRTS + " INTEGER," +
                    DbContract.ClothesUnlocks.COLUMN_PANTS + " INTEGER," +
                    DbContract.ClothesUnlocks.COLUMN_SHOES + " INTEGER);";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DbContract.User.TABLE_NAME_USER;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserStats.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_CLOTHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
