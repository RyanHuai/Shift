package com.mainframevampire.shift.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.data.model.ShiftDetail;

import java.sql.Blob;
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

    public void createShiftContents(ShiftDetail shiftDetail) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        ContentValues productValues = new ContentValues();
        productValues.put(ShiftsSQLiteHelper.COLUMN_ID, shiftDetail.getId());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START, shiftDetail.getStart());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END, shiftDetail.getEnd());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_LATITUDE, shiftDetail.getStartLatitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_LONGITUDE, shiftDetail.getStartLongitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_ROAD, shiftDetail.getStartAddress());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_CITY, shiftDetail.getStartCity());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_STATE, shiftDetail.getStartState());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_POSTCODE, shiftDetail.getStartPostcode());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_COUNTRY, shiftDetail.getStartCountry());
        productValues.put(ShiftsSQLiteHelper.COLUMN_START_LOCATION_STATUS, shiftDetail.getStartLocationStatus());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_LATITUDE, shiftDetail.getEndLatitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_LONGITUDE, shiftDetail.getEndLongitude());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_ROAD, shiftDetail.getEndAddress());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_CITY, shiftDetail.getEndCity());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_STATE, shiftDetail.getEndState());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_POSTCODE, shiftDetail.getEndPostcode());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_COUNTRY, shiftDetail.getEndCountry());
        productValues.put(ShiftsSQLiteHelper.COLUMN_END_LOCATION_STATUS, shiftDetail.getEndLocationStatus());
        productValues.put(ShiftsSQLiteHelper.COLUMN_IMAGE, shiftDetail.getImage());
        productValues.put(ShiftsSQLiteHelper.COLUMN_SHIFT_LAST_UPDATE_DATE, currentDate);

        database.insert(ShiftsSQLiteHelper.SHIFTS_DETAIL_TABLE, null, productValues);

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

    public void updateShiftsTable(ShiftDetail shiftDetail) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        ContentValues updateProductValue = new ContentValues();

        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START, shiftDetail.getStart());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END, shiftDetail.getEnd());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_LATITUDE, shiftDetail.getStartLatitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_LONGITUDE, shiftDetail.getStartLongitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_ROAD, shiftDetail.getStartAddress());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_CITY, shiftDetail.getStartCity());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_STATE, shiftDetail.getStartState());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_POSTCODE, shiftDetail.getStartPostcode());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_COUNTRY, shiftDetail.getStartCountry());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_START_LOCATION_STATUS, shiftDetail.getStartLocationStatus());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_LATITUDE, shiftDetail.getEndLatitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_LONGITUDE, shiftDetail.getEndLongitude());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_ROAD, shiftDetail.getEndAddress());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_CITY, shiftDetail.getEndCity());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_STATE, shiftDetail.getEndState());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_POSTCODE, shiftDetail.getEndPostcode());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_COUNTRY, shiftDetail.getEndCountry());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_END_LOCATION_STATUS, shiftDetail.getEndLocationStatus());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_IMAGE, shiftDetail.getImage());
        updateProductValue.put(ShiftsSQLiteHelper.COLUMN_SHIFT_LAST_UPDATE_DATE, currentDate);


        database.update(ShiftsSQLiteHelper.SHIFTS_DETAIL_TABLE,
                updateProductValue,
                " ID = " + "'" + shiftDetail.getId() + "'",
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

    public ArrayList<ShiftDetail> readAllShiftsTable() {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ShiftsSQLiteHelper.SHIFTS_DETAIL_TABLE +
                " ORDER BY ID DESC" , null);

        ArrayList<ShiftDetail> ShiftDetails = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ShiftDetail shiftDetail  = new ShiftDetail(
                        getIntFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_ID),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_ROAD),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_CITY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_STATE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_POSTCODE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_COUNTRY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LOCATION_STATUS),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_ROAD),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_CITY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_STATE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_POSTCODE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_COUNTRY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LOCATION_STATUS),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_IMAGE));
                ShiftDetails.add(shiftDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return ShiftDetails;
    }

    public ShiftDetail readShiftTableByID(int id) {
        SQLiteDatabase database = open();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ShiftsSQLiteHelper.SHIFTS_DETAIL_TABLE +
                        " WHERE ID =  " + id , null);

        ShiftDetail shiftDetail = null;
        if (cursor.moveToFirst()){
            do {
                shiftDetail  = new ShiftDetail(
                        getIntFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_ID),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_ROAD),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_CITY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_STATE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_POSTCODE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_COUNTRY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_START_LOCATION_STATUS),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LATITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LONGITUDE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_ROAD),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_CITY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_STATE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_POSTCODE),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_COUNTRY),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_END_LOCATION_STATUS),
                        getStringFromColumnName(cursor,ShiftsSQLiteHelper.COLUMN_IMAGE));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);

        return shiftDetail;
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

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ShiftsSQLiteHelper.SHIFTS_DETAIL_TABLE, null);

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
