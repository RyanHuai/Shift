package com.mainframevampire.shift.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mainframevampire.shift.R;
import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.InputShift;
import com.mainframevampire.shift.data.model.OutputShift;
import com.mainframevampire.shift.data.remote.APIService;
import com.mainframevampire.shift.data.remote.ApiUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAPIService = ApiUtils.getAPIService();
        loadBusiness();
        loadShifts();
        InputShift inputShift = new InputShift(
                "2017-01-17T06:35:57+00:00",
                "-23.832270",
                "5555.084356"
        );
        startShift(inputShift);

        InputShift inputEndShift = new InputShift(
                "2017-01-24T06:35:57+00:00",
                "-23.832270",
                "5555.084356"
        );
        endShift(inputEndShift);
    }


    private void loadBusiness() {
        mAPIService.saveBusiness().enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                if(response.isSuccessful()) {
                    Business business = response.body();
                    Log.i(TAG, "business.name:" + business.getName());
                    Log.i(TAG, "business.lego:" + business.getLogo());
                }
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveBusiness to API.");
            }
        });
    }

    private void loadShifts() {
        mAPIService.saveShifts().enqueue(new Callback<List<OutputShift>>() {
            @Override
            public void onResponse(Call<List<OutputShift>> call, Response<List<OutputShift>> response) {
                if(response.isSuccessful()) {
                    List<OutputShift> outputShifts = response.body();
                    Log.i(TAG, "Shift1.id:" + outputShifts.get(0).getId());
                    Log.i(TAG, "Shift1.start:" + outputShifts.get(0).getStart());
                    Log.i(TAG, "Shift1.end:" + outputShifts.get(0).getEnd());
                    Log.i(TAG, "Shift1.startLatitude:" + outputShifts.get(0).getStartLatitude());
                    Log.i(TAG, "Shift1.startLongitude:" + outputShifts.get(0).getStartLongitude());
                    Log.i(TAG, "Shift1.endLatitude:" + outputShifts.get(0).getEndLatitude());
                    Log.i(TAG, "Shift1.endLongitude:" + outputShifts.get(0).getEndLongitude());
                    Log.i(TAG, "Shift1.image:" + outputShifts.get(0).getImage());
                    Log.i(TAG, "Shift2.id:" + outputShifts.get(1).getId());
                    Log.i(TAG, "Shift2.start:" + outputShifts.get(1).getStart());
                    Log.i(TAG, "Shift2.end:" + outputShifts.get(1).getEnd());
                    Log.i(TAG, "Shift2.startLatitude:" + outputShifts.get(1).getStartLatitude());
                    Log.i(TAG, "Shift2.startLongitude:" + outputShifts.get(1).getStartLongitude());
                    Log.i(TAG, "Shift2.endLatitude:" + outputShifts.get(1).getEndLatitude());
                    Log.i(TAG, "Shift2.endLongitude:" + outputShifts.get(1).getEndLongitude());
                    Log.i(TAG, "Shift2.image:" + outputShifts.get(1).getImage());
                }
            }

            @Override
            public void onFailure(Call<List<OutputShift>> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveShifts to API.");
            }
        });
    }

    private void startShift(InputShift inputShift) {
        mAPIService.saveStartShift(inputShift).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, "saveStartShift:" + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveStartShift to API.");
            }
        });
    }

    private void endShift(InputShift inputEndShift) {
        mAPIService.saveEndShift(inputEndShift).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, "saveStartShift:" + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveStartShift to API.");
            }
        });
    }




}
