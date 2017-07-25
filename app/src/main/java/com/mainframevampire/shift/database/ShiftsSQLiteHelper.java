package com.mainframevampire.shift.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShiftsSQLiteHelper extends SQLiteOpenHelper  {
    private static final String DB_NAME = "shift.db";
    private static final int DB_VERSION = 1;
    //Business Table functionality
    public static final String BUSINESS_TABLE = "BUSINESS";
    public static final String COLUMN_NAME ="NAME";
    public static final String COLUMN_LOGO ="LOGO";
    public static final String COLUMN_BUSINESS_LAST_UPDATE_DATE = "BUSINESS_LAST_UPDATE_DATE";
    public static final String CREATE_TABLE_BUSINESS =
            "CREATE TABLE " + BUSINESS_TABLE + "("
                    + COLUMN_NAME + " TEXT," +
                    COLUMN_LOGO + " TEXT," +
                    COLUMN_BUSINESS_LAST_UPDATE_DATE + " TEXT)";

    //Shifts Table functionality
    public static final String SHIFTS_TABLE = "SHIFTS";
    public static final String COLUMN_ID ="ID";
    public static final String COLUMN_START ="START";
    public static final String COLUMN_END ="END";
    public static final String COLUMN_START_LATITUDE ="START_LATITUDE";
    public static final String COLUMN_START_LONGITUDE ="START_LONGITUDE";
    public static final String COLUMN_END_LATITUDE ="END_LATITUDE";
    public static final String COLUMN_END_LONGITUDE ="END_LONGITUDE";
    public static final String COLUMN_IMAGE ="IMAGE";
    public static final String COLUMN_SHIFT_LAST_UPDATE_DATE = "SHIFT_LAST_UPDATE_DATE";
    public static final String CREATE_TABLE_SHIFTS =
            "CREATE TABLE " + SHIFTS_TABLE + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_START + " TEXT," +
                    COLUMN_END + " TEXT," +
                    COLUMN_START_LATITUDE + " TEXT," +
                    COLUMN_START_LONGITUDE + " TEXT," +
                    COLUMN_END_LATITUDE + " TEXT," +
                    COLUMN_END_LONGITUDE + " TEXT," +
                    COLUMN_IMAGE + " TEXT," +
                    COLUMN_SHIFT_LAST_UPDATE_DATE + " TEXT)";

    public ShiftsSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BUSINESS);
        db.execSQL(CREATE_TABLE_SHIFTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
