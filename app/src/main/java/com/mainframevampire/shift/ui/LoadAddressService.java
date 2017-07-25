package com.mainframevampire.shift.ui;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.data.model.ShiftDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoadAddressService extends IntentService {

    private static final String TAG = LoadAddressService.class.getSimpleName();

    public LoadAddressService() {
        super("LoadAddressService");

    }
    @Override
    protected void onHandleIntent(Intent intent) {
        ShiftDetail shiftDetail = intent.getParcelableExtra(MainActivity.SHIFT_DETAIL);
        String messageSource = intent.getStringExtra(MainActivity.MESSAGE_SOURCE);

        Log.d(TAG, shiftDetail.getId() + "");
        Log.d(TAG, messageSource);

        //get start address detail
        if (isNetworkAvailable()) {
            Double dbStartLatitude = Double.parseDouble(shiftDetail.getStartLatitude());
            Double dbStartLongitude = Double.parseDouble(shiftDetail.getStartLongitude());
            if (isValidLatLng(dbStartLatitude, dbStartLongitude)) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(dbStartLatitude, dbStartLongitude, 1);
                    if (addresses.size() > 0) {
                        shiftDetail.setStartAddress( addresses.get(0).getAddressLine(0));
                        shiftDetail.setStartCity(addresses.get(0).getLocality());
                        shiftDetail.setStartState(addresses.get(0).getAdminArea());
                        shiftDetail.setStartCountry(addresses.get(0).getCountryName());
                        shiftDetail.setStartPostcode(addresses.get(0).getPostalCode());
                        shiftDetail.setStartLocationStatus("0");
                    } else {
                        shiftDetail.setStartLocationStatus("1");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    shiftDetail.setStartLocationStatus("1");
                }
            } else {
                shiftDetail.setStartLocationStatus("2");
            }
        } else {
            shiftDetail.setStartLocationStatus("3");
        }

        //get end address detail
        if (isNetworkAvailable()) {
            Double dbEndLatitude = Double.parseDouble(shiftDetail.getEndLatitude());
            Double dbEndLongitude = Double.parseDouble(shiftDetail.getEndLongitude());
            if (isValidLatLng(dbEndLatitude, dbEndLongitude)) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(dbEndLatitude, dbEndLongitude, 1);
                    if (addresses.size() > 0) {
                        shiftDetail.setEndAddress( addresses.get(0).getAddressLine(0));
                        shiftDetail.setEndCity(addresses.get(0).getLocality());
                        shiftDetail.setEndState(addresses.get(0).getAdminArea());
                        shiftDetail.setEndCountry(addresses.get(0).getCountryName());
                        shiftDetail.setEndPostcode(addresses.get(0).getPostalCode());
                        shiftDetail.setEndLocationStatus("0");
                    } else {
                        shiftDetail.setEndLocationStatus("1");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    shiftDetail.setEndLocationStatus("1");
                }
            } else {
                shiftDetail.setEndLocationStatus("2");
            }
        } else {
            shiftDetail.setEndLocationStatus("3");
        }

        //set message to UI thread and update UI
        //send download result to activity
        Intent messageIntent = new Intent(MainActivity.BROADCAST_ACTION);
        messageIntent.putExtra(MainActivity.KEY_MESSAGE, shiftDetail);
        messageIntent.putExtra(MainActivity.MESSAGE_SOURCE, messageSource);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private boolean isValidLatLng(double lat, double lng){
        if(lat < -90 || lat > 90)
        {
            return false;
        }
        else if(lng < -180 || lng > 180)
        {
            return false;
        }
        return true;
    }



}
