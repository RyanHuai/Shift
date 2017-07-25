package com.mainframevampire.shift.ui;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.data.model.ShiftDetail;
import com.mainframevampire.shift.database.ShiftsDataSource;

import java.util.ArrayList;


public class LoadTableService extends IntentService {


    public LoadTableService() {
        super("LoadTableService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("LoadTableService:", "in");
        String tableName = intent.getStringExtra(MainActivity.TABLE_NAME);
        if (tableName.equals("SHIFTS")) {
            ShiftDetail shiftDetailFromWeb = intent.getParcelableExtra(MainActivity.SHIFT_DETAIL);
            ShiftsDataSource dataSource = new ShiftsDataSource(this);
            ShiftDetail shiftDetailInTableById = dataSource.readShiftTableByID(shiftDetailFromWeb.getId());
            if (shiftDetailInTableById == null) {
                dataSource.createShiftContents(shiftDetailFromWeb);
            } else if (shiftDetailInTableById.getEnd().equals("")) {
                dataSource.updateShiftsTable(shiftDetailFromWeb);
            }

        }
        else if (tableName.equals("BUSINESS")) {
            String name = intent.getStringExtra(MainActivity.BUSINESS_NAME);
            String logo = intent.getStringExtra(MainActivity.BUSINESS_LOGO);
            Business business = new Business(name, logo);
            ShiftsDataSource dataSource = new ShiftsDataSource(this);
            int count = dataSource.GetBusinessTableCount();
            if (count == 0) {
                ShiftsDataSource dataSourceShift = new ShiftsDataSource(this);
                dataSourceShift.createBusinessContents(business);
            }
        }

    }
}
