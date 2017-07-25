package com.mainframevampire.shift.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.Shift;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShiftsDataSource {
    private Context mContext;
    private ShiftsSQLiteHelper mShiftsSQLiteHelper;

    public ShiftsDataSource(Context context) {
        mContext = context;
        mShiftsSQLiteHelper = new ShiftsSQLiteHelper(context);
    }

    private SQLiteDatabase open() {
        return mShiftsSQLiteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void createBusinessContents(Business business) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        ContentValues productValues = new ContentValues();
        productValues.put(ShiftsSQLiteHelper.COLUMN_NAME, business.getName());
        productValues.put(ShiftsSQLiteHelper.COLUMN_LOGO, business.getLogo());
        productValues.put(ShiftsSQLiteHelper.COLUMN_BUSINESS_LAST_UPDATE_DATE, currentDate);

        database.insert(ShiftsSQLiteHelper.BUSINESS_TABLE, null, productValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void createShiftContents(Shift shift) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        ContentValues productValues = new ContentValues();
        productValues.put(ShiftsSQLiteHelper.COLUMN_ID, shift.getId());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START, shift.getStart());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END, shift.getEnd());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_LATITUDE, shift.getStartLatitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_LONGITUDE, shift.getStartLongitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_LATITUDE, shift.getEndLatitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_LONGITUDE, shift.getEndLongitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_IMAGE, shift.getImage());
        productValues.put(ShiftsSQLiteHelper.COLUMN_SHIFT_LAST_UPDATE_DATE, currentDate);

        database.insert(ShiftsSQLiteHelper.SHIFTS_TABLE, null, productValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void updateBusinessTable(Business business) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues updateProductValue = new ContentValues();
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_NAME, business.getName());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_LOGO, business.getLogo());


        database.update(ShiftsSQLiteHelper.BUSINESS_TABLE,
                updateProductValue,
                " NAME = " + "'" + business.getName() + "'",
                null);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void updateShiftsTable(Shift shift) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        ContentValues updateProductValue = new ContentValues();
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START, shift.getStart());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END, shift.getEnd());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_LATITUDE, shift.getStartLatitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_LONGITUDE, shift.getStartLongitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_LATITUDE, shift.getEndLatitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_LONGITUDE, shift.getEndLongitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_IMAGE, shift.getImage());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_SHIFT_LAST_UPDATE_DATE, currentDate);


        database.update(ShiftsSQLiteHelper.SHIFTS_TABLE,
                updateProductValue,
                " ID = " + "'" + shift.getId() + "'",
                null);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public Business readBusinessTable() {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ShiftsSQLiteHelper.BUSINESS_TABLE, null);

       Business business = null;
        if (cursor.moveToFirst()){
            do {
                business = new Business(
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_NAME),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_LOGO));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return business;
    }

    public ArrayList<Shift> readAllShiftsTable() {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ShiftsSQLiteHelper.SHIFTS_TABLE +
                " ORDER BY ID DESC" , null);

        ArrayList<Shift> shifts = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Shift shift  = new Shift(
                        getIntFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_ID),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_IMAGE));
                shifts.add(shift);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return shifts;
    }

    public Shift readShiftTableByID(int id) {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ShiftsSQLiteHelper.SHIFTS_TABLE +
                        " WHERE ID =  " + id , null);

        Shift shift = null;
        if (cursor.moveToFirst()){
            do {
                shift  = new Shift(
                        getIntFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_ID),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_IMAGE));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return shift;
    }

    public int GetBusinessTableCount() {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ShiftsSQLiteHelper.BUSINESS_TABLE, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        close(database);

        return count;
    }

    public int GetShiftsTableCount() {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ShiftsSQLiteHelper.SHIFTS_TABLE, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        close(database);

        return count;
    }

    private String getStringFromColumnName(Cursor cursor, String ColumnName) {
        int columnIndex = cursor.getColumnIndex(ColumnName);
        return cursor.getString(columnIndex);
    }

    private int getIntFromColumnName(Cursor cursor, String ColumnName) {
        int columnIndex = cursor.getColumnIndex(ColumnName);
        return cursor.getInt(columnIndex);
    }



}
