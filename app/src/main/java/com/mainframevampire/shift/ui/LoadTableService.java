package com.mainframevampire.shift.ui;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.Shift;
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
            ArrayList<Shift> shifts = intent.getParcelableArrayListExtra(MainActivity.SHIFTS);
            for (Shift shiftFromWeb : shifts) {
                ShiftsDataSource dataSource = new ShiftsDataSource(this);
                Shift shiftInTable = dataSource.readShiftTableByID(shiftFromWeb.getId());
                if (shiftInTable == null) {
                    dataSource.createShiftContents(shiftFromWeb);
                } else if (shiftInTable.getEnd().equals("")) {
                    dataSource.updateShiftsTable(shiftFromWeb);
                }
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
