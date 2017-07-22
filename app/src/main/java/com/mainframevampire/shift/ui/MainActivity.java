package com.mainframevampire.shift.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mainframevampire.shift.Adapter;
import com.mainframevampire.shift.R;
import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.InputShift;
import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.data.remote.APIService;
import com.mainframevampire.shift.data.remote.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private APIService mAPIService;

    //view variable
    private TextView mOfflineModeLabel;
    private ImageView mBusinessImageView;
    private TextView mBusinessText;

    private LinearLayout mCurrentShiftLayout;
    private ImageView mCurrentImageView;
    private TextView mCurrentStartDateAndTime;
    private TextView mCurrentStartStreet;
    private TextView mCurrentStartState;
    private TextView mCurrentStartCountry;
    private Button mEndButton;

    private RecyclerView mRecyclerView;
    private List<Shift> mShifts;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOfflineModeLabel = (TextView) findViewById(R.id.offlineModeLabel);
        mBusinessImageView = (ImageView) findViewById(R.id.businessImage);
        mBusinessText = (TextView) findViewById(R.id.businessText);

        mCurrentShiftLayout = (LinearLayout) findViewById(R.id.currentShiftLayout);
        mCurrentImageView = (ImageView) findViewById(R.id.currentImageView);
        mCurrentStartDateAndTime = (TextView) findViewById(R.id.currentStartDateAndTime);
        mCurrentStartStreet = (TextView) findViewById(R.id.currentStartStreet);
        mCurrentStartState = (TextView) findViewById(R.id.currentStartState);
        mCurrentStartCountry = (TextView) findViewById(R.id.currentStartCountry);
        mEndButton = (Button) findViewById(R.id.endButton);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        if (!isNetworkAvailable()) {
            mOfflineModeLabel.setVisibility(View.VISIBLE);
            //TODO Load from local database
        } else {
            mOfflineModeLabel.setVisibility(View.GONE);
            mAPIService = ApiUtils.getAPIService();
            //load from Deputy web
            loadBusinessFromWeb();
            loadShiftsFromWeb();
        }

        //mAPIService = ApiUtils.getAPIService();

//        loadBusinessFromWeb();
//        loadShiftsFromWeb();
//        InputShift inputShift = new InputShift(
//                "2017-01-17T06:35:57+00:00",
//                "-23.832270",
//                "5555.084356"
//        );
//        startShift(inputShift);
//
//        InputShift inputEndShift = new InputShift(
//                "2017-01-24T06:35:57+00:00",
//                "-23.832270",
//                "5555.084356"
//        );
//        endShift(inputEndShift);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {
            mOfflineModeLabel.setVisibility(View.VISIBLE);
        } else {
            mOfflineModeLabel.setVisibility(View.GONE);
        }
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

    private void loadBusinessFromWeb() {
        mAPIService.saveBusiness().enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                if(response.isSuccessful()) {
                    Business business = response.body();
                    mBusinessText.setText(business.getName());
                    Picasso.with(MainActivity.this).load(business.getLogo()).into(mBusinessImageView);
                }
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveBusiness to API.");
            }
        });
    }

    private void loadShiftsFromWeb() {
        mAPIService.saveShifts().enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(Call<List<Shift>> call, Response<List<Shift>> response) {
                if(response.isSuccessful()) {
                    mShifts = response.body();
                    Collections.sort(mShifts);
                    Log.d("end", mShifts.get(0).getEnd());
                    if(mShifts.get(0).getEnd().equals("")) {
                        //shift in progress
                        mShifts.remove(0);
                        mCurrentShiftLayout.setVisibility(View.VISIBLE);
                        Picasso.with(MainActivity.this).load(mShifts.get(0).getImage()).into(mCurrentImageView);
                        String startDate = mShifts.get(0).getStart().substring(0, 10);
                        String startTime = mShifts.get(0).getStart().substring(11, 19);
                        mCurrentStartDateAndTime.setText(startDate + " " + startTime);
                    } else {
                        mCurrentShiftLayout.setVisibility(View.GONE);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mAdapter = new Adapter(MainActivity.this, mShifts);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Shift>> call, Throwable t) {
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
